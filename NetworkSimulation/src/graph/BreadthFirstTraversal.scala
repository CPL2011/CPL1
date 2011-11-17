package graph
import scala.collection.mutable.ListBuffer
/**
  *An object that provides the functionality to traverse nodes breadth first 
  */
object BreadthFirstTraversal extends Traversal {

   /**
    * traverses the connected part of a graph, breadth-first starting from the given node, whilst applying the given
    * higher order function on each visited node.
    * @param f : a higher order function getting a Node as argument and returning Nothing
    * @param node : an instance of the class Node representing the root from where the traversal will begin
    */
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