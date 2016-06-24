package java.text

import java.util.Locale

import scala.scalajs.LocaleRegistry
import scala.scalajs.locale.ldml.{LDML, LDMLDigitSymbols, LDMLNumberingSystem}

object DecimalFormatSymbols {

  def getAvailableLocales(): Array[Locale] = Array.empty

  def getInstance(): DecimalFormatSymbols =
    getInstance(Locale.getDefault(Locale.Category.FORMAT))

  def getInstance(locale: Locale): DecimalFormatSymbols =
    initialize(locale, new DecimalFormatSymbols(locale))

  private def initialize(locale: Locale, dfs: DecimalFormatSymbols): DecimalFormatSymbols = {
    LocaleRegistry.ldml(locale).map(toDFS(locale, dfs, _)).getOrElse(dfs)
  }

  private def toDFS(locale: Locale, dfs: DecimalFormatSymbols, ldml: LDML): DecimalFormatSymbols = {

    def parentNumberingSystem(ldml: LDML): Option[LDMLNumberingSystem] =
      ldml.defaultNS.orElse(ldml.parent.flatMap(parentNumberingSystem))

    def parentSymbol(ldml: LDML, contains: LDMLDigitSymbols => Option[String]): Option[String] =
      ldml.digitSymbols.flatMap(d => contains(d))
        .orElse(ldml.parent.flatMap(parentSymbol(_, contains)))

    def setSymbolChar(ldml: LDML, contains: LDMLDigitSymbols => Option[String], set: Char => Unit): Unit =
      parentSymbol(ldml, contains).foreach(v =>
        if (v.isEmpty) set(0) else set(v.charAt(0)))

    def setSymbolStr(ldml: LDML, contains: LDMLDigitSymbols => Option[String], set: String => Unit): Unit =
      parentSymbol(ldml, contains).foreach(set)

    // Read the zero from the default numeric system
    parentNumberingSystem(ldml).flatMap(_.digits.headOption)
      .foreach(dfs.setZeroDigit)
    // Set the components of the decimal format symbol
    setSymbolChar(ldml, _.decimal, dfs.setDecimalSeparator)
    setSymbolChar(ldml, _.group, dfs.setGroupingSeparator)
    setSymbolChar(ldml, _.list, dfs.setPatternSeparator)
    setSymbolChar(ldml, _.percent, dfs.setPercent)
    setSymbolChar(ldml, _.minus, dfs.setMinusSign)
    setSymbolChar(ldml, _.perMille, dfs.setPerMill)
    setSymbolStr(ldml, _.infinity, dfs.setInfinity)
    setSymbolStr(ldml, _.nan, dfs.setNaN)
    setSymbolStr(ldml, _.exp, dfs.setExponentSeparator)
    // CLDR fixes the pattern character
    // http://www.unicode.org/reports/tr35/tr35-numbers.html#Number_Format_Patterns
    dfs.setDigit('#')
    dfs
  }
}

class DecimalFormatSymbols(private[this] val locale: Locale) extends Cloneable {
  private[this] var zeroDigit: Option[Char] = None
  private[this] var minusSign: Option[Char] = None
  private[this] var decimalSeparator: Option[Char] = None
  private[this] var groupingSeparator: Option[Char] = None
  private[this] var perMill: Option[Char] = None
  private[this] var percent: Option[Char] = None
  private[this] var digit: Option[Char] = None
  private[this] var patternSeparator: Option[Char] = None
  private[this] var infinity: Option[String] = None
  private[this] var nan: Option[String] = None
  private[this] var exp: Option[String] = None

  DecimalFormatSymbols.initialize(locale, this)

  def this() = this(Locale.getDefault(Locale.Category.FORMAT))

  def getZeroDigit(): Char = zeroDigit.getOrElse(0)

  def setZeroDigit(zeroDigit: Char): Unit =
    this.zeroDigit = Some(zeroDigit)

  def getGroupingSeparator(): Char = groupingSeparator.getOrElse(0)

  def setGroupingSeparator(groupingSeparator: Char): Unit =
    this.groupingSeparator = Some(groupingSeparator)

  def getDecimalSeparator(): Char = decimalSeparator.getOrElse(0)

  def setDecimalSeparator(decimalSeparator: Char): Unit =
    this.decimalSeparator = Some(decimalSeparator)

  def getPerMill(): Char = perMill.getOrElse(0)

  def setPerMill(perMill: Char): Unit =
    this.perMill = Some(perMill)

  def getPercent(): Char = percent.getOrElse(0)

  def setPercent(percent: Char): Unit =
    this.percent = Some(percent)

  def getDigit(): Char = digit.getOrElse(0)

  def setDigit(digit: Char): Unit =
    this.digit = Some(digit)

  def getPatternSeparator(): Char = patternSeparator.getOrElse(0)

  def setPatternSeparator(patternSeparator: Char): Unit =
    this.patternSeparator = Some(patternSeparator)

  def getInfinity(): String = infinity.getOrElse("")

  def setInfinity(infinity: String): Unit =
    this.infinity = Some(infinity)

  def getNaN(): String = nan.getOrElse("")

  def setNaN(nan: String): Unit =
    this.nan = Some(nan)

  def getMinusSign(): Char = minusSign.getOrElse(0)

  def setMinusSign(minusSign: Char): Unit =
    this.minusSign = Some(minusSign)

  // TODO Implement currency methods
  //def getCurrencySymbol(): String

  //def setCurrencySymbol(currency: String): Unit

  //def getInternationalCurrencySymbol(): String

  //def setInternationalCurrencySymbol(currency: String): Unit

  //def getCurrency(): Currency

  //def setCurrency(currency: Currency): Unit

  //def getMonetaryDecimalSeparator(): Char

  //def setMonetaryDecimalSeparator(sep: Char): Unit

  def getExponentSeparator(): String= exp.getOrElse("")

  def setExponentSeparator(sep: String): Unit = {
    if (sep == null) throw new NullPointerException()
    this.exp = Some(sep)
  }

  override def clone(): AnyRef =
    new DecimalFormatSymbols(locale)

  override def equals(obj: Any): Boolean =
    obj match {
      case d: DecimalFormatSymbols =>
        d.getZeroDigit == getZeroDigit &&
        d.getGroupingSeparator == getGroupingSeparator &&
        d.getDecimalSeparator == getDecimalSeparator &&
        d.getPerMill == getPerMill &&
        d.getPercent == getPercent &&
        d.getDigit == getDigit &&
        d.getPatternSeparator == getPatternSeparator &&
        d.getInfinity == getInfinity  &&
        d.getNaN == getNaN &&
        d.getMinusSign == getMinusSign &&
        d.getExponentSeparator == getExponentSeparator
      case _ => false
    }

  // Oddly the JVM seems to always return the same
  // it breaks the hashCode contract, will skip implementing
  //override def hashCode(): Int = locale.hashCode()
    /*val prime = 31
    var result = 1
    result = prime * result + getZeroDigit().hashCode()
    result = prime * result + getGroupingSeparator().hashCode()
    result = prime * result + getDecimalSeparator().hashCode()
    result = prime * result + getPerMill().hashCode()
    result = prime * result + getPercent().hashCode()
    result = prime * result + getDigit().hashCode()
    result = prime * result + getPatternSeparator().hashCode()
    result = prime * result + (if (getInfinity() != null) getInfinity().hashCode() else 0)
    result = prime * result + (if (getNaN() != null) getNaN().hashCode() else 0)
    result = prime * result + getMinusSign().hashCode()
    result = prime * result + getExponentSeparator().hashCode()
    result
  }*/
}
