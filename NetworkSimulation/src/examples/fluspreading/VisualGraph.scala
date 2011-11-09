package examples.fluspreading

import graph.Graph
import org.ubiety.ubigraph.UbigraphClient

class VisualGraph(ubiClient2:UbigraphClient) extends Graph with TurnClient with EventClient with RoundClient {
  val REFRESH_RATE = 2 * SimulationTime.TICKS_PER_MINUTE
  var elapsedTime = REFRESH_RATE + 1
  var refreshRate:Int = REFRESH_RATE
  
  ubigraphClient = ubiClient2
  
  def setRefreshRate(rr:Int) {refreshRate = rr}
  
  private def refreshVisualization(duration:Int){
    if(elapsedTime > REFRESH_RATE) {
      visualize
      elapsedTime = 0
    }
    elapsedTime += duration
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
    visualize
  }
}