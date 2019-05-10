package models

trait ModelCompanion[M <: Model[M]] {
  protected def dbTable: DatabaseTable[M]

  private[models] def apply(jsonValue: JValue): M

  private def instances: Iterable[M] = dbTable.instances.values

  def all: List[M] = instances.toList

  def find(id: Int): Option[M] = dbTable.instances.get(id)

  def exists(attr: String, value: Any): Boolean = 
    instances.exists(x => x.toMap.get(attr) == Some(value))

  def delete(id: Int): Unit = dbTable.delete(id)

  def filter(mapOfAttributes: Map[String, Any]): List[M] = {
    all.filter(
      obj => mapOfAttributes.forall(
        attr => obj.toMap.get(attr._1) == Some(attr._2)
      )
    )
  }

  def findByAttribute(attr: String, value: Any): M = {
    return filter(Map(attr -> value)).head
  }

}

trait Model[M <: Model[M]] { self: M =>
  protected var _id: Int = 0

  def id: Int = _id

  protected def dbTable: DatabaseTable[M]

  def toMap: Map[String, Any] = Map("id" -> _id)

  def save(): Unit = {
    if (_id == 0) { _id = dbTable.getNextId }
    dbTable.save(this)
  }
}
