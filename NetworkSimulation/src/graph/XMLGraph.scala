package graph
import scala.xml.parsing.ConstructingParser$
import java.io.File
import scala.xml.NodeSeq$
import scala.collection.immutable.HashMap

class XMLGraph extends Graph{
  
 
//  def toXML() = <Graph>
//  <Nodes>
//    {nodes.values.map(n=>n.toXML())}
//  </Nodes>
// </Graph>
		  		
  def loadGraph(path:String){
    val node:scala.xml.Node = loadXML(path)
    //nodes.clear()
    nodes = new HashMap[Int, Node]()
   // edges.clear()
    for(i <-((((node)\"Nodes") \ "Node") )) addNode(new Node(i))
    for(n <-((((node)\"Nodes") \ "Node") \\ "Edge")){ 
      val s:Int = (n \ "source").text.toInt
      val d:Int = (n \ "destination").text.toInt
      addEdge(s,d)
    }
  }
  
  def saveGraph(path:String){
    val f:File = new File(path)
  
  if(f.exists()) {
    if(f.delete())    println("deleted old xml")
    else println("could not delete old xml!")
  }
  //  scala.xml.XML.save(path,toXML)
  }
  
  def loadXML(path:String)=    scala.xml.XML.loadFile(path)

 }