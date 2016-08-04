package testsuite.javalib.text

import java.text.{DecimalFormat, DecimalFormatSymbols, NumberFormat}
import java.util.Locale

import org.junit.Test
import org.junit.Assert._
import testsuite.utils.{LocaleTestSetup, Platform}

class NumberFormatTest extends LocaleTestSetup {
  case class TestCase(tag: String, l: Locale, cldr21: Boolean, nf: String, inf: String, pf: String)

  val stdLocales = List(
    TestCase("und", Locale.ROOT, cldr21 = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("en", Locale.ENGLISH, cldr21 = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("fr", Locale.FRENCH, cldr21 = true, "#,##0.###", "#,##0", "#,##0\u00A0%"),
    TestCase("de", Locale.GERMAN, cldr21 = true, "#,##0.###", "#,##0", "#,##0\u00A0%"),
    TestCase("it", Locale.ITALIAN, cldr21 = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("ko", Locale.KOREAN, cldr21 = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("zh", Locale.CHINESE, cldr21 = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("zh", Locale.SIMPLIFIED_CHINESE, cldr21 = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("zh-TW", Locale.TRADITIONAL_CHINESE, cldr21 = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("fr-FR", Locale.FRANCE, cldr21 = true, "#,##0.###", "#,##0", "#,##0\u00A0%"),
    TestCase("de-DE", Locale.GERMANY, cldr21 = true, "#,##0.###", "#,##0", "#,##0\u00A0%"),
    TestCase("it-IT", Locale.ITALY, cldr21 = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("ko-KO", Locale.KOREA, cldr21 = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("zh-CH", Locale.CHINA, cldr21 = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("zh-CH", Locale.PRC, cldr21 = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("zh-TW", Locale.TAIWAN, cldr21 = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("en-GB", Locale.UK, cldr21 = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("en-US", Locale.US, cldr21 = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("en-CA", Locale.CANADA, cldr21 = true, "#,##0.###", "#,##0", "#,##0%"),
    TestCase("fr-CA", Locale.CANADA_FRENCH, cldr21 = true, "#,##0.###", "#,##0", "#,##0\u00A0%")
  )

  @Test def test_constants(): Unit = {
    assertEquals(0, NumberFormat.INTEGER_FIELD)
    assertEquals(1, NumberFormat.FRACTION_FIELD)
  }

  @Test def test_default_instance(): Unit = {
    val nf = NumberFormat.getNumberInstance.asInstanceOf[DecimalFormat]
    assertEquals("#,##0.###", nf.toPattern)
    assertEquals(DecimalFormatSymbols.getInstance(), nf.getDecimalFormatSymbols)

    val pf = NumberFormat.getPercentInstance.asInstanceOf[DecimalFormat]
    assertEquals("#,##0%", pf.toPattern)
    assertEquals(DecimalFormatSymbols.getInstance(), pf.getDecimalFormatSymbols)
  }

  @Test def test_default_locales(): Unit = {
    stdLocales.foreach { t =>
      val nf = NumberFormat.getNumberInstance(t.l).asInstanceOf[DecimalFormat]
      assertEquals(t.nf, nf.toPattern)
      assertFalse(nf.isParseIntegerOnly)
      assertEquals(DecimalFormatSymbols.getInstance(t.l), nf.getDecimalFormatSymbols)

      val inf = NumberFormat.getIntegerInstance(t.l).asInstanceOf[DecimalFormat]
      assertEquals(t.inf, inf.toPattern)
      assertTrue(inf.isParseIntegerOnly)
      assertEquals(DecimalFormatSymbols.getInstance(t.l), inf.getDecimalFormatSymbols)

      val pf = NumberFormat.getPercentInstance(t.l).asInstanceOf[DecimalFormat]
      assertEquals(t.pf, pf.toPattern)
      assertEquals(DecimalFormatSymbols.getInstance(t.l), pf.getDecimalFormatSymbols)
    }
  }
}
