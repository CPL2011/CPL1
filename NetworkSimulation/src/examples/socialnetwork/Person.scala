package examples.socialnetwork

import graph.Node
import engine.TurnClient
import engine.EventClient
import engine.RoundClient
import engine.Event
import scala.collection.mutable.HashMap
import graph.Edge

class Person(label: Int) extends Node(label) with TurnClient with RoundClient with EventClient {
  val FRIENDLY_RATE = 0.001f
  
  var friendlyRate = FRIENDLY_RATE
  def friends = getNeighbours
  var futureLinks = new HashMap[Int, Edge]
  
  var needsVisualization = true
  
  def addFriend(friend: Person) = {
    addNeighbour(friend)
    friend.addNeighbour(this)
  }
  
//  private def gatherFutureLinks(duration: Int)
  
  override def doTurn(timestamp: Int, duration: Int) {
    
  }
  
  override def doRound(timestamp: Int, duration: Int) {
    
  }
  
  override def nextRound {}
  
  override def notify(event: Event) {
    
  }
}