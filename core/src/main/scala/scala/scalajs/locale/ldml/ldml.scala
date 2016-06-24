package scala.scalajs.locale.ldml

import java.util.Locale

/**
  * Interfaces describing the digit symbols
  */
case class LDMLNumberingSystem(id: String, digits: Seq[Char])

case class LDMLDigitSymbols(decimal: Option[String],
    group: Option[String],
    list: Option[String],
    percent: Option[String],
    minus: Option[String],
    perMille: Option[String],
    infinity: Option[String],
    nan: Option[String],
    exp: Option[String])

/**
  * Interfaces describing an LDML Locale
  */
case class LDMLLocale(language: String, territory: Option[String],
    variant: Option[String], script: Option[String])

/**
  * Wrapper to LDML
  */
case class LDML(parent: Option[LDML], locale: LDMLLocale, defaultNS: Option[LDMLNumberingSystem],
    digitSymbols: Option[LDMLDigitSymbols] = None) {

  def languageTag: String = toLocale.toLanguageTag

  def toLocale: Locale = {
    if (locale.language == "root") {
      new Locale.Builder().setLanguage("")
        .setRegion(locale.territory.getOrElse(""))
        .setScript(locale.script.getOrElse(""))
        .setVariant(locale.variant.getOrElse("")).build
    } else {
      new Locale.Builder().setLanguage(locale.language)
        .setRegion(locale.territory.getOrElse(""))
        .setScript(locale.script.getOrElse(""))
        .setVariant(locale.variant.getOrElse("")).build
    }
  }
}
