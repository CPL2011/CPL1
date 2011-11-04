package graph
import org.ubiety.ubigraph.UbigraphClient

class Edge(source: Node, destination: Node) {
  def getSource():Node = source
  def getDestination():Node = destination
  def visualize(ubigraphClient : UbigraphClient) = ubigraphClient.newEdge(source.getLabel, destination.getLabel) 
  def toXML() = <Edge>
        <source>{source.getLabel.toString()}</source>
        <destination>{destination.getLabel.toString()}</destination>
      </Edge>
}