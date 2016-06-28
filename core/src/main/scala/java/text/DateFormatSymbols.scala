package java.text

import java.util.Locale

import scala.scalajs.locale.LocaleRegistry
import scala.scalajs.locale.ldml.{LDML, NumberingSystem, Symbols}

object DateFormatSymbols {

  def getAvailableLocales(): Array[Locale] = Locale.getAvailableLocales

  def getInstance(): DateFormatSymbols =
    getInstance(Locale.getDefault(Locale.Category.FORMAT))

  def getInstance(locale: Locale): DateFormatSymbols =
    initialize(locale, new DateFormatSymbols(locale))

  private def initialize(locale: Locale,
      dfs: DateFormatSymbols): DateFormatSymbols = {

    LocaleRegistry
      .ldml(locale)
      .map(l => dfs)
      .getOrElse(dfs)
  }
}

class DateFormatSymbols(private[this] val locale: Locale)
    extends Cloneable {
  private[this] var eras: Array[String] = Array()
  private[this] var months: Array[String] = Array()
  private[this] var shortMonths: Array[String] = Array()
  private[this] var weekdays: Array[String] = Array()
  private[this] var shortWeekdays: Array[String] = Array()
  private[this] var amPmStrings: Array[String] = Array()
  private[this] var zoneStrings: Array[Array[String]] = Array()
  private[this] var localPatternChars: String = null

  DateFormatSymbols.initialize(locale, this)

  def this() = this(Locale.getDefault(Locale.Category.FORMAT))

  def getEras(): Array[String] = eras

  def setEras(eras: Array[String]): Unit = this.eras = eras

  def getMonths(): Array[String] = months

  def setMonths(months: Array[String]): Unit =
    this.months = months

  def getShortMonths(): Array[String] = shortMonths

  def setShortMonths(shortMonths: Array[String]): Unit =
    this.shortMonths = shortMonths

  def getWeekdays(): Array[String] = weekdays

  def setWeekdays(weekdays: Array[String]): Unit =
    this.weekdays = weekdays

  def getShortWeekdays(): Array[String] = shortWeekdays

  def setShortWeekdays(shortWeekdays: Array[String]): Unit =
    this.shortWeekdays = shortWeekdays

  def getAmPmStrings(): Array[String] = amPmStrings

  def setAmPmStrings(amPmStrings: Array[String]): Unit =
    this.amPmStrings = amPmStrings

  def getZoneStrings(): Array[Array[String]] = zoneStrings

  def setZoneStrings(zoneStrings: Array[Array[String]]): Unit =
    this.zoneStrings = zoneStrings

  def getLocalPatternChars(): String = localPatternChars

  def setLocalPatternChars(localPatternChars: String): Unit =
    this.localPatternChars = localPatternChars
}
