package graph

import org.ubiety.ubigraph.UbigraphClient
import scala.collection.mutable.HashMap
import scala.collection.mutable.ListBuffer


  class Graph {
  private var nodes = new HashMap[Int, Node]()
  private var edges = new HashMap[(Int, Int), Edge]()
  
  def getNodes():HashMap[Int, Node] = nodes
  def getEdges():HashMap[(Int, Int), Edge] = edges
  
  def addNode(nodeID: Int) = 
    if(!nodes.contains(nodeID)) nodes += ((nodeID, new Node(nodeID)))
  def removeNode(nodeID: Int) = 
    nodes.get(nodeID) match {
      case Some(node) => {
	edges.filterKeys({case (x, y) => x == nodeID || y == nodeID }).foreach({case (edgeKey, edge) => edge.remove}) 
	nodes -= nodeID
      }
      case None => {
	System.err.println("The specified node cannot be removed since it is no member of this graph")
      }


    }
   
  def addEdge(source: Int, destination: Int) = {
    (nodes.get(source), nodes.get(destination)) match {
      case (Some(sourceNode), Some(destinationNode)) => {
	edges += (((source, destination), new Edge(sourceNode, destinationNode)))
      }
      case _ => {
	System.err.println("Both the source and destination id have to specify existing nodes in the graph. This is not the case")
      }
    }
  }
  def removeEdge(source: Int, destination: Int) = {
    edges.get((source, destination)) match {
      case Some(edge) => {
	edge.remove
	edges -= ((source, destination))
      }
      case None => {
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
  
  def visualize = {
    var ubigraphClient = new UbigraphClient
    nodes.values.foreach(e => e.visualize(ubigraphClient))
    edges.values.foreach(e => e.visualize(ubigraphClient))
  }
}






