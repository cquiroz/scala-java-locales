package testsuite.javalib.text

import java.math.RoundingMode
import java.text.{DecimalFormat, DecimalFormatSymbols, NumberFormat}

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
    assertFalse(f.isDecimalSeparatorAlwaysShown)
    assertFalse(f.isParseBigDecimal)
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

    f.setDecimalSeparatorAlwaysShown(false)
    assertFalse(f.isDecimalSeparatorAlwaysShown)

    f.setParseBigDecimal(true)
    assertTrue(f.isParseBigDecimal)
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
    assertEquals("", f1.getPositivePrefix)
    assertEquals("", f1.getPositiveSuffix)
    assertEquals("-", f1.getNegativePrefix)
    assertEquals("", f1.getNegativeSuffix)

    val f2 = new DecimalFormat("(#,##0.00)")
    assertEquals("(", f2.getPositivePrefix)
    assertEquals(")", f2.getPositiveSuffix)
    assertEquals("-(", f2.getNegativePrefix)
    assertEquals(")", f2.getNegativeSuffix)

    val f3 = new DecimalFormat("##0.#####E0")
    assertEquals("", f3.getPositivePrefix)
    assertEquals("", f3.getPositiveSuffix)
    assertEquals("-", f3.getNegativePrefix)
    assertEquals("", f3.getNegativeSuffix)
  }

  @Test def test_format_positive_prefix(): Unit = {
    val nf = NumberFormat.getIntegerInstance.asInstanceOf[DecimalFormat]
    nf.setPositivePrefix("ABC")
    assertEquals("ABC123", nf.format(123))
    assertEquals("ABC0", nf.format(0))
    assertEquals("-123", nf.format(-123))
    assertEquals("ABC1,000", nf.format(1000))
    assertEquals("ABC100,000", nf.format(100000))
    assertEquals("ABC10,000,000", nf.format(10000000))
    assertEquals("ABC2,147,483,647", nf.format(Int.MaxValue))
    assertEquals("ABC9,223,372,036,854,775,807", nf.format(Long.MaxValue))
    assertEquals("-1,000", nf.format(-1000))
    assertEquals("-100,000", nf.format(-100000))
    assertEquals("-10,000,000", nf.format(-10000000))
    assertEquals("-2,147,483,648", nf.format(Int.MinValue))
    assertEquals("-9,223,372,036,854,775,808", nf.format(Long.MinValue))
  }

  @Test def test_format_positive_suffix(): Unit = {
    val nf = NumberFormat.getIntegerInstance.asInstanceOf[DecimalFormat]
    nf.setPositiveSuffix("ABC")
    assertEquals("123ABC", nf.format(123))
    assertEquals("0ABC", nf.format(0))
    assertEquals("-123", nf.format(-123))
    assertEquals("1,000ABC", nf.format(1000))
    assertEquals("100,000ABC", nf.format(100000))
    assertEquals("10,000,000ABC", nf.format(10000000))
    assertEquals("2,147,483,647ABC", nf.format(Int.MaxValue))
    assertEquals("9,223,372,036,854,775,807ABC", nf.format(Long.MaxValue))
    assertEquals("-1,000", nf.format(-1000))
    assertEquals("-100,000", nf.format(-100000))
    assertEquals("-10,000,000", nf.format(-10000000))
    assertEquals("-2,147,483,648", nf.format(Int.MinValue))
    assertEquals("-9,223,372,036,854,775,808", nf.format(Long.MinValue))
  }

  @Test def test_format_negative_prefix(): Unit = {
    val nf = NumberFormat.getIntegerInstance.asInstanceOf[DecimalFormat]
    nf.setNegativePrefix("ABC")
    assertEquals("123", nf.format(123))
    assertEquals("0", nf.format(0))
    assertEquals("ABC123", nf.format(-123))
    assertEquals("1,000", nf.format(1000))
    assertEquals("100,000", nf.format(100000))
    assertEquals("10,000,000", nf.format(10000000))
    assertEquals("2,147,483,647", nf.format(Int.MaxValue))
    assertEquals("9,223,372,036,854,775,807", nf.format(Long.MaxValue))
    assertEquals("ABC1,000", nf.format(-1000))
    assertEquals("ABC100,000", nf.format(-100000))
    assertEquals("ABC10,000,000", nf.format(-10000000))
    assertEquals("ABC2,147,483,648", nf.format(Int.MinValue))
    assertEquals("ABC9,223,372,036,854,775,808", nf.format(Long.MinValue))
  }

  @Test def test_format_negative_suffix(): Unit = {
    val nf = NumberFormat.getIntegerInstance.asInstanceOf[DecimalFormat]
    nf.setNegativeSuffix("ABC")
    assertEquals("123", nf.format(123))
    assertEquals("0", nf.format(0))
    assertEquals("-123ABC", nf.format(-123))
    assertEquals("1,000", nf.format(1000))
    assertEquals("100,000", nf.format(100000))
    assertEquals("10,000,000", nf.format(10000000))
    assertEquals("2,147,483,647", nf.format(Int.MaxValue))
    assertEquals("9,223,372,036,854,775,807", nf.format(Long.MaxValue))
    assertEquals("-1,000ABC", nf.format(-1000))
    assertEquals("-100,000ABC", nf.format(-100000))
    assertEquals("-10,000,000ABC", nf.format(-10000000))
    assertEquals("-2,147,483,648ABC", nf.format(Int.MinValue))
    assertEquals("-9,223,372,036,854,775,808ABC", nf.format(Long.MinValue))
  }

  @Test def test_format_multiplier(): Unit = {
    val df = NumberFormat.getIntegerInstance.asInstanceOf[DecimalFormat]
    df.setMultiplier(2)
    assertEquals("246", df.format(123))
    assertEquals("0", df.format(0))
    assertEquals("-246", df.format(-123))
    assertEquals("2,000", df.format(1000))
    assertEquals("200,000", df.format(100000))
    assertEquals("20,000,000", df.format(10000000))
    //assertEquals("4,294,967,294", df.format(Int.MaxValue))
    //assertEquals("18,446,744,073,709,551,614", df.format(Long.MaxValue))
    assertEquals("-2,000", df.format(-1000))
    assertEquals("-200,000", df.format(-100000))
    assertEquals("-20,000,000", df.format(-10000000))
    //assertEquals("-4,294,967,296", df.format(Int.MinValue))
    //assertEquals("-18,446,744,073,709,551,616", df.format(Long.MinValue))

    df.setMultiplier(100)
    assertEquals("12,300", df.format(123))
    assertEquals("0", df.format(0))
    assertEquals("-12,300", df.format(-123))
    assertEquals("100,000", df.format(1000))
    assertEquals("10,000,000", df.format(100000))
    assertEquals("1,000,000,000", df.format(10000000))
    //assertEquals("214,748,364,700", df.format(Int.MaxValue))
    //assertEquals("922,337,203,685,477,580,700", df.format(Long.MaxValue))
    assertEquals("-100,000", df.format(-1000))
    assertEquals("-10,000,000", df.format(-100000))
    assertEquals("-1,000,000,000", df.format(-10000000))
    //assertEquals("-214,748,364,800", df.format(Int.MinValue))
    //assertEquals("-922,337,203,685,477,580,800", df.format(Long.MinValue))
  }

  @Test def test_format_group_size(): Unit = {
    val df = NumberFormat.getIntegerInstance.asInstanceOf[DecimalFormat]
    df.setGroupingSize(4)
    assertEquals("123", df.format(123))
    assertEquals("0", df.format(0))
    assertEquals("-123", df.format(-123))
    assertEquals("1000", df.format(1000))
    assertEquals("10,0000", df.format(100000))
    assertEquals("1000,0000", df.format(10000000))
    assertEquals("21,4748,3647", df.format(Int.MaxValue))
    assertEquals("922,3372,0368,5477,5807", df.format(Long.MaxValue))
    assertEquals("-1000", df.format(-1000))
    assertEquals("-10,0000", df.format(-100000))
    assertEquals("-1000,0000", df.format(-10000000))
    assertEquals("-21,4748,3648", df.format(Int.MinValue))
    assertEquals("-922,3372,0368,5477,5808", df.format(Long.MinValue))
  }
}
