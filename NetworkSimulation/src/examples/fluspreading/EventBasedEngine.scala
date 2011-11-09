package examples.fluspreading

import graph.Graph
import scala.collection.mutable.Queue
import com.sun.xml.internal.ws.api.pipe.Engine

class EventBasedEngine(graph:Graph) extends SimulationEngine(graph) {

  var events:Queue[Event] = new Queue[Event]
  var clients:List[EventClient] = List[EventClient]()
  
  
  def run(){
    while(shouldContinue) {
      if(!events.isEmpty) {
        events.foreach(event => 
          clients.foreach(client=> client.notify(event) )
        )
      }
    }
  }
  
  def addEventClient(client:EventClient) {clients ::= client}
  
  def send(event:Event){ events.enqueue(event) }
}

abstract class Event {
  var timestamp:Int = 0
}

trait EventClient {
  private var engine:EventBasedEngine = null
  private def setEngine(e:EventBasedEngine){
    engine = e
  }
  def createEvent(event:Event){
    event.timestamp = engine.getCurrentTime
    engine.send(event)
  }
  def notify(event:Event)
}