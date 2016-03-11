package com.astamuse.asta4d.scala.sample

import com.astamuse.asta4d.web.dispatch.mapping.UrlMappingRuleInitializer
import com.astamuse.asta4d.web.dispatch.mapping.handy.HandyRuleSet
import com.astamuse.asta4d.scala.handyrule.SHandyRuleSet

class UrlRules extends UrlMappingRuleInitializer[SHandyRuleSet]{
  override def initUrlMappingRules(rules: SHandyRuleSet){
    
    "/" -> "/templates/index.html"
    
    
    rules.add("/", "/templates/index.html")
    rules.add("/basicRendering",  "/templates/basicRendering.html")
    
    rules.add("").handler((x: Int)=>{
      
    }).forward("", "");
    
    rules.add("", "").attribute("");
    
    rules.add("").attribute("").handler((x: Int)=>{
      
    }).forward("", "");
    
  }
}