package examples.fluspreading
import graph.Graph
import graph.Traversal
import graph.Node

class TurnBasedEngine(graph: Graph,traverser:Traversal) extends SimulationEngine(graph){
	var ticksPerStep:Int = 10
	
	
   var turnClients:List[TurnClient] = List[TurnClient]()
  
	override def run() {
	  while(shouldContinue) {
	    graph.traverse(traverser,doTurn,1)
	    turnClients.foreach(c=>c.step(currentTime,ticksPerStep))
	    currentTime += ticksPerStep
	  }
	}
	
	def doTurn(n:Node){
	  n.step(currentTime, ticksPerStep)
	}
	
	def addTurnClient(c:TurnClient) {
	  turnClients ::= c
	}
}

trait TurnClient {
  def step(currentTime:Int, duration:Int)
}