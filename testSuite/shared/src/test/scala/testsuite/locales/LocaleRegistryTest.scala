package testsuite.locales

import java.util.Locale

import locales.LocaleRegistry
import org.junit.Assert._
import org.junit.{Before, Test}

import locales.cldr.LDML
import locales.cldr.data._

import testsuite.utils.Platform
import testsuite.utils.LocaleTestSetup

class LocaleRegistryTest extends LocaleTestSetup {
  // Clean up the locale database, there are different implementations for
  // the JVM and JS
  @Before def cleanup: Unit = super.cleanDatabase

  case class LocaleTestCase(ldml: LDML, tag: String, tostring: String,
      toLanguageTag: String, hasExtensions: Boolean, parent: Option[LDML])

  // Test some locales and their numeric systems
  val testData = List(
    LocaleTestCase(root, "", "", "und", hasExtensions = false, None),
    LocaleTestCase(am, "am", "am", "am", hasExtensions = false, Some(root)),
    LocaleTestCase(ar_001, "ar-001", "ar_001", "ar-001", hasExtensions = false, Some(ar)),
    LocaleTestCase(zh_Hant_TW, "zh-TW", "zh_TW", "zh-TW", hasExtensions = false, Some(zh_Hant)),
    // Locales with special roots
    LocaleTestCase(az_Cyrl, "az-Cyrl", "az__#Cyrl", "az-Cyrl", hasExtensions = false, Some(root)),
    LocaleTestCase(az_Cyrl_AZ, "az-AZ-Cyrl", "az_AZ", "az-AZ", hasExtensions = false, Some(az_Cyrl)),
     // es_CL parent is es_419 instead of es
    LocaleTestCase(es_CL, "es-CL", "es_CL", "es-CL", hasExtensions = false, Some(es_419)),
     // en_BS parent is en_001 instead of en
    LocaleTestCase(en_BS, "en-BS", "en_BS", "en-BS", hasExtensions = false, Some(en_001)),
     // en_SI parent is en_150 instead of en
    LocaleTestCase(en_SI, "en-SI", "en_SI", "en-SI", hasExtensions = false, Some(en_150)),
    LocaleTestCase(pt_MZ, "pt-MZ", "pt_MZ", "pt-MZ", hasExtensions = false, Some(pt_PT)),
    LocaleTestCase(zh_Hant_MO, "zh-Hant-MO", "zh_MO_#Hant", "zh-Hant-MO", hasExtensions = false, Some(zh_Hant_HK))
  )

  @Test def test_install(): Unit = {
    if (!Platform.executingInJVM) {
      testData.foreach { l =>
        LocaleRegistry.installLocale(l.ldml)
      }
    }

    testData.foreach { ltc =>
      val locale = Locale.forLanguageTag(ltc.tag)
      assertEquals(ltc.tostring, locale.toString)
      assertEquals(ltc.toLanguageTag, locale.toLanguageTag)
      assertEquals(ltc.hasExtensions, locale.hasExtensions)
    }
  }

  @Test def test_install_and_retrieve(): Unit = {
    if (!Platform.executingInJVM) testData.foreach { l =>
      LocaleRegistry.installLocale(l.ldml)
      assertEquals(Some(l.ldml), LocaleRegistry.ldml(l.ldml.toLocale))
      assertEquals(l.parent, LocaleRegistry.ldml(l.ldml.toLocale).flatMap(_.parent))
    }
  }

  @Test def test_available_locales(): Unit = {
    super.cleanDatabase
    val originalLength = Locale.getAvailableLocales.length
    if (!Platform.executingInJVM) {
      testData.foreach { l =>
        LocaleRegistry.installLocale(l.ldml)
      }
      assertEquals(originalLength + testData.length - 1, //root is already installed
        Locale.getAvailableLocales.length)
    }
  }
}
