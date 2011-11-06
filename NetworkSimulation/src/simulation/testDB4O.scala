package simulation
import graph.PersistenceGraph
import com.db4o.Db4o
import com.db4o.Db4oEmbedded

object TestDB4O extends Application {
  var graph = new PersistenceGraph("jordidb");
  
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
  
  graph.storeGraph()
  graph.closeDb()
  
  var graph2 = PersistenceGraph.getGraphFromDb("jordidb");
  println(graph2.nodes)
}