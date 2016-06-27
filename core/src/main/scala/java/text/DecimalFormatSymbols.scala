package java.text

import java.util.Locale

import scala.scalajs.locale.LocaleRegistry
import scala.scalajs.locale.ldml.{LDML, Symbols, NumberingSystem}
import scala.scalajs.locale.ldml.data.numericsystems.latn
import scala.scalajs.locale.ldml.data.numericsystems.latn
import scala.scalajs.locale.ldml.data.minimal.root

object DecimalFormatSymbols {

  def getAvailableLocales(): Array[Locale] = Locale.getAvailableLocales

  def getInstance(): DecimalFormatSymbols =
    getInstance(Locale.getDefault(Locale.Category.FORMAT))

  def getInstance(locale: Locale): DecimalFormatSymbols =
    initialize(locale, new DecimalFormatSymbols(locale))

  private def initialize(locale: Locale, dfs: DecimalFormatSymbols): DecimalFormatSymbols = {
    // Find the correct number system
    def ns(ldml: LDML): NumberingSystem =
      ldml.defaultNS.flatMap { n =>
        root.digitSymbols.find(_.ns == n).collect {
          case s if s.aliasOf.isDefined => latn // All aliases go to latn
          case s => n
        }
      }.getOrElse(latn)

    LocaleRegistry.ldml(locale).map(l => toDFS(locale, dfs, l, ns(l))).getOrElse(dfs)
  }

  private def toDFS(locale: Locale, dfs: DecimalFormatSymbols,
     ldml: LDML, ns: NumberingSystem): DecimalFormatSymbols = {

    def parentSymbols(ldml: LDML, ns: NumberingSystem): Option[Symbols] =
      ldml.digitSymbols.find(_.ns == ns).orElse(ldml.parent.flatMap(parentSymbols(_, ns)))

    def parentSymbolR[A](ldml: LDML, ns: NumberingSystem, contains: Symbols => Option[A]): Option[A] =
      parentSymbols(ldml, ns).flatMap {
        case s @ Symbols(_, Some(alias), _, _, _, _, _, _, _, _, _) =>
          parentSymbolR(ldml, alias, contains)

        case s @ Symbols(_, _, _, _, _, _, _, _, _, _, _) =>
          contains(s).orElse(ldml.parent.flatMap(parentSymbolR(_, ns, contains)))
      }

    def setSymbol[A](ldml: LDML, ns: NumberingSystem, contains: Symbols => Option[A], set: A => Unit): Unit =
      parentSymbolR(ldml, ns, contains).foreach(set)

    // Read the zero from the default numeric system
    ns.digits.headOption.foreach(dfs.setZeroDigit)
    // Set the components of the decimal format symbol
    setSymbol(ldml, ns, _.decimal, dfs.setDecimalSeparator)
    setSymbol(ldml, ns, _.group, dfs.setGroupingSeparator)
    setSymbol(ldml, ns, _.list, dfs.setPatternSeparator)
    setSymbol(ldml, ns, _.percent, dfs.setPercent)
    setSymbol(ldml, ns, _.minus, dfs.setMinusSign)
    setSymbol(ldml, ns, _.perMille, dfs.setPerMill)
    setSymbol(ldml, ns, _.infinity, dfs.setInfinity)
    setSymbol(ldml, ns, _.nan, dfs.setNaN)
    setSymbol(ldml, ns, _.exp, dfs.setExponentSeparator)
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
