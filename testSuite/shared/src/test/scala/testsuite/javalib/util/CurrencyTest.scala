package testsuite.javalib.util

import java.util.{ Currency, Locale }
import testsuite.utils.Platform

class CurrencyTest extends munit.FunSuite {
  case class CurrencyTestResults(
    expectedCurrencyCode:   String,
    expectedNumericCode:    Int,
    expectedFractionDigits: Int,
    expectedSymbol:         String,
    expectedDisplayName:    String
  )

  // Have separate js vs jvm results to allow for CLDR differences
  trait CombinedCurrencyTestResults {
    def jsNativeResults: CurrencyTestResults
    def jvmResults: CurrencyTestResults
  }

  case class LocaleCurrencyTest(
    locale:          Locale,
    jsNativeResults: CurrencyTestResults,
    jvmResults:      CurrencyTestResults
  ) extends CombinedCurrencyTestResults
  object LocaleCurrencyTest         {
    def apply(locale: Locale, results: CurrencyTestResults): LocaleCurrencyTest =
      LocaleCurrencyTest(locale, results, results)
  }

  case class CodeCurrencyTest(
    currencyCode:    String,
    jsNativeResults: CurrencyTestResults,
    jvmResults:      CurrencyTestResults
  ) extends CombinedCurrencyTestResults
  object CodeCurrencyTest           {
    def apply(currencyCode: String, results: CurrencyTestResults): CodeCurrencyTest =
      CodeCurrencyTest(currencyCode, results, results)
  }

  case class DefaultLocaleCurrencyTest(defaultLocale: Locale, tests: Seq[CodeCurrencyTest])

  // Given a locale, lookup a currency code for it & test values
  private val localeCurrencyTests: Seq[LocaleCurrencyTest] = Seq(
    LocaleCurrencyTest(
      Locale.CANADA,
      results = CurrencyTestResults("CAD", 124, 2, "$", "Canadian Dollar")
    ),
    LocaleCurrencyTest(
      Locale.CANADA_FRENCH,
      jsNativeResults = CurrencyTestResults("CAD", 124, 2, "$", "dollar canadien"),
      jvmResults = CurrencyTestResults("CAD", 124, 2, "$ CA", "dollar canadien")
    ),
    LocaleCurrencyTest(
      Locale.CHINA,
      jsNativeResults = CurrencyTestResults("CNY", 156, 2, "¥", "人民币"),
      jvmResults = CurrencyTestResults("CNY", 156, 2, "¥", "人民币")
    ),
    LocaleCurrencyTest(
      Locale.FRANCE,
      results = CurrencyTestResults("EUR", 978, 2, "€", "euro")
    ),
    LocaleCurrencyTest(
      Locale.GERMANY,
      results = CurrencyTestResults("EUR", 978, 2, "€", "Euro")
    ),
    LocaleCurrencyTest(
      Locale.ITALY,
      jsNativeResults = CurrencyTestResults("EUR", 978, 2, "€", "euro"),
      jvmResults = CurrencyTestResults("EUR", 978, 2, "€", "euro")
    ),
    LocaleCurrencyTest(
      Locale.JAPAN,
      results = CurrencyTestResults("JPY", 392, 0, "￥", "日本円")
    ),
    LocaleCurrencyTest(
      Locale.KOREA,
      results = CurrencyTestResults("KRW", 410, 0, "₩", "대한민국 원")
    ),
    LocaleCurrencyTest(
      Locale.PRC,
      jsNativeResults = CurrencyTestResults("CNY", 156, 2, "¥", "人民币"),
      jvmResults = CurrencyTestResults("CNY", 156, 2, "¥", "人民币")
    ),
    LocaleCurrencyTest(
      Locale.TAIWAN,
      jsNativeResults = CurrencyTestResults("TWD", 901, 2, "$", "新台幣"),
      jvmResults = CurrencyTestResults("TWD", 901, 2, "$", "新台幣")
    ),
    LocaleCurrencyTest(
      Locale.UK,
      jsNativeResults = CurrencyTestResults("GBP", 826, 2, "£", "British Pound"),
      jvmResults = CurrencyTestResults("GBP", 826, 2, "£", "British Pound")
    ),
    LocaleCurrencyTest(
      Locale.US,
      jsNativeResults = CurrencyTestResults("USD", 840, 2, "$", "US Dollar"),
      jvmResults = CurrencyTestResults("USD", 840, 2, "$", "US Dollar")
    )
  )

