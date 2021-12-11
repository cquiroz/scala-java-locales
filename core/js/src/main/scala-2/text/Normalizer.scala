package java.text

import scala.scalajs.js

object Normalizer {

  @inline def normalize(src: CharSequence, form: Form): String =
    if (src == null || form == null)
      throw new NullPointerException
    else
      src.toString().asInstanceOf[js.Dynamic].normalize(form.name()).asInstanceOf[String]

  @inline def isNormalized(src: CharSequence, form: Form): Boolean =
    normalize(src, form).contentEquals(src)

  final class Form private (name: String, ordinal: Int) extends Enum[Form](name, ordinal)

  object Form {
    final val NFC: Form  = new Form("NFC", 0)
    final val NFD: Form  = new Form("NFD", 1)
    final val NFKC: Form = new Form("NFKC", 2)
    final val NFKD: Form = new Form("NFKD", 3)
  }

}
