package app

import cask._
import models._
import scala.collection.script.Location
import scala.collection.mutable.ListBuffer
import upickle.default._

object RestfulAPIServer extends MainRoutes  {
  override def host: String = "0.0.0.0"
  override def port: Int = 4000

  @get("/")
  def root(): Response = {  
    JSONResponse("Ok")
  }

  /*
   *  Location routes
   *  - locations (GET)
   *  - locations (POST)
   */
  @get("/api/locations")
  def locations(): Response = {
    JSONResponse(Location.all.map(location => location.toMap))
  }

  @postJson("/api/locations")
  def locations(name: String, coordX: Int, coordY: Int): Response = {
    if (Location.exists("name", name)) {
      return JSONResponse("existing location name", 409)
    }

    val location = Location(name, coordX, coordY)
    location.save()
    JSONResponse(location.id)
  }

  /*
   *  User routes
   *  - deleteUser (POST)
   */
  @post("/api/users/delete/:username")
  def deleteUser(username: String): Response = {
    // Se chequea la existencia del usuario, se lo instancia y se procede 
    // a eliminarlo. En caso de no existir se envia el error correspondiente.
    if (Consumer.exists("username", username)) {
      val instanceUser = Consumer.findByAttribute("username", username)
      Consumer.delete(instanceUser.id)
      return JSONResponse("Ok")
    
    } else if (Provider.exists("username", username)) {
      val instanceUser = Provider.findByAttribute("username", username)
      Provider.delete(instanceUser.id)
      return JSONResponse("Ok")
    }

    return JSONResponse("non existing user", 404)
  }

  /*
   *  Consumers routes
   *  - providers  (GET)
   *  - providers  (POST)
   */
  
  @get("/api/consumers")
  def consumers(): Response = {
    JSONResponse(Consumer.all.map(consumer => consumer.toMap))
  }

  @postJson("/api/consumers")
  def consumers(username: String, locationName: String): Response = {
    // Chequeo de parametros validos.
    if (!Consumer.validUsername(username)) {
      return JSONResponse("existing username", 409)
    }
    if (!Location.exists("name", locationName)) {
      return JSONResponse("non existing location", 404)
    }

    // Se obtiene la instancia de la locacion para ser asociada al usuario.
    val locationInstance = Location.findByAttribute("name", locationName)

    // Crea el nuevo consumidor, se guarda y envia el mensaje correspondiente.
    val consumer = Consumer(username, locationInstance.id, 0)
    consumer.save()
    JSONResponse(consumer.id)
  }
  
  
  /*
   *  Providers routes
   *  - providers  (GET)
   *  - providers  (POST)
   */

  @get("/api/providers")
  def getProviders(locationName: String = ""): Response = {
    // De no recibir parametro, locationName mantiene su valor por defecto,
    // lo cual hace listar y enviar todos los proveedores.
    if (locationName == "") {
      return JSONResponse(Provider.all.map(provider => provider.toMap))
    }
    if (!Location.exists("name", locationName)) {
      return JSONResponse("non existing location", 404)      
    }

    // Se obtiene el id de la locacion, utilizada para filtrar a los 
    // proveedores que esten asociados a ella. Se los lista y se envian.
    val locationId = Location.findByAttribute("name", locationName).id
    
    val response = Provider.filter(
      Map("locationId" -> locationId)).map(provider => provider.toMap
    )
    JSONResponse(response)
  }

  @postJson("/api/providers")
  def providers(username: String, storeName: String, 
                locationName: String, maxDeliveryDistance: Int): Response = {
    // Chequeo de parametros validos, se envia el mensaje de error segun corresponda.
    if (!Provider.validUsername(username)) {
      return JSONResponse("existing username", 409)
    } else if (!Provider.validStoreName(storeName)) {
      return JSONResponse("existing storeName", 409)
    } else if (maxDeliveryDistance < 0) {
      return JSONResponse("negative maxDeliveryDistance", 400)
    } else if (!Location.exists("name", locationName)) {
      return JSONResponse("non existing location", 404)
    }
    
    // Se obtiene el id de la locacion para asociarla al proveedor, se crea
    // la instancia con los parametros recibidos y se guarda.
    val locationId = Location.findByAttribute("name", locationName).id
    val provider = Provider(username, storeName, 
                            locationId, 0, maxDeliveryDistance)
    provider.save()
    JSONResponse(provider.id)
  }
 
  /*
   *  Orders routes
   *  - orders        (GET)
   *  - orders        (POST)
   *  - orders/detail (GET)
   */ 
  
  @get("api/orders/detail/:id")
  def ordersDetail(id: Int): Response = {
    // Busca la orden dada por su id. En caso de existir se la instancia y se
    // envia. Caso contrario envia el error correspondiente.
    val ordersInstance = Order.find(id) match {
      case Some(s) => s
      case _ => return JSONResponse("non existing order",404)
    }

    val response = ordersInstance.detail
    JSONResponse(response)
  }

  @get("/api/orders")
  def orders(username: String): Response = {
    // Nuestra implementacion retorna las ordenes hechas por un consumidor.
    // Chequea que exista el consumidor, filtra entre todas las ordenes las
    // que fueron hechas por dicho usuario y las envia. Caso contrario envia
    // el error correspondiente.
    if (!Consumer.exists("username", username)) {
      return JSONResponse("non existing user", 404)
    }
    val response = Order.filter(
      Map("consumerUsername" -> username)).map(order => order.noItem
    )
    JSONResponse(response)
  }