  // Given a default locale, lookup other currencies by code & compare values
  private val defaultLocaleCurrencyTests: Seq[DefaultLocaleCurrencyTest] = Seq(
    DefaultLocaleCurrencyTest(
      defaultLocale = Locale.US,
      tests = Seq(
        CodeCurrencyTest(
          currencyCode = "CAD",
          jsNativeResults = CurrencyTestResults("CAD", 124, 2, "CA$", "Canadian Dollar"),
          jvmResults = CurrencyTestResults("CAD", 124, 2, "CA$", "Canadian Dollar")
        ),
        CodeCurrencyTest(
          currencyCode = "EUR",
          jsNativeResults = CurrencyTestResults("EUR", 978, 2, "€", "Euro"),
          jvmResults = CurrencyTestResults("EUR", 978, 2, "€", "Euro")
        ),
        CodeCurrencyTest(
          currencyCode = "JPY",
          jsNativeResults = CurrencyTestResults("JPY", 392, 0, "¥", "Japanese Yen"),
          jvmResults = CurrencyTestResults("JPY", 392, 0, "¥", "Japanese Yen")
        ),
        CodeCurrencyTest(
          currencyCode = "KRW",
          jsNativeResults = CurrencyTestResults("KRW", 410, 0, "₩", "South Korean Won"),
          jvmResults = CurrencyTestResults("KRW", 410, 0, "₩", "South Korean Won")
        ),
        CodeCurrencyTest(
          currencyCode = "CNY",
          jsNativeResults = CurrencyTestResults("CNY", 156, 2, "CN¥", "Chinese Yuan"),
          jvmResults = CurrencyTestResults("CNY", 156, 2, "CN¥", "Chinese Yuan")
        ),
        CodeCurrencyTest(
          currencyCode = "TWD",
          jsNativeResults = CurrencyTestResults("TWD", 901, 2, "NT$", "New Taiwan Dollar"),
          jvmResults = CurrencyTestResults("TWD", 901, 2, "NT$", "New Taiwan Dollar")
        ),
        CodeCurrencyTest(
          currencyCode = "GBP",
          jsNativeResults = CurrencyTestResults("GBP", 826, 2, "£", "British Pound"),
          jvmResults = CurrencyTestResults("GBP", 826, 2, "£", "British Pound")
        ),
        CodeCurrencyTest(
          currencyCode = "USD",
          jsNativeResults = CurrencyTestResults("USD", 840, 2, "$", "US Dollar"),
          jvmResults = CurrencyTestResults("USD", 840, 2, "$", "US Dollar")
        )
      )
    ),
    DefaultLocaleCurrencyTest(
      defaultLocale = Locale.GERMANY,
      tests = Seq(
        CodeCurrencyTest(
          currencyCode = "CAD",
          results = CurrencyTestResults("CAD", 124, 2, "CA$", "Kanadischer Dollar")
        ),
        CodeCurrencyTest(
          currencyCode = "EUR",
          results = CurrencyTestResults("EUR", 978, 2, "€", "Euro")
        ),
        CodeCurrencyTest(
          currencyCode = "JPY",
          jsNativeResults = CurrencyTestResults("JPY", 392, 0, "¥", "Japanischer Yen"),
          jvmResults = CurrencyTestResults("JPY", 392, 0, "¥", "Japanischer Yen")
        ),
        CodeCurrencyTest(
          currencyCode = "KRW",
          results = CurrencyTestResults("KRW", 410, 0, "₩", "Südkoreanischer Won")
        ),
        CodeCurrencyTest(
          currencyCode = "CNY",
          results = CurrencyTestResults("CNY", 156, 2, "CN¥", "Renminbi Yuan")
        ),
        CodeCurrencyTest(
          currencyCode = "TWD",
          results = CurrencyTestResults("TWD", 901, 2, "NT$", "Neuer Taiwan-Dollar")
        ),
        CodeCurrencyTest(
          currencyCode = "GBP",
          jsNativeResults = CurrencyTestResults("GBP", 826, 2, "£", "Britisches Pfund"),
          jvmResults = CurrencyTestResults("GBP", 826, 2, "£", "Britisches Pfund")
        ),
        CodeCurrencyTest(
          currencyCode = "USD",
          results = CurrencyTestResults("USD", 840, 2, "$", "US-Dollar")
        )
      )
    ),
    DefaultLocaleCurrencyTest(
      defaultLocale = Locale.CHINA,
      tests = Seq(
        CodeCurrencyTest(
          currencyCode = "CAD",
          results = CurrencyTestResults("CAD", 124, 2, "CA$", "加拿大元")
        ),
        CodeCurrencyTest(
          currencyCode = "EUR",
          results = CurrencyTestResults("EUR", 978, 2, "€", "欧元")
        ),
        CodeCurrencyTest(
          currencyCode = "JPY",
          results = CurrencyTestResults("JPY", 392, 0, "JP¥", "日元")
        ),
        CodeCurrencyTest(
          currencyCode = "KRW",
          jsNativeResults = CurrencyTestResults("KRW", 410, 0, "￦", "韩元"),
          jvmResults = CurrencyTestResults("KRW", 410, 0, "￦", "韩元")
        ),
        CodeCurrencyTest(
          currencyCode = "CNY",
          jsNativeResults = CurrencyTestResults("CNY", 156, 2, "¥", "人民币"),
          jvmResults = CurrencyTestResults("CNY", 156, 2, "¥", "人民币")
        ),
        CodeCurrencyTest(
          currencyCode = "TWD",
          results = CurrencyTestResults("TWD", 901, 2, "NT$", "新台币")
        ),
        CodeCurrencyTest(
          currencyCode = "GBP",
          results = CurrencyTestResults("GBP", 826, 2, "£", "英镑")
        ),
        CodeCurrencyTest(
          currencyCode = "USD",
          results = CurrencyTestResults("USD", 840, 2, "US$", "美元")
        )
      )
    )
  )

