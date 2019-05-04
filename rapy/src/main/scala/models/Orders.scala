package models

object Orders extends ModelCompanion[Orders] {
  protected def dbTable: DatabaseTable[Orders] = Database.locations

    def apply(name: String, coordX: Int, coordY: Int): Orders =
      new Orders(name, coordX, coordY)

  private[models] def apply(jsonValue: JValue): Orders = {
    val value = jsonValue.extract[Orders]
    value._id = (jsonValue \ "id").extract[Int]
    value.save()
    value
  }
 }

class Orders(val name: String, val coordX: Int, val coordY: Int) extends Model[Orders] {
  protected def dbTable: DatabaseTable[Orders] = Orders.dbTable

  override def toMap: Map[String, Any] = 
    super.toMap + ("name" -> name, "coordX" -> coordX, "coordY" -> coordY)

  override def toString: String = s"Orders: $name"
}
