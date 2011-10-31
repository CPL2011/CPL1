package graph
import scala.collection.mutable.ListBuffer

object DepthFirstTraversal extends Traversal {
  def traverse(f: Node => Unit, node: Node) = {
    var visited : List[Node] = Nil;
    def dft(f: Node => Unit, node: Node) : Unit = {
      f(node)
      visited = node :: visited
      node.getConnectedEdges.reverse.foreach(e => if (!visited.contains(e)) dft(f, e.getDestination()))
    }
    dft(f, node)
  }
}