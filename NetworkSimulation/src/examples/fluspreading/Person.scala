package examples.fluspreading

import graph.Node
import scala.util.Random
import org.ubiety.ubigraph.UbigraphClient
import engine.Event
import engine.TimeBasedEvent
import engine.TurnClient
import engine.RoundClient
import engine.EventClient
import engine.SimulationTime

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
	val INFECTIONRATE:Float = 0.1f
	val INFECTION_DURATION = 1 * SimulationTime.TICKS_PER_HOUR
	
	var infectionrate:Float = INFECTIONRATE
	var status:InfectionStatus = Susceptible
	var infectionDuration:Int = 20 * SimulationTime.TICKS_PER_MINUTE
	var needsVisualization = true;
	
	var tempStatus:InfectionStatus = null
	
    def updateVisualization(ubigraphClient : UbigraphClient) = {
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
	
  //Some lame Event Based Example but it works as expected
  //All person get infected by the same TriggerEvent so on exactly the same time
  //To simulate this we added randomization to randomize the order in case
  //multiple events occur on the same time.
  //Perhaps we should randomize the order in wich the EventClients receive the Events as well.
  override def notify(event:Event){
    event match {
      case e:TriggerEvent => createEvent(new PersonInfected(this))
      case e:PersonInfected =>
        if( e.person.equals(this) )
        	updateStatus(InfectionStatus.Infectious)
        	createEvent(new PersonDead(this, e.getTimeStamp + SimulationTime.TICKS_PER_MINUTE * 20))
        //status = getNextStatus( 1 * SimulationTime.TICKS_PER_MINUTE)
      case e:PersonDead => 
        if(e.person.equals(this))
          updateStatus(InfectionStatus.Removed)
      case _ => println("Unknown Event at Person" + event.name);
    }
  }
	
  def updateStatus(s:InfectionStatus){
    if(s.equals(status)) return
    status = s
    needsVisualization = true;
  }
  
  private def expose(duration:Int) :InfectionStatus = {
    var infectedNeighbours = getNeighbours.count(isInfected)
    var exposurerate = Math.min(1, duration * 1.0f / SimulationTime.TICKS_PER_MINUTE)
    if(Random.nextFloat()> Math.pow(1-infectionrate*exposurerate,infectedNeighbours))
      return Infectious
    return status
  }
  
  private def isInfected(node:Node):Boolean = {
    node match{
      case p:Person => p.isInfected
      case _ => false
    }
  }
	
  def isInfected:Boolean = {
    if(status.equals(Infectious))
      return true
    return false
  }
}

