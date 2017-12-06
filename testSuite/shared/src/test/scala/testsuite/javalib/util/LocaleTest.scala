package testsuite.javalib.util

import java.util.Locale

import org.junit.{Before, Test}
import org.junit.Assert._

import testsuite.utils.LocaleTestSetup
import testsuite.utils.Platform
import testsuite.utils.AssertThrows.expectThrows

class LocaleTest extends LocaleTestSetup {
  // Clean up the locale database, there are different implementations for
  // the JVM and JS
  @Before def cleanup: Unit = super.cleanDatabase

  @Test def test_null_constructor(): Unit = {
    expectThrows(classOf[NullPointerException], new Locale(null))
    expectThrows(classOf[NullPointerException], new Locale("", null))
    expectThrows(classOf[NullPointerException], new Locale("", "", null))
  }

  @Test def test_constructor(): Unit = {
    assertEquals("en", new Locale("en", "US").getLanguage)
    assertEquals("US", new Locale("en", "US").getCountry)
    assertEquals("POSIX", new Locale("en", "US", "POSIX").getVariant)

    // No syntactic checks
    assertEquals("abcdef", new Locale("ABCDEF", "longcountryname").getLanguage)
    assertEquals("LONGCOUNTRYNAME",
      new Locale("abcdef", "longcountryname").getCountry)
  }

  @Test def test_special_cases(): Unit = {
    assertEquals("ja", new Locale("ja", "JP", "JP").getLanguage)
    assertEquals("JP", new Locale("ja", "JP", "JP").getCountry)
    assertEquals("JP", new Locale("ja", "JP", "JP").getVariant)
    assertEquals("ca-japanese", new Locale("ja", "JP", "JP").getExtension('u'))

    assertEquals("th", new Locale("th", "TH", "TH").getLanguage)
    assertEquals("TH", new Locale("th", "TH", "TH").getCountry)
    assertEquals("TH", new Locale("th", "TH", "TH").getVariant)
    assertEquals("nu-thai", new Locale("th", "TH", "TH").getExtension('u'))
  }

  @Test def test_default_ENGLISH(): Unit = {
    assertEquals("en", Locale.forLanguageTag("en").getLanguage)
    assertEquals("", Locale.forLanguageTag("en").getCountry)
    assertEquals("", Locale.forLanguageTag("en").getVariant)
    assertEquals("", Locale.forLanguageTag("en").getScript)

    assertEquals(Locale.ENGLISH, Locale.forLanguageTag("en"))
  }

  @Test def test_default_FRENCH(): Unit = {
    assertEquals("fr", Locale.forLanguageTag("fr").getLanguage)
    assertEquals("", Locale.forLanguageTag("fr").getCountry)
    assertEquals("", Locale.forLanguageTag("fr").getVariant)
    assertEquals("", Locale.forLanguageTag("fr").getScript)

    assertEquals(Locale.FRENCH, Locale.forLanguageTag("fr"))
  }

  @Test def test_default_GERMAN(): Unit = {
    assertEquals("de", Locale.forLanguageTag("de").getLanguage)
    assertEquals("", Locale.forLanguageTag("de").getCountry)
    assertEquals("", Locale.forLanguageTag("de").getVariant)
    assertEquals("", Locale.forLanguageTag("de").getScript)

    assertEquals(Locale.GERMAN, Locale.forLanguageTag("de"))
  }

  @Test def test_default_ITALIAN(): Unit = {
    assertEquals("it", Locale.forLanguageTag("it").getLanguage)
    assertEquals("", Locale.forLanguageTag("it").getCountry)
    assertEquals("", Locale.forLanguageTag("it").getVariant)
    assertEquals("", Locale.forLanguageTag("it").getScript)

    assertEquals(Locale.ITALIAN, Locale.forLanguageTag("it"))
  }

  @Test def test_default_JAPANESE(): Unit = {
    assertEquals("ja", Locale.forLanguageTag("ja").getLanguage)
    assertEquals("", Locale.forLanguageTag("ja").getCountry)
    assertEquals("", Locale.forLanguageTag("ja").getVariant)
    assertEquals("", Locale.forLanguageTag("ja").getScript)

    assertEquals(Locale.JAPANESE, Locale.forLanguageTag("ja"))
  }

