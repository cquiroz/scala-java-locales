package java.text

class ParseException(private[this] val s: String, private[this] val errorOffset: Int)
    extends Exception(s) {

  def getErrorOffset(): Int = errorOffset
}
