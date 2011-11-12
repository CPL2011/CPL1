package examples.persistence

import graph.persistence.Db4oPersistence
import graph.Graph

class Db4oGraph(p:String) extends Graph with Db4oPersistence{
  override var path = p
  
  override def load():Db4oGraph = {
    val t = queryDb((g:Db4oGraph)=>(g.path==path)).last
    return t
  }
}