  @Test def test_default_KOREAN(): Unit = {
    assertEquals("ko", Locale.forLanguageTag("ko").getLanguage)
    assertEquals("", Locale.forLanguageTag("ko").getCountry)
    assertEquals("", Locale.forLanguageTag("ko").getVariant)
    assertEquals("", Locale.forLanguageTag("ko").getScript)

    assertEquals(Locale.KOREAN, Locale.forLanguageTag("ko"))
  }

  @Test def test_default_CHINESE(): Unit = {
    assertEquals("zh", Locale.forLanguageTag("zh").getLanguage)
    assertEquals("", Locale.forLanguageTag("zh").getCountry)
    assertEquals("", Locale.forLanguageTag("zh").getVariant)
    assertEquals("", Locale.forLanguageTag("zh").getScript)

    assertEquals(Locale.CHINESE, Locale.forLanguageTag("zh"))
  }

  @Test def test_default_SIMPLIFIED_CHINESE(): Unit = {
    assertEquals("zh", Locale.forLanguageTag("zh-CN").getLanguage)
    assertEquals("CN", Locale.forLanguageTag("zh-CN").getCountry)
    assertEquals("", Locale.forLanguageTag("zh-CN").getVariant)
    assertEquals("", Locale.forLanguageTag("zh-CN").getScript)

    assertEquals(Locale.SIMPLIFIED_CHINESE, Locale.forLanguageTag("zh-CN"))
  }

  @Test def test_default_FRANCE(): Unit = {
    assertEquals("fr", Locale.forLanguageTag("fr-FR").getLanguage)
    assertEquals("FR", Locale.forLanguageTag("fr-FR").getCountry)
    assertEquals("", Locale.forLanguageTag("fr-FR").getVariant)
    assertEquals("", Locale.forLanguageTag("fr-FR").getScript)

    assertEquals(Locale.FRANCE, Locale.forLanguageTag("fr-FR"))
  }

  @Test def test_default_GERMANY(): Unit = {
    assertEquals("de", Locale.forLanguageTag("de-DE").getLanguage)
    assertEquals("DE", Locale.forLanguageTag("de-DE").getCountry)
    assertEquals("", Locale.forLanguageTag("de-DE").getVariant)
    assertEquals("", Locale.forLanguageTag("de-DE").getScript)

    assertEquals(Locale.GERMANY, Locale.forLanguageTag("de-DE"))
  }

  @Test def test_default_ITALY(): Unit = {
    assertEquals("it", Locale.forLanguageTag("it-IT").getLanguage)
    assertEquals("IT", Locale.forLanguageTag("it-IT").getCountry)
    assertEquals("", Locale.forLanguageTag("it-IT").getVariant)
    assertEquals("", Locale.forLanguageTag("it-IT").getScript)

    assertEquals(Locale.ITALY, Locale.forLanguageTag("it-IT"))
  }
  @Test def test_default_JAPAN(): Unit = {
    assertEquals("ja", Locale.forLanguageTag("ja-JP").getLanguage)
    assertEquals("JP", Locale.forLanguageTag("ja-JP").getCountry)
    assertEquals("", Locale.forLanguageTag("ja-JP").getVariant)
    assertEquals("", Locale.forLanguageTag("ja-JP").getScript)

    assertEquals(Locale.JAPAN, Locale.forLanguageTag("ja-JP"))
  }

  @Test def test_default_KOREA(): Unit = {
    assertEquals("ko", Locale.forLanguageTag("ko-KR").getLanguage)
    assertEquals("KR", Locale.forLanguageTag("ko-KR").getCountry)
    assertEquals("", Locale.forLanguageTag("ko-KR").getVariant)
    assertEquals("", Locale.forLanguageTag("ko-KR").getScript)

    assertEquals(Locale.KOREA, Locale.forLanguageTag("ko-KR"))
  }

