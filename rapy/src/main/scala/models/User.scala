package models

object User extends ModelCompanion[User] {
  protected def dbTable: DatabaseTable[User] = Database.users

    def apply(name: String): User =
      new User(name)

  private[models] def apply(jsonValue: JValue): User = {
    val value = jsonValue.extract[User]
    value._id = (jsonValue \ "id").extract[Int]
    value.save()
    value
  }
 }

class User(val name: String) extends Model[User] {
  protected def dbTable: DatabaseTable[User] = User.dbTable

  override def toMap: Map[String, Any] = super.toMap + ("name" -> name)

  override def toString: String = s"User: $name"
}
