package testsuite.javalib.text

import java.text.{DecimalFormat, DecimalFormatSymbols, NumberFormat}
import java.util.Locale
import java.math.RoundingMode

import locales.{DecimalFormatUtil, LocaleRegistry}
import locales.cldr.LDML
import locales.cldr.data._
import org.junit.{Ignore, Test}
import org.junit.Assert._
import testsuite.utils.{LocaleTestSetup, Platform}
import testsuite.utils.AssertThrows._

class NumberFormatTest extends LocaleTestSetup {
  case class TestCase(tag: String, ldml: LDML, l: Locale, cldr21: Boolean, nf: String, inf: String, pf: String)

  val stdLocales = List(
    //TestCase("und", root, Locale.ROOT, cldr21 = true, "#,##0.###", "#,##0", "#,##0%"),
    //TestCase("en", root, Locale.ENGLISH, cldr21 = true, "#,##0.###", "#,##0", "#,##0%"),
    //TestCase("fr", root, Locale.FRENCH, cldr21 = true, "#,##0.###", "#,##0", "#,##0\u00A0%"),
    //TestCase("de", root, Locale.GERMAN, cldr21 = true, "#,##0.###", "#,##0", "#,##0\u00A0%"),
    //TestCase("it", root, Locale.ITALIAN, cldr21 = true, "#,##0.###", "#,##0", "#,##0%"),
    //TestCase("ko", root, Locale.KOREAN, cldr21 = true, "#,##0.###", "#,##0", "#,##0%"),
    //TestCase("zh", root, Locale.CHINESE, cldr21 = true, "#,##0.###", "#,##0", "#,##0%"),
    //TestCase("zh", root, Locale.SIMPLIFIED_CHINESE, cldr21 = true, "#,##0.###", "#,##0", "#,##0%"),
    //TestCase("zh-TW", root, Locale.TRADITIONAL_CHINESE, cldr21 = true, "#,##0.###", "#,##0", "#,##0%"),
    //TestCase("fr-FR", root, Locale.FRANCE, cldr21 = true, "#,##0.###", "#,##0", "#,##0\u00A0%"),
    //TestCase("de-DE", root, Locale.GERMANY, cldr21 = true, "#,##0.###", "#,##0", "#,##0\u00A0%"),
    //TestCase("it-IT", root, Locale.ITALY, cldr21 = true, "#,##0.###", "#,##0", "#,##0%"),
    //TestCase("ko-KO", root, Locale.KOREA, cldr21 = true, "#,##0.###", "#,##0", "#,##0%"),
    //TestCase("zh-CH", root, Locale.CHINA, cldr21 = true, "#,##0.###", "#,##0", "#,##0%"),
    //TestCase("zh-CH", root, Locale.PRC, cldr21 = true, "#,##0.###", "#,##0", "#,##0%"),
    //TestCase("zh-TW", root, Locale.TAIWAN, cldr21 = true, "#,##0.###", "#,##0", "#,##0%"),
    //TestCase("en-GB", root, Locale.UK, cldr21 = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("en-US", root, Locale.US, cldr21 = true, "#,##0.###", "#,##0", "#,##0%")
    //TestCase("en-CA", root, Locale.CANADA, cldr21 = true, "#,##0.###", "#,##0", "#,##0%"),
    //TestCase("fr-CA", root, Locale.CANADA_FRENCH, cldr21 = true, "#,##0.###", "#,##0", "#,##0\u00A0%")
  )