  @Test def test_default_CHINA(): Unit = {
    assertEquals("zh", Locale.forLanguageTag("zh-CN").getLanguage)
    assertEquals("CN", Locale.forLanguageTag("zh-CN").getCountry)
    assertEquals("", Locale.forLanguageTag("zh-CN").getVariant)
    assertEquals("", Locale.forLanguageTag("zh-CN").getScript)

    assertEquals(Locale.CHINA, Locale.forLanguageTag("zh-CN"))
  }

  @Test def test_default_PRC(): Unit = {
    assertEquals("zh", Locale.forLanguageTag("zh-CN").getLanguage)
    assertEquals("CN", Locale.forLanguageTag("zh-CN").getCountry)
    assertEquals("", Locale.forLanguageTag("zh-CN").getVariant)
    assertEquals("", Locale.forLanguageTag("zh-CN").getScript)

    assertEquals(Locale.PRC, Locale.forLanguageTag("zh-CN"))
  }

  @Test def test_default_TAIWAN(): Unit = {
    assertEquals("zh", Locale.forLanguageTag("zh-TW").getLanguage)
    assertEquals("TW", Locale.forLanguageTag("zh-TW").getCountry)
    assertEquals("", Locale.forLanguageTag("zh-TW").getVariant)
    assertEquals("", Locale.forLanguageTag("zh-TW").getScript)

    assertEquals(Locale.TAIWAN, Locale.forLanguageTag("zh-TW"))
    assertEquals(Locale.TRADITIONAL_CHINESE, Locale.forLanguageTag("zh-TW"))
  }

  @Test def test_default_UK(): Unit = {
    assertEquals("en", Locale.forLanguageTag("en-GB").getLanguage)
    assertEquals("GB", Locale.forLanguageTag("en-GB").getCountry)
    assertEquals("", Locale.forLanguageTag("en-GB").getVariant)
    assertEquals("", Locale.forLanguageTag("en-GB").getScript)

    assertEquals(Locale.UK, Locale.forLanguageTag("en-GB"))
  }

  @Test def test_default_US(): Unit = {
    assertEquals("en", Locale.forLanguageTag("en-US").getLanguage)
    assertEquals("US", Locale.forLanguageTag("en-US").getCountry)
    assertEquals("", Locale.forLanguageTag("en-US").getVariant)
    assertEquals("", Locale.forLanguageTag("en-US").getScript)

    assertEquals(Locale.US, Locale.forLanguageTag("en-US"))
  }

  @Test def test_default_CANADA(): Unit = {
    assertEquals("en", Locale.forLanguageTag("en-CA").getLanguage)
    assertEquals("CA", Locale.forLanguageTag("en-CA").getCountry)
    assertEquals("", Locale.forLanguageTag("en-CA").getVariant)
    assertEquals("", Locale.forLanguageTag("en-CA").getScript)

    assertEquals(Locale.CANADA, Locale.forLanguageTag("en-CA"))
  }

  @Test def test_default_CANADA_FRENCH(): Unit = {
    assertEquals("fr", Locale.forLanguageTag("fr-CA").getLanguage)
    assertEquals("CA", Locale.forLanguageTag("fr-CA").getCountry)
    assertEquals("", Locale.forLanguageTag("fr-CA").getVariant)
    assertEquals("", Locale.forLanguageTag("fr-CA").getScript)

    assertEquals(Locale.CANADA_FRENCH, Locale.forLanguageTag("fr-CA"))
  }

  @Test def test_default_ROOT(): Unit = {
    assertEquals("", Locale.forLanguageTag("").getLanguage)
    assertEquals("", Locale.forLanguageTag("").getCountry)
    assertEquals("", Locale.forLanguageTag("").getVariant)
    assertEquals("", Locale.forLanguageTag("").getScript)

    assertEquals(Locale.ROOT, Locale.forLanguageTag(""))
  }

  @Test def test_extension_flags(): Unit = {
    assertEquals('u', Locale.UNICODE_LOCALE_EXTENSION)
    assertEquals('x', Locale.PRIVATE_USE_EXTENSION)
  }

  @Test def test_chinese_equivalences(): Unit = {
    assertEquals(Locale.SIMPLIFIED_CHINESE, Locale.CHINA)
    assertEquals(Locale.TRADITIONAL_CHINESE, Locale.TAIWAN)
  }

