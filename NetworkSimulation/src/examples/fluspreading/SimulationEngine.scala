package examples.fluspreading
import graph.Graph

abstract class SimulationEngine(graph:Graph) {
  
  var currentTime = 0
  var conditions:List[Condition] = List[Condition]()
  
  def run()
  
  def shouldContinue():Boolean = {
    for(c : Condition <- conditions) {
     if(!c.shouldContinue)
       return false
    }
    return true
  }
	
  def addNecessaryCondition(c:Condition) {
    conditions ::= c
  }
  
  def getCurrentTime:Int = currentTime
}

abstract class Condition {
  def shouldContinue : Boolean
}

object SimulationTime {
  def TICKS_PER_SECOND: Int = 200
  def TICKS_PER_MINUTE: Int = TICKS_PER_SECOND * 60
  def TICKS_PER_HOUR: Int = TICKS_PER_MINUTE * 60
}