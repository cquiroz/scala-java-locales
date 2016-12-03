package java.text

// Make this a super class so we can match against it
object IgnoreFieldPosition extends FieldPosition(0)

class FieldPosition(private[this] val attribute: Format.Field, private[this] val fieldId: Int) {
  private[this] var beginIndex: Int = 0
  private[this] var endIndex: Int = 0

  def this(attribute: Format.Field) = this(attribute, -1)

  def this(fieldId: Int) = this(null, fieldId)

  def getFieldAttribute(): Format.Field = attribute

  def getField(): Int = fieldId

  def getBeginIndex(): Int = beginIndex

  def getEndIndex(): Int = endIndex

  def setBeginIndex(bi: Int): Unit = beginIndex = bi

  def setEndIndex(ei: Int): Unit = endIndex = ei

  override def equals(other: Any): Boolean = other match {
    case that: FieldPosition =>
      getBeginIndex == that.getBeginIndex &&
      getEndIndex == that.getEndIndex &&
      getFieldAttribute == that.getFieldAttribute &&
      getField == that.getField
    case _ => false
  }

  override def hashCode(): Int = {
    // NOTE, the JVM doesn't use field attribute on hash but it uses on equal
    val state = Seq(getBeginIndex, getEndIndex,/* getFieldAttribute,*/ getField)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }


  override def toString(): String =
    s"java.text.FieldPosition[field=$getField,attribute=$getFieldAttribute,beginIndex=$getBeginIndex,endIndex=$getEndIndex]"
}
