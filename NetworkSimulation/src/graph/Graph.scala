package graph

import scala.collection.immutable.HashMap
import scala.collection.mutable.ListBuffer


  class Graph {
  var nodes = new HashMap[Int, Node]()
  
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
  
  
//  def visualize = {
//    nodes.values.foreach(e => e.visualize(ubigraphClient))
//    var edges : List[Edge] = Nil
//    nodes.values.foreach(e => edges = edges ++ e.originatingEdges.values.toList)
//    edges.foreach(e => e.visualize(ubigraphClient))
//  }
  
//  def removeVisualization = {
//    nodes.values.foreach(e => e.removeVisualization(ubigraphClient))
//    //var edges : List[Edge] = Nil
//    //nodes.values.foreach(e => edges = edges ++ e.originatingEdges.values.toList)
//    //edges.foreach(e => e.removeVisualization(ubigraphClient))
//  }  
}






