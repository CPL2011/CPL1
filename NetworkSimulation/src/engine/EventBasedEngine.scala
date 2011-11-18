package engine

import graph.Graph
import graph.Node
import com.sun.xml.internal.ws.api.pipe.Engine
import scala.util.Random
import scala.collection.mutable.PriorityQueue

/**
 * EventBasedEngine will handle the events.
 * All Clients receive all Events in a correct ordre of time.
 * If Events have occured before current simulation time a warning is printed.
 */
class EventBasedEngine(graph:Graph) extends SimulationEngine(graph) {
  private val randomization = 100
  var events:PriorityQueue[Event] = new PriorityQueue[Event]()
  var clients:List[EventClient] = List[EventClient]()
  
  /**
   * Start the engine
   */
  def run(){
    println("EventEngine started");
        while(shouldContinue && !events.isEmpty) {
          doStep()
          
        }
    println("EventEngine stopped");
  }
  override def doStep(){
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
  /**
   * Add clients to the engine
   * @param client : Is the event client that now can send and receive Events.
   */
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
  
  /**
   * Adds an event to the Queue of the engine.
   * This event will be send to all clients after all previous events are send.
   * If multiple events occur on the same time the ordre in wich the events occur will be randomized.
   * @param event : Event that should be send to all clients
   */
  def send(event:Event){
    event.setTimestamp(this)
	event.prepareOrdening(randomization)
    events.enqueue(event)
    }
}

/**
 * You can extend the Event class to create Events
 * The timestamp of the event will be set to the currentTime of the engine plus the delay.
 * If you would like to set the timestamp of the event manualy pleas use TimeBasedEvent class or override the setTimestamp method.
 */
abstract class Event extends Ordered[Event] {
  protected var timestamp:Int = -1
  var name:String
  //Specify the delay between creating and receiving this Event
  var delay:Int = 100
  private var engine:EventBasedEngine = null
  private var ordening:Int = -1
  
  final def compare(e:Event) = e.ordening - ordening
  
  /**
   * This is a library method only. The timestamp will be set automaticaly when the Event is send.
   * You can override this method to set the timestamp of this event manualy or you can use the TimeBasedEvent class.
   * @param engine : EventBasedEngine that will handle this event.
   */
  def setTimestamp(engine:EventBasedEngine){
      if(timestamp < 0)
    	timestamp = engine.getCurrentTime + delay
    else
    	throw new Exception("Timestamp of event is Modified!\n Pleas use the TimeBasedEvent class if you would like to set timestamp manualy.")
  }
  
  /**
   * This is a library method only. The ordening value is the timestamp with some randomization.
   */
  def prepareOrdening(randomization:Int) {
    if(ordening < 0)
      if(timestamp < 0)
        throw new Exception("TimeStamp not set. Cannot prepare Ordening. \nIf you use the EventClient.createEvent method the Event will be ordened.")
      else
    	ordening = timestamp*randomization + Random.nextInt(randomization)
    else
    	throw new Exception("Event is already send to the engine. Cannot send it twice")
  }
  
  //Send back an unmodifiable timeStamp of this event.
  final def getTimeStamp:Int = { val t = timestamp ; return t }
}

/**
 * This class can be used if you would like to create Events with specific timeStamps.
 * @param time : Time at wich the event should occur.
 */
abstract class TimeBasedEvent(val time:Int) extends Event{
  timestamp = time
  if(!(time > 0))
    throw new Exception("Time of Event \"" + name + "\" must be greater then zero")
	
	override def setTimestamp(engine:EventBasedEngine) { 
    if(this.timestamp < engine.getCurrentTime)
      println("Warning!: The Event \"" + name + "\" occured before current simulation time" );
  }
}

/**
 * Event clients can be added to the EventEngine in order to be notifyd of events.
 * To send events clients can use the createEvent(Event) method wich is already defined.
 */
trait EventClient {
  /**
   * the engine is automaticaly added after the client is added to the engine
   */
  var engine:EventBasedEngine = null
  
  /**
   * Use this method to send events to the engine and respectively to all EventClients
   * @param event : Event that has to be send to the EventEngine of this Client
   */
  final def createEvent(event:Event){
    engine.send(event)
  }
  
  //All occured events will be send to the EventClient.
  def notify(event:Event)
}