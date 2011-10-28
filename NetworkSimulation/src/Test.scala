//--> in this code the word 'neighbour' refers to a reachable node

import scala.collection.mutable.HashMap
import scala.collection.mutable.ListBuffer

class Node(label: String) {
  var neighbours: List[Node] = Nil
 
  def addNeighbour(node : Node) = neighbours = node :: neighbours
  def removeNeighbour(node : Node) = neighbours diff List(node)
  def getNeighbours : List[Node] = neighbours
  
}

class Edge(source: Node, destination: Node) {
  source.addNeighbour(destination)
  //def remove = source.removeNeighbour(destination)
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
  var nodes = new HashMap[String, Node]()
  var edges = new HashMap[(String, String), Edge]()
  
  def addNode(node: String) = 
    if(!nodes.contains(node)) nodes += ((node, new Node(node)))
  def removeNode(node: String) = 
    nodes -= node

  def addEdge(source: String, destination: String) = {
    (nodes.get(source), nodes.get(destination)) match {
      case (Some(sourceNode), Some(destinationNode)) => {
	edges += (((source, destination), new Edge(sourceNode, destinationNode)))
      }
      case _ => {
	System.err.println("Both the source and destination String have to specify existing nodes in the graph. This is not the case")
      }
    }
  }
  def removeEdge(source: String, destination: String) =
    edges -= ((source, destination))

  def traverse(f: Node => Unit, node: String) = {
    nodes.get(node) match {
      case Some(node) => {
	DepthFirstTraversal.traverse(f, node)
      }
      case None => { 
	System.err.println("The specified node is no member of this graph")
      }
    }
    
  }    
}

object Test extends App {
  var graph = new Graph
  graph.addNode("node1")
  graph.addNode("node2")
  graph.addNode("node3")
  graph.addNode("node4")
  graph.addNode("node5")
  graph.addNode("node6")
  graph.addNode("node7")
  graph.addNode("node8")
  graph.addNode("node9")

  graph.addEdge("node1", "node2")
  graph.addEdge("node1", "node3")
  graph.addEdge("node2", "node4")
  graph.addEdge("node2", "node5")  
  graph.addEdge("node3", "node6")
  graph.addEdge("node3", "node7")
  graph.addEdge("node4", "node8")
  graph.addEdge("node8", "node9")
  graph.addEdge("node5", "node6")

  graph.traverse(e => println(e.getNeighbours.toString),"node1")
  graph.traverse(e => println(e.getNeighbours.toString),"node10")
}

