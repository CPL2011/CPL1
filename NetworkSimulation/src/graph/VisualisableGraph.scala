package graph

import scala.collection.mutable.ListBuffer

class VisualisableGraph extends Graph with Visual {

  private var visualizedNodes = new ListBuffer[Node]
  private var visualizedEdges = new ListBuffer[Edge]
  private var nodesToBeRemoved = new ListBuffer[Node]
  private var edgesToBeRemoved = new ListBuffer[Edge]
  
  def visualize(updateNode : Node => Unit) : Unit = { 
    visualizeNodes(updateNode)
    visualizeEdges
  }
  private def visualizeNodes(updateNode : Node => Unit) = {
    unvisualizeNodesToBeRemoved
    nodes.values.foreach(e => if (visualizedNodes.contains(e)) updateNode(e) else {
      if (ubigraphClient.newVertex(e.label) == 0) {
        visualizedNodes += e
        updateNode(e)
      } else System.err.println("Error: Node " + e.label + " has already been visualized. The internal logic should prevent this from occurring")
    })
  }
  
  private def visualizeEdges = {
    unvisualizeEdgesToBeRemoved
    var edges : List[Edge] = Nil
    nodes.values.foreach(e => edges = edges ++ e.originatingEdges.values.toList)
    edges.foreach(e => if (visualizedEdges.contains(e)) {/*want to modify? then add f as parameter*/ } else {
      if (ubigraphClient.newEdge(cantorPairing(e.source.label, e.destination.label), e.source.label, e.destination.label) == 0) {
        visualizedEdges += e
      } else System.err.println("Error: The edge (" + e.source.label + "," + e.destination.label + ") has already been visualized. The internal logic should prevent this from occurring")
    }) 
  }
  
  private def unvisualizeNodesToBeRemoved = {
    nodesToBeRemoved.foreach(node => ubigraphClient.removeVertex(node.label))
    visualizedNodes = visualizedNodes.diff(nodesToBeRemoved)
    nodesToBeRemoved.clear() 
  }
  
  private def unvisualizeEdgesToBeRemoved = {
    edgesToBeRemoved.foreach(edge => ubigraphClient.removeEdge(cantorPairing(edge.source.label, edge.destination.label)))
    visualizedEdges = visualizedEdges.diff(edgesToBeRemoved)
    edgesToBeRemoved.clear()    
  }
  
  override def removeNode(nodeID: Int) = {
    visualizedNodes.find(node => node.label == nodeID) match {
      case Some(node) => nodesToBeRemoved += node
      case None => ()
    }
    super.removeNode(nodeID)
    //ubigraphClient.removeVertex(nodeID)
    //visualizedNodes -= nodes.get(nodeID).get 
  }
  
  override def removeEdge(source: Int, destination: Int) = {
    visualizedEdges.find(edge => edge.source.label == source && edge.destination.label == destination) match {
      case Some(edge) => edgesToBeRemoved += edge
      case None => ()
    }
    super.removeEdge(source, destination)
    //ubigraphClient.removeEdge(cantorPairing(source, destination))
    //visualizedEdges -= nodes.get(source).get.getEdgeTo(destination).get
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
  
  private def cantorPairing(x: Int, y: Int) = (x+y)*(x+y+1)/2+y

}
