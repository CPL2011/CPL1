package examples.fluspreading
import graph.PersistenceGraph
import simulation.TestDescription
import graph.BreadthFirstTraversal
import graph.Node
import org.ubiety.ubigraph.UbigraphClient

object FluSpreading {
  var ubigraphClient:UbigraphClient = null
  
  def main(args: Array[String]) {
    
      val test:TestDescription = new TestDescription(true,"http://192.168.132.129:20738/RPC2")
	  var graph = new PersistenceGraph("jordidb")
	  
	  graph.addNode(new Person(1))
	  
	  if(test.has == true) ubigraphClient = new UbigraphClient(test.svr)
      ubigraphClient.clear()
      
	  graph.traverse(BreadthFirstTraversal, initVisualisation,1)
	  
	  var engine = new TurnBasedEngine(graph,BreadthFirstTraversal)
      engine.addNecessaryCondition(new Condition{
        def shouldContinue:Boolean = {
          if(engine.getCurrentTime < SimulationTime.TICKS_PER_HOUR * 4)
            return true
          return false
        }
      })
      
      engine.run
      
      graph.visualize(test.has, test.svr)
  }
  
  	def initVisualisation(n:Node) {
  	  n match {
  	    case p:Person=> p.initVisualization(ubigraphClient)
  	  }
	}
}