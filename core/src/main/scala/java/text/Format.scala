package java.text

import AttributedCharacterIterator.Attribute

abstract class Format protected () extends Cloneable {
  def format(obj: AnyRef): String = format(obj, new StringBuffer(), new FieldPosition(0)).toString

  def format(obj: AnyRef, toAppendTo: StringBuffer, pos: FieldPosition): StringBuffer

  def formatToCharacterIterator(obj: AnyRef): AttributedCharacterIterator =
    new Format.EmptyAttributedCharacterIterator

  def parseObject(source: String, pos: ParsePosition): AnyRef

  def parseObject(source: String): AnyRef =
    parseObject(source, new ParsePosition(0))
}

object Format {
  private class EmptyAttributedCharacterIterator extends AttributedCharacterIterator {
    override def getAttributes: java.util.Map[Attribute, AnyRef] =
      new java.util.HashMap()

    override def getAttribute(attribute: Attribute): AnyRef = null

    override def getAllAttributeKeys: java.util.Set[Attribute] =
      new java.util.TreeSet()

    override def getRunLimit: Int = 0

    override def getRunLimit(attribute: Attribute): Int = 0

    override def getRunLimit(attributes: java.util.Set[_ <: Attribute]): Int = 0

    override def getRunStart: Int = 0

    override def getRunStart(attribute: Attribute): Int = 0

    override def getRunStart(attributes: java.util.Set[_ <: Attribute]): Int = 0

    override def next(): Char = 0

    override def setIndex(position: Int): Char = 0

    override def getIndex: Int = 0

    override def last(): Char = 0

    override def getBeginIndex: Int = 0

    override def getEndIndex: Int = 0

    override def current(): Char = 0

    override def previous(): Char = 0

    override def first(): Char = 0
  }

  class Field protected (private[this] val name: String) extends AttributedCharacterIterator.Attribute(name) {

    override def toString(): String = s"${getClass.getName}($name)"
  }
}
