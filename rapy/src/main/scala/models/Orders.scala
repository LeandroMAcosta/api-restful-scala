package models

object Orders extends ModelCompanion[Orders] {
  protected def dbTable: DatabaseTable[Orders] = Database.orders

    def apply(consumerId: Int, consumerUsername: String,providerId: Int, 
              providerStoreName: String, orderTotal: Float,status: String): 
              Orders = new Orders(consumerId,consumerUsername,providerId,
                                  providerStoreName,orderTotal,status)

  private[models] def apply(jsonValue: JValue): Orders = {
    val value = jsonValue.extract[Orders]
    value._id = (jsonValue \ "id").extract[Int]
    value.save()
    value
  }
 }

class Orders(val consumerId: Int,val consumerUsername: String,
             val providerId: Int,val providerStoreName: String,val orderTotal: Float,
             val status: String) extends Model[Orders] {
  
  protected def dbTable: DatabaseTable[Orders] = Orders.dbTable

  override def toMap: Map[String, Any] = 
    super.toMap + ("consumerId"-> consumerId, "consumerUsername" -> consumerUsername,
                   "providerId" -> providerId, "providerStoreName" -> providerStoreName,
                   "orderTotal" -> orderTotal, "statues" -> status)

  override def toString: String = s"Orders: $id"
}
