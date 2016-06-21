package org.scalajs.testsuite.testsuite.locale

import java.text.DecimalFormatSymbols
import java.util.Locale

import org.junit.Assert._
import org.junit.{Before, Test}
import org.scalajs.testsuite.utils.LocaleTestSetup
import org.scalajs.testsuite.utils.Platform

import scala.scalajs.LocaleRegistry
import scala.scalajs.locale.ldml.LDML
import scala.scalajs.locale.ldml.data.all._

class LocaleRegistryTest extends LocaleTestSetup {
  // Clean up the locale database, there are different implementations for
  // the JVM and JS
  @Before def cleanup: Unit = super.cleanDatabase

  def test_dfs(l: Locale, symbols: List[String]): Unit = {
    val dfs = DecimalFormatSymbols.getInstance(l)

    assertEquals(symbols(0).charAt(0), dfs.getZeroDigit)
    assertEquals(symbols(1).charAt(0), dfs.getGroupingSeparator)
    assertEquals(symbols(2).charAt(0), dfs.getPerMill)
    assertEquals(symbols(3).charAt(0), dfs.getPercent)
    assertEquals(symbols(4).charAt(0), dfs.getDigit)
    assertEquals(symbols(5).charAt(0), dfs.getPatternSeparator)
    assertEquals(symbols(6), dfs.getInfinity)
    assertEquals(symbols(7), dfs.getNaN)
    assertEquals(symbols(8).charAt(0), dfs.getMinusSign)
  }

  case class LocaleTestCase(ldml: LDML, tag: String, tostring: String, toLanguageTag: String,
      hasExtensions: Boolean, symbols: List[String])

  // Test some locales and their numeric systems
  val testData = List(
    LocaleTestCase(am, "am", "am", "am", false, List("0", ",", "‰", "%", "#", ";", "∞", "NaN", "-")),
    LocaleTestCase(ar_001, "ar_001", "", "und", false, List("0", ",", "‰", "%", "#", ";", "∞", "NaN", "-")),
    LocaleTestCase(zh_Hant_TW, "zh_TW", "", "und", false, List("0", ",", "‰", "%", "#", ";", "∞", "NaN", "-")))

  @Test def test_install(): Unit = {
    if (!Platform.executingInJVM) testData.foreach { l => LocaleRegistry.installLocale(l.ldml) }

    testData.foreach { ltc =>
      val locale = Locale.forLanguageTag(ltc.tag)
      assertEquals(ltc.tostring, locale.toString)
      assertEquals(ltc.toLanguageTag, locale.toLanguageTag)
      assertEquals(ltc.hasExtensions, locale.hasExtensions)

      // check DecimalFormatSymbols, this fails on JS unless the locale is
      // registered previously
      test_dfs(locale, ltc.symbols)
    }
  }
}
