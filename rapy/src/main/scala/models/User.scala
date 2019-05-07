package models

// object User extends ModelCompanion[User] {
//   // protected def dbTable: DatabaseTable[User] = Database.users

//   // def apply(username: String, locationId: Int, balance: Int): User =
//   //   new User(username, locationId, balance)

//   private[models] def apply(jsonValue: JValue): User = {
//     val value = jsonValue.extract[User]
//     value._id = (jsonValue \ "id").extract[Int]
//     value.save()
//     value
//   }
// }

// trait User(val username: String, val locationId: Int, val balance: Int) {

//   // protected def dbTable: DatabaseTable[User] = User.dbTable

//   override def toMap: Map[String, Any] = 
//   super.toMap + ("username" -> username, 
//                  "locationId" -> locationId,
//                  "balance" -> balance)

//   override def toString: String = s"User: $username"
// }


case class User(val username: String, val locationId: Int, val balance: Int)