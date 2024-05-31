package java.text

import java.nio.CharBuffer
import java.nio.charset.Charset
import scala.scalanative.libc.stdlib
import scala.scalanative.unsafe._

import Normalizer._

private object NormalizerImpl {

  def normalize(src: CharSequence, form: Form): String =
    if (src == null || form == null)
      throw new NullPointerException
    else
      Zone { implicit z: Zone =>
        import Form._
        import utf8proc._

        val cstr = form match {
          case NFC  => utf8proc_NFC(charSeqToCString(src))
          case NFD  => utf8proc_NFD(charSeqToCString(src))
          case NFKC => utf8proc_NFKC(charSeqToCString(src))
          case NFKD => utf8proc_NFKD(charSeqToCString(src))
        }

        val normalized = fromCString(cstr) // TODO can be further optimized
        stdlib.free(cstr)
        normalized
      }

  private def charSeqToCString(cs: CharSequence)(implicit z: Zone): CString = cs match {
    case str: String => toCString(str)
    case str         =>
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
  def utf8proc_NFD(str:  CString): CString = extern
  def utf8proc_NFC(str:  CString): CString = extern
  def utf8proc_NFKD(str: CString): CString = extern
  def utf8proc_NFKC(str: CString): CString = extern
}
