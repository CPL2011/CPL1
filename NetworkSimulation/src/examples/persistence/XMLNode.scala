package examples.persistence

import graph.persistence.XML
import graph.Edge
import graph.Node
import scala.collection.immutable.HashMap
/**
 * An XML Node is a graph.Node that has an XML representation.
 * It mixes in the XML trait, and thus must override toXML()
 */
class XMLNode(label: Int) extends Node(label) with XML {
/**
 * Construct a new XMLNode from a scala.xml.Node
 */
def this(n:scala.xml.Node) = this((n\\"@label").text.toInt)
/**
 * Construct a new XMLNode from a graph.Node
 */
def this(n:Node) = {this(n.label)
  this.originatingEdges = n.originatingEdges
  this.arrivingEdges = n.arrivingEdges
 
}

  /**
   * this function returns the XML representation of the XMLNode
   */
override def toXML(): scala.xml.Node = <Node label={label.toString()}> 
  	{originatingEdges.values.map((e:Edge)=>e.toXML)}
  </Node> 
  /**
   * allows for the implicit conversion of a graph.Edge to an XMLEdge
   */
  implicit def edgeToXml(e:Edge):XMLEdge = new XMLEdge(e.source,e.destination)
   
    	

}