package examples.fluspreading

import scala.util.Random
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
import engine.EventClient
import engine.TurnClient
import engine.RoundClient

object FluSpreading {
  
  def main(args: Array[String]) {
    val infectedPersonRate = 0.03f
	  var graph = new VisualGraph
	  var visualizer = new GraphVisualizer(graph)
      graph.setRemoteUbigraphServerHost("http://192.168.132.132:20738/RPC2")
      
      var i:Int = 0
      while(i < 200){
    	  var p:Person = new Person(i)
    	  if(infectedPersonRate > Random.nextFloat())
    	    p.updateStatus(InfectionStatus.Infectious)
    	  graph.addNode(p)
    	  i += 1
      }
      graph.addUnidirectionalEdges(0.005)
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
      engine.addRoundClient(engineMonitor)
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
      engine.addTurnClient(engineMonitor)
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
object engineMonitor extends RoundClient with EventClient with TurnClient {
  val refreshRate:Int = SimulationTime.TICKS_PER_MINUTE * 20
  var nextRefresh:Int = 0
  
  def doTurn(time:Int, duration:Int){
    displayTime(time)
  }
  def doRound(time:Int, duration:Int){
    displayTime(time)
  }
  private def displayTime(time:Int) {
    if(time >= nextRefresh) {
      println("CurrentTime: " + SimulationTime.toString(time) );
      nextRefresh = nextRefresh + refreshRate
    }
  }
  def nextRound() {}
  def notify(event:Event){
    
  }
}
class TriggerEvent(time:Int) extends TimeBasedEvent(time) {
  var name = "TriggerEvent"
}