  // The tests operate with ENGLISH as the default locale
  @Test def test_default_locale(): Unit = {
    assertEquals(Locale.ENGLISH, Locale.getDefault)
  }

  @Test def test_default_locale_per_category(): Unit = {
    assertEquals(Locale.ENGLISH, Locale.getDefault(Locale.Category.DISPLAY))
    assertEquals(Locale.ENGLISH, Locale.getDefault(Locale.Category.FORMAT))
    expectThrows(classOf[NullPointerException], Locale.getDefault(null))
  }

  @Test def test_set_default_locale(): Unit = {
    Locale.setDefault(Locale.CANADA_FRENCH)
    assertEquals(Locale.CANADA_FRENCH, Locale.getDefault)
    // As a side effect this sets the defaults for each category
    assertEquals(Locale.CANADA_FRENCH,
      Locale.getDefault(Locale.Category.DISPLAY))
    assertEquals(Locale.CANADA_FRENCH,
      Locale.getDefault(Locale.Category.FORMAT))

    Locale.setDefault(Locale.Category.DISPLAY, Locale.CHINESE)
    assertEquals(Locale.CANADA_FRENCH, Locale.getDefault)
    assertEquals(Locale.CHINESE, Locale.getDefault(Locale.Category.DISPLAY))

    expectThrows(classOf[NullPointerException], Locale.setDefault(null))
  }

  @Test def test_get_available_locales(): Unit = {
    assertTrue(Locale.getAvailableLocales.contains(Locale.CHINESE))
    assertTrue(Locale.getAvailableLocales.contains(Locale.ENGLISH))
    assertTrue(Locale.getAvailableLocales.contains(Locale.ITALY))
  }

  @Test def test_get_iso_codes(): Unit = {
    // The data from CLDR gives a different amount of countries and
    // languages than the JVM
    val countriesCount = if (Platform.executingInJVM) 250 else 247
    val languagesCount = if (Platform.executingInJVM) 188 else 126
    assertEquals(countriesCount, Locale.getISOCountries.length)
    assertEquals(languagesCount, Locale.getISOLanguages.length)
  }

  @Test def test_special_cases_language(): Unit = {
    val iwLocale = new Locale("iw")
    assertEquals("iw", iwLocale.getLanguage)
    val heLocale = new Locale("he")
    assertEquals("iw", heLocale.getLanguage)

    val jiLocale = new Locale("ji")
    assertEquals("ji", jiLocale.getLanguage)
    val yiLocale = new Locale("yi")
    assertEquals("ji", yiLocale.getLanguage)

    val inLocale = new Locale("in")
    assertEquals("in", inLocale.getLanguage)
    val idLocale = new Locale("id")
    assertEquals("in", idLocale.getLanguage)
  }

  @Test def test_has_extensions(): Unit = {
    // You can only add extensions with Locale.Builder
    val b1 = new Locale.Builder()
    val locale = b1.setExtension('a', "ca-japanese").build
    assertTrue(locale.hasExtensions)

    assertFalse(new Locale("en", "US").hasExtensions)
    // Special cases
    assertTrue(new Locale("ja", "JP", "JP").hasExtensions)

    // Unicode extensions
    val b2 = new Locale.Builder()
    val locale2 = b2.setUnicodeLocaleKeyword("nu", "thai").build
    assertTrue(locale2.hasExtensions)
  }

  @Test def test_strip_extensions(): Unit = {
    // You can only add extensions with Locale.Builder
    val b1 = new Locale.Builder()
    val locale = b1.setExtension('a', "ca-japanese").build
    assertFalse(locale.stripExtensions.hasExtensions)

    // Special cases
    assertFalse(new Locale("ja", "JP", "JP").stripExtensions().hasExtensions)
    assertFalse(new Locale("th", "TH", "TH").stripExtensions().hasExtensions)
  }

