package org.scalajs.testsuite.javalib.text

import java.text.DecimalFormatSymbols
import java.util.Locale

import org.junit.{Before, Ignore, Test}
import org.junit.Assert._
import org.scalajs.testsuite.utils.LocaleTestSetup
import org.scalajs.testsuite.utils.Platform

class DecimalFormatSymbolsTest extends LocaleTestSetup {
  // Clean up the locale database, there are different implementations for
  // the JVM and JS
  @Before def cleanup: Unit = super.cleanDatabase

  val testData = Map(
    Locale.ROOT                -> List("0", ",", "‰", "%", "#", ";", "∞", "NaN", "-"),
    Locale.ENGLISH             -> List("0", ",", "‰", "%", "#", ";", "∞", "NaN", "-"),
    Locale.FRENCH              -> List("0", "\u00A0", "‰", "%", "#", ";", "∞", "NaN", "-"),
    Locale.GERMAN              -> List("0", ".", "‰", "%", "#", ";", "∞", "NaN", "-"),
    Locale.ITALIAN             -> List("0", ".", "‰", "%", "#", ";", "∞", "NaN", "-"),
    //Locale.JAPANESE -> List("0", ",", "‰", "%", "#", ";", "∞", "NaN（非数）", "-")
    Locale.KOREAN              -> List("0", ",", "‰", "%", "#", ";", "∞", "NaN", "-"),
    Locale.CHINESE             -> List("0", ",", "‰", "%", "#", ";", "∞", "NaN", "-"),
    Locale.SIMPLIFIED_CHINESE  -> List("0", ",", "‰", "%", "#", ";", "∞", "NaN", "-"),
    Locale.TRADITIONAL_CHINESE -> List("0", ",", "‰", "%", "#", ";", "∞", "非數值", "-"),
    Locale.FRANCE              -> List("0", "\u00A0", "‰", "%", "#", ";", "∞", "NaN", "-"),
    Locale.GERMANY             -> List("0", ".", "‰", "%", "#", ";", "∞", "NaN", "-"),
    Locale.ITALY               -> List("0", ".", "‰", "%", "#", ";", "∞", "NaN", "-"),
    //Locale.JAPAN -> List("0", ",", "‰", "%", "#", ";", "∞", "NaN（非数）", "-")
    Locale.KOREA               -> List("0", ",", "‰", "%", "#", ";", "∞", "NaN", "-"),
    Locale.CHINA               -> List("0", ",", "‰", "%", "#", ";", "∞", "NaN", "-"),
    Locale.PRC                 -> List("0", ",", "‰", "%", "#", ";", "∞", "NaN", "-"),
    Locale.TAIWAN              -> List("0", ",", "‰", "%", "#", ";", "∞", "非數值", "-"),
    Locale.UK                  -> List("0", ",", "‰", "%", "#", ";", "∞", "NaN", "-"),
    Locale.US                  -> List("0", ",", "‰", "%", "#", ";", "∞", "NaN", "-"),
    Locale.CANADA              -> List("0", ",", "‰", "%", "#", ";", "∞", "NaN", "-"),
    Locale.CANADA_FRENCH       -> List("0", "\u00A0", "‰", "%", "#", ";", "∞", "NaN", "-")
  )

  @Test def test_decimal_format_symbol(): Unit = {
    testData.foreach {
      case (l, symbols) =>
        val dfs = DecimalFormatSymbols.getInstance(l)

        assertEquals(symbols(0).charAt(0), dfs.getZeroDigit)
        assertEquals(symbols(1).charAt(0), dfs.getGroupingSeparator)
        assertEquals(symbols(2).charAt(0), dfs.getPerMill)
        assertEquals(symbols(3).charAt(0), dfs.getPercent)
        assertEquals(symbols(4).charAt(0), dfs.getDigit)
        assertEquals(symbols(5).charAt(0), dfs.getPatternSeparator)
        assertEquals(symbols(6), dfs.getInfinity)
        assertEquals(symbols(7), dfs.getNaN)
        assertEquals(symbols(8).charAt(0), dfs.getMinusSign)
    }
  }

  @Test def test_decimal_format_jp(): Unit = {
    // The JVM Japanese NaN is not the same as from CLDR
    List(Locale.JAPAN, Locale.JAPANESE).foreach { l =>
      val dfs = DecimalFormatSymbols.getInstance(l)

      assertEquals('0', dfs.getZeroDigit)
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
    }
  }

}
