package testsuite.javalib.text

import java.text.NumberFormat

import org.junit.Test
import org.junit.Assert._

class NumberFormatTest {
  @Test def test_constants(): Unit = {
    assertEquals(0, NumberFormat.INTEGER_FIELD)
    assertEquals(1, NumberFormat.FRACTION_FIELD)
  }
}
