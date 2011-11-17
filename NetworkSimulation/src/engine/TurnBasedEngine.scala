package engine
import graph.Graph
import graph.Traversal
import graph.Node

class TurnBasedEngine(graph: Graph) extends SimulationEngine(graph){
	var ticksPerStep:Int = 10
	
	
   var turnClients:List[TurnClient] = List[TurnClient]()
  
	override def run() {
	  println("TurnBasedEngine started");
	  while(shouldContinue) {
	    var itt:Iterator[Node] = graph.nodes.valuesIterator
	    while(itt.hasNext) {
	      var next:Node = itt.next
	      next.doTurn(currentTime,ticksPerStep)
	      turnClients.foreach(c=>c.doEachTurn(currentTime,ticksPerStep))
	    }
	    turnClients.foreach(c=>c.doTurn(currentTime,ticksPerStep))
	    currentTime += ticksPerStep
	  }
	  println("TurnbasedEngine stopped");
	}
	
	def addTurnClient(c:TurnClient) {
	  turnClients ::= c
	}
}

trait TurnClient {
  def doTurn(currentTime:Int, duration:Int)
  def doEachTurn(currentTime:Int, duration:Int){}
}