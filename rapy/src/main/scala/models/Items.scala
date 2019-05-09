package models

object Items extends ModelCompanion[Items] {
  protected def dbTable: DatabaseTable[Items] = Database.items

  def apply(name: String, price: Float, description: String, providerId: Int): Items =
    new Items(name, price, description, providerId)

  private[models] def apply(jsonValue: JValue): Items = {
    val value = jsonValue.extract[Items]
    value._id = (jsonValue \ "id").extract[Int]
    value.save()
    value
  }
 }

class Items(name: String, price: Float, description: String, providerId: Int) extends Model[Items] {
  protected def dbTable: DatabaseTable[Items] = Items.dbTable

  override def toMap: Map[String, Any] = 
    super.toMap + ("name" -> name, "price" -> price, "description" -> description, "providerId" -> providerId)

  override def toString: String = s"Item: $name"

  def getPrice() = price
}
