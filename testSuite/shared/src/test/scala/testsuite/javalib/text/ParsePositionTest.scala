package testsuite.javalib.text

import java.text.{FieldPosition, Format, ParsePosition}

import org.junit.Assert._
import org.junit.Test

class ParsePositionTest {
  @Test def test_constructors(): Unit = {
    val p1 = new ParsePosition(1)
    assertEquals(1, p1.getIndex())
    assertEquals(-1, p1.getErrorIndex())
  }

  @Test def test_setters(): Unit = {
    val p1 = new ParsePosition(1)
    assertEquals(1, p1.getIndex())
    assertEquals(-1, p1.getErrorIndex())

    p1.setIndex(2)
    p1.setErrorIndex(2)

    assertEquals(2, p1.getIndex())
    assertEquals(2, p1.getErrorIndex())
  }

  @Test def test_equals_hash_code(): Unit = {
    val p1 = new ParsePosition(1)
    assertEquals(p1, p1)
    assertEquals(p1.hashCode(), p1.hashCode())

    val p2 = new ParsePosition(1)
    assertEquals(p1, p2)
    assertEquals(p1.hashCode(), p2.hashCode())

    val p3 = new ParsePosition(2)
    assertFalse(p1.equals(p3))
    assertFalse(p1.hashCode() == p3.hashCode())

    p1.setIndex(4)
    assertFalse(p1.equals(p2))
    assertFalse(p1.hashCode() == p2.hashCode())

    val p5 = new ParsePosition(1)
    val p6 = new ParsePosition(1)
    p5.setErrorIndex(1)
    assertFalse(p5.equals(p6))
    assertFalse(p5.hashCode() == p6.hashCode())
  }

  @Test def test_to_string(): Unit = {
    val p1 = new ParsePosition(1)
    assertEquals("java.text.ParsePosition[index=1,errorIndex=-1]", p1.toString)
  }
}
