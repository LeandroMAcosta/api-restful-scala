package models

trait User{
  val username: String 
  val locationId: Int 
  var balance: Float

  def toMap = Map("username" -> username, 
                  "locationId" -> locationId, 
                  "balance" -> balance)
}
