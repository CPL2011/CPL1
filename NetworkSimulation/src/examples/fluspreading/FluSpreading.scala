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
import engine.statistics.Statistics

object FluSpreading {
	val infectedPersonRate = 0.03f
			var statisticsPath = "fluspreadingData"
			def main(args: Array[String]) {


		println("Fluespreading example will start 3 times each time with another Engine.");
		var graph = new VisualGraph("fluspreading.db")
		var visualizer = new GraphVisualizer(graph)
		if(args.length > 0)
			graph.setRemoteUbigraphServerHost(args.first)

			var i:Int = 0
			while(i < 200){								//Graph setup
				var p:Person = new Person(i)
			if(infectedPersonRate > Random.nextFloat())
				p.updateStatus(InfectionStatus.Infectious)
				graph.addNode(p)
				i += 1
			}
			graph.addUnidirectionalEdges(0.005)
			graph.openDb()//to illustrate load/store functionality, the initial graph is now saved
			graph.save()	//to restore before each simulation, a 'new' graph will be loaded from the database
		


			startTurnBasedEngine(graph,visualizer)
			graph.removeVisualization
			

			val graph2:VisualGraph = graph.loadGraph()
			graph2.nodes.values.foreach(resetPerson)
			graph2.visualize
			startRoundBasedEngine(graph2,visualizer)
			graph2.removeVisualization


			
			val graph3:VisualGraph = graph.loadGraph()
			graph.closeDb()
			graph3.nodes.values.foreach(resetPerson)
			
			graph3.visualize
			startEventBasedEngine(graph3,visualizer)
			graph3.removeVisualization
			println("Fluespreading done");
	}

	private def resetPerson(node:Node) {
		node match {
		case p:Person =>
		p.resetState
		if(infectedPersonRate > Random.nextFloat())
			p.updateStatus(InfectionStatus.Infectious)
		case _ =>
		}
	}
	
 /**
   * The next three functions will run the simulation
   * First some 'statistic' functions are added which will be used for datagathering
   * next the simulation is run
   * finally the collected statistics are written into a file
   */
	private def startRoundBasedEngine(graph:VisualGraph,visualizer:GraphVisualizer) {
		var engine = new RoundEngine(graph)

		engine.addStatistic(engine.numberOfNodes,"Nodes")
		engine.addStatistic(engine.averageNeighbores,"Av_Neighbours")
		engine.addStatistic(getSusceptibleAmount,"Susceptible")
		engine.addStatistic(getInfectedAmount,"Infected")
		engine.addStatistic(getRemovedAmount,"Removed")

		setStopCondition(engine)
		engine.addRoundClient(visualizer)
		engine.addRoundClient(new engineMonitor)
		engine.run
		engine.writeStatisticsToFile(statisticsPath + "_round.txt")
	}

	private def startEventBasedEngine(graph:VisualGraph,visualizer:GraphVisualizer) {
		var engine = new EventEngine(graph)

		engine.addStatistic(engine.numberOfNodes,"Nodes")
		engine.addStatistic(engine.averageNeighbores,"Av_Neighbours")
		engine.addStatistic(getSusceptibleAmount,"Susceptible")
		engine.addStatistic(getInfectedAmount,"Infected")
		engine.addStatistic(getRemovedAmount,"Removed")

		setStopCondition(engine)
		engine.addEventClient(visualizer)
		var event:Event = new TriggerEvent(100)
		engine.send(event)
		engine.run
		engine.writeStatisticsToFile(statisticsPath + "_event.txt")
	}

	private def startTurnBasedEngine(graph:VisualGraph,visualizer:GraphVisualizer){
		var engine = new TurnEngine(graph)

		engine.addStatistic(engine.numberOfNodes,"Nodes")
		engine.addStatistic(engine.averageNeighbores,"Av_Neighbours")
		engine.addStatistic(getSusceptibleAmount,"Susceptible")
		engine.addStatistic(getInfectedAmount,"Infected")
		engine.addStatistic(getRemovedAmount,"Removed")

		setStopCondition(engine)
		engine.addTurnClient(visualizer)
		engine.addTurnClient(new engineMonitor)
		engine.run
		engine.writeStatisticsToFile(statisticsPath + "_turn.txt")
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
	/**
	 * Definition of some functions used for data gathering
	 * These functions must have an Any object as parameter and return a string
	 */
	private def getInfectedAmount(g:Any):String = g match{
	case g:VisualGraph=>getPersonStatusAmount(g,"Infected").toString
	case _=> ""
	}
	private def getSusceptibleAmount(g:Any):String = g match{
	case g:VisualGraph => getPersonStatusAmount(g,"Susceptible").toString
	case _=>""
	}
	private def getRemovedAmount(g:Any):String = g match{
	case g:VisualGraph=>getPersonStatusAmount(g,"Removed").toString
	case _=> ""
	}

	/**
	 * helper function for the statistics functions
	 */
	private def getPersonStatusAmount(g:VisualGraph,status:String):Int = {
			var i = 0
					for((_,n)<-g.nodes) n match{
					case n:Person => status match {
					case "Susceptible" => if(n.isSusceptible)i +=1
					case "Infected" => if(n.isInfected)i +=1
					case "Removed" => if(n.isRemoved)i +=1
					}
					case _=>
					}

			return i
	}

}
class engineMonitor extends RoundClient with TurnClient {
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
}
class TriggerEvent(time:Int) extends TimeBasedEvent(time) {
	var name = "TriggerEvent"
}
/**
 * Definition of the engines that will allow datagathering
 */
class EventEngine(graph:VisualGraph) extends EventBasedEngine(graph) with Statistics{
	override val subject = graph
			setSamplePeriod(300)

}
class TurnEngine(graph:VisualGraph) extends TurnBasedEngine(graph) with Statistics{
	override val subject = graph
			setSamplePeriod(2000)
}
class RoundEngine(graph:VisualGraph) extends RoundBasedEngine(graph) with Statistics{
	override val subject = graph
			setSamplePeriod(2000)															
}