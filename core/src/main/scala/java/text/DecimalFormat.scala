package java.text

import java.math.{ RoundingMode, BigDecimal => JavaBigDecimal, BigInteger => JavaBigInteger }
import java.util.{ Currency, Locale }
import java.util.LocalesDb
import locales.{ DecimalFormatUtil, ParsedPattern }
import scala.math.{ max, min }

// The constructor needs a non-localized pattern
class DecimalFormat(
  private[this] val pattern: String,
  private[this] var symbols: DecimalFormatSymbols
) extends NumberFormat {

  def this(pattern: String) = this(pattern, DecimalFormatSymbols.getInstance())

  def this() = {
    this(
      LocalesDb
        .ldml(Locale.getDefault)
        .flatMap(_.numberPatterns.decimalFormat)
        .getOrElse("#,##0.##"),
      DecimalFormatSymbols.getInstance()
    )
  }

  // This holds all of the specifics about the decimal pattern
  private var parsedPattern: ParsedPattern = usePattern(pattern)

  private var decimalSeparatorAlwaysShown: Boolean = false
  private var parseBigDecimal: Boolean             = false
  private def currency: Currency                   = Currency.getInstance(Locale.getDefault)

  // Helpers to avoid using .compareTo, annoying have to re-import within defs
  private val bigIntegerOrdering = implicitly[Ordering[JavaBigInteger]]

  private val bigDecimalOrdering = implicitly[Ordering[JavaBigDecimal]]

  // Need to be able to update the complete pattern for this instance
  private def usePattern(p: String): ParsedPattern = {
    this.parsedPattern = DecimalFormatUtil.toParsedPattern(p)

    this.parsedPattern
  }

  private def useScientificNotation: Boolean = parsedPattern.minimumExponentDigits.isDefined

  override def format(number: Double, toAppendTo: StringBuffer, pos: FieldPosition): StringBuffer =
    subFormat(JavaBigDecimal.valueOf(number), toAppendTo, pos)

  override def format(number: Long, toAppendTo: StringBuffer, pos: FieldPosition): StringBuffer =
    subFormat(JavaBigDecimal.valueOf(number), toAppendTo, pos)

  private def handleGroupSeparator(builder: StringBuilder, idx: Int) =
    if ((isGroupingUsed) &&
        (getGroupingSize > 0) &&
        (idx > 0) &&
        (idx % getGroupingSize == 0)) {
      builder.append(symbols.getGroupingSeparator)
    }

  /* Write number to strBuilder, return Digits Written */
  private def formatNumber(
    number:        JavaBigInteger,
    builder:       StringBuilder,
    isIntegerPart: Boolean
  ): Int = {
    import bigIntegerOrdering.mkOrderingOps

    var digitsWritten: Int = 0

    var n: JavaBigInteger = number
    while (n > JavaBigInteger.ZERO &&
           (if (isIntegerPart) (totalDigitsWritten(digitsWritten) <= getMaximumIntegerDigits)
            else true)) {
      if (isIntegerPart && !useScientificNotation) handleGroupSeparator(builder, digitsWritten)

      val curr: JavaBigInteger = n.remainder(JavaBigInteger.TEN)
      n = n.divide(JavaBigInteger.TEN)
      builder.append(curr.intValue)
      digitsWritten += 1
    }

    digitsWritten
  }

  // If the exponent after the mantissa should be a particular number multiple (like engineering notation/etc)
  private def isExponentPowerMultiple(): Boolean =
    useScientificNotation &&
      (getMaximumIntegerDigits > getMinimumIntegerDigits && getMaximumIntegerDigits > 1)

  // JavaDocs: The number of significant digits in the mantissa is the sum of the minimum integer and maximum
  // fraction digits, and is unaffected by the maximum integer digits.
  // Experimental/JVM note: if (isIntegerMultiple) then precision is maxIntegerDigits + max fraction digits
  private def totalExponentPrecision(): Int =
    if (isExponentPowerMultiple) (getMaximumIntegerDigits + getMaximumFractionDigits)
    else (getMinimumIntegerDigits + getMaximumFractionDigits)

  // Return the scaled big decimal + power unit
  def getExponentNumberAndPower(n: JavaBigDecimal): (JavaBigDecimal, Int) = {
    import bigDecimalOrdering.mkOrderingOps

    // zero shortcut
    if (n.compareTo(JavaBigDecimal.ZERO) == 0) (JavaBigDecimal.ZERO, 0)
    else {
      val isJustFraction: Boolean      = (n.abs < JavaBigDecimal.ONE)
      val originalDecimalPosition: Int = (n.precision - n.scale)
      val newPrecision: Int            = min(n.precision, totalExponentPrecision)

      val newIntegerSize = if (isExponentPowerMultiple) {
        def matchesMultiple(idx: Int): Boolean =
          (((originalDecimalPosition - (getMaximumIntegerDigits - idx)) % getMaximumIntegerDigits) == 0)

        (0 until getMaximumIntegerDigits)
          .collectFirst {
            case idx: Int if matchesMultiple(idx) => (getMaximumIntegerDigits - idx)
          }
          .getOrElse(1)
      } else {
        max(min(newPrecision, getMaximumIntegerDigits), 1)
      }

      val scalePrecision = {
        val unscaled = new JavaBigDecimal(n.unscaledValue, newPrecision - newIntegerSize)

        // TODO: WTF is going on here with the rounding mode?
        unscaled.divide(
          JavaBigDecimal.TEN.pow(n.precision - newPrecision),
          if (isJustFraction) RoundingMode.HALF_UP else getRoundingMode
        )
      }

      (
        scalePrecision,
        originalDecimalPosition - newIntegerSize
      )
    }
  }

  // Based upon number count, add in the number of group separators
  private def totalDigitsWritten(count: Int): Int =
    if (isGroupingUsed && getGroupingSize > 0 && count > 0) {
      count + (count / getGroupingSize)
    } else count

  private def repeatDigits(count: Int, c: Char): String =
    (0 until count).map(_ => c).mkString

  // Handle formatting any big decimal...I'm sure this algorithm can be optimized
  // ...Trying to get a mostly correct/easier to read/understand implementation first
  // TODO: Currently ignoring FieldPosition argument
  private def subFormat(
    number:     JavaBigDecimal,
    toAppendTo: StringBuffer,
    pos:        FieldPosition
  ): StringBuffer = {
    import bigDecimalOrdering.mkOrderingOps

    val isNegative: Boolean = number.signum == -1

    // Add Prefix
    val prefix: String = if (isNegative) getNegativePrefix() else getPositivePrefix()
    toAppendTo.append(prefix)

    val multiplied: JavaBigDecimal =
      number.multiply(JavaBigDecimal.valueOf(getMultiplier.toLong)).abs

    // Round the target number based upon expected fractions, so we can compare it to the integer
    val (targetNumber: JavaBigDecimal, expPower: Int) =
      if (useScientificNotation) {
        getExponentNumberAndPower(multiplied)
      } else {
        (
          multiplied.setScale(
            max(getMaximumFractionDigits(), getMinimumFractionDigits()),
            getRoundingMode
          ),
          0
        )
      }

    val integerStrBuilder           = new StringBuilder()
    val integerPart: JavaBigDecimal = new JavaBigDecimal(targetNumber.toBigInteger, 0)
    var integerDigitsWritten: Int   = 0

    if ((integerPart.compareTo(JavaBigDecimal.ZERO) == 0) || (getMaximumIntegerDigits == 0)) {
      integerStrBuilder.append(symbols.getZeroDigit)
      integerDigitsWritten += 1
    } else {
      integerDigitsWritten += formatNumber(integerPart.toBigInteger, integerStrBuilder, true)
    }

    // Add integer digit padding if needed
    if (integerDigitsWritten < getMinimumIntegerDigits) {
      (integerDigitsWritten until getMinimumIntegerDigits).foreach { _ =>
        handleGroupSeparator(integerStrBuilder, integerDigitsWritten)

        integerStrBuilder.append(symbols.getZeroDigit)
        integerDigitsWritten += 1
      }
    }

    // We have the integer portion ready, append it to the builder
    toAppendTo.append(integerStrBuilder.result.reverse)

    // We have a fraction value
    if (targetNumber > integerPart) {
      toAppendTo.append(symbols.getDecimalSeparator)

      val fractionStrBuilder = new StringBuilder()

      // Special case for exponents
      val fractionMaxDigits: Int = if (useScientificNotation) {
        totalExponentPrecision - integerDigitsWritten
      } else getMaximumFractionDigits

      // Set scale to max fraction digits, subtract integer part, & get
      // 12.0123 -> (set scale 5) 12.01230 -> (minus 12) 0.01230) -> unscaled value 1230
      val fractionPart: JavaBigInteger =
        targetNumber
          .setScale(fractionMaxDigits, getRoundingMode)
          .subtract(integerPart)
          .unscaledValue()

      // Convert integer 1230 to a reversed string (0321)
      formatNumber(fractionPart, fractionStrBuilder, false)

      // 0321
      val unscaledString = fractionStrBuilder.result

      // Drop extra zero's at the end, then add significant '0's to end, then reverse it
      // 0321 => 321 => 3210 => 0123
      val fractionStr: String = {
        val truncatedStr: String = unscaledString.dropWhile(_ == symbols.getZeroDigit)

        truncatedStr +
          (0 until (fractionMaxDigits - unscaledString.length))
            .map(_ => symbols.getZeroDigit)
            .mkString
      }.reverse

      // Add our fraction with significant prefix zeroes
      toAppendTo.append(fractionStr)

      // Add zero-end padding minimum fraction digits
      if (fractionStr.length < getMinimumFractionDigits) {
        toAppendTo.append(
          repeatDigits(getMinimumFractionDigits - fractionStr.length, symbols.getZeroDigit)
        )
      }
      // No fraction value, but we have a minimum fraction digits to set...
      // 0.00 equals (0) returns false :/, so can't use ordering
    } else if (targetNumber.compareTo(integerPart) == 0 && getMinimumFractionDigits > 0) {
      toAppendTo.append(
        s"${symbols.getDecimalSeparator}${repeatDigits(getMinimumFractionDigits, symbols.getZeroDigit)}"
      )
    }

    if (useScientificNotation) {
      toAppendTo.append(symbols.getExponentSeparator)
      toAppendTo.append(expPower.toString)
    }

    // Add Suffixes
    val suffix: String = if (isNegative) getNegativeSuffix() else getPositiveSuffix()
    toAppendTo.append(suffix)
  }

  def parse(source: String, parsePosition: ParsePosition): Number = ???

  override def parseObject(source: String, pos: ParsePosition): AnyRef = ???

  // TODO implement
  //def parse(source: String, parsePosition: ParsePosition): Number = ???
  //def parse(source: String): Number = ???

  def getDecimalFormatSymbols(): DecimalFormatSymbols = symbols

  def setDecimalFormatSymbols(symbols: DecimalFormatSymbols): Unit = {
    this.symbols = symbols
    usePattern(this.pattern)
  }

  // Swap out the percent or mile characters from the prefix/suffix localized characters set
  private def replaceLocalizedPrefixOrSuffixSymbols(s: String): String =
    if (s == null) ""
    else
      s.replace(DecimalFormatUtil.PatternCharPercent, symbols.getPercent)
        .replace(DecimalFormatUtil.PatternCharPerMile, symbols.getPerMill)
        .replace(DecimalFormatUtil.PatternCharCurrencySymbol.toString, getCurrency().getSymbol())

  def getPositivePrefix(): String = {
    val p: String = parsedPattern.positivePrefix.getOrElse("")
    replaceLocalizedPrefixOrSuffixSymbols(p)
  }

  def setPositivePrefix(newValue: String): Unit =
    this.parsedPattern = parsedPattern.copy(positivePrefix = Option(newValue))

  // This is slightly special, in that a - will be added to the positive prefix if the original pattern
  // did not have a negative pattern specified
  def getNegativePrefix(): String = {
    val p: String =
      parsedPattern.negativePrefix
        .orElse(parsedPattern.defaultNegativePrefix.map(p => s"${symbols.getMinusSign}$p"))
        .getOrElse(symbols.getMinusSign.toString)

    replaceLocalizedPrefixOrSuffixSymbols(p)
  }

  def setNegativePrefix(newValue: String): Unit =
    this.parsedPattern = parsedPattern.copy(negativePrefix = Option(newValue))

  def getPositiveSuffix(): String =
    replaceLocalizedPrefixOrSuffixSymbols(parsedPattern.positiveSuffix.getOrElse(""))

  def setPositiveSuffix(newValue: String): Unit =
    this.parsedPattern = parsedPattern.copy(positiveSuffix = Option(newValue))

  // If no explicit negative suffix, use the positive, unless we explicitly set it to blank
  def getNegativeSuffix(): String = {
    val s: String =
      parsedPattern.negativeSuffix
        .orElse(parsedPattern.defaultNegativeSuffix)
        .getOrElse("")

    replaceLocalizedPrefixOrSuffixSymbols(s)
  }

  def setNegativeSuffix(newValue: String): Unit =
    this.parsedPattern = parsedPattern.copy(negativeSuffix = Option(newValue))

  def getMultiplier(): Int = parsedPattern.multiplier

  def setMultiplier(newValue: Int): Unit =
    this.parsedPattern = parsedPattern.copy(multiplier = newValue)

  def getGroupingSize(): Int = parsedPattern.groupingSize

  def setGroupingSize(newValue: Int): Unit =
    this.parsedPattern =
      parsedPattern.copy(groupingSize = newValue, isGroupingUsed = (newValue > 0))

  override def isGroupingUsed(): Boolean = parsedPattern.isGroupingUsed

  override def setGroupingUsed(newValue: Boolean): Unit =
    this.parsedPattern = parsedPattern.copy(isGroupingUsed = newValue)

  def isDecimalSeparatorAlwaysShown(): Boolean = this.decimalSeparatorAlwaysShown

  def setDecimalSeparatorAlwaysShown(newValue: Boolean): Unit =
    this.decimalSeparatorAlwaysShown = newValue

  def isParseBigDecimal(): Boolean = this.parseBigDecimal

  def setParseBigDecimal(newValue: Boolean): Unit = this.parseBigDecimal = newValue

  def toPattern(): String          = generatePattern(false)
  def toLocalizedPattern(): String = generatePattern(true)

  private def generatePattern(localize: Boolean): String = {
    val isExponent: Boolean =
      parsedPattern.minimumExponentDigits.isDefined || parsedPattern.maximumExponentDigits.isDefined

    // Create the core pattern.
    val sb = new StringBuilder

    val requiredIntegersStr: String = repeatDigits(
      getMinimumIntegerDigits(),
      if (localize) symbols.getZeroDigit else DecimalFormatUtil.PatternCharZeroDigit
    )

    val optionalIntegersStr: String = repeatDigits(
      if (isExponent) (getMaximumIntegerDigits() - getMinimumIntegerDigits())
      else max((getGroupingSize() - getMinimumIntegerDigits() + 1), 1),
      if (localize) symbols.getDigit else DecimalFormatUtil.PatternCharDigit
    )

    if (isGroupingUsed() && getGroupingSize() > 0) {
      val integerStr = requiredIntegersStr + optionalIntegersStr
      val c: Char =
        if (localize) symbols.getGroupingSeparator
        else DecimalFormatUtil.PatternCharGroupingSeparator
      sb.append(integerStr.grouped(getGroupingSize()).mkString(c.toString).reverse)
    } else {
      sb.append(optionalIntegersStr)
      sb.append(requiredIntegersStr)
    }

    val requiredFractionsStr: String = repeatDigits(
      getMinimumFractionDigits(),
      if (localize) symbols.getZeroDigit else DecimalFormatUtil.PatternCharZeroDigit
    )

    val optionalFractionsStr: String = repeatDigits(
      getMaximumFractionDigits() - getMinimumFractionDigits(),
      if (localize) symbols.getDigit else DecimalFormatUtil.PatternCharDigit
    )

    // Append the decimal separator
    if (requiredFractionsStr.nonEmpty || optionalFractionsStr.nonEmpty)
      sb.append(
        if (localize) symbols.getDecimalSeparator
        else DecimalFormatUtil.PatternCharDecimalSeparator
      )

    // Add Fractions
    if (requiredFractionsStr.nonEmpty) sb.append(requiredFractionsStr)
    if (optionalFractionsStr.nonEmpty) sb.append(optionalFractionsStr)

    // Add Exponents
    if (isExponent) {
      sb.append(
        if (localize) symbols.getExponentSeparator else DecimalFormatUtil.PatternCharExponent
      )

      val minStr: String = parsedPattern.minimumExponentDigits
        .map { len: Int =>
          repeatDigits(
            len,
            if (localize) symbols.getZeroDigit
            else DecimalFormatUtil.PatternCharZeroDigit
          )
        }
        .getOrElse("")

      sb.append(minStr)

      val maxStr = parsedPattern.maximumExponentDigits
        .map { len: Int =>
          repeatDigits(
            len - minStr.size,
            if (localize) symbols.getDigit
            else DecimalFormatUtil.PatternCharDigit
          )
        }
        .getOrElse("")

      sb.append(maxStr)
    }

    val pattern: String = sb.toString()

    val result = new StringBuilder

    parsedPattern.positivePrefix.map(result.append)
    result.append(pattern)
    parsedPattern.positiveSuffix.map(result.append)

    // Add negative pattern
    if (parsedPattern.negativePrefix.isDefined || parsedPattern.negativeSuffix.isDefined) {
      // Pattern separator
      result.append(
        if (localize) symbols.getPatternSeparator else DecimalFormatUtil.PatternCharSeparator
      )

      parsedPattern.negativePrefix.foreach(result.append)
      result.append(pattern)
      parsedPattern.negativeSuffix.foreach(result.append)
    }

    result.toString()
  }

  override def getMaximumIntegerDigits(): Int =
    parsedPattern.maximumIntegerDigits.getOrElse(Int.MaxValue)

  override def setMaximumIntegerDigits(newValue: Int): Unit = {
    val newMax: Int = max(newValue, 0)

    this.parsedPattern = parsedPattern.copy(
      maximumIntegerDigits = Some(newMax),
      minimumIntegerDigits = parsedPattern.minimumIntegerDigits.map(min(_, newMax))
    )
  }

  override def getMinimumIntegerDigits(): Int = parsedPattern.minimumIntegerDigits.getOrElse(0)

  override def setMinimumIntegerDigits(newValue: Int): Unit = {
    val newMin: Int = max(newValue, 0)

    this.parsedPattern = parsedPattern.copy(
      maximumIntegerDigits = parsedPattern.maximumIntegerDigits.map(max(_, newMin)),
      minimumIntegerDigits = Some(newMin)
    )
  }

  override def getMaximumFractionDigits(): Int = parsedPattern.maximumFractionDigits.getOrElse(5)

  override def setMaximumFractionDigits(newValue: Int): Unit = {
    val newMax: Int = max(newValue, 0)

    this.parsedPattern = parsedPattern.copy(
      maximumFractionDigits = Some(newMax),
      minimumFractionDigits = parsedPattern.minimumFractionDigits.map(min(_, newMax))
    )
  }

  override def getMinimumFractionDigits(): Int = parsedPattern.minimumFractionDigits.getOrElse(0)

  override def setMinimumFractionDigits(newValue: Int): Unit = {
    val newMin: Int = max(newValue, 0)

    this.parsedPattern = parsedPattern.copy(
      maximumFractionDigits = parsedPattern.maximumFractionDigits.map(max(_, newMin)),
      minimumFractionDigits = Some(newMin)
    )
  }

  def applyPattern(pattern: String): Unit = usePattern(pattern)

  def applyLocalizedPattern(pattern: String): Unit = {
    val standardPattern = pattern

    usePattern(standardPattern)
  }

  override def getCurrency(): Currency = currency

  override def setCurrency(currency: Currency): Unit = ??? //this.currency = currency

  override def clone(): AnyRef = {
    val f = new DecimalFormat(toPattern())

    // Non pattern-based settings to propogate
    f.setParseIntegerOnly(isParseIntegerOnly)
    f.setParseBigDecimal(isParseBigDecimal)
    f.setGroupingUsed(isGroupingUsed)
    f.setRoundingMode(getRoundingMode)

    f
  }

  override def hashCode(): Int = {
    val prime  = 31
    var result = 1
    result = prime * result + getCurrency().hashCode()
    result = prime * result + getDecimalFormatSymbols().hashCode()
    result = prime * result + getGroupingSize().hashCode()
    result = prime * result + getMaximumFractionDigits().hashCode()
    result = prime * result + getMaximumIntegerDigits().hashCode()
    result = prime * result + getMinimumFractionDigits().hashCode()
    result = prime * result + getMinimumIntegerDigits().hashCode()
    result = prime * result + getMultiplier().hashCode()
    result = prime * result + getNegativePrefix().hashCode()
    result = prime * result + getNegativeSuffix().hashCode()
    result = prime * result + getPositivePrefix().hashCode()
    result = prime * result + getPositiveSuffix().hashCode()
    result = prime * result + getRoundingMode().hashCode()
    result = prime * result + isDecimalSeparatorAlwaysShown().hashCode()
    result = prime * result + isParseBigDecimal().hashCode()
    result
  }

  override def equals(obj: Any): Boolean =
    obj match {
      case f: DecimalFormat =>
        f.getCurrency() == getCurrency &&
          f.getDecimalFormatSymbols() == getDecimalFormatSymbols &&
          f.getGroupingSize() == getGroupingSize() &&
          f.getMaximumFractionDigits() == getMaximumFractionDigits() &&
          f.getMaximumIntegerDigits() == getMaximumIntegerDigits() &&
          f.getMinimumFractionDigits() == getMinimumFractionDigits() &&
          f.getMinimumIntegerDigits() == getMinimumIntegerDigits() &&
          f.getMultiplier() == getMultiplier() &&
          f.getNegativePrefix() == getNegativePrefix() &&
          f.getNegativeSuffix() == getNegativeSuffix() &&
          f.getPositivePrefix() == getPositivePrefix() &&
          f.getPositiveSuffix() == getPositiveSuffix() &&
          f.getRoundingMode() == getRoundingMode() &&
          f.isDecimalSeparatorAlwaysShown() == isDecimalSeparatorAlwaysShown() &&
          f.isParseBigDecimal() == isParseBigDecimal()

      case _ => false
    }
}
