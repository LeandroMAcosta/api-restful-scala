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
  def providers(locationName: String): Response = {
    val locationInstance = Location.findByAttribute("name", locationName) match {
      case Some(s) => s
      case _ => return JSONResponse("Non existing location", 409)
    }
    val locationId = locationInstance.getId()
    JSONResponse(Provider.filter(Map("locationId" -> locationId)).map(provider => provider.toMap))
  }

  @postJson("/api/providers")
  def providers(username: String, storeName: String, 
                location: String, maxDeliveryDistance: Int): Response = {
    if (User.exists("username", username)) {
      return JSONResponse("Existing username", 409)
    }
    
    val locationInstance = Location.findByAttribute("name", location) match {
      case Some(s) => s
      case _ => return JSONResponse("Non existing location", 409)
    }
    
    val locationId = locationInstance.getId()
    val provider = Provider(username, storeName, locationId, maxDeliveryDistance, "provider")
    provider.save()
    JSONResponse(provider.id)
  }


  @get("/api/consumers")
  def consumers(): Response = {
    JSONResponse(Consumer.all.map(consumer => consumer.toMap))
  }

  @postJson("/api/consumers")
  def consumers(username: String, location: String): Response = {
    if (User.exists("username", username)) {
      return JSONResponse("Existing username", 409)
    }

    val locationInstance = Location.findByAttribute("name", location) match {
      case Some(s) => s
      case _ => return JSONResponse("Not existing location", 409)
    }
    
    val locationId = locationInstance.getId()

    val consumer = Consumer(username, locationId, "consumer")
    consumer.save()
    JSONResponse(consumer.id)
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
