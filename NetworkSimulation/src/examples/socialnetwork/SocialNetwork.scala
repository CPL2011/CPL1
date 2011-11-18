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
import statistics.Statistics

object SocialNetwork {
  val statisticsPath:String = "socialNetwork"
  def main(args: Array[String]) {
    
    println("Social networks starting.");
	  var graph = new VisualGraph("social.db")
	  var visualizer = new GraphVisualizer(graph)
	  
	  if(args.length>0)
		  graph.setRemoteUbigraphServerHost(args.first)
	  
	  var i:Int = 0
	  while(i < 250){
	    graph.addNode(new Person(i))
	    i += 1
	  }
	  graph.addBidirectionalEdges(0.02)
	  
	  graph.openDb()
			graph.save()


			startTurnBasedEngine(graph,visualizer)
			graph.removeVisualization
			

			val graph2:VisualGraph = graph.loadGraph()
			//graph2.nodes.values.foreach(resetPerson)
			graph2.visualize
			startRoundBasedEngine(graph2,visualizer)
			graph2.removeVisualization


			
			val graph3:VisualGraph = graph.loadGraph()
			graph.closeDb()
			//graph3.nodes.values.foreach(resetPerson)
			
			graph3.visualize
			startEventBasedEngine(graph3,visualizer)
			graph3.removeVisualization
			println("Fluespreading done");
	  println("Social networks stopped.");
  }
  
  private def startTurnBasedEngine(graph:VisualGraph,visualizer:GraphVisualizer){
	  var engine = new TurnEngine(graph)
	  
	  engine.addStatistic(engine.numberOfNodes,"#Nodes")
	  engine.addStatistic(engine.averageNeighbores,"Av Neighbours")
	  engine.addStatistic(getEdgesAmount,"#edgeTotal")
		
	  setStopCondition(engine)
      engine.addTurnClient(visualizer)
      engine.run
      engine.writeStatisticsToFile(statisticsPath + "_turn.txt")
  }
  
  private def startRoundBasedEngine(graph:VisualGraph,visualizer:GraphVisualizer) {
	  var engine = new RoundEngine(graph)
	  
	  engine.addStatistic(engine.numberOfNodes,"#Nodes")
	  engine.addStatistic(engine.averageNeighbores,"Av Neighbours")
	  engine.addStatistic(getEdgesAmount,"#edgeTotal")
	  
	  setStopCondition(engine)
      engine.addRoundClient(visualizer)
      engine.run
      engine.writeStatisticsToFile(statisticsPath + "_round.txt")
  }
  
  private def startEventBasedEngine(graph:VisualGraph,visualizer:GraphVisualizer) {
	  var engine = new EventEngine(graph)
	  
	  engine.addStatistic(engine.numberOfNodes,"#Nodes")
	  engine.addStatistic(engine.averageNeighbores,"Av Neighbours")
	  engine.addStatistic(getEdgesAmount,"#edgeTotal")
	  
	  setStopCondition(engine)
      engine.addEventClient(visualizer)
      var event:Event = new TriggerEvent(100)
	  engine.send(event)
      engine.run
      engine.writeStatisticsToFile(statisticsPath + "_event.txt")
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
  private def getEdgesAmount(g:Any):String = g match{
	case g:VisualGraph=>{
	  var i:Int = 0
	  for((_,n:Person)<-g.nodes){
	    i += n.friends.size
	  }
	  (i/2).toString	//every edge is counted twice
	}
	case _=> ""
	}
	
}
class EventEngine(graph:VisualGraph) extends EventBasedEngine(graph) with Statistics{
	override val subject = graph
			setSamplePeriod(1)

}
class TurnEngine(graph:VisualGraph) extends TurnBasedEngine(graph) with Statistics{
	override val subject = graph
			setSamplePeriod(1)
}
class RoundEngine(graph:VisualGraph) extends RoundBasedEngine(graph) with Statistics{
	override val subject = graph
			setSamplePeriod(1)															
}
class TriggerEvent(time:Int) extends TimeBasedEvent(time) {
  var name = "TriggerEvent"
}