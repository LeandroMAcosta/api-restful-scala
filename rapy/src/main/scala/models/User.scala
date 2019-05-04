package models

object User extends ModelCompanion[User] {
  protected def dbTable: DatabaseTable[User] = Database.users

  def apply(username: String, locationId: Int, typeOfUser: String): User =
    new User(username, locationId, typeOfUser)

  private[models] def apply(jsonValue: JValue): User = {
    val value = jsonValue.extract[User]
    value._id = (jsonValue \ "id").extract[Int]
    value.save()
    value
  }
}

class User(val username: String, val locationId: Int, val typeOfUser: String) extends Model[User] {
  protected def dbTable: DatabaseTable[User] = User.dbTable

  override def toMap: Map[String, Any] = 
    super.toMap + ("username" -> username, "locationId" -> locationId, "typeOfUser" -> typeOfUser)

  override def toString: String = s"User: $username"
}
