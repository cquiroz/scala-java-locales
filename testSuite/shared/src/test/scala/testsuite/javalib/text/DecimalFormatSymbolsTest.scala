package testsuite.javalib.text

import java.text.DecimalFormatSymbols
import java.util.Locale
import testsuite.utils.Platform
import testsuite.utils.AssertThrows.expectThrows

class DecimalFormatSymbolsTest extends munit.FunSuite {

  case class LocaleTestItem(tag: String, cldr21: Boolean = false)

  val englishSymbols = List("0", ".", ",", "‰", "%", "#", ";", "∞", "NaN", "-", "E")

  val standardLocalesData = List(
    Locale.ROOT ->
      List("0", ".", ",", "‰", "%", "#", ";", "∞", "NaN", "-", "E"),
    Locale.ENGLISH -> englishSymbols,
    Locale.GERMAN ->
      List("0", ",", ".", "‰", "%", "#", ";", "∞", "NaN", "-", "E"),
    Locale.ITALIAN ->
      List("0", ",", ".", "‰", "%", "#", ";", "∞", "NaN", "-", "E"),
    Locale.KOREAN ->
      List("0", ".", ",", "‰", "%", "#", ";", "∞", "NaN", "-", "E"),
    Locale.CHINESE ->
      List("0", ".", ",", "‰", "%", "#", ";", "∞", "NaN", "-", "E"),
    Locale.SIMPLIFIED_CHINESE ->
      List("0", ".", ",", "‰", "%", "#", ";", "∞", "NaN", "-", "E"),
    Locale.TRADITIONAL_CHINESE ->
      List("0", ".", ",", "‰", "%", "#", ";", "∞", "非數值", "-", "E"),
    Locale.GERMANY ->
      List("0", ",", ".", "‰", "%", "#", ";", "∞", "NaN", "-", "E"),
    Locale.ITALY ->
      List("0", ",", ".", "‰", "%", "#", ";", "∞", "NaN", "-", "E"),
    Locale.KOREA ->
      List("0", ".", ",", "‰", "%", "#", ";", "∞", "NaN", "-", "E"),
    Locale.CHINA ->
      List("0", ".", ",", "‰", "%", "#", ";", "∞", "NaN", "-", "E"),
    Locale.PRC ->
      List("0", ".", ",", "‰", "%", "#", ";", "∞", "NaN", "-", "E"),
    Locale.TAIWAN ->
      List("0", ".", ",", "‰", "%", "#", ";", "∞", "非數值", "-", "E"),
    Locale.UK ->
      List("0", ".", ",", "‰", "%", "#", ";", "∞", "NaN", "-", "E"),
    Locale.US ->
      List("0", ".", ",", "‰", "%", "#", ";", "∞", "NaN", "-", "E"),
    Locale.CANADA_FRENCH ->
      List("0", ",", "\u00A0", "‰", "%", "#", ";", "∞", "NaN", "-", "E")
  )

  val extraLocalesData = List(
    // af uses latn
    LocaleTestItem("af") ->
      List("0", ",", "\u00A0", "‰", "%", "#", ";", "∞", "NaN", "-", "E"),
    LocaleTestItem("az") ->
      List("0", ",", ".", "‰", "%", "#", ";", "∞", "NaN", "-", "E"),
    LocaleTestItem("az-Cyrl") ->
      List("0", ",", ".", "‰", "%", "#", ";", "∞", "NaN", "-", "E"),
    // bn has a default ns but it is a latn alias
    LocaleTestItem("bn") ->
      List("0", ".", ",", "‰", "%", "#", ";", "∞", "NaN", "-", "E"),
    LocaleTestItem("es-CL") ->
      List("0", ",", ".", "‰", "%", "#", ";", "∞", "NaN", "-", "E"),
    LocaleTestItem("zh") ->
      List("0", ".", ",", "‰", "%", "#", ";", "∞", "NaN", "-", "E"),
    LocaleTestItem("zh-Hant") ->
      List("0", ".", ",", "‰", "%", "#", ";", "∞", "非數值", "-", "E")
  )

