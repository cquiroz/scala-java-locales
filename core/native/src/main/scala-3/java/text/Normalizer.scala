package java.text

object Normalizer {

  def normalize(src: CharSequence, form: Form): String =
    NormalizerImpl.normalize(src, form)

  def isNormalized(src: CharSequence, form: Form): Boolean =
    normalize(src, form).contentEquals(src)

  enum Form extends java.lang.Enum[Form]:
    case NFC, NFD, NFKC, NFKD

}
