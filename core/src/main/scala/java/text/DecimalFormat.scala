package java.text

class DecimalFormat(private[this] val pattern: String, private[this] var symbols: DecimalFormatSymbols)
    extends NumberFormat {
  def this(pattern: String) = this(pattern, DecimalFormatSymbols.getInstance())

  def this() = this("???", DecimalFormatSymbols.getInstance())

  override final def format(obj: AnyRef, toAppendTo: StringBuffer, pos: FieldPosition): StringBuffer = ???

  override def parseObject(source: String, pos: ParsePosition): AnyRef = ???

  // TODO implement
  //def format(number: Double, toAppendTo: StringBuffer, pos: FieldPosition): StringBuffer = ???
  //def format(number: Long, toAppendTo: StringBuffer, pos: FieldPosition): StringBuffer = ???
  //def parse(source: String, parsePosition: ParsePosition): Number = ???
  //def parse(source: String): Number = ???
  def getDecimalFormatSymbols(): DecimalFormatSymbols = symbols

  def setDecimalFormatSymbols(symbols: DecimalFormatSymbols): Unit =
    this.symbols = symbols

  // def getPositivePrefix(): String = ???
  // def setPositivePrefix(newValue: String): Unit = ???
  // def getNegativePrefix(): String = ???
  // def setNegativePrefix(newValue: String): Unit = ???
  // def getPositiveSuffix(): String = ???
  // def setPositiveSuffix(newValue: String): Unit = ???
  // def getNegativeSuffix(): String = ???
  // def setNegativeSuffix(newValue: String): Unit = ???
  // def getMultiplier(): Int = ???
  // def setMultiplier(newValue: Int): Unit = ???
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
