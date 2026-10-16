package locales

import java.util.Locale

object DefaultLocale {
  lazy val window: Window = JSHostLanguage.window

  def platformLocale: Locale = {
    val lang = JSHostLanguage.navigatorLanguage()
    val l    = lang.filter(LocalesDb.ldmls.contains).getOrElse("en")
    LocalesDb.localeForLanguageTag(l).getOrElse(LocalesDb.root.toLocale)
  }

}
