package com.simplifide.generate.language

import com.simplifide.generate.generator.SimpleSegment
import com.simplifide.generate.blocks.basic.flop.ClockControl
import com.simplifide.generate.language.LanguageFactory.ExpressionConversion
import com.simplifide.generate.parser.model._
import com.simplifide.generate.signal.{SignalTrait, OpType, FixedType}
import scala.{Some, None}

/**
 * Created by IntelliJ IDEA.
 * User: andy
 * Date: 7/28/11
 * Time: 7:18 PM
 * To change this template use File | Settings | File Templates.
 */

class Conversions {

}

object Conversions {

  implicit def Signal2SignalTrait(signal:Signal):SignalTrait = {
    if (signal.isInstanceOf[SignalTrait]) signal.asInstanceOf[SignalTrait]
    else null
  }

  implicit def Expression2Segment(expression:Expression):SimpleSegment = {
    if (expression.isInstanceOf[SimpleSegment]) expression.asInstanceOf[SimpleSegment]
    else new ExpressionConversion(expression)
  }

  implicit def Clock2FlopControl(clock:Clock):ClockControl = {
    if (clock.isInstanceOf[ClockControl]) clock.asInstanceOf[ClockControl]
    else ClockControl.default
  }

  implicit def OptionModelFixed2Fixed(fixed:Option[Model.Fixed]):Option[FixedType] = {
    fixed match {
      case None    => None
      case Some(x) => if (x.isInstanceOf[FixedType]) Some(x.asInstanceOf[FixedType])
                      else if (x == Model.NoFixed) Some(FixedType.None)
                      else Some(FixedType.signed(x.width,x.fraction))
    }
  }


  implicit def ModelFixed2Fixed(x:Model.Fixed):FixedType = {
    if (x.isInstanceOf[FixedType]) (x.asInstanceOf[FixedType])
    else if (x == Model.NoFixed) (FixedType.None)
    else (FixedType.signed(x.width,x.fraction))
  }

  implicit def SignalType2OpType(op:SignalType):OpType = {
    if (op.isInstanceOf[OpType]) op.asInstanceOf[OpType]
    else OpType.Signal
  }
}