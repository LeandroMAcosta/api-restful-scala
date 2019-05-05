package app

import cask._
import models._
import scala.collection.script.Location

object RestfulAPIServer extends MainRoutes  {
  override def host: String = "0.0.0.0"
  override def port: Int = 4000

  @get("/")
  def root(): Response = {
    println("\n\nNo filtrado: ")
    Location.all.foreach(println)
    
    println("Filtrado: ")
    Location.filter(Map("coordX" -> 10)).foreach(println)  
    JSONResponse("asdasd", 200)
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
  @postJson("/api/users/delete/")
  def deleteUser(userId: Int): Response = {
    val instanceUser = User.find(userId)
    instanceUser match {
      case Some(user) => User.delete(userId)
      case _ => return JSONResponse("non existing user", 404)
    }
    return JSONResponse("Ok")
  }

  
  /*
   *  Providers routes
   *  - providers  (GET)
   *  - providers  (POST)
   */

  @get("/api/providers")
  def providers(locationName: String): Response = {
    val locationInstance = Location.findByAttribute("name", locationName) match {
      case Some(s) => s
      case _ => return JSONResponse("non existing location", 404)
    }
    val locationId = locationInstance.id
    JSONResponse(Provider.filter(Map("locationId" -> locationId)).map(provider => provider.toMap))
  }

  @postJson("/api/providers")
  def providers(username: String, storeName: String, 
                location: String, maxDeliveryDistance: Int): Response = {
    if (User.exists("username", username) || Provider.exists("storeName", storeName) ) {
      return JSONResponse("existing username/storeName", 409)
    } else if (maxDeliveryDistance < 0) {
      return JSONResponse("negative maxDeliveryDistance ", 400)
    }
    
    val locationInstance = Location.findByAttribute("name", location) match {
      case Some(s) => s
      case _ => return JSONResponse("non existing location", 409)
    }
    
    val locationId = locationInstance.id
    val provider = Provider(username, storeName, locationId, maxDeliveryDistance, "provider", 0)
    provider.save()
    JSONResponse(provider.id)
  }

  
  /*
   *  Consumers routes
   *  - providers  (GET)
   *  - providers  (POST)
   */
  
  @get("/api/consumers")
  def consumers(locationName: String): Response = {
    val locationInstance = Location.findByAttribute("name", locationName) match {
      case Some(s) => s
      case _ => return JSONResponse("non existing location", 409)
    }
    val locationId = locationInstance.id
    JSONResponse(Consumer.filter(Map("locationId" -> locationId)).map(consumer => consumer.toMap))
  }

  @postJson("/api/consumers")
  def consumers(username: String, location: String): Response = {
    if (User.exists("username", username)) {
      return JSONResponse("existing username", 409)
    }

    val locationInstance = Location.findByAttribute("name", location) match {
      case Some(s) => s
      case _ => return JSONResponse("not existing location", 409)
    }
    
    val locationId = locationInstance.id

    val consumer = Consumer(username, locationId, "consumer", 0)
    consumer.save()
    JSONResponse(consumer.id)
  }
  

  /*
   *  Items routes
   *  - items  (GET)
   *  - items  (POST)
   *  - delete (POST)
   */
  
  @get("/api/items")
  def items(providerUsername: String): Response = {
    if (! Provider.exists("username", providerUsername)){
      return JSONResponse("non existing provider", 404)
    }

    val provider = Provider.findByAttribute("username", providerUsername) match {
      case Some(id) => id
      case _ => return JSONResponse("non existing provider", 404)
    }

    val itemsList = Items.filter(Map("providerId" -> provider.id))
    JSONResponse(itemsList.map(item => item.toMap))
  }

  @postJson("/api/items")
  def items(name: String, description: String, price: Float, providerUsername: String): Response = {
    val providerId = Provider.findByAttribute("username", providerUsername) match {
      case Some(id) => id.id
      case _ => return JSONResponse("non existing provider", 404)
    }

    if (Items.exists("name", name)) {
      return JSONResponse("existing item for provider", 409)
    }
    val item = Items(name, price, description, providerId)
    item.save()
    JSONResponse(providerId, 200)
  }

  @postJson("/api/items/delete")
  def delete(id: Int): Response = {
    if (Items.exists("id", id)) {
      Items.delete(id)
      JSONResponse("Ok", 200)
    }
    else {
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
