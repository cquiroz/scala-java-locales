package locales

/**
  * Value objects build out of CLDR XML data
  */
case class Calendar(id: String) {
  val scalaSafeName: String = id.replace("-", "_")
}

case class MonthSymbols(months: Seq[String], shortMonths: Seq[String])
object MonthSymbols {
  val zero = MonthSymbols(Seq.empty, Seq.empty)
}

case class WeekdaysSymbols(weekdays: Seq[String], shortWeekdays: Seq[String])
object WeekdaysSymbols {
  val zero = WeekdaysSymbols(Seq.empty, Seq.empty)
}

case class AmPmSymbols(amPm: Seq[String])
object AmPmSymbols {
  val zero = AmPmSymbols(Seq.empty)
}

case class EraSymbols(eras: Seq[String])
object EraSymbols {
  val zero = EraSymbols(Seq.empty)
}

case class CalendarSymbols(months: MonthSymbols, weekdays: WeekdaysSymbols,
    amPm: AmPmSymbols, eras: EraSymbols)

case class DateTimePattern(patternType: String, pattern: String)

case class CalendarPatterns(datePatterns: List[DateTimePattern], timePatterns: List[DateTimePattern])

object CalendarPatterns {
  val zero = CalendarPatterns(Nil, Nil)
}

case class NumericSystem(id: String, digits: String)

case class NumberSymbols(system: NumericSystem,
    aliasOf: Option[NumericSystem] = None,
    decimal: Option[Char] = None,
    group: Option[Char] = None,
    list: Option[Char] = None,
    percent: Option[Char] = None,
    plus: Option[Char] = None,
    minus: Option[Char] = None,
    perMille: Option[Char] = None,
    infinity: Option[String] = None,
    nan: Option[String] = None,
    exp: Option[String] = None)

object NumberSymbols {
  def alias(system: NumericSystem, aliasOf: NumericSystem): NumberSymbols =
    NumberSymbols(system, aliasOf = Some(aliasOf))
}

// http://www.unicode.org/reports/tr35/tr35-numbers.html#Currencies
case class CurrencyDisplayName(name: String, count: Option[String])
case class CurrencySymbol(symbol: String, alt: Option[String])

case class NumberCurrency(currencyCode: String,
    symbols: Seq[CurrencySymbol],
    displayNames: Seq[CurrencyDisplayName])

// CurrencyData in supplemental/supplementalData.xml that defines currency availability by region
//    and digits/formatting, augment with Numeric Code Mappings & Master Currency Code List
case class CurrencyData(currencyTypes: Seq[CurrencyType],
    fractions: Seq[CurrencyDataFractionsInfo],
    regions: Seq[CurrencyDataRegion],
    numericCodes: Seq[CurrencyNumericCode])

case class CurrencyType(currencyCode: String, currencyName: String)

case class CurrencyNumericCode(currencyCode: String, numericCode: Int)

case class CurrencyDataFractionsInfo(currencyCode: String, digits: Int, rounding: Int,
    cashDigits: Option[Int], cashRounding: Option[Int])

case class CurrencyDataRegion(countryCode: String, currencies: Seq[CurrencyDataRegionCurrency])

case class CurrencyDataRegionCurrency(currencyCode: String,
    from: Option[String], to: Option[String], tender: Option[Boolean])

case class XMLLDMLLocale(language: String, territory: Option[String],
    variant: Option[String], script: Option[String])

case class XMLLDML(locale: XMLLDMLLocale, fileName: String, defaultNS: Option[NumericSystem],
    digitSymbols: Map[NumericSystem, NumberSymbols], calendar: Option[CalendarSymbols],
    datePatterns: Option[CalendarPatterns], currencies: Seq[NumberCurrency]) {

  val scalaSafeName: String = {
    List(Some(locale.language), locale.script, locale.territory, locale.variant)
      .flatten.mkString("_")
  }
}
