package graph
import scala.util.Random
import scala.collection.mutable.HashMap
import engine.Event
import engine.TurnClient
import engine.EventClient
import engine.RoundClient

class Node(val label: Int) extends TurnClient with RoundClient with EventClient {
  
  var originatingEdges = new HashMap[Int, Edge]()
  var arrivingEdges = new HashMap[Int, Edge]()
  
  def getEdgeTo(destination : Int) : Option[Edge] = originatingEdges.get(destination)
  
  def getEdgeFrom(source : Int) : Option[Edge] = arrivingEdges.get(source)
  
  def getParents : List[Node] = {
    var parents : List[Node] = Nil
    arrivingEdges.values.foreach(e => parents ::= e.source)
    parents
  }
  
  def getChildren : List[Node] = {
    var children : List[Node] = Nil
    originatingEdges.values.foreach(e => children ::= e.destination)
    children
  }
  
  def getNeighbours : List[Node] = getParents ++ getChildren

  def addOriginatingEdge(edge: Edge) = { 
    if (!originatingEdges.contains((edge.destination.label))) {
      originatingEdges += ((edge.destination.label, edge))
    } else System.err.println("The edge (" + edge.source.label + "," + edge.destination.label + ") has already been added to this graph")
  }
  
  def addArrivingEdge(edge: Edge) = { 
    if (!arrivingEdges.contains((edge.source.label))) {
      arrivingEdges += ((edge.source.label, edge))
    } else System.err.println("The edge (" + edge.source.label + "," + edge.destination.label + ") has already been added to this graph")
  }
  
  def addNeighbour(node : Node) = {
    var edge = new Edge(this, node)
    originatingEdges += ((node.label, edge))
    node.arrivingEdges += ((label, edge))
  }
  
  def removeNeighbour(nodeID: Int) = {
    originatingEdges.get(nodeID) match {
      case Some(edge) => {
        originatingEdges -= (nodeID)
        edge.destination.arrivingEdges -= (label)
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
  def nextRound {}
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
