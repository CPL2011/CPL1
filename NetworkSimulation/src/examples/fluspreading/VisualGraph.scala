package examples.fluspreading

import graph.Graph
import org.ubiety.ubigraph.UbigraphClient
import graph.VisualisableGraph
import graph.Node

class VisualGraph(ubiClient2:UbigraphClient) extends VisualisableGraph with TurnClient with EventClient with RoundClient {
  val REFRESH_RATE = 2 * SimulationTime.TICKS_PER_MINUTE
  var elapsedTime = REFRESH_RATE + 1
  var refreshRate:Int = REFRESH_RATE
  
  def setRefreshRate(rr:Int) {refreshRate = rr}
  ubigraphClient = ubiClient2
  
  private def refreshVisualization(duration:Int){
    if(elapsedTime > REFRESH_RATE) {
      visualize(refreshVisualization)
      elapsedTime = 0
    }
    elapsedTime += duration
  }
  
  def refreshVisualization(n:Node){
    n match{
      case p:Person => p.refreshVisualization(ubigraphClient)
      case _ =>
    }
  }
  
  def doTurn(currentTime:Int, duration:Int) {
    refreshVisualization(duration)
  }
  
  //Speciaal voor steven :p
  /*override def doEachTurn(currentTime:Int, duration:Int) {
    visualize
  }*/
  
  def doRound(timestamp:Int, duration:Int){
    refreshVisualization(duration)
  }
  
  def nextRound(){
    //visualize "next round started"??
  }
  
  def notify(event:Event){
    visualize(refreshVisualization)
  }
}