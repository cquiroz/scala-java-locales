package locales

import locales.cldr.CLDRMetadata
import locales.cldr.LDML
import locales.cldr.LocalesProvider
import locales.cldr.NumberingSystem
import org.portablescala.reflect._
import java.util.Locale

object LocalesDb {

  val provider: LocalesProvider =
    Reflect
      .lookupLoadableModuleClass("locales.cldr.data.LocalesProvider$", null)
      .map { m =>
        m.loadModule()
          .asInstanceOf[LocalesProvider]
      }
      .getOrElse(locales.cldr.fallback.LocalesProvider)

  val root: LDML = provider.root

  val latn: NumberingSystem = provider.latn

  val ldmls = provider.ldmls

  val metadata: CLDRMetadata = provider.metadata

  val currencydata = provider.currencyData

  /**
    * Attempts to give a Locale for the given tag if available
    */
  def localeForLanguageTag(languageTag: String): Option[Locale] =
    // TODO Support alternative tags for the same locale
    if (languageTag == "und")
      Some(Locale.ROOT)
    else
      provider.ldmls.get(languageTag).map(_.toLocale)

  /**
    * Returns the ldml for the given locale
    */
  def ldml(locale: Locale): Option[LDML] = {
    val tag =
      if (locale.toLanguageTag() == "zh-CN") "zh-Hans-CN"
      else if (locale.toLanguageTag() == "zh-TW") "zh-Hant-TW"
      else locale.toLanguageTag()
    provider.ldmls.get(tag)
  }
}
