package com.astamuse.asta4d.scala.sample

import com.astamuse.asta4d.web.dispatch.mapping.UrlMappingRuleInitializer
import com.astamuse.asta4d.web.dispatch.mapping.ext.UrlMappingRuleHelper
import com.astamuse.asta4d.scala.Rule._

class UrlRules extends UrlMappingRuleInitializer{
  override def initUrlMappingRules(rules: UrlMappingRuleHelper){
    
    "/" -> "/templates/index.html"
    
    
    rules.add("/", "/templates/index.html")
    rules.add("/basicRendering",  "/templates/basicRendering.html")
    
    rules.add("").handlerx((x: Int)=>{
      
    });
    
  }
}