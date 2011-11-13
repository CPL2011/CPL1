package graph

import scala.collection.mutable.ListBuffer
import org.ubiety.ubigraph.UbigraphClient

/**
 * Visualizable extends the Graph class with definitions to allow visualization of it to be made.
 * The visualizations rely on an active on an active Ubigraph server.
 */
trait Visualizable extends Graph {
  
  /** variable used to store the UbiGraph client*/
   var ubigraphClient = new UbigraphClient
  /** variable used to store the nodes that are present in the visualization*/
  private var visualizedNodes = new ListBuffer[Node]
  /** variable used to store the edges that are present in the visualization*/
  private var visualizedEdges = new ListBuffer[Edge]
  /** variable used to store the nodes that should be removed on the next visualization call */
  private var nodesToBeRemoved = new ListBuffer[Node] // The same functionality could be provided by using 
		  											  // the diff operation on visualizedNodes and nodes, 
		  											  // thus making this var redundant. 
		  										      // However, there's a notable difference in performance 
		  											  // (constant time vs. O(n) in function of total nodes 
		  											  // present) ; this being my motivation for picking 
		  											  // this approach.
  /** variable used to store the edges that should be removed on the next visualization call */
  private var edgesToBeRemoved = new ListBuffer[Edge] 
  
  /**
   * Provides a visualization as specified by the underlying graph.
   * The given Traversal is used to traverse the connected nodes of the graph starting
   * from the node with the given nodeID. The function updateNode is applied to each node 
   * that gets traversed.
   * @param traverser : specifying the way nodes should be traversed
   * @param nodeID : the id of the root, the node from which the traversal should start
   * @param updateNode : a function to be applied to each traversed node 
   */
  def visualize(traverser: Traversal, nodeID: Int, updateNode: Node => Unit) : Unit = {
    visualizeNodes(traverser, nodeID, updateNode)
    visualizeEdges
  }
  
  /**
   * Provides a visualization as specified by the underlying graph.
   * The given function is applied to all nodes in the graph in a random order
   * @param updateNode : a function to be applied to each node in the graph
   */
  def visualize(updateNode : Node => Unit) : Unit = { 
    visualizeNodes(updateNode)
    visualizeEdges
  }
  
  /**
   * Provides a visualization as specified by the underlying graph.
   */
  def visualize : Unit = {
    visualizeNodes((node: Node) => ())	
    visualizeEdges
  }
  
  /**
   * Provides a visualization of the nodes as specified by the underlying graph.
   * The given Traversal is used to traverse the connected nodes of the graph starting
   * from the node with the given nodeID. The function updateNode is applied to each node 
   * that gets traversed.
   * @param traverser : specifying the way nodes should be traversed 
   * @param nodeID : the id of the root, the node from which the traversal should start
   * @param updateNode : a function to be applied to each traversed node
   */
  private def visualizeNodes(traverser: Traversal, nodeID: Int, updateNode : Node => Unit) = {
    unvisualizeNodesToBeRemoved
    traverse(traverser, (e: Node) => if (visualizedNodes.contains(e)) updateNode(e) else {
      if (ubigraphClient.newVertex(e.label) == 0) {
        visualizedNodes += e
        updateNode(e)
      } else System.err.println("Error: Node " + e.label + " has already been visualized. The internal logic should prevent this from occurring")
    }, nodeID) 
  }
  
  /**
   * Provides a visualization of the nodes as specified by the underlying graph
   * In a random order the given function is applied to each node in the graph
   * @param updateNode : a function to be applied to each node in the graph
   */
  private def visualizeNodes(updateNode : Node => Unit) = {
    unvisualizeNodesToBeRemoved
    nodes.values.foreach(e => if (visualizedNodes.contains(e)) updateNode(e) else {
      if (ubigraphClient.newVertex(e.label) == 0) {
        visualizedNodes += e
        updateNode(e)
      } else System.err.println("Error: Node " + e.label + " has already been visualized. The internal logic should prevent this from occurring")
    })
  }
  
  /**
   * Provides a visualization of the edges as specified by the underlying graph.
   */
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
  
  /**
   * Removes the still visible nodes that have been removed in the underlying graph.
   */
  private def unvisualizeNodesToBeRemoved = {
    nodesToBeRemoved.foreach(node => ubigraphClient.removeVertex(node.label))
    visualizedNodes = visualizedNodes.diff(nodesToBeRemoved)
    nodesToBeRemoved.clear() 
  }
  
  /**
   * Removes the still visible edges that have been removed in the underlying graph.
   */
  private def unvisualizeEdgesToBeRemoved = {
    edgesToBeRemoved.foreach(edge => ubigraphClient.removeEdge(cantorPairing(edge.source.label, edge.destination.label)))
    visualizedEdges = visualizedEdges.diff(edgesToBeRemoved)
    edgesToBeRemoved.clear()    
  }
  
  /**
   * Overriden method removeNode 
   * if the node with the given label is currently visualized, it is 
   * added to the list of nodes that should be removed on the next visualization call
   * (see parent definition for further effects)
   * @param nodeID : the label of the node that should be removed
   */
  override def removeNode(nodeID: Int) = {
    visualizedNodes.find(node => node.label == nodeID) match {
      case Some(node) => nodesToBeRemoved += node
      case None => ()
    }
    super.removeNode(nodeID)
    //ubigraphClient.removeVertex(nodeID)
    //visualizedNodes -= nodes.get(nodeID).get 
  }
  
  /**
   * Overridden method removeEdge
   * if the edge with the given source and destination label is currently visualized, 
   * it is added to the list of edges that should be removed on the next visualization call
   * (see parent definition for further effects)
   * @param source : the label of the source node of the edge requested for removal
   * @param destination : the label of the destination node of the edge requested for removal
   */
  override def removeEdge(source: Int, destination: Int) = {
    visualizedEdges.find(edge => edge.source.label == source && edge.destination.label == destination) match {
      case Some(edge) => edgesToBeRemoved += edge
      case None => ()
    }
    super.removeEdge(source, destination)
    //ubigraphClient.removeEdge(cantorPairing(source, destination))
    //visualizedEdges -= nodes.get(source).get.getEdgeTo(destination).get
  }
  
  /**
   * Remove the visualization of the underlying graph.
   */
  def removeVisualization : Unit = {
    visualizedNodes.foreach(e => ubigraphClient.removeVertex(e.label)) //ubigraph automatically removes connected edges
    visualizedNodes.clear()
    visualizedEdges.clear()
  }
  
  /**
   * A bijective function that maps the two Integer parameters unto another Integer.
   * @param x : one of two integers to be used in a bijective mapping to another integer
   * @param y : one of two integers to be used in a bijective mapping to another integer
   */
  private def cantorPairing(x: Int, y: Int) : Int = (x+y)*(x+y+1)/2+y

}
