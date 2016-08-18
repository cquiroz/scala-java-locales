package testsuite.javalib.text

import java.math.RoundingMode
import java.text.{DecimalFormat, DecimalFormatSymbols}

import org.junit.Assert._
import org.junit.Test

class DecimalFormatTest {
  @Test def test_constructor(): Unit = {
    val f = new DecimalFormat("##0.#####E0")
    assertEquals("##0.#####E0", f.toPattern)
    assertEquals(DecimalFormatSymbols.getInstance(), f.getDecimalFormatSymbols)
    assertFalse(f.isParseIntegerOnly)
    assertEquals(3, f.getMaximumIntegerDigits)
    assertEquals(1, f.getMinimumIntegerDigits)
    assertEquals(5, f.getMaximumFractionDigits)
    assertEquals(0, f.getMinimumFractionDigits)
    assertEquals(RoundingMode.HALF_EVEN, f.getRoundingMode)
    assertFalse(f.isGroupingUsed)

    assertEquals("", f.getPositivePrefix)
    assertEquals("", f.getPositiveSuffix)
    assertEquals("-", f.getNegativePrefix)
    assertEquals("", f.getNegativeSuffix)
    assertEquals(1, f.getMultiplier)
  }

  @Test def test_setters(): Unit = {
    val f = new DecimalFormat("##0.#####E0")
    f.setParseIntegerOnly(true)
    assertTrue(f.isParseIntegerOnly)

    f.setMaximumIntegerDigits(0)
    assertEquals(0, f.getMaximumIntegerDigits)
    f.setMaximumIntegerDigits(1)
    // Negatives are allowed but rounded to 0
    f.setMaximumIntegerDigits(-1)
    assertEquals(0, f.getMaximumIntegerDigits)

    f.setMinimumIntegerDigits(0)
    assertEquals(0, f.getMinimumIntegerDigits)
    f.setMinimumIntegerDigits(1)
    // Negatives are allowed but rounded to 0
    f.setMinimumIntegerDigits(-1)
    assertEquals(0, f.getMinimumIntegerDigits)

    f.setRoundingMode(RoundingMode.CEILING)
    assertEquals(RoundingMode.CEILING, f.getRoundingMode)

    f.setMaximumFractionDigits(0)
    assertEquals(0, f.getMaximumFractionDigits)
    f.setMaximumFractionDigits(1)
    // Negatives are allowed but rounded to 0
    f.setMaximumFractionDigits(-1)
    assertEquals(0, f.getMaximumFractionDigits)

    f.setMinimumFractionDigits(0)
    assertEquals(0, f.getMinimumFractionDigits)
    f.setMinimumFractionDigits(1)
    // Negatives are allowed but rounded to 0
    f.setMinimumFractionDigits(-1)
    assertEquals(0, f.getMinimumFractionDigits)

    f.setGroupingUsed(true)
    assertTrue(f.isGroupingUsed)

    f.setPositivePrefix("A")
    assertEquals("A", f.getPositivePrefix)

    f.setNegativePrefix("B")
    assertEquals("B", f.getNegativePrefix)

    f.setPositiveSuffix("C")
    assertEquals("C", f.getPositiveSuffix)

    f.setNegativeSuffix("D")
    assertEquals("D", f.getNegativeSuffix)

    // Check for null arguments
    f.setPositivePrefix(null)
    assertNull(f.getPositivePrefix)
    f.setPositiveSuffix(null)
    assertNull(f.getPositiveSuffix)
    f.setNegativePrefix(null)
    assertNull(f.getNegativePrefix)
    f.setNegativeSuffix(null)
    assertNull(f.getNegativeSuffix)

    f.setMultiplier(2)
    assertEquals(2, f.getMultiplier)

    f.setMultiplier(-1)
    assertEquals(-1, f.getMultiplier)
  }

  @Test def test_max_min_interactions(): Unit = {
    val f = new DecimalFormat("##0.#####E0")

    f.setMinimumIntegerDigits(3)
    // less than minimum, update minimum
    f.setMaximumIntegerDigits(1)
    assertEquals(1 ,f.getMinimumIntegerDigits)

    // more than maximum, update maximum
    f.setMaximumIntegerDigits(3)
    assertEquals(3 ,f.getMaximumIntegerDigits)

    f.setMinimumFractionDigits(3)
    // less than minimum, update minimum
    f.setMaximumFractionDigits(1)
    assertEquals(1 ,f.getMinimumFractionDigits)

    // more than maximum, update maximum
    f.setMaximumFractionDigits(3)
    assertEquals(3 ,f.getMaximumFractionDigits)
  }
}
