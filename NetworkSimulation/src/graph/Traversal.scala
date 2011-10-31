package graph
import scala.collection.mutable.ListBuffer

abstract class Traversal {
   def traverse(f: Node => Unit, node: Node)
}

