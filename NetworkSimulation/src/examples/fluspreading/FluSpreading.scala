package examples.fluspreading
import graph.PersistenceGraph
import simulation.TestDescription
import graph.BreadthFirstTraversal
import graph.Node
import org.ubiety.ubigraph.UbigraphClient
import graph.Graph
import com.sun.xml.internal.ws.api.pipe.Engine

object FluSpreading {
  var ubigraphClient:UbigraphClient = null
  
  def main(args: Array[String]) {
      ubigraphClient = new UbigraphClient("http://192.168.132.129:20738/RPC2")
	  var graph = new VisualGraph("jordidb",ubigraphClient)
      
      graph.addNode(new Person(0))
      var i:Int = 1
      while(i < 100){
    	  graph.addNode(new Person(i))
    	  graph.addEdge(i-1,i)
    	  graph.addEdge(i,i-1)
    	  i += 1
      }
      
	  ubigraphClient.clear()
	  graph.traverse(BreadthFirstTraversal, initVisualisation,0)
	  
	  var engine = new TurnBasedEngine(graph,BreadthFirstTraversal)
      
      engine.addNecessaryCondition(new Condition{
        def shouldContinue:Boolean = {
          if(engine.getCurrentTime < SimulationTime.TICKS_PER_HOUR * 4)
            return true
          return false
        }
      })
      
      engine.addTurnClient(graph)
      engine.run
  }
  
  	def initVisualisation(n:Node) {
  	  n match {
  	    case p:Person=> p.initVisualization(ubigraphClient)
  	  }
	}
}
class VisualGraph(dbname:String,ubiClient2:UbigraphClient) extends PersistenceGraph(dbname) with TurnClient {
  val REFRESH_RATE = 2 * SimulationTime.TICKS_PER_MINUTE
  var elapsedTime = REFRESH_RATE + 1
  ubigraphClient = ubiClient2
  
  def step(currentTime:Int, duration:Int) {
    if(elapsedTime > REFRESH_RATE) {
      visualize
      elapsedTime = 0
    }
    elapsedTime += duration
  }
}