  @Test def test_to_string(): Unit = {
    // Examples from javadocs
    val l1 = new Locale.Builder().setLanguage("en").build
    assertEquals("en", l1.toString)
    val l2 = new Locale.Builder().setLanguage("de").setRegion("DE").build
    assertEquals("de_DE", l2.toString)
    val l3 = new Locale.Builder().setRegion("GB").build
    assertEquals("_GB", l3.toString)
    val l4 = new Locale("en", "US", "WIN")
    assertEquals("en_US_WIN", l4.toString)
    val l5 = new Locale.Builder().setLanguage("de").setVariant("POSIX").build
    assertEquals("de__POSIX", l5.toString)
    val l6 = new Locale.Builder().setLanguage("zh").setRegion("CN")
        .setScript("Hans").build
    assertEquals("zh_CN_#Hans", l6.toString)
    val l7 = new Locale.Builder().setLanguage("zh").setRegion("TW")
        .setScript("Hant").setExtension('x', "java").build
    assertEquals("zh_TW_#Hant_x-java", l7.toString)
    val l8 = new Locale("th", "TH", "TH")
    assertEquals("th_TH_TH_#u-nu-thai", l8.toString)
    val l9 = new Locale("", "", "POSIX")
    assertEquals("", l9.toString)
  }

  @Test def test_to_language_tag(): Unit = {
    val l1 = new Locale.Builder().setLanguage("en").build
    assertEquals("en", l1.toLanguageTag)
    val l2 = new Locale.Builder().setLanguage("de").setRegion("DE").build
    assertEquals("de-DE", l2.toLanguageTag)
    val l3 = new Locale.Builder().setRegion("GB").build
    assertEquals("und-GB", l3.toLanguageTag)
    val l4 = new Locale("en", "US", "WIN")
    assertEquals("en-US-x-lvariant-WIN", l4.toLanguageTag)
    val l5 = new Locale.Builder().setLanguage("de").setVariant("POSIX").build
    assertEquals("de-POSIX", l5.toLanguageTag)
    val l6 = new Locale.Builder().setLanguage("zh").setRegion("CN")
        .setScript("Hans").build
    assertEquals("zh-Hans-CN", l6.toLanguageTag)
    val l7 = new Locale.Builder().setLanguage("zh").setRegion("TW")
        .setScript("Hant").setExtension('x', "java").build
    assertEquals("zh-Hant-TW-x-java", l7.toLanguageTag)
    val l8 = new Locale("th", "TH", "TH")
    assertEquals("th-TH-u-nu-thai-x-lvariant-TH", l8.toLanguageTag)
    val l9 = new Locale("en", "US", "Oracle_JDK_Standard_Edition")
    assertEquals("en-US-Oracle-x-lvariant-JDK-Standard-Edition",
        l9.toLanguageTag)

    // Special cases
    val l10 = new Locale("iw")
    assertEquals("he", l10.toLanguageTag)
    val l11 = new Locale("ji", "", "POSIX")
    assertEquals("yi-POSIX", l11.toLanguageTag)
    val l12 = new Locale("in", "IN")
    assertEquals("id-IN", l12.toLanguageTag)
    val l13 = new Locale("no", "NO", "NY")
    assertEquals("nn-NO", l13.toLanguageTag)
  }

  def assertLocaleFromTag(l: Locale, ln: String, c: String, s: String,
      v: String, ext: Map[Char, String] = Map.empty): Unit = {
    assertEquals(ln, l.getLanguage)
    assertEquals(c, l.getCountry)
    assertEquals(s, l.getScript)
    assertEquals(v, l.getVariant)
    assertTrue(ext.forall {
      case (x, v) => l.getExtensionKeys().contains(x) && l.getExtension(x) == v
    })

  }

