package graph
import org.ubiety.ubigraph.UbigraphClient
import scala.util.Random
import scala.collection.mutable.HashMap
import examples.fluspreading.Event

class Node(val label: Int) {
  def this(n:scala.xml.Node) = this((n\"@label").text.toInt)
  var originatingEdges = new HashMap[(Int, Int), Edge]()
  var arrivingEdges = new HashMap[(Int, Int), Edge]()
 
  def addNeighbour(node : Node) = {
    var edge = new Edge(this, node)
    var newEntry = (((this.label, node.label), edge))
    originatingEdges += newEntry
    node.arrivingEdges += newEntry
  }
  
  def removeNeighbour(nodeID: Int) = {
    originatingEdges.get((label, nodeID)) match {
      case Some(edge) => {
        originatingEdges -= ((label, nodeID))
        edge.destination.arrivingEdges -= ((label, nodeID))
      }
      case None => {
       	System.err.println("The specified edge cannot be removed since it is no member of this graph")
      }
    } 
  }
  
  def remove = {
    arrivingEdges.values.foreach(e => e.source.removeNeighbour(label))
    //after this run arrivingEdges should be empty
    originatingEdges.values.foreach(e => removeNeighbour(e.destination.label))
    //after this run originatingEdges should be empty
  }
  
  def doTurn(timestamp:Int, duration:Int){}
  def doRound(timestamp:Int, duration:Int){}
  def notify(event:Event){}
  
//  def visualize(ubigraphClient : UbigraphClient) = {
//    if (ubigraphClient.newVertex(label) == -1) {
//      System.err.println("Node " + label + " has already been visualized")
//    }
//  }
  
//  def removeVisualization(ubigraphClient : UbigraphClient) = {
//    if (ubigraphClient.removeVertex(label) == -1) {
//      System.err.println("Node " + label + " has not yet been visualized")
//    }
//  }
  
 /// def toXML() = <Node label={label.toString()}>
  //    {originatingEdges.map(e=>e.toXML())}
  //  </Node>
  
  
}

trait Infectable{
  var infected:Boolean = false
  var dead:Boolean = false

  def isInfected(infectedNeighbours:Int,infectionChance:Float):Boolean = {
    if(dead) false
    else if(infected) true
    else{
      if(Random.nextFloat()> Math.pow(1-infectionChance,infectedNeighbours)){// if randomnumber > survivalchance(==1-infectionchance)^infectedneighbours
    	  infect()	
    	  true											// infection will take place
      }
      else false
     }
  }
  
  private def infect(){
    infected=true
  }
}

class InfectableNode(label:Int, var infectionChance:Float) extends Node(label) with Infectable{
  def this(n:scala.xml.Node) = {
    this((n\"@label").text.toInt,(n\"infectionChance").text.toFloat)
    dead = (n\"dead").text.toBoolean
    infected = (n\"infected").text.toBoolean
  }
  
  def infect(){
    isInfected(originatingEdges.size, infectionChance)
  }
  
  
  //override def toXML() = <Node label={label.toString()}>
  //    {originatingEdges.map(e=>e.toXML())}
  //    <infected>{infected}</infected>
  //    <infectionChance>{iChance}</infectionChance>
  //    <dead>{dead}</dead>
  //  </Node>
}
