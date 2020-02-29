package testsuite.javalib.util

import java.util.{ Currency, Locale }

// Shared test data for JVM/JS so it's not duplicated
trait CurrencyTest extends munit.FunSuite {
  case class CurrencyTestResults(
    expectedCurrencyCode:   String,
    expectedNumericCode:    Int,
    expectedFractionDigits: Int,
    expectedSymbol:         String,
    expectedDisplayName:    String
  )

  // Have separate js vs jvm results to allow for CLDR differences
  trait CombinedCurrencyTestResults {
    def jsResults: CurrencyTestResults
    def jvmResults: CurrencyTestResults
  }

  case class LocaleCurrencyTest(
    locale:     Locale,
    jsResults:  CurrencyTestResults,
    jvmResults: CurrencyTestResults
  ) extends CombinedCurrencyTestResults

  case class CodeCurrencyTest(
    currencyCode: String,
    jsResults:    CurrencyTestResults,
    jvmResults:   CurrencyTestResults
  ) extends CombinedCurrencyTestResults

  case class DefaultLocaleCurrencyTest(defaultLocale: Locale, tests: Seq[CodeCurrencyTest])

  // Given a locale, lookup a currency code for it & test values
  private val localeCurrencyTests: Seq[LocaleCurrencyTest] = Seq(
    LocaleCurrencyTest(
      Locale.CANADA,
      jsResults  = CurrencyTestResults("CAD", 124, 2, "$", "Canadian Dollar"),
      jvmResults = CurrencyTestResults("CAD", 124, 2, "$", "Canadian Dollar")
    ),
    LocaleCurrencyTest(
      Locale.CANADA_FRENCH,
      jsResults  = CurrencyTestResults("CAD", 124, 2, "$", "dollar canadien"),
      jvmResults = CurrencyTestResults("CAD", 124, 2, "$", "dollar canadien")
    ),
    LocaleCurrencyTest(
      Locale.CHINA,
      jsResults  = CurrencyTestResults("CNY", 156, 2, "¥", "人民币"),
      jvmResults = CurrencyTestResults("CNY", 156, 2, "￥", "人民币")
    ),
    LocaleCurrencyTest(
      Locale.FRANCE,
      jsResults  = CurrencyTestResults("EUR", 978, 2, "€", "euro"),
      jvmResults = CurrencyTestResults("EUR", 978, 2, "€", "euro")
    ),
    LocaleCurrencyTest(
      Locale.GERMANY,
      jsResults  = CurrencyTestResults("EUR", 978, 2, "€", "Euro"),
      jvmResults = CurrencyTestResults("EUR", 978, 2, "€", "Euro")
    ),
    LocaleCurrencyTest(
      Locale.ITALY,
      jsResults  = CurrencyTestResults("EUR", 978, 2, "€", "euro"),
      jvmResults = CurrencyTestResults("EUR", 978, 2, "€", "Euro")
    ),
    LocaleCurrencyTest(
      Locale.JAPAN,
      jsResults  = CurrencyTestResults("JPY", 392, 0, "￥", "日本円"),
      jvmResults = CurrencyTestResults("JPY", 392, 0, "￥", "日本円")
    ),
    LocaleCurrencyTest(
      Locale.KOREA,
      jsResults  = CurrencyTestResults("KRW", 410, 0, "₩", "대한민국 원"),
      jvmResults = CurrencyTestResults("KRW", 410, 0, "₩", "대한민국 원")
    ),
    LocaleCurrencyTest(
      Locale.PRC,
      jsResults  = CurrencyTestResults("CNY", 156, 2, "¥", "人民币"),
      jvmResults = CurrencyTestResults("CNY", 156, 2, "￥", "人民币")
    ),
    LocaleCurrencyTest(
      Locale.TAIWAN,
      jsResults  = CurrencyTestResults("TWD", 901, 2, "$", "新台幣"),
      jvmResults = CurrencyTestResults("TWD", 901, 2, "NT$", "新臺幣")
    ),
    LocaleCurrencyTest(
      Locale.UK,
      jsResults  = CurrencyTestResults("GBP", 826, 2, "£", "British Pound"),
      jvmResults = CurrencyTestResults("GBP", 826, 2, "£", "British Pound Sterling")
    ),
    LocaleCurrencyTest(
      Locale.US,
      jsResults  = CurrencyTestResults("USD", 840, 2, "$", "US Dollar"),
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
          jsResults    = CurrencyTestResults("CAD", 124, 2, "CA$", "Canadian Dollar"),
          jvmResults   = CurrencyTestResults("CAD", 124, 2, "CA$", "Canadian Dollar")
        ),
        CodeCurrencyTest(
          currencyCode = "EUR",
          jsResults    = CurrencyTestResults("EUR", 978, 2, "€", "Euro"),
          jvmResults   = CurrencyTestResults("EUR", 978, 2, "€", "Euro")
        ),
        CodeCurrencyTest(
          currencyCode = "JPY",
          jsResults    = CurrencyTestResults("JPY", 392, 0, "¥", "Japanese Yen"),
          jvmResults   = CurrencyTestResults("JPY", 392, 0, "¥", "Japanese Yen")
        ),
        CodeCurrencyTest(
          currencyCode = "KRW",
          jsResults    = CurrencyTestResults("KRW", 410, 0, "₩", "South Korean Won"),
          jvmResults   = CurrencyTestResults("KRW", 410, 0, "₩", "South Korean Won")
        ),
        CodeCurrencyTest(
          currencyCode = "CNY",
          jsResults    = CurrencyTestResults("CNY", 156, 2, "CN¥", "Chinese Yuan"),
          jvmResults   = CurrencyTestResults("CNY", 156, 2, "CN¥", "Chinese Yuan")
        ),
        CodeCurrencyTest(
          currencyCode = "TWD",
          jsResults    = CurrencyTestResults("TWD", 901, 2, "NT$", "New Taiwan Dollar"),
          jvmResults   = CurrencyTestResults("TWD", 901, 2, "NT$", "New Taiwan Dollar")
        ),
        CodeCurrencyTest(
          currencyCode = "GBP",
          jsResults    = CurrencyTestResults("GBP", 826, 2, "£", "British Pound"),
          jvmResults   = CurrencyTestResults("GBP", 826, 2, "£", "British Pound Sterling")
        ),
        CodeCurrencyTest(
          currencyCode = "USD",
          jsResults    = CurrencyTestResults("USD", 840, 2, "$", "US Dollar"),
          jvmResults   = CurrencyTestResults("USD", 840, 2, "$", "US Dollar")
        )
      )
    ),
    DefaultLocaleCurrencyTest(
      defaultLocale = Locale.GERMANY,
      tests = Seq(
        CodeCurrencyTest(
          currencyCode = "CAD",
          jsResults    = CurrencyTestResults("CAD", 124, 2, "CA$", "Kanadischer Dollar"),
          jvmResults   = CurrencyTestResults("CAD", 124, 2, "CA$", "Kanadischer Dollar")
        ),
        CodeCurrencyTest(
          currencyCode = "EUR",
          jsResults    = CurrencyTestResults("EUR", 978, 2, "€", "Euro"),
          jvmResults   = CurrencyTestResults("EUR", 978, 2, "€", "Euro")
        ),
        CodeCurrencyTest(
          currencyCode = "JPY",
          jsResults    = CurrencyTestResults("JPY", 392, 0, "¥", "Japanischer Yen"),
          jvmResults   = CurrencyTestResults("JPY", 392, 0, "¥", "Japanische Yen")
        ),
        CodeCurrencyTest(
          currencyCode = "KRW",
          jsResults    = CurrencyTestResults("KRW", 410, 0, "₩", "Südkoreanischer Won"),
          jvmResults   = CurrencyTestResults("KRW", 410, 0, "₩", "Südkoreanischer Won")
        ),
        CodeCurrencyTest(
          currencyCode = "CNY",
          jsResults    = CurrencyTestResults("CNY", 156, 2, "CN¥", "Renminbi Yuan"),
          jvmResults   = CurrencyTestResults("CNY", 156, 2, "CN¥", "Renminbi Yuan")
        ),
        CodeCurrencyTest(
          currencyCode = "TWD",
          jsResults    = CurrencyTestResults("TWD", 901, 2, "NT$", "Neuer Taiwan-Dollar"),
          jvmResults   = CurrencyTestResults("TWD", 901, 2, "NT$", "Neuer Taiwan-Dollar")
        ),
        CodeCurrencyTest(
          currencyCode = "GBP",
          jsResults    = CurrencyTestResults("GBP", 826, 2, "£", "Britisches Pfund"),
          jvmResults   = CurrencyTestResults("GBP", 826, 2, "£", "Pfund Sterling")
        ),
        CodeCurrencyTest(
          currencyCode = "USD",
          jsResults    = CurrencyTestResults("USD", 840, 2, "$", "US-Dollar"),
          jvmResults   = CurrencyTestResults("USD", 840, 2, "$", "US-Dollar")
        )
      )
    ),
    DefaultLocaleCurrencyTest(
      defaultLocale = Locale.CHINA,
      tests = Seq(
        CodeCurrencyTest(
          currencyCode = "CAD",
          jsResults    = CurrencyTestResults("CAD", 124, 2, "CA$", "加拿大元"),
          jvmResults   = CurrencyTestResults("CAD", 124, 2, "CA$", "加拿大元")
        ),
        CodeCurrencyTest(
          currencyCode = "EUR",
          jsResults    = CurrencyTestResults("EUR", 978, 2, "€", "欧元"),
          jvmResults   = CurrencyTestResults("EUR", 978, 2, "€", "欧元")
        ),
        CodeCurrencyTest(
          currencyCode = "JPY",
          jsResults    = CurrencyTestResults("JPY", 392, 0, "JP¥", "日元"),
          jvmResults   = CurrencyTestResults("JPY", 392, 0, "JP¥", "日元")
        ),
        CodeCurrencyTest(
          currencyCode = "KRW",
          jsResults    = CurrencyTestResults("KRW", 410, 0, "￦", "韩元"),
          jvmResults   = CurrencyTestResults("KRW", 410, 0, "￦", "韩圆")
        ),
        CodeCurrencyTest(
          currencyCode = "CNY",
          jsResults    = CurrencyTestResults("CNY", 156, 2, "¥", "人民币"),
          jvmResults   = CurrencyTestResults("CNY", 156, 2, "￥", "人民币")
        ),
        CodeCurrencyTest(
          currencyCode = "TWD",
          jsResults    = CurrencyTestResults("TWD", 901, 2, "NT$", "新台币"),
          jvmResults   = CurrencyTestResults("TWD", 901, 2, "NT$", "新台币")
        ),
        CodeCurrencyTest(
          currencyCode = "GBP",
          jsResults    = CurrencyTestResults("GBP", 826, 2, "£", "英镑"),
          jvmResults   = CurrencyTestResults("GBP", 826, 2, "£", "英镑")
        ),
        CodeCurrencyTest(
          currencyCode = "USD",
          jsResults    = CurrencyTestResults("USD", 840, 2, "US$", "美元"),
          jvmResults   = CurrencyTestResults("USD", 840, 2, "US$", "美元")
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
    localeCurrencyTests.foreach { test: LocaleCurrencyTest =>
      // Even when you get a currency for a specific locale, description calls (getSymbol and getDisplayName)
      // return strings will be based upon the default locale, so lets set the default locale to the test locale
      Locale.setDefault(test.locale)
      val localeCurrency = Currency.getInstance(test.locale)
      testCurrency(localeCurrency, f(test))
    }

    // Set a default locale, then lookup multiple currencies by code and test results
    defaultLocaleCurrencyTests.foreach { defaultTest: DefaultLocaleCurrencyTest =>
      Locale.setDefault(defaultTest.defaultLocale)
      defaultTest.tests.foreach { test: CodeCurrencyTest =>
        val codeCurrency = Currency.getInstance(test.currencyCode)
        testCurrency(codeCurrency, f(test))
      }
    }
  }
}
