package examples.socialnetwork
import engine.TimeBasedEvent
import engine.SimulationEngine
import engine.Condition
import engine.SimulationTime
import engine.TurnBasedEngine
import engine.Event
import engine.EventBasedEngine
import engine.RoundBasedEngine
import graph.Graph

object SocialNetwork {
  
  def main(args: Array[String]) {
    
    println("Social networks starting.");
	  var graph = new VisualGraph
	  var visualizer = new GraphVisualizer(graph)
	  
	  if(args.length>0)
		  graph.setRemoteUbigraphServerHost(args.first)
	  
	  var i:Int = 0
	  while(i < 100){
	    graph.addNode(new Person(i))
	    i += 1
	  }
	  graph.addBidirectionalEdges(0.02)
	  
	  graph.ubigraphClient.clear()
	  
	  //startTurnBasedEngine(graph,visualizer)
	  //ubigraphClient.clear
	  //startRoundBasedEngine(graph,visualizer)
	  //ubigraphClient.clear
	  startEventBasedEngine(graph,visualizer)
	  println("Social networks stopped.");
  }
  
  private def startTurnBasedEngine(graph:VisualGraph,visualizer:GraphVisualizer){
	  var engine = new TurnBasedEngine(graph)
	  setStopCondition(engine)
      engine.addTurnClient(visualizer)
      engine.run
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
  
  private def setStopCondition(engine:SimulationEngine) {
        engine.addNecessaryCondition(new Condition{
        def shouldContinue:Boolean = {
          if(engine.getCurrentTime < SimulationTime.TICKS_PER_MINUTE)
            return true
          return false
        }
      })
  }
}

class TriggerEvent(time:Int) extends TimeBasedEvent(time) {
  var name = "TriggerEvent"
}