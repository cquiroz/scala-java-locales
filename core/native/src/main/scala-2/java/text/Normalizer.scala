package java.text

object Normalizer {

  def normalize(src: CharSequence, form: Form): String =
    NormalizerImpl.normalize(src, form)

  def isNormalized(src: CharSequence, form: Form): Boolean =
    normalize(src, form).contentEquals(src)

  final class Form private (name: String, ordinal: Int) extends Enum[Form](name, ordinal)

  object Form {
    final val NFC: Form  = new Form("NFC", 0)
    final val NFD: Form  = new Form("NFD", 1)
    final val NFKC: Form = new Form("NFKC", 2)
    final val NFKD: Form = new Form("NFKD", 3)
  }

}
