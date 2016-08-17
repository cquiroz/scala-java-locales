package locales

import java.text.DecimalFormat

object DecimalFormatUtil {
  val PatternCharZeroDigit = '0'
  val PatternCharGroupingSeparator = ','
  val PatternCharDecimalSeparator = '.'
  val PatternCharPerMile = '\u2030'
  val PatternCharPercent = '%'
  val PatternCharDigit = '#'
  val PatternCharSeparator = ';'
  val PatternCharExponent = 'E'
  val PatternCharMinus = '-'

  private val allPatternChars = List(PatternCharZeroDigit, PatternCharGroupingSeparator, PatternCharDecimalSeparator,
    PatternCharPerMile, PatternCharPercent, PatternCharDigit, PatternCharSeparator, PatternCharExponent, PatternCharMinus)

  def lastIndexOfPattern(pattern: String, symbbl: Char): Int = {
    val max = pattern.length - 1
    allPatternChars.filterNot(_ == symbbl).map(pattern.lastIndexOf(_, max)).max
  }

  def suffixFor(format: DecimalFormat, symbol: Char): String = {
    val pattern = format.toPattern
    pattern.substring(lastIndexOfPattern(pattern, symbol) + 1, pattern.length).replaceAll(symbol.toString, localizedSymbol(format, symbol).toString)
  }

  def localizedSymbol(f: DecimalFormat, symbol: Char): Char = {
    symbol match {
      case PatternCharPercent => f.getDecimalFormatSymbols.getPercent
      case PatternCharMinus   => f.getDecimalFormatSymbols.getMinusSign
      case x => x
    }
  }
}
