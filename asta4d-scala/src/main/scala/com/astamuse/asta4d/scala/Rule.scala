package com.astamuse.asta4d.scala

import com.astamuse.asta4d.web.dispatch.mapping.ext.HandyRuleWithHandler

/**
 * Do not use it, just for experiment, does not work.
 */
@deprecated
object Rule{
  implicit class HandyRuleWithHandlerExtension(r: HandyRuleWithHandler) {
    def handlerx[A, B](f: Function1[A, B])={
      println("hffh")
      r.handler(f);
    }
  }
}