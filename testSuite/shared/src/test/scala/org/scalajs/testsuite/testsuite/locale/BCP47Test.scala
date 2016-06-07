package org.scalajs.testsuite.testsuite.locale

import org.junit.Test
import org.junit.Assert._

import scala.scalajs.locale.BCP47
import scala.scalajs.locale.BCP47.{GrandfatheredTag, LanguageTag, PrivateUseTag}
import scala.scalajs.locale.ldml.data.metadata._

class BCP47Test {

  @Test def test_language(): Unit = {
    isoLanguages.map(BCP47.parseTag).zip(isoLanguages).foreach {
      case (Some(LanguageTag(lang, None, None, None, Nil, Nil, None)), l) =>
        assertEquals(lang, l)

      case _ =>
        fail()
    }
  }

  @Test def test_language_region(): Unit = {
    val tags = for {
      l <- isoLanguages
      r <- isoCountries
    } yield (l, r, s"$l-$r")
    tags.map { case (l, r, t) => (l, r, BCP47.parseTag(t)) }.foreach {
      case (l, r, Some(LanguageTag(lang, None, None, region, Nil, Nil, None))) =>
        assertEquals(l, lang)
        assertEquals(Some(r), region)

      case _ =>
        fail()
    }
  }

  @Test def test_language_region_script(): Unit = {
    val tags = for {
      l <- isoLanguages
      r <- isoCountries
      s <- scripts
    } yield (l, r, s, s"$l-$s-$r")
    tags.map { case (l, r, s, t) => (l, r, s, BCP47.parseTag(t)) }.foreach {
      case (l, r, s, Some(LanguageTag(lang, None, script, region, Nil, Nil, None))) =>
        assertEquals(l, lang)
        assertEquals(Some(r), region)
        assertEquals(Some(s), script)

      case _ =>
        fail()
    }
  }

  @Test def test_grandfathered(): Unit = {
    val regularGrandFathered = BCP47.regular.split("\\|")
    val irregularGrandFathered = BCP47.irregular.split("\\|")
    val grandFathered = regularGrandFathered ++ irregularGrandFathered
    grandFathered.map(BCP47.parseTag).zip(grandFathered).foreach {
      case (Some(GrandfatheredTag(t)), g) => assertEquals(g, t)
      case _ => fail()
    }
  }

  @Test def test_ext_lang(): Unit = {
    val chineseWithExt = List("zh-gan", "zh-yue", "zh-cmn")
    chineseWithExt.map(BCP47.parseTag).zip(chineseWithExt).foreach {
      case (Some(LanguageTag(lang, extLang, _, _, _, _, _)), t) =>
        assertEquals("zh", lang)
        assertEquals(Some(t.replace("zh-", "")), extLang)

      case _ => fail()
    }
  }

  // samples taken from Appendix A of the BCP 47 specification
  // https://tools.ietf.org/html/bcp47#appendix-A
  @Test def test_simple_languages_subtag_samples(): Unit = {
    // Simple language subtag:
    // de (German)
    assertEquals(Some(LanguageTag("de", None, None, None, Nil, Nil, None)),
      BCP47.parseTag("de"))
    // fr (French)
    assertEquals(Some(LanguageTag("fr", None, None, None, Nil, Nil, None)),
      BCP47.parseTag("fr"))
    // ja (Japanese)
    assertEquals(Some(LanguageTag("ja", None, None, None, Nil, Nil, None)),
      BCP47.parseTag("ja"))
    // i-enochian (example of a grandfathered tag)
    assertEquals(Some(GrandfatheredTag("i-enochian")), BCP47.parseTag("i-enochian"))
  }

  @Test def test_languages_script_samples(): Unit = {
    // Language subtag plus Script subtag:
    // zh-Hant (Chinese written using the Traditional Chinese script)
    assertEquals(Some(LanguageTag("zh", None, Some("Hant"), None, Nil, Nil, None)),
      BCP47.parseTag("zh-Hant"))
    // zh-Hans (Chinese written using the Simplified Chinese script)
    assertEquals(Some(LanguageTag("zh", None, Some("Hans"), None, Nil, Nil, None)),
      BCP47.parseTag("zh-Hans"))
    // sr-Cyrl (Serbian written using the Cyrillic script)
    assertEquals(Some(LanguageTag("sr", None, Some("Cyrl"), None, Nil, Nil, None)),
      BCP47.parseTag("sr-Cyrl"))
    // sr-Latn (Serbian written using the Latin script)
    assertEquals(Some(LanguageTag("sr", None, Some("Latn"), None, Nil, Nil, None)),
      BCP47.parseTag("sr-Latn"))
  }

  @Test def test_languages_extended_samples(): Unit = {
    // Extended language subtags:
    // zh-cmn-Hans-CN (Chinese, Mandarin, Simplified script, as used in China)
    assertEquals(Some(LanguageTag("zh", Some("cmn"), Some("Hans"), Some("CN"), Nil, Nil, None)),
      BCP47.parseTag("zh-cmn-Hans-CN"))
    // cmn-Hans-CN (Mandarin Chinese, Simplified script, as used in China)
    assertEquals(Some(LanguageTag("cmn", None, Some("Hans"), Some("CN"), Nil, Nil, None)),
      BCP47.parseTag("cmn-Hans-CN"))
    // zh-yue-HK (Chinese, Cantonese, as used in Hong Kong SAR)
    assertEquals(Some(LanguageTag("zh", Some("yue"), None, Some("HK"), Nil, Nil, None)),
      BCP47.parseTag("zh-yue-HK"))
    // yue-HK (Cantonese Chinese, as used in Hong Kong SAR)
    assertEquals(Some(LanguageTag("yue", None, None, Some("HK"), Nil, Nil, None)),
      BCP47.parseTag("yue-HK"))
  }

