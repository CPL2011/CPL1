package examples
import examples.persistence.XMLGraph
import scala.collection.mutable.ListBuffer
import statistics.Statistics
import graph.Visualizable 
import graph.DepthFirstTraversal
import graph.Node
import graph.Graph
import graph.Edge
import graph.BreadthFirstTraversal
import org.ubiety.ubigraph.UbigraphClient

object Test extends Application {

  
  var leGraph = new Graph with Visualizable
//  leGraph.setRemoteUbigraphServerHost("http://192.168.56.101:20738/RPC2")
  leGraph.ubigraphClient.clear
  var p:Node = new Node(0)
      leGraph.addNode(p)
      var r:Int = 1
      while(r < 300){
    	  leGraph.addNode(new Node(r))
    	  leGraph.addEdge(new Edge(leGraph.nodes.get(r-1).get,leGraph.nodes.get(r).get))
    	  r += 1
      }
      leGraph.addEdge(new Edge(leGraph.nodes.get(r-1).get, leGraph.nodes.get(0).get))
      leGraph.visualize
      Thread.sleep(7000)
      leGraph.visualize((node : Node) => leGraph.ubigraphClient.setVertexAttribute(node.label,"color","#0000ff"))
      Thread.sleep(2000)
      leGraph.visualize(BreadthFirstTraversal, p.label, (node : Node) => leGraph.ubigraphClient.setVertexAttribute(node.label,"color","#fff200"))
      leGraph.removeEdges(0.2)
      var nbOfNodes = 301
      while (nbOfNodes < 401) {
        leGraph.addNode(nbOfNodes)
        nbOfNodes += 1
      }
      leGraph.visualize
      Thread.sleep(2000)
      leGraph.removeVisualization
  
