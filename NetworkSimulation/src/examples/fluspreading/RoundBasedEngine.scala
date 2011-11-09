package examples.fluspreading

import graph.Graph

class RoundBasedEngine(graph:Graph) extends SimulationEngine(graph) {

  def run(): Unit = {  }

}

trait RoundClient{
  def doRound(timestamp:Int, duration:Int)
  def nextRound
}