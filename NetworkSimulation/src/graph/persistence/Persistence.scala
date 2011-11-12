package graph.persistence

abstract trait Persistence {  
  var path:String
  def load():Persistence
  def save():Unit
  def setPath(p:String):Unit = path=p
}