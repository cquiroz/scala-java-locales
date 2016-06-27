package org.scalajs.testsuite.javalib.text

import java.text.DecimalFormatSymbols
import java.util.Locale

import org.junit.{Before, Ignore, Test}
import org.junit.Assert._
import org.scalajs.testsuite.utils.LocaleTestSetup
import org.scalajs.testsuite.utils.Platform
import org.scalajs.testsuite.utils.AssertThrows.expectThrows

import scala.scalajs.locale.LocaleRegistry
import scala.scalajs.locale.ldml.data.all.af
import scala.scalajs.locale.ldml.data.all.ar
import scala.scalajs.locale.ldml.data.all.az
import scala.scalajs.locale.ldml.data.all.az_Cyrl
import scala.scalajs.locale.ldml.data.all.bn
import scala.scalajs.locale.ldml.data.all.es_CL
import scala.scalajs.locale.ldml.data.all.fi_FI
import scala.scalajs.locale.ldml.data.all.fa
import scala.scalajs.locale.ldml.data.all.it_CH
import scala.scalajs.locale.ldml.data.all.ka
import scala.scalajs.locale.ldml.data.all.lv
import scala.scalajs.locale.ldml.data.all.my
import scala.scalajs.locale.ldml.data.all.ru_RU
import scala.scalajs.locale.ldml.data.all.smn
import scala.scalajs.locale.ldml.data.all.smn_FI
import scala.scalajs.locale.ldml.data.all.zh
import scala.scalajs.locale.ldml.data.all.zh_Hant

class DecimalFormatSymbolsTest extends LocaleTestSetup {
  // Clean up the locale database, there are different implementations for
  // the JVM and JS
  @Before def cleanup: Unit = super.cleanDatabase

  val englishSymbols = List("0", ".", ",", "‰", "%", "#", ";", "∞", "NaN", "-", "E")

  val standardLocalesData = List(
    Locale.ROOT                -> List("0", ".", ",", "‰", "%", "#", ";", "∞", "NaN", "-", "E"),
    Locale.ENGLISH             -> englishSymbols,
    Locale.FRENCH              -> List("0", ",", "\u00A0", "‰", "%", "#", ";", "∞", "NaN", "-", "E"),
    Locale.GERMAN              -> List("0", ",", ".", "‰", "%", "#", ";", "∞", "NaN", "-", "E"),
    Locale.ITALIAN             -> List("0", ",", ".", "‰", "%", "#", ";", "∞", "NaN", "-", "E"),
    Locale.KOREAN              -> List("0", ".", ",", "‰", "%", "#", ";", "∞", "NaN", "-", "E"),
    Locale.CHINESE             -> List("0", ".", ",", "‰", "%", "#", ";", "∞", "NaN", "-", "E"),
    Locale.SIMPLIFIED_CHINESE  -> List("0", ".", ",", "‰", "%", "#", ";", "∞", "NaN", "-", "E"),
    Locale.TRADITIONAL_CHINESE -> List("0", ".", ",", "‰", "%", "#", ";", "∞", "非數值", "-", "E"),
    Locale.FRANCE              -> List("0", ",", "\u00A0", "‰", "%", "#", ";", "∞", "NaN", "-", "E"),
    Locale.GERMANY             -> List("0", ",", ".", "‰", "%", "#", ";", "∞", "NaN", "-", "E"),
    Locale.ITALY               -> List("0", ",", ".", "‰", "%", "#", ";", "∞", "NaN", "-", "E"),
    Locale.KOREA               -> List("0", ".", ",", "‰", "%", "#", ";", "∞", "NaN", "-", "E"),
    Locale.CHINA               -> List("0", ".", ",", "‰", "%", "#", ";", "∞", "NaN", "-", "E"),
    Locale.PRC                 -> List("0", ".", ",", "‰", "%", "#", ";", "∞", "NaN", "-", "E"),
    Locale.TAIWAN              -> List("0", ".", ",", "‰", "%", "#", ";", "∞", "非數值", "-", "E"),
    Locale.UK                  -> List("0", ".", ",", "‰", "%", "#", ";", "∞", "NaN", "-", "E"),
    Locale.US                  -> List("0", ".", ",", "‰", "%", "#", ";", "∞", "NaN", "-", "E"),
    Locale.CANADA              -> List("0", ".", ",", "‰", "%", "#", ";", "∞", "NaN", "-", "E"),
    Locale.CANADA_FRENCH       -> List("0", ",", "\u00A0", "‰", "%", "#", ";", "∞", "NaN", "-", "E")
  )

