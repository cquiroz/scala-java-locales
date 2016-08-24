package java.text

import java.math.RoundingMode
import java.util.Locale

import locales.{DecimalFormatUtil, LocaleRegistry}
import locales.cldr.{LDML, NumberPatterns}

import scala.math.{max, min}

 abstract class NumberFormat protected () extends Format {
  private[this] var parseIntegerOnly: Boolean = false
  private[this] var maximumIntegerDigits: Int = 3
  private[this] var minimumIntegerDigits: Int = 1
  private[this] var maximumFractionDigits: Int = 5
  private[this] var minimumFractionDigits: Int = 0
  private[this] var roundingMode: RoundingMode = RoundingMode.HALF_EVEN
  private[this] var groupingUsed: Boolean = false

  override def parseObject(source: String, pos: ParsePosition): AnyRef

  override def format(obj: AnyRef, toAppendTo: StringBuffer, pos: FieldPosition): StringBuffer = ???

  // TODO implement
  // final def format(number: Double): String = ???
  // final def format(number: Long): String = ???
  // def format(number: Double, toAppendTo: StringBuffer, pos: FieldPosition): StringBuffer = ???
  // def format(number: Long, toAppendTo: StringBuffer, pos: FieldPosition): StringBuffer = ???
  // def parse(source: String, parsePosition: ParsePosition): Number = ???
  // def parse(source: String): Number = ???

  def isParseIntegerOnly(): Boolean = this.parseIntegerOnly

  def setParseIntegerOnly(value: Boolean): Unit = this.parseIntegerOnly = value

  // override def hashCode(): Int = ???
  // override def equals(obj: Any): Boolean = ???
  // override def clone(): Any = ???

  def isGroupingUsed(): Boolean = this.groupingUsed

  def setGroupingUsed(newValue: Boolean): Unit = this.groupingUsed = newValue

  def getMaximumIntegerDigits(): Int = this.maximumIntegerDigits

  def setMaximumIntegerDigits(newValue: Int): Unit = {
    this.maximumIntegerDigits = max(newValue, 0)
    this.minimumIntegerDigits = min(this.minimumIntegerDigits, this.maximumIntegerDigits)
  }

  def getMinimumIntegerDigits(): Int = this.minimumIntegerDigits

  def setMinimumIntegerDigits(newValue: Int): Unit = {
    this.minimumIntegerDigits = max(newValue, 0)
    this.maximumIntegerDigits = max(this.minimumIntegerDigits, this.maximumIntegerDigits)
  }

  def getMaximumFractionDigits(): Int = this.maximumFractionDigits

  def setMaximumFractionDigits(newValue: Int): Unit = {
    this.maximumFractionDigits = max(newValue, 0)
    this.minimumFractionDigits = min(this.minimumFractionDigits, this.maximumFractionDigits)
  }

  def getMinimumFractionDigits(): Int = this.minimumFractionDigits

  def setMinimumFractionDigits(newValue: Int): Unit = {
    this.minimumFractionDigits = max(newValue, 0)
    this.maximumFractionDigits = max(this.minimumFractionDigits, this.maximumFractionDigits)
  }

  // def getCurrency(): Currency = ???
  // def setCurrency(currency: Currency): Unit = ???

  def getRoundingMode(): RoundingMode = roundingMode

  def setRoundingMode(mode: RoundingMode): Unit = this.roundingMode = mode
}

object NumberFormat {
  val INTEGER_FIELD: Int = 0
  val FRACTION_FIELD: Int = 1

  private def setup(nf: DecimalFormat): NumberFormat = {
    nf.setMaximumIntegerDigits(Integer.MAX_VALUE)
    nf.setMaximumFractionDigits(3)
    nf.setGroupingUsed(true) // Should this be inferred from the pattern?
    nf
  }

  private def integerSetup(nf: DecimalFormat): NumberFormat = {
    setup(nf)
    nf.setMaximumFractionDigits(0)
    nf
  }

  private def percentSetup(nf: DecimalFormat): NumberFormat = {
    setup(nf)
    nf.setMaximumFractionDigits(0)
    nf.setMultiplier(100)
    nf
  }

  private def patternsR(ldml: LDML, get: NumberPatterns => Option[String]): Option[String] =
    get(ldml.numberPatterns).orElse(ldml.parent.flatMap(patternsR(_, get)))

  final def getInstance(): NumberFormat = getNumberInstance()

  def getInstance(inLocale: Locale): NumberFormat = getNumberInstance(inLocale)

  final def getNumberInstance(): NumberFormat =
    getInstance(Locale.getDefault(Locale.Category.FORMAT))

  def getNumberInstance(inLocale: Locale): NumberFormat =
    LocaleRegistry.ldml(inLocale).flatMap { ldml =>
      val ptrn = patternsR(ldml, _.decimalPattern)
      ptrn.map(new DecimalFormat(_, DecimalFormatSymbols.getInstance(inLocale))).map(setup)
    }.getOrElse(new DecimalFormat("", DecimalFormatSymbols.getInstance(inLocale)))

  final def getIntegerInstance(): NumberFormat =
    getIntegerInstance(Locale.getDefault(Locale.Category.FORMAT))

  def getIntegerInstance(inLocale: Locale): NumberFormat = {
    val f = LocaleRegistry.ldml(inLocale).flatMap { ldml =>
      val ptrn = patternsR(ldml, _.decimalPattern)
      ptrn.map(p => new DecimalFormat(p.substring(0, p.indexOf(".")),
        DecimalFormatSymbols.getInstance(inLocale))).map(integerSetup)
    }.getOrElse(new DecimalFormat("", DecimalFormatSymbols.getInstance(inLocale)))
    f.setParseIntegerOnly(true)
    f
  }

  //final def getCurrencyInstance(): NumberFormat = ???
  //def getCurrencyInstance(inLocale: Locale): NumberFormat = ???

  final def getPercentInstance(): NumberFormat =
    getPercentInstance(Locale.getDefault(Locale.Category.FORMAT))

  def getPercentInstance(inLocale: Locale): NumberFormat =
    LocaleRegistry.ldml(inLocale).flatMap { ldml =>
      val ptrn = patternsR(ldml, _.percentPattern)
      ptrn.map(new DecimalFormat(_, DecimalFormatSymbols.getInstance(inLocale))).map(percentSetup)
    }.getOrElse(new DecimalFormat("", DecimalFormatSymbols.getInstance(inLocale)))

  def getAvailableLocales(): Array[Locale] = Locale.getAvailableLocales
}
