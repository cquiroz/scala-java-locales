package scala.scalajs.locale.ldml

import java.util.Locale

/**
  * Interfaces describing LDNL
  */
case class LDMLLocale(language: String, territory: Option[String],
                      variant: Option[String], script: Option[String])

case class LDML(locale: LDMLLocale) {
  // TODO support script and extensions
  def languageTag: String = {
    locale.language +
    locale.territory.fold("")(t => s"-$t") + locale.variant.fold("")(v => s"-$v")
  }

  def toLocale: Locale =
    new Locale(locale.language, locale.territory.getOrElse(""), locale.variant.getOrElse(""))
}
