package com.astamuse.asta4d.scala

import scala.Tuple2
import com.astamuse.asta4d.render.Renderer
import com.astamuse.asta4d.render.SpecialRenderer
import com.astamuse.asta4d.render.Renderable
import com.astamuse.asta4d.render.ElementSetter
import com.astamuse.asta4d.Component
import com.astamuse.asta4d.util.ElementUtil
import org.jsoup.nodes.Element
import scala.collection.Iterable
import scala.collection.JavaConversions
import scala.collection.JavaConversions._
import com.astamuse.asta4d.render.test.RendererTester
import com.astamuse.asta4d.render.test.TestableElementWrapper

/**
 * This object is to supply implicit conventions for simplify rendering and rendering test.
 */
object R {

  implicit class RendererTesterExtension(tester: RendererTester){
    def getAsScalaList[A](selector: String)(implicit typ: Manifest[A]) : List[A] = {
      val list = tester.getAsList(selector, typ.runtimeClass)
      list.toList.asInstanceOf[List[A]]
    }
    def getAsRendererTesterScalaList(selector: String) : List[RendererTester] = {
      tester.getAsScalaList[Renderer](selector).map { RendererTester.forRenderer(_) }
    }
  }
  
  implicit class StringExtension(s: String){
    def parseAsSingle() = ElementUtil.parseAsSingle(s)
  }
  
  implicit class RendererExtension(r: Renderer){
    def &(nr: Renderer) = r.add(nr)
  }
  
  implicit class Tuple2Extension[A](t: Tuple2[String, A]){
    def &(nr: Renderer) = {
      convert2Renderer(t).add(nr)
    }
    def asRenderer() = convert2Renderer(t)
    
    protected def convert2Renderer(x: Tuple2[String, A]) = tuple2renderer(x)
  }
    
  implicit class Tuple2ExtensionList(t: Tuple2[String, scala.collection.Iterable[Any]]) extends Tuple2Extension[scala.collection.Iterable[Any]](t){
    override protected def convert2Renderer(x: Tuple2[String, scala.collection.Iterable[Any]]) = tuple2rendererIterable(x)
  }
  
  implicit def tuple2renderer(t: Tuple2[String, Any]) = {
    Renderer.create(t._1, t._2)
  }
  
  implicit def tuple2rendererIterable(t: Tuple2[String, scala.collection.Iterable[Any]]) = {
    Renderer.create(t._1, JavaConversions.asJavaIterable(t._2))
  }
  
  /**
   * "selector" -> ("selector"->"value")
   * 
   */
  implicit def tuple2rendererSeqTuple(t: Tuple2[String, Tuple2[String, Any]]) = {
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
  
  implicit def tuple2rendererSpecialRenderer(t: Tuple2[String, SpecialRenderer]) = {
    Renderer.create(t._1, t._2)
  }
  
  implicit def tuple2rendererRenderer(t: Tuple2[String, Renderer]) = {
    Renderer.create(t._1, t._2)
  }

  implicit def tuple2rendererRenderable(t: Tuple2[String, Renderable]) = {
    Renderer.create(t._1, t._2)
  }
  
  implicit def tuple2rendererElem(t: Tuple2[String, Element]) = {
    Renderer.create(t._1, t._2)
  }
  
  implicit def tuple2rendererElementSetter(t: Tuple2[String, ElementSetter]) = {
    Renderer.create(t._1, t._2)
  }
  
  implicit def tuple2rendererComponent(t: Tuple2[String, Component]) = {
    Renderer.create(t._1, t._2)
  }
  
  /**
   * to handle Func0[Renderer] as Renderable
   */
  implicit def tuple2rendererF0(t: Tuple2[String, Function0[Renderer]]) = {
    Renderer.create(t._1, new Renderable(){
      def render(): Renderer = t._2.apply()
    })
  }
  
  implicit class Tuple2AttrExtension(t: Tuple2[Tuple2[String, String], Any]){
    def &(nr: Renderer) = {
      tuple2AttrRenderer(t).add(nr)
    }
  }
  
  implicit def tuple2AttrRenderer(t: Tuple2[Tuple2[String, String], Any]) = {
    Renderer.create(t._1._1, t._1._2, t._2)
  }
  
  implicit def tuple2AttrRendererLong(t: Tuple2[Tuple2[String, String], Long]) = {
    Renderer.create(t._1._1, t._1._2, t._2.asInstanceOf[java.lang.Long])
  }
  
  implicit def tuple2AttrRendererInt(t: Tuple2[Tuple2[String, String], Int]) = {
    Renderer.create(t._1._1, t._1._2, t._2.asInstanceOf[java.lang.Integer])
  }
  
  implicit def tuple2AttrRendererBool(t: Tuple2[Tuple2[String, String], Boolean]) = {
    Renderer.create(t._1._1, t._1._2, t._2.asInstanceOf[java.lang.Boolean])
  }
  
  implicit def tuple2AttrRendererStr(t: Tuple2[Tuple2[String, String], String]) = {
    Renderer.create(t._1._1, t._1._2, t._2)
  }

}