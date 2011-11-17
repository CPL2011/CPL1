package examples.gui

import scala.swing._
import scala.swing.event._
import java.io.File

object TestGUI extends SimpleGUIApplication{
  
	var pPath = ""
	var sPath = ""
	var uAdress = ""
	def top = new MainFrame {
	  
	  title = "Test Panel"
	  val testButton = new Button {
	    text = "Test"
	  }
	  
	  object persistencePath extends TextField { columns = 32; text = new File("").getAbsolutePath()  }
	  object statisticsPath extends TextField { columns = 32; text = new File("").getAbsolutePath()  }
	  object ubigraphAdress extends TextField { columns = 32; text = "local server" }
	  
	  val persistencePathLabel = new Label{
	    text = "insert path"
	  }
	  val statisticsPathlabel = new Label{
	    text = "insert path"
	  }
	  val ubigraphLabel = new Label{
	    text = "set ubigraph adress (default = local server)"
	  }
	  val chooseLabel = new Label{
	    text = "Select test"
	  }
	  val emptyLabel = new Label{
	    text = ""
	  }

	  
	  
	  val testChooser = new ComboBox[String](List("test1","test2","test3","..."))
	  
	  contents = new GridPanel(5,2) {
	    hGap=5
	    vGap=15
		contents += persistencePathLabel  
	    contents += persistencePath
	   
	    contents += statisticsPathlabel 
	    contents += statisticsPath
	    
	    contents += ubigraphLabel  
	    contents += ubigraphAdress
	    
	    contents += chooseLabel
	    contents += testChooser
	    
	    contents += emptyLabel
	    contents += testButton
	    
	    border = Swing.EmptyBorder(30, 30, 30, 30)

		  
	  }
	  	listenTo(testButton)
		reactions += {
		//case EditDone(`persistencePath`) =>
		//case EditDone(`statisticsPath`) =>
	  	  case WindowClosing(e) => System.exit(0)
	  	  case ButtonClicked(`testButton`) =>{
	  	    emptyLabel.text = "executing dummy " + testChooser.selection.item
	  	  }
		  
		  
		
	  	}
	  	
	  	pack
  centerOnScreen
  visible = true

	    
	    
	}
}