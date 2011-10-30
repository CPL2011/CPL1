package simulation


import graph._

object Test extends App {
  
  
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
  println(graph.getNodes().keySet)
  println("stored nodes' values")
  println(l2)

  g.closeDb()
  g.deleteDb("test.db")
}