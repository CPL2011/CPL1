package examples.fluspreading
import graph.PersistenceGraph
import simulation.TestDescription
import graph.BreadthFirstTraversal
import graph.Node
import org.ubiety.ubigraph.UbigraphClient
import graph.Graph
import com.sun.xml.internal.ws.api.pipe.Engine
import graph.Edge

object FluSpreading {
  var ubigraphClient:UbigraphClient = null
  
  def main(args: Array[String]) {
      //ubigraphClient = new UbigraphClient("http://192.168.132.129:20738/RPC2")
     ubigraphClient = new UbigraphClient("http://192.168.253.134:20738/RPC2")
	  var graph = new VisualGraph(ubigraphClient)
      
      var p:Person = new Person(0)
      graph.addNode(p)
      var i:Int = 1
      while(i < 100){
    	  graph.addNode(new Person(i))
    	  graph.addEdge(i-1,i)
    	  graph.addEdge(i,i-1)
    	  i += 1
      }
      
	  ubigraphClient.clear()
	  
	  //startTurnBasedEngine(graph)
	  //ubigraphClient.clear
	  //startRoundBasedEngine(graph)
	  //ubigraphClient.clear
	  startEventBasedEngine(graph)
  }
  
  private def startRoundBasedEngine(graph:VisualGraph) {
	  var engine = new RoundBasedEngine(graph)
	  setStopCondition(engine)
      engine.addRoundClient(graph)
      engine.run
  }
  
  private def startEventBasedEngine(graph:VisualGraph) {
	  var engine = new EventBasedEngine(graph)
	  setStopCondition(engine)
      engine.addEventClient(graph)
      var event:Event = new TriggerEvent(100)
	  engine.send(event)
      engine.run
  }
  
  private def startTurnBasedEngine(graph:VisualGraph){
	  var engine = new TurnBasedEngine(graph)
	  setStopCondition(engine)
      engine.addTurnClient(graph)
      engine.run
  }
  
  private def setStopCondition(engine:SimulationEngine) {
        engine.addNecessaryCondition(new Condition{
        def shouldContinue:Boolean = {
          if(engine.getCurrentTime < SimulationTime.TICKS_PER_HOUR * 4)
            return true
          return false
        }
      })
  }
}
class TriggerEvent(time:Int) extends TimeBasedEvent(time) {
  var name = "TriggerEvent"
}