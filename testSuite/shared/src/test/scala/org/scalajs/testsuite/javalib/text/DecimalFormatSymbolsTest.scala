package org.scalajs.testsuite.javalib.text

import java.text.DecimalFormatSymbols
import java.util.Locale

import org.junit.{Before, Ignore, Test}
import org.junit.Assert._
import org.scalajs.testsuite.utils.LocaleTestSetup

class DecimalFormatSymbolsTest extends LocaleTestSetup {
  // Clean up the locale database, there are different implementations for
  // the JVM and JS
  @Before def cleanup: Unit = super.cleanDatabase

  @Ignore @Test def test_root(): Unit = {
    val dfs = DecimalFormatSymbols.getInstance(Locale.forLanguageTag(""))

    //assertEquals('0', dfs.getZeroDigit)
    assertEquals(',', dfs.getGroupingSeparator)
    // 8240 is the per/mile character: ‰
    assertEquals('‰', dfs.getPerMill)
    assertEquals('%', dfs.getPercent)
    assertEquals('#', dfs.getDigit)
    assertEquals(';', dfs.getPatternSeparator)
    assertEquals("∞", dfs.getInfinity)
    assertEquals("NaN", dfs.getNaN)
    assertEquals('-', dfs.getMinusSign)
  }

  @Ignore @Test def test_decimal_format_symbol_en(): Unit = {
    val dfs = DecimalFormatSymbols.getInstance(Locale.ENGLISH)

    //assertEquals('0', dfs.getZeroDigit)
    assertEquals(',', dfs.getGroupingSeparator)
    assertEquals('‰', dfs.getPerMill)
    assertEquals('%', dfs.getPercent)
    assertEquals('#', dfs.getDigit)
    assertEquals(';', dfs.getPatternSeparator)
    assertEquals("∞", dfs.getInfinity)
    assertEquals("NaN", dfs.getNaN)
    assertEquals('-', dfs.getMinusSign)
  }

  val testData = Map(
    Locale.GERMAN -> List("0", ".", "‰", "%", "#", ";", "∞", "NaN", "-")
  )

  @Test def test_decimal_format_symbol(): Unit = {
    testData.foreach {
      case (l, symbols) =>
        val dfs = DecimalFormatSymbols.getInstance(l)

        //assertEquals('0', dfs.getZeroDigit)
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

}
