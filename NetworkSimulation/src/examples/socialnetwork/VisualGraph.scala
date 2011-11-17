package examples.socialnetwork
import graph.Graph
import graph.Visualizable
import engine.SimulationTime
import graph.Node

class VisualGraph extends Graph with Visualizable {
  var refreshRate:Int = 10 * SimulationTime.TICKS_PER_SECOND
  var elapsedTime = refreshRate+1
  
  def refreshVisualization(duration:Int){
    if(elapsedTime > refreshRate) {
      visualize
      elapsedTime = 0
    }
    elapsedTime += duration
  }
}