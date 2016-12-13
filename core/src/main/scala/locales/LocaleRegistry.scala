package locales

import java.text.DecimalFormatSymbols
import java.util.Locale

import scala.collection.{Map, mutable}
import locales.cldr.LDML
import locales.cldr.data

/**
  * Implements a database of locales
  */
object LocaleRegistry {

  // The spec requires some locales by default
  lazy val en: LDML         = data.en
  lazy val fr: LDML         = data.fr
  lazy val de: LDML         = data.de
  lazy val it: LDML         = data.it
  lazy val ja: LDML         = data.ja
  lazy val ko: LDML         = data.ko
  lazy val zh: LDML         = data.zh
  // The JVM uses Chinese without script unlike CLDR
  lazy val zh_Hans_CN: LDML = data.zh_Hans_CN
    .copy(locale = data.zh_Hans_CN.locale.copy(script = None))
  lazy val zh_Hant_TW: LDML = data.zh_Hant_TW
    .copy(locale = data.zh_Hant_TW.locale.copy(script = None))
  lazy val fr_FR: LDML      = data.fr_FR
  lazy val de_DE: LDML      = data.de_DE
  lazy val it_IT: LDML      = data.it_IT
  lazy val ja_JP: LDML      = data.ja_JP
  lazy val ko_KR: LDML      = data.ko_KR
  lazy val en_GB: LDML      = data.en_GB
  lazy val en_US: LDML      = data.en_US
  lazy val en_CA: LDML      = data.en_CA
  lazy val fr_CA: LDML      = data.fr_CA
  lazy val root: LDML       = data.root

  case class LocaleCldr(locale: Locale,
      decimalFormatSymbol: Option[DecimalFormatSymbols])

  private lazy val defaultLocales: Map[String, LDML] = Map(
    root.languageTag -> root,
    en.languageTag -> en,
    fr.languageTag -> fr,
    de.languageTag -> de,
    it.languageTag -> it,
    ja.languageTag -> ja,
    ko.languageTag -> ko,
    zh.languageTag -> zh,
    zh_Hans_CN.languageTag -> zh_Hans_CN,
    zh_Hant_TW.languageTag -> zh_Hant_TW,
    fr_FR.languageTag -> fr_FR,
    de_DE.languageTag -> de_DE,
    it_IT.languageTag -> it_IT,
    ja_JP.languageTag -> ja_JP,
    ko_KR.languageTag -> ko_KR,
    en_GB.languageTag -> en_GB,
    en_US.languageTag -> en_US,
    en_CA.languageTag -> en_CA,
    fr_CA.languageTag -> fr_CA
  )

  private var defaultLocale: Locale = en.toLocale
  private var defaultPerCategory: Map[Locale.Category, Option[Locale]] =
    Locale.Category.values().map(_ -> Some(defaultLocale)).toMap

  private lazy val ldmls: mutable.Map[String, LDML] = mutable.Map.empty

  initDefaultLocales()

  /**
    * Install an ldml class making its locale available to the runtime
    */
  def installLocale(ldml: LDML): Unit = ldmls += ldml.languageTag -> ldml

  /**
    * Cleans the registry, useful for testing
    */
  def resetRegistry(): Unit = {
    defaultLocale = en.toLocale
    defaultPerCategory =
        Locale.Category.values().map(_ -> Some(defaultLocale)).toMap
    ldmls.clear()
    initDefaultLocales()
  }

  private def initDefaultLocales(): Unit = {
    // Initialize defaults
    defaultLocales.foreach {
      case (_, l) => installLocale(l)
    }
  }

  def default: Locale = defaultLocale

  def default(category: Locale.Category): Locale = {
    if (category == null) {
      throw new NullPointerException("Argument cannot be null")
    } else {
      defaultPerCategory.get(category).flatten
        .getOrElse(throw new IllegalStateException(s"No default locale set for category $category"))
    }
  }

  def setDefault(newLocale: Locale): Unit = {
    if (newLocale == null) {
      throw new NullPointerException("Argument cannot be null")
    }
    defaultLocale = newLocale
    defaultPerCategory = Locale.Category.values().map(_ -> Some(newLocale)).toMap
  }

  def setDefault(category: Locale.Category, newLocale: Locale): Unit = {
    if (category == null || newLocale == null) {
      throw new NullPointerException("Argument cannot be null")
    } else {
      defaultPerCategory = defaultPerCategory + (category -> Some(newLocale))
    }
  }

  /**
    * Attempts to give a Locale for the given tag if available
    */
  def localeForLanguageTag(languageTag: String): Option[Locale] = {
    // TODO Support alternative tags for the same locale
    ldmls.get(languageTag).map(_.toLocale)
  }

  /**
    * Returns a list of available locales
    */
  def availableLocales: Iterable[Locale] = ldmls.map(_._2.toLocale)

  /**
    * Returns the ldml for the given locale
    */
  def ldml(locale: Locale): Option[LDML] = ldmls.get(locale.toLanguageTag)
}