  var edgeGraph = new Graph with Visualizable
//  edgeGraph.setRemoteUbigraphServerHost("http://192.168.56.101:20738/RPC2")
  var s = 0
  while(s<100) {
    edgeGraph.addNode(s)
    s+=1
  }
  edgeGraph.visualize
  edgeGraph.addBidirectionalEdges(0.01)
  edgeGraph.addUnidirectionalEdges(0.01)
  Thread.sleep(5000)
  edgeGraph.visualize
  edgeGraph.traverse(BreadthFirstTraversal, (node : Node) => edgeGraph.ubigraphClient.setVertexAttribute(node.label,"color","#0000ff"))
  Thread.sleep(2000)
  edgeGraph.traverse(DepthFirstTraversal, (node : Node) => edgeGraph.ubigraphClient.setVertexAttribute(node.label,"color","#aaccff"))
  edgeGraph.visualize
  Thread.sleep(5000)
  var t = 0
  while(t<10) {
    edgeGraph.removeEdges(0.004)
    edgeGraph.removeNodes(0.002*Math.pow(t,2))
    t+=1
    edgeGraph.visualize
    Thread.sleep(1000)
  }
  Thread.sleep(5000)
  edgeGraph.removeVisualization
  
  
  //var visGraph = new VisualisableGraph
  //or
//  var visGraph = new XMLGraph("test.xml") with Visualizable with Statistics
//  visGraph.addStatistic(visGraph.numberOfNodes,"NoN")
//  visGraph.addStatistic(visGraph.averageNeighbores,"AvEdges")
//  var a = 1
//  while(a<=20) {
//    visGraph.addNode(a)
//    visGraph.gatherStat()
//    a+=1
//  }
//  a = 1
//  while(a<=10) {
//    visGraph.addEdge(a,15-a)
//    visGraph.gatherStat()
//    a+=1
//  }
//  var b=14
//  while(b>7) {
//    visGraph.addEdge(b,b-1)
//    visGraph.gatherStat()
//    b-=1
//  }
//  visGraph.addEdge(17,19)
//  visGraph.addEdge(16,18)
//  visGraph.gatherStat()
//  visGraph.traverse(DepthFirstTraversal, e => print(e.label + " "))
//  println
//  
//  
//  visGraph.visualize
//  Thread.sleep(3000)
//  visGraph.removeNode(1)
//  visGraph.visualize
//  Thread.sleep(3000)
//  visGraph.removeEdge(8,7)
//  visGraph.removeEdge(16,18)
//  visGraph.removeEdge(5,10)
//  visGraph.removeEdge(9,8)
//  visGraph.removeEdge(12,11)
//  visGraph.visualize
//  Thread.sleep(3000)
//  visGraph.addNode(4993)
//  visGraph.addNode(4994)
//  visGraph.addNode(4995)
//  visGraph.addNode(4996)
//  visGraph.addNode(4997)
//  visGraph.addNode(4998)
//  visGraph.addNode(4999)
//  visGraph.addNode(5000)
//  visGraph.addNode(5001)
//  
//  visGraph.gatherStat()
//  
//  visGraph.addEdge(5000,4993)
//  visGraph.addEdge(5000,4994)
//  visGraph.addEdge(5000,4995)
//  visGraph.addEdge(5000,4996)
//  visGraph.addEdge(5000,4997)
//  visGraph.addEdge(5000,4998)
//  visGraph.addEdge(5000,4999)
//  visGraph.addEdge(4994,4995)
//  visGraph.addEdge(4994,4996)
//  visGraph.addEdge(4994,4997)
//  visGraph.addEdge(4994,4998)
//  visGraph.addEdge(4994,4999)
//  visGraph.addEdge(5000,5001)
//  visGraph.addEdge(4997,4999)
//  
//  visGraph.gatherStat()
//  
//  visGraph.visualize(DepthFirstTraversal, 5000, (node: Node) => visGraph.ubigraphClient.setVertexAttribute(node.label,"color","#0000ff"))
//  Thread.sleep(2000)
//  visGraph.addNode(5002)
//  visGraph.addNode(5003)
//  visGraph.addNode(5004)
//  visGraph.addNode(5005)
//  
//  visGraph.gatherStat()
//  
//  visGraph.addEdge(5001,5002)
//  visGraph.addEdge(5002,5003)
//  visGraph.addEdge(5003,5004)
//  visGraph.addEdge(5004,5005)
//  visGraph.visualize((x: Node) => ())
//  Thread.sleep(3000)
//  visGraph.addNode(111111) // I thought it was more logical if a newly created node also immediately got
//  //updated. as such this node will turn red on the subsequent def call. If someone disagrees,
//  //you can easily change it by deleting 'updateNode(e)' in visualisableGraph def visualizeNodes
//  visGraph.visualize((node : Node) => visGraph.ubigraphClient.setVertexAttribute(node.label,"color","#ff0000"))
//  Thread.sleep(10000)
//  visGraph.removeVisualization
//  
//  visGraph.gatherStat()
//  visGraph.writeStatisticsToFile("test.txt")
//
////
////
////
//  //var test = new TestDescription(true, "http://192.168.56.101:20738/RPC2")
//  //var test = new TestDescription(true,"http://192.168.253.128:20738/RPC2")
//  var test = new TestDescription(false,"")
//  var graph = new Graph
//  
//
//  var i = 1
//  while(i<=21) {
//    graph.addNode(i)
//    i+=1
//  }
//  
//  graph.addEdge(1, 2)
//  graph.addEdge(1, 3)
//  graph.addEdge(2, 4)
//  graph.addEdge(2, 5)
//  graph.addEdge(3, 6)
//  graph.addEdge(3, 7)
//  graph.addEdge(4, 8)
//  graph.addEdge(8, 9)
//  graph.addEdge(5, 6)
//  graph.addEdge(10, 11)
//  graph.addEdge(12, 13)
//  graph.addEdge(6, 14)
//  graph.addEdge(6, 15)
//  graph.addEdge(15, 16)
//  graph.addEdge(15, 17)
//  graph.addEdge(15, 18)
//  graph.addEdge(1, 19)
//  graph.addEdge(1, 20)
//  graph.addEdge(1, 21)
//  
//  
//
//  
//  
//  //print the object ids of the reachable nodes of each node it visits (in order)
//  graph.traverse(BreadthFirstTraversal, e => println(e.originatingEdges.toString), 1)
// 
//  //check if two nodes are connected
//  var connected = false
//  graph.traverse(BreadthFirstTraversal, node => if (node.label == 9) connected = true, 2)
//  if (connected) System.out.println("connected") else System.out.println("unconnected")
//
//  //retrieve an ordered list of the visited nodes
//  var nodeListBuffer = new ListBuffer[Node]()
//  graph.traverse(BreadthFirstTraversal, node => nodeListBuffer += node, 1)
//  System.out.println("The ListBuffer contains an ordered list of size " + nodeListBuffer.size)
//
//  
//  
//  println("testing xml")
//  visGraph.save()
//  val v:Graph = visGraph.load() match{		//for this example a 'cast' to graph is sufficient
//    case Some(t:Graph)=>t
//    case None=>null
//  }
//  println(v.nodes.size)
//  println(visGraph.nodes.size)
  //graph.traverse(DepthFirstTraversal, e => println(e.originatingEdges.toString), 100) // should trigger an error message
  //graph.removeEdge(1, 2)
  //graph.removeEdge(1, 3)
  //graph.removeEdge(1, 21)
// graph.removeEdge(1, 20)
// graph.removeEdge(1, 19)
// graph.removeNode(1)

// graph.visualize
// Thread.sleep(5000)
// graph.removeVisualization
  
//
// System.out.println("---------------------------")
//
//
//
//// println("testing db4o...")
//// graph.storeGraph()
//// println("graph stored")
//// graph.closeDb()
//
//// println("retrieving graph from file")
//// val g = PersistenceGraph.getGraphFromDb("test.db")
//// val l = g.queryDb((n:Node)=>true)
//// val l2 = for(n:Node<-l) yield n.getLabel
//// println("original nodes' values")
//// println(graph.getNodes().keySet)
//// println("stored nodes' values")
//// println(l2)
////
//// g.closeDb()
//// g.deleteDb("test.db")
//
//
//
//// graph.saveGraph("test.xml")
//// val g:XMLGraph = new XMLGraph()
////
////
//// g.loadGraph("test.xml")
//// println("original: ")
//// println(graph.nodes)
//// //println(graph.edges)
//// println("loaded: ")
//// println(g.nodes)
//// //println(g.edges)
//
}

class TestDescription(hasServer: Boolean, server: String) {
  val has = hasServer
  val svr = server
}
