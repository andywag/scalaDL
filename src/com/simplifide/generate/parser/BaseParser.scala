package com.simplifide.generate.parser

import collection.mutable.ListBuffer
import model._
import operator.BitOperations
import com.simplifide.generate.signal.FixedType
import com.sun.xml.internal.fastinfoset.util.ValueArray

/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 7/14/11
 * Time: 2:57 PM
 * To change this template use File | Settings | File Templates.
 */

class BaseParser {

  /** Scope which is used for addition of statements */
  implicit val scope = this


  val statements = new ListBuffer[Expression]()
  val signals    = new ListBuffer[Signal]()

  private def debugState(statement:Expression) {
    System.out.println(statement)
    statement.split.foreach(x => System.out.println("   " + x))
  }
  def debug {
    statements.foreach(System.out.println(_))
  }

  /** Attaches and assign statement */
  def assign(statement:Expression) = statements.append(statement)

  def constant(value:Double) = {
     val values = List.tabulate(32)(i => value*scala.math.pow(2.0,i-16))
     //val floors = values.map(x => scala.math.floor(x))

     val intValue = values.reverse.indexWhere(x => scala.math.floor(x) == 0)
     val fracValue = values.indexWhere(x => (x - scala.math.floor(x) == 0))

     ObjectFactory.Constant("",value,Model.Fixed(fracValue - intValue,fracValue - 16))

  }

  def constant(value:Double,fixed:Model.Fixed = Model.NoFixed) =
    ObjectFactory.Constant("",value,fixed)


  def signal(name:String, typ:SignalType = SignalType.SignalTypeImpl,fixed:Model.Fixed = Model.Fixed(1,0)):Signal = {
    signal(ObjectFactory.Signal(name,typ,fixed)(List()))
  }
  /** Convenience method for creating a signal */
  def array(name:String, typ:SignalType = SignalType.SignalTypeImpl,fixed:Model.Fixed = Model.Fixed(1,0))(arr:Int*):Signal = {
    signal(ObjectFactory.Signal(name,typ,fixed)(arr.toList))
  }

  /** Adds a signal to the module */
  def signal[T <: Signal](signal:T):T = {
    signals.append(signal)
    signal
  }
  def signal(values:Signal*) {
    signals.appendAll(values.toList)
  }

  def $cat(expressions:Expression*):Expression              =  new BitOperations.Concatenation(expressions.toList)
  def $repeat(expression:Expression, length:Int):Expression =  new BitOperations.Repeat(expression)



}

object BaseParser {

}