package graph
import org.ubiety.ubigraph.UbigraphClient
import scala.util.Random
import scala.collection.mutable.HashMap

class Node(label: Int) {
  def this(n:scala.xml.Node) = this((n\"@label").text.toInt)
  var originatingEdges = new HashMap[(Int, Int), Edge]()
  var arrivingEdges = new HashMap[(Int, Int), Edge]()
 
  def getLabel : Int = label
  def getOriginatingEdges = originatingEdges
  def addNeighbour(node : Node) = {
    var edge = new Edge(this, node)
    var newEntry = (((this.getLabel, node.getLabel), edge))
    originatingEdges += newEntry
    node.arrivingEdges += newEntry
  }
  
  def removeNeighbour(nodeID: Int) = {
    originatingEdges.get((label, nodeID)) match {
      case Some(edge) => {
        originatingEdges -= ((label, nodeID))
        edge.getDestination().arrivingEdges -= ((label, nodeID))
      }
      case None => {
       	System.err.println("The specified edge cannot be removed since it is no member of this graph")
      }
    } 
  }
  
  def remove = {
    arrivingEdges.values.foreach(e => e.getSource().removeNeighbour(getLabel))
    //after this run arrivingEdges should be empty
    originatingEdges.values.foreach(e => removeNeighbour(e.getDestination.getLabel))
    //after this run originatingEdges should be empty
  }
  
  def visualize(ubigraphClient : UbigraphClient) = ubigraphClient.newVertex(label)
 /// def toXML() = <Node label={label.toString()}>
  //    {originatingEdges.map(e=>e.toXML())}
  //  </Node>
  
  
}

trait Infectable{
  var infected:Boolean = false
  var dead:Boolean = false
  
  def isDead():Boolean = dead
  

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

class InfectableNode(label:Int,infectionChance:Float) extends Node(label) with Infectable{
  def this(n:scala.xml.Node) = {
    this((n\"@label").text.toInt,(n\"infectionChance").text.toFloat)
    dead = (n\"dead").text.toBoolean
    infected = (n\"infected").text.toBoolean
  }
  var iChance = infectionChance;
  
  def getInfectionChance():Float = iChance
  
  def setInfectionChance(f:Float):Unit =
  	{iChance = f}
  
  def infect(){
    isInfected(originatingEdges.size,getInfectionChance())
  }
  //override def toXML() = <Node label={label.toString()}>
  //    {originatingEdges.map(e=>e.toXML())}
  //    <infected>{infected}</infected>
  //    <infectionChance>{iChance}</infectionChance>
  //    <dead>{dead}</dead>
  //  </Node>
}
