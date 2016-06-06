package scala.scalajs.locale

object BCP47 {
  lazy val extlang = "(?:-[A-Za-z]{3}){0,3}"
  lazy val language = s"([A-Za-z]{2,3})($extlang)?|[A-Za-z]{4}|[A-Za-z]{5,8}"
  lazy val script = "[A-Za-z]{4}"
  lazy val region = "[A-Za-z]{2}|[0-9]{3}"
  lazy val variant = "([A-Za-z0-9]{5,8}|[0-9][A-Za-z0-9]{3})"
  lazy val variantR = s"($variant)".r
  lazy val singleton = "[0-9A-WY-Za-wy-z]"
  lazy val extension = s"$singleton(?:-[A-Za-z0-9]{2,8})+"
  lazy val privateUse = "x(?:-[A-Za-z0-9]{1,8})+"
  lazy val langtag =
    s"^(?:$language)(-$script)?(-$region)?(?:-$variant)*(-$extension)*(-$privateUse)?$$"
  lazy val regular =
    List("art-lojban", "cel-gaulish", "no-bok", "no-nyn", "zh-guoyu",
    "zh-hakka", "zh-min-nan", "zh-min", "zh-xiang").mkString("|")
  lazy val irregular =
    List("en-GB-oed", "i-ami", "i-bnn", "i-default", "i-enochian", "i-hak",
    "i-klingon", "i-lux", "i-mingo", "i-navajo", "i-pwn", "i-tao", "i-tay",
    "i-tsu", "sgn-BE-FR", "sgn-BE-NL", "sgn-CH-DE").mkString("|")
  lazy val grandfathered = s"($irregular|$regular)"
  lazy val langtagRegex = s"$grandfathered|$langtag|$privateUse".r

  sealed trait BCP47Tag
  case class LanguageTag(language: String, extendedLanguag: Option[String],
      script: Option[String], region: Option[String], variant: Option[String],
      extension: Option[String], privateUse: Option[String]) extends BCP47Tag
  case class GrandfatheredTag(language: String) extends BCP47Tag
  case class PrivateUseTag(privateUse: String) extends BCP47Tag

  // Remove the initial dash
  @inline private def rd(l: String): Option[String] =
    Option(l).filter(_.nonEmpty).map(_.substring(1))

  def parseTag(tag: String): BCP47Tag = {
    tag match {
      // Groups:
      // g: grandfathered
      // l: language
      // el: extended language (including language)
      // s: script
      // r: region
      case langtagRegex(_, l, el, s, r, _, _, _) if l != null =>
        // a bug on js regex doesn't extract the lang directly, but we can
        // extract it from the ext-lang value
        //val lang = Option(el).map(l.replace(_, "")).getOrElse(l)
        LanguageTag(l, rd(el), rd(s), rd(r), None, None, None)

      case langtagRegex(g, _, _, _, _, _, _, _) if g != null =>
        GrandfatheredTag(g)

      case _ =>
        println("NONE")
        ???
    }
  }


}
