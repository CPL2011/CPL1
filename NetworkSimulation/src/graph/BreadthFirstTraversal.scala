package graph
import scala.collection.mutable.ListBuffer

object BreadthFirstTraversal extends Traversal {
  def traverse(f: Node => Unit, node: Node) = {
    def bft(f: Node => Unit, node: Node) : Unit = {
      var queue = new ListBuffer[Node]()
      var visited : List[Node] = Nil
      f(node)
      visited = node :: visited
      queue += node
      while (!queue.isEmpty) {
        queue.head.getOriginatingEdges.values.foreach(
            e => if (!visited.contains(e.getDestination())) { 
              f(e.getDestination()) 
              visited = e.getDestination() :: visited
              queue += e.getDestination()
            }
        )
        queue = queue.tail;
      }
    }
    bft(f, node)
  }
}