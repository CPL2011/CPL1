package graph

import scala.collection.mutable.ListBuffer

/**
 * An abstract class containing a method for traversing (= visiting each (connected) element) a graph
 */
abstract class Traversal {
  
   /**
    * traverses the connected part of a graph starting from the given node, whilst applying the given
    * higher order function on each visited node.
    * @param f : a higher order function getting a Node as argument and returning Nothing
    * @param node : an instance of the class Node representing the root from where the traversal will begin
    */
   def traverse(f: Node => Unit, node: Node)
}