  // Metodo utilizado en la siguiente implementacion para mejorar la 
  // legibilidad del codigo. Verifica que el conjunto de items dados 
  // pertenezcan al conjunto de items del proveedor.
  private def validItems(jsonItems: List[ItemJSON], 
                         itemsProvider: List[Items]): Boolean = {
    jsonItems.forall(
      item => itemsProvider.exists(
        itProvider => itProvider.name == item.name
      )
    )
  }

  @postJson("/api/orders")
  def orders(providerUsername: String, 
             consumerUsername: String, 
             items: List[ItemJSON]): Response = {
    // Chequeo de parametros validos.
    if (!Provider.exists("username", providerUsername)) {
      return JSONResponse("non existing provider", 404)
    } else if (!Consumer.exists("username", consumerUsername)) {
      return JSONResponse("non existing consumer", 404)
    }
    // Se instancian los objetos que serán utilizados para crear la orden.
    val provider = Provider.filter(Map("username" -> providerUsername)).head
    val consumer = Consumer.filter(Map("username" -> consumerUsername)).head
    val location = Location.find(consumer.locationId).get
    val jsonItems = items.toList

    val itemsProvider = Items.filter(Map("providerId" -> provider.id))

    //Se chequea que los items solicitados esten disponibles por el proveedor.
    if (!validItems(jsonItems, itemsProvider)) {
      return JSONResponse("non existing item for provider", 404)
    }

    // Se calcula el precio total de la orden.
    var orderTotal: Float = 0

    jsonItems.foreach(
      item => orderTotal += 
        itemsProvider.find(
          itemProvider => itemProvider.name == item.name
        ).get.getPrice() * item.amount
    )
   
    // Se arma una lista de pares (id, cantidad) de los items para poder 
    // detallar la orden cuando sea solicitado. 
    val returnItems = jsonItems.map(
      item => 
        Map("id" -> provider.getItem(item.name),
            "amount" -> item.amount
        ) 
    )

    // Se crea la orden, se actualizan los balances de proveedor y consumidor,
    // y se guardan. Luego se envia la respuesta correspondiente.
    val order = Order(consumer.id, consumer.username, location.name, 
                      provider.id, provider.storeName, 
                      orderTotal, "payed", returnItems) 

    order.save()

    consumer.charge(orderTotal)
    provider.pay(orderTotal)
    consumer.save()
    provider.save()
    JSONResponse(order.id)
  }

  @post("/api/orders/delete/:id")
  def orderDelete(id : Int): Response = {
    // En caso de existir la orden dada se elimina, caso contrario
    // envia el error correspondiente.
    val ordersInstance =  Order.find(id) match {
      case Some(s) => s
      case _ => return JSONResponse("non existing order", 404)
    }
    Order.delete(ordersInstance.id)
    JSONResponse("Ok")
  }
  
  @post("/api/orders/deliver/:id")
  def orderDeliver(id : Int): Response = {
    // En caso de existir la orden dada, se la marca como enviada.
    // caso contrario envia el error correspondiente.
    var order =  Order.find(id) match {
      case Some(s) => s
      case _ => return JSONResponse("non existing order", 404)
    }
    order.status = "delivered"
    order.save()

    JSONResponse("Ok")  
  }
  
  /*
   *  Items routes
   *  - items  (GET)
   *  - items  (POST)
   *  - delete (POST)
   */
  
  @get("/api/items")
  def items(providerUsername: String = ""): Response = {
    // En caso de no recibir parametro, providerUsername toma su valor por
    // defecto listando todo item. Caso contrario valida que sea un proveedor
    // valido y lista todos sus items disponibles.
    if (providerUsername == "") {
      return JSONResponse(Items.all.map(item => item.toMap))
    }
    if (!Provider.exists("username", providerUsername)){
      return JSONResponse("non existing provider", 404)
    }
    
    val provider = Provider.findByAttribute("username", providerUsername)
    val itemsList = Items.filter(Map("providerId" -> provider.id))

    JSONResponse(itemsList.map(item => item.toMap))
  }

  @postJson("/api/items")
  def items(name: String, description: String, 
            price: Float, providerUsername: String): Response = {
    // Chequeo de datos validos (existencia del proveedor, del item y precio
    // positivo) con sus correspondientes errores como respuesta.
    if (!Provider.exists("username", providerUsername)) {
      return JSONResponse("non existing provider", 404)
    }
    if (Items.exists("name", name)) {
      return JSONResponse("existing item for provider", 409)
    }
    if (price < 0) {
      return JSONResponse("negative price", 400)
    }

    // Se obtiene el id del proveedor, se crea el item y se guarda.
    val providerId = Provider.findByAttribute("username", providerUsername).id
    val item = Items(name, price, description, providerId)
    item.save()
    JSONResponse(providerId, 200)
  }

  @post("/api/items/delete/:id")
  def deleteItem(id: Int): Response = {
    // Se chequea la existencia del item con el id recibido y se procede a 
    // eliminarlo. Caso contrario se envia el error correspondiente.
    if (Items.exists("id", id)) {
      Items.delete(id)
      JSONResponse("Ok")
    } else {
      JSONResponse("non existing item", 404)
    }
  }


  override def main(args: Array[String]): Unit = {
    System.err.println("\n " + "=" * 39)
    System.err.println(s"| Server running at http://$host:$port ")

    if (args.length > 0) {
      val databaseDir = args(0)
      Database.loadDatabase(databaseDir)
      System.err.println(s"| Using database directory $databaseDir ")
    } else {
      Database.loadDatabase()  // Use default location
    }
    System.err.println(" " + "=" * 39 + "\n")

    super.main(args)
  }

  initialize()
}
