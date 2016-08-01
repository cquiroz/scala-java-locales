package java.text

import java.util.Locale

import locales.LocaleRegistry
import locales.cldr.{LDML, NumberPatterns}

abstract class NumberFormat protected () extends Format {
  def parseObject(source: String, pos: ParsePosition): AnyRef

  override def format(obj: AnyRef, toAppendTo: StringBuffer, pos: FieldPosition): StringBuffer = ???

  // TODO implement
  // final def format(number: Double): String = ???
  // final def format(number: Long): String = ???
  // def format(number: Double, toAppendTo: StringBuffer, pos: FieldPosition): StringBuffer = ???
  // def format(number: Long, toAppendTo: StringBuffer, pos: FieldPosition): StringBuffer = ???
  // def parse(source: String, parsePosition: ParsePosition): Number = ???
  // def parse(source: String): Number = ???
  // def isParseIntegerOnly(): Boolean = ???
  // def setParseIntegerOnly(value: Boolean): Unit = ???
  // override def hashCode(): Int = ???
  // override def equals(obj: Any): Boolean = ???
  // override def clone(): Any = ???
  // def isGroupingUsed(): Boolean = ???
  // def setGroupingUsed(newValue: Boolean): Unit = ???
  // def getMaximumIntegerDigits(): Int = ???
  // def setMaximumIntegerDigits(newValue: Int): Unit = ???
  // def getMinimumIntegerDigits(): Int = ???
  // def setMinimumIntegerDigits(newValue: Int): Unit = ???
  // def getMaximumFractionDigits(): Int = ???
  // def setMaximumFractionDigits(newValue: Int): Unit = ???
  // def getMinimumFractionDigits(): Int = ???
  // def setMinimumFractionDigits(newValue: Int): Unit = ???
  // def getCurrency(): Currency = ???
  // def setCurrency(currency: Currency): Unit = ???
  // def getRoundingMode(): RoundingMode = ???
  // def setRoundingMode(currency: RoundingMode): Unit = ???
}

object NumberFormat {
  val INTEGER_FIELD: Int = 0
  val FRACTION_FIELD: Int = 1

  private def patternsR(ldml: LDML, get: NumberPatterns => Option[String]): Option[String] =
    get(ldml.numberPatterns).orElse(ldml.parent.flatMap(patternsR(_, get)))

  final def getInstance(): NumberFormat = getNumberInstance()

  def getInstance(inLocale: Locale): NumberFormat = getNumberInstance(inLocale)

  final def getNumberInstance(): NumberFormat = getInstance(Locale.getDefault(Locale.Category.FORMAT))

  def getNumberInstance(inLocale: Locale): NumberFormat =
    LocaleRegistry.ldml(inLocale).flatMap { ldml =>
      val ptrn = patternsR(ldml, _.decimalPattern)
      ptrn.map(new DecimalFormat(_, DecimalFormatSymbols.getInstance(inLocale)))
    }.getOrElse(new DecimalFormat("", DecimalFormatSymbols.getInstance(inLocale)))

  //final def getIntegerInstance(): NumberFormat = ???
  //def getIntegerInstance(inLocale: Locale): NumberFormat = ???
  //final def getCurrencyInstance(): NumberFormat = ???
  //def getCurrencyInstance(inLocale: Locale): NumberFormat = ???

  final def getPercentInstance(): NumberFormat = getPercentInstance(Locale.getDefault(Locale.Category.FORMAT))
  def getPercentInstance(inLocale: Locale): NumberFormat =
    LocaleRegistry.ldml(inLocale).flatMap { ldml =>
      val ptrn = patternsR(ldml, _.percentPattern)
      ptrn.map(new DecimalFormat(_, DecimalFormatSymbols.getInstance(inLocale)))
    }.getOrElse(new DecimalFormat("", DecimalFormatSymbols.getInstance(inLocale)))

  //def getAvailableLocales(): Array[Locale] = ???
}
