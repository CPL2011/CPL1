package examples.fluspreading

import graph.Graph
import graph.Visualizable
import graph.Node
import engine.SimulationTime
import graph.persistence.Db4oPersistence
import examples.persistence.Db4oGraph

/**
 * A graph class that supports loading a graph from storage as well as the functionality
 * to give a visual representation of itself
 */
class VisualGraph(p:String) extends Db4oGraph(p) with Visualizable with Db4oPersistence{
  val REFRESH_RATE = 2 * SimulationTime.TICKS_PER_MINUTE
  var elapsedTime = REFRESH_RATE + 1
  var refreshRate:Int = REFRESH_RATE
  
  /**
   * Loads a graph object from the path given by the class parameter p.
   * If no graph is present, null gets returned
   */
  def loadGraph():VisualGraph = load() match{
    case Some(t:VisualGraph)=>t
    case _=>null
  }
  
  /**
   * sets the refresh rate at the given Integer-value.
   */
  def setRefreshRate(rr:Int) {refreshRate = rr}
  
  /**
   * Move the elapsed time forward with the given increment.
   * If the elapsed time exceeds the period between each refresh,
   * the graph gets re-visualized.
   * @param duration : The period of time by which the visualization should be
   * advanced.
   */
  def refreshVisualization(duration:Int){
    if(elapsedTime > REFRESH_RATE) {
      visualize(updateVisualization)
      elapsedTime = 0
    }
    elapsedTime += duration
  }
  
  /**
   * If the given node is an instance of the Person class,
   * its visualization gets updated.
   * @param n : a instance of class Node, or of a class extending Node
   */
  def updateVisualization(n:Node){
    n match{
      case p:Person => p.updateVisualization(ubigraphClient)
      case _ =>
    }
  }
  
  /**
   * A def overridden from Visualizable, which will apply the def updateVisualization
   * on each node when the graph gets traversed during visualization. 
   */
  override def visualize {
    visualize(updateVisualization)
  }
}