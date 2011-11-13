package simulation.stat
import graph.Graph
import scala.collection.mutable.HashMap
import scala.collection.mutable.StringBuilder
import examples.fluspreading.VisualGraph
import examples.fluspreading.Person
import scala.collection.mutable.ListBuffer

trait Statistics {
  var statistics:List[((Statistics)=>String,String)] = List()
  var results:HashMap[String,ListBuffer[String]] = new HashMap[String,ListBuffer[String]]
  var length:Int = 0

  def addStatistic(g:Statistics=>String,s:String):Unit = {
    
    statistics = (g:Statistics=>String,s:String)::statistics
    results.put(s,new ListBuffer[String])
    
  }
  /**
   * gathers statistics from every function and puts them into the results map
   */
  def gatherStat(){
    
    length += 1
    for((f,id)<-statistics){
    var v = results.get(id)	//We need to use pattern matching because HashMap.get returns an Option[T] 
    
    v match  {					
       case Some(v) => v.+=:(f(this))
       case None =>		
      }
    }
  }
  
  /**
   * this function generates a string that contains all the results
   * The data is separated by tabs
   * A header is also used
   */
  def getStatistics():String = {
     var sb:StringBuilder = new StringBuilder
     
     for(id<-results.keySet) sb.append(id + "\t")
     
     sb.append("\n")
     
     var values = for(l<-results.values)yield l.reverse	//we need to reverse the lists because we used cons to construct them

     while(values.head.size>0){
    	 for(l<-values){
    	   sb.append(l.head + "\t")
    	   l.-=(l.head)
    	 }
    	 
    		 sb.append("\n")	 			 
     }
     return sb.toString()
  }
  def writeStatisticsToFile(path:String){
    val out = new java.io.FileWriter(path)
    out.write(getStatistics())
    out.close
  }
  /******************************************************************
   * 		definition of a couple of statistics funcitons			*
   *****************************************************************/
  def numberOfNodes(g:Statistics):String = g match{
    case g:Graph=>g.nodes.size.toString()
    case _ => ""
  }
  def averageNeighbores(g:Statistics):String = g match{
    case g:Graph=>{
	  	var nr = 0f
	    for(n<-g.nodes.values){
	      nr+= n.originatingEdges.size
	    }
	  	return (nr/g.nodes.size).toString()
	    }
    case _=>""
  }
    
  def numberOfInfected(g:VisualGraph):String = {
    	var infected:Int=0
  		for(n<-g.nodes) n match{
  		  case n:Person=>if(n.isInfected)infected+=1
  		  case _ =>
  		}
    	return infected.toString()
    }
  

}