  @Test def test_for_language_tag(): Unit = {
    val l1 = Locale.forLanguageTag("")
    assertLocaleFromTag(l1, "", "", "", "")

    val l2 = Locale.forLanguageTag("en")
    assertLocaleFromTag(l2, "en", "", "", "")

    val l3 = Locale.forLanguageTag("de-DE")
    assertLocaleFromTag(l3, "de", "DE", "", "")

    val l4 = Locale.forLanguageTag("und-GB")
    assertLocaleFromTag(l4, "", "GB", "", "")

    val l5 = Locale.forLanguageTag("en-US-x-lvariant-WIN")
    assertLocaleFromTag(l5, "en", "US", "", "WIN")

    val l6 = Locale.forLanguageTag("zh-Hans-CN")
    assertLocaleFromTag(l6, "zh", "CN", "Hans", "")

    val l7 = Locale.forLanguageTag("zh-Hant-TW-x-java")
    assertLocaleFromTag(l7, "zh", "TW", "Hant", "", Map('x' -> "java"))

    val l8 = Locale.forLanguageTag("th-TH-u-nu-thai-x-lvariant-TH")
    assertLocaleFromTag(l8, "th", "TH", "", "TH", Map('u' -> "nu-thai"))

    val l9 = Locale.forLanguageTag("en-US-Oracle-x-lvariant-JDK-Standard-Edition")
    assertLocaleFromTag(l9, "en", "US", "", "Oracle_JDK_Standard_Edition")

    val l10 = Locale.forLanguageTag("en-US-x-lvariant-POSIX")
    assertLocaleFromTag(l10, "en", "US", "", "POSIX")

    val l11 = Locale.forLanguageTag("de-POSIX-x-URP-lvariant-Abc-Def")
    assertLocaleFromTag(l11, "de", "", "", "POSIX_Abc_Def", Map('x' -> "urp"))

    val l12 = Locale.forLanguageTag("ar-aao")
    assertLocaleFromTag(l12, "aao", "", "", "")

    val l13 = Locale.forLanguageTag("en-abc-def-us")
    assertLocaleFromTag(l13, "abc", "US", "", "")
  }

  @Test def test_for_language_tag_special_cases(): Unit = {
    assertEquals("ja-JP-u-ca-japanese-x-lvariant-JP",
      Locale.forLanguageTag("ja-JP-x-lvariant-JP").toLanguageTag)

    assertEquals("th-TH-u-nu-thai-x-lvariant-TH",
      Locale.forLanguageTag("th-TH-x-lvariant-TH").toLanguageTag)

    // Special cases
    val l1 = Locale.forLanguageTag("iw")
    assertLocaleFromTag(l1, "iw", "", "", "")
    val l2 = Locale.forLanguageTag("he")
    assertLocaleFromTag(l2, "iw", "", "", "")
    val l3 = Locale.forLanguageTag("ji")
    assertLocaleFromTag(l3, "ji", "", "", "")
    val l4 = Locale.forLanguageTag("yi")
    assertLocaleFromTag(l4, "ji", "", "", "")
    val l5 = Locale.forLanguageTag("in")
    assertLocaleFromTag(l5, "in", "", "", "")
    val l6 = Locale.forLanguageTag("id")
    assertLocaleFromTag(l6, "in", "", "", "")
  }

  @Test def test_for_language_tag_grandfathereded(): Unit = {
    // grandfathered mapping
    val mapping = List("art-lojban" -> "jbo", "i-ami" -> "ami",
      "i-bnn" -> "bnn", "i-hak" -> "hak", "i-klingon" -> "tlh", "i-lux" -> "lb",
      "i-hak" -> "hak", "i-navajo" -> "nv", "i-pwn" -> "pwn", "i-tao" -> "tao",
      "i-tay" -> "tay", "i-tsu" -> "tsu", "no-bok" -> "nb", "no-nyn" -> "nn",
      "sgn-BE-FR" -> "sfb", "sgn-BE-NL" -> "vgt", "sgn-CH-DE" -> "sgg",
      "zh-guoyu" -> "cmn", "zh-hakka" -> "hak", "zh-min-nan" -> "nan",
      "zh-xiang" -> "hsn",
      "cel-gaulish" -> "xtg") /* In javadocs cel-gaulish is xtg-x-cel-gaulish */

    mapping.foreach { case (g, e) =>
      val l = Locale.forLanguageTag(g)
      assertLocaleFromTag(l, e, "", "", "")
    }

    val l1 = Locale.forLanguageTag("en-GB-oed")
    assertLocaleFromTag(l1, "en", "GB", "", "", Map('x' -> "oed"))
    val l2 = Locale.forLanguageTag("i-default")
    assertLocaleFromTag(l2, "en", "", "", "", Map('x' -> "i-default"))
    val l3 = Locale.forLanguageTag("i-enochian")
    assertLocaleFromTag(l3, "", "", "", "", Map('x' -> "i-enochian"))
    val l4 = Locale.forLanguageTag("i-mingo")
    assertLocaleFromTag(l4, "see", "", "", "", Map('x' -> "i-mingo"))
    val l5 = Locale.forLanguageTag("zh-min")
    assertLocaleFromTag(l5, "nan", "", "", "", Map('x' -> "zh-min"))
  }

