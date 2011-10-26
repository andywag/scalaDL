package com.simplifide.generate.language

import com.simplifide.generate.parser.model.{Model, Expression}
import com.simplifide.generate.parser.ObjectFactory
import com.simplifide.generate.parser.math.{Multiplier}
import com.simplifide.generate.blocks.basic.fixed.complex.ComplexMultiplySegment
import com.simplifide.generate.blocks.basic.fixed.AdditionSegment2

/**
 * Created by IntelliJ IDEA.
 * User: andy
 * Date: 8/8/11
 * Time: 8:42 PM
 * To change this template use File | Settings | File Templates.
 */

class SignalFactory {

}

object SignalFactory {
   def round(expression:Expression,fixed:Model.Fixed,internal:Model.Fixed = Model.NoFixed):Expression = {
    expression match {
      case a:ComplexMultiplySegment => a.createRound
      case AdditionSegment2(name,x,y,sign,_,_) => return ObjectFactory.AdderRound(x, y, sign, fixed, internal)
      case Multiplier(x,y)      => return ObjectFactory.MultRound(x,y,fixed,internal)
      case _                    => return ObjectFactory.RoundInt(expression,fixed,internal)
    }
  }

  def roundClip(expression:Expression,fixed:Model.Fixed,internal:Model.Fixed  = Model.NoFixed):Expression = {
    expression match {
      case a:ComplexMultiplySegment => a.createRoundClip
      case AdditionSegment2(name,x,y,sign,_,_) => return ObjectFactory.AdderRoundClip(x,y,sign, fixed, internal)
      case Multiplier(x,y)      => return ObjectFactory.MultRoundClip(x,y,fixed,internal)
      case _                    => return ObjectFactory.RoundClip(expression,fixed,internal)
    }
  }

  def truncate(expression:Expression,fixed:Model.Fixed,internal:Model.Fixed  = Model.NoFixed):Expression = {
    expression match {
      case a:ComplexMultiplySegment => a.createTruncate
      case AdditionSegment2(name,x,y,sign,_,_) => return  ObjectFactory.AdderTrunc(x,y,sign,fixed,internal)
      case Multiplier(x,y)      => return ObjectFactory.MultTrunc(x,y,fixed,internal)
      case _                    => return ObjectFactory.Truncate(expression,fixed,internal)
    }
  }

  def truncateClip(expression:Expression,fixed:Model.Fixed,internal:Model.Fixed  = Model.NoFixed):Expression = {
    expression match {
      case a:ComplexMultiplySegment => a.createTruncateClip
      case AdditionSegment2(name,x,y,sign,_,_)    => return ObjectFactory.AdderTruncClip(x,y,sign,fixed,internal)
      case Multiplier(x,y)         => return ObjectFactory.MultTruncClip(x,y,fixed,internal)
      case _                       => return ObjectFactory.TruncateClip(expression,fixed,internal)
    }
  }
}