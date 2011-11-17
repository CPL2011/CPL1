package graph
import org.ubiety.ubigraph.UbigraphClient
/**
  * An edge symbolizes a connection between two Nodes
  * Default edges are directed edges
  * 
  * @param source The Node where the edge starts
  * @param destination The NOde where the edge ends  
  */
class Edge(val source: Node, val destination: Node) {
  
  /**
    * After an edge has been created, the nodes of the edge needs to be informed 
    * that there is an edge between them.
    * Only if this method is invoked will the Graph 'know' that there is an edge between these nodes
    * 
    *  @param Graph The graph to which the nodes of the edge belong.
    */
  def informNodes(graph: Graph) = {
    if (graph.hasNode(source) && graph.hasNode(destination)) { 
      source.addOriginatingEdge(this)
      destination.addArrivingEdge(this) 
    } else System.err.println("An edge between the specified nodes cannot be formed since they don't belong to the graph the edge has been assigned to")
  }
//  def visualize(ubigraphClient : UbigraphClient) = {
//    if (ubigraphClient.newEdge(cantorPairing(source.label, destination.label), source.label, destination.label) == -1) {
//      System.err.println("The edge (" + source.label + "," + destination.label + ") has already been visualized")
//    }
//  }
//  
//  def removeVisualization(ubigraphClient : UbigraphClient) = {
//    if (ubigraphClient.removeEdge(cantorPairing(source.label, destination.label)) == -1) {
//      System.err.println("The edge (" + source.label + "," + destination.label + ") has not yet been visualized")
//    }
//  }
//  def cantorPairing(x: Int, y: Int) = (x+y)*(x+y+1)/2+y
//  def cantorUnpairing(z: Int) : (Int, Int) = {
//    val w = Math.floor((Math.sqrt(8*z+1)-1)/2).toInt
//    val t = (w*w+w)/2
//    val y = z-t
//    val x = w-y
//    (x,y)
//  }


}