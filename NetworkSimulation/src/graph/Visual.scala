package graph

import org.ubiety.ubigraph.UbigraphClient

trait Visual {
  var ubigraphClient = new UbigraphClient
  def visualize(updateNode : Node => Unit)
  def removeVisualization : Unit
}