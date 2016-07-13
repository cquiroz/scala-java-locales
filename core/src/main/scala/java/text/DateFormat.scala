package java.text

import java.util.Locale

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

  val FULL: Int = 1
  val LONG: Int = 2
  val MEDIUM: Int = 3
  val SHORT: Int = 4
  val DEFAULT: Int = 2

  final def getTimeInstance(): DateFormat = ???
  final def getTimeInstance(style: Int): DateFormat = ???
  final def getTimeInstance(style: Int, aLocale: Locale): DateFormat = ???
  final def getDateInstance(): DateFormat = ???
  final def getDateInstance(style: Int): DateFormat = ???
  final def getDateInstance(style: Int, aLocale: Locale): DateFormat = ???
  final def getDateTimeInstance(): DateFormat = ???
  final def getDateTimeInstance(style: Int): DateFormat = ???
  final def getDateTimeInstance(style: Int, aLocale: Locale): DateFormat = ???
  final def getInstance(style: Int, aLocale: Locale): DateFormat = ???
  def getAvailableLocales(): Array[Locale] = ???
}
