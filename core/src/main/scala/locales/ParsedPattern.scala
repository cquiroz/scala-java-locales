package locales

case class ParsedPattern(
  val positivePrefix: Option[String] = None,
  val positiveSuffix: Option[String] = None,

  val negativePrefix: Option[String] = None,
  val negativeSuffix: Option[String] = None,

  val defaultNegativePrefix: Option[String] = None,
  val defaultNegativeSuffix: Option[String] = None,

  val multiplier: Int = 1, // Might need to test if the multiplier needs to be negative vs positive specific
  val groupingSize: Int = 0,
  val minimumIntegerDigits: Option[Int] = None,
  val minimumFractionDigits: Option[Int] = None,
  val minimumExponentDigits: Option[Int] = None,
  val maximumIntegerDigits: Option[Int] = None,
  val maximumFractionDigits: Option[Int] = None,
  val maximumExponentDigits: Option[Int] = None
)
