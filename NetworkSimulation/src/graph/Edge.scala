package graph
import org.ubiety.ubigraph.UbigraphClient

class Edge(val source: Node, val destination: Node) {
  
//  def visualize(ubigraphClient : UbigraphClient) = {
//    if (ubigraphClient.newEdge(cantorPairing(source.label, destination.label), source.label, destination.label) == -1) {
//      System.err.println("The edge (" + source.label + "," + destination.label + ") has already been visualized")
//    }
//  }
//  
//  def removeVisualization(ubigraphClient : UbigraphClient) = {
//    if (ubigraphClient.removeEdge(cantorPairing(source.label, destination.label)) == -1) {
//      System.err.println("The edge (" + source.label + "," + destination.label + ") has not yet been visualized")
//    }
//  }
//  def cantorPairing(x: Int, y: Int) = (x+y)*(x+y+1)/2+y
//  def cantorUnpairing(z: Int) : (Int, Int) = {
//    val w = Math.floor((Math.sqrt(8*z+1)-1)/2).toInt
//    val t = (w*w+w)/2
//    val y = z-t
//    val x = w-y
//    (x,y)
//  }
  def toXML() = <Edge>
        <source>{source.label.toString()}</source>
        <destination>{destination.label.toString()}</destination>
      </Edge>
}