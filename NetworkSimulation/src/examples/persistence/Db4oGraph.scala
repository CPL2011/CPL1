package examples.persistence

import graph.persistence.Db4oPersistence
import graph.Graph

class Db4oGraph(p:String) extends Graph with Db4oPersistence{
  override var path = p
  
  override def load():Option[Db4oGraph] = {
    super.load() match{
      case t:Option[Db4oGraph] => t
      case _ => null
    }
   
  }
}