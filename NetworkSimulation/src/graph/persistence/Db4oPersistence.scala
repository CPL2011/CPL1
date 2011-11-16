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
  * A trait which adds db4o database functionality to a graph.
  * 
  * The programmer is responsible for opening and closing the database.
  * openDb will open the database and set the path of the database, 
  * 
  * package download: http://www.db4o.com/community/qdownload.aspx?file=java.zip
  */
trait Db4oPersistence extends Graph with Persistence {
/**
  * The database
  */
var db:ObjectContainer = null;
/**
  * Opens the database that is located at the path-variable
  * The user is responsible to open the database.
  */
def openDb():Unit = {
	val config:EmbeddedConfiguration  = Db4oEmbedded.newConfiguration();
	config.common().add(new TransparentActivationSupport());
	db = Db4oEmbedded.openFile(config,path)
}
/**
  * closes the database
  * The user is responsible to close the database.
  */
def closeDb():Unit = { 
  try{
    db.close()
  }catch{
    case e:Exception=>println("could not close db")
  }
}

/**Clears the database
  * 
  * The database can be cleared by deleting the database file.
  * This is done because it is not clear what objects are stored in the db, if db4o stores additional object in the db 
  * and thus manually deleting all the object could be tricky. This approach solves that problem.
  */
def clearDb(){
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
  * This implicit conversion is used in queryDb[T]
  * @param predicate A function that given an object returns a boolean
  * @return that function converted in a com.db4o.query.Predicate
  */
implicit def toPredicate[T](predicate: T => Boolean):Predicate[T] =
new Predicate[T]() {
	def `match`(p : T) : Boolean = {
		return predicate(p)
	}
}
/**
  * Provides implicit conversion of an ObjectSet to a (more usable) List
  * This implicit conversion is used for the return value of queryDb[T]
  * 
  * A RichObjectSet, which mixes scala's Iterator in the objectSet is created. Because it the Iterator provides a 'toList' function,
  * the conversion is pretty easy.
  * 
  * @param objectSet A com.db4o.ObjectSet
  * @return that ObjectSet converted in a List
  */
implicit def toList[T](objectSet: ObjectSet[T] ):List[T] = (new RichObjectSet[T](objectSet)).toList


/*
 * EXAMPLE OF USAGE
 *def predicate(n:Node):Boolean = n.neighbours.length>X&&...||...
 *queryDb(predicate)
 */
/**
  *Query the database.
  *Given a function that takes in as parameter the object that you want to retrieve from the database
  *and that will return a boolean that indicates if the object satisfies your constraints
  *
  *@param predicate a function "representing" the query
  *@return a list of objects that satisfy the query
  */
def queryDb[T](predicate: T => Boolean):List[T] = db query predicate 

/** saves this object in the database
  * 
  * To ensure that only one 'root' object, like a Graph object, is stored in the database,
  * the database is cleared before this object is stored
  */
override def save(){
  closeDb()
  clearDb()			//we need to clear the database to ensure that only one 'root' object will be stored
  openDb()			
  db.store(this)
}

/**
  * Loads the Db4oPersistence object stored in the db file located at the 'path'
  * @return the object that is loaded from the database 
  */
override def load():Option[Db4oPersistence] = 
  queryDb((g:Db4oPersistence)=>(g.path==path))  match{
      case t:List[Db4oPersistence] => {
        if(t.size==1) Some(t.first)
        else None
      }
      case _ => None
    }
}
/**
  * This companion object provides a 'static' method for the Db4oPersistence trait which will load an 
  * object from the database located at the path
  * The returned object has the same path variable as the argument(Only 1 Db4oPersistence object should be in
  * the db at all times????)
  */
object Db4oPersistence extends Db4oPersistence{
  /**
    * The path of the database
    * 
    * Because object cannot have parameters, the path is instantiated as an empty string
    * but this 'invalid path' is never a problem 
    */
  override var path:String = "" 		
  def load(p:String):Option[Db4oPersistence] = {
    setPath(p)
    openDb()
    val ret = load()
    closeDb()
    return ret
  }
}

/**
  * extend DB4O's ObjectSet with scala's Iterator for an easy conversion to a List
  */
class RichObjectSet[T](objectSet:ObjectSet[T]) extends Iterator[T] {
	def hasNext:Boolean =  objectSet.hasNext()
			def next:T = objectSet.next()
}


