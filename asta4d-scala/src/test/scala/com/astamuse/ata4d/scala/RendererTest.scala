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

class RendererTest extends BaseTest{
  @Test
  def testGetSingle() {

    def renderFn(): Renderer = {
      "#someIdForInt" -> 12345 &
      "#someIdForLong" -> 12345L &
      "#someIdForBool" -> true &
      "#someIdForStr" -> "a str" &
      "#someIdForNull" -> (null.asInstanceOf[Object]) &
      "#someIdForClear"-> Clear &
      "#someIdForElementSetter" -> new ChildReplacer("<div></div>".parseAsSingle()) & 
      "#someIdForElement" -> "<div>eee</div>".parseAsSingle() &
      "#someIdForRenderer" -> ("#value" -> "value")
    }

    val tester: RendererTester = RendererTester.forRenderer(renderFn());
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

    val recursiveTester = RendererTester.forRenderer(tester.get("#someIdForRenderer").asInstanceOf[Renderer]);
    Assert.assertEquals(recursiveTester.get("#value"), "value");
    
    // noop test
    val noopTester = RendererTester.forRenderer(Renderer.create());
    Assert.assertTrue(noopTester.noOp());

  }
  
  @Test
  def testGetAttr() {
    def renderFn(): Renderer = {
      ("#id", "+class") -> "yyy" & // use parentheses to clarify the operation target
      ("#id", "-class") -> "zzz" &
      "#id" -> "+class" -> "xxx" & // use tuple arrow straightaway to simplify input
      "#id" -> "value" -> "hg" &
      "#id" -> "href" -> null & 
      "#X" -> "value" -> new Date(123456L)
    }
    
    val tester = RendererTester.forRenderer(renderFn());
    Assert.assertEquals(tester.getAttrAsList("#id", "+class"), Arrays.asList("yyy", "xxx"));
    Assert.assertEquals(tester.getAttr("#id", "-class"), "zzz");
    Assert.assertEquals(tester.getAttr("#id", "value"), "hg");
    Assert.assertEquals(tester.getAttr("#id", "href"), null);
    Assert.assertEquals(tester.getAttr("#X", "value"), new Date(123456L));

    Assert.assertFalse(tester.noOp("#id", "+class"));
    Assert.assertFalse(tester.noOp("#id", "-class"));
    Assert.assertFalse(tester.noOp("#id", "value"));

    Assert.assertTrue(tester.noOp("#id", "+cccc"));
    Assert.assertTrue(tester.noOp("#id", "-cccc"));
    Assert.assertTrue(tester.noOp("#id", "cccc"));
  }
}