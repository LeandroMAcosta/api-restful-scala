// package models

// object Consumer extends ModelCompanion[User] {
//   protected def dbTable: DatabaseTable[User] = Database.consumers

//   def apply(username: String, locationId: Int, typeOfUser: String, balance: Int): Consumer =
//     new Consumer(username, locationId, typeOfUser, balance)

//   private[models] def apply(jsonValue: JValue): Consumer = {
//     val value = jsonValue.extract[Consumer]
//     value._id = (jsonValue \ "id").extract[Int]
//     value.save()
//     value
//   }

// }

// class Consumer(username: String,
//                locationId: Int,
//                typeOfUser: String,
//                balance: Int) extends User(username, locationId, typeOfUser, balance) {

//   override def toMap: Map[String, Any] = super.toMap

//   override def toString: String = s"Consumer: $username"
// }