  @Test def test_language_script_region_samples(): Unit = {
    // Language-Script-Region:
    // zh-Hans-CN (Chinese written using the Simplified script as used in mainland China)
    assertEquals(Some(LanguageTag("zh", Some("cmn"), Some("Hans"), Some("CN"), Nil, Nil, None)),
      BCP47.parseTag("zh-cmn-Hans-CN"))
    // sr-Latn-RS (Serbian written using the Latin script as used in Serbia)
    assertEquals(Some(LanguageTag("sr", None, Some("Latn"), Some("RS"), Nil, Nil, None)),
      BCP47.parseTag("sr-Latn-RS"))
  }

  @Test def test_language_variant_samples(): Unit = {
    // Language-Variant:
    // sl-rozaj (Resian dialect of Slovenian)
    assertEquals(Some(LanguageTag("sl", None, None, None, List("rozaj"), Nil, None)),
      BCP47.parseTag("sl-rozaj"))
    // sl-rozaj-biske (San Giorgio dialect of Resian dialect of Slovenian)
    assertEquals(Some(LanguageTag("sl", None, None, None, List("rozaj", "biske"), Nil, None)),
      BCP47.parseTag("sl-rozaj-biske"))
    // sl-nedis (Nadiza dialect of Slovenian)
    assertEquals(Some(LanguageTag("sl", None, None, None, List("nedis"), Nil, None)),
      BCP47.parseTag("sl-nedis"))
  }

  @Test def test_language_region_variant_samples(): Unit = {
    // Language-Region-Variant:
    // de-CH-1901 (German as used in Switzerland using the 1901 variant [orthography])
    assertEquals(Some(LanguageTag("de", None, None, Some("CH"), List("1901"), Nil, None)),
      BCP47.parseTag("de-CH-1901"))
    // sl-IT-nedis (Slovenian as used in Italy, Nadiza dialect)
    assertEquals(Some(LanguageTag("sl", None, None, Some("IT"), List("nedis"), Nil, None)),
      BCP47.parseTag("sl-IT-nedis"))
  }

  @Test def test_language_script_region_variant_samples(): Unit = {
    // Language-Script-Region-Variant:
    // hy-Latn-IT-arevela (Eastern Armenian written in Latin script, as used in Italy)
    assertEquals(Some(LanguageTag("hy", None, Some("Latn"), Some("IT"), List("arevela"), Nil, None)),
      BCP47.parseTag("hy-Latn-IT-arevela"))
  }

  @Test def test_language_region_samples(): Unit = {
    // Language-Region:
    // de-DE (German for Germany)
    assertEquals(Some(LanguageTag("de", None, None, Some("DE"), Nil, Nil, None)),
      BCP47.parseTag("de-DE"))
    // en-US (English as used in the United States)
    assertEquals(Some(LanguageTag("en", None, None, Some("US"), Nil, Nil, None)),
      BCP47.parseTag("en-US"))
    // es-419 (Spanish appropriate for the Latin America and Caribbean
    // region using the UN region code)
    assertEquals(Some(LanguageTag("es", None, None, Some("419"), Nil, Nil, None)),
      BCP47.parseTag("es-419"))
  }

  @Test def test_private_use_samples(): Unit = {
    // Private use subtags:
    // de-CH-x-phonebk
    assertEquals(Some(LanguageTag("de", None, None, Some("CH"), Nil, Nil, Some("phonebk"))),
      BCP47.parseTag("de-CH-x-phonebk"))
    // az-Arab-x-AZE-derbend
    assertEquals(Some(LanguageTag("az", None, Some("Arab"), None, Nil, Nil, Some("AZE-derbend"))),
      BCP47.parseTag("az-Arab-x-AZE-derbend"))
  }

  @Test def test_private_use_tag(): Unit = {
    // Private use registry values:
    // x-whatever (private use using the singleton 'x')
    assertEquals(Some(PrivateUseTag("whatever")), BCP47.parseTag("x-whatever"))
  }

  @Test def test_extensions_samples(): Unit = {
    // Tags that use extensions:
    // en-US-u-islamcal
    assertEquals(Some(LanguageTag("en", None, None, Some("US"), Nil, List("u-islamcal"), None)),
      BCP47.parseTag("en-US-u-islamcal"))
    // zh-CN-a-myext-x-private
    assertEquals(Some(LanguageTag("zh", None, None, Some("CN"), Nil, List("a-myext"), Some("private"))),
      BCP47.parseTag("zh-CN-a-myext-x-private"))
    // en-a-myext-b-another
    assertEquals(Some(LanguageTag("en", None, None, None, Nil, List("a-myext", "b-another"), None)),
      BCP47.parseTag("en-a-myext-b-another"))
  }

  @Test def test_invalid_samples(): Unit = {
    // Tags that use extensions:
    // de-419-DE (two region tags)
    assertEquals(None, BCP47.parseTag("de-419-DE"))
    // a-DE (use of a single-character subtag in primary position; note
    // that there are a few grandfathered tags that start with "i-" that
    // are valid)
    assertEquals(None, BCP47.parseTag("a-DE"))
  }
}
