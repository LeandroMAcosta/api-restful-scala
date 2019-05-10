package models

case class User(val username: String, val locationId: Int, val balance: Float) {
    def toMap = Map("username" -> username, 
                         "locationId" -> locationId, 
                         "balance" -> balance)
}
