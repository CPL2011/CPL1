package graph
import org.ubiety.ubigraph.UbigraphClient

class Node(label: Int) {
  var neighbours: List[Node] = Nil
  //If you store a list of Nodes the Node has to do some kind of Query to find the Edge that connects this neigbour.
  //Shouldn't it be better to keep a list of Edges? Query's are verry cpu intensive.
  
  def getLabel : Int = label
  def addNeighbour(node : Node) = neighbours = node :: neighbours
  def removeNeighbour(node : Node) = neighbours diff List(node)
  def getNeighbours : List[Node] = neighbours
  def visualize(ubigraphClient : UbigraphClient) = ubigraphClient.newVertex(label)
  
  
}
