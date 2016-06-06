package scala.scalajs

import java.text.DecimalFormatSymbols
import java.util.Locale

import scala.collection.Map
import locale.ldml.{LDML, LDMLLocale}

/**
  * Implements a database of locales
  */
object LocaleRegistry {

  private var defaultLocale: Option[Locale] = None
  private var defaultPerCategory: Map[Locale.Category, Option[Locale]] =
    Locale.Category.values().map(_ -> None).toMap

  // The spec requires some locales by default

  // Auto generated code, don't change
  val en: LDML         = LDML(LDMLLocale("en", None, None, None))
  val fr: LDML         = LDML(LDMLLocale("fr", None, None, None))
  val de: LDML         = LDML(LDMLLocale("de", None, None, None))
  val it: LDML         = LDML(LDMLLocale("it", None, None, None))
  val ja: LDML         = LDML(LDMLLocale("ja", None, None, None))
  val ko: LDML         = LDML(LDMLLocale("ko", None, None, None))
  val zh: LDML         = LDML(LDMLLocale("zh", None, None, None))
  val zh_CN_Hans: LDML = LDML(LDMLLocale("zh", Some("CN"), None, Some("Hans")))
  val zh_TW_Hant: LDML = LDML(LDMLLocale("zh", Some("TW"), None, Some("Hant")))
  val fr_FR: LDML      = LDML(LDMLLocale("fr", Some("FR"), None, None))
  val de_DE: LDML      = LDML(LDMLLocale("de", Some("DE"), None, None))
  val it_IT: LDML      = LDML(LDMLLocale("it", Some("IT"), None, None))
  val ja_JP: LDML      = LDML(LDMLLocale("ja", Some("JP"), None, None))
  val ko_KR: LDML      = LDML(LDMLLocale("ko", Some("KR"), None, None))
  val en_GB: LDML      = LDML(LDMLLocale("en", Some("GB"), None, None))
  val en_US: LDML      = LDML(LDMLLocale("en", Some("US"), None, None))
  val en_CA: LDML      = LDML(LDMLLocale("en", Some("CA"), None, None))
  val fr_CA: LDML      = LDML(LDMLLocale("fr", Some("CA"), None, None))

  case class LocaleCldr(locale: Locale,
      decimalFormatSymbol: Option[DecimalFormatSymbols])

  private val defaultLocales: Map[String, LDML] = Map(
    en.languageTag -> en,
    fr.languageTag -> fr,
    de.languageTag -> de,
    it.languageTag -> it,
    ja.languageTag -> ja,
    ko.languageTag -> ko,
    zh.languageTag -> zh,
    zh_CN_Hans.languageTag -> zh_CN_Hans,
    zh_TW_Hant.languageTag -> zh_TW_Hant,
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

  private var locales: Map[String, LDML] = Map.empty

  def default: Locale = defaultLocale
    .getOrElse(throw new IllegalStateException("No default locale set"))

  def default(category: Locale.Category): Locale = {
    if (category == null) throw new NullPointerException("Argument cannot be null")
    else defaultPerCategory.get(category).flatten
      .getOrElse(throw new IllegalStateException(s"No default locale set for category $category"))
  }

  def setDefault(newLocale: Locale): Unit = {
    defaultLocale = Some(newLocale)
    defaultPerCategory = Locale.Category.values().map(_ -> Some(newLocale)).toMap
  }

  def setDefault(category: Locale.Category, newLocale: Locale): Unit = {
    if (category == null || newLocale == null) throw new NullPointerException("Argument cannot be null")
    else defaultPerCategory = defaultPerCategory + (category -> Some(newLocale))
  }

  /**
    * Attempts to give a Locale for the given tag if available
    */
  def localeForLanguageTag(languageTag: String): Option[Locale] = {
    // TODO Support alternative tags for the same locale
    (defaultLocales ++ locales).get(languageTag).map(_.toLocale)
  }

  def availableLocales:Iterable[Locale] =
    (defaultLocales ++ locales).map(_._2.toLocale)

  /**
    * Attempts to give a Locale for the given tag if avaibale
    */
  def decimalFormatSymbol(locale: Locale): Option[DecimalFormatSymbols] = ???

  /**
    * Cleans the registry, useful for testing
    */
  def resetRegistry(): Unit = {
    defaultLocale = None
    defaultPerCategory =
        Locale.Category.values().map(_ -> None).toMap
    //locales = Map.empty
  }

  def installLocale(json: String): Unit = {
    // TODO Support all the options for unicode, including variants, numeric regions, etc
    val simpleLocaleRegex = "([a-zA-Z]{2,3})[-_]([a-zA-Z]{2})?.*".r

    /*val localeJson = js.JSON.parse(json).asInstanceOf[CLDR]

    // Read basic locale data
    val localeName = localeJson.locale.toString
    val locale = localeName match {
      case simpleLocaleRegex(lang, region) => Some(new Locale(lang, region, ""))
      case _                               => None
    }*/

    /*val dfs = if (localeJson.number.nu.contains("latn")) {
      // Uses latin numbers
      val zeroSign = '0'
      val decimal = localeJson.number.symbols.latn.decimal.charAt(0)
      val negativeSign = localeJson.number.symbols.latn.minusSign.charAt(0)

      val decimalFormatSymbol = new DecimalFormatSymbols()
      decimalFormatSymbol.setZeroDigit(zeroSign)
      decimalFormatSymbol.setDecimalSeparator(decimal)
      decimalFormatSymbol.setMinusSign(negativeSign)
      Some(decimalFormatSymbol)
    } else {
      None
    }*/

    /*locale.foreach {l =>
      locales = locales + (localeName -> LocaleCldr(l, dfs)) + (localeName.replaceAll("-", "_") -> LocaleCldr(l, dfs))
    }*/

  }
}
