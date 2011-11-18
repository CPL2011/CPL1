package examples.socialnetwork

import graph.Node
import engine.TurnClient
import engine.EventClient
import engine.RoundClient
import engine.Event
import graph.Edge

/**
 * This class represents a person in a social network.
 */
class Person(label: Int) extends Node(label) with TurnClient with RoundClient with EventClient {
  val FRIENDLY_RATE = 0.001f
  val FRIENDS_SATURATION = 50
  
  var friendlyRate = FRIENDLY_RATE
  var friendsSaturation = FRIENDS_SATURATION
  def friends = getNeighbours
  var futureFriends: List[Person] = Nil
  
  /**
   * Add a friend, so that this person and the friend are bidirectionally connected.
   */
  def addFriend(friend: Person) = {
    addNeighbour(friend)
    friend.addNeighbour(this)
  }
  
  /**
   * Gather friends of friends that will become this person's friends in the future.
   */
  private def gatherFutureFriends(duration: Int) {
    if(friends.length > friendsSaturation)
      return
    friends.foreach(friend =>
      friend.getNeighbours.foreach(friendOfaFriend =>
        friendOfaFriend match {
          case p:Person => if(!p.equals(this) && !friends.contains(p) && Math.random > Math.pow(1-friendlyRate,duration))
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
        }
      case _ => println("Unknown Event at Person" + event.name)
    }
  }
}

class MeetFriendsEvent(val person: Person) extends Event {
  var name = "Meet friends"
}