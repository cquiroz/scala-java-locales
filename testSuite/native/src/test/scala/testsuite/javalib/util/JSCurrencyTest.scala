package testsuite.javalib.util

import java.util.Locale
import java.util.Currency
import utest._
import testsuite.utils.LocaleTestSetup
import testsuite.utils.AssertThrows.expectThrows

class JSCurrencyTest extends TestSuite with LocaleTestSetup with CurrencyTest {

  // Clean up the locale database, there are different implementations for
  // the JVM and JS
  override def utestBeforeEach(path: Seq[String]): Unit = super.cleanDatabase

  val tests = Tests {
    'test_available_currencies - {
      assertTrue(Currency.getAvailableCurrencies().size() > 0)
    }

    'test_standard_locales - {
      test_standard_locales(_.jsResults)
    }
  }
}
