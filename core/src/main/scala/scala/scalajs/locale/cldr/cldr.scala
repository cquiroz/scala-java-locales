package scala.scalajs.locale.cldr

import java.util.Locale

/**
 * Interfaces describing the digit symbols
 */
case class NumberingSystem(id: String, digits: Seq[Char])

case class Symbols(ns: NumberingSystem, aliasOf: Option[NumberingSystem],
    decimal: Option[Char], group: Option[Char], list: Option[Char],
    percent: Option[Char], minus: Option[Char], perMille: Option[Char],
    infinity: Option[String], nan: Option[String], exp: Option[String])

/** Interfaces describing calendar data */
case class Calendar(id: String)

/**
 * Interfaces describing an LDML Locale
 */
case class LDMLLocale(language: String, territory: Option[String],
    variant: Option[String], script: Option[String])

/**
 * Wrapper to LDML
 */
case class LDML(parent: Option[LDML], locale: LDMLLocale,
    defaultNS: Option[NumberingSystem], digitSymbols: List[Symbols] = Nil) {

  def languageTag: String = toLocale.toLanguageTag

  def toLocale: Locale = {
    if (locale.language == "root") {
      new Locale.Builder()
        .setLanguage("")
        .setRegion(locale.territory.getOrElse(""))
        .setScript(locale.script.getOrElse(""))
        .setVariant(locale.variant.getOrElse(""))
        .build
    } else {
      new Locale.Builder()
        .setLanguage(locale.language)
        .setRegion(locale.territory.getOrElse(""))
        .setScript(locale.script.getOrElse(""))
        .setVariant(locale.variant.getOrElse(""))
        .build
    }
  }
}
