package graph.persistence
import graph.Graph
import scala.collection.immutable.HashMap
import graph.Node
import java.io.File

/**
  * A trait that provides the functionality to store an XML representation of an object
  * 
  * If a class mixes in this trait, it still needs to define how the class is transformed into XML (override toXML())
  * And how the class is constructed from XML (override load())
  */
trait XMLPersistence extends Graph with Persistence with XML{
  
  
  /*abstract methods
   *These methods need to be implemented in the classes which use this trait
   */
  /**
    * Loads an object
    * @return An object that is constructed from an XML file. This is an Option because there is a possibility that the load can fail
    */
  override def load():Option[XMLPersistence]
  /**
    * Creates an XML representation of the object
    * @return The XML representation of the object
    */
  override def toXML():scala.xml.Node
  /**
    * Loads a file from the path
    * @return an XML Node containing the XML in the path
    */
  def loadFile():scala.xml.Node = scala.xml.XML.loadFile(path)
  
  /**
    * Saves the XML representation of the object at the path-field
    */
  def save() {
    try{
    val f:File = new File(path)
  
	  if(f.exists()) {
	    if(f.delete())    println("deleted old xml")
	    else println("could not delete old xml!")
	  }
    scala.xml.XML.save(path,toXML)
    }catch{
      case e:Exception=>println("could not save xml")
    }
  }
}

