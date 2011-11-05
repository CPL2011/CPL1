package graph
import org.ubiety.ubigraph.UbigraphClient

class Edge(val source: Node, val destination: Node) {
  def visualize(ubigraphClient : UbigraphClient) = ubigraphClient.newEdge(source.label, destination.label) 
  def toXML() = <Edge>
        <source>{source.label.toString()}</source>
        <destination>{destination.label.toString()}</destination>
      </Edge>
}