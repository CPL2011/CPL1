package examples.persistence

import graph.persistence.XML
import graph.Edge
/**
 * An XMLEdge is a graph.Edge with an XML representation
 */
class XMLEdge(source: graph.Node, destination: graph.Node) extends Edge(source,destination) with XML {
 
/**
   * this function returns the XML representation of the XMLEdge
   */
  override def toXML(): scala.xml.Node = <Edge>
        <source>{source.label.toString()}</source>
        <destination>{destination.label.toString()}</destination>
      </Edge>

}