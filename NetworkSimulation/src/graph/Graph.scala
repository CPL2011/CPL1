package graph

import scala.collection.immutable.HashMap
import scala.collection.mutable.ListBuffer
/**
  * A Graph represents a network of Nodes (actors) that are connected with Edges (links)
  * 
  * @param nodeMap an initial map of Nodes and keys of the graph
  */
class Graph(nodeMap : HashMap[Int, Node]) {
  /**
    *Create an empty Graph 
    */
  def this() = this(new HashMap[Int, Node])
  /**
    * A Map of unique keys and Nodes
    * The keys correspond to the label of the nodes 
    */
  var nodes = nodeMap
  /**
    * checks if the nodes Map contains a node
    * @param node 
    * @return  
    */
  def hasNode(node: Node) = nodes.contains(node.label)
  /**
    *adds a new node based on the given identifier to the graph
    *if this identifier is not already used
    *@param nodeID the label/key of the new node 
    */
  def addNode(nodeID: Int) =  
    if(!nodes.contains(nodeID)) nodes += ((nodeID, new Node(nodeID)))
  /**
    *adds a node the graph if this node is not already a member of the graph
    *@param node
    */
  def addNode(node: Node) = 
    if(!nodes.contains(node.label)) nodes += ((node.label, node))
   /**
     * removes a node from the graph based on its identifier
     * @param nodeID  
     */
  def removeNode(nodeID: Int) = 
    nodes.get(nodeID) match {
      case Some(node) => {
	    //edges.filterKeys({case (x, y) => x == nodeID || y == nodeID }).foreach({case (edgeKey, edge) => edge.remove}) 
        node.remove
	    nodes -= nodeID
      }
      case None => {
	System.err.println("The specified node cannot be removed since it is no member of this graph")
      }
    }
  
  /** With a given probability, each node can be removed from the graph
    * @param probability
    */
  def removeNodes(probability: Double) =
    nodes.values.foreach(node =>
      if(Math.random < probability) removeNode(node.label)
    )
    /**
     * indirectly inform the nodes of an edge that this edge is connected to the nodes
     * @param edge
     */
  def addEdge(edge: Edge) = {
	  edge.informNodes(this)
  }
  
  def addEdge(source: Int, destination: Int) = {
    (nodes.get(source), nodes.get(destination)) match {
      case (Some(sourceNode), Some(destinationNode)) => {
        sourceNode.addNeighbour(destinationNode)
      }
      case _ => {
	    System.err.println("Both the source and destination id have to specify existing nodes in the graph. This is not the case")
      }
    }
  }
  
  /**
   * DEPRECATED
   * Currently this method only adds edges IN ONE DIRECTION
   * @param: The probability that an edge is formed  between each two node pairs
   */
/**  def adEdges(probability: Double) = {
    var destinations = nodes.values.toList.tail
    nodes.values.foreach(source => {
      if (!destinations.isEmpty) {
        destinations.foreach(destination =>  
          if (Math.random < probability) source.addNeighbour(destination)
        )
        destinations = destinations.tail 
      }
    })
  }*/
  
  /**
   * Adds unidirectional edges between nodes in the graph that are not yet connected,
   * with a given probability.
   */
  def addUnidirectionalEdges(probability: Double) = {
    val destinations = nodes.values.toList
    nodes.values.foreach(source =>
      destinations.foreach(destination =>
        if(source != destination && !source.getNeighbours.contains(destination) && Math.random < probability)
          source.addNeighbour(destination)
      )
    )
  }
  
  /**
   * Adds bidirectional edges between nodes in the graph that are not yet connected,
   * with a given probability.
   */
  def addBidirectionalEdges(probability: Double) = {
    var destinations = nodes.values.toList.tail
    nodes.values.foreach(source => {
      if (!destinations.isEmpty) {
        destinations.foreach(destination =>  
          if (!source.getNeighbours.contains(destination) && Math.random < probability) {
            source.addNeighbour(destination)
            destination.addNeighbour(source)
          }
        )
        destinations = destinations.tail 
      }
    })
  }

  def removeEdge(source: Int, destination: Int) = {
    (nodes.get(source), nodes.get(destination)) match {
      case (Some(sourceNode), Some(destinationNode)) => {
        sourceNode.removeNeighbour(destination)
      }
      case _ => {
	    System.err.println("The specified edge cannot be removed since it is no member of this graph")
      }
    }
  }
  
  def removeEdges(probability: Double) = {
	var edges = new ListBuffer[Edge]
	traverse(DepthFirstTraversal, (node) => edges ++= node.originatingEdges.values)
    edges.foreach(edge =>
        if (Math.random < probability) removeEdge(edge.source.label, edge.destination.label)
    )
  }
  
  def traverse(traverser: Traversal, f: Node => Unit, nodeID: Int) = {
    nodes.get(nodeID) match {
      case Some(node) => {
	traverser.traverse(f, node)
      }
      case None => { 
	System.err.println("The specified node is no member of this graph")
      }
    }
    
  }
  
  def traverse(traverser: Traversal, f: Node => Unit) = {
    var visitedNodes = new ListBuffer[Node]()
    while(nodes.values.toList.diff(visitedNodes).size != 0) {
      var nextNodeToVisit = nodes.values.toList.diff(visitedNodes).first
      var consideredAsRootNodes = new ListBuffer[Node]
      consideredAsRootNodes += nextNodeToVisit
      var looping = false
      // the code used to prevent looping makes it essentially so
      // that the root chosen is not necessarily the one a human would pick.
      // perhaps replace it with a more powerful algorithm later
      while (!nextNodeToVisit.arrivingEdges.isEmpty && !looping) {
        if (consideredAsRootNodes.contains(nextNodeToVisit)) looping = true
        else nextNodeToVisit = nextNodeToVisit.arrivingEdges.head._2.source
      }
      traverser.traverse({node => if (!visitedNodes.contains(node)) {f(node); visitedNodes += node}}, nextNodeToVisit)
    }
  }
  
  def traverse(traverser: Traversal) : Unit = traverse(traverser, (node: Node) => ())
  
  def traverse(traverser: Traversal, nodeInt: Int) : Unit = traverse(traverser, (node: Node) => (), nodeInt)
}






