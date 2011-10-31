package graph
import org.ubiety.ubigraph.UbigraphClient

class Node(label: Int) {
  var connectedEdges: List[Edge] = Nil
  //If you store a list of Nodes the Node has to do some kind of Query to find the Edge that connects this neigbour.
  //Shouldn't it be better to keep a list of Edges? Query's are verry cpu intensive.
  //---> Since the edges were stored in a hashmap and you'd know the labels of the source and dest node (which serve as
  //---> the key to the map, retrieval of the edge would only require constant time. Nevertheless, storing edges 
  //---> might indeed be a bit more logical. So altough it does create the awkward situation were a node 
  //---> can access itself through each edge it stores, I've made the change.
 
  def getLabel : Int = label
  def addEdge(edge : Edge) = connectedEdges = edge :: connectedEdges
  def removeNeighbour(edge: Edge) = connectedEdges diff List(edge)
  def getConnectedEdges : List[Edge] = connectedEdges
  def visualize(ubigraphClient : UbigraphClient) = ubigraphClient.newVertex(label)
  
  
}