  // samples taken from Appendix A of the BCP 47 specification
  // https://tools.ietf.org/html/bcp47#appendix-A
  @Test def test_simple_languages_subtag_samples(): Unit = {
    // Simple language subtag:
    // de (German)
    val l1 = Locale.forLanguageTag("de")
    assertLocaleFromTag(l1, "de", "", "", "")

    // fr (French)
    val l2 = Locale.forLanguageTag("fr")
    assertLocaleFromTag(l2, "fr", "", "", "")

    // ja (Japanese)
    val l3 = Locale.forLanguageTag("ja")
    assertLocaleFromTag(l3, "ja", "", "", "")

    // i-enochian (example of a grandfathered tag)
    val l4 = Locale.forLanguageTag("i-enochian")
    assertLocaleFromTag(l4, "", "", "", "", Map('x' -> "i-enochian"))
  }

  @Test def test_languages_script_samples(): Unit = {
    // Language subtag plus Script subtag:
    // zh-Hant (Chinese written using the Traditional Chinese script)
    val l1 = Locale.forLanguageTag("zh-Hant")
    assertLocaleFromTag(l1, "zh", "", "Hant", "")

    // zh-Hans (Chinese written using the Simplified Chinese script)
    val l2 = Locale.forLanguageTag("zh-Hans")
    assertLocaleFromTag(l2, "zh", "", "Hans", "")

    // sr-Cyrl (Serbian written using the Cyrillic script)
    val l3 = Locale.forLanguageTag("sr-Cyrl")
    assertLocaleFromTag(l3, "sr", "", "Cyrl", "")

    // sr-Latn (Serbian written using the Latin script)
    val l4 = Locale.forLanguageTag("sr-Latn")
    assertLocaleFromTag(l4, "sr", "", "Latn", "")
  }

  @Test def test_languages_extended_samples(): Unit = {
    // Extended language subtags:
    // zh-cmn-Hans-CN (Chinese, Mandarin, Simplified script, as used in China)
    val l1 = Locale.forLanguageTag("zh-cmn-Hans-CN")
    assertLocaleFromTag(l1, "cmn", "CN", "Hans", "")

    // cmn-Hans-CN (Mandarin Chinese, Simplified script, as used in China)
    val l2 = Locale.forLanguageTag("cmn-Hans-CN")
    assertLocaleFromTag(l2, "cmn", "CN", "Hans", "")

    // zh-yue-HK (Chinese, Cantonese, as used in Hong Kong SAR)
    val l3 = Locale.forLanguageTag("zh-yue-HK")
    assertLocaleFromTag(l3, "yue", "HK", "", "")

    // yue-HK (Cantonese Chinese, as used in Hong Kong SAR)
    val l4 = Locale.forLanguageTag("yue-HK")
    assertLocaleFromTag(l4, "yue", "HK", "", "")
  }

  @Test def test_language_script_region_samples(): Unit = {
    // Language-Script-Region:
    // zh-Hans-CN (Chinese written using the Simplified script as used in mainland China)
    val l1 = Locale.forLanguageTag("zh-Hans-CN")
    assertLocaleFromTag(l1, "zh", "CN", "Hans", "")

    // sr-Latn-RS (Serbian written using the Latin script as used in Serbia)
    val l2 = Locale.forLanguageTag("sr-Latn-RS")
    assertLocaleFromTag(l2, "sr", "RS", "Latn", "")
  }

