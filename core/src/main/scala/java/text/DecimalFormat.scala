package java.text

class DecimalFormat(private[this] val pattern: String, private[this] var symbols: DecimalFormatSymbols)
    extends NumberFormat {

  private var positivePrefix: String = ""
  private var negativePrefix: String = "-"
  private var positiveSuffix: String = ""
  private var negativeSuffix: String = ""
  private var multiplier: Int = 1

  def this(pattern: String) = this(pattern, DecimalFormatSymbols.getInstance())

  def this() = this("???", DecimalFormatSymbols.getInstance())

  override final def format(obj: AnyRef, toAppendTo: StringBuffer, pos: FieldPosition): StringBuffer = ???

  def format(number: Double, toAppendTo: StringBuffer, pos: FieldPosition): StringBuffer = ???

  def format(number: Long, toAppendTo: StringBuffer, pos: FieldPosition): StringBuffer = ???

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
  // def getGroupingSize(): Int = ???
  // def setGroupingSize(newValue: Int): Unit = ???
  // def isDecimalSeparatorAlwaysShown(): Boolean = ???
  // def setDecimalSeparatorAlwaysShown(newValue: Boolean): Unit = ???
  // def isParseBigDecimal(): Boolean = ???
  // def setParseBigDecimal(newValue: Boolean): Unit = ???
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
