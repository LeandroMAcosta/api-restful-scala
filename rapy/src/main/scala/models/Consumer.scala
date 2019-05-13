package models

object Consumer extends ModelCompanion[Consumer] {
  protected def dbTable: DatabaseTable[Consumer] = Database.consumers

  def apply(username: String, locationId: Int, balance: Int): Consumer =
    new Consumer(username, locationId, balance)

  private[models] def apply(jsonValue: JValue): Consumer = {
    val value = jsonValue.extract[Consumer]
    value._id = (jsonValue \ "id").extract[Int]
    value.save()
    value
  }

  // Metodo implementados para mejorar la sintaxis y legibilidad del codigo.
  def validUsername(username: String): Boolean = {
    return !Provider.exists("username", username) && 
           !Consumer.exists("username", username)
  }

}

class Consumer(val username: String, val locationId: Int, var balance: Float)
               extends User with Model[Consumer] {
  
  protected def dbTable: DatabaseTable[Consumer] = Consumer.dbTable

  // Actualizacion del balance del consumidor.
  def charge(price: Float) = 
    balance = balance - price

  override def toMap: Map[String, Any] = super.toMap + 
    ("username" -> username, "locationId" -> locationId, "balance" -> balance)

  override def toString: String = s"Consumer: $username"
}
