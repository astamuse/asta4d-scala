package com.astamuse.asta4d.scala

import scala.Tuple2
import com.astamuse.asta4d.render.Renderer
import com.astamuse.asta4d.render.SpecialRenderer
import com.astamuse.asta4d.render.Renderable
import com.astamuse.asta4d.render.ElementSetter
import com.astamuse.asta4d.Component
import com.astamuse.asta4d.util.ElementUtil
import org.jsoup.nodes.Element

object R {

  implicit class StringExtension(s: String){
    def parseAsSingle() = ElementUtil.parseAsSingle(s)
  }
  
  implicit class RendererExtension(r: Renderer){
    def &(nr: Renderer) = r.add(nr)
  }
  
  implicit class Tuple2Extension(t: Tuple2[String, Any]){
    def &(nr: Renderer) = {
      tuple2renderer(t).add(nr)
    }
  }
  
  implicit def tuple2renderer(t: Tuple2[String, Any]) = {
    Renderer.create(t._1, t._2)
  }
  
  implicit def tuple2Wrenderer(t: Tuple2[String, Tuple2[String, Any]]) = {
    Renderer.create(t._1, tuple2renderer(t._2))
  }
  
  implicit def tuple2rendererInt(t: Tuple2[String, Int]) = {
    Renderer.create(t._1, t._2.asInstanceOf[Integer])
  }
  
  implicit def tuple2rendererLong(t: Tuple2[String, Long]) = {
    Renderer.create(t._1, t._2.asInstanceOf[java.lang.Long])
  }
  
  implicit def tuple2rendererBool(t: Tuple2[String, Boolean]) = {
    Renderer.create(t._1, t._2.asInstanceOf[java.lang.Boolean])
  }
  
  implicit def tuple2rendererSR(t: Tuple2[String, SpecialRenderer]) = {
    Renderer.create(t._1, t._2)
  }
  
  implicit def tuple2rendererR(t: Tuple2[String, Renderer]) = {
    Renderer.create(t._1, t._2)
  }

  implicit def tuple2rendererRb(t: Tuple2[String, Renderable]) = {
    Renderer.create(t._1, t._2)
  }
  
  implicit def tuple2rendererElem(t: Tuple2[String, Element]) = {
    Renderer.create(t._1, t._2)
  }
  
  implicit def tuple2rendererEs(t: Tuple2[String, ElementSetter]) = {
    Renderer.create(t._1, t._2)
  }
  
  implicit def tuple2rendererCom(t: Tuple2[String, Component]) = {
    Renderer.create(t._1, t._2)
  }
  
  
  implicit class Tuple2AttrExtension(t: Tuple2[Tuple2[String, String], Any]){
    def &(nr: Renderer) = {
      tuple2AttrRenderer(t).add(nr)
    }
  }
  
  implicit def tuple2AttrRenderer(t: Tuple2[Tuple2[String, String], Any]) = {
    Renderer.create(t._1._1, t._1._2, t._2)
  }

}