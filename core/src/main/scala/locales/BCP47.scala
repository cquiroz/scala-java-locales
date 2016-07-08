package locales

object BCP47 {
  // The regular experssions are carefully curated to work both in
  // JVM and JS(rhino/node), be careful when editing them
  lazy val extlang = "(?:-[A-Za-z]{3}){0,3}"
  lazy val language = s"([A-Za-z]{2,3})($extlang)?|[A-Za-z]{4}|[A-Za-z]{5,8}"
  lazy val script = "[A-Za-z]{4}"
  lazy val region = "([A-Za-z]{2}|[0-9]{3})"
  lazy val variant = "([A-Za-z0-9]{5,8}|[0-9][A-Za-z0-9]{3})"
  lazy val variantR = s"($variant)".r
  lazy val singleton = "[0-9A-WY-Za-wy-z]"
  lazy val extension = s"$singleton(?:-[A-Za-z0-9]{2,8})+"
  lazy val privateUse = "x(?:-[A-Za-z0-9]{1,8})+"
  lazy val langtag =
    s"^(?:$language)(-$script)?(?:-$region)?((?:-$variant)*)((?:-$extension)*)(-$privateUse)?$$"
  lazy val regular = List("art-lojban", "cel-gaulish", "no-bok", "no-nyn",
      "zh-guoyu", "zh-hakka", "zh-min-nan", "zh-min", "zh-xiang").mkString("|")
  lazy val irregular =
    List("en-GB-oed", "i-ami", "i-bnn", "i-default", "i-enochian", "i-hak",
        "i-klingon", "i-lux", "i-mingo", "i-navajo", "i-pwn", "i-tao", "i-tay",
        "i-tsu", "sgn-BE-FR", "sgn-BE-NL", "sgn-CH-DE").mkString("|")
  lazy val grandfathered = s"($irregular|$regular)"
  lazy val langtagRegex = s"$grandfathered|$langtag|($privateUse)".r

  // ADT for BCP47 results
  sealed trait BCP47Tag
  case class LanguageTag(language: String, extendedLanguag: Option[String],
      script: Option[String], region: Option[String], variant: List[String],
      extension: List[String], privateUse: Option[String])
      extends BCP47Tag
  case class GrandfatheredTag(language: String) extends BCP47Tag
  case class PrivateUseTag(privateUse: String) extends BCP47Tag

  // Remove the initial dash
  @inline private def rd(l: String): Option[String] =
    Option(l).filter(_.nonEmpty).map(_.substring(1))

  // Convert to list removing dashes
  @inline private def rdl(l: String): List[String] =
    Option(l)
      .filter(_.nonEmpty)
      .map(_.substring(1).split("-").toList)
      .getOrElse(Nil)

  // Convert to list of extensions
  @inline private def rde(l: String): List[String] = {
    Option(l)
      .filter(_.nonEmpty)
      .map(_.split(s"-").toList.filter(_.nonEmpty).sliding(2, 2).collect { case List(a, b) => s"$a-$b" }.toList)
      .getOrElse(Nil)
  }

  // Cleans up the private use tag
  @inline private def puc(l: String): Option[String] =
    Option(l).filter(_.nonEmpty).map(_.replaceFirst("-x-", ""))

  def parseTag(tag: String): Option[BCP47Tag] = {
    // https://tools.ietf.org/html/bcp47#section-2.1
    tag.trim match {
      // Groups:
      // g: grandfathered
      // l: language
      // el: extended language (including language)
      // s: script
      // r: region
      // v: variants (separated by dash)
      // x: Extensions
      // p: private use subtag
      case langtagRegex(_, l, el, s, r, v, _, x, p, _) if l != null =>
        // a bug on js regex doesn't extract the lang directly, but we can
        // extract it from the ext-lang value
        //val lang = Option(el).map(l.replace(rdl, "")).getOrElse(l)
        Some(LanguageTag(l, rd(el), rd(s), Option(r), rdl(v), rde(x), puc(p)))

      case langtagRegex(g, _, _, _, _, _, _, _, _, _) if g != null =>
        Some(GrandfatheredTag(g))

      case langtagRegex(_, _, _, _, _, _, _, _, _, p) if p != null =>
        Some(PrivateUseTag(p.replaceFirst("x-", "")))

      case _ =>
        None
    }
  }
}
