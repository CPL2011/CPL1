package engine

import graph.Graph
import graph.Node

/**
 * Round based engine will first call doRound and then nextRound on each RoundClient.
 * You can prepare the next status of the RoundClient in doRound and when nextRound is called
 * set the next status as the current status of the RoundClient.
 */
class RoundBasedEngine(graph:Graph) extends SimulationEngine(graph) {
	var ticksPerStep:Int = 10
	var roundClients:List[RoundClient] = List[RoundClient]()
  
	/**
	 * Start the engine
	 */
  def run() {
	  println("RoundBasedEngine started");
	  while(shouldContinue) {
	    doStep
	  }
	  println("RoundBasedEngine stopped");
  }
	override def doStep(){
	  forallClients(doRound)
	  forallClients(nextRound)
	  currentTime += ticksPerStep
	}
	
	private def forallClients(f:RoundClient=>Unit) {
	  	var itt:Iterator[Node] = graph.nodes.valuesIterator
	    while(itt.hasNext) {
	      var next:Node = itt.next
	      f(next)
	    }
	    roundClients.foreach(c=>f(c))
	}
	
	private def doRound(c:RoundClient) {
	  c.doRound(currentTime,ticksPerStep)
	}
	
	private def nextRound(c:RoundClient) {
	  c.nextRound
	}

	def addRoundClient(client:RoundClient) {
		roundClients ::= client
	}
}

/**
 * RoundClients can be added tot the RoundBasedEngine.
 * The engine will call the doRound and nextRound functions with the correct arguments
 */
trait RoundClient{
  def doRound(timestamp:Int, duration:Int)
  def nextRound
}