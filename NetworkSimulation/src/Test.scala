//--> in this code the word 'neighbour' refers to a reachable node

import org.ubiety.ubigraph.UbigraphClient
import scala.collection.mutable.HashMap
import scala.collection.mutable.ListBuffer
import com.db4o.ObjectContainer
import com.db4o.Db4o
import com.db4o.query.Predicate
import com.db4o.ObjectSet
import java.io.File

class Node(label: Int) {
  var neighbours: List[Node] = Nil
  //If you store a list of Nodes the Node has to do some kind of Query to find the Edge that connects this neigbour.
  //Shouldn't it be better to keep a list of Edges? Query's are verry cpu intensive.
  
  def getLabel : Int = label
  def addNeighbour(node : Node) = neighbours = node :: neighbours
  def removeNeighbour(node : Node) = neighbours diff List(node)
  def getNeighbours : List[Node] = neighbours
  def visualize(ubigraphClient : UbigraphClient) = ubigraphClient.newVertex(label)
  
  
}

class Edge(source: Node, destination: Node) {
  source.addNeighbour(destination)
  def getSource():Node = source
  def getDestination():Node = destination
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
	db = Db4o openFile path
}

def closeDb():Unit = db.close()
//clear db by deleting the file and restart the db...
def deleteDb(path:String){
  val thedb:File = new File(path)
  
  if(thedb.exists()) {
    if(thedb.delete())    println("deleted old test db")
    else println(path + " does not exist!")
  }
    
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
class PersistenceGraph(p:String) extends Graph with Db4oPersistence{
    val path = p
    	//Maybe this is  the sollution to your described problem with the nullpointers.
    	//Do I do not know if db4o will do with possible loops
    	//EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
	//config.common().objectClass(Graph.class).cascadeOnUpdate(true);
	//openDb(config,path)
	openDb(path)
	
def storeNode(i:Int,node:Node){
	db.store((i,node))
}
def storeEdge(e:Edge){
	db.store(e)
}
def storeGraph(){
	db.store(this)
	for((_,n)<-nodes) db.store(n)//db4o does not store the nodes and edges when you just store the graph??->I've got some nullpointers
	for((_,e)<-edges) db.store(e)//when I left out these 2 lines-_-
	//The default update depth for all objects is 1, meaning that only primitive and String members will be updated, but changes in object members will not be reflected.

}

//def getGraphFromPersistence():PersistenceGraph{
//   this = queryDb((g:PersistenceGraph)=>g.path==path)
//   nodes.clear()
//   edges.clear()
//   for(n:Node<-queryDb((n:Node)=>true)) addNode(n.getLabel)
//   for(e:Edge<-queryDb((e:Edge)=>true)) addEdge(e.getSource().getLabel,e.getDestination().getLabel)
//}
}
object PersistenceGraph extends Graph with Db4oPersistence{
  
 def getGraphFromDb(path:String):PersistenceGraph = {
 	//This can be a problem to.
 	//The default activation depth for any object is 5, so our example above runs into nulls after traversing 5 references.
 	//Proposed sollution:
 	//Configuration config = Db4o.newConfiguration();
	//config.objectClass(Node.class).cascadeOnActivate(true);
	//config.objectClass(Edge.class).cascadeOnActivate(true);
    openDb(path)
    val p:PersistenceGraph = queryDb((g:PersistenceGraph)=>(g.path==path)).last
    
    closeDb()
    p.openDb(path)
    return p
  }
    
}

object Test extends Application {
  
  
  var graph = new PersistenceGraph("test.db")
  

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
  graph.storeGraph()
  println("graph stored")
  graph.closeDb()
  
  println("retrieving graph from file")
  val g = PersistenceGraph.getGraphFromDb("test.db")
  val l = g.queryDb((n:Node)=>true)
  val l2 = for(n:Node<-l) yield n.getLabel
  println("original nodes' values")
  println(graph.nodes.keySet)
  println("stored nodes' values")
  println(l2)

  g.closeDb()
  g.deleteDb("test.db")
}

