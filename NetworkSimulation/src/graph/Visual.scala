package graph

import org.ubiety.ubigraph.UbigraphClient

trait Visual {
  var ubigraphClient = new UbigraphClient
  def visualize : Unit
  def visualize(updateNode : Node => Unit) : Unit
  def visualize(traverser: Traversal, nodeID: Int, updateNode: Node => Unit) : Unit
  def removeVisualization : Unit
}
