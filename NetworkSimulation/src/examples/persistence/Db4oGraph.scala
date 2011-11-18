package examples.persistence

import graph.persistence.Db4oPersistence
import graph.Graph

/**
 * A graph that supports loading a graph from a db4o database
 */
class Db4oGraph(p:String) extends Graph with Db4oPersistence{
  /**
   * This var indicates where the database is located
   */
  override var path = p
  
  /**
   * Loads the graph present in the given path
   * If the stored graph is not an instance of class Db4oGraph then None gets returned
   */
  override def load():Option[Db4oGraph] = {
    super.load() match{
      case t:Some[Db4oGraph] => t
      case _ => None
    }
   
  }
}