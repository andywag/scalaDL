package com.simplifide.generate.signal

import com.simplifide.generate.generator.{SegmentReturn, CodeWriter, SimpleSegment}
import com.simplifide.generate.blocks.basic.fixed.FixedSelect
import com.simplifide.generate.parser.model.{Clock, Signal}
import com.simplifide.generate.blocks.basic.operator.Select

/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/


trait SignalTrait extends SimpleSegment with Signal{

  override val name:String
  val opType:OpType
  val fixed:FixedType



  override def apply(clk:Clock) = child(clk.delay).asInstanceOf[Signal]
  override def apply(index:Int) = child(index).asInstanceOf[Signal]

  override def sliceFixed(fixed:FixedType):SimpleSegment = new FixedSelect(this,fixed)
  override def copy(index:Int):SignalTrait = SignalTrait(name + "_" + index, opType, fixed)

  /** Changes the type of the signal. Mainly used for Input Output Changes during connections */
  def changeType(typ:OpType):SignalTrait = SignalTrait(this.name,typ,this.fixed)

  val arrayLength = 0

  /** Number of child signals */
  override def numberOfChildren:Int = 0

  override def child(index:Int):SimpleSegment = slice(index)
  /** Creates a New Signal */
  def newSignal(nam:String,
                optype:OpType = this.opType,
                fix:FixedType = this.fixed):SignalTrait

  def allSignalChildren:List[SignalTrait] = this.allChildren.map(_.asInstanceOf[SignalTrait])
  /** Create Slice is used for creating the variables in an array whereas slice is
    * used to get the variables. There may be a subtle difference between the 2 methods. Creation of the slice is called
    * when creating the children slice is called on the actual exp
    */
  def createSlice(index:Int):SignalTrait = {
    val cop = this.copy(this.name + "_" + index)
    cop
  }
  /** Creates the subsignal associated with this vector index */
  def slice(index:Int):SignalTrait  = this
  /** Returns all of the children associated with this vector. This method only works on the vector portion
    * of the operation */
  override def children:List[SignalTrait] = List()
  /** Create a list of appendSignal declarations for this appendSignal. This will expand the vector into a larger set of signals */
  def copy(nam:String,optype:OpType=opType,fix:FixedType=fixed):SignalTrait = {
    val cop = newSignal(nam,optype,fix)
    cop
  }

  def createCode(writer:CodeWriter):SegmentReturn = SegmentReturn.segment(name)

  def sign:SimpleSegment = Select.sign(this)







}

object SignalTrait {

  def apply(name:String) = new Signal(name,OpType.Signal,FixedType.Simple)
  def apply(name:String,optype:OpType) = new Signal(name,optype,FixedType.Simple)
  def apply(name:String,fixed:FixedType) = new Signal(name,OpType.Signal,fixed)
  def apply(name:String,optype:OpType,fixed:FixedType) = new Signal(name,optype,fixed)

  def newSignal(name:String) = new Signal(name,OpType.Signal,FixedType.Simple)
  /** Creates a new single bit appendSignal with the OpType optype */
  def newSignal(name:String,optype:OpType) = new Signal(name,optype,FixedType.Simple)
  /** Creates a new appendSignal with a fixed type as well and programmable optype */
  def newSignal(name:String,fixed:FixedType) = new Signal(name,OpType.Signal,fixed)
  /** Creates a new appendSignal with a fixed type as well and programmable optype */
  def newSignal(name:String,optype:OpType,fixed:FixedType) = new Signal(name,optype,fixed)
  /** Creates a new appendSignal */

  class Signal(override val name:String,override val opType:OpType,override val fixed:FixedType) extends SignalTrait {

    override val isInput  = opType.isInput
    override val isOutput = opType.isOutput

    override def newSignal(nam:String,optype:OpType,fix:FixedType):SignalTrait = new Signal(nam,optype,fix)
    override def slice(index:Int):SignalTrait = {
      if (this.numberOfChildren == 0) this             // Kind of a kludge shouldn't be required
      else new Signal(name + "_" + index,opType,fixed)
    }
  }
}
