package examples.socialnetwork
import engine.TurnClient
import engine.RoundClient
import engine.EventClient
import engine.Event

class GraphVisualizer(graph : VisualGraph) extends TurnClient with RoundClient with EventClient {
  def doTurn(currentTime:Int, duration:Int) {
    graph.refreshVisualization(duration)
  }
  
  def doRound(timestamp:Int, duration:Int){
    graph.refreshVisualization(duration)
  }
  
  def nextRound {}
  
  def notify(event:Event){
    graph.visualize
  }
}