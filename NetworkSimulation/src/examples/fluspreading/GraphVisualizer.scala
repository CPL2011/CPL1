package examples.fluspreading
import engine.RoundClient
import engine.TurnClient
import engine.EventClient
import engine.Event
import graph.Visualizable

class GraphVisualizer(graph:VisualGraph) extends TurnClient with EventClient with RoundClient{

  def doTurn(currentTime:Int, duration:Int) {
    graph.refreshVisualization(duration)
  }
  
  /*override def doEachTurn(currentTime:Int, duration:Int) {
    visualize
  }*/
  
  def doRound(timestamp:Int, duration:Int){
    graph.refreshVisualization(duration)
  }
  
  def nextRound(){
    //visualize "next round started"??
  }
  
  def notify(event:Event){
    graph.visualize
  }
}