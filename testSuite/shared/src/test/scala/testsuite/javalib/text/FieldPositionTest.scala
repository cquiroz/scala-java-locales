package testsuite.javalib.text

import java.text.{FieldPosition, Format}

import org.junit.Test
import org.junit.Assert._

class FieldPositionTest {
  case class TestField(name: String) extends Format.Field(name)

  @Test def test_constructors(): Unit = {
    val field = TestField("abc")

    val f1 = new FieldPosition(field, 1)
    assertEquals(field, f1.getFieldAttribute())
    assertEquals(1, f1.getField())
    assertEquals(0, f1.getBeginIndex())
    assertEquals(0, f1.getEndIndex())

    val f2 = new FieldPosition(field)
    assertEquals(field, f2.getFieldAttribute())
    assertEquals(-1, f2.getField())
    assertEquals(0, f2.getBeginIndex())
    assertEquals(0, f2.getEndIndex())

    val f3 = new FieldPosition(1)
    assertNull(f3.getFieldAttribute())
    assertEquals(1, f3.getField())
    assertEquals(0, f3.getBeginIndex())
    assertEquals(0, f3.getEndIndex())
  }

  @Test def test_begin_end_index(): Unit = {
    val field = TestField("abc")

    val f1 = new FieldPosition(field, 1)
    f1.setBeginIndex(10)
    assertEquals(10, f1.getBeginIndex())
    f1.setBeginIndex(-10)
    assertEquals(-10, f1.getBeginIndex())

    f1.setEndIndex(10)
    assertEquals(10, f1.getEndIndex())
    f1.setEndIndex(-10)
    assertEquals(-10, f1.getEndIndex())
  }

  @Test def test_equals_hash_code(): Unit = {
    val field = TestField("abc")

    val f1 = new FieldPosition(field, 1)
    assertEquals(f1, f1)
    assertEquals(f1.hashCode(), f1.hashCode())

    val f2 = new FieldPosition(field, 1)
    assertEquals(f1, f2)
    assertEquals(f1.hashCode(), f2.hashCode())

    val f3 = new FieldPosition(field)
    assertFalse(f1.equals(f3))
    assertFalse(f1.hashCode() == f3.hashCode())

    val f4 = new FieldPosition(1)
    assertFalse(f1.equals(f4))
    // hashcode is broken on the JVM
    assertEquals(f1.hashCode(), f4.hashCode())

    f2.setBeginIndex(1)
    assertFalse(f1.equals(f2))
    assertFalse(f1.hashCode() == f2.hashCode())

    val f5 = new FieldPosition(field, 1)
    f5.setEndIndex(1)
    assertFalse(f1.equals(f5))
    assertFalse(f1.hashCode() == f5.hashCode())
  }

  @Test def test_to_string(): Unit = {
    val field = TestField("abc")
    val f1 = new FieldPosition(field, 1)
    assertEquals("java.text.FieldPosition[field=1,attribute=testsuite.javalib.text.FieldPositionTest$TestField(abc),beginIndex=0,endIndex=0]", f1.toString) // scalastyle:off
  }
}
