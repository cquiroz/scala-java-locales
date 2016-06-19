package scala.scalajs.locale.ldml

import java.util.Locale

/**
  * Interfaces describing the digit symbols
  */
case class LDMLNumberingSystem(id: String)

case class LDMLDigitSymbols(system: LDMLDigitSymbols)

/**
  * Interfaces describing an LDML Locale
  */
case class LDMLLocale(language: String, territory: Option[String],
    variant: Option[String], script: Option[String])

/**
  * Wrapper to LDML
  */
case class LDML(parent: Option[LDML], locale: LDMLLocale, defaultNS: Option[LDMLNumberingSystem],
    digitSymbols: Map[LDMLNumberingSystem, LDMLDigitSymbols] = Map.empty) {
  def languageTag: String = toLocale.toLanguageTag

  def toLocale: Locale = {
    new Locale.Builder().setLanguage(locale.language)
      .setRegion(locale.territory.getOrElse(""))
      .setScript(locale.script.getOrElse(""))
      .setVariant(locale.variant.getOrElse("")).build
  }
}
