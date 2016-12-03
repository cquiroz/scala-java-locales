package java.text

import java.math.{RoundingMode, BigDecimal => JavaBigDecimal, BigInteger => JavaBigInteger}
import java.util.Locale
import locales.{DecimalFormatUtil, LocaleRegistry, ParsedPattern}

// The constructor needs a non-localized pattern
class DecimalFormat(private[this] val pattern: String, private[this] var symbols: DecimalFormatSymbols)
    extends NumberFormat {

  def this(pattern: String) = this(pattern, DecimalFormatSymbols.getInstance())

  def this() = {
    this(
      LocaleRegistry.ldml(Locale.getDefault).flatMap{ _.numberPatterns.decimalPattern }.getOrElse(???),
      DecimalFormatSymbols.getInstance()
    )
  }

  // This holds all of the specifics about the decimal pattern
  private var parsedPattern = applyPattern(pattern)

  //private[this] var maximumIntegerDigits: Int = 3
  //override def minimumIntegerDigits: Int = parsedPattern.minimumIntegerDigits.getOrElse(1)
  //private[this] var maximumFractionDigits: Int = 5
  //private[this] var minimumFractionDigits: Int = 0
  //private[this] var roundingMode: RoundingMode = RoundingMode.HALF_EVEN
  //private[this] var groupingUsed: Boolean = false


  private var decimalSeparatorAlwaysShown: Boolean = false
  private var parseBigDecimal: Boolean = false

  // Need to be able to update the complete pattern for this instance
  private def applyPattern(p: String): ParsedPattern = {
    this.parsedPattern = DecimalFormatUtil.toParsedPattern(p)

    // Set NumberFormat values (possibly remove the defs from NumberFormat to clean this up)
    parsedPattern.minimumFractionDigits.foreach{ setMinimumFractionDigits(_) }
    parsedPattern.minimumIntegerDigits.foreach{ setMinimumIntegerDigits(_) }
    parsedPattern.maximumFractionDigits.foreach{ setMaximumFractionDigits(_) }

    this.parsedPattern
  }

  private def useScientificNotation: Boolean = parsedPattern.minimumExponentDigits.isDefined

  override def format(number: Double, toAppendTo: StringBuffer, pos: FieldPosition): StringBuffer =
    subFormat(JavaBigDecimal.valueOf(number), toAppendTo, pos)

  override def format(number: Long, toAppendTo: StringBuffer, pos: FieldPosition): StringBuffer =
    subFormat(JavaBigDecimal.valueOf(number), toAppendTo, pos)

  // Handle formatting any big decimal...I'm sure this algorithm can be optimized
  // ...Trying to get a mostly correct/easier to read/understand implementation first
  private def subFormat(number: JavaBigDecimal, toAppendTo: StringBuffer, pos: FieldPosition): StringBuffer = {
    val negative: Boolean = number.signum == -1

    // Add Prefixes
    if (negative) {
      toAppendTo.append(getNegativePrefix())
    } else {
      toAppendTo.append(getPositivePrefix())
    }

    // Imperative - ish ?
    val multiplied: JavaBigDecimal = number.multiply(JavaBigDecimal.valueOf(getMultiplier))

    if (useScientificNotation) {
      // We could probably re-use the decimal/numeric formatting...
      ???
    } else {

      val integerStr = new StringBuilder()

      // TODO: Can I use a single tracking variable for the separator/max lenght?
      var integerDigitsWritten: Int = 0 // number of integer digits written
      var totalIntegerDigitsWritten: Int = 0 // includes group separators

      val integerPart: JavaBigDecimal = multiplied.setScale(0, RoundingMode.DOWN).abs

      if ((integerPart.compareTo(JavaBigDecimal.ZERO) == 0) || (getMaximumIntegerDigits == 0)) {
        integerStr.append(symbols.getZeroDigit)
        integerDigitsWritten += 1
        totalIntegerDigitsWritten += 1
      }

      var n: JavaBigInteger = integerPart.toBigInteger
      while (
        n.compareTo(JavaBigInteger.ZERO) == 1 &&
          totalIntegerDigitsWritten <= getMaximumIntegerDigits
      ) {
        if (
          (isGroupingUsed) &&
          (getGroupingSize > 0) &&
          (integerDigitsWritten > 0) &&
          (integerDigitsWritten % getGroupingSize == 0)
        ) {
          integerStr.append(symbols.getGroupingSeparator)
          totalIntegerDigitsWritten += 1
        }

        val curr: JavaBigInteger = n.remainder(JavaBigInteger.TEN)
        n = n.divide(JavaBigInteger.TEN)
        integerStr.append(curr.intValue)
        integerDigitsWritten += 1
        totalIntegerDigitsWritten += 1
      }

      // TODO: refactor the grouping code so its not duplicated
      if (integerDigitsWritten < getMinimumIntegerDigits)
        ((integerDigitsWritten + 1) to getMinimumIntegerDigits).foreach{ _ =>
          if (
            (isGroupingUsed) &&
              (getGroupingSize > 0) &&
              (integerDigitsWritten > 0) &&
              (integerDigitsWritten % getGroupingSize == 0)
          ) {
            integerStr.append(symbols.getGroupingSeparator)
            totalIntegerDigitsWritten += 1
          }

          integerStr.append(DecimalFormatUtil.PatternCharZeroDigit)
          integerDigitsWritten += 1
          totalIntegerDigitsWritten += 1
        }

      toAppendTo.append(integerStr.result.reverse)


      multiplied.compareTo(integerPart) match {
        // We have a fractional part
        case 1  =>
          toAppendTo.append(symbols.getDecimalSeparator)

          val fractionStrBuilder = new StringBuilder()

          // Set scale to max fraction digits, subtract integer part, & get
          // 12.0123 -> (set scale 5) 12.01230 -> (minus 12) 0.01230) -> unscaled value 1230
          val fractionPart: JavaBigInteger =
            multiplied.setScale(getMaximumFractionDigits, getRoundingMode).subtract(integerPart).unscaledValue()

          // Convert integer 1230 to a reversed string (0321)
          var n: JavaBigInteger = fractionPart
          while (n.compareTo(JavaBigInteger.ZERO) == 1) {
            val curr: JavaBigInteger = n.remainder(JavaBigInteger.TEN)
            n = n.divide(JavaBigInteger.TEN)
            fractionStrBuilder.append(curr.intValue)
          }

          // 0321
          val unscaledString = fractionStrBuilder.result

          // Drop extra zero's at the end, then add significant '0's to end, then reverse it
          // 0321 => 321 => 3210 => 0123
          val fractionStr: String =
          (
            unscaledString.dropWhile(_ == DecimalFormatUtil.PatternCharZeroDigit) +
            (0 to (getMaximumFractionDigits - unscaledString.length)).map{ _ =>
              DecimalFormatUtil.PatternCharZeroDigit
            }.mkString
          ).reverse

          // Add our fraction with significant prefix zeroes
          toAppendTo.append(fractionStr)

          // Add zero-end padding minimum fraction digits
          if (fractionStr.length < getMinimumFractionDigits)
            toAppendTo.append(
              (fractionStr.length until getMinimumFractionDigits).map{ _ => DecimalFormatUtil.PatternCharZeroDigit }
            )

        // No fraction, but we have a minimum fraction digits to set...
        case _ if (getMinimumFractionDigits > 0) =>
          toAppendTo.append(s"${symbols.getDecimalSeparator}${symbols.getZeroDigit * getMinimumFractionDigits}")
        case _  => // do nothing
      }
    }

    // Add Suffixes
    if (negative) {
      toAppendTo.append(getNegativeSuffix())
    } else {
      toAppendTo.append(getPositiveSuffix())
    }
  }

  def parse(source: String, parsePosition: ParsePosition): Number = ???

  override def parseObject(source: String, pos: ParsePosition): AnyRef = ???

  // TODO implement
  //def format(number: Double, toAppendTo: StringBuffer, pos: FieldPosition): StringBuffer = ???
  //def format(number: Long, toAppendTo: StringBuffer, pos: FieldPosition): StringBuffer = ???
  //def parse(source: String, parsePosition: ParsePosition): Number = ???
  //def parse(source: String): Number = ???

  def getDecimalFormatSymbols(): DecimalFormatSymbols = symbols

  def setDecimalFormatSymbols(symbols: DecimalFormatSymbols): Unit = {
    this.symbols = symbols
    applyPattern(this.pattern)
  }

  // Swap out the percent or mile characters from the prefix/suffix localized characters set
  private def replaceLocalizedPrefixOrSuffixSymbols(s: String): String = {
    if (s == null) ""
    else s.replace(DecimalFormatUtil.PatternCharPercent, symbols.getPercent)
           .replace(DecimalFormatUtil.PatternCharPerMile, symbols.getPerMill)
  }

  def getPositivePrefix(): String = {
    val p: String = parsedPattern.positivePrefix.getOrElse("")
    replaceLocalizedPrefixOrSuffixSymbols(p)
  }

  def setPositivePrefix(newValue: String): Unit = {
    this.parsedPattern = parsedPattern.copy(positivePrefix = Option(newValue))

  }

  // This is slightly special, in that a - will be added to the positive prefix if the original pattern
  // did not have a negative pattern specified
  def getNegativePrefix(): String = {
    val p: String = (
      parsedPattern.negativePrefix orElse
      parsedPattern.defaultNegativePrefix.map{ p => s"${symbols.getMinusSign}$p"}
    ).getOrElse(symbols.getMinusSign.toString)

    replaceLocalizedPrefixOrSuffixSymbols(p)
  }

  def setNegativePrefix(newValue: String): Unit =
    this.parsedPattern = parsedPattern.copy(negativePrefix = Option(newValue))

  def getPositiveSuffix(): String = replaceLocalizedPrefixOrSuffixSymbols(parsedPattern.positiveSuffix.getOrElse(""))

  def setPositiveSuffix(newValue: String): Unit =
    this.parsedPattern = parsedPattern.copy(positiveSuffix = Option(newValue))

  // If no explicit negative suffix, use the positive, unless we explicitly set it to blank
  def getNegativeSuffix(): String = {
    val s: String = (
      parsedPattern.negativeSuffix orElse
      parsedPattern.defaultNegativeSuffix
    ).getOrElse("")

    replaceLocalizedPrefixOrSuffixSymbols(s)
  }

  def setNegativeSuffix(newValue: String): Unit =
    this.parsedPattern = parsedPattern.copy(negativeSuffix = Option(newValue))

  def getMultiplier(): Int = parsedPattern.multiplier

  def setMultiplier(newValue: Int): Unit =
    this.parsedPattern = parsedPattern.copy(multiplier = newValue)

  // override def setGroupingUsed(newValue: Boolean): Unit = ???

  def getGroupingSize(): Int = parsedPattern.groupingSize

  def setGroupingSize(newValue: Int): Unit =
    this.parsedPattern = parsedPattern.copy(groupingSize = newValue)

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
  // def applyLocalizedPattern(pattern: String)


  // def getCurrency(): Currency = ???
  // def setCurrency(currency: Currency): Unit = ???
  // def getRoundingMode(): RoundingMode = ???
  // def setRoundingMode(currency: RoundingMode): Unit = ???
}
