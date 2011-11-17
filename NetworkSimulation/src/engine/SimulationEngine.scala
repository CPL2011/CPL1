package engine
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
  
  def getCurrentTime:Int = {
    val result = currentTime
    return result
  }
}

abstract class Condition {
  def shouldContinue : Boolean
}

//SimulationTime defines how much ticks per second there are.
object SimulationTime {
  var TICKS_PER_SECOND: Int = 200
  def TICKS_PER_MINUTE: Int = TICKS_PER_SECOND * 60
  def TICKS_PER_HOUR: Int = TICKS_PER_MINUTE * 60
  
  /**
   * Returns a string represenentation of the time in HH:MM:SS
   */
  def toString(time:Int):String = {
    return time/TICKS_PER_HOUR + ":" + (time%TICKS_PER_HOUR)/TICKS_PER_MINUTE + ":" + (time%TICKS_PER_MINUTE)/TICKS_PER_SECOND
  }
}