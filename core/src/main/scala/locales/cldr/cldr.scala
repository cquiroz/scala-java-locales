package locales.cldr

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
case class CalendarSymbols(months: List[String], shortMonths: List[String],
    weekdays: List[String], shortWeekdays: List[String], amPm: List[String],
    eras: List[String])

case class CalendarPatterns(datePatterns: Map[Int, String], timePatterns: Map[Int, String])

/** Number Currencies */
case class CurrencyDisplayName(name: String, count: Option[String])
case class CurrencySymbol(symbol: String, alt: Option[String])
case class NumberCurrency(currencyCode: String, symbols: Seq[CurrencySymbol], displayNames: Seq[CurrencyDisplayName])

/**
 * Interfaces describing the supplementary currency data
 */
case class CurrencyData(currencyTypes: Seq[CurrencyType],
    fractions: Seq[CurrencyDataFractionsInfo],
    regions: Seq[CurrencyDataRegion],
    numericCodes: Seq[CurrencyNumericCode])

case class CurrencyType(currencyCode: String, currencyName: String)

case class CurrencyNumericCode(currencyCode: String, numericCode: Int)

// currency code "DEFAULT" is used if currency code doesn't exist
case class CurrencyDataFractionsInfo(currencyCode: String, digits: Int, rounding: Int,
    cashDigits: Option[Int], cashRounding: Option[Int])

case class CurrencyDataRegion(countryCode: String, currencies: Seq[CurrencyDataRegionCurrency])

case class CurrencyDataRegionCurrency(currencyCode: String,
    from: Option[String], to: Option[String], tender: Option[Boolean])

/** Number Formatting Patterns */
case class NumberPatterns(decimalPattern: Option[String], percentPattern: Option[String])

/**
 * Interfaces describing an LDML Locale
 */
case class LDMLLocale(language: String, territory: Option[String],
    variant: Option[String], script: Option[String])

/**
 * Wrapper to LDML
 */
case class LDML(parent: Option[LDML],
    locale: LDMLLocale,
    defaultNS: Option[NumberingSystem],
    digitSymbols: List[Symbols] = Nil,
    calendarSymbols: Option[CalendarSymbols],
    calendarPatterns: Option[CalendarPatterns],
    currencies: List[NumberCurrency],
    numberPatterns: NumberPatterns) {

  private val byCurrencyCode: Map[String, NumberCurrency] =
    currencies.groupBy{ _.currencyCode }.map{ case (code, list) => code.toUpperCase -> list.head }

  // Need to lookup the symbol & description independently
  def getNumberCurrencySymbol(currencyCode: String): Seq[CurrencySymbol] = {
    (
      byCurrencyCode.get(currencyCode.toUpperCase).filter{ _.symbols.nonEmpty }.map{ _.symbols } orElse
      parent.map{ _.getNumberCurrencySymbol(currencyCode) }
    ).getOrElse(IndexedSeq.empty)
  }

  def getNumberCurrencyDescription(currencyCode: String): Seq[CurrencyDisplayName] = {
    (
      byCurrencyCode.get(currencyCode.toUpperCase).filter{ _.displayNames.nonEmpty }.map{ _.displayNames } orElse
      parent.map{ _.getNumberCurrencyDescription(currencyCode) }
    ).getOrElse(IndexedSeq.empty)
  }

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
