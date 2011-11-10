package graph

import scala.collection.mutable.ListBuffer

class VisualisableGraph extends Graph with Visual {

  var visualizedNodes = new ListBuffer[Node]
  var visualizedEdges = new ListBuffer[Edge]
  
  def visualize(updateNode : Node => Unit) : Unit = { 
    nodes.values.foreach(e => if (visualizedNodes.contains(e)) updateNode(e) else {
      if (ubigraphClient.newVertex(e.label) == 0) {
        visualizedNodes += e
      } else System.err.println("Error: Node " + e.label + " has already been visualized")
    })
    var edges : List[Edge] = Nil
    nodes.values.foreach(e => edges = edges ++ e.originatingEdges.values.toList)
    edges.foreach(e => if (visualizedEdges.contains(e)) {/*want to modify? then add f as parameter*/ } else {
      if (ubigraphClient.newEdge(cantorPairing(e.source.label, e.destination.label), e.source.label, e.destination.label) == 0) {
        visualizedEdges += e
      } else System.err.println("Error: The edge (" + e.source.label + "," + e.destination.label + ") has already been visualized")
    })
    def cantorPairing(x: Int, y: Int) = (x+y)*(x+y+1)/2+y
  }
  
  // Currently this should behave exactly the same as clearing the ubigraphClient
  // However, if you would choose to pass the ubigraphClient as a parameter to the graph,
  // This will obviously allow you to display multiple graphs in a single ubigraph visualization and 
  // let you remove specific graphs from said visualization.
  def removeVisualization : Unit = {
    visualizedNodes.foreach(e => ubigraphClient.removeVertex(e.label)) //ubigraph automatically removes connected edges
    visualizedNodes.clear()
    visualizedEdges.clear()
  }
}
