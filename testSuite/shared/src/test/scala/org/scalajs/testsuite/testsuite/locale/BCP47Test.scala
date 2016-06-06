package org.scalajs.testsuite.testsuite.locale

import org.junit.{Ignore, Test}
import org.junit.Assert._

import scala.scalajs.locale.BCP47
import scala.scalajs.locale.BCP47.{GrandfatheredTag, LanguageTag}
import scala.scalajs.locale.ldml.data.metadata._

class BCP47Test {

  @Test def test_language(): Unit = {
    isoLanguages.map(BCP47.parseTag).zip(isoLanguages).foreach {
      case (LanguageTag(lang, None, None, None, None, None, None), l) =>
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
      case (l, r, LanguageTag(lang, None, None, region, None, None, None)) =>
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
      case (l, r, s, LanguageTag(lang, None, script, region, None, None, None)) =>
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
      case (GrandfatheredTag(t), g) => assertEquals(g, t)
      case _ => fail()
    }
  }

  @Test def test_ext_lang(): Unit = {
    val chineseWithExt = List("zh-gan", "zh-yue", "zh-cmn")
    chineseWithExt.map(BCP47.parseTag).zip(chineseWithExt).foreach {
      case (LanguageTag(lang, extLang, _, _, _, _, _), t) =>
        assertEquals("zh", lang)
        assertEquals(Some(t.replace("zh-", "")), extLang)

      case _ => fail()
    }
  }
}
