package com.simplifide.generate.blocks.basic.fixed

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import com.simplifide.generate.blocks.basic.condition.SimpleMux
import com.simplifide.generate.blocks.basic.operator._
import com.simplifide.generate.generator.{SimpleSegment, CodeWriter, SegmentReturn}
import com.simplifide.generate.signal.{Constant, SignalTrait, FixedType}

class ClipSegment(val input:SimpleSegment,override val fixed:FixedType) extends SimpleSegment{
  

  
    override def createCode(writer:CodeWriter):SegmentReturn = {
      val inFix  = input.fixed
      val outFix = fixed
      
      val diff = (inFix.width - inFix.fraction) - (outFix.width - outFix.fraction)
      val bot  = input.fixed.width - diff - 1
    
      val width = input.fixed.width
      val top = new Select(input,Some(input.fixed.width-1),None)
      var neg = new BinaryOperator.AND(top,new UnaryOperator.Not(new Select(input,Some(width-2),Some(width-2))))
      var pos = new BinaryOperator.AND(new UnaryOperator.Not(top),new Select(input,Some(width-2),Some(width-2)))

      for (i <- bot until width-2) {
        neg = new BinaryOperator.AND(neg,new UnaryOperator.Not(new Select(input,Some(i),Some(i))))
        pos = new BinaryOperator.AND(pos,new UnaryOperator.Not(new Select(input,Some(i),Some(i))))
      }


      // Truncate the appendSignal
      val signal = new FixedSelect(input,fixed) //new Select(input,Some(outFix.width-1),Some(0))
      val mux  = new SimpleMux(pos,new Constant.Max(outFix),signal)
      val mux2 = new SimpleMux(neg,new Constant.Min(outFix),mux)
      return writer.createCode(mux2)
    
    }

}
