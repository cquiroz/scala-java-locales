package java.text

import java.util.Locale

class SimpleDateFormat(private[this] val pattern: String,
   private[this] var symbols: DateFormatSymbols) extends DateFormat {
  def this(pattern: String) = this(pattern, DateFormatSymbols.getInstance())

  def this(pattern: String, aLocale: Locale) = this(pattern, DateFormatSymbols.getInstance(aLocale))

  //def this() = this("???", DecimalFormatSymbols.getInstance())

  override final def format(obj: AnyRef, toAppendTo: StringBuffer, pos: FieldPosition): StringBuffer = ???

  override final def parseObject(source: String, pos: ParsePosition): AnyRef = ???

  // def set2DigitYearStart(startDate: Date): Unit = ???
  // def get2DigitYearStart(): Date = ???
  // override final def format(date: Date, toAppendTo: StringBuffer, pos: FieldPosition): StringBuffer = ???
  // def formatToCharacterIterator(obj: AnyRef): AttributedCharacterIterator =
  // def parse(text: String, pos: ParsePosition): Date = ???
  def toPattern(): String = pattern
  // def toLocalizedPattern(): String = pattern
  // def applyPattern(pattern: String): Unit = ???
  // def applyLocalizedPattern(pattern: String): Unit = ???
  def getDateFormatSymbols(): DateFormatSymbols = symbols

  def setDateFormatSymbols(symbols: DateFormatSymbols): Unit =
    this.symbols = symbols

  // override def clone(): Any = ???
  // override def hashCode(): Int = ???
  // override def equals(obj: Any): Boolean = ???
}
