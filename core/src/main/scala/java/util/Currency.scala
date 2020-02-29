package java.util

import scala.collection.{ Map => SMap, Set => SSet }
import scala.collection.JavaConverters._
import locales.cldr.{ CurrencyDataFractionsInfo, CurrencyType }

object Currency {
  private val countryCodeToCurrencyCodeMap: SMap[String, String] =
    LocalesDb.currencydata.regions.map { r =>
      r.countryCode -> r.currencies
        .find(_.to.isEmpty)
        .orElse(r.currencies.headOption)
        .map(_.currencyCode)
        .get
    }.toMap

  private val all: SSet[Currency] = LocalesDb.currencydata.currencyTypes.map {
    currencyType: CurrencyType =>
      val fractions: CurrencyDataFractionsInfo =
        LocalesDb.currencydata.fractions
          .find(_.currencyCode == currencyType.currencyCode)
          .orElse(LocalesDb.currencydata.fractions.find(_.currencyCode == "DEFAULT"))
          .get

      val numericCode: Int =
        LocalesDb.currencydata.numericCodes
          .find(_.currencyCode == currencyType.currencyCode)
          .map(_.numericCode)
          .getOrElse(0)

      Currency(
        currencyType.currencyCode,
        numericCode,
        fractions.digits,
        currencyType.currencyName,
        None
      )
  }.toSet

  private val currencyCodeMap: SMap[String, Currency] =
    all.toSeq.groupBy(_.getCurrencyCode).map {
      case (currencyCode: String, matches: Seq[Currency]) => currencyCode -> matches.head
    }

  def getAvailableCurrencies(): java.util.Set[Currency] = all.asJava

  // NullPointerException - if locale or its country code is null
  // IllegalArgumentException - if the country of the given locale is not a supported ISO 3166 country code.
  def getInstance(locale: Locale): Currency = {
    if (locale.getCountry == null || locale.getCountry.isEmpty) throw new NullPointerException

    countryCodeToCurrencyCodeMap
      .get(locale.getCountry)
      .flatMap(currencyCodeMap.get)
      .getOrElse(
        throw new IllegalArgumentException(s"No currency available for ${locale.toLanguageTag}")
      )
  }

  def getInstance(currencyCode: String): Currency = currencyCodeMap(currencyCode)
}

final case class Currency private (
  currencyCode:   String,
  numericCode:    Int,
  fractionDigits: Int,
  defaultName:    String,
  currencyLocale: Option[Locale]
) {

  def defaultLocale: Locale = currencyLocale.getOrElse(Locale.getDefault)

  // Gets the ISO 4217 currency code of this currency.
  def getCurrencyCode(): String = currencyCode

  // Gets the default number of fraction digits used with this currency.
  def getDefaultFractionDigits(): Int = fractionDigits

  // Gets the name that is suitable for displaying this currency for the default DISPLAY locale.
  def getDisplayName(): String = getDisplayName(defaultLocale)

  // Gets the name that is suitable for displaying this currency for the specified locale.
  def getDisplayName(locale: Locale): String =
    LocalesDb
      .ldml(locale)
      .flatMap { ldml =>
        ldml.getNumberCurrencyDescription(currencyCode).find(_.count.isEmpty).map(_.name)
      }
      .getOrElse(currencyCode)

  // Returns the ISO 4217 numeric code of this currency.
  def getNumericCode(): Int = numericCode

  // Gets the symbol of this currency for the default DISPLAY locale.
  def getSymbol(): String = getSymbol(defaultLocale)

  // Gets the symbol of this currency for the specified locale.
  def getSymbol(locale: Locale): String =
    LocalesDb
      .ldml(locale)
      .flatMap { ldml =>
        val symbols = ldml.getNumberCurrencySymbol(currencyCode)

        // TODO: might need a more sophisticated symbol-matcher
        // The tests from the JVM indicate we prefer the "wide" over "narrow" symbol

        symbols
          .find(_.alt.isEmpty)
          .orElse(symbols.find(_.alt.exists(_ == "narrow")))
          .map(_.symbol)
      }
      .getOrElse(currencyCode)

  // Returns the ISO 4217 currency code of this currency.
  override def toString(): String = currencyCode
}
