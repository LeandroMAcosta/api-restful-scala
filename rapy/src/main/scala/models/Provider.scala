package models

object Provider extends ModelCompanion[Provider] {
  protected def dbTable: DatabaseTable[Provider] = Database.providers

  def apply(username: String, storeName: String, locationId: Int,
            balance: Int, maxDeliveryDistance: Int): Provider = {
              
    new Provider(username, locationId,balance, storeName, maxDeliveryDistance)
  }

  private[models] def apply(jsonValue: JValue): Provider = {
    val value = jsonValue.extract[Provider]
    value._id = (jsonValue \ "id").extract[Int]
    value.save()
    value
  }

  // Metodos implementados para mejorar la sintaxis y legibilidad del codigo.
  def validUsername(username: String): Boolean = {
    return !Provider.exists("username", username) && 
           !Consumer.exists("username", username)
  }

  def validStoreName(storeName: String): Boolean = {
    !Provider.exists("storeName", storeName)
  }

}

class Provider(val username: String, val locationId: Int, var balance: Float,
               val storeName: String, val maxDeliveryDistance: Int) 
               extends User with Model[Provider] {

  protected def dbTable: DatabaseTable[Provider] = Provider.dbTable

  override def toMap: Map[String, Any] = {
    super[Model].toMap ++ super[User].toMap + (
      "storeName" -> storeName, 
      "maxDeliveryDistance" -> maxDeliveryDistance
    )
  }

  override def toString: String = s"Provider: $username"

  def getItem(name: String): Int = {
    return Items.filter(Map("name" -> name, "providerId" -> id)).head.id
  }
  
  // Actualizacion del balance del proveedor.
  def pay(price: Float) = 
    balance = balance + price
  
}
