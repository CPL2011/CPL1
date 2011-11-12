package examples.persistence

import graph.persistence.XML
import graph.Edge
import graph.Node
import scala.collection.immutable.HashMap

class XMLNode(label: Int) extends Node(label) with XML {
def this(n:scala.xml.Node) = this((n\\"@label").text.toInt)
def this(n:Node) = {this(n.label)
  this.originatingEdges = n.originatingEdges
  this.arrivingEdges = n.arrivingEdges
 
}

  
override def toXML(): scala.xml.Node = <Node label={label.toString()}> 
  	{originatingEdges.values.map((e:Edge)=>e.toXML)}
  </Node> 
  
  implicit def edgeToXml(e:Edge):XMLEdge = new XMLEdge(e.source,e.destination)
   
    	

}