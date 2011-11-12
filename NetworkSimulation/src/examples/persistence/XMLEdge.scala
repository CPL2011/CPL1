package examples.persistence

import graph.persistence.XML
import graph.Edge

class XMLEdge(source: graph.Node, destination: graph.Node) extends Edge(source,destination) with XML {
 

  override def toXML(): scala.xml.Node = <Edge>
        <source>{source.label.toString()}</source>
        <destination>{destination.label.toString()}</destination>
      </Edge>

}