package models

object Provider extends ModelCompanion[User] {
  protected def dbTable: DatabaseTable[User] = Database.users

    def apply(username: String, location: String,
              storeName: String, maxDeliveryDistance: Int): Provider =
      new Provider(username, location, storeName, maxDeliveryDistance)

  private[models] def apply(jsonValue: JValue): Provider = {
    val value = jsonValue.extract[Provider]
    value._id = (jsonValue \ "id").extract[Int]
    value.save()
    value
  }
 }

class Provider(username: String,
               location: String, 
               val storeName: String, 
               val maxDeliveryDistance: Int) extends User(username, location) {

  override def toMap: Map[String, Any] = 
    super.toMap + ("storeName" -> storeName, 
                   "maxDeliveryDistance" -> maxDeliveryDistance)

  override def toString: String = s"Provider: $username"
}
