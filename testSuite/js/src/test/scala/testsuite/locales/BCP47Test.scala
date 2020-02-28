package testsuite.locales

import locales.BCP47
import locales.BCP47.{ LanguageTag }
import locales.cldr.data.metadata._

class BCP47Test extends munit.FunSuite {
  test("test_language") {
    isoLanguages.map(BCP47.parseTag).zip(isoLanguages).foreach {
      case (Some(LanguageTag(lang, None, None, None, Nil, Nil, None)), l) =>
        assertEquals(lang, l)

      case _ =>
        fail("Shouldn't happen")
    }
  }

  test("test_language_region") {
    val tags = for {
      l <- isoLanguages
      r <- isoCountries
    } yield (l, r, s"$l-$r")
    tags.map { case (l, r, t) => (l, r, BCP47.parseTag(t)) }.foreach {
      case (l, r, Some(LanguageTag(lang, None, None, region, Nil, Nil, None))) =>
        assertEquals(l, lang)
        assertEquals(Some(r): Option[String], region)

      case _ =>
        fail("Shouldn't happen")
    }
  }

  test("test_language_region_script") {
    val tags = for {
      l <- isoLanguages
      r <- isoCountries
      s <- scripts
    } yield (l, r, s, s"$l-$s-$r")
    tags.map { case (l, r, s, t) => (l, r, s, BCP47.parseTag(t)) }.foreach {
      case (l, r, s, Some(LanguageTag(lang, None, script, region, Nil, Nil, None))) =>
        assertEquals(l, lang)
        assertEquals(Some(r): Option[String], region)
        assertEquals(Some(s): Option[String], script)

      case _ =>
        fail("Shouldn't happen")
    }
  }
}
