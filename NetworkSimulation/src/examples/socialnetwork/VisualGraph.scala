package examples.socialnetwork
import graph.Graph
import graph.Visualizable
import engine.SimulationTime
import graph.Node
import examples.persistence.Db4oGraph
import graph.persistence.Db4oPersistence

class VisualGraph(p:String) extends Db4oGraph(p) with Visualizable with Db4oPersistence {
  var refreshRate:Int = 10 * SimulationTime.TICKS_PER_SECOND
  var elapsedTime = refreshRate+1
  
  def loadGraph():VisualGraph = load() match{
    case Some(t:VisualGraph)=>t
    case _=>null
  }
  
  def refreshVisualization(duration:Int){
    if(elapsedTime > refreshRate) {
      visualize
      elapsedTime = 0
    }
    elapsedTime += duration
  }
}