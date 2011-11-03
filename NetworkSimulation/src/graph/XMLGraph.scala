package graph
import scala.xml.parsing.ConstructingParser$
import java.io.File
import scala.xml.NodeSeq$

class XMLGraph extends Graph{
  
 
  def toXML() = <Graph>
  <Nodes>
    {nodes.values.map(n=>n.toXML())}
  </Nodes>
</Graph>
		  		
  def setGraphFromXML(path:String){
    val node:scala.xml.Node = loadGraph(path)
    nodes.clear()
    edges.clear()
    for(i <-((((node)\"Nodes") \ "Node") \\ "@label")) addNode(i.text.toInt)
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
    scala.xml.XML.save(path,toXML)
  }
  
  def loadGraph(path:String)=    scala.xml.XML.loadFile(path)

 }