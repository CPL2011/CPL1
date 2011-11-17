package graph
import scala.util.Random
import scala.collection.mutable.HashMap
import engine.Event
import engine.TurnClient
import engine.EventClient
import engine.RoundClient
/**
  * A node represents an actor in a network 
  * 
  * @param label A unique integer that corresponds to this Node
  */
class Node(val label: Int) extends TurnClient with RoundClient with EventClient {
  /**
    * A Map that contains the keys and edges that start in this node
    * The key is a unique integer
    */
  var originatingEdges = new HashMap[Int, Edge]()
    /**
    * A Map that contains the keys and edges that end in this node
    * The key is a unique integer
    */
  var arrivingEdges = new HashMap[Int, Edge]()
  
  /** Get an edge that starts in this node that corresponds to the given key
    * @param destination the key is used to retrieve an edge
    * @return An Option[Edge] corresponding to the given key
    */
  def getEdgeTo(destination : Int) : Option[Edge] = originatingEdges.get(destination)
  
  /** Get an edge that arrives in this node that corresponds to the given key
    * @param source  the key is used to retrieve an edge
    * @return An Option[Edge] corresponding to the given key
    */
  def getEdgeFrom(source : Int) : Option[Edge] = arrivingEdges.get(source)
  /**
    *Get the nodes at the other end of the edges that end in this node
    *@return a List containing the parents of this node 
    */
  def getParents : List[Node] = {
    var parents : List[Node] = Nil
    arrivingEdges.values.foreach(e => parents ::= e.source)
    parents
  }
  /**
    * get the nodes at the other end of the edges that start in this node
    * @return a List containing the children of this node
    */
  def getChildren : List[Node] = {
    var children : List[Node] = Nil
    originatingEdges.values.foreach(e => children ::= e.destination)
    children
  }
  /**
    * Get the nodes at the other end of the edges that are connected to this nodes
    * @return a List containing the neighbours of this node
    */
  def getNeighbours : List[Node] = getParents ++ getChildren
  /**
    * Add an outgoing edge by adding it to the originatingEdges map
    * @param edge the edge that is added to the node
    */
  def addOriginatingEdge(edge: Edge) = { 
    if (!originatingEdges.contains((edge.destination.label))) {
      originatingEdges += ((edge.destination.label, edge))
    } else System.err.println("The edge (" + edge.source.label + "," + edge.destination.label + ") has already been added to this graph")
  }
  /**
    * Add an incoming edge by adding it to the arrivingEdges map
    * @param edge the edge that is added to the node
    */
  def addArrivingEdge(edge: Edge) = { 
    if (!arrivingEdges.contains((edge.source.label))) {
      arrivingEdges += ((edge.source.label, edge))
    } else System.err.println("The edge (" + edge.source.label + "," + edge.destination.label + ") has already been added to this graph")
  }
  
  /**
    * Add a 'bidirectional' edge by adding it to the arrivingEdges and originatingEdges maps
    * @param edge the edge that is added to the node
    */
  def addNeighbour(node : Node) = {
    var edge = new Edge(this, node)
    originatingEdges += ((node.label, edge))
    node.arrivingEdges += ((label, edge))
  }
  /**
    * removes a 'bidirectional' edge by removing it from the arrivingEdges and originatingEdges maps
    * @param nodeID the node identifier of the node to which the edge lies that will be removed
    */
  def removeNeighbour(nodeID: Int) = {
    originatingEdges.get(nodeID) match {
      case Some(edge) => {
        originatingEdges -= (nodeID)
        edge.destination.arrivingEdges -= (label)
      }
      case None => {
       	System.err.println("The specified edge cannot be removed since it is no member of this graph")
      }
    } 
  }
  
  /** Removes the edges to and from this node.
    *  Before the node can be removed from the graph, all edges to and from this node need to be removed.
    */
  def remove = {
    arrivingEdges.values.foreach(e => e.source.removeNeighbour(label))
    //after this run arrivingEdges should be empty
    originatingEdges.values.foreach(e => removeNeighbour(e.destination.label))
    //after this run originatingEdges should be empty
  }
  
  def doTurn(timestamp:Int, duration:Int){}
  def doRound(timestamp:Int, duration:Int){}
  def nextRound {}
  def notify(event:Event){}
  
}
