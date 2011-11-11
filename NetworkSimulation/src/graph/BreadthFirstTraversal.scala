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
        queue.head.getChildren.foreach(
            node => if (!visited.contains(node)) { 
              f(node) 
              visited = node :: visited
              queue += node
            }
        )
        queue = queue.tail;
      }
    }
    bft(f, node)
  }
}