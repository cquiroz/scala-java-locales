package locales

import java.text.{DecimalFormat, DecimalFormatSymbols}

import scala.math.max

case class PatternParts(prefix: String, pattern: String, suffix: String)
object PatternParts {
  def apply(pattern: String):PatternParts = PatternParts("", pattern, "")
}

case class DecimalPatterns(positive: PatternParts, negative: PatternParts)

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

  private val numberPatternChars = List(PatternCharZeroDigit, PatternCharDigit, PatternCharDecimalSeparator, PatternCharMinus,
    PatternCharGroupingSeparator, PatternCharExponent)

  def lastIndexOfPattern(pattern: String, symbbl: Char): Int = {
    val max = pattern.length - 1
    allPatternChars.filterNot(_ == symbbl).map(pattern.lastIndexOf(_, max)).max
  }

  def suffixFor(format: DecimalFormat, symbol: Char): String = {
    val pattern = format.toPattern
    pattern.substring(lastIndexOfPattern(pattern, symbol) + 1, pattern.length).replaceAll(symbol.toString, localizedSymbol(format, symbol).toString)
  }

  def decimalPatternSplit(pattern: String): PatternParts = {
    // Traverse the string to find the suffix and prefix
    val prefix = new StringBuilder()
    val body = new StringBuilder()
    val suffix = new StringBuilder()
    val quoteChar = '''

    // Fast parsing with mutable vars
    var prefixReady = false
    var bodyReady = false
    var skipCount = 0
    for {
      (c, i) <- pattern.zipWithIndex.toList
    } {
      val inPatternChar = numberPatternChars.contains(c)
      if (skipCount == 0) {
        if (c == quoteChar) {
          val nextIdx = pattern.indexOf(quoteChar, i + 1)
          if (nextIdx >= 0) {
            val next = if (nextIdx == i + 1) {
              "'"
            } else {
              pattern.substring(i + 1, nextIdx)
            }
            if (!prefixReady) {
              prefix ++= next
            } else if (!bodyReady) {
              bodyReady = true
              suffix ++= next
            } else {
              suffix ++= next
            }
            skipCount = nextIdx - i
          } else {
            throw new RuntimeException()
          }
        } else {
          if (!prefixReady && !inPatternChar) {
            prefix += c
          } else if (!prefixReady && inPatternChar) {
            prefixReady = true
          }
          if (prefixReady && inPatternChar) {
            body += c
          } else if (prefixReady && !inPatternChar) {
            bodyReady = true
          }
          if (bodyReady && !inPatternChar) {
            suffix += c
          }
        }
      } else {
        skipCount = skipCount - 1
      }
    }

    PatternParts(prefix.toString(), body.toString, suffix.toString)
  }

  def localizeString(str: String, symbols: DecimalFormatSymbols): String = {
    str.map {
      case PatternCharPercent => symbols.getPercent
      case PatternCharMinus   => symbols.getMinusSign
      case PatternCharPerMile => symbols.getPerMill
      case c                  => c
    }
  }

  def toDecimalPatterns(pattern: String): DecimalPatterns = pattern.split(';').toList match {
    case Nil => DecimalPatterns(PatternParts(""), PatternParts(""))
    case p :: Nil => DecimalPatterns(decimalPatternSplit(p), decimalPatternSplit(p))
    case p :: n :: _ => DecimalPatterns(decimalPatternSplit(p), decimalPatternSplit(n))
  }

  def groupingCount(pattern: String): Int = {
    val pat = pattern.filterNot(c => c == PatternCharPercent || !allPatternChars.contains(c))
    val decIndex = pat.lastIndexOf(PatternCharDecimalSeparator)
    val actualDecIndex = if (decIndex < 0) pat.length - 1 else decIndex - 1
    val lastSeparatorIndex = pat.lastIndexOf(PatternCharGroupingSeparator, actualDecIndex)
    if (pat.isEmpty) 3
    else if (lastSeparatorIndex < 0) 0
    else (if (decIndex < 0 ) pat.length - 1 - lastSeparatorIndex else actualDecIndex - lastSeparatorIndex)
  }

  def localizedSymbol(f: DecimalFormat, symbol: Char): Char = {
    symbol match {
      case PatternCharPercent => f.getDecimalFormatSymbols.getPercent
      case PatternCharMinus   => f.getDecimalFormatSymbols.getMinusSign
      case x => x
    }
  }
}