  private def testCurrency(currency: Currency, expectedResults: CurrencyTestResults): Unit = {
    import expectedResults._

    assertEquals(expectedCurrencyCode, currency.getCurrencyCode)
    assertEquals(expectedNumericCode, currency.getNumericCode)
    assertEquals(expectedFractionDigits, currency.getDefaultFractionDigits)
    assertEquals(expectedSymbol, currency.getSymbol)
    assertEquals(expectedDisplayName, currency.getDisplayName)
  }

  protected def test_standard_locales(
    f: CombinedCurrencyTestResults => CurrencyTestResults
  ): Unit = {
    // Basic test, get a locale's currency, and test results
    localeCurrencyTests.foreach { (test: LocaleCurrencyTest) =>
      // Even when you get a currency for a specific locale, description calls (getSymbol and getDisplayName)
      // return strings will be based upon the default locale, so lets set the default locale to the test locale
      Locale.setDefault(test.locale)
      val localeCurrency = Currency.getInstance(test.locale)
      testCurrency(localeCurrency, f(test))
    }

    // Set a default locale, then lookup multiple currencies by code and test results
    defaultLocaleCurrencyTests.foreach { (defaultTest: DefaultLocaleCurrencyTest) =>
      Locale.setDefault(defaultTest.defaultLocale)
      defaultTest.tests.foreach { (test: CodeCurrencyTest) =>
        val codeCurrency = Currency.getInstance(test.currencyCode)
        testCurrency(codeCurrency, f(test))
      }
    }
  }

  test("available_currencies") {
    assert(Currency.getAvailableCurrencies().size() > 0)
  }

  test("standard_locales") {
    if (Platform.executingInJVM) {
      test_standard_locales(_.jvmResults)
    } else {
      test_standard_locales(_.jsNativeResults)
    }
  }

  override def munitIgnore: Boolean = Platform.executingInScalaNative
}
