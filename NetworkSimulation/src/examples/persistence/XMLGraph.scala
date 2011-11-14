package examples.persistence

import graph.Graph
import graph.persistence.XML
import scala.collection.immutable.HashMap
import graph.Node
import graph.persistence.XMLPersistence

class XMLGraph(p:String) extends Graph with XMLPersistence {
	override var path  = p
  
	def this(node:scala.xml.Node,p:String) = { 
		this(p)
		nodes = new HashMap[Int, Node]()
		for(i <-(((node) \ "Node") )){ addNode(new XMLNode(i))}
		for(n <-(((node) \ "Node") \\ "Edge")){ 
			val s:Int = (n \ "source").text.toInt
			val d:Int = (n \ "destination").text.toInt
			
			addEdge(s,d)
		}

	}
	/**
	 * To load an XMLGraph, first set the correct path and then invoke this mehtod
	 */
	override def load():Option[XMLGraph] = try{
	  Some(new XMLGraph(loadFile(),path))
	  
	}catch{
	  case e:Exception=>None
	}
	
	override def toXML():scala.xml.Node = {
<Graph>
    {nodes.values.map(n=>n.toXML())}
</Graph>
}
	
	implicit def nodeToXMLNode(n:Node):XMLNode = new XMLNode(n)


}