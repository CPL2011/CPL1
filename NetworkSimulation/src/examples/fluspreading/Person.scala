package examples.fluspreading

import graph.Node
import scala.util.Random
import org.ubiety.ubigraph.UbigraphClient

object InfectionStatus extends Enumeration {
    type InfectionStatus = Value
    val Susceptible, Infectious, Removed = Value
}

import InfectionStatus._

class Person(label:Int) extends Node(label) {
	val RED = "#ff0000"
	val GREEN = "#00ff00"
	val BLUE = "#0000ff"
	val INFECTIONRATE:Float = 0.01f
	val INFECTION_DURATION = 1 * SimulationTime.TICKS_PER_HOUR
	
	var infectionrate:Float = INFECTIONRATE
	var status:InfectionStatus = Susceptible
	var infectionDuration:Int = 20 * SimulationTime.TICKS_PER_MINUTE
	
    override def visualize(ubigraphClient : UbigraphClient) = {
	  var color = status match {
	    case Susceptible => GREEN
	    case Infectious => RED
	    case Removed => BLUE
	  }
	  ubigraphClient.setVertexAttribute(label,"color",color)
	}
	
	def initVisualization(ubigraphClient : UbigraphClient) = ubigraphClient.newVertex(label)
	
  override def step(timestamp : Int, duration : Int) {
    status match {
	    case Susceptible => expose(duration)
	    case Infectious => 
	      if(infectionDuration>=0)
	        infectionDuration -= duration 
	      else 
	        status = InfectionStatus.Removed
	    case Removed => return
	  }
    return
  }
	
  private def expose(duration:Int) {
    var infectedNeighbours = 1
    var exposurerate = Math.min(1, duration * 1.0f / SimulationTime.TICKS_PER_MINUTE)
    if(Random.nextFloat()> Math.pow(1-infectionrate*exposurerate,infectedNeighbours))
      status = Infectious
  }
	
  def isInfected:Boolean = {
    if(status.equals(Infectious))
      return true
    return false
  }
}

