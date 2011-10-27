//--> in this code the word 'neighbour' refers to a reachable node

import scala.collection.mutable.ListBuffer

object Test extends App {
  var graph = new Graph with BreadthFirstTraversal;
  val node1,node2, node3, node4, node5, node6, node7, node8, node9 = new Node()
  val edge1 = new Edge(node1, node2)
  val edge2 = new Edge(node1, node3)
  val edge3 = new Edge(node2, node4)
  val edge4 = new Edge(node2, node5)
  val edge5 = new Edge(node3, node6)
  val edge6 = new Edge(node3, node7)
  val edge7 = new Edge(node4, node8)
  val edge8 = new Edge(node8, node9)
  val edge9 = new Edge(node5, node6)
  graph.traverse(e => println(e.getNeighbours.toString),node1)
}

// abstract class Graph {
//   def nodes: List[Node]
//   def edges: List[Edge]
  
//   def addNode(node: Node)
//   def removeNode(node: Node)
  
//   def addEdge(edge: Edge)
//   def removeEdge(edge: Edge)

// }

class Node {
  var neighbours: List[Node] = Nil

  def addNeighbour(node : Node) = neighbours = node :: neighbours
  def removeNeighbour(node : Node) = neighbours diff List(node)
  def getNeighbours : List[Node] = neighbours
  
}

class Edge(source: Node, destination: Node) {
  source.addNeighbour(destination)
  //def remove = source.removeNeighbour(destination)
}

class Graph {
  var nodes: List[Node] = Nil
  var edges: List[Edge] = Nil
  
  def addNode(node: Node) = 
    if(! nodes.contains(node)) nodes = node :: nodes
  def removeNode(node: Node) = {
    nodes = nodes diff List(node)
  }

  def addEdge(edge: Edge) = 
    if(! edges.contains(edge)) edges = edge :: edges
  def removeEdge(edge: Edge) =
    edges = edges diff List(edge)
}

// abstract class Traversal {
//   def traverse(f: Node => Unit, node: Node)
// }

trait DepthFirstTraversal {//extends Traversal {
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

trait BreadthFirstTraversal {//extends Traversal {
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


