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

  @Test def test_root(): Unit = {
    val dfs = DecimalFormatSymbols.getInstance(Locale.forLanguageTag(""))

    assertEquals('0', dfs.getZeroDigit)
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

  @Test def test_decimal_format_symbol_en(): Unit = {
    val dfs = DecimalFormatSymbols.getInstance(Locale.ENGLISH)

    assertEquals('0', dfs.getZeroDigit)
    assertEquals(',', dfs.getGroupingSeparator)
    assertEquals('‰', dfs.getPerMill)
    assertEquals('%', dfs.getPercent)
    assertEquals('#', dfs.getDigit)
    assertEquals(';', dfs.getPatternSeparator)
    assertEquals("∞", dfs.getInfinity)
    assertEquals("NaN", dfs.getNaN)
    assertEquals('-', dfs.getMinusSign)
  }

}