  val extraLocalesData = List(
    // af uses latn
    (af, "af") -> List("0", ",", "\u00A0", "‰", "%", "#", ";", "∞", "NaN", "-", "E"),
    (az, "az") -> List("0", ",", ".", "‰", "%", "#", ";", "∞", "NaN", "-", "E"),
    (az_Cyrl, "az_Cyrl") -> List("0", ".", ",", "‰", "%", "#", ";", "∞", "NaN", "-", "E"),
    // ar has a default arab set of symbols
    (ar, "ar") -> List("٠", "٫", "٬", "؉", "٪", "#", "؛", "∞", "ليس رقم", "\u002D", "اس"),
    // bn has a default ns but it is a latn alias
    (bn, "bn") -> List("0", ".", ",", "‰", "%", "#", ";", "∞", "NaN", "-", "E"),
    (es_CL, "es_CL") -> List("0", ".", ",", "‰", "%", "#", ";", "∞", "NaN", "-", "E"),
    (fi_FI, "fi_FI") -> List("0", ".", ",", "‰", "%", "#", ";", "∞", "NaN", "-", "E"),
    (it_CH, "it_CH") -> List("0", ".", ",", "‰", "%", "#", ";", "∞", "NaN", "-", "E"),
    (ru_RU, "ru_RU") -> List("0", ".", ",", "‰", "%", "#", ";", "∞", "NaN", "-", "E"),
    (smn_FI, "smn_FI") -> List("0", ".", ",", "‰", "%", "#", ";", "∞", "NaN", "-", "E"),
    (zh, "zh") -> List("0", ".", ",", "‰", "%", "#", ";", "∞", "NaN", "-", "E"),
    (zh_Hant, "zh_Hant") -> List("0", ".", ",", "‰", "%", "#", ";", "∞", "NaN", "-", "E")
  )

  def test_dfs(dfs: DecimalFormatSymbols, symbols: List[String]): Unit = {
    assertEquals(symbols(0).charAt(0), dfs.getZeroDigit)
    assertEquals(symbols(1).charAt(0), dfs.getDecimalSeparator)
    assertEquals(symbols(2).charAt(0), dfs.getGroupingSeparator)
    assertEquals(symbols(3).charAt(0), dfs.getPerMill)
    assertEquals(symbols(4).charAt(0), dfs.getPercent)
    assertEquals(symbols(5).charAt(0), dfs.getDigit)
    assertEquals(symbols(6).charAt(0), dfs.getPatternSeparator)
    assertEquals(symbols(7), dfs.getInfinity)
    // Sometimes the JVM uses unicode 'SPACE' instead of 'NO-BREAK SPACE'
    assertEquals(symbols(8), dfs.getNaN.replace('\u00A0', '\u0020'))
    assertEquals(symbols(9).charAt(0), dfs.getMinusSign)
    assertEquals(symbols(10), dfs.getExponentSeparator)
  }

  @Test def test_default_locales_decimal_format_symbol(): Unit = {
    standardLocalesData.foreach {
      case (l, symbols) =>
        val dfs = DecimalFormatSymbols.getInstance(l)
        test_dfs(dfs, symbols)
    }
  }

  @Test def test_extra_locales_decimal_format_symbol(): Unit = {
    extraLocalesData.foreach {
      case ((d, tag), symbols) =>
        if (!Platform.executingInJVM) {
          LocaleRegistry.installLocale(d)
        }
        val l = Locale.forLanguageTag(tag)
        val dfs = DecimalFormatSymbols.getInstance(l)
        test_dfs(dfs, symbols)
    }
  }

