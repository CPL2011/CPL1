package graph.persistence

import graph.Graph
import java.io.File
import com.db4o.ObjectContainer
import com.db4o.Db4o
import com.db4o.query.Predicate
import com.db4o.ObjectSet
import com.db4o.config.EmbeddedConfiguration
import com.db4o.Db4oEmbedded
import com.db4o.ta.TransparentActivationSupport

/**
 * package download: http://www.db4o.com/community/qdownload.aspx?file=java.zip
 */

/**
 * A trait which adds db4o database functionality to a graph.
 * 
 * The programmer is responsible for opening and closing the database.
 * openDb will open the database and set the path of the database, 
 * 
 * Probably best to put this trait at the end of the inheritance tree???
 */
trait Db4oPersistence extends Graph with Persistence {
var db:ObjectContainer = null;



//def openDb(config:EmbeddedConfiguration,path:String):Unit = {
//  db = Db4oEmbedded.openFile(config,path)
//
//}

def openDb():Unit = {
	val config:EmbeddedConfiguration  = Db4oEmbedded.newConfiguration();
/*config.common().objectClass("class graph.Node").cascadeOnActivate(true);
	config.common().objectClass("class graph.Edge").cascadeOnActivate(true);
	config.common().objectClass("class graph.PersistenceGraph").cascadeOnActivate(true);
	config.common().objectClass("class graph.Graph").cascadeOnActivate(true);
	config.common().objectClass("class scala.collection.mutable.HashMap").cascadeOnActivate(true);

	config.common().objectClass("class graph.Node").cascadeOnDelete(true);
	config.common().objectClass("class graph.Edge").cascadeOnDelete(true);
	config.common().objectClass("class graph.PersistenceGraph").cascadeOnDelete(true);
	config.common().objectClass("class graph.Graph").cascadeOnDelete(true);
	config.common().objectClass("class scala.collection.mutable.HashMap").cascadeOnDelete(true);

	config.common().objectClass("class graph.Node").cascadeOnUpdate(true);
	config.common().objectClass("class graph.Edge").cascadeOnUpdate(true);
	config.common().objectClass("class graph.PersistenceGraph").cascadeOnUpdate(true);
	config.common().objectClass("class graph.Graph").cascadeOnUpdate(true);
	config.common().objectClass("class scala.collection.mutable.HashMap").cascadeOnUpdate(true);
	config.common().activationDepth(100);
	config.common().updateDepth(100);
	config.common().maxStackDepth(100);*/
	config.common().add(new TransparentActivationSupport());
	db = Db4oEmbedded.openFile(config,path)
}

def closeDb():Unit = { 
  try{db.close()
  }catch{
    case e:Exception=>println("could not close db")
  }
}

/**
 * The database can be cleared by deleting the file.
 * This is done because it is not clear what objects are stored in the db, and deleting them
 * all would be complicated.
 */
def deleteDb(){
try{ 
	val thedb:File = new File(path)
	
	if(thedb.exists()) {
		if(thedb.delete())    println("deleted old test db")
		else println(path + " does not exist!")
	}
}catch{
  case e:Exception => println("could not delete database")
}

}
/**
 * Provides implicit conversion of a function to a predicate 
 * 
 * used in queryDb[T]
 */
implicit def toPredicate[T](predicate: T => Boolean):Predicate[T] =
new Predicate[T]() {
	def `match`(p : T) : Boolean = {
		return predicate(p)
	}
}
/**
 * Provides implicit conversion of an ObjectSet to a (more usable) List
 * 
 * used for the return value of queryDb[T]
 */
implicit def toList[T](objectSet: ObjectSet[T] ):List[T] = (new RichObjectSet[T](objectSet)).toList


/**
 *Query the database.
 * 
 *EXAMPLE OF USAGE
 *def predicate(n:Node):Boolean = n.neighbours.length>X&&...||...
 *queryDb(predicate)
 */
def queryDb[T](predicate: T => Boolean):List[T] = db query predicate 

//def storeNode(i:Int,node:Node){
//    
//	db.store((i,node))
//}
//def storeEdge(e:Edge){
//	db.store(e)
//}
override def save(){
	db.store(this)
}
override def load():Db4oPersistence = queryDb((g:Db4oPersistence)=>(g.path==path)).last

   


}

/**
 * extend DB4O's ObjectSet with scala's Iterator
 */
class RichObjectSet[T](objectSet:ObjectSet[T]) extends Iterator[T] {
	def hasNext:Boolean =  objectSet.hasNext()
			def next:T = objectSet.next()
}
