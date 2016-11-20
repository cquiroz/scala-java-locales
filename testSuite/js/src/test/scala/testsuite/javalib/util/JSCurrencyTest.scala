package testsuite.javalib.util

import java.util.Locale
import java.util.Currency
import org.junit.{Before, Test}
import org.junit.Assert._
import testsuite.utils.LocaleTestSetup
import testsuite.utils.AssertThrows.expectThrows

class JSCurrencyTest extends LocaleTestSetup with CurrencyTest {

  // Clean up the locale database, there are different implementations for
  // the JVM and JS
  @Before def cleanup: Unit = super.cleanDatabase


  @Test def test_available_currencies(): Unit = {
    assertTrue(Currency.getAvailableCurrencies().size() > 0)
  }

  @Test def test_standard_locales(): Unit = {
    test_standard_locales(_.jsResults)
  }
}
