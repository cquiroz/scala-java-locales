package locales.cldr.fallback

import locales.cldr._

object LocalesProvider extends LocalesProvider {
  def root: LDML                 = data._root
  def ldmls: Map[String, LDML]   =
    data._all_.all.map {
      case l => (l.languageTag, l)
    }.toMap
  def latn: NumberingSystem      = data.numericsystems.latn
  def currencyData: CurrencyData =
    new CurrencyData(
      Seq.empty,
      Seq.empty,
      Seq.empty,
      Seq.empty
    )
  def metadata: CLDRMetadata     =
    new CLDRMetadata(Array.empty, Map.empty, Array.empty, Map.empty, Array.empty)
}
