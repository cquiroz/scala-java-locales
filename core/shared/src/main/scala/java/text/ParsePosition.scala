package java.text

class ParsePosition(private[this] var index: Int) {
  private[this] var errorIndex: Int = -1

  def getIndex(): Int = index

  def setIndex(index: Int): Unit = this.index = index

  def setErrorIndex(ei: Int): Unit = this.errorIndex = ei

  def getErrorIndex(): Int = errorIndex

  override def equals(other: Any): Boolean = other match {
    case that: ParsePosition =>
      getErrorIndex == that.getErrorIndex &&
        getIndex == that.getIndex
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(getErrorIndex, getIndex)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }

  override def toString(): String =
    s"java.text.ParsePosition[index=$getIndex,errorIndex=$getErrorIndex]"
}
