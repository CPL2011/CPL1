package examples.fluspreading

import graph.BreadthFirstTraversal
import graph.Node
import graph.Graph
import graph.Edge
import engine.EventBasedEngine
import engine.Event
import engine.TurnBasedEngine
import engine.TimeBasedEvent
import engine.SimulationEngine
import engine.Condition
import engine.SimulationTime
import engine.RoundBasedEngine

object FluSpreading {
  
  def main(args: Array[String]) {
	  var graph = new VisualGraph
	  var visualizer = new GraphVisualizer(graph)
      graph.setRemoteUbigraphServerHost("http://192.168.132.131:20738/RPC2")
      
      var p:Person = new Person(0)
      graph.addNode(p)
      var i:Int = 1
      while(i < 100){
    	  graph.addNode(new Person(i))
    	  graph.addEdge(i-1,i)
    	  graph.addEdge(i,i-1)
    	  i += 1
      }
      
	  graph.ubigraphClient.clear()
	  
	  //startTurnBasedEngine(graph,visualizer)
	  //ubigraphClient.clear
	  //startRoundBasedEngine(graph,visualizer)
	  //ubigraphClient.clear
	  startEventBasedEngine(graph,visualizer)
  }
  
  private def startRoundBasedEngine(graph:Graph,visualizer:GraphVisualizer) {
	  var engine = new RoundBasedEngine(graph)
	  setStopCondition(engine)
      engine.addRoundClient(visualizer)
      engine.run
  }
  
  private def startEventBasedEngine(graph:VisualGraph,visualizer:GraphVisualizer) {
	  var engine = new EventBasedEngine(graph)
	  setStopCondition(engine)
      engine.addEventClient(visualizer)
      var event:Event = new TriggerEvent(100)
	  engine.send(event)
      engine.run
  }
  
  private def startTurnBasedEngine(graph:VisualGraph,visualizer:GraphVisualizer){
	  var engine = new TurnBasedEngine(graph)
	  setStopCondition(engine)
      engine.addTurnClient(visualizer)
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