  val extraLocales = List(
    TestCase("af", af, Locale.ROOT, cldr21 = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("az", az, Locale.ROOT, cldr21 = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("az-Cyrl", az_Cyrl, Locale.ROOT, cldr21 = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("it-CH", it_CH, Locale.ROOT, cldr21 = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("zh", zh, Locale.ROOT, cldr21 = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("zh-Hant", zh_Hant, Locale.ROOT, cldr21 = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("fa", fa, Locale.ROOT, cldr21 = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("fi-FI", fi_FI, Locale.ROOT, cldr21 = true, "#,##0.###", "#,##0", "#,##0\u00A0%"),
    TestCase("ja", ja, Locale.ROOT, cldr21 = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("ka", ka, Locale.ROOT, cldr21 = true, "#,##0.###", "#,##0", "#,##0\u00A0%"),
    TestCase("lv", lv, Locale.ROOT, cldr21 = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("my", my, Locale.ROOT, cldr21 = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("ru-RU", ru_RU, Locale.ROOT, cldr21 = true, "#,##0.###", "#,##0", "#,##0\u00A0%")
  )

  val extraLocalesDiff = List(
    TestCase("ar", ar, Locale.ROOT, cldr21 = true, "#,##0.###;#,##0.###-", "#,##0;#,##0-", "#,##0%"),
    TestCase("ar", ar, Locale.ROOT, cldr21 = false, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("bn", bn, Locale.ROOT, cldr21 = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("bn", bn, Locale.ROOT, cldr21 = false, "#,##,##0.###", "#,##,##0", "#,##,##0%"),
    TestCase("es-CL", es_CL, Locale.ROOT, cldr21 = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("es-CL", es_CL, Locale.ROOT, cldr21 = false, "#,##0.###", "#,##0", "#,##0 %"),
    TestCase("smn", smn, Locale.ROOT, cldr21 = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("smn-FI", smn_FI, Locale.ROOT, cldr21 = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("smn", smn, Locale.ROOT, cldr21 = false, "#,##0.###", "#,##0", "#,##0\u00A0%"),
    TestCase("smn-FI", smn_FI, Locale.ROOT, cldr21 = false, "#,##0.###", "#,##0", "#,##0\u00A0%")
  )

  @Test def test_constants(): Unit = {
    assertEquals(0, NumberFormat.INTEGER_FIELD)
    assertEquals(1, NumberFormat.FRACTION_FIELD)
  }

  @Test def test_available_locales(): Unit = {
    assertTrue(NumberFormat.getAvailableLocales.contains(Locale.ENGLISH))
  }

  @Test def test_default_instance(): Unit = {
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

  @Test def test_default_locales(): Unit = {
    stdLocales.foreach { t: TestCase =>
      val nf = NumberFormat.getNumberInstance(t.l).asInstanceOf[DecimalFormat]
      assertEquals("nf.toPattern", t.nf, nf.toPattern)
      assertFalse("nf.isParseIntegerOnly", nf.isParseIntegerOnly)
      assertEquals("nf.getDecimalFormatSymbols", DecimalFormatSymbols.getInstance(t.l), nf.getDecimalFormatSymbols)
      assertEquals("nf.getMaximumIntegerDigits", Integer.MAX_VALUE, nf.getMaximumIntegerDigits)
      assertEquals("nf.getMinimumIntegerDigits", 1, nf.getMinimumIntegerDigits)
      assertEquals("nf.getMaximumFractionDigits", 3, nf.getMaximumFractionDigits)
      assertEquals("nf.getMinimumFractionDigits", 0, nf.getMinimumFractionDigits)
      assertTrue("nf.isGroupingUsed", nf.isGroupingUsed)
      assertEquals("nf.getRoundingMode", RoundingMode.HALF_EVEN, nf.getRoundingMode)
      assertEquals("nf.getPositivePrefix", "", nf.getPositivePrefix)
      assertEquals("nf.getPositiveSuffix", "", nf.getPositiveSuffix)
      assertEquals("nf.getNegativePrefix", nf.getDecimalFormatSymbols.getMinusSign.toString, nf.getNegativePrefix)
      assertEquals("nf.getNegativeSuffix", "", nf.getNegativeSuffix)
      assertEquals("nf.getMultiplier", 1, nf.getMultiplier)
      assertEquals("nf.getGroupingSize", 3, nf.getGroupingSize)
      assertFalse("nf.isDecimalSeparatorAlwaysShown", nf.isDecimalSeparatorAlwaysShown)
      assertFalse("nf.isParseBigDecimal", nf.isParseBigDecimal)

      val inf = NumberFormat.getIntegerInstance(t.l).asInstanceOf[DecimalFormat]
      assertEquals("inf.toPattern", t.inf, inf.toPattern)
      assertTrue("inf.isParseIntegerOnly", inf.isParseIntegerOnly)
      assertEquals("inf.getDecimalFormatSymbols", DecimalFormatSymbols.getInstance(t.l), inf.getDecimalFormatSymbols)
      assertEquals("inf.getMaximumIntegerDigits", Integer.MAX_VALUE, inf.getMaximumIntegerDigits)
      assertEquals("inf.getMinimumIntegerDigits", 1, inf.getMinimumIntegerDigits)
      assertEquals("inf.getMaximumFractionDigits", 0, inf.getMaximumFractionDigits)
      assertEquals("inf.getMinimumFractionDigits", 0, inf.getMinimumFractionDigits)
      assertTrue("inf.isGroupingUsed", inf.isGroupingUsed)
      assertEquals("inf.getRoundingMode", RoundingMode.HALF_EVEN, inf.getRoundingMode)
      assertEquals("inf.getPositivePrefix", "", inf.getPositivePrefix)
      assertEquals("inf.getPositiveSuffix", "", inf.getPositiveSuffix)
      assertEquals("inf.getNegativePrefix", inf.getDecimalFormatSymbols.getMinusSign.toString, inf.getNegativePrefix)
      assertEquals("inf.getNegativeSuffix", "", inf.getNegativeSuffix)
      assertEquals("inf.getMultiplier", 1, inf.getMultiplier)
      assertEquals("inf.getGroupingSize", 3, inf.getGroupingSize)
      assertFalse("inf.isDecimalSeparatorAlwaysShown", inf.isDecimalSeparatorAlwaysShown)
      assertFalse("inf.isParseBigDecimal", inf.isParseBigDecimal)

      val pf = NumberFormat.getPercentInstance(t.l).asInstanceOf[DecimalFormat]
      assertEquals("pf.toPattern", t.pf, pf.toPattern)
      assertEquals("pf.getDecimalFormatSymbols", DecimalFormatSymbols.getInstance(t.l), pf.getDecimalFormatSymbols)
      assertEquals("pf.getMaximumIntegerDigits", Integer.MAX_VALUE, pf.getMaximumIntegerDigits)
      assertEquals("pf.getMinimumIntegerDigits", 1, pf.getMinimumIntegerDigits)
      assertEquals("pf.getMaximumFractionDigits", 0, pf.getMaximumFractionDigits)
      assertEquals("pf.getMinimumFractionDigits", 0, pf.getMinimumFractionDigits)
      assertTrue("pf.isGroupingUsed", pf.isGroupingUsed)
      assertEquals("pf.getRoundingMode", RoundingMode.HALF_EVEN, pf.getRoundingMode)
      assertEquals("pf.getPositivePrefix", "", pf.getPositivePrefix)
      assertEquals("pf.getPositiveSuffix", DecimalFormatUtil.suffixFor(pf, DecimalFormatUtil.PatternCharPercent), pf.getPositiveSuffix)
      assertEquals("pf.getNegativePrefix", pf.getDecimalFormatSymbols.getMinusSign.toString, pf.getNegativePrefix)
      assertEquals("pf.getNegativeSuffix", DecimalFormatUtil.suffixFor(pf, DecimalFormatUtil.PatternCharPercent), pf.getNegativeSuffix)
      assertEquals("pf.getMultiplier", 100, pf.getMultiplier)
      assertEquals("pf.getGroupingSize", 3, pf.getGroupingSize)
      assertFalse("pf.isDecimalSeparatorAlwaysShown", pf.isDecimalSeparatorAlwaysShown)
      assertFalse("pf.isParseBigDecimal", pf.isParseBigDecimal)
    }
  }

  @Test def test_extra_locales(): Unit = {
    extraLocales.foreach { t =>
      if (!Platform.executingInJVM) {
        LocaleRegistry.installLocale(t.ldml)
      }
      val l = Locale.forLanguageTag(t.tag)
      val nf = NumberFormat.getNumberInstance(l).asInstanceOf[DecimalFormat]
      assertEquals("nf.toPattern", t.nf, nf.toPattern)
      assertFalse("nf.isParseIntegerOnly", nf.isParseIntegerOnly)
      assertEquals("nf.getDecimalFormatSymbols", DecimalFormatSymbols.getInstance(l), nf.getDecimalFormatSymbols)
      assertEquals("nf.getMaximumIntegerDigits", Integer.MAX_VALUE, nf.getMaximumIntegerDigits)
      assertEquals("nf.getMinimumIntegerDigits", 1, nf.getMinimumIntegerDigits)
      assertEquals("nf.getMaximumFractionDigits", 3, nf.getMaximumFractionDigits)
      assertEquals("nf.getMinimumFractionDigits", 0, nf.getMinimumFractionDigits)
      assertTrue("nf.isGroupingUsed", nf.isGroupingUsed)
      assertEquals("nf.getRoundingMode", RoundingMode.HALF_EVEN, nf.getRoundingMode)
      assertEquals("nf.getPositivePrefix", "", nf.getPositivePrefix)
      assertEquals("nf.getPositiveSuffix", "", nf.getPositiveSuffix)
      assertEquals("nf.getNegativePrefix", nf.getDecimalFormatSymbols.getMinusSign.toString, nf.getNegativePrefix)
      assertEquals("nf.getNegativeSuffix", "", nf.getNegativeSuffix)
      assertEquals("nf.getMultiplier", 1, nf.getMultiplier)
      assertEquals("nf.getGroupingSize", 3, nf.getGroupingSize)
      assertFalse("nf.isDecimalSeparatorAlwaysShown", nf.isDecimalSeparatorAlwaysShown)
      assertFalse("nf.isParseBigDecimal", nf.isParseBigDecimal)

      val inf = NumberFormat.getIntegerInstance(l).asInstanceOf[DecimalFormat]
      assertEquals("inf.toPattern", t.inf, inf.toPattern)
      assertTrue("inf.isParseIntegerOnly", inf.isParseIntegerOnly)
      assertEquals("inf.getDecimalFormatSymbols", DecimalFormatSymbols.getInstance(l), inf.getDecimalFormatSymbols)
      assertEquals("inf.getMaximumIntegerDigits", Integer.MAX_VALUE, inf.getMaximumIntegerDigits)
      assertEquals("inf.getMinimumIntegerDigits", 1, inf.getMinimumIntegerDigits)
      assertEquals("inf.getMaximumFractionDigits", 0, inf.getMaximumFractionDigits)
      assertEquals("inf.getMinimumFractionDigits", 0, inf.getMinimumFractionDigits)
      assertTrue("inf.isGroupingUsed", inf.isGroupingUsed)
      assertEquals("inf.getRoundingMode", RoundingMode.HALF_EVEN, inf.getRoundingMode)
      assertEquals("inf.getPositivePrefix", "", inf.getPositivePrefix)
      assertEquals("inf.getPositiveSuffix", "", inf.getPositiveSuffix)
      assertEquals("inf.getNegativePrefix", inf.getDecimalFormatSymbols.getMinusSign.toString, inf.getNegativePrefix)
      assertEquals("inf.getNegativeSuffix", "", inf.getNegativeSuffix)
      assertEquals("inf.getMultiplier", 1, inf.getMultiplier)
      assertEquals("inf.getGroupingSize", 3, inf.getGroupingSize)
      assertFalse("inf.isDecimalSeparatorAlwaysShown", inf.isDecimalSeparatorAlwaysShown)
      assertFalse("inf.isParseBigDecimal", inf.isParseBigDecimal)

      val pf = NumberFormat.getPercentInstance(l).asInstanceOf[DecimalFormat]
      assertEquals("pf.toPattern", t.pf, pf.toPattern)
      assertEquals("pf.getDecimalFormatSymbols", DecimalFormatSymbols.getInstance(l), pf.getDecimalFormatSymbols)
      assertEquals("pf.getMaximumIntegerDigits", Integer.MAX_VALUE, pf.getMaximumIntegerDigits)
      assertEquals("pf.getMinimumIntegerDigits", 1, pf.getMinimumIntegerDigits)
      assertEquals("pf.getMaximumFractionDigits", 0, pf.getMaximumFractionDigits)
      assertEquals("pf.getMinimumFractionDigits", 0, pf.getMinimumFractionDigits)
      assertTrue("pf.isGroupingUsed", pf.isGroupingUsed)
      assertEquals("pf.getRoundingMode", RoundingMode.HALF_EVEN, pf.getRoundingMode)
      assertEquals("pf.getPositivePrefix", "", pf.getPositivePrefix)
      assertEquals("pf.getPositiveSuffix", DecimalFormatUtil.suffixFor(pf, DecimalFormatUtil.PatternCharPercent), pf.getPositiveSuffix)
      assertEquals("pf.getNegativePrefix", pf.getDecimalFormatSymbols.getMinusSign.toString, pf.getNegativePrefix)
      assertEquals("pf.getNegativeSuffix", DecimalFormatUtil.suffixFor(pf, DecimalFormatUtil.PatternCharPercent), pf.getNegativeSuffix)
      assertEquals("pf.getMultiplier", 100, pf.getMultiplier)
      assertEquals("pf.getGroupingSize", 3, pf.getGroupingSize)
      assertFalse("pf.isDecimalSeparatorAlwaysShown", pf.isDecimalSeparatorAlwaysShown)
      assertFalse("pf.isParseBigDecimal", pf.isParseBigDecimal)
    }
  }

  @Test def test_extra_locales_diff(): Unit = {
    extraLocalesDiff.filter(t =>
      (Platform.executingInJVM && t.cldr21) || (!Platform.executingInJVM && !t.cldr21)).foreach { t =>

      if (!Platform.executingInJVM) {
        LocaleRegistry.installLocale(t.ldml)
      }
      val l = Locale.forLanguageTag(t.tag)
      val nf = NumberFormat.getNumberInstance(l).asInstanceOf[DecimalFormat]
      assertEquals("nf.toPattern", t.nf, nf.toPattern)
      assertFalse("nf.isParseIntegerOnly", nf.isParseIntegerOnly)
      assertEquals("nf.getDecimalFormatSymbols", DecimalFormatSymbols.getInstance(l), nf.getDecimalFormatSymbols)
      assertEquals("nf.getMaximumIntegerDigits", Integer.MAX_VALUE, nf.getMaximumIntegerDigits)
      assertEquals("nf.getMinimumIntegerDigits", 1, nf.getMinimumIntegerDigits)
      assertEquals(3, nf.getMaximumFractionDigits)
      assertEquals(0, nf.getMinimumFractionDigits)
      assertTrue("nf.isGroupingUsed", nf.isGroupingUsed)
      assertEquals("nf.getRoundingMode", RoundingMode.HALF_EVEN, nf.getRoundingMode)
      assertEquals("nf.getPositivePrefix", "", nf.getPositivePrefix)
      assertEquals("nf.getPositiveSuffix", "", nf.getPositiveSuffix)
      assertTrue("hodge bodge minus sign", nf.getDecimalFormatSymbols.getMinusSign.toString == nf.getNegativePrefix ||
        nf.getDecimalFormatSymbols.getMinusSign.toString == nf.getNegativeSuffix)
      assertEquals("nf.getMultiplier", 1, nf.getMultiplier)
      assertEquals("nf.getGroupingSize", 3, nf.getGroupingSize)
      assertFalse("nf.isDecimalSeparatorAlwaysShown", nf.isDecimalSeparatorAlwaysShown)
      assertFalse("nf.isParseBigDecimal", nf.isParseBigDecimal)

      val inf = NumberFormat.getIntegerInstance(l).asInstanceOf[DecimalFormat]
      assertEquals(t.inf, inf.toPattern)
      assertTrue(inf.isParseIntegerOnly)
      assertEquals(DecimalFormatSymbols.getInstance(l), inf.getDecimalFormatSymbols)
      assertEquals(Integer.MAX_VALUE, inf.getMaximumIntegerDigits)
      assertEquals(1, inf.getMinimumIntegerDigits)
      assertEquals(0, inf.getMaximumFractionDigits)
      assertEquals(0, inf.getMinimumFractionDigits)
      assertTrue(inf.isGroupingUsed)
      assertEquals(RoundingMode.HALF_EVEN, inf.getRoundingMode)
      assertEquals("", inf.getPositivePrefix)
      assertEquals("", inf.getPositiveSuffix)
      assertTrue(inf.getDecimalFormatSymbols.getMinusSign.toString == inf.getNegativePrefix ||
        inf.getDecimalFormatSymbols.getMinusSign.toString == inf.getNegativeSuffix)
      assertEquals(1, inf.getMultiplier)
      assertEquals(3, inf.getGroupingSize)
      assertFalse(inf.isDecimalSeparatorAlwaysShown)
      assertFalse(inf.isParseBigDecimal)

      val pf = NumberFormat.getPercentInstance(l).asInstanceOf[DecimalFormat]
      assertEquals(t.pf, pf.toPattern)
      assertEquals(DecimalFormatSymbols.getInstance(l), pf.getDecimalFormatSymbols)
      assertEquals(Integer.MAX_VALUE, pf.getMaximumIntegerDigits)
      assertEquals(1, pf.getMinimumIntegerDigits)
      assertEquals(0, pf.getMaximumFractionDigits)
      assertEquals(0, pf.getMinimumFractionDigits)
      assertTrue(pf.isGroupingUsed)
      assertEquals(RoundingMode.HALF_EVEN, pf.getRoundingMode)
      assertEquals("", pf.getPositivePrefix)
      assertEquals(DecimalFormatUtil.suffixFor(pf, DecimalFormatUtil.PatternCharPercent), pf.getPositiveSuffix)
      assertTrue(pf.getDecimalFormatSymbols.getMinusSign.toString == pf.getNegativePrefix ||
        pf.getDecimalFormatSymbols.getMinusSign.toString == pf.getNegativeSuffix)
      assertEquals(100, pf.getMultiplier)
      assertEquals(3, pf.getGroupingSize)
      assertFalse(pf.isDecimalSeparatorAlwaysShown)
      assertFalse(pf.isParseBigDecimal)
    }
  }

  @Test def test_format_not_allowed(): Unit = {
    val nf = NumberFormat.getNumberInstance
    expectThrows(classOf[IllegalArgumentException], nf.format("Abc"))
  }

  @Test def test_format_integer(): Unit = {
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

  @Test def test_format_grouping(): Unit = {
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

  @Test def test_format_max_digits_count(): Unit = {
    val nf = NumberFormat.getIntegerInstance
    nf.setMaximumIntegerDigits(0)
    assertEquals("123", "0", nf.format(123))
    assertEquals("0", "0", nf.format(0))
    assertEquals("-123", "-0", nf.format(-123))
    assertEquals("1000", "0", nf.format(1000))
    assertEquals("100000", "0", nf.format(100000))
    assertEquals("10000000", "0", nf.format(10000000))
    assertEquals(s"${Int.MaxValue}", "0", nf.format(Int.MaxValue))
    assertEquals(s"${Long.MaxValue}", "0", nf.format(Long.MaxValue))
    assertEquals("-1000", "-0", nf.format(-1000))
    assertEquals("-100000", "-0", nf.format(-100000))
    assertEquals("-10000000", "-0", nf.format(-10000000))
    assertEquals(s"${Int.MinValue}", "-0", nf.format(Int.MinValue))
    assertEquals(s"${Long.MinValue}", "-0", nf.format(Long.MinValue))

    nf.setMaximumIntegerDigits(5)
    assertEquals("123-5", "123", nf.format(123))
    assertEquals("0-5", "0", nf.format(0))
    assertEquals("-123-5", "-123", nf.format(-123))
    assertEquals("1000-5", "1,000", nf.format(1000))
    assertEquals("100000-5", "00,000", nf.format(100000))
    assertEquals("10000000-5", "00,000", nf.format(10000000))
    assertEquals(s"${Int.MaxValue}-5", "83,647", nf.format(Int.MaxValue))
    assertEquals(s"${Long.MaxValue}-5", "75,807", nf.format(Long.MaxValue))
    assertEquals("-1000-5", "-1,000", nf.format(-1000))
    assertEquals("-100000-5", "-00,000", nf.format(-100000))
    assertEquals("-10000000-5", "-00,000", nf.format(-10000000))
    assertEquals(s"${Int.MinValue}-5", "-83,648", nf.format(Int.MinValue))
    assertEquals(s"${Long.MinValue}-5", "-75,808", nf.format(Long.MinValue))
  }

  @Test def test_format_min_digits_count(): Unit = {
    val nf = NumberFormat.getIntegerInstance
    nf.setMinimumIntegerDigits(0)
    assertEquals("123", "123", nf.format(123))
    assertEquals("0", "0", nf.format(0))
    assertEquals("-123", "-123", nf.format(-123))
    assertEquals("1,000", "1,000", nf.format(1000))
    assertEquals("100,000", "100,000", nf.format(100000))
    assertEquals("10,000,000", "10,000,000", nf.format(10000000))
    assertEquals("2,147,483,647", "2,147,483,647", nf.format(Int.MaxValue))
    assertEquals("9,223,372,036,854,775,807", "9,223,372,036,854,775,807", nf.format(Long.MaxValue))
    assertEquals("-1,000", "-1,000", nf.format(-1000))
    assertEquals("-100,000", "-100,000", nf.format(-100000))
    assertEquals("-10,000,000", "-10,000,000", nf.format(-10000000))
    assertEquals("-2,147,483,648", "-2,147,483,648", nf.format(Int.MinValue))
    assertEquals("-9,223,372,036,854,775,808", "-9,223,372,036,854,775,808", nf.format(Long.MinValue))

    nf.setMinimumIntegerDigits(5)
    assertEquals("00,123", "00,123", nf.format(123))
    assertEquals("00,000", "00,000", nf.format(0))
    assertEquals("-00,123", "-00,123", nf.format(-123))
    assertEquals("01,000", "01,000", nf.format(1000))
    assertEquals("100,000", "100,000", nf.format(100000))
    assertEquals("10,000,000", "10,000,000", nf.format(10000000))
    assertEquals("2,147,483,647", "2,147,483,647", nf.format(Int.MaxValue))
    assertEquals("9,223,372,036,854,775,807", "9,223,372,036,854,775,807", nf.format(Long.MaxValue))
    assertEquals("-01,000", "-01,000", nf.format(-1000))
    assertEquals("-100,000", "-100,000", nf.format(-100000))
    assertEquals("-10,000,000", "-10,000,000", nf.format(-10000000))
    assertEquals("-2,147,483,648", "-2,147,483,648", nf.format(Int.MinValue))
    assertEquals("-9,223,372,036,854,775,808", "-9,223,372,036,854,775,808", nf.format(Long.MinValue))
  }

  @Test def test_format_locales_different_group(): Unit = {
    val nf = NumberFormat.getIntegerInstance(Locale.CANADA_FRENCH)
    // Different group separator
    assertEquals("123", nf.format(123))
    assertEquals("0", nf.format(0))
    assertEquals("-123", nf.format(-123))
    assertEquals("1\u00A0000", nf.format(1000))
    assertEquals("100\u00A0000", nf.format(100000))
    assertEquals("10\u00A0000\u00A0000", nf.format(10000000))
    assertEquals("2\u00A0147\u00A0483\u00A0647", nf.format(Int.MaxValue))
    assertEquals("9\u00A0223\u00A0372\u00A0036\u00A0854\u00A0775\u00A0807", nf.format(Long.MaxValue))
    assertEquals("-1\u00A0000", nf.format(-1000))
    assertEquals("-100\u00A0000", nf.format(-100000))
    assertEquals("-10\u00A0000\u00A0000", nf.format(-10000000))
    assertEquals("-2\u00A0147\u00A0483\u00A0648", nf.format(Int.MinValue))
    assertEquals("-9\u00A0223\u00A0372\u00A0036\u00A0854\u00A0775\u00A0808", nf.format(Long.MinValue))
  }

  @Test def test_format_locales_different_negative(): Unit = {
    if (!Platform.executingInJVM) {
      LocaleRegistry.installLocale(lt)
    }
    val nf = NumberFormat.getIntegerInstance(Locale.forLanguageTag("lt")).asInstanceOf[DecimalFormat]
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
