package testsuite.javalib.util

import java.util.{Currency, Locale}
import org.junit.Assert._

// Shared test data for JVM/JS so it's not duplicated
trait CurrencyTest {
  case class CurrencyTestResults(
    expectedCurrencyCode: String,
    expectedNumericCode: Int,
    expectedFractionDigits: Int,
    expectedSymbol: String,
    expectedDisplayName: String
  )

  // Have separate js vs jvm results to allow for CLDR differences
  case class CurrencyTest(
    testByLocale: Locale,
    testByLanguageTag: String,
    jsResults: CurrencyTestResults,
    jvmResults: CurrencyTestResults
  )

  case class DefaultLocaleCurrencyTests(defaultLocale: Locale, tests: Seq[CurrencyTest])

  private val US_standardCountryLocales = DefaultLocaleCurrencyTests(Locale.US, Seq(
    CurrencyTest(
      testByLocale = Locale.CANADA,
      testByLanguageTag = "en-CA",
      jsResults  = CurrencyTestResults("CAD", 124, 2, "CAD", "Canadian Dollar"),
      jvmResults = CurrencyTestResults("CAD", 124, 2, "CA$", "Canadian Dollar")
    ),
    CurrencyTest(
      testByLocale = Locale.CANADA_FRENCH,
      testByLanguageTag = "fr-CA",
      jsResults  = CurrencyTestResults("CAD", 124, 2, "CAD", "Canadian Dollar"),
      jvmResults = CurrencyTestResults("CAD", 124, 2, "CA$", "Canadian Dollar")
    ),
    CurrencyTest(
      testByLocale = Locale.CHINA,
      testByLanguageTag = "zh-CN",
      jsResults  = CurrencyTestResults("CNY", 156, 2, "CNY", "Chinese Yuan"),
      jvmResults = CurrencyTestResults("CNY", 156, 2, "CN¥", "Chinese Yuan")
    ),
    CurrencyTest(
      testByLocale = Locale.FRANCE,
      testByLanguageTag = "fr-FR",
      jsResults  = CurrencyTestResults("EUR", 978, 2, "EUR", "Euro"),
      jvmResults = CurrencyTestResults("EUR", 978, 2, "€", "Euro")
    ),
    CurrencyTest(
      testByLocale = Locale.GERMANY,
      testByLanguageTag = "de-DE",
      jsResults  = CurrencyTestResults("EUR", 978, 2, "EUR", "Euro"),
      jvmResults = CurrencyTestResults("EUR", 978, 2, "€", "Euro")
    ),
    CurrencyTest(
      testByLocale = Locale.ITALY,
      testByLanguageTag = "it-IT",
      jsResults  = CurrencyTestResults("EUR", 978, 2, "EUR", "Euro"),
      jvmResults = CurrencyTestResults("EUR", 978, 2, "€", "Euro")
    ),
    CurrencyTest(
      testByLocale = Locale.JAPAN,
      testByLanguageTag = "ja-JP",
      jsResults  = CurrencyTestResults("JPY", 392, 0, "JPY", "Japanese Yen"),
      jvmResults = CurrencyTestResults("JPY", 392, 0, "¥", "Japanese Yen")
    ),
    CurrencyTest(
      testByLocale = Locale.KOREA,
      testByLanguageTag = "ko-KR",
      jsResults  = CurrencyTestResults("KRW", 410, 0, "KRW", "South Korean Won"),
      jvmResults = CurrencyTestResults("KRW", 410, 0, "₩", "South Korean Won")
    ),
    CurrencyTest(
      testByLocale = Locale.PRC,
      testByLanguageTag = "zh-CN",
      jsResults  = CurrencyTestResults("CNY", 156, 2, "CNY", "Chinese Yuan"),
      jvmResults = CurrencyTestResults("CNY", 156, 2, "CN¥", "Chinese Yuan")
    ),
    CurrencyTest(
      testByLocale = Locale.TAIWAN,
      testByLanguageTag = "zh-TW",
      jsResults  = CurrencyTestResults("TWD", 901, 2, "TWD", "New Taiwan Dollar"),
      jvmResults = CurrencyTestResults("TWD", 901, 2, "NT$", "New Taiwan Dollar")
    ),
    CurrencyTest(
      testByLocale = Locale.UK,
      testByLanguageTag = "en-GB",
      jsResults  = CurrencyTestResults("GBP", 826, 2, "GBP", "British Pound"),
      jvmResults = CurrencyTestResults("GBP", 826, 2, "£", "British Pound Sterling")
    ),
    CurrencyTest(
      testByLocale = Locale.US,
      testByLanguageTag = "en-US",
      jsResults  = CurrencyTestResults("USD", 840, 2, "$", "US Dollar"),
      jvmResults = CurrencyTestResults("USD", 840, 2, "$", "US Dollar")
    )
  ))

