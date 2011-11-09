package examples.fluspreading

import graph.Node
import scala.util.Random
import org.ubiety.ubigraph.UbigraphClient

object InfectionStatus extends Enumeration {
    type InfectionStatus = Value
    val Susceptible, Infectious, Removed = Value
}

import InfectionStatus._

class PersonInfected extends Event

class Person(label:Int) extends Node(label) with TurnClient with RoundClient with EventClient{
	val RED = "#ff0000"
	val GREEN = "#00ff00"
	val BLUE = "#0000ff"
	val INFECTIONRATE:Float = 0.01f
	val INFECTION_DURATION = 1 * SimulationTime.TICKS_PER_HOUR
	
	var infectionrate:Float = INFECTIONRATE
	var status:InfectionStatus = Susceptible
	var infectionDuration:Int = 20 * SimulationTime.TICKS_PER_MINUTE
	
	var tempStatus:InfectionStatus = null
	
    override def visualize(ubigraphClient : UbigraphClient) = {
	  var color = status match {
	    case Susceptible => GREEN
	    case Infectious => RED
	    case Removed => BLUE
	  }
	  ubigraphClient.setVertexAttribute(label,"color",color)
	}
	
	def initVisualization(ubigraphClient : UbigraphClient) = ubigraphClient.newVertex(label)
	
	private def getNextStatus(duration:Int):InfectionStatus = {
		status match {
	    case Susceptible => return expose(duration)
	    case Infectious => 
	      if(infectionDuration>=0) {
	        infectionDuration -= duration
	        return status
	      }
	      else 
	        return InfectionStatus.Removed
	    case Removed => return status
	  }
	}
	
  override def doTurn(timestamp : Int, duration : Int) {
	  status = getNextStatus(duration)
  }
  
  override def doRound(timestamp:Int, duration: Int) {
    tempStatus = getNextStatus(duration)
  }
  
  override def nextRound { status = tempStatus }
	
	
  override def notify(event:Event){
    event match {
      case e:PersonInfected => status = getNextStatus( 1 * SimulationTime.TICKS_PER_MINUTE)
    }
  }
	
  private def expose(duration:Int) :InfectionStatus = {
    var infectedNeighbours = 1
    var exposurerate = Math.min(1, duration * 1.0f / SimulationTime.TICKS_PER_MINUTE)
    if(Random.nextFloat()> Math.pow(1-infectionrate*exposurerate,infectedNeighbours))
      return Infectious
    return status
  }
	
  def isInfected:Boolean = {
    if(status.equals(Infectious))
      return true
    return false
  }
}

