package testsuite.javalib.text

import java.text.{DecimalFormat, DecimalFormatSymbols}

import org.junit.Assert._
import org.junit.Test

class DecimalFormatTest {
  @Test def test_constructor(): Unit = {
    val f = new DecimalFormat("##0.#####E0")
    assertEquals("##0.#####E0", f.toPattern)
    assertEquals(DecimalFormatSymbols.getInstance(), f.getDecimalFormatSymbols)
    assertFalse(f.isParseIntegerOnly)
  }

  @Test def test_setters(): Unit = {
    val f = new DecimalFormat("##0.#####E0")
    f.setParseIntegerOnly(true)
    assertTrue(f.isParseIntegerOnly)
  }
}