  private val GERMANY_standardCountryLocales = DefaultLocaleCurrencyTests(Locale.GERMANY, Seq(
    CurrencyTest(
      testByLocale = Locale.CANADA,
      testByLanguageTag = "en-CA",
      jsResults  = CurrencyTestResults("CAD", 124, 2, "CAD", "Kanadischer Dollar"),
      jvmResults = CurrencyTestResults("CAD", 124, 2, "CA$", "Kanadischer Dollar")
    ),
    CurrencyTest(
      testByLocale = Locale.CANADA_FRENCH,
      testByLanguageTag = "fr-CA",
      jsResults  = CurrencyTestResults("CAD", 124, 2, "CAD", "Kanadischer Dollar"),
      jvmResults = CurrencyTestResults("CAD", 124, 2, "CA$", "Kanadischer Dollar")
    ),
    CurrencyTest(
      testByLocale = Locale.CHINA,
      testByLanguageTag = "zh-CN",
      jsResults  = CurrencyTestResults("CNY", 156, 2, "CNY", "Renminbi Yuan"),
      jvmResults = CurrencyTestResults("CNY", 156, 2, "CN¥", "Renminbi Yuan")
    ),
    CurrencyTest(
      testByLocale = Locale.FRANCE,
      testByLanguageTag = "fr-FR",
      jsResults  = CurrencyTestResults("EUR", 978, 2, "€", "Euro"),
      jvmResults = CurrencyTestResults("EUR", 978, 2, "€", "Euro")
    ),
    CurrencyTest(
      testByLocale = Locale.GERMANY,
      testByLanguageTag = "de-DE",
      jsResults  = CurrencyTestResults("EUR", 978, 2, "€", "Euro"),
      jvmResults = CurrencyTestResults("EUR", 978, 2, "€", "Euro")
    ),
    CurrencyTest(
      testByLocale = Locale.ITALY,
      testByLanguageTag = "it-IT",
      jsResults  = CurrencyTestResults("EUR", 978, 2, "€", "Euro"),
      jvmResults = CurrencyTestResults("EUR", 978, 2, "€", "Euro")
    ),
    CurrencyTest(
      testByLocale = Locale.JAPAN,
      testByLanguageTag = "ja-JP",
      jsResults  = CurrencyTestResults("JPY", 392, 0, "JPY", "Japanischer Yen"),
      jvmResults = CurrencyTestResults("JPY", 392, 0, "¥", "Japanische Yen")
    ),
    CurrencyTest(
      testByLocale = Locale.KOREA,
      testByLanguageTag = "ko-KR",
      jsResults  = CurrencyTestResults("KRW", 410, 0, "KRW", "Südkoreanischer Won"),
      jvmResults = CurrencyTestResults("KRW", 410, 0, "₩", "Südkoreanischer Won")
    ),
    CurrencyTest(
      testByLocale = Locale.PRC,
      testByLanguageTag = "zh-CN",
      jsResults  = CurrencyTestResults("CNY", 156, 2, "CNY", "Renminbi Yuan"),
      jvmResults = CurrencyTestResults("CNY", 156, 2, "CN¥", "Renminbi Yuan")
    ),
    CurrencyTest(
      testByLocale = Locale.TAIWAN,
      testByLanguageTag = "zh-TW",
      jsResults  = CurrencyTestResults("TWD", 901, 2, "TWD", "Neuer Taiwan-Dollar"),
      jvmResults = CurrencyTestResults("TWD", 901, 2, "NT$", "Neuer Taiwan-Dollar")
    ),
    CurrencyTest(
      testByLocale = Locale.UK,
      testByLanguageTag = "en-GB",
      jsResults  = CurrencyTestResults("GBP", 826, 2, "GBP", "Britisches Pfund"),
      jvmResults = CurrencyTestResults("GBP", 826, 2, "£", "Pfund Sterling")
    ),
    CurrencyTest(
      testByLocale = Locale.US,
      testByLanguageTag = "en-US",
      jsResults  = CurrencyTestResults("USD", 840, 2, "USD", "US-Dollar"),
      jvmResults = CurrencyTestResults("USD", 840, 2, "$", "US-Dollar")
    )
  ))


