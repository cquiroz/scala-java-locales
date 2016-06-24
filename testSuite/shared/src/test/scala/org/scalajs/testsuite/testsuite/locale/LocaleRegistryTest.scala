package org.scalajs.testsuite.testsuite.locale

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

  case class LocaleTestCase(ldml: LDML, tag: String, tostring: String, toLanguageTag: String,
      hasExtensions: Boolean)

  // Test some locales and their numeric systems
  val testData = List(
    LocaleTestCase(am, "am", "am", "am", false),
    LocaleTestCase(ar_001, "ar_001", "", "und", false),
    LocaleTestCase(zh_Hant_TW, "zh_TW", "", "und", false))

  @Test def test_install(): Unit = {
    if (!Platform.executingInJVM) testData.foreach { l => LocaleRegistry.installLocale(l.ldml) }

    testData.foreach { ltc =>
      val locale = Locale.forLanguageTag(ltc.tag)
      assertEquals(ltc.tostring, locale.toString)
      assertEquals(ltc.toLanguageTag, locale.toLanguageTag)
      assertEquals(ltc.hasExtensions, locale.hasExtensions)
    }
  }

  @Test def test_available_locales(): Unit = {
    val originalLength = Locale.getAvailableLocales.length
    if (!Platform.executingInJVM) testData.foreach { l => LocaleRegistry.installLocale(l.ldml) }
    if (!Platform.executingInJVM) assertEquals(originalLength + testData.length, Locale.getAvailableLocales.length)
  }
}
