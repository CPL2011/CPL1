package simulation


import graph._
import scala.collection.mutable.ListBuffer
import simulation.stat.Statistics
class testGraph extends XMLGraph with Statistics
object Test extends Application {

  
  var visGraph = new VisualisableGraph
  var a = 1
  while(a<=20) {
    visGraph.addNode(a)
    a+=1
  }
  a = 1
  while(a<=10) {
    visGraph.addEdge(a,15-a)
    a+=1
  }
  var b=14
  while(b>7) {
    visGraph.addEdge(b,b-1)
    b-=1
  }
  visGraph.addEdge(17,19)
  visGraph.addEdge(16,18)
  visGraph.traverse(DepthFirstTraversal, e => print(e.label + " "))
  println
 
//  
//  visGraph.visualize((x: Node) => ())
//  Thread.sleep(10000)
//  visGraph.removeEdge(1,2)
//  visGraph.removeNode(1)
//  visGraph.visualize(turnNodesRed)
//  def turnNodesRed(node : Node) = {
//    visGraph.ubigraphClient.setVertexAttribute(node.label,"color","#ff0000")
//  }
//  Thread.sleep(10000)
//  visGraph.removeVisualization

//  
//  
//  
  //var test = new TestDescription(true, "http://192.168.56.101:20738/RPC2")
  //var test = new TestDescription(true,"http://192.168.253.128:20738/RPC2")
  var test = new TestDescription(false,"")
  var graph = new testGraph()
  graph.addStatistic(graph.numberOfNodes,"NoN")
  graph.addStatistic(graph.averageNeighbores,"AvEdges")

  var i = 1
  while(i<=21) {
    graph.addNode(i)
    if(i%5==0)graph.gatherStat(graph)
    i+=1
  }
  
  graph.addEdge(1, 2)
  graph.addEdge(1, 3)
  graph.addEdge(2, 4)
  graph.addEdge(2, 5)  
  graph.addEdge(3, 6)
  graph.gatherStat(graph)
  graph.addEdge(3, 7)
  graph.addEdge(4, 8)
  graph.addEdge(8, 9)
  graph.addEdge(5, 6)
  graph.gatherStat(graph)
  graph.addEdge(10, 11)
  graph.addEdge(12, 13)
  graph.addEdge(6, 14)
  graph.addEdge(6, 15)
  graph.addEdge(15, 16)
  graph.gatherStat(graph)
  graph.addEdge(15, 17)
  graph.addEdge(15, 18)
  graph.addEdge(1, 19)
  graph.addEdge(1, 20)
  graph.addEdge(1, 21)
  graph.gatherStat(graph)
  
  graph.writeStatisticsToFile("test.txt")
  
  //print the object ids of the reachable nodes of each node it visits (in order)
  graph.traverse(BreadthFirstTraversal, e => println(e.originatingEdges.toString), 1) 
 
  //check if two nodes are connected
  var connected = false
  graph.traverse(BreadthFirstTraversal, node => if (node.label == 9) connected = true, 2) 
  if (connected) System.out.println("connected") else System.out.println("unconnected") 

  //retrieve an ordered list of the visited nodes 
  var nodeListBuffer = new ListBuffer[Node]()
  graph.traverse(BreadthFirstTraversal, node => nodeListBuffer += node, 1)
  System.out.println("The ListBuffer contains an ordered list of size " + nodeListBuffer.size)

  //graph.traverse(DepthFirstTraversal, e => println(e.originatingEdges.toString), 100) // should trigger an error message
  //graph.removeEdge(1, 2)
  //graph.removeEdge(1, 3)
  //graph.removeEdge(1, 21)
//  graph.removeEdge(1, 20)
//  graph.removeEdge(1, 19)
//  graph.removeNode(1)

//  graph.visualize
//  Thread.sleep(5000)
//  graph.removeVisualization
  
//  
//  System.out.println("---------------------------")
//  
//  
//  
////  println("testing db4o...")
////  graph.storeGraph()
////  println("graph stored")
////  graph.closeDb()
//  
////  println("retrieving graph from file")
////  val g = PersistenceGraph.getGraphFromDb("test.db")
////  val l = g.queryDb((n:Node)=>true)
////  val l2 = for(n:Node<-l) yield n.getLabel
////  println("original nodes' values")
////  println(graph.getNodes().keySet)
////  println("stored nodes' values")
////  println(l2)
////
////  g.closeDb()
////  g.deleteDb("test.db")
//
//  
//  
////  graph.saveGraph("test.xml")
////  val g:XMLGraph = new XMLGraph()
////
////  
////  g.loadGraph("test.xml")
////  println("original: ")
////  println(graph.nodes)
////  //println(graph.edges)
////  println("loaded: ")
////  println(g.nodes)
////  //println(g.edges)
//  
}

class TestDescription(hasServer: Boolean, server: String) {
  val has = hasServer
  val svr = server
}