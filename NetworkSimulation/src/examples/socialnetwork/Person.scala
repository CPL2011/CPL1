package examples.socialnetwork

import graph.Node
import engine.TurnClient
import engine.EventClient
import engine.RoundClient
import engine.Event
import graph.Edge

class Person(label: Int) extends Node(label) with TurnClient with RoundClient with EventClient {
  val FRIENDLY_RATE = 0.0001f
  val FRIENDS_SATURATION = 50
  
  var friendlyRate = FRIENDLY_RATE
  var friendsSaturation = FRIENDS_SATURATION
  def friends = getNeighbours
  var futureFriends: List[Person] = Nil
  
  var needsVisualization = true
  
  def addFriend(friend: Person) = {
    addNeighbour(friend)
    friend.addNeighbour(this)
  }
  
  private def gatherFutureFriends(duration: Int) {
    if(friends.length > friendsSaturation)
      return
    friends.foreach(friend =>
      friend.getNeighbours.foreach(friendOfaFriend =>
        friendOfaFriend match {
          case p:Person => if(!p.equals(this) && !friends.contains(p) && Math.random < friendlyRate*duration)
            futureFriends ::= p
          case _ =>
        }))
  }
  
  override def doTurn(timestamp: Int, duration: Int) {
    gatherFutureFriends(duration)
    futureFriends.foreach(friend => addFriend(friend))
    futureFriends = Nil
  }
  
  override def doRound(timestamp: Int, duration: Int) = gatherFutureFriends(duration)
  
  override def nextRound {
    futureFriends.foreach(friend => addFriend(friend))
    futureFriends = Nil
  }
  
  override def notify(event: Event) {
    event match {
      case e:TriggerEvent => createEvent(new MeetFriendsEvent(this))
      case e:MeetFriendsEvent =>
        if(e.person.equals(this) && friends.length < friendsSaturation) {
          gatherFutureFriends(1)
          futureFriends.foreach(friend => addFriend(friend))
          futureFriends = Nil
          createEvent(new MeetFriendsEvent(this))
        }
      case _ => println("Unknown Event at Person" + event.name)
    }
  }
}

class MeetFriendsEvent(val person: Person) extends Event {
  var name = "Meet friends"
}