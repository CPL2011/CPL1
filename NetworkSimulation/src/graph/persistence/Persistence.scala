package graph.persistence

abstract trait Persistence {  
  var path:String
  def load():Option[Persistence]
  def save():Unit
  def setPath(p:String):Unit = path=p
}