package models

object Provider extends ModelCompanion[User] {
  protected def dbTable: DatabaseTable[User] = Database.users

    def apply(username: String, storeName: String, locationId: Int, 
              maxDeliveryDistance: Int, typeOfUser: String): Provider =
      new Provider(username, locationId, typeOfUser, storeName, maxDeliveryDistance)

  private[models] def apply(jsonValue: JValue): Provider = {
    val value = jsonValue.extract[Provider]
    value._id = (jsonValue \ "id").extract[Int]
    value.save()
    value
  }

  override def all = User.all.filter(user => user.toMap.get("typeOfUser") == Some("provider"))

}

class Provider(username: String,
               locationId: Int, 
               typeOfUser: String,
               val storeName: String, 
               val maxDeliveryDistance: Int) extends User(username, locationId, typeOfUser) {

  override def toMap: Map[String, Any] = 
    super.toMap + ("storeName" -> storeName, 
                   "maxDeliveryDistance" -> maxDeliveryDistance)

  override def toString: String = s"Provider: $username"
}
