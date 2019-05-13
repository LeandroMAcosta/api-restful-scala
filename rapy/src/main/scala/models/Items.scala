package models

import upickle.default.{ReadWriter => RW, macroRW}
case class ItemJSON(name: String, amount: Int)
object ItemJSON {
  implicit val rw: RW[ItemJSON] = macroRW
}

object Items extends ModelCompanion[Items] {
  protected def dbTable: DatabaseTable[Items] = Database.items

  def apply(name: String, price: Float, description: String,
            providerId: Int): Items =
    new Items(name, price, description, providerId)

  private[models] def apply(jsonValue: JValue): Items = {
    val value = jsonValue.extract[Items]
    value._id = (jsonValue \ "id").extract[Int]
    value.save()
    value
  }
 }

class Items(val name: String, val price: Float, val description: String,
            val providerId: Int) extends Model[Items] {
  protected def dbTable: DatabaseTable[Items] = Items.dbTable

  override def toMap: Map[String, Any] = 
    super.toMap + ("name" -> name, 
                   "price" -> price, 
                   "description" -> description, 
                   "providerId" -> providerId)

  override def toString: String = s"Item: $name"

  def getPrice() = price
}
