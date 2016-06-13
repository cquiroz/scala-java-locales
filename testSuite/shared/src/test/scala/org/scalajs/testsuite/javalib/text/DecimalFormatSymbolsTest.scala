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

  @Test def test_decimal_format_symbol_en(): Unit = {
    val dfs = DecimalFormatSymbols.getInstance(Locale.ENGLISH)

    assertEquals('.', dfs.getDecimalSeparator)
    assertEquals('-', dfs.getMinusSign)
    assertEquals('0', dfs.getZeroDigit)
  }

}
