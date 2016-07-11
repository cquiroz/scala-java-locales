package java.text

abstract class Format protected () extends Cloneable {
  def format(obj: AnyRef): String = format(obj, new StringBuffer(), new FieldPosition(0)).toString()

  def format(obj: AnyRef, toAppendTo: StringBuffer, pos: FieldPosition): StringBuffer

  // TODO Implement
  //def formatToCharacterIterator(obj: AnyRef): AttributedCharacterIterator

  //def parseObject(source: String, pos: ParsePosition): AnyRef

  //def parseObject(source: String): AnyRef
}

object Format {
  class Field protected (private[this] val name: String) extends AttributedCharacterIterator.Attribute(name) {

    override def toString = s"${getClass.getName}($name)"
  }
}
