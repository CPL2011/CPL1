package engine

import graph.Graph
import graph.Node
import com.sun.xml.internal.ws.api.pipe.Engine
import scala.util.Random
import scala.collection.mutable.PriorityQueue

class EventBasedEngine(graph:Graph) extends SimulationEngine(graph) {
  
  var events:PriorityQueue[Event] = new PriorityQueue[Event]()
  var clients:List[EventClient] = List[EventClient]()
  
  
  def run(){
    println("EventEngine started");
        while(shouldContinue && !events.isEmpty) {
          var event = events.dequeue()
          currentTime = event.getTimeStamp
          var itt:Iterator[Node] = graph.nodes.valuesIterator
          	while(itt.hasNext) {
          	  var n:Node = itt.next()
          	  n.engine = this
          	  n.notify(event)
          	}
          clients.foreach(client=> client.notify(event) )
        }
    println("EventEngine stopped");
  }
  
  def addEventClient(client:EventClient) : Boolean = {
    if(client.engine != null && !this.equals(client.engine))
      throw new Exception("Client is already added to another engine!! Could not add client");
    else
    {
    	if(this.equals(client.engine))
    		return false
    	
    	clients ::= client
    	client.engine = this
    	return true
    }
  }
  
  def send(event:Event){
    event.setTimestamp(this)
	event.prepareOrdening
    events.enqueue(event)
    }
}

abstract class Event extends Ordered[Event] {
  protected var timestamp:Int = -1
  var name:String
  //Specify the delay between creating and receiving this Event
  var delay:Int = 100
  private var engine:EventBasedEngine = null
  private var ordening:Int = -1
  
  def compare(e:Event) = e.ordening - ordening
  
  //EventClients normaly have no direct access to the engine so they cannot easily abuse this method
  //as this method is meant to be used only by the EventClient trait.
  def setTimestamp(engine:EventBasedEngine){
      if(timestamp < 0)
    	timestamp = engine.getCurrentTime + delay
    else
    	throw new Exception("Timestamp modified???")
  }
  
  def prepareOrdening {
    if(ordening < 0)
      if(timestamp < 0)
        throw new Exception("TimeStamp not set. Cannot prepare Ordening. \nIf you use the EventClient.createEvent method the Event will be ordened.")
      else
    	ordening = timestamp*100 + Random.nextInt(100)
    else
    	throw new Exception("Event is already send to the engine. Cannot send it twice")
  }
  
  //Send back an unmodifiable timeStamp of this event.
  def getTimeStamp:Int = { val t = timestamp ; return t }
}

abstract class TimeBasedEvent(val time:Int) extends Event{
  timestamp = time
  if(!(time > 0))
    throw new Exception("Time must be greater then zero")
	
	override def setTimestamp(engine:EventBasedEngine) { }
}

trait EventClient {
  var engine:EventBasedEngine = null
  
  //Use this method to send events to the engine and respectively to all EventClients
  def createEvent(event:Event){
    engine.send(event)
  }
  
  //All occured events will be send to the EventClient.
  def notify(event:Event)
}