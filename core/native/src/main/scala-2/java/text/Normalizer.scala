package java.text

import java.nio.CharBuffer
import java.nio.charset.Charset
import scala.scalanative.libc.stdlib
import scala.scalanative.unsafe._

object Normalizer {

  def normalize(src: CharSequence, form: Form): String =
    if (src == null || form == null)
      throw new NullPointerException
    else
      Zone { implicit z =>
        import Form._
        import utf8proc._

        val cstr = form match {
          case NFC  => utf8proc_NFC(toCString(src))
          case NFD  => utf8proc_NFD(toCString(src))
          case NFKC => utf8proc_NFKC(toCString(src))
          case NFKD => utf8proc_NFKD(toCString(src))
        }

        val normalized = fromCString(cstr) // TODO can be further optimized
        stdlib.free(cstr)
        normalized
      }

  def isNormalized(src: CharSequence, form: Form): Boolean =
    normalize(src, form).contentEquals(src)

  sealed abstract class Form private (name: String, ordinal: Int) extends Enum[Form](name, ordinal)

  object Form {
    case object NFC  extends Form("NFC", 0)
    case object NFD  extends Form("NFD", 1)
    case object NFKC extends Form("NFKC", 2)
    case object NFKD extends Form("NFKD", 3)
  }

  private def toCString(str: CharSequence)(implicit z: Zone): CString = {
    import scalanative.unsigned._

    val encoder = Charset.defaultCharset().newEncoder()
    val cb      = CharBuffer.wrap(str)
    val bb      = encoder.encode(cb)

    val n    = bb.limit()
    val cstr = z.alloc((n + 1).toULong)

    var i = 0
    while (i < n) {
      !(cstr + i.toLong) = bb.get(i)
      i += 1
    }
    !(cstr + i.toLong) = 0.toByte

    cstr
  }

}

@link("utf8proc")
@extern
private object utf8proc {
  def utf8proc_NFD(str: CString): CString  = extern
  def utf8proc_NFC(str: CString): CString  = extern
  def utf8proc_NFKD(str: CString): CString = extern
  def utf8proc_NFKC(str: CString): CString = extern
}
