//--> in this code the word 'neighbour' refers to a reachable node

import org.ubiety.ubigraph.UbigraphClient
import scala.collection.mutable.HashMap
import scala.collection.mutable.ListBuffer

class Node(label: Int) {
  var neighbours: List[Node] = Nil
  
  def getLabel : Int = label
  def addNeighbour(node : Node) = neighbours = node :: neighbours
  def removeNeighbour(node : Node) = neighbours diff List(node)
  def getNeighbours : List[Node] = neighbours
  def visualize(ubigraphClient : UbigraphClient) = ubigraphClient.newVertex(label)
}

class Edge(source: Node, destination: Node) {
  source.addNeighbour(destination)
  def visualize(ubigraphClient : UbigraphClient) = ubigraphClient.newEdge(source.getLabel, destination.getLabel) 
  def remove = source.removeNeighbour(destination)
}

abstract class Traversal {
   def traverse(f: Node => Unit, node: Node)
}

object DepthFirstTraversal extends Traversal {
  def traverse(f: Node => Unit, node: Node) = {
    var visited : List[Node] = Nil;
    def dft(f: Node => Unit, node: Node) : Unit = {
      f(node)
      visited = node :: visited
      node.getNeighbours.reverse.foreach(e => if (!visited.contains(e)) dft(f, e))
    }
    dft(f, node)
  }
}

object BreadthFirstTraversal extends Traversal {
  def traverse(f: Node => Unit, node: Node) = {
    def bft(f: Node => Unit, node: Node) : Unit = {
      var queue = new ListBuffer[Node]()
      var visited : List[Node] = Nil
      f(node)
      visited = node :: visited
      queue += node
      while (!queue.isEmpty) {
	queue.head.getNeighbours.reverse.foreach(
	  e => if (!visited.contains(e)) { 
	    f(e) 
	    visited = e :: visited
	    queue += e
	  }
	)
	queue = queue.tail;
      }
    }
    bft(f, node)
  }
}


class Graph {
  var nodes = new HashMap[Int, Node]()
  var edges = new HashMap[(Int, Int), Edge]()
  
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

object Test extends App {
  var graph = new Graph

  var i = 1
  while(i<=21) {
    graph.addNode(i)
    i+=1
  }
  
  graph.addEdge(1, 2)
  graph.addEdge(1, 3)
  graph.addEdge(2, 4)
  graph.addEdge(2, 5)  
  graph.addEdge(3, 6)
  graph.addEdge(3, 7)
  graph.addEdge(4, 8)
  graph.addEdge(8, 9)
  graph.addEdge(5, 6)
  graph.addEdge(10, 11)
  graph.addEdge(12, 13)
  graph.addEdge(6, 14)
  graph.addEdge(6, 15)
  graph.addEdge(15, 16)
  graph.addEdge(15, 17)
  graph.addEdge(15, 18)
  graph.addEdge(1, 19)
  graph.addEdge(1, 20)
  graph.addEdge(1, 21)

  
  graph.traverse(BreadthFirstTraversal, e => println(e.getNeighbours.toString), 1) //print the object ids of the reachable nodes of each node it visits (in order)
  System.out.println("---------------------------")
  graph.traverse(DepthFirstTraversal, e => println(e.getNeighbours.toString), 100) // should trigger an error message
  //graph.removeEdge(1, 2)
  //graph.removeEdge(1, 3)
  //graph.removeEdge(1, 21)
  //graph.removeEdge(1, 20)
  //graph.removeEdge(1, 19)
  graph.removeNode(1)
  graph.visualize // should create a successful visualisation

}

