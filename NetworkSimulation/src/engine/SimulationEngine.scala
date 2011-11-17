package engine
import graph.Graph

abstract class SimulationEngine(graph:Graph) {
  
  var currentTime = 0
  var conditions:List[Condition] = List[Condition]()
  
  def run
  
  /**
  * If one condition returns false ShouldContinue is false.
  */
  def shouldContinue:Boolean = {
    for(c : Condition <- conditions) {
     if(!c.shouldContinue)
       return false
    }
    return true
  }

  /**
   * Adds a condition to the simulation that has to be true.
   * If not the simulation will stop as soon as possible.
   * @param c : a function that returns a boolean false if the Simulation must stop
   */
  def addNecessaryCondition(c:Condition) {
    conditions ::= c
  }
  
  /**
   * Some number representing the currentTime of the simulation.
   * With the object SimulationTime you can convert this value to Hours,Minutes,Seconds. 
   */
  def getCurrentTime:Int = {
    val result = currentTime
    return result
  }
}

abstract class Condition {
  def shouldContinue : Boolean
}

/**
 * SimulationTime defines how much ticks per second there are.
 * And is a conversion tool from ticks to Time and vice versa
 */
object SimulationTime {
  var TICKS_PER_SECOND: Int = 200
  def TICKS_PER_MINUTE: Int = TICKS_PER_SECOND * 60
  def TICKS_PER_HOUR: Int = TICKS_PER_MINUTE * 60
  
  def getHours(time:Int): Int = {return time/TICKS_PER_HOUR}
  def getMinutes(time:Int) : Int = {return (time%TICKS_PER_HOUR)/TICKS_PER_MINUTE}
  def getSeconds(time:Int) : Int = {return (time%TICKS_PER_MINUTE)/TICKS_PER_SECOND}
  
  /**
   * Returns a string represenentation of the time in HH:MM:SS
   */
  def toString(time:Int):String = {
    return getHours(time) + ":" + getMinutes(time) + ":" + getSeconds(time)
  }
}