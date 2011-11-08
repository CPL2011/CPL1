package examples.fluspreading
import graph.Graph
import graph.Traversal
import graph.Node

class TurnBasedEngine(graph: Graph,traverser:Traversal) extends SimulationEngine(graph){
	var ticksPerStep:Int = 10
	
	override def run() {
	  while(shouldContinue) {
	    graph.traverse(traverser,doTurn,1)
	    currentTime += ticksPerStep
	  }
	}
	
	def doTurn(n:Node){
	  n.step(currentTime, ticksPerStep)
	}
}