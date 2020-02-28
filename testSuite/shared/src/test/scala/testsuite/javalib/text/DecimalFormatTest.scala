package testsuite.javalib.text

import java.math.RoundingMode
import java.text.{ DecimalFormat, DecimalFormatSymbols, NumberFormat }
import java.util.Locale

// Need Locale for Currency support
class DecimalFormatTest extends munit.FunSuite {
  Locale.setDefault(Locale.US)

  test("constructor") {
    val f = new DecimalFormat("##0.#####E0")
    assertEquals("##0.#####E0", f.toPattern)
    assertEquals(
      DecimalFormatSymbols.getInstance(),
      f.getDecimalFormatSymbols
    )
    assert(!f.isParseIntegerOnly)
    assertEquals(3, f.getMaximumIntegerDigits)
    assertEquals(1, f.getMinimumIntegerDigits)
    assertEquals(5, f.getMaximumFractionDigits)
    assertEquals(0, f.getMinimumFractionDigits)
    assertEquals(RoundingMode.HALF_EVEN, f.getRoundingMode)
    assert(!f.isGroupingUsed)

    assertEquals("", f.getPositivePrefix)
    assertEquals("", f.getPositiveSuffix)
    assertEquals("-", f.getNegativePrefix)
    assertEquals("", f.getNegativeSuffix)
    assertEquals(1, f.getMultiplier)
    assertEquals(0, f.getGroupingSize)
    assert(!f.isDecimalSeparatorAlwaysShown)
    assert(!f.isParseBigDecimal)
  }

  test("setters") {
    val f = new DecimalFormat("##0.#####E0")
    f.setParseIntegerOnly(true)
    assert(f.isParseIntegerOnly)

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
    assert(f.isGroupingUsed)

    f.setPositivePrefix("A")
    assertEquals("A", f.getPositivePrefix)

    f.setNegativePrefix("B")
    assertEquals("B", f.getNegativePrefix)

    f.setPositiveSuffix("C")
    assertEquals("C", f.getPositiveSuffix)

    f.setNegativeSuffix("D")
    assertEquals("D", f.getNegativeSuffix)

    // TODO: this should probably be fixed in the impl to do the nulls like the JVM

    // Check for null arguments
    //f.setPositivePrefix(null)
    //assertNull(f.getPositivePrefix)
    //f.setPositiveSuffix(null)
    //assertNull(f.getPositiveSuffix)
    //f.setNegativePrefix(null)
    //assertNull(f.getNegativePrefix)
    //f.setNegativeSuffix(null)
    //assertNull(f.getNegativeSuffix)

    f.setMultiplier(2)
    assertEquals(2, f.getMultiplier)

    f.setMultiplier(-1)
    assertEquals(-1, f.getMultiplier)

    f.setGroupingSize(5)
    assertEquals(5, f.getGroupingSize)
    f.setGroupingSize(-1)
    assertEquals(-1, f.getGroupingSize)

    f.setDecimalSeparatorAlwaysShown(false)
    assert(!f.isDecimalSeparatorAlwaysShown)

    f.setParseBigDecimal(true)
    assert(f.isParseBigDecimal)
  }

