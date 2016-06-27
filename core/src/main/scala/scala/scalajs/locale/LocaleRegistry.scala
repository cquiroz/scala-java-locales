package scala.scalajs.locale

import java.text.DecimalFormatSymbols
import java.util.Locale

import scala.collection.{Map, mutable}
import scala.scalajs.locale.ldml.LDML
import scala.scalajs.locale.ldml.data.minimal

/**
  * Implements a database of locales
  */
object LocaleRegistry {

  private var defaultLocale: Option[Locale] = None
  private var defaultPerCategory: Map[Locale.Category, Option[Locale]] =
    Locale.Category.values().map(_ -> None).toMap

  // The spec requires some locales by default
  lazy val en: LDML         = minimal.en
  lazy val fr: LDML         = minimal.fr
  lazy val de: LDML         = minimal.de
  lazy val it: LDML         = minimal.it
  lazy val ja: LDML         = minimal.ja
  lazy val ko: LDML         = minimal.ko
  lazy val zh: LDML         = minimal.zh
  // The JVM uses Chinese without script unlike CLDR
  lazy val zh_Hans_CN: LDML = minimal.zh_Hans_CN
    .copy(locale = minimal.zh_Hans_CN.locale.copy(script = None))
  lazy val zh_Hant_TW: LDML = minimal.zh_Hant_TW
    .copy(locale = minimal.zh_Hant_TW.locale.copy(script = None))
  lazy val fr_FR: LDML      = minimal.fr_FR
  lazy val de_DE: LDML      = minimal.de_DE
  lazy val it_IT: LDML      = minimal.it_IT
  lazy val ja_JP: LDML      = minimal.ja_JP
  lazy val ko_KR: LDML      = minimal.ko_KR
  lazy val en_GB: LDML      = minimal.en_GB
  lazy val en_US: LDML      = minimal.en_US
  lazy val en_CA: LDML      = minimal.en_CA
  lazy val fr_CA: LDML      = minimal.fr_CA
  lazy val root: LDML       = minimal.root

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
    defaultLocale = None
    defaultPerCategory =
        Locale.Category.values().map(_ -> None).toMap
    ldmls.empty
    initDefaultLocales()
  }

  private def initDefaultLocales(): Unit = {
    // Initialize
    defaultLocales.foreach {
      case (_, l) => installLocale(l)
    }
  }

  def default: Locale = defaultLocale
    .getOrElse(throw new IllegalStateException("No default locale set"))

  def default(category: Locale.Category): Locale = {
    if (category == null) {
      throw new NullPointerException("Argument cannot be null")
    } else {
      defaultPerCategory.get(category).flatten
        .getOrElse(throw new IllegalStateException(s"No default locale set for category $category"))
    }
  }

  def setDefault(newLocale: Locale): Unit = {
    defaultLocale = Some(newLocale)
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
  def ldml(locale: Locale): Option[LDML] = ldmls.get(locale.toLanguageTag())
}
