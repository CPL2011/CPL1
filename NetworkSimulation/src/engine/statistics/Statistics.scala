package engine.statistics
import graph.Graph
import scala.collection.mutable.HashMap
import scala.collection.mutable.StringBuilder
import examples.fluspreading.VisualGraph
import examples.fluspreading.Person
import scala.collection.mutable.ListBuffer
import java.io.File
import engine.SimulationEngine
/**
  * This trait provides the capability for flexible statistics gathering.
  * After the statistics are gathered, a dataset can be written to a file for post analysis, for example by R
  * This trait keeps it's own notion of time to be compatible with different simulation engines
  * 
  * The user needs to add a number of statistic functions, and set the samplePeriod
  * After the simulation is complete, the user can call writeStatisticsToFile(path:String) to write the data in a text file
  * 
  */
trait Statistics extends SimulationEngine{
  
  /**
   * The period (number of 'doTurn' calls) at which will be sampled
   */
  var samplePeriod:Int = 1
  /**
   * The turn at which the engine is
   */
  var turn:Int = 0
  /**
    * A HashMap containing String identifiers and listbuffers that contain the results of the data gathering
    */
  var results:HashMap[String,ListBuffer[String]] = new HashMap[String,ListBuffer[String]]
  /**
    * A list that contains a tuple  functions and function-identifiers. 
    * The functions, which return a single String, are used for the data gathering.
    * The identifiers are used to add the result of the function to the right result list 
    */
  var statistics:List[((Any)=>String,String)] = List()
  /**
    * The amount of data entries of the results
    */
  var length:Int = 0
  val subject:Any
  
  def setSamplePeriod(p:Int){
    samplePeriod = p
  }
  /**
    * Add a statistical function that will be used for data gathering
    * This will create a new entry in the statistics List and results Map
    * @param obj a function that given an object returns a string representing the gathered statistic of that object.
    * @param s an identifier string for the statistic that is added. If this parameter is not unique, the statistic will not be added
    */
  def addStatistic(obj:Any=>String,s:String):Unit = {
    if(!results.contains(s)){
    statistics = (obj:Any=>String,s:String)::statistics
    results.put(s,new ListBuffer[String])
    
    }
  }
  /**
    * gathers statistics from every function in the statistics list and puts them into the results map
    * the length variable will be increased by 1
    * This is sort of a higher order function where the function parameter is retrieved from an external list
    */
  def gatherStat(a:Any):Unit={
    
    length += 1
    for((f,id)<-statistics){
    var v = results.get(id)	//We need to use pattern matching because HashMap.get returns an Option[T] 
    
    v match  {					
       case Some(v) => v.+=:(f(a))
       case None =>		
      }
    }
  }
  
  /**
    * This function generates a string that contains all the results
    * The data is separated by tabs and a header, which will be the statistics identifier, is inserted for each columns
    * @return A String representing the statistics. The different statistics are speparated by a tab, and the identifier is used a header
    */
  def getStatistics():String = {
     var sb:StringBuilder = new StringBuilder
     
     for(id<-results.keySet) sb.append("[" + id + "]" + "\t")
     
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
  /**
    * Retrieves the results and writes them to a file
    * @param path The path of the file 
    */
  def writeStatisticsToFile(path:String){
    try{
      val v = new File(path)
    if(v.exists) v.delete()
    val out = new java.io.FileWriter(path)
    out.write(getStatistics())
    out.close
    }catch{
      case e:Exception=>println("could not write to file " + path)
    }
  }
  override def doStep(){
    super.doStep 
    turn+=1
    if(turn%(samplePeriod)==0)    gatherStat(subject)
    
  }
  
  /******************************************************************
   * 		definition of a couple of statistics funcitons			*
   *****************************************************************/
  def numberOfNodes(g:Any):String = g match{
    case g:Graph=>g.nodes.size.toString()
    case _ => ""
  }
  def averageNeighbores(g:Any):String = g match{
    case g:Graph=>{
	  	var nr = 0f
	    for(n<-g.nodes.values){
	      nr+= n.originatingEdges.size
	    }
	  	return (nr/g.nodes.size).toString()
	    }
    case _=>""
  }
    

  

}