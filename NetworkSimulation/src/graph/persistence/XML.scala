package graph.persistence
/**
  * An abstract trait that provides the methods to give an XML representation of the object
  */
trait XML {
  def toXML():scala.xml.Node;
}