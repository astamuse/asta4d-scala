package com.astamuse.ata4d.scala

import com.astamuse.asta4d.test.render.infra.BaseTest
import org.testng.annotations.Test
import com.astamuse.asta4d.render.Renderer
import com.astamuse.asta4d.render.test.RendererTester
import org.testng.Assert
import com.astamuse.asta4d.scala.R._
import com.astamuse.asta4d.render.SpecialRenderer.Clear
import com.astamuse.asta4d.render.ChildReplacer
import com.astamuse.asta4d.util.ElementUtil
import com.astamuse.asta4d.render.test.TestableElementWrapper
import java.util.Arrays
import java.util.Date
import com.astamuse.asta4d.render.Renderable
import java.util.stream.Collectors

import org.scalatest.Assertions._

class RendererTest extends BaseTest{
  @Test
  def testGetSingle() {

    def render(): Renderer = {
      "#someIdForInt" -> 12345 &
      "#someIdForLong" -> 12345L &
      "#someIdForBool" -> true &
      "#someIdForStr" -> "a str" &
      "#someIdForNull" -> (null.asInstanceOf[Object]) &
      "#someIdForClear"-> Clear &
      "#someIdForElementSetter" -> new ChildReplacer("<div></div>".parseAsSingle()) & 
      "#someIdForElement" -> "<div>eee</div>".parseAsSingle() &
      "#someIdForRenderer" -> ("#value" -> "value") &
      "#someIdForRenderable" -> (()=>("#id" -> "xx").asRenderer())
    }

    val tester: RendererTester = RendererTester.forRenderer(render());
    Assert.assertFalse(tester.noOp());
    Assert.assertEquals(tester.get("#someIdForInt"), 12345);
    Assert.assertEquals(tester.get("#someIdForLong"), 12345L);
    Assert.assertEquals(tester.get("#someIdForStr"), "a str");
    Assert.assertEquals(tester.get("#someIdForNull"), null);
    Assert.assertEquals(tester.get("#someIdForClear"), Clear);
    
    Assert.assertFalse(tester.noOp("#someIdForClear"));
    Assert.assertTrue(tester.noOp("#notexistop"));

    Assert.assertEquals(tester.get("#someIdForElementSetter"), new ChildReplacer(ElementUtil.parseAsSingle("<div></div>")));
    Assert.assertEquals(tester.get("#someIdForElement"), TestableElementWrapper.parse("<div>eee</div>"));

    var recursiveTester = RendererTester.forRenderer(tester.get("#someIdForRenderer").asInstanceOf[Renderer]);
    Assert.assertEquals(recursiveTester.get("#value"), "value");
    
    recursiveTester = RendererTester.forRenderer(tester.get("#someIdForRenderable").asInstanceOf[Renderable].render());
    Assert.assertEquals(recursiveTester.get("#id"), "xx");

  }
  
  @Test
  def testGetList() {
    
    def render() = {
      "#someIdForInt" -> List(123, 456, 789) &
      "#someIdForLong" -> List(123L, 456L, 789L) &
      "#someIdForBool" -> List(true, true, false) &
      "#someIdForStr" -> List("str1", "str2", "str3") &
      "#someIdForElementSetter" -> 
        List(new ChildReplacer("<div>1</div>".parseAsSingle()), new ChildReplacer("<div>2</div>".parseAsSingle())) &
      "#someIdForElement" -> List("<div>1</div>".parseAsSingle(), "<div>2</div>".parseAsSingle())
    }
    
    // perform test
    val tester = RendererTester.forRenderer(render);
    println(tester.getAsList("#someIdForInt"))
    Assert.assertEquals(tester.getAsList("#someIdForInt"), Arrays.asList(123, 456, 789));
    Assert.assertEquals(tester.getAsList("#someIdForLong"), Arrays.asList(123L, 456L, 789L));
    Assert.assertEquals(tester.getAsList("#someIdForBool"), Arrays.asList(true, true, false));
    Assert.assertEquals(tester.getAsList("#someIdForStr"), Arrays.asList("str1", "str2", "str3"));

    Assert.assertEquals(tester.getAsList("#someIdForElementSetter"),
            Arrays.asList(new ChildReplacer("<div>1</div>".parseAsSingle()),
                    new ChildReplacer("<div>2</div>".parseAsSingle())));

    Assert.assertEquals(tester.getAsList("#someIdForElement"),
              Arrays.asList(TestableElementWrapper.parse("<div>1</div>"), TestableElementWrapper.parse("<div>2</div>")));

  }
  
