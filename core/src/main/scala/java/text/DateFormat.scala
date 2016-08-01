package java.text

import java.util.Locale

import locales.LocaleRegistry
import locales.cldr.{CalendarPatterns, LDML}

abstract class DateFormat protected () extends Format {
  // override final def format(obj: AnyRef, toAppendTo: StringBuffer, pos: FieldPosition): StringBuffer = ???
  // def format(date: Date, toAppendTo: StringBuffer, pos: FieldPosition): StringBuffer = ???

  // TODO implement
  // final def format(date: Date): String = ???
  // def parse(source: String, parsePosition: ParsePosition): Date = ???
  // def parse(source: String): Date = ???
  // override final def parseObject(source: String, pos: ParsePosition): AnyRef = ???
  // def setCalendar(newCalendar: Calendar): Unit = ???
  // def getCalendar(): Calendar = ???
  // def setNumberFormat(newNumberFormat: NumberFormat): Unit = ???
  // def getNumberFormat(): NumberFormat = ???
  // def setTimeZone(zone: TimeZone): Unit = ???
  // def getTimeZone(): TimeZone = ???
  // def setLenient(lenient: Boolean): Unit = ???
  // def isLenient(): Boolean = ???
  // override def hashCode(): Int = ???
  // override def equals(obj: Any): Boolean = ???
  // override def clone(): Any = ???
}

object DateFormat {
  val ERA_FIELD: Int = 0
  val YEAR_FIELD: Int = 1
  val MONTH_FIELD: Int = 2
  val DATE_FIELD: Int = 3
  val HOUR_OF_DAY1_FIELD: Int = 4
  val HOUR_OF_DAY0_FIELD: Int = 5
  val MINUTE_FIELD: Int = 6
  val SECOND_FIELD: Int = 7
  val MILLISECOND_FIELD: Int = 8
  val DAY_OF_WEEK_FIELD: Int = 9
  val DAY_OF_YEAR_FIELD: Int = 10
  val DAY_OF_WEEK_IN_MONTH_FIELD: Int = 11
  val WEEK_OF_YEAR_FIELD: Int = 12
  val WEEK_OF_MONTH_FIELD: Int = 13
  val AM_PM_FIELD: Int = 14
  val HOUR1_FIELD: Int = 15
  val HOUR0_FIELD: Int = 16
  val TIMEZONE_FIELD: Int = 17

  val FULL: Int = 0
  val LONG: Int = 1
  val MEDIUM: Int = 2
  val SHORT: Int = 3
  val DEFAULT: Int = 2

  private def patternsR(ldml: LDML, get: CalendarPatterns => Option[String]): Option[String] =
    ldml.calendarPatterns.flatMap(get).orElse(ldml.parent.flatMap(patternsR(_, get)))

  final def getTimeInstance(): DateFormat = getTimeInstance(DEFAULT)

  final def getTimeInstance(style: Int): DateFormat =
    getTimeInstance(style, Locale.getDefault(Locale.Category.FORMAT))

  final def getTimeInstance(style: Int, aLocale: Locale): DateFormat =
    LocaleRegistry.ldml(aLocale).flatMap { ldml =>
      val ptrn = patternsR(ldml, _.timePatterns.get(style))
      ptrn.map(new SimpleDateFormat(_, aLocale))
    }.getOrElse(new SimpleDateFormat("", aLocale))

  final def getDateInstance(): DateFormat = getDateInstance(DEFAULT)

  final def getDateInstance(style: Int): DateFormat =
    getDateInstance(style, Locale.getDefault(Locale.Category.FORMAT))

  final def getDateInstance(style: Int, aLocale: Locale): DateFormat =
    LocaleRegistry.ldml(aLocale).flatMap { ldml =>
      val ptrn = patternsR(ldml, _.datePatterns.get(style))
      ptrn.map(new SimpleDateFormat(_, aLocale))
    }.getOrElse(new SimpleDateFormat("", aLocale))

  final def getDateTimeInstance(): DateFormat = getDateInstance(DEFAULT)

  final def getDateTimeInstance(dateStyle: Int, timeStyle: Int): DateFormat =
    getDateTimeInstance(dateStyle, timeStyle, Locale.getDefault(Locale.Category.FORMAT))

  final def getDateTimeInstance(dateStyle: Int, timeStyle: Int, aLocale: Locale): DateFormat =
    LocaleRegistry.ldml(aLocale).flatMap { ldml =>
      val datePtrn = patternsR(ldml, _.datePatterns.get(dateStyle))
      val timePtrn = patternsR(ldml, _.timePatterns.get(timeStyle))
      (datePtrn, timePtrn) match {
        case (Some(d), Some(t)) => Some(new SimpleDateFormat(s"$d $t", aLocale))
        case (Some(d), None) => Some(new SimpleDateFormat(s"$d", aLocale))
        case (None, Some(t)) => Some(new SimpleDateFormat(s"$t", aLocale))
        case _ => Some(new SimpleDateFormat("", aLocale))
      }
    }.getOrElse(new SimpleDateFormat("", aLocale))

  final def getInstance(): DateFormat =
    getDateTimeInstance(SHORT, SHORT)

  def getAvailableLocales(): Array[Locale] = Locale.getAvailableLocales
}