  @Test def test_available_locales(): Unit = {
    val initial = DecimalFormatSymbols.getAvailableLocales.length
    assertTrue(initial > 0)
    if (!Platform.executingInJVM) {
      LocaleRegistry.installLocale(af)
      // In JS all locales have a decimal format symbols instance
      assertEquals(initial + 1, DecimalFormatSymbols.getAvailableLocales.length)
    }
  }

  @Test def test_decimal_format_jp(): Unit = {
    // The JVM Japanese NaN is not the same as from CLDR
    List(Locale.JAPAN, Locale.JAPANESE).foreach { l =>
      val dfs = DecimalFormatSymbols.getInstance(l)

      assertEquals('0', dfs.getZeroDigit)
      assertEquals('.', dfs.getDecimalSeparator)
      assertEquals(',', dfs.getGroupingSeparator)
      assertEquals('‰', dfs.getPerMill)
      assertEquals('%', dfs.getPercent)
      assertEquals('#', dfs.getDigit)
      assertEquals(';', dfs.getPatternSeparator)
      assertEquals("∞", dfs.getInfinity)
      if (Platform.executingInJVM)
        assertEquals("NaN（非数）", dfs.getNaN)
      else
        assertEquals("NaN", dfs.getNaN)
      assertEquals('-', dfs.getMinusSign)
      assertEquals("E", dfs.getExponentSeparator)
    }
  }

  @Test def test_defaults(): Unit = {
    val dfs = new DecimalFormatSymbols()
    test_dfs(dfs, englishSymbols)
  }

  @Test def test_setters(): Unit = {
    val dfs = new DecimalFormatSymbols()
    dfs.setZeroDigit('1')
    assertEquals('1', dfs.getZeroDigit)
    dfs.setGroupingSeparator('1')
    assertEquals('1', dfs.getGroupingSeparator)
    dfs.setDecimalSeparator('1')
    assertEquals('1', dfs.getDecimalSeparator)
    dfs.setPerMill('1')
    assertEquals('1', dfs.getPerMill)
    dfs.setPercent('1')
    assertEquals('1', dfs.getPercent)
    dfs.setDigit('1')
    assertEquals('1', dfs.getDigit)
    dfs.setPatternSeparator('1')
    assertEquals('1', dfs.getPatternSeparator)
    dfs.setMinusSign('1')
    assertEquals('1', dfs.getMinusSign)

    dfs.setInfinity(null)
    assertNull(dfs.getInfinity)
    dfs.setInfinity("Inf")
    assertEquals("Inf", dfs.getInfinity)

    dfs.setNaN(null)
    assertNull(dfs.getNaN)
    dfs.setNaN("nan")
    assertEquals("nan", dfs.getNaN)

    expectThrows(classOf[NullPointerException], dfs.setExponentSeparator(null))
    dfs.setExponentSeparator("exp")
    assertEquals("exp", dfs.getExponentSeparator)
  }

  @Test def test_clone(): Unit = {
    val dfs = new DecimalFormatSymbols()
    assertEquals(dfs, dfs.clone())
    assertNotSame(dfs, dfs.clone())
  }

  @Test def test_equals(): Unit = {
    val dfs = new DecimalFormatSymbols()
    assertEquals(dfs, dfs)
    assertSame(dfs, dfs)
    assertFalse(dfs.equals(null))
    assertFalse(dfs.equals(1))
    val dfs2 = new DecimalFormatSymbols()
    assertEquals(dfs, dfs2)
    assertNotSame(dfs, dfs2)
    dfs2.setDigit('i')
    assertFalse(dfs.equals(dfs2))
  }

  @Ignore @Test def test_hash_code(): Unit = {
    val dfs = new DecimalFormatSymbols()
    assertEquals(dfs.hashCode, dfs.hashCode)
    val dfs2 = new DecimalFormatSymbols()
    assertEquals(dfs.hashCode, dfs2.hashCode)
    dfs2.setExponentSeparator("abc")
    // These tests should fail but they pass on the JVM
    assertEquals(dfs.hashCode, dfs2.hashCode)
    standardLocalesData.filter(_._1 != Locale.ROOT).foreach {
      case (l, symbols) =>
        val df = DecimalFormatSymbols.getInstance(l)
        assertFalse(dfs.hashCode.equals(df.hashCode))
    }
  }
}
