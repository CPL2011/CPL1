package examples.fluspreading

import graph.Graph
import graph.Node

class RoundBasedEngine(graph:Graph) extends SimulationEngine(graph) {
	var ticksPerStep:Int = 10
	var roundClients:List[RoundClient] = List[RoundClient]()
  
  def run() {
	  println("RoundBasedEngine started");
	  while(shouldContinue) {
	    forallClients(doRound)
	    forallClients(nextRound)
	    currentTime += ticksPerStep
	  }
	  println("RoundBasedEngine stopped");
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

trait RoundClient{
  def doRound(timestamp:Int, duration:Int)
  def nextRound
}