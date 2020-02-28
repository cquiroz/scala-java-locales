package java.text

import java.util.{ Date, Locale }

class SimpleDateFormat(
  private[this] val pattern: String,
  private[this] var symbols: DateFormatSymbols
) extends DateFormat {
  def this(pattern: String) = this(pattern, DateFormatSymbols.getInstance())

  def this(pattern: String, aLocale: Locale) = this(pattern, DateFormatSymbols.getInstance(aLocale))

  //def this() = this("???", DecimalFormatSymbols.getInstance())

  def format(date: Date): String = {
    val fields = Array(
      (date.getYear + 1900).toString.reverse,
      (date.getMonth + 1).toString.reverse,
      date.getDate.toString.reverse,
      date.getHours.toString.reverse,
      date.getMinutes.toString.reverse,
      date.getSeconds.toString.reverse,
      (date.getTime - Date.UTC(date.getYear,
                               date.getMonth,
                               date.getDay,
                               date.getHours,
                               date.getMinutes,
                               date.getSeconds)).toString.reverse
    )

    def pop(pos: Int): Char =
      if (fields(pos).size > 0) {
        val res = fields(pos)(0)
        fields(pos) = fields(pos).drop(1)
        res
      } else '0'

    var patternPos = pattern.length

    val sb = new StringBuffer()
    while (patternPos > 0) {
      patternPos -= 1

      pattern(patternPos) match {
        case 'G' => ???
        case 'y' => sb.append(pop(0))
        case 'Y' => ???
        case 'M' => sb.append(pop(1))
        case 'w' => ???
        case 'W' => ???
        case 'D' => ???
        case 'd' => sb.append(pop(2))
        case 'F' => ???
        case 'E' => ???
        case 'u' => ???
        case 'a' => ???
        case 'H' => sb.append(pop(3))
        case 'k' => ???
        case 'K' => ???
        case 'h' => ???
        case 'm' => sb.append(pop(4))
        case 's' => sb.append(pop(5))
        case 'S' => sb.append(pop(6))
        case 'z' => ???
        case 'Z' => ???
        case 'X' => ???
        case other =>
          sb.append(other)
      }
    }

    sb.reverse.toString
  }

  override final def format(
    obj:        AnyRef,
    toAppendTo: StringBuffer,
    pos:        FieldPosition
  ): StringBuffer = ???

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
