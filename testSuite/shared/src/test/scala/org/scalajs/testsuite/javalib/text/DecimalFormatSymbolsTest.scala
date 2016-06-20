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

  val testData = Map(
    Locale.ROOT -> List("0", ",", "‰", "%", "#", ";", "∞", "NaN", "-"),
    Locale.ENGLISH -> List("0", ",", "‰", "%", "#", ";", "∞", "NaN", "-"),
    Locale.FRENCH -> List("0", "\u00A0", "‰", "%", "#", ";", "∞", "NaN", "-"),
    Locale.GERMAN -> List("0", ".", "‰", "%", "#", ";", "∞", "NaN", "-"),
    Locale.ITALIAN -> List("0", ".", "‰", "%", "#", ";", "∞", "NaN", "-")
    //Locale.JAPANESE -> List("0", ",", "‰", "%", "#", ";", "∞", "NaN（非数）", "-")
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
