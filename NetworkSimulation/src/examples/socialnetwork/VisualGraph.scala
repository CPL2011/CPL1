package examples.socialnetwork
import graph.Graph
import graph.Visualizable
import engine.SimulationTime
import graph.Node
import examples.persistence.Db4oGraph
import graph.persistence.Db4oPersistence

/**
 * A graph class that supports loading a graph from storage as well as the functionality
 * to give a visual representation of itself, representing the social network graph.
 */
class VisualGraph(p:String) extends Db4oGraph(p) with Visualizable with Db4oPersistence {
  var refreshRate:Int = 10 * SimulationTime.TICKS_PER_SECOND
  var elapsedTime = refreshRate+1
  
  /**
   * Loads a graph object from the path given by the class parameter p.
   * If no graph is present, null gets returned
   */
  def loadGraph():VisualGraph = load() match{
    case Some(t:VisualGraph)=>t
    case _=>null
  }
  
  /**
   * Move the elapsed time forward with the given increment.
   * If the elapsed time exceeds the period between each refresh,
   * the graph gets re-visualized.
   * @param duration : The period of time by which the visualization should be
   * advanced.
   */
  def refreshVisualization(duration:Int){
    if(elapsedTime > refreshRate) {
      visualize
      elapsedTime = 0
    }
    elapsedTime += duration
  }
}