  @Test def test_language_variant_samples(): Unit = {
    // Language-Variant:
    // sl-rozaj (Resian dialect of Slovenian)
    val l1 = Locale.forLanguageTag("sl-rozaj")
    assertLocaleFromTag(l1, "sl", "", "", "rozaj")

    // sl-rozaj-biske (San Giorgio dialect of Resian dialect of Slovenian)
    val l2 = Locale.forLanguageTag("sl-rozaj-biske")
    assertLocaleFromTag(l2, "sl", "", "", "rozaj_biske")

    // sl-nedis (Nadiza dialect of Slovenian)
    val l3 = Locale.forLanguageTag("sl-nedis")
    assertLocaleFromTag(l3, "sl", "", "", "nedis")
  }

  @Test def test_language_region_variant_samples(): Unit = {
    // Language-Region-Variant:
    // de-CH-1901 (German as used in Switzerland using the 1901 variant [orthography])
    val l1 = Locale.forLanguageTag("de-CH-1901")
    assertLocaleFromTag(l1, "de", "CH", "", "1901")
    // sl-IT-nedis (Slovenian as used in Italy, Nadiza dialect)
    val l2 = Locale.forLanguageTag("sl-IT-nedis")
    assertLocaleFromTag(l2, "sl", "IT", "", "nedis")
  }

  @Test def test_language_script_region_variant_samples(): Unit = {
    // Language-Script-Region-Variant:
    // hy-Latn-IT-arevela (Eastern Armenian written in Latin script, as used in Italy)
    val l1 = Locale.forLanguageTag("hy-Latn-IT-arevela")
    assertLocaleFromTag(l1, "hy", "IT", "Latn", "arevela")
  }

  @Test def test_language_region_samples(): Unit = {
    // Language-Region:
    // de-DE (German for Germany)
    val l1 = Locale.forLanguageTag("de-DE")
    assertLocaleFromTag(l1, "de", "DE", "", "")

    // en-US (English as used in the United States)
    val l2 = Locale.forLanguageTag("en-US")
    assertLocaleFromTag(l2, "en", "US", "", "")

    // es-419 (Spanish appropriate for the Latin America and Caribbean
    // region using the UN region code)
    val l3 = Locale.forLanguageTag("es-419")
    assertLocaleFromTag(l3, "es", "419", "", "")
  }

  @Test def test_private_use_samples(): Unit = {
    // Private use subtags:
    // de-CH-x-phonebk
    val l1 = Locale.forLanguageTag("de-CH-x-phonebk")
    assertLocaleFromTag(l1, "de", "CH", "", "", Map('x' -> "phonebk"))

    // az-Arab-x-AZE-derbend
    val l2 = Locale.forLanguageTag("az-Arab-x-AZE-derbend")
    assertLocaleFromTag(l2, "az", "", "Arab", "", Map('x' -> "aze-derbend"))
  }

  @Test def test_private_use_tag(): Unit = {
    // Private use registry values:
    // x-whatever (private use using the singleton 'x')
    val l1 = Locale.forLanguageTag("x-whatever")
    assertLocaleFromTag(l1, "", "", "", "", Map('x' -> "whatever"))
  }

  @Test def test_extensions_samples(): Unit = {
    // Tags that use extensions:
    // en-US-u-islamcal
    val l1 = Locale.forLanguageTag("en-US-u-islamcal")
    assertLocaleFromTag(l1, "en", "US", "", "", Map('u' -> "islamcal"))

    // zh-CN-a-myext-x-private
    val l2 = Locale.forLanguageTag("zh-CN-a-myext-x-private")
    assertLocaleFromTag(l2, "zh", "CN", "", "", Map('x' -> "private", 'a' -> "myext"))

    // en-a-myext-b-another
    val l3 = Locale.forLanguageTag("en-a-myext-b-another")
    assertLocaleFromTag(l3, "en", "", "", "", Map('b' -> "another", 'a' -> "myext"))
  }

  @Test def test_invalid_samples(): Unit = {
    // Tags that use extensions:
    // de-419-DE (two region tags)
    val l1 = Locale.forLanguageTag("de-419-DE")
    assertLocaleFromTag(l1, "de", "419", "", "")

    // a-DE (use of a single-character subtag in primary position; note
    // that there are a few grandfathered tags that start with "i-" that
    // are valid)
    val l2 = Locale.forLanguageTag("a-DE")
    assertLocaleFromTag(l2, "", "", "", "")
  }

}