  // These locales show differences with Java due to a different CLDR version
  val localesDiff = List(
    // ar has a default arab set of symbols
    LocaleTestItem("ar", cldr21 = true) ->
      List("٠", "٫", "٬", "؉", "٪", "#", "؛", "∞", "ليس رقم", "\u002D", "اس"), // JVM
    LocaleTestItem("ar") ->
      List("0", ".", ",", "‰", "%", "#", ";", "∞", "ليس رقمًا", "-", "E"), // JS
    LocaleTestItem("fr", cldr21 = true) ->
      List("0", ",", "\u00A0", "‰", "%", "#", ";", "∞", "NaN", "-", "E"),
    LocaleTestItem("fr", cldr21 = false) ->
      List("0", ",", "\u202F", "‰", "%", "#", ";", "∞", "NaN", "-", "E"),
    LocaleTestItem("it-CH", cldr21 = true) ->
      List("0", ".", "'", "‰", "%", "#", ";", "∞", "NaN", "-", "E"),
    LocaleTestItem("it-CH") ->
      List("0", ".", "’", "‰", "%", "#", ";", "∞", "NaN", "-", "E"),
    // fa uses arabext
    LocaleTestItem("fa", cldr21 = true) ->
      List("۰", "٫", "٬", "؉", "٪", "#", "؛", "∞", "NaN", "-", "×۱۰^"), // JVM
    LocaleTestItem("fa") ->
      List("۰", "٫", "٬", "؉", "٪", "#", "؛", "∞", "ناعدد", "−", "×۱۰^"), // JS
    LocaleTestItem("fi-FI", cldr21 = true) ->
      List("0", ",", "\u00A0", "‰", "%", "#", ";", "∞", "epäluku", "-", "E"),
    LocaleTestItem("fi-FI", cldr21 = false) ->
      List("0", ",", "\u00A0", "‰", "%", "#", ";", "∞", "epäluku", "−", "E"),
    LocaleTestItem("ja", cldr21 = true) ->
      List("0", ".", ",", "‰", "%", "#", ";", "∞", "NaN（非数）", "-", "E"), // JVM
    LocaleTestItem("ja") ->
      List("0", ".", ",", "‰", "%", "#", ";", "∞", "NaN", "-", "E"), // JS
    LocaleTestItem("ka", cldr21 = true) ->
      List("0", ",", ".", "‰", "%", "#", ";", "∞", "NaN", "-", "E"), // JVM
    LocaleTestItem("ka") ->
      List("0", ",", "\u00A0", "‰", "%", "#", ";", "∞", "არ არის რიცხვი", "-", "E"), // JS
    LocaleTestItem("lv", cldr21 = true) ->
      List("0", ",", "\u00A0", "‰", "%", "#", ";", "∞", "nav skaitlis", "\u2212", "E"), // JVM
    LocaleTestItem("lv") ->
      List("0", ",", "\u00A0", "‰", "%", "#", ";", "∞", "NS", "-", "E"), // JS
    LocaleTestItem("my", cldr21 = true) ->
      List("၀", ".", ",", "‰", "%", "#", "၊", "∞", "NaN", "-", "E"), // JVM
    LocaleTestItem("my") ->
      List("0", ".", ",", "‰", "%", "#", ";", "∞", "ဂဏန်းမဟုတ်သော", "-", "E"), // JS
    LocaleTestItem("smn", cldr21 = true) ->
      List("0", ".", ",", "‰", "%", "#", ";", "∞", "NaN", "-", "E"), // JVM
    LocaleTestItem("smn") ->
      List("0", ",", "\u00A0", "‰", "%", "#", ";", "∞", "epiloho", "-", "E"), // JS
    LocaleTestItem("smn-FI", cldr21 = true) ->
      List("0", ".", ",", "‰", "%", "#", ";", "∞", "NaN", "-", "E"),
    LocaleTestItem("smn-FI") ->
      List("0", ",", "\u00A0", "‰", "%", "#", ";", "∞", "epiloho", "-", "E"),
    LocaleTestItem("ru-RU", cldr21 = true) ->
      List("0", ",", "\u00A0", "‰", "%", "#", ";", "∞", "не число", "-", "E"),
    LocaleTestItem("ru-RU") ->
      List("0", ",", "\u00A0", "‰", "%", "#", ";", "∞", "не число", "-", "E"),
    LocaleTestItem("ca", cldr21 = true) ->
      List("0", ",", ".", "‰", "%", "#", ";", "∞", "NaN", "-", "E"),
    LocaleTestItem("ca") ->
      List("0", ",", ".", "‰", "%", "#", ";", "∞", "NaN", "-", "E")
  )

  def test_dfs(dfs: DecimalFormatSymbols, symbols: List[String]): Unit = {
    assertEquals(symbols(0).charAt(0), dfs.getZeroDigit)
    assertEquals(symbols(1).charAt(0), dfs.getDecimalSeparator)
    assertEquals(symbols(2).charAt(0), dfs.getGroupingSeparator)
    assertEquals(symbols(3).charAt(0), dfs.getPerMill)
    assertEquals(symbols(4).charAt(0), dfs.getPercent)
    assertEquals(symbols(5).charAt(0), dfs.getDigit)
    assertEquals(symbols(6).charAt(0), dfs.getPatternSeparator)
    assertEquals(symbols(7), dfs.getInfinity)
    assertEquals(symbols(8), dfs.getNaN)
    assertEquals(symbols(9).charAt(0), dfs.getMinusSign)
    assertEquals(symbols(10), dfs.getExponentSeparator)
  }

