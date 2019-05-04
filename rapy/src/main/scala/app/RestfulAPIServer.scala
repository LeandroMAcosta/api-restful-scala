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

  @get("/api/locations")
  def locations(): Response = {
    JSONResponse(Location.all.map(location => location.toMap))
  }

  @postJson("/api/locations")
  def locations(name: String, coordX: Int, coordY: Int): Response = {
    if (Location.exists("name", name)) {
      return JSONResponse("Existing location", 409)
    }

    val location = Location(name, coordX, coordY)
    location.save()
    JSONResponse(location.id)
  }

  @get("/api/providers")
  def providers(): Response = {
    JSONResponse(Provider.all.map(provider => provider.toMap))
  }

  @postJson("/api/providers")
  def providers(username: String, storeName: String, 
                location: String, maxDeliveryDistance: Int): Response = {
    if (User.exists("username", username)) {
      return JSONResponse("Existing username", 409)
    }
    
    val locationInstance = Location.findByAttribute("name", location) match {
      case Some(s) => s
      case _ => return JSONResponse("Not existing location", 409)
    }
    
    val locationId = locationInstance.getId()
    val provider = Provider(username, storeName, locationId, maxDeliveryDistance, "provider")
    provider.save()
    JSONResponse(provider.id)
  }

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
      case Some(id) => id.getId()
      case _ => return JSONResponse("non existing provider", 404)
    }

    if (Items.exists("name", name)) {
      return JSONResponse("existing item for provider", 409)
    }
    val item = Items(name, price, description, providerId)
    item.save()
    JSONResponse(providerId, 200)
  }

  // @get("/api/consumers")
  // def consumers(): Response = {
  //   JSONResponse(Consumer.all.map(consumer => consumer.toMap))
  // }

  // @postJson("/api/consumers")
  // def consumers(username: String, location: String): Response = {
  //   if (User.exists("username", username)) {
  //     return JSONResponse("Existing username", 409)
  //   }

  //   val consumer = Consumer(username, location, "consumer")
  //   consumer.save()
  //   JSONResponse(consumer.id)
  // }

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
