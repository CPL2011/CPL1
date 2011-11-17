package graph
import scala.collection.mutable.ListBuffer

/**
 * An object extending Traversal, providing a depth first traversal
 */
object DepthFirstTraversal extends Traversal {

 /**
    * traverses the connected part of a graph depth-first, starting from the given node, whilst applying the given
    * higher order function on each visited node.
    * @param f : a higher order function getting a Node as argument and returning Nothing
    * @param node : an instance of the class Node representing the root from where the traversal will begin
    */
  def traverse(f: Node => Unit, node: Node) = {
    var visited : List[Node] = Nil;
    def dft(f: Node => Unit, node: Node) : Unit = {
      f(node)
      visited = node :: visited
      node.getChildren.foreach(node => if (!visited.contains(node)) dft(f, node))
    }
    dft(f, node)
  }
}