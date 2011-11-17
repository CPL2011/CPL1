package examples.socialnetwork
import graph.Graph
import graph.Visualizable
import engine.SimulationTime
import graph.Node

class VisualGraph extends Graph with Visualizable {
  val REFRESH_RATE = 2 * SimulationTime.TICKS_PER_MINUTE
  var elapsedTime = REFRESH_RATE + 1
  var refreshRate:Int = REFRESH_RATE
  
  def setRefreshRate(rr:Int) {refreshRate = rr}
  
  def refreshVisualization(duration:Int){
    if(elapsedTime > REFRESH_RATE) {
      visualize(updateVisualization)
      elapsedTime = 0
    }
    elapsedTime += duration
  }
  
  def updateVisualization(n:Node){
    n match{
//      case p:Person => p.updateVisualization(ubigraphClient)
      case _ =>
    }
  }
  
  override def visualize {
    visualize(updateVisualization)
  }
}