  private val CHINA_standardCountryLocales = DefaultLocaleCurrencyTests(Locale.CHINA, Seq(
    CurrencyTest(
      testByLocale = Locale.CANADA,
      testByLanguageTag = "en-CA",
      jsResults  = CurrencyTestResults("CAD", 124, 2, "CAD", "加拿大元"),
      jvmResults = CurrencyTestResults("CAD", 124, 2, "CA$", "加拿大元")
    ),
    CurrencyTest(
      testByLocale = Locale.CANADA_FRENCH,
      testByLanguageTag = "fr-CA",
      jsResults  = CurrencyTestResults("CAD", 124, 2, "CAD", "加拿大元"),
      jvmResults = CurrencyTestResults("CAD", 124, 2, "CA$", "加拿大元")
    ),
    CurrencyTest(
      testByLocale = Locale.CHINA,
      testByLanguageTag = "zh-CN",
      jsResults  = CurrencyTestResults("CNY", 156, 2, "\u00A5" /* narrow width yen ¥ */ , "人民币"),
      jvmResults = CurrencyTestResults("CNY", 156, 2, "￥", "人民币")
    ),
    CurrencyTest(
      testByLocale = Locale.FRANCE,
      testByLanguageTag = "fr-FR",
      jsResults  = CurrencyTestResults("EUR", 978, 2, "EUR", "欧元"),
      jvmResults = CurrencyTestResults("EUR", 978, 2, "€", "欧元")
    ),
    CurrencyTest(
      testByLocale = Locale.GERMANY,
      testByLanguageTag = "de-DE",
      jsResults  = CurrencyTestResults("EUR", 978, 2, "EUR", "欧元"),
      jvmResults = CurrencyTestResults("EUR", 978, 2, "€", "欧元")
    ),
    CurrencyTest(
      testByLocale = Locale.ITALY,
      testByLanguageTag = "it-IT",
      jsResults  = CurrencyTestResults("EUR", 978, 2, "EUR", "欧元"),
      jvmResults = CurrencyTestResults("EUR", 978, 2, "€", "欧元")
    ),
    CurrencyTest(
      testByLocale = Locale.JAPAN,
      testByLanguageTag = "ja-JP",
      jsResults  = CurrencyTestResults("JPY", 392, 0, "JPY", "日元"),
      jvmResults = CurrencyTestResults("JPY", 392, 0, "JP¥", "日元")
    ),
    CurrencyTest(
      testByLocale = Locale.KOREA,
      testByLanguageTag = "ko-KR",
      jsResults  = CurrencyTestResults("KRW", 410, 0, "KRW", "韩元"),
      jvmResults = CurrencyTestResults("KRW", 410, 0, "￦", "韩圆")
    ),
    CurrencyTest(
      testByLocale = Locale.PRC,
      testByLanguageTag = "zh-CN",
      jsResults  = CurrencyTestResults("CNY", 156, 2, "\u00A5"/* narrow width yen ¥ */, "人民币"),
      jvmResults = CurrencyTestResults("CNY", 156, 2, "￥", "人民币")
    ),
    CurrencyTest(
      testByLocale = Locale.TAIWAN,
      testByLanguageTag = "zh-TW",
      jsResults  = CurrencyTestResults("TWD", 901, 2, "TWD", "新台币"),
      jvmResults = CurrencyTestResults("TWD", 901, 2, "NT$", "新台币")
    ),
    CurrencyTest(
      testByLocale = Locale.UK,
      testByLanguageTag = "en-GB",
      jsResults  = CurrencyTestResults("GBP", 826, 2, "GBP", "英镑"),
      jvmResults = CurrencyTestResults("GBP", 826, 2, "£", "英镑")
    ),
    CurrencyTest(
      testByLocale = Locale.US,
      testByLanguageTag = "en-US",
      jsResults  = CurrencyTestResults("USD", 840, 2, "USD", "美元"),
      jvmResults = CurrencyTestResults("USD", 840, 2, "US$", "美元")
    )
  ))

  private val countryTests = Seq(
    US_standardCountryLocales,
    GERMANY_standardCountryLocales,
    CHINA_standardCountryLocales
  )

  private def testCurrency(currency: Currency, test: CurrencyTest, expectedResults: CurrencyTestResults): Unit = {
    import expectedResults._

    val currentTestMsg: String = s"currency: ${currency} and test: $test and expectedResutls: $expectedResults"
    assertEquals(currentTestMsg, expectedCurrencyCode, currency.getCurrencyCode)
    assertEquals(currentTestMsg, expectedNumericCode, currency.getNumericCode)
    assertEquals(currentTestMsg, expectedFractionDigits, currency.getDefaultFractionDigits)
    assertEquals(currentTestMsg, expectedSymbol, currency.getSymbol)
    assertEquals(currentTestMsg, expectedDisplayName, currency.getDisplayName)
  }

  protected def test_standard_locales(f: CurrencyTest => CurrencyTestResults): Unit = {
    countryTests.foreach{ regionTest: DefaultLocaleCurrencyTests =>
      Locale.setDefault(regionTest.defaultLocale)
      regionTest.tests.foreach{ test: CurrencyTest =>
        val localeCurrency = Currency.getInstance(test.testByLocale)
        testCurrency(localeCurrency, test, f(test))

        val langageTagCurrency = Currency.getInstance(Locale.forLanguageTag(test.testByLanguageTag))
        testCurrency(langageTagCurrency, test, f(test))
      }
    }
  }
}