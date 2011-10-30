package graph
import scala.collection.mutable.ListBuffer

abstract class Traversal {
   def traverse(f: Node => Unit, node: Node)
}

object DepthFirstTraversal extends Traversal {
  def traverse(f: Node => Unit, node: Node) = {
    var visited : List[Node] = Nil;
    def dft(f: Node => Unit, node: Node) : Unit = {
      f(node)
      visited = node :: visited
      node.getNeighbours.reverse.foreach(e => if (!visited.contains(e)) dft(f, e))
    }
    dft(f, node)
  }
}

object BreadthFirstTraversal extends Traversal {
  def traverse(f: Node => Unit, node: Node) = {
    def bft(f: Node => Unit, node: Node) : Unit = {
      var queue = new ListBuffer[Node]()
      var visited : List[Node] = Nil
      f(node)
      visited = node :: visited
      queue += node
      while (!queue.isEmpty) {
	queue.head.getNeighbours.reverse.foreach(
	  e => if (!visited.contains(e)) { 
	    f(e) 
	    visited = e :: visited
	    queue += e
	  }
	)
	queue = queue.tail;
      }
    }
    bft(f, node)
  }
}