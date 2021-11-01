package locales

import java.util.Locale

object DefaultLocale {
  def platformLocale: Locale = {
    val lang          = System.getProperty("user.language")
    val countrySuffix = System.getProperty("user.country") match {
      case "" => ""
      case s  => s"_$s"
    }

    val localeString = s"$lang$countrySuffix"

    LocalesDb.localeForLanguageTag(localeString).getOrElse(LocalesDb.root.toLocale)
  }
}
