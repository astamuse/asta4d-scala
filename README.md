# asta4d-scala

asta4d-scala is a project to afford more scala-like APIs for the original asta4d project.

## Rendering

- simple rendering

    ```scala
    import com.astamuse.asta4d.scala.R._
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
    ```
  
- list rendering

    ```scala
    import com.astamuse.asta4d.scala.R._
    def render() = {
      "#someIdForRenderer" -> List(123, 456, 789).map { i => {
        "#id"-> ("id-" + i) & "#otherId"-> ("otherId-" + i)
      }}
    }
    ```

    ```scala
    import com.astamuse.asta4d.scala.R._
    def render() = {
      "#someIdForInt" -> List(123, 456, 789) &
      "#someIdForLong" -> List(123L, 456L, 789L) &
      "#someIdForBool" -> List(true, true, false) &
      "#someIdForStr" -> List("str1", "str2", "str3") &
      "#someIdForElementSetter" -> 
        List(new ChildReplacer("<div>1</div>".parseAsSingle()), new ChildReplacer("<div>2</div>".parseAsSingle())) &
      "#someIdForElement" -> List("<div>1</div>".parseAsSingle(), "<div>2</div>".parseAsSingle())
    }
    ```

- attr rendering

    ```scala
    import com.astamuse.asta4d.scala.R._
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
    ```

## using RendererTester with scala way(by ScalaTest as sample)

    ```scala
    import com.astamuse.asta4d.scala.R._
    val tester = RendererTester.forRenderer(render())
    val testerList = tester.getAsRendererTesterScalaList("#someIdForRenderer")
      
    assert(List("id-123", "id-456", "id-789") === testerList.map(_.get("#id")))
    assert(List("otherId-123", "otherId-456", "otherId-789") === testerList.map(_.get("#otherId")))
    ```

