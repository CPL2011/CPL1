package examples.fluspreading

import graph.Node
import scala.util.Random
import org.ubiety.ubigraph.UbigraphClient

object InfectionStatus extends Enumeration {
    type InfectionStatus = Value
    val Susceptible, Infectious, Removed = Value
}

import InfectionStatus._

class PersonInfected(val person:Person) extends Event{
	var name = "PersonInfected"
}

class PersonDead(val person:Person, time:Int) extends TimeBasedEvent(time){
  var name = "PersonDead"
}

class Person(label:Int) extends Node(label) with TurnClient with RoundClient with EventClient{
	val RED = "#ff0000"
	val GREEN = "#00ff00"
	val BLUE = "#0000ff"
	val INFECTIONRATE:Float = 0.01f
	val INFECTION_DURATION = 1 * SimulationTime.TICKS_PER_HOUR
	
	var infectionrate:Float = INFECTIONRATE
	var status:InfectionStatus = Susceptible
	var infectionDuration:Int = 20 * SimulationTime.TICKS_PER_MINUTE
	var needsVisualization = true;
	
	var tempStatus:InfectionStatus = null
	
    def refreshVisualization(ubigraphClient : UbigraphClient) = {
	  if(needsVisualization){
	    needsVisualization = false
		  var color = status match {
		    case Susceptible => GREEN
		    case Infectious => RED
		    case Removed => BLUE
		  }
		  ubigraphClient.setVertexAttribute(label,"color",color)
	  }
	}
	
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
	  updateStatus(getNextStatus(duration))
  }
  
  override def doRound(timestamp:Int, duration: Int) {
    tempStatus = getNextStatus(duration)
  }
  
  override def nextRound { updateStatus( tempStatus ) }
	
	
  override def notify(event:Event){
    event match {
      case e:TriggerEvent => createEvent(new PersonInfected(this))
      case e:PersonInfected =>
        if( e.person.equals(this) )
        	updateStatus(InfectionStatus.Infectious)
        	createEvent(new PersonDead(this, e.getTimeStamp + 2000))
        //status = getNextStatus( 1 * SimulationTime.TICKS_PER_MINUTE)
      case e:PersonDead => 
        if(e.person.equals(this))
          updateStatus(InfectionStatus.Removed)
      case _ => println("Unknown Event at Person" + event.name);
    }
  }
	
  private def updateStatus(s:InfectionStatus){
    if(s.equals(status)) return
    
    status = s
    needsVisualization = true;
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

