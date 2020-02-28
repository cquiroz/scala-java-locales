package testsuite.javalib.text

import java.text.{ DecimalFormat, DecimalFormatSymbols, NumberFormat }
import java.util.Locale
import java.math.RoundingMode

import locales.DecimalFormatUtil
import testsuite.utils.Platform
import testsuite.utils.AssertThrows._

final case class TestCase(
  tag:    String,
  l:      Locale,
  cldr21: Boolean,
  nf:     String,
  inf:    String,
  pf:     String
)

class NumberFormatTest extends munit.FunSuite {
  Locale.setDefault(Locale.US) // For Currency support

  val stdLocales = List(
    TestCase("und", Locale.ROOT, cldr21              = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("en", Locale.ENGLISH, cldr21            = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("fr", Locale.FRENCH, cldr21             = true, "#,##0.###", "#,##0", "#,##0\u00A0%"),
    TestCase("de", Locale.GERMAN, cldr21             = true, "#,##0.###", "#,##0", "#,##0\u00A0%"),
    TestCase("it", Locale.ITALIAN, cldr21            = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("ko", Locale.KOREAN, cldr21             = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("zh", Locale.CHINESE, cldr21            = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("zh", Locale.SIMPLIFIED_CHINESE, cldr21 = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase(
      "zh-TW",
      Locale.TRADITIONAL_CHINESE,
      cldr21 = true,
      "#,##0.###",
      "#,##0",
      "#,##0%"
    ),
    TestCase("fr-FR", Locale.FRANCE, cldr21  = true, "#,##0.###", "#,##0", "#,##0\u00A0%"),
    TestCase("de-DE", Locale.GERMANY, cldr21 = true, "#,##0.###", "#,##0", "#,##0\u00A0%"),
    TestCase("it-IT", Locale.ITALY, cldr21   = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("ko-KO", Locale.KOREA, cldr21   = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("zh-CH", Locale.CHINA, cldr21   = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("zh-CH", Locale.PRC, cldr21     = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("zh-TW", Locale.TAIWAN, cldr21  = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("en-GB", Locale.UK, cldr21      = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("en-US", Locale.US, cldr21      = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("en-CA", Locale.CANADA, cldr21  = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase(
      "fr-CA",
      Locale.CANADA_FRENCH,
      cldr21 = true,
      "#,##0.###",
      "#,##0",
      "#,##0\u00A0%"
    )
  )

  val extraLocales = List(
    TestCase("af", Locale.ROOT, cldr21      = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("az", Locale.ROOT, cldr21      = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("az-Cyrl", Locale.ROOT, cldr21 = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("it-CH", Locale.ROOT, cldr21   = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("zh", Locale.ROOT, cldr21      = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("zh-Hant", Locale.ROOT, cldr21 = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("fa", Locale.ROOT, cldr21      = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("fi-FI", Locale.ROOT, cldr21   = true, "#,##0.###", "#,##0", "#,##0\u00A0%"),
    TestCase("ja", Locale.ROOT, cldr21      = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("lv", Locale.ROOT, cldr21      = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("my", Locale.ROOT, cldr21      = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("ru-RU", Locale.ROOT, cldr21   = true, "#,##0.###", "#,##0", "#,##0\u00A0%")
  )

  val extraLocalesDiff = List(
    TestCase(
      "ar",
      Locale.ROOT,
      cldr21 = true,
      "#,##0.###;#,##0.###-",
      "#,##0;#,##0-",
      "#,##0%"
    ),
    TestCase("ar", Locale.ROOT, cldr21 = false, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("bn", Locale.ROOT, cldr21 = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("ka", Locale.ROOT, cldr21 = false, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("ka", Locale.ROOT, cldr21 = true, "#,##0.###", "#,##0", "#,##0\u00A0%"),
    // TODO: there is something weird about #,##,##0.###, the JVM corrects it to
    // @ val f = new DecimalFormat("#,##,##0.###"); f.toPattern => "#,##0.###"
    // TestCase("bn", Locale.ROOT, cldr21 = false, "#,##,##0.###", "#,##,##0", "#,##,##0%"),
    TestCase("es-CL", Locale.ROOT, cldr21  = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("es-CL", Locale.ROOT, cldr21  = false, "#,##0.###", "#,##0", "#,##0 %"),
    TestCase("smn", Locale.ROOT, cldr21    = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("smn-FI", Locale.ROOT, cldr21 = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("smn", Locale.ROOT, cldr21    = false, "#,##0.###", "#,##0", "#,##0\u00A0%"),
    TestCase("smn-FI", Locale.ROOT, cldr21 = false, "#,##0.###", "#,##0", "#,##0\u00A0%")
  )

  test("constants") {
    assertEquals(0, NumberFormat.INTEGER_FIELD)
    assertEquals(1, NumberFormat.FRACTION_FIELD)
  }

  test("available_locales") {
    assert(NumberFormat.getAvailableLocales.contains(Locale.ENGLISH))
  }

  test("default_instance") {
    val nf = NumberFormat.getNumberInstance.asInstanceOf[DecimalFormat]
    assertEquals("#,##0.###", nf.toPattern)
    assertEquals(DecimalFormatSymbols.getInstance(), nf.getDecimalFormatSymbols)

    val inf = NumberFormat.getIntegerInstance.asInstanceOf[DecimalFormat]
    assertEquals("#,##0", inf.toPattern)
    assertEquals(DecimalFormatSymbols.getInstance(), inf.getDecimalFormatSymbols)

    val pf = NumberFormat.getPercentInstance.asInstanceOf[DecimalFormat]
    assertEquals("#,##0%", pf.toPattern)
    assertEquals(DecimalFormatSymbols.getInstance(), pf.getDecimalFormatSymbols)
  }

  test("default_locales") {
    stdLocales.foreach { t: TestCase =>
      val nf = NumberFormat.getNumberInstance(t.l).asInstanceOf[DecimalFormat]
      assertEquals(t.nf, nf.toPattern)
      assert(!nf.isParseIntegerOnly)
      assertEquals(
        DecimalFormatSymbols.getInstance(t.l),
        nf.getDecimalFormatSymbols
      )
      assertEquals(Integer.MAX_VALUE, nf.getMaximumIntegerDigits)
      assertEquals(1, nf.getMinimumIntegerDigits)
      assertEquals(3, nf.getMaximumFractionDigits)
      assertEquals(0, nf.getMinimumFractionDigits)
      assert(nf.isGroupingUsed)
      assertEquals(RoundingMode.HALF_EVEN, nf.getRoundingMode)
      assertEquals("", nf.getPositivePrefix)
      assertEquals("", nf.getPositiveSuffix)
      assertEquals(
        nf.getDecimalFormatSymbols.getMinusSign.toString,
        nf.getNegativePrefix
      )
      assertEquals("", nf.getNegativeSuffix)
      assertEquals(1, nf.getMultiplier)
      assertEquals(3, nf.getGroupingSize)
      assert(!nf.isDecimalSeparatorAlwaysShown)
      assert(!nf.isParseBigDecimal)

      val inf = NumberFormat.getIntegerInstance(t.l).asInstanceOf[DecimalFormat]
      assertEquals(t.inf, inf.toPattern)
      assert(inf.isParseIntegerOnly)
      assertEquals(
        DecimalFormatSymbols.getInstance(t.l),
        inf.getDecimalFormatSymbols
      )
      assertEquals(Integer.MAX_VALUE, inf.getMaximumIntegerDigits)
      assertEquals(1, inf.getMinimumIntegerDigits)
      assertEquals(0, inf.getMaximumFractionDigits)
      assertEquals(0, inf.getMinimumFractionDigits)
      assert(inf.isGroupingUsed)
      assertEquals(RoundingMode.HALF_EVEN, inf.getRoundingMode)
      assertEquals("", inf.getPositivePrefix)
      assertEquals("", inf.getPositiveSuffix)
      assertEquals(
        inf.getDecimalFormatSymbols.getMinusSign.toString,
        inf.getNegativePrefix
      )
      assertEquals("", inf.getNegativeSuffix)
      assertEquals(1, inf.getMultiplier)
      assertEquals(3, inf.getGroupingSize)
      assert(!inf.isDecimalSeparatorAlwaysShown)
      assert(!inf.isParseBigDecimal)

      val pf = NumberFormat.getPercentInstance(t.l).asInstanceOf[DecimalFormat]
      assertEquals(t.pf, pf.toPattern)
      assertEquals(
        DecimalFormatSymbols.getInstance(t.l),
        pf.getDecimalFormatSymbols
      )
      assertEquals(Integer.MAX_VALUE, pf.getMaximumIntegerDigits)
      assertEquals(1, pf.getMinimumIntegerDigits)
      assertEquals(0, pf.getMaximumFractionDigits)
      assertEquals(0, pf.getMinimumFractionDigits)
      assert(pf.isGroupingUsed)
      assertEquals(RoundingMode.HALF_EVEN, pf.getRoundingMode)
      assertEquals("", pf.getPositivePrefix)
      assertEquals(
        DecimalFormatUtil.suffixFor(pf, DecimalFormatUtil.PatternCharPercent),
        pf.getPositiveSuffix
      )
      assertEquals(
        pf.getDecimalFormatSymbols.getMinusSign.toString,
        pf.getNegativePrefix
      )
      assertEquals(
        DecimalFormatUtil.suffixFor(pf, DecimalFormatUtil.PatternCharPercent),
        pf.getNegativeSuffix
      )
      assertEquals(100, pf.getMultiplier)
      assertEquals(3, pf.getGroupingSize)
      assert(!pf.isDecimalSeparatorAlwaysShown)
      assert(!pf.isParseBigDecimal)
    }
  }

  test("extra_locales") {
    extraLocales.foreach { t =>
      val l  = Locale.forLanguageTag(t.tag)
      val nf = NumberFormat.getNumberInstance(l).asInstanceOf[DecimalFormat]
      assertEquals(t.nf, nf.toPattern)
      assert(!nf.isParseIntegerOnly)
      assertEquals(
        DecimalFormatSymbols.getInstance(l),
        nf.getDecimalFormatSymbols
      )
      assertEquals(Integer.MAX_VALUE, nf.getMaximumIntegerDigits)
      assertEquals(1, nf.getMinimumIntegerDigits)
      assertEquals(3, nf.getMaximumFractionDigits)
      assertEquals(0, nf.getMinimumFractionDigits)
      assert(nf.isGroupingUsed)
      assertEquals(RoundingMode.HALF_EVEN, nf.getRoundingMode)
      assertEquals("", nf.getPositivePrefix)
      assertEquals("", nf.getPositiveSuffix)
      assertEquals(
        nf.getDecimalFormatSymbols.getMinusSign.toString,
        nf.getNegativePrefix
      )
      assertEquals("", nf.getNegativeSuffix)
      assertEquals(1, nf.getMultiplier)
      assertEquals(3, nf.getGroupingSize)
      assert(!nf.isDecimalSeparatorAlwaysShown)
      assert(!nf.isParseBigDecimal)

      val inf = NumberFormat.getIntegerInstance(l).asInstanceOf[DecimalFormat]
      assertEquals(t.inf, inf.toPattern)
      assert(inf.isParseIntegerOnly)
      assertEquals(
        DecimalFormatSymbols.getInstance(l),
        inf.getDecimalFormatSymbols
      )
      assertEquals(Integer.MAX_VALUE, inf.getMaximumIntegerDigits)
      assertEquals(1, inf.getMinimumIntegerDigits)
      assertEquals(0, inf.getMaximumFractionDigits)
      assertEquals(0, inf.getMinimumFractionDigits)
      assert(inf.isGroupingUsed)
      assertEquals(RoundingMode.HALF_EVEN, inf.getRoundingMode)
      assertEquals("", inf.getPositivePrefix)
      assertEquals("", inf.getPositiveSuffix)
      assertEquals(
        inf.getDecimalFormatSymbols.getMinusSign.toString,
        inf.getNegativePrefix
      )
      assertEquals("", inf.getNegativeSuffix)
      assertEquals(1, inf.getMultiplier)
      assertEquals(3, inf.getGroupingSize)
      assert(!inf.isDecimalSeparatorAlwaysShown)
      assert(!inf.isParseBigDecimal)

      val pf = NumberFormat.getPercentInstance(l).asInstanceOf[DecimalFormat]
      assertEquals(t.pf, pf.toPattern)
      assertEquals(
        DecimalFormatSymbols.getInstance(l),
        pf.getDecimalFormatSymbols
      )
      assertEquals(Integer.MAX_VALUE, pf.getMaximumIntegerDigits)
      assertEquals(1, pf.getMinimumIntegerDigits)
      assertEquals(0, pf.getMaximumFractionDigits)
      assertEquals(0, pf.getMinimumFractionDigits)
      assert(pf.isGroupingUsed)
      assertEquals(RoundingMode.HALF_EVEN, pf.getRoundingMode)
      assertEquals("", pf.getPositivePrefix)
      assertEquals(
        DecimalFormatUtil.suffixFor(pf, DecimalFormatUtil.PatternCharPercent),
        pf.getPositiveSuffix
      )
      assertEquals(
        pf.getDecimalFormatSymbols.getMinusSign.toString,
        pf.getNegativePrefix
      )
      assertEquals(
        DecimalFormatUtil.suffixFor(pf, DecimalFormatUtil.PatternCharPercent),
        pf.getNegativeSuffix
      )
      assertEquals(100, pf.getMultiplier)
      assertEquals(3, pf.getGroupingSize)
      assert(!pf.isDecimalSeparatorAlwaysShown)
      assert(!pf.isParseBigDecimal)
    }
  }

  test("extra_locales_diff") {
    extraLocalesDiff
      .filter(t => (Platform.executingInJVM && t.cldr21) || (!Platform.executingInJVM && !t.cldr21))
      .foreach { t =>
        val l  = Locale.forLanguageTag(t.tag)
        val nf = NumberFormat.getNumberInstance(l).asInstanceOf[DecimalFormat]
        assertEquals(t.nf, nf.toPattern)
        assert(!nf.isParseIntegerOnly)
        assertEquals(
          DecimalFormatSymbols.getInstance(l),
          nf.getDecimalFormatSymbols
        )
        assertEquals(Integer.MAX_VALUE, nf.getMaximumIntegerDigits)
        assertEquals(1, nf.getMinimumIntegerDigits)
        assertEquals(3, nf.getMaximumFractionDigits)
        assertEquals(0, nf.getMinimumFractionDigits)
        assert(nf.isGroupingUsed)
        assertEquals(RoundingMode.HALF_EVEN, nf.getRoundingMode)
        assertEquals("", nf.getPositivePrefix)
        assertEquals("", nf.getPositiveSuffix)
        assert(
          nf.getDecimalFormatSymbols.getMinusSign.toString == nf.getNegativePrefix ||
            nf.getDecimalFormatSymbols.getMinusSign.toString == nf.getNegativeSuffix
        )
        assertEquals(1, nf.getMultiplier)
        assertEquals(3, nf.getGroupingSize)
        assert(!nf.isDecimalSeparatorAlwaysShown)
        assert(!nf.isParseBigDecimal)

        val inf = NumberFormat.getIntegerInstance(l).asInstanceOf[DecimalFormat]
        assertEquals(t.inf, inf.toPattern)
        assert(inf.isParseIntegerOnly)
        assertEquals(DecimalFormatSymbols.getInstance(l), inf.getDecimalFormatSymbols)
        assertEquals(Integer.MAX_VALUE, inf.getMaximumIntegerDigits)
        assertEquals(1, inf.getMinimumIntegerDigits)
        assertEquals(0, inf.getMaximumFractionDigits)
        assertEquals(0, inf.getMinimumFractionDigits)
        assert(inf.isGroupingUsed)
        assertEquals(RoundingMode.HALF_EVEN, inf.getRoundingMode)
        assertEquals("", inf.getPositivePrefix)
        assertEquals("", inf.getPositiveSuffix)
        assert(
          inf.getDecimalFormatSymbols.getMinusSign.toString == inf.getNegativePrefix ||
            inf.getDecimalFormatSymbols.getMinusSign.toString == inf.getNegativeSuffix
        )
        assertEquals(1, inf.getMultiplier)
        assertEquals(3, inf.getGroupingSize)
        assert(!inf.isDecimalSeparatorAlwaysShown)
        assert(!inf.isParseBigDecimal)

        val pf = NumberFormat.getPercentInstance(l).asInstanceOf[DecimalFormat]
        assertEquals(t.pf, pf.toPattern)
        assertEquals(DecimalFormatSymbols.getInstance(l), pf.getDecimalFormatSymbols)
        assertEquals(Integer.MAX_VALUE, pf.getMaximumIntegerDigits)
        assertEquals(1, pf.getMinimumIntegerDigits)
        assertEquals(0, pf.getMaximumFractionDigits)
        assertEquals(0, pf.getMinimumFractionDigits)
        assert(pf.isGroupingUsed)
        assertEquals(RoundingMode.HALF_EVEN, pf.getRoundingMode)
        assertEquals("", pf.getPositivePrefix)
        assertEquals(
          DecimalFormatUtil.suffixFor(pf, DecimalFormatUtil.PatternCharPercent),
          pf.getPositiveSuffix
        )
        assert(
          pf.getDecimalFormatSymbols.getMinusSign.toString == pf.getNegativePrefix ||
            pf.getDecimalFormatSymbols.getMinusSign.toString == pf.getNegativeSuffix
        )
        assertEquals(100, pf.getMultiplier)
        assertEquals(3, pf.getGroupingSize)
        assert(!pf.isDecimalSeparatorAlwaysShown)
        assert(!pf.isParseBigDecimal)
      }
  }

  test("format_not_allowed") {
    val nf = NumberFormat.getNumberInstance
    expectThrows(classOf[IllegalArgumentException], nf.format("Abc"))
  }

  test("format_integer") {
    val nf = NumberFormat.getIntegerInstance
    assertEquals("123", nf.format(123))
    assertEquals("0", nf.format(0))
    assertEquals("-123", nf.format(-123))
    assertEquals("1,000", nf.format(1000))
    assertEquals("100,000", nf.format(100000))
    assertEquals("10,000,000", nf.format(10000000))
    assertEquals("2,147,483,647", nf.format(Int.MaxValue))
    assertEquals("9,223,372,036,854,775,807", nf.format(Long.MaxValue))
    assertEquals("-1,000", nf.format(-1000))
    assertEquals("-100,000", nf.format(-100000))
    assertEquals("-10,000,000", nf.format(-10000000))
    assertEquals("-2,147,483,648", nf.format(Int.MinValue))
    assertEquals("-9,223,372,036,854,775,808", nf.format(Long.MinValue))
  }

  test("format_grouping") {
    val nf = NumberFormat.getIntegerInstance
    nf.setGroupingUsed(false)
    assertEquals("123", nf.format(123))
    assertEquals("0", nf.format(0))
    assertEquals("-123", nf.format(-123))
    assertEquals("1000", nf.format(1000))
    assertEquals("100000", nf.format(100000))
    assertEquals("10000000", nf.format(10000000))
    assertEquals("2147483647", nf.format(Int.MaxValue))
    assertEquals("9223372036854775807", nf.format(Long.MaxValue))
    assertEquals("-1000", nf.format(-1000))
    assertEquals("-100000", nf.format(-100000))
    assertEquals("-10000000", nf.format(-10000000))
    assertEquals("-2147483648", nf.format(Int.MinValue))
    assertEquals("-9223372036854775808", nf.format(Long.MinValue))
  }

  test("format_max_digits_count") {
    val nf = NumberFormat.getIntegerInstance
    nf.setMaximumIntegerDigits(0)
    assertEquals("0", nf.format(123))
    assertEquals("0", nf.format(0))
    assertEquals("-0", nf.format(-123))
    assertEquals("0", nf.format(1000))
    assertEquals("0", nf.format(100000))
    assertEquals("0", nf.format(10000000))
    assertEquals("0", nf.format(Int.MaxValue))
    assertEquals("0", nf.format(Long.MaxValue))
    assertEquals("-0", nf.format(-1000))
    assertEquals("-0", nf.format(-100000))
    assertEquals("-0", nf.format(-10000000))
    assertEquals("-0", nf.format(Int.MinValue))
    assertEquals("-0", nf.format(Long.MinValue))

    nf.setMaximumIntegerDigits(5)
    assertEquals("123", nf.format(123))
    assertEquals("0", nf.format(0))
    assertEquals("-123", nf.format(-123))
    assertEquals("1,000", nf.format(1000))
    assertEquals("00,000", nf.format(100000))
    assertEquals("00,000", nf.format(10000000))
    assertEquals("83,647", nf.format(Int.MaxValue))
    assertEquals("75,807", nf.format(Long.MaxValue))
    assertEquals("-1,000", nf.format(-1000))
    assertEquals("-00,000", nf.format(-100000))
    assertEquals("-00,000", nf.format(-10000000))
    assertEquals("-83,648", nf.format(Int.MinValue))
    assertEquals("-75,808", nf.format(Long.MinValue))
  }

  test("format_min_digits_count") {
    val nf = NumberFormat.getIntegerInstance
    nf.setMinimumIntegerDigits(0)
    assertEquals("123", nf.format(123))
    assertEquals("0", nf.format(0))
    assertEquals("-123", nf.format(-123))
    assertEquals("1,000", nf.format(1000))
    assertEquals("100,000", nf.format(100000))
    assertEquals("10,000,000", nf.format(10000000))
    assertEquals("2,147,483,647", nf.format(Int.MaxValue))
    assertEquals(
      "9,223,372,036,854,775,807",
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

    nf.setMinimumIntegerDigits(5)
    assertEquals("00,123", nf.format(123))
    assertEquals("00,000", nf.format(0))
    assertEquals("-00,123", nf.format(-123))
    assertEquals("01,000", nf.format(1000))
    assertEquals("100,000", nf.format(100000))
    assertEquals("10,000,000", nf.format(10000000))
    assertEquals("2,147,483,647", nf.format(Int.MaxValue))
    assertEquals(
      "9,223,372,036,854,775,807",
      nf.format(Long.MaxValue)
    )
    assertEquals("-01,000", nf.format(-1000))
    assertEquals("-100,000", nf.format(-100000))
    assertEquals("-10,000,000", nf.format(-10000000))
    assertEquals("-2,147,483,648", nf.format(Int.MinValue))
    assertEquals(
      "-9,223,372,036,854,775,808",
      nf.format(Long.MinValue)
    )
  }

  test("format_locales_different_group") {
    val nf = NumberFormat.getIntegerInstance(Locale.CANADA_FRENCH)
    // Different group separator
    assertEquals("123", nf.format(123))
    assertEquals("0", nf.format(0))
    assertEquals("-123", nf.format(-123))
    assertEquals("1\u00A0000", nf.format(1000))
    assertEquals("100\u00A0000", nf.format(100000))
    assertEquals("10\u00A0000\u00A0000", nf.format(10000000))
    assertEquals("2\u00A0147\u00A0483\u00A0647", nf.format(Int.MaxValue))
    assertEquals(
      "9\u00A0223\u00A0372\u00A0036\u00A0854\u00A0775\u00A0807",
      nf.format(Long.MaxValue)
    )
    assertEquals("-1\u00A0000", nf.format(-1000))
    assertEquals("-100\u00A0000", nf.format(-100000))
    assertEquals("-10\u00A0000\u00A0000", nf.format(-10000000))
    assertEquals("-2\u00A0147\u00A0483\u00A0648", nf.format(Int.MinValue))
    assertEquals(
      "-9\u00A0223\u00A0372\u00A0036\u00A0854\u00A0775\u00A0808",
      nf.format(Long.MinValue)
    )
  }

  test("format_locales_different_negative") {
    val nf =
      NumberFormat.getIntegerInstance(Locale.forLanguageTag("lt")).asInstanceOf[DecimalFormat]
    nf.getDecimalFormatSymbols.setGroupingSeparator(',')
    // Different negative sign
    assertEquals("123", nf.format(123))
    assertEquals("0", nf.format(0))
    assertEquals("\u2212123", nf.format(-123))
    assertEquals("1,000", nf.format(1000))
    assertEquals("100,000", nf.format(100000))
    assertEquals("10,000,000", nf.format(10000000))
    assertEquals("2,147,483,647", nf.format(Int.MaxValue))
    assertEquals("9,223,372,036,854,775,807", nf.format(Long.MaxValue))
    assertEquals("\u22121,000", nf.format(-1000))
    assertEquals("\u2212100,000", nf.format(-100000))
    assertEquals("\u221210,000,000", nf.format(-10000000))
    assertEquals("\u22122,147,483,648", nf.format(Int.MinValue))
    assertEquals("\u22129,223,372,036,854,775,808", nf.format(Long.MinValue))
  }
}
