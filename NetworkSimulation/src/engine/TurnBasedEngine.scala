package engine
import graph.Graph
import graph.Traversal
import graph.Node

/**
 * Round based engine will call doTurn on each Client with the current time
 */
class TurnBasedEngine(graph: Graph) extends SimulationEngine(graph){
	/**
	 * Each step the simulation will proceed a couple of steps.
	 * Increasing the step size will allow the clients to advance more per step.
	 * Resulting in a faster simulation as there will be less points in time to calculate.
	 * Reducing the step size on the other hand will increase the precision of each step.
	 */
	//
	var ticksPerStep:Int = 10
	
	//Keep a list of all additional TurnClients
    var turnClients:List[TurnClient] = List[TurnClient]()
  
    /**
     * Starts the engine
     */
	override def run() {
	  println("TurnBasedEngine started");
	  while(shouldContinue) {
	    doStep
	  }
	  println("TurnbasedEngine stopped");
	}
	
	override def doStep(){ 
	  var itt:Iterator[Node] = graph.nodes.valuesIterator
	    while(itt.hasNext) {
	      var next:Node = itt.next
	      next.doTurn(currentTime,ticksPerStep)
	      turnClients.foreach(c=>c.doEachTurn(currentTime,ticksPerStep))
	    }
	    turnClients.foreach(c=>c.doTurn(currentTime,ticksPerStep))
	    currentTime += ticksPerStep
	}
	
	def addTurnClient(c:TurnClient) {
	  turnClients ::= c
	}
}
	

/**
 * TurnClients can be added tot the TurnBasedEngine.
 * The engine will call the doTurn with the correct arguments
 */
trait TurnClient {
  def doTurn(currentTime:Int, duration:Int)
  def doEachTurn(currentTime:Int, duration:Int){}
}