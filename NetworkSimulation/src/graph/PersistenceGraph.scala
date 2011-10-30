package graph
import java.io.File
import com.db4o.ObjectContainer
import com.db4o.Db4o
import com.db4o.query.Predicate
import com.db4o.ObjectSet
import com.db4o.config.EmbeddedConfiguration
import com.db4o.Db4oEmbedded

class PersistenceGraph(p:String) extends Graph with Db4oPersistence{
    val path = p
    	//Maybe this is  the sollution to your described problem with the nullpointers.
    	//Do I do not know if db4o will do with possible loops
	openDb(path)
	
	
def storeNode(i:Int,node:Node){
    
	db.store((i,node))
}
def storeEdge(e:Edge){
	db.store(e)
}
def storeGraph(){
	db.store(this)
	//for((i,n)<-getNodes()) storeNode(i,n)//db4o does not store the nodes and edges when you just store the graph??->I've got some nullpointers
	//for((_,e)<-edges) db.store(e)//when I left out these 2 lines-_-
	//The default update depth for all objects is 1, meaning that only primitive and String members will be updated, but changes in object members will not be reflected.

}

//def getGraphFromPersistence():PersistenceGraph{
//   this = queryDb((g:PersistenceGraph)=>g.path==path)
//   nodes.clear()
//   edges.clear()
//   for(n:Node<-queryDb((n:Node)=>true)) addNode(n.getLabel)
//   for(e:Edge<-queryDb((e:Edge)=>true)) addEdge(e.getSource().getLabel,e.getDestination().getLabel)
//}
}
object PersistenceGraph extends Graph with Db4oPersistence{
  
 def getGraphFromDb(path:String):PersistenceGraph = {
 	//This can be a problem to.
 	//The default activation depth for any object is 5, so our example above runs into nulls after traversing 5 references.
 	//Proposed sollution:
 	//Configuration config = Db4o.newConfiguration();
	//config.objectClass(Node.class).cascadeOnActivate(true);
	//config.objectClass(Edge.class).cascadeOnActivate(true);
    openDb(path)
    val p:PersistenceGraph = queryDb((g:PersistenceGraph)=>(g.path==path)).last
    
    closeDb()
    p.openDb(path)
    return p
  }
    
}