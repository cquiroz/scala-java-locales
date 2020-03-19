package java.text

trait AttributedCharacterIterator extends CharacterIterator {
  def getRunStart(): Int

  def getRunStart(attribute: AttributedCharacterIterator.Attribute): Int

  def getRunStart(attributes: java.util.Set[_ <: AttributedCharacterIterator.Attribute]): Int

  def getRunLimit(): Int

  def getRunLimit(attribute: AttributedCharacterIterator.Attribute): Int

  def getRunLimit(attributes: java.util.Set[_ <: AttributedCharacterIterator.Attribute]): Int

  def getAttributes(): java.util.Map[AttributedCharacterIterator.Attribute, AnyRef]

  def getAttribute(attribute: AttributedCharacterIterator.Attribute): AnyRef

  def getAllAttributeKeys(): java.util.Set[AttributedCharacterIterator.Attribute]
}

object AttributedCharacterIterator {
  class Attribute protected (private[this] val name: String) {
    override final def equals(that: Any): Boolean = that match {
      case t: Attribute => this.eq(t) // As per javadocs
      case _            => false
    }

    override final def hashCode(): Int = name.hashCode

    override def toString: String = s"java.text.AttributedCharacterIterator$$Attribute($getName)"

    protected def getName(): String = name
  }

  object Attribute {
    val LANGUAGE: Attribute             = new Attribute("language")
    val READING: Attribute              = new Attribute("reading")
    val INPUT_METHOD_SEGMENT: Attribute = new Attribute("input_method_segment")
  }
}
