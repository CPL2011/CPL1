package examples.persistence

import graph.Graph
import graph.persistence.XML
import scala.collection.immutable.HashMap
import graph.Node
import graph.persistence.XMLPersistence
/**
 * An XMLGraph is a graph that can be loaded from and stored in an XML file
 * 
 * This example class shows that to use the XML functionality, a bit of extra work is required in comparison to db4o
 * 
 *@param p the path of the XML file 
 */
class XMLGraph(p:String) extends Graph with XMLPersistence {
	override var path  = p
	/**
	 * A constructor which allows to create a new XMLGraph from a scala.xml.node
	 * First all the nodes will be extracted and added to the graph, and then the edges are extracted and added to the graph
	 * @param node The XML node which contains the XML to construct a graph
	 * @param p The path that will be set for the new XMLGraph
	 */
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
	/**
	 * Generate a scala.xml.Node which represents this graph
	 */
	override def toXML():scala.xml.Node = {
<Graph>
    {nodes.values.map(n=>n.toXML())}
</Graph>
}
	/**
	 * allow implicit conversion of a graph.Node to an XMLNode
	 */
	implicit def nodeToXMLNode(n:Node):XMLNode = new XMLNode(n)


}