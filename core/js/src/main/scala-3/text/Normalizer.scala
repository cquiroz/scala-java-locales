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

  enum Form extends java.lang.Enum[Form]:
    case NFC, NFD, NFKC, NFKD

}
