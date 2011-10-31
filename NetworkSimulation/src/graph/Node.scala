package graph
import org.ubiety.ubigraph.UbigraphClient
import scala.util.Random

class Node(label: Int) {
  var neighbours: List[Node] = Nil
  //If you store a list of Nodes the Node has to do some kind of Query to find the Edge that connects this neigbour.
  //Shouldn't it be better to keep a list of Edges? Query's are verry cpu intensive.
  
  def getLabel : Int = label
  def addNeighbour(node : Node) = neighbours = node :: neighbours
  def removeNeighbour(node : Node) = neighbours diff List(node)
  def getNeighbours : List[Node] = neighbours
  def visualize(ubigraphClient : UbigraphClient) = ubigraphClient.newVertex(label)
  
  
}

trait Infectable{
  var infected:Boolean = false
  val dead:Boolean = false
  
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

class InfecableNode(label:Int,infectionChance:Float) extends Node(label) with Infectable{
  
  var iChance = infectionChance;
  
  def getInfectionChance():Float = iChance
  
  def setInfectionChance(f:Float):Unit =
  	{iChance = f}
  
  def infect(){
    isInfected(neighbours.size,getInfectionChance())
  }
  
}
