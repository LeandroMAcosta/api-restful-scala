package models

object User extends ModelCompanion[User] {
  protected def dbTable: DatabaseTable[User] = Database.users

    def apply(username: String, location: String): User =
      new User(username, location)

  private[models] def apply(jsonValue: JValue): User = {
    val value = jsonValue.extract[User]
    value._id = (jsonValue \ "id").extract[Int]
    value.save()
    value
  }
 }

class User(val username: String, val location: String) extends Model[User] {
  protected def dbTable: DatabaseTable[User] = User.dbTable

  override def toMap: Map[String, Any] = 
    super.toMap + ("username" -> username, "location" -> location)

  override def toString: String = s"User: $username"
}
