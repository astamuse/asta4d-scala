# asta4d-scala

- simple rendering

  ```scala
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
  ```
  
- attr rendering

  ```scala
  def renderFn(): Renderer = {
    ("#id", "+class") -> "yyy" & // use parentheses to clarify the operation target
    ("#id", "-class") -> "zzz" &
    "#id" -> "+class" -> "xxx" & // use tuple arrow straightaway to simplify input
    "#id" -> "value" -> "hg" &
    "#id" -> "href" -> null & 
    "#X" -> "value" -> new Date(123456L)
  }
  ```