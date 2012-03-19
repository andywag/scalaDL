package com.simplifide.generate.parser

import collection.mutable.ListBuffer
import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.signal.{FixedType, Constant, OpType, SignalTrait}

/**
 * Created by IntelliJ IDEA.
 * User: awagner
 * Date: 8/9/11
 * Time: 3:14 PM
 * To change this template use File | Settings | File Templates.
 */

/** Trait which holds a list buffer of signals as well as containing convenience methods
 *  for dealing with appendSignal creation.
 **/
trait SignalHolder extends SignalMethods{



  /** Main item which contains a List of signals*/
  val signals    = new ListBuffer[SignalTrait]()

  /*** Adds a appendSignal to the module */
  override def appendSignal[T <: SignalTrait](signal:T):T = {
    signals.append(signal)
    signal
  }


  /** Create an internal array */
  def internal_array(name:String,typ:OpType,fixed:FixedType,depth:Int):SignalTrait =  {
    val sig = SignalTrait(name,typ,fixed,depth)
    signals.append(sig)
    sig
  }

  /** Appends a list of signals*/
  def signal(values:SignalTrait*):SignalTrait = {
    signals.appendAll(values.toList)
    values(0)
  }

  def signal(values:List[SignalTrait]):SignalTrait = {
    signals.appendAll(values.toList)
    if (values.size > 0) values(0) else null
  }

  def inputs(values:SignalTrait*) {
    signals.appendAll(values.map(_.changeType(OpType.Input)))
  }

  def outputs(values:SignalTrait*) {
    signals.appendAll(values.map(_.changeType(OpType.Output)))
  }
    /** Assign the clock to the module */
  def assignClock(clock:ClockControl):ClockControl = {
    appendSignal(clock.getBus(OpType.Input))
    clock
  }
  /** Creates a Constant without a width */
  def C(value:Int) = com.simplifide.generate.signal.NewConstant(value)
  /** Creates a Constant */
  def C(width:Int, value:Int) = com.simplifide.generate.signal.NewConstant(value,width)
  /** Creates a Constant with a Hex Value */
  //def H(width:Int, value:String) =
  //  new com.simplifide.generate.signal.Constant.Hex(value,FixedType.unsigned(width,0))






}