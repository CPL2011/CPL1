package graph
import org.ubiety.ubigraph.UbigraphClient
import scala.util.Random

class Node(label: Int) {
  def this(n:scala.xml.Node) = this((n\"@label").text.toInt)
  var connectedEdges: List[Edge] = Nil
  //If you store a list of Nodes the Node has to do some kind of Query to find the Edge that connects this neigbour.
  //Shouldn't it be better to keep a list of Edges? Query's are verry cpu intensive.
  //---> Since the edges were stored in a hashmap and you'd know the labels of the source and dest node (which serve as
  //---> the key to the map, retrieval of the edge would only require constant time. Nevertheless, storing edges 
  //---> is indeed more logical. So altough it does create the awkward situation were a node 
  //---> can access itself through each edge it stores, I've made the change.
  
  def getLabel : Int = label
  def addConnectedEdge(edge : Edge) = connectedEdges = edge :: connectedEdges
  def removeConnectedEdge(edge: Edge) = connectedEdges diff List(edge)
  def getConnectedEdges : List[Edge] = connectedEdges
  def visualize(ubigraphClient : UbigraphClient) = ubigraphClient.newVertex(label)
  
  def toXML() = <Node label={label.toString()}>
      {connectedEdges.map(e=>e.toXML())}
    </Node>
  
  
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
    isInfected(getConnectedEdges.size,getInfectionChance())
  }
  override def toXML() = <Node label={label.toString()}>
      {connectedEdges.map(e=>e.toXML())}
      <infected>{infected}</infected>
      <infectionChance>{iChance}</infectionChance>
      <dead>{dead}</dead>
    </Node>
}
