package graph
import org.ubiety.ubigraph.UbigraphClient

class Edge(source: Node, destination: Node) {
  source.addNeighbour(destination)
  def getSource():Node = source
  def getDestination():Node = destination
  def visualize(ubigraphClient : UbigraphClient) = ubigraphClient.newEdge(source.getLabel, destination.getLabel) 
  def remove = source.removeNeighbour(destination)
}