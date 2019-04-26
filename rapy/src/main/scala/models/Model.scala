package models

trait ModelCompanion[M <: Model[M]] {
  protected def dbTable: DatabaseTable[M]

  private[models] def apply(jsonValue: JValue): M

  def existAttr(m: M): Bool = m.toString()

  def all: List[M] = dbTable.instances.values.toList

  def find(id: Int): Option[M] = dbTable.instances.get(id)

  def exists(attr: String, value: Any): Boolean = {
    dbTable.instances.values.exists(x => x.toMap.get(attr) == Some(value)) 
  }

  def delete(id: Int): Unit = { ??? }

  def filter(mapOfAttributes: Map[String, Any]): List[M] = ??? /*dbTable.instances.filterKeys()*/
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
