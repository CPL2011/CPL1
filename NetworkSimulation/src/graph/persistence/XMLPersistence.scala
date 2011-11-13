package graph.persistence
import graph.Graph
import scala.collection.immutable.HashMap
import graph.Node
import java.io.File


trait XMLPersistence extends Graph with Persistence with XML{

 
  
  def loadFile()=scala.xml.XML.loadFile(path)
  /**
   * abstract methods
   */
  override def load():XMLPersistence
  override def toXML():scala.xml.Node
  
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