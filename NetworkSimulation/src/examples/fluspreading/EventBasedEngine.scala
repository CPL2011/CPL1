package examples.fluspreading

import graph.Graph
import com.sun.xml.internal.ws.api.pipe.Engine
import scala.util.Random
import scala.collection.mutable.PriorityQueue

class EventBasedEngine(graph:Graph) extends SimulationEngine(graph) {

  var events:PriorityQueue[Event] = new PriorityQueue[Event]()
  var clients:List[EventClient] = List[EventClient]()
  
  
  def run(){
    events.enqueue(new SimulationStarted)
        while(shouldContinue && !events.isEmpty) {
          var event = events.dequeue() 
          currentTime = event.timestamp
          clients.foreach(client=> client.notify(event) )
        }
  }
  
  def addEventClient(client:EventClient) {clients ::= client}
  
  def send(event:Event){ events.enqueue(event) }
}

abstract class Event {
  var timestamp:Int = 0
  
  
}

class SimulationStarted extends Event{}

trait EventClient {
  private var engine:EventBasedEngine = null
  private def setEngine(e:EventBasedEngine){
    engine = e
  }
  def createEvent(event:Event){
    event.timestamp = (engine.getCurrentTime + Random.nextInt(100))
    engine.send(event)
  }
  def notify(event:Event)
}