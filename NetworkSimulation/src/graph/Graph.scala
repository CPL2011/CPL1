package graph

import scala.collection.immutable.HashMap
import scala.collection.mutable.ListBuffer

class Graph(nodeMap : HashMap[Int, Node]) {
  def this() = this(new HashMap[Int, Node])

  var nodes = nodeMap
  
  def addNode(nodeID: Int) =  
    if(!nodes.contains(nodeID)) nodes += ((nodeID, new Node(nodeID)))
  def addNode(node: Node) = 
    if(!nodes.contains(node.label)) nodes += ((node.label, node))
    
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
   
  def removeNodes(probability: Double) =
    nodes.values.foreach(node =>
      if(Math.random < probability) removeNode(node.label)
    )
    
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
      var comparisonNode = nextNodeToVisit
      var looping = false
      // the code used to prevent looping makes it essentially so
      // that the root chosen is not necessarily the one a human would pick.
      // perhaps replace it with a more powerful algorithm later
      while (!nextNodeToVisit.arrivingEdges.isEmpty && !looping) {
        nextNodeToVisit = nextNodeToVisit.arrivingEdges.head._2.source
        if (comparisonNode.label == nextNodeToVisit.label) looping = true
      }
      traverser.traverse({node => if (!visitedNodes.contains(node)) {f(node); visitedNodes += node}}, nextNodeToVisit)
    }
  }
  
  def traverse(traverser: Traversal) : Unit = traverse(traverser, (node: Node) => ())
  
  def traverse(traverser: Traversal, nodeInt: Int) : Unit = traverse(traverser, (node: Node) => (), nodeInt)
}






