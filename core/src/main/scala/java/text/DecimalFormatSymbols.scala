package java.text

import java.util.Locale

import scala.scalajs.LocaleRegistry

object DecimalFormatSymbols {

  def getAvailableLocales():Array[Locale] = Array.empty

  def getInstance():DecimalFormatSymbols =
    getInstance(Locale.getDefault(Locale.Category.FORMAT))

  def getInstance(locale: Locale):DecimalFormatSymbols =
    LocaleRegistry.decimalFormatSymbol(locale).getOrElse(throw new IllegalArgumentException("Unknown locale"))
}

class DecimalFormatSymbols(locale: Locale) {

  def this() = this(Locale.getDefault(Locale.Category.FORMAT))

  private[this] var zeroDigit: Option[Char] = None
  private[this] var minusSign: Option[Char] = None
  private[this] var decimalSeparator: Option[Char] = None
  private[this] var groupingSeparator: Option[Char] = None
  private[this] var perMill: Option[Char] = None
  private[this] var percent: Option[Char] = None
  private[this] var digit: Option[Char] = None
  private[this] var patternSeparator: Option[Char] = None
  private[this] var infinity: Option[String] = None
  private[this] var nan: Option[String] = None

  def getZeroDigit(): Char = zeroDigit.getOrElse(0)

  def setZeroDigit(zeroDigit: Char): Unit =
    this.zeroDigit = Some(zeroDigit)

  def getGroupingSeparator(): Char = groupingSeparator.getOrElse(0)

  def setGroupingSeparator(groupingSeparator: Char): Unit =
    this.groupingSeparator = Some(groupingSeparator)

  def getDecimalSeparator(): Char = decimalSeparator.getOrElse(0)

  def setDecimalSeparator(decimalSeparator: Char): Unit =
    this.decimalSeparator = Some(decimalSeparator)

  def getPerMill(): Char = perMill.getOrElse(0)

  def setPerMill(perMill: Char): Unit =
    this.perMill = Some(perMill)

  def getPercent(): Char = percent.getOrElse(0)

  def setPercent(percent: Char): Unit =
    this.percent = Some(percent)

  def getDigit(): Char = digit.getOrElse(0)

  def setDigit(digit: Char): Unit =
    this.digit = Some(digit)

  def getPatternSeparator(): Char = patternSeparator.getOrElse(0)

  def setPatternSeparator(patternSeparator: Char): Unit =
    this.patternSeparator = Some(patternSeparator)

  def getInfinity(): String = infinity.getOrElse("")

  def setInfinity(infinity: String): Unit =
    this.infinity = Some(infinity)

  def getNaN(): String = nan.getOrElse("")

  def setNaN(nan: String): Unit =
    this.nan = Some(nan)

  def getMinusSign(): Char = minusSign.getOrElse(0)

  def setMinusSign(minusSign: Char): Unit =
    this.minusSign = Some(minusSign)

  // TODO Implement currency methods
  //def getCurrencySymbol(): String

  //def setCurrencySymbol(currency: String): Unit

  //def getInternationalCurrencySymbol(): String

  //def setInternationalCurrencySymbol(currency: String): Unit

  //def getCurrency(): Currency

  //def setCurrency(currency: Currency): Unit

  //def getMonetaryDecimalSeparator(): Char

  //def setMonetaryDecimalSeparator(sep: Char): Unit

}
