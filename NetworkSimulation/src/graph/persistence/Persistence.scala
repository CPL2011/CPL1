package graph.persistence
/**
  * An abstract trait that provides the methods to store an object
  * Each persistence trait should inherit from this trait or one of its sub-traits
  */
abstract trait Persistence {  
  /**
    * The path to which the object is stored
    */
  var path:String
  /**
    * Load the object from the path
    */
  def load():Option[Persistence]
  /**
    * Store the object at the path
    */
  def save():Unit
  /**
    * Set the path
    */
  def setPath(p:String):Unit = path=p
}