  test("default_locales_decimal_format_symbol") {
    standardLocalesData.foreach {
      case (l, symbols) =>
        val dfs = DecimalFormatSymbols.getInstance(l)
        test_dfs(dfs, symbols)
    }
  }

  test("extra_locales_decimal_format_symbol") {
    extraLocalesData.foreach {
      case (LocaleTestItem(tag, _), symbols) =>
        val l   = Locale.forLanguageTag(tag)
        val dfs = DecimalFormatSymbols.getInstance(l)
        test_dfs(dfs, symbols)
    }
  }

  // These tests give the same data on CLDR 21
  test("extra_locales_not_agreeing_decimal_format_symbol") {
    localesDiff.foreach {
      case (LocaleTestItem(tag, cldr21), symbols) =>
        val l   = Locale.forLanguageTag(tag)
        val dfs = DecimalFormatSymbols.getInstance(l)
        if (Platform.executingInJVM && cldr21) {
          test_dfs(dfs, symbols)
        }
        if (!Platform.executingInJVM && !cldr21) {
          test_dfs(dfs, symbols)
        }
    }
  }

  test("available_locales") {
    val initial = DecimalFormatSymbols.getAvailableLocales.length
    assert(initial > 0)
  }

  test("defaults") {
    val dfs = new DecimalFormatSymbols()
    test_dfs(dfs, englishSymbols)
  }

  test("setters") {
    val dfs = new DecimalFormatSymbols()
    dfs.setZeroDigit('1')
    assertEquals('1', dfs.getZeroDigit)
    dfs.setGroupingSeparator('1')
    assertEquals('1', dfs.getGroupingSeparator)
    dfs.setDecimalSeparator('1')
    assertEquals('1', dfs.getDecimalSeparator)
    dfs.setPerMill('1')
    assertEquals('1', dfs.getPerMill)
    dfs.setPercent('1')
    assertEquals('1', dfs.getPercent)
    dfs.setDigit('1')
    assertEquals('1', dfs.getDigit)
    dfs.setPatternSeparator('1')
    assertEquals('1', dfs.getPatternSeparator)
    dfs.setMinusSign('1')
    assertEquals('1', dfs.getMinusSign)

    dfs.setInfinity(null)
    assert(null == dfs.getInfinity)
    dfs.setInfinity("Inf")
    assertEquals("Inf", dfs.getInfinity)

    dfs.setNaN(null)
    assert(null == dfs.getNaN)
    dfs.setNaN("nan")
    assertEquals("nan", dfs.getNaN)

    expectThrows(classOf[NullPointerException], dfs.setExponentSeparator(null))
    dfs.setExponentSeparator("exp")
    assertEquals("exp", dfs.getExponentSeparator)
  }

  test("clone") {
    val dfs = new DecimalFormatSymbols()
    assertEquals(dfs, dfs.clone().asInstanceOf[DecimalFormatSymbols])
    assert(dfs ne dfs.clone())
  }

  test("equals") {
    val dfs = new DecimalFormatSymbols()
    assertEquals(dfs, dfs)
    assert(dfs eq dfs)
    assert(!dfs.equals(null))
    assert(!dfs.equals(1))
    val dfs2 = new DecimalFormatSymbols()
    assertEquals(dfs, dfs2)
    assert(dfs ne dfs2)
    dfs2.setDigit('i')
    assert(!dfs.equals(dfs2))
  }

  test("bad_tag_matches_root_dfs") {
    val l   = Locale.forLanguageTag("no_NO")
    val dfs = DecimalFormatSymbols.getInstance(l)
    standardLocalesData.foreach {
      case (Locale.ROOT, symbols) =>
        test_dfs(dfs, symbols)
      case (_, _) =>
    }
  }

  // test("hash_code") {
  //   val dfs = new DecimalFormatSymbols()
  //   assertEquals(dfs.hashCode, dfs.hashCode)
  //   val dfs2 = new DecimalFormatSymbols()
  //   assertEquals(dfs.hashCode, dfs2.hashCode)
  //   dfs2.setExponentSeparator("abc")
  //   // These tests should fail but they pass on the JVM
  //   assertEquals(dfs.hashCode, dfs2.hashCode)
  //   standardLocalesData.filter(_._1 != Locale.ROOT).foreach {
  //     case (l, symbols) =>
  //       val df = DecimalFormatSymbols.getInstance(l)
  //       assert(!dfs.hashCode.equals(df.hashCode))
  //   }
  // }

}
