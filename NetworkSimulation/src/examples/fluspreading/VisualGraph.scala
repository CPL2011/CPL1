package examples.fluspreading

import graph.Graph
import graph.Visualizable
import graph.Node
import engine.SimulationTime
import graph.persistence.Db4oPersistence
import examples.persistence.Db4oGraph

class VisualGraph(p:String) extends Db4oGraph(p) with Visualizable with Db4oPersistence{
  val REFRESH_RATE = 2 * SimulationTime.TICKS_PER_MINUTE
  var elapsedTime = REFRESH_RATE + 1
  var refreshRate:Int = REFRESH_RATE
  
  def loadGraph():VisualGraph = load() match{
    case Some(t:VisualGraph)=>t
    case _=>null
  }
  
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
      case p:Person => p.updateVisualization(ubigraphClient)
      case _ =>
    }
  }
  
  override def visualize {
    visualize(updateVisualization)
  }
}