  /*
    // JavaDoc Example: If there is an explicit negative subpattern, it serves only to specify the negative prefix and
    // suffix; the number of digits, minimal digits, and other characteristics are all the same as the positive pattern.
    // That means that "#,##0.0#;(#)" produces precisely the same behavior as "#,##0.0#;(#,##0.0#)".

    // Uhh...there's a strange bug in the JVM:
    // val f = new java.text.DecimalFormat("#,##0.0#;(#)") => f.format(-1) => "(1.0" (missing last ')')
    test("javadoc_negative_prefix") {
      val f1 = new DecimalFormat("#,##0.0#;(#)")
      val f2 = new DecimalFormat("#,##0.0#;(#,##0.0#)")

      //assertEquals("##0.#####E0", f.toPattern)
      assertEquals(f1.getDecimalFormatSymbols, f2.getDecimalFormatSymbols)
      assertEquals(f1.isParseIntegerOnly, f2.isParseIntegerOnly)
      assertEquals(f1.getMaximumIntegerDigits, f2.getMaximumIntegerDigits)
      assertEquals(f1.getMinimumIntegerDigits, f2.getMinimumIntegerDigits)
      assertEquals(f1.getMaximumFractionDigits, f2.getMaximumFractionDigits)
      assertEquals(f1.getMinimumFractionDigits, f2.getMinimumFractionDigits)
      assertEquals(f1.getRoundingMode, f2.getRoundingMode)
      assertEquals(f1.isGroupingUsed, f2.isGroupingUsed)

      assertEquals(f1.getPositivePrefix, f2.getPositivePrefix)
      assertEquals(f1.getPositiveSuffix, f2.getPositiveSuffix)
      assertEquals(f1.getNegativePrefix, f2.getNegativePrefix)
      assertEquals(f1.getNegativeSuffix, f2.getNegativeSuffix)
      assertEquals(f1.getMultiplier, f2.getMultiplier)
      assertEquals(f1.getGroupingSize, f2.getGroupingSize)
      assertEquals(f1.isDecimalSeparatorAlwaysShown, f2.isDecimalSeparatorAlwaysShown)
      assertEquals(f1.isParseBigDecimal, f2.isParseBigDecimal)
    }*/
  test("max_min_interactions") {
    val f = new DecimalFormat("##0.#####E0")

    f.setMinimumIntegerDigits(3)
    // less than minimum, update minimum
    f.setMaximumIntegerDigits(1)
    assertEquals(1, f.getMinimumIntegerDigits)

    // more than maximum, update maximum
    f.setMaximumIntegerDigits(3)
    assertEquals(3, f.getMaximumIntegerDigits)

    f.setMinimumFractionDigits(3)
    // less than minimum, update minimum
    f.setMaximumFractionDigits(1)
    assertEquals(1, f.getMinimumFractionDigits)

    // more than maximum, update maximum
    f.setMaximumFractionDigits(3)
    assertEquals(3, f.getMaximumFractionDigits)
  }

  test("grouping_size_parsing") {
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

  test("prefixes_parsing") {
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

  test("format_positive_prefix") {
    val nf = NumberFormat.getIntegerInstance.asInstanceOf[DecimalFormat]
    nf.setPositivePrefix("ABC")
    assertEquals("ABC123", nf.format(123))
    assertEquals("ABC0", nf.format(0))
    assertEquals("-123", nf.format(-123))
    assertEquals("ABC1,000", nf.format(1000))
    assertEquals("ABC100,000", nf.format(100000))
    assertEquals("ABC10,000,000", nf.format(10000000))
    assertEquals("ABC2,147,483,647", nf.format(Int.MaxValue))
    assertEquals(
      "ABC9,223,372,036,854,775,807",
      nf.format(Long.MaxValue)
    )
    assertEquals("-1,000", nf.format(-1000))
    assertEquals("-100,000", nf.format(-100000))
    assertEquals("-10,000,000", nf.format(-10000000))
    assertEquals("-2,147,483,648", nf.format(Int.MinValue))
    assertEquals(
      "-9,223,372,036,854,775,808",
      nf.format(Long.MinValue)
    )
  }

  test("format_positive_suffix") {
    val nf = NumberFormat.getIntegerInstance.asInstanceOf[DecimalFormat]
    nf.setPositiveSuffix("ABC")
    assertEquals("123ABC", nf.format(123))
    assertEquals("0ABC", nf.format(0))
    assertEquals("-123", nf.format(-123))
    assertEquals("1,000ABC", nf.format(1000))
    assertEquals("100,000ABC", nf.format(100000))
    assertEquals("10,000,000ABC", nf.format(10000000))
    assertEquals("2,147,483,647ABC", nf.format(Int.MaxValue))
    assertEquals(
      "9,223,372,036,854,775,807ABC",
      nf.format(Long.MaxValue)
    )
    assertEquals("-1,000", nf.format(-1000))
    assertEquals("-100,000", nf.format(-100000))
    assertEquals("-10,000,000", nf.format(-10000000))
    assertEquals("-2,147,483,648", nf.format(Int.MinValue))
    assertEquals(
      "-9,223,372,036,854,775,808",
      nf.format(Long.MinValue)
    )
  }

  test("format_negative_prefix") {
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
    assertEquals(
      "ABC9,223,372,036,854,775,808",
      nf.format(Long.MinValue)
    )
  }

  test("format_negative_suffix") {
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
    assertEquals(
      "-9,223,372,036,854,775,808ABC",
      "-9,223,372,036,854,775,808ABC",
      nf.format(Long.MinValue)
    )
  }

  test("format_multiplier") {
    val df = NumberFormat.getIntegerInstance.asInstanceOf[DecimalFormat]
    df.setMultiplier(2)
    assertEquals("246", df.format(123))
    assertEquals("0", df.format(0))
    assertEquals("-246", df.format(-123))
    assertEquals("2,000", df.format(1000))
    assertEquals("200,000", df.format(100000))
    assertEquals("20,000,000", df.format(10000000))
    assertEquals("4,294,967,294", df.format(Int.MaxValue))
    assertEquals("18,446,744,073,709,551,614", df.format(Long.MaxValue))
    assertEquals("-2,000", df.format(-1000))
    assertEquals("-200,000", df.format(-100000))
    assertEquals("-20,000,000", df.format(-10000000))
    assertEquals("-4,294,967,296", df.format(Int.MinValue))
    assertEquals("-18,446,744,073,709,551,616", df.format(Long.MinValue))

    df.setMultiplier(100)
    assertEquals("12,300", df.format(123))
    assertEquals("0", df.format(0))
    assertEquals("-12,300", df.format(-123))
    assertEquals("100,000", df.format(1000))
    assertEquals("10,000,000", df.format(100000))
    assertEquals("1,000,000,000", df.format(10000000))
    assertEquals("214,748,364,700", df.format(Int.MaxValue))
    assertEquals("922,337,203,685,477,580,700", df.format(Long.MaxValue))
    assertEquals("-100,000", df.format(-1000))
    assertEquals("-10,000,000", df.format(-100000))
    assertEquals("-1,000,000,000", df.format(-10000000))
    assertEquals("-214,748,364,800", df.format(Int.MinValue))
    assertEquals("-922,337,203,685,477,580,800", df.format(Long.MinValue))
  }

  test("format_group_size") {
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
  test("format_decimals") {
    val df = NumberFormat.getNumberInstance.asInstanceOf[DecimalFormat]
    assertEquals("123.457", df.format(123.4567))
    assertEquals("0", df.format(0.0))
    assertEquals("-123.457", df.format(-123.4567))
    assertEquals("1,000.123", df.format(1000.1234))
    assertEquals("10,000.123", df.format(10000.1234))
    assertEquals("10,000,000.123", df.format(10000000.1234))
    assertEquals("-1,000.123", df.format(-1000.1234))
    assertEquals("-10,000.123", df.format(-10000.1234))
    assertEquals("-10,000,000.123", df.format(-10000000.1234))
  }
  test("format_decimals_min_fractions") {
    val df = NumberFormat.getNumberInstance.asInstanceOf[DecimalFormat]
    df.setMinimumFractionDigits(5)
    assertEquals("123.00000", df.format(123))
    assertEquals("123.45670", df.format(123.4567))
    assertEquals("123.00010", df.format(123.0001))
    assertEquals("0.00000", df.format(0.0))
    assertEquals("-123.00000", df.format(-123))
    assertEquals("-123.45670", df.format(-123.4567))
    assertEquals("-123.00010", df.format(-123.0001))
    assertEquals("1,000.12340", df.format(1000.1234))
    assertEquals("10,000.12340", df.format(10000.1234))
    assertEquals("10,000,000.12340", df.format(10000000.1234))
    assertEquals("2,147,483,647.00000", df.format(Int.MaxValue))
    assertEquals(
      "9,223,372,036,854,775,807.00000",
      "9,223,372,036,854,775,807.00000",
      df.format(Long.MaxValue)
    )
    assertEquals("-1,000.12340", df.format(-1000.1234))
    assertEquals("-10,000.12340", df.format(-10000.1234))
    assertEquals("-10,000,000.12340", df.format(-10000000.1234))
    assertEquals("-2,147,483,648.00000", df.format(Int.MinValue))
    assertEquals(
      "-9,223,372,036,854,775,808.00000",
      "-9,223,372,036,854,775,808.00000",
      df.format(Long.MinValue)
    )
  }

  test("format_decimals_max_fractions") {
    val df = NumberFormat.getNumberInstance.asInstanceOf[DecimalFormat]
    df.setMaximumFractionDigits(2)

    assertEquals("123.46", df.format(123.4567))
    assertEquals("123", df.format(123.0001))
    assertEquals("0", df.format(0.0))
    assertEquals("-123.46", df.format(-123.4567))
    assertEquals("-123", df.format(-123.0001))
    assertEquals("1,000.12", df.format(1000.1234))
    assertEquals("10,000.12", df.format(10000.1234))
    assertEquals("10,000,000.12", df.format(10000000.1234))
    assertEquals("-1,000.12", df.format(-1000.1234))
    assertEquals("-10,000.12", df.format(-10000.1234))
    assertEquals("-10,000,000.12", df.format(-10000000.1234))
  }

  test("format_exponents") {
    val f = new DecimalFormat("##0.#####E0")

    assertEquals("0E0", f.format(0.0))

    assertEquals("123.0001E0", f.format(123.0001))
    assertEquals("123.4567E0", f.format(123.4567))
    assertEquals("1.0001234E3", f.format(1000.1234))
    assertEquals("10.000123E3", f.format(10000.1234))
    assertEquals("10E6", f.format(10000000.1234))
    assertEquals("2.1474836E9", f.format(Int.MaxValue))
    assertEquals("9.223372E18", f.format(Long.MaxValue))

    assertEquals("-123.0001E0", f.format(-123.0001))
    assertEquals("-123.4567E0", f.format(-123.4567))
    assertEquals("-1.0001234E3", f.format(-1000.1234))
    assertEquals("-10.000123E3", f.format(-10000.1234))
    assertEquals("-10E6", f.format(-10000000.1234))
    assertEquals("-2.1474836E9", f.format(Int.MinValue))
    assertEquals("-9.223372E18", f.format(Long.MinValue))
  }

  test("format_exponents_one_integer_max_three_fractions") {
    val f = new DecimalFormat("0.###E0")

    assertEquals("0E0", f.format(0.0))

    assertEquals("1.235E2", f.format(123.4567))
    assertEquals("1.23E2", f.format(123.0001))
    assertEquals("1E3", f.format(1000.1234))
    assertEquals("1E4", f.format(10000.1234))
    assertEquals("1E7", f.format(10000000.1234))
    assertEquals("2.147E9", f.format(Int.MaxValue))
    assertEquals("9.223E18", f.format(Long.MaxValue))

    assertEquals("-1.235E2", f.format(-123.4567))
    assertEquals("-1.23E2", f.format(-123.0001))
    assertEquals("-1E3", f.format(-1000.1234))
    assertEquals("-1E4", f.format(-10000.1234))
    assertEquals("-1E7", f.format(-10000000.1234))
    assertEquals("-2.147E9", f.format(Int.MinValue))
    assertEquals("-9.223E18", f.format(Long.MinValue))
  }

  test("format_exponents_one_integer_min_one_fractions") {
    val f = new DecimalFormat("0.0##E0")

    assertEquals("1.0E0", f.format(1))
    assertEquals("1.0E1", f.format(10))
    assertEquals("1.0E2", f.format(100))
    assertEquals("1.0E3", f.format(1000.1234))
    assertEquals("1.0E3", f.format(1000.1234))
    assertEquals("1.0E4", f.format(10000.1234))
    assertEquals("1.0E7", f.format(10000000.1234))

    assertEquals("-1.0E0", f.format(-1))
    assertEquals("-1.0E1", f.format(-10))
    assertEquals("-1.0E2", f.format(-100))
    assertEquals("-1.0E3", f.format(-1000.1234))
    assertEquals("-1.0E3", f.format(-1000.1234))
    assertEquals("-1.0E4", f.format(-10000.1234))
    assertEquals("-1.0E7", f.format(-10000000.1234))
  }

  // Exponent should always be in multiples of two (2,-2,4,-4,6,-6...)
  test("format_exponents_multiples_two") {
    val f = new DecimalFormat("#0.##E0")

    assertEquals("0E0", f.format(0))

    assertEquals("1E0", f.format(1))
    assertEquals("12E0", f.format(12))
    assertEquals("1.23E2", f.format(123))
    assertEquals("12.34E2", f.format(1234))

    assertEquals("1.234E4", f.format(12345))
    assertEquals("12.35E4", f.format(123456))
    assertEquals("1.235E6", f.format(1234567))
    assertEquals("12.35E6", f.format(12345678))

    assertEquals("-1E0", f.format(-1))
    assertEquals("-12E0", f.format(-12))
    assertEquals("-1.23E2", f.format(-123))
    assertEquals("-12.34E2", f.format(-1234))
    assertEquals("-1.234E4", f.format(-12345))
    assertEquals("-12.35E4", f.format(-123456))
    assertEquals("-1.235E6", f.format(-1234567))
    assertEquals("-12.35E6", f.format(-12345678))

    assertEquals("10E-2", f.format(.1))
    assertEquals("12E-2", f.format(.12))
    assertEquals("12.3E-2", f.format(.123))
    assertEquals("12.34E-2", f.format(.1234))
    assertEquals("12.35E-2", f.format(.12345))

    assertEquals("12.34E-4", f.format(.001234))
    assertEquals("1.234E-4", f.format(.0001234))
    assertEquals("12.34E-6", f.format(.00001234))
    assertEquals("1.234E-6", f.format(.000001234))
    assertEquals("12.34E-8", f.format(.0000001234))

    assertEquals("-10E-2", f.format(-.1))
    assertEquals("-12E-2", f.format(-.12))
    assertEquals("-12.3E-2", f.format(-.123))
    assertEquals("-12.34E-2", f.format(-.1234))

    assertEquals("-12.34E-4", f.format(-.001234))
    assertEquals("-1.234E-4", f.format(-.0001234))
    assertEquals("-12.34E-6", f.format(-.00001234))
    assertEquals("-1.234E-6", f.format(-.000001234))
    assertEquals("-12.34E-8", f.format(-.0000001234))
  }

  // Exponent should always be in multiples of three (3,-3,6,-6,9,-9...)
  test("format_exponents_multiples_three") {
    val f = new DecimalFormat("##0.##E0")

    assertEquals("0E0", f.format(0))

    assertEquals("1E0", f.format(1))
    assertEquals("12E0", f.format(12))
    assertEquals("123E0", f.format(123))
    assertEquals("1.234E3", f.format(1234))
    assertEquals("12.345E3", f.format(12345))
    assertEquals("123.46E3", f.format(123456))
    assertEquals("1.2346E6", f.format(1234567))
    assertEquals("12.346E6", f.format(12345678))
    assertEquals("123.46E6", f.format(123456789))
    assertEquals("1.2346E9", f.format(1234567890))

    assertEquals("-1E0", f.format(-1))
    assertEquals("-12E0", f.format(-12))
    assertEquals("-123E0", f.format(-123))
    assertEquals("-1.234E3", f.format(-1234))
    assertEquals("-12.345E3", f.format(-12345))
    assertEquals("-123.46E3", f.format(-123456))
    assertEquals("-1.2346E6", f.format(-1234567))
    assertEquals("-12.346E6", f.format(-12345678))
    assertEquals("-123.46E6", f.format(-123456789))
    assertEquals("-1.2346E9", f.format(-1234567890))

    assertEquals("100E-3", f.format(.1))
    assertEquals("120E-3", f.format(.12))
    assertEquals("123E-3", f.format(.123))
    assertEquals("123.4E-3", f.format(.1234))
    assertEquals("123.45E-3", f.format(.12345))
    assertEquals("123.46E-3", f.format(.123456))
    assertEquals("123.46E-3", f.format(.1234567))

    assertEquals("-100E-3", f.format(-.1))
    assertEquals("-120E-3", f.format(-.12))
    assertEquals("-123E-3", f.format(-.123))
    assertEquals("-123.4E-3", f.format(-.1234))
    assertEquals("-123.45E-3", f.format(-.12345))
    assertEquals("-123.46E-3", f.format(-.123456))
    assertEquals("-123.46E-3", f.format(-.1234567))

    assertEquals("1.234E-3", f.format(.001234))
    assertEquals("123.4E-6", f.format(.0001234))
    assertEquals("12.34E-6", f.format(.00001234))
    assertEquals("1.234E-6", f.format(.000001234))
    assertEquals("123.4E-9", f.format(.0000001234))

    assertEquals("-1.234E-3", f.format(-.001234))
    assertEquals("-123.4E-6", f.format(-.0001234))
    assertEquals("-12.34E-6", f.format(-.00001234))
    assertEquals("-1.234E-6", f.format(-.000001234))
    assertEquals("-123.4E-9", f.format(-.0000001234))
  }

  test("format_currency") {
    val f = NumberFormat.getCurrencyInstance(Locale.US)

    assertEquals("$0.00", f.format(0))

    assertEquals("$0.12", f.format(.12))
    assertEquals("$123.00", f.format(123))
    assertEquals("$123.45", f.format(123.45))
    assertEquals("$1,234.56", f.format(1234.56))
    assertEquals("$12,345.67", f.format(12345.67))
    assertEquals("$123,456.78", f.format(123456.78))
    assertEquals("$1,234,567.89", f.format(1234567.89))
    assertEquals("$12,345,678.90", f.format(12345678.90))

    assertEquals("($0.12)", f.format(-.12))
    assertEquals("($123.00)", f.format(-123))
    assertEquals("($123.45)", f.format(-123.45))
    assertEquals("($1,234.56)", f.format(-1234.56))
    assertEquals("($12,345.67)", f.format(-12345.67))
    assertEquals("($123,456.78)", f.format(-123456.78))
    assertEquals("($1,234,567.89)", f.format(-1234567.89))
    assertEquals("($12,345,678.90)", f.format(-12345678.90))
  }

  test("to_pattern_positive_patterns") {
    val patternTests: Seq[(String, String)] = Seq(
      ("#,##0.##", "#,##0.##"),
      ("#,##0.0#", "#,##0.0#"),
      ("#0.0#", "#0.0#"),
      ("#.#", "#0.#"),
      ("#.0", "#.0"),
      ("0.0", "#0.0"),
      ("##0.##E0", "##0.##E0"),
      ("##0.##E00", "##0.##E00")
    )

    patternTests.foreach {
      case (in: String, out: String) =>
        val f = new DecimalFormat(in)
        val p = new DecimalFormat("foo" + in)
        val s = new DecimalFormat(in + "bar")
        val b = new DecimalFormat("foo" + in + "bar")

        assertEquals(out, f.toPattern)
        assertEquals("foo" + out, p.toPattern)
        assertEquals(out + "bar", s.toPattern)
        assertEquals("foo" + out + "bar", b.toPattern)
    }
  }

  test("to_pattern_negative_patterns") {
    val patternTests: Seq[(String, String)] = Seq(
      ("#,##0.##;(#,##0.##)", "#,##0.##;(#,##0.##)"),
      ("#,##0.0#;(#,##0.0#)", "#,##0.0#;(#,##0.0#)"),
      ("###0.0#;(###0.0#)", "#0.0#;(#0.0#)"),
      ("#.#;(#.#)", "#0.#;(#0.#)"),
      ("#.0;(#.0)", "#.0;(#.0)"),
      ("0.0;(0.0)", "#0.0;(#0.0)"),
      ("##0.##E0;(##0.##E0)", "##0.##E0;(##0.##E0)"),
      ("##0.##E00;(##0.##E00)", "##0.##E00;(##0.##E00)"),
      // Add positive prefix/sufixes
      ("foo#,##0.##bar;(#,##0.##)", "foo#,##0.##bar;(#,##0.##)"),
      ("foo#,##0.0#bar;(#,##0.0#)", "foo#,##0.0#bar;(#,##0.0#)"),
      ("foo###0.0#bar;(###0.0#)", "foo#0.0#bar;(#0.0#)"),
      ("foo#.#bar;(#.#)", "foo#0.#bar;(#0.#)"),
      ("foo#.0bar;(#.0)", "foo#.0bar;(#.0)"),
      ("foo0.0bar;(0.0)", "foo#0.0bar;(#0.0)"),
      ("foo##0.##E0bar;(##0.##E0)", "foo##0.##E0bar;(##0.##E0)"),
      ("foo##0.##E00bar;(##0.##E00)", "foo##0.##E00bar;(##0.##E00)")
    )
    patternTests.foreach {
      case (in: String, out: String) =>
        val f = new DecimalFormat(in)

        assertEquals(out, f.toPattern)
    }
  }
}
