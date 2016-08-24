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
    assertEquals(0, f.getGroupingSize)
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

    f.setGroupingSize(5)
    assertEquals(5, f.getGroupingSize)
    f.setGroupingSize(-1)
    assertEquals(-1, f.getGroupingSize)
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

  @Test def test_grouping_size_parsing(): Unit = {
    val f1 = new DecimalFormat("##0.#####E0")
    assertEquals(0, f1.getGroupingSize)

    val f2 = new DecimalFormat("##,####,####")
    assertEquals(4, f2.getGroupingSize)

    val f3 = new DecimalFormat("##,####,####.##")
    assertEquals(4, f3.getGroupingSize)

    val f4 = new DecimalFormat("")
    assertEquals(3, f4.getGroupingSize)

    val f5 = new DecimalFormat("#####")
    assertEquals(0, f5.getGroupingSize)

    val f6 = new DecimalFormat("#,##,###,####")
    assertEquals(4, f6.getGroupingSize)

    val f7 = new DecimalFormat("######,####")
    assertEquals(4, f7.getGroupingSize)

    val f8 = new DecimalFormat("#,##0%")
    assertEquals(3, f8.getGroupingSize)

    val f9 = new DecimalFormat("#,##0 %")
    assertEquals(3, f9.getGroupingSize)

    val f10 = new DecimalFormat("#A,##P0W%")
    assertEquals(3, f10.getGroupingSize)

    val f11 = new DecimalFormat("#A,## #P0W%")
    assertEquals(4, f11.getGroupingSize)

    val f12 = new DecimalFormat("###,#'#'")
    assertEquals(1, f12.getGroupingSize)
  }

  @Test def test_prefixes_parsing(): Unit = {
    val f1 = new DecimalFormat("#,##0.00")
    assertEquals("", f1.getPositivePrefix())
    assertEquals("", f1.getPositiveSuffix())
    assertEquals("-", f1.getNegativePrefix())
    assertEquals("", f1.getNegativeSuffix())

    val f2 = new DecimalFormat("(#,##0.00)")
    assertEquals("(", f2.getPositivePrefix())
    assertEquals(")", f2.getPositiveSuffix())
    assertEquals("-(", f2.getNegativePrefix())
    assertEquals(")", f2.getNegativeSuffix())

    val f3 = new DecimalFormat("##0.#####E0")
    assertEquals("", f3.getPositivePrefix())
    assertEquals("", f3.getPositiveSuffix())
    assertEquals("-", f3.getNegativePrefix())
    assertEquals("", f3.getNegativeSuffix())
  }
}
