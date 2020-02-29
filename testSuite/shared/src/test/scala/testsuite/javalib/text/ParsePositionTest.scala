package testsuite.javalib.text

import java.text.ParsePosition

class ParsePositionTest extends munit.FunSuite {
  test("constructors") {
    val p1 = new ParsePosition(1)
    assertEquals(1, p1.getIndex())
    assertEquals(-1, p1.getErrorIndex())
  }

  test("setters") {
    val p1 = new ParsePosition(1)
    assertEquals(1, p1.getIndex())
    assertEquals(-1, p1.getErrorIndex())

    p1.setIndex(2)
    p1.setErrorIndex(2)

    assertEquals(2, p1.getIndex())
    assertEquals(2, p1.getErrorIndex())
  }

  test("equals_hash_code") {
    val p1 = new ParsePosition(1)
    assertEquals(p1, p1)
    assertEquals(p1.hashCode(), p1.hashCode())

    val p2 = new ParsePosition(1)
    assertEquals(p1, p2)
    assertEquals(p1.hashCode(), p2.hashCode())

    val p3 = new ParsePosition(2)
    assert(!p1.equals(p3))
    assert(p1.hashCode() != p3.hashCode())

    p1.setIndex(4)
    assert(!p1.equals(p2))
    assert(p1.hashCode() != p2.hashCode())

    val p5 = new ParsePosition(1)
    val p6 = new ParsePosition(1)
    p5.setErrorIndex(1)
    assert(!p5.equals(p6))
    assert(p5.hashCode() != p6.hashCode())
  }

  test("to_string") {
    val p1 = new ParsePosition(1)
    assertEquals("java.text.ParsePosition[index=1,errorIndex=-1]", p1.toString)
  }

}
