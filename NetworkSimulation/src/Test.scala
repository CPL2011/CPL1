//--> in this code the word 'neighbour' refers to a reachable node

import org.ubiety.ubigraph.UbigraphClient
import scala.collection.mutable.HashMap
import scala.collection.mutable.ListBuffer
import com.db4o.ObjectContainer
import com.db4o.Db4o
import com.db4o.query.Predicate
import com.db4o.ObjectSet

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

/**
 * package download: http://www.db4o.com/community/qdownload.aspx?file=java.zip
 */
trait Db4oPersistence{

var db:ObjectContainer = null;
def openDb(path:String){
	db = Db4o openFile "path"
}

def closeDb():Unit = db.close()

def storeNode(i:Int,node:Node){
	db.store((i,node))
}
def storeEdge(e:Edge){
	db.store(e)
}
def storeGraph(g:Graph){
	for((s,n)<-g.nodes) storeNode(s,n)
	for((_,e)<-g.edges) storeEdge(e)
}
implicit def toPredicate[T](predicate: T => Boolean) =
new Predicate[T]() {


	//def `match`(point : Any) : Boolean = 
	//		throw new Exception("This should never be called!")
	def `match`(p : T) : Boolean = {
			return predicate(p)
	}
}
implicit def toList[T](objectSet: ObjectSet[T] ) =
(new RichObjectSet[T](objectSet)).toList

//query return db4o ObjectSet class
//because of the implicit def it is automatically converted to a List[T]
//that is why we needed the RichObjectSet...
/*
 *EXAMPLE OF USAGE
 *def predicate(n:Node):Boolean = n.neighbours.length>X&&...||...
 *queryDb(predicate)
 */
def queryDb[T](predicate: T => Boolean):List[T] =
db query predicate 


}
/*
 * extend DB4O's ObjectSet with scala's Iterator
 */
class RichObjectSet[T](objectSet:ObjectSet[T]) extends Iterator[T] {
	def hasNext:Boolean =  objectSet.hasNext()
			def next:T = objectSet.next()
}
class PersistentGraph(path:String) extends Graph with Db4oPersistence{
	openDb(path)
}

object Test extends App {
  var graph = new PersistentGraph("test.db")

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
  
  println("testing db4o...")
  graph.storeGraph(graph)
  //pred to retrieve all nodes
  def testPred(n:Node):Boolean = true
  val l = graph.queryDb(testPred)
  for(n<-l) println(n.toString())
  graph.closeDb()

}

