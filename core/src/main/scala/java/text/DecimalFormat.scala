package java.text

import locales.DecimalFormatUtil._

class DecimalFormat(private[this] val pattern: String, private[this] var symbols: DecimalFormatSymbols)
    extends NumberFormat {

  private val patterns = toDecimalPatterns(pattern)

  private var positivePrefix: String = localizeString(patterns.positive.prefix, symbols)
  private var negativePrefix: String =
    if (patterns.negative.prefix.isEmpty) {
      symbols.getMinusSign.toString
    } else {
      if (!patterns.negative.prefix.contains(PatternCharMinus)) {
        localizeString("-" + patterns.negative.prefix, symbols)
      } else {
        localizeString(patterns.negative.prefix, symbols)
      }
    }
  private var positiveSuffix: String = localizeString(patterns.positive.suffix, symbols)
  private var negativeSuffix: String = localizeString(patterns.negative.suffix, symbols)
  private var multiplier: Int = 1
  private var groupingSize: Int = groupingCount(patterns.positive.pattern)
  private var decimalSeparatorAlwaysShown: Boolean = false
  private var parseBigDecimal: Boolean = false

  def this(pattern: String) = this(pattern, DecimalFormatSymbols.getInstance())

  def this() = this("???", DecimalFormatSymbols.getInstance())

  override def format(number: Double, toAppendTo: StringBuffer, pos: FieldPosition): StringBuffer = ???

  override def format(number: Long, toAppendTo: StringBuffer, pos: FieldPosition): StringBuffer = {
    val result = if (number == 0) new StringBuffer("0") else new StringBuffer("")
    val negative = number < 0
    // Imperative way to do it
    var remainder = if (negative) -number else number
    var pos = result.length()
    while (remainder != 0 || pos < getMinimumIntegerDigits) {
      val leftover = remainder % 10
      val t = (leftover + (leftover >> 31)) ^ (leftover >> 31)
      if (pos < getMaximumIntegerDigits) {
        result.append(t.toString)
      }
      if (getMaximumIntegerDigits == 0) {
        result.append("0")
        remainder = 0
      } else {
        pos += 1
        if (pos < getMaximumIntegerDigits && isGroupingUsed && pos % getGroupingSize() == 0 && ((remainder + (remainder >> 31)) ^ (remainder >> 31)) >= 10) {
          result.append(getDecimalFormatSymbols().getGroupingSeparator)
        } else

        if (remainder < 10 && pos < getMinimumIntegerDigits && pos % getGroupingSize() == 0) {
          result.append(getDecimalFormatSymbols().getGroupingSeparator)
        }
        remainder = remainder / 10
      }
    }
    if (negative) {
      result.append(getNegativePrefix())
    }
    result.reverse()
  }

  override def format(number: Long): String = format(number, new StringBuffer, new FieldPosition(0)).toString

  def parse(source: String, parsePosition: ParsePosition): Number = ???

  def parseObject(source: String, pos: ParsePosition): AnyRef = ???

  // TODO implement
  //def format(number: Double, toAppendTo: StringBuffer, pos: FieldPosition): StringBuffer = ???
  //def format(number: Long, toAppendTo: StringBuffer, pos: FieldPosition): StringBuffer = ???
  //def parse(source: String, parsePosition: ParsePosition): Number = ???
  //def parse(source: String): Number = ???

  def getDecimalFormatSymbols(): DecimalFormatSymbols = symbols

  def setDecimalFormatSymbols(symbols: DecimalFormatSymbols): Unit =
    this.symbols = symbols

  def getPositivePrefix(): String = this.positivePrefix

  def setPositivePrefix(newValue: String): Unit = this.positivePrefix = newValue

  def getNegativePrefix(): String = this.negativePrefix

  def setNegativePrefix(newValue: String): Unit = this.negativePrefix = newValue

  def getPositiveSuffix(): String = this.positiveSuffix

  def setPositiveSuffix(newValue: String): Unit = this.positiveSuffix = newValue

  def getNegativeSuffix(): String = this.negativeSuffix

  def setNegativeSuffix(newValue: String): Unit = this.negativeSuffix = newValue

  def getMultiplier(): Int = this.multiplier

  def setMultiplier(newValue: Int): Unit = this.multiplier = newValue

  // override def setGroupingUsed(newValue: Boolean): Unit = ???

  def getGroupingSize(): Int = this.groupingSize

  def setGroupingSize(newValue: Int): Unit = this.groupingSize = newValue

  def isDecimalSeparatorAlwaysShown(): Boolean = this.decimalSeparatorAlwaysShown

  def setDecimalSeparatorAlwaysShown(newValue: Boolean): Unit =
    this.decimalSeparatorAlwaysShown = newValue

  def isParseBigDecimal(): Boolean = this.parseBigDecimal

  def setParseBigDecimal(newValue: Boolean): Unit = this.parseBigDecimal = newValue
  // override def clone(): Any = ???
  // override def hashCode(): Int = ???
  // override def equals(obj: Any): Boolean = ???
  def toPattern(): String = pattern
  // def toLocalizedPattern(): String = pattern
  // def applyPattern(pattern: String): Unit = ???
  // def setMaximumIntegerDigits(newValue: Int): Unit = ???
  // def setMinimumIntegerDigits(newValue: Int): Unit = ???
  // def setMaximumFractionDigits(newValue: Int): Unit = ???
  // def setMinimumFractionDigits(newValue: Int): Unit = ???
  // def getMaximumIntegerDigits(): Int = ???
  // def getMinimumIntegerDigits(): Int = ???
  // def getMaximumFractionDigits(): Int = ???
  // def getMinimumFractionDigits(): Int = ???
  // def getCurrency(): Currency = ???
  // def setCurrency(currency: Currency): Unit = ???
  // def getRoundingMode(): RoundingMode = ???
  // def setRoundingMode(currency: RoundingMode): Unit = ???
}