  @Test
  def testGetListAsRenderer() {
    
    def render() = {
      "#someIdForRenderer" -> List(123, 456, 789).map { i => {
        "#id"-> ("id-" + i) & "#otherId"-> ("otherId-" + i)
      }}
    }

    val tester = RendererTester.forRenderer(render());

    //to test with java way to make sure everything is OK
    {
      val renderList = tester.getAsList("#someIdForRenderer", classOf[Renderer]);
      val testerList = RendererTester.forRendererList(renderList);
      val confirmIdList = Arrays.asList("123", "456", "789");
      Assert.assertEquals(testerList.size(), 3);
      for (i <- 0 until testerList.size()) {
          val recursiveTester = testerList.get(i);
          Assert.assertEquals(recursiveTester.get("#id"), "id-" + confirmIdList.get(i));
          Assert.assertEquals(recursiveTester.get("#otherId"), "otherId-" + confirmIdList.get(i));
      }
    }
    
    //to test with scala way (by ScalaTest)
    {
      val testerList = tester.getAsRendererTesterScalaList("#someIdForRenderer")
      assert(List("id-123", "id-456", "id-789") === testerList.map(_.get("#id")))
      assert(List("otherId-123", "otherId-456", "otherId-789") === testerList.map(_.get("#otherId")))
    }
  }
  
  @Test
  def testGetAttr() {
    //return type is even not necessary since the & operator will always convert the tuple to Renderer
    def render() = { 
      ("#id", "+class") -> "yyy" & // use parentheses to clarify the operation target
      ("#id", "-class") -> "zzz" &
      "#id" -> "+class" -> "xxx" & // use tuple arrow straightaway to simplify input
      "#id" -> "value" -> new Date(123456L) &
      "#id" -> "href" -> null & 
      "#idstr" -> "value" -> "hg" &
      "#idint" -> "value" -> 3 &
      "#idlong" -> "value" -> 3L &
      "#idbool" -> "value" -> true 
      
    }
    
    val tester = RendererTester.forRenderer(render());

    Assert.assertEquals(tester.getAttrAsList("#id", "+class"), Arrays.asList("yyy", "xxx"));
    Assert.assertEquals(tester.getAttr("#id", "-class"), "zzz");
    Assert.assertEquals(tester.getAttr("#id", "value"), new Date(123456L));
    Assert.assertEquals(tester.getAttr("#id", "href"), null);

    Assert.assertEquals(tester.getAttr("#idstr", "value"), "hg");
    Assert.assertEquals(tester.getAttr("#idint", "value"), "3");
    Assert.assertEquals(tester.getAttr("#idlong", "value"), "3");
    Assert.assertEquals(tester.getAttr("#idbool", "value"), "true");

    Assert.assertFalse(tester.noOp("#id", "+class"));
    Assert.assertFalse(tester.noOp("#id", "-class"));
    Assert.assertFalse(tester.noOp("#id", "value"));
    Assert.assertFalse(tester.noOp("#id", "href"));

    Assert.assertFalse(tester.noOp("#idstr", "value"));
    Assert.assertFalse(tester.noOp("#idint", "value"));
    Assert.assertFalse(tester.noOp("#idlong", "value"));
    Assert.assertFalse(tester.noOp("#idbool", "value"));

    Assert.assertTrue(tester.noOp("#id", "+cccc"));
    Assert.assertTrue(tester.noOp("#id", "-cccc"));
    Assert.assertTrue(tester.noOp("#id", "cccc"));
  }
}