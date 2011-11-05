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
        queue.head.originatingEdges.values.foreach(
            e => if (!visited.contains(e.destination)) { 
              f(e.destination) 
              visited = e.destination :: visited
              queue += e.destination
            }
        )
        queue = queue.tail;
      }
    }
    bft(f, node)
  }
}