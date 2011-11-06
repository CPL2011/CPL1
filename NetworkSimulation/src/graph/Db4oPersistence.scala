package graph

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
trait Db4oPersistence{

var db:ObjectContainer = null;

def openDb(config:EmbeddedConfiguration,path:String):Unit = db = Db4oEmbedded.openFile(config,path)

def openDb(path:String):Unit = {
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

def closeDb():Unit = db.close()
//clear db by deleting the file and restart the db...
def deleteDb(path:String){
  val thedb:File = new File(path)
  
  if(thedb.exists()) {
    if(thedb.delete())    println("deleted old test db")
    else println(path + " does not exist!")
  }
    
  }

implicit def toPredicate[T](predicate: T => Boolean) =
new Predicate[T]() {


	//def `match`(point : Any) : Boolean = 
	//		throw new Exception("This should never be called!")
	def `match`(p : T) : Boolean = {
			return predicate(p)
	}
}
implicit def toList[T](objectSet: ObjectSet[T] ) =
(new RichObjectSet[T](objectSet)).toList

//query return db4o ObjectSet class
//because of the implicit def it is automatically converted to a List[T]
//that is why we needed the RichObjectSet...
/*
 *EXAMPLE OF USAGE
 *def predicate(n:Node):Boolean = n.neighbours.length>X&&...||...
 *queryDb(predicate)
 */
def queryDb[T](predicate: T => Boolean):List[T] =
db query predicate 
}
/*
 * extend DB4O's ObjectSet with scala's Iterator
 */
class RichObjectSet[T](objectSet:ObjectSet[T]) extends Iterator[T] {
	def hasNext:Boolean =  objectSet.hasNext()
			def next:T = objectSet.next()
}
