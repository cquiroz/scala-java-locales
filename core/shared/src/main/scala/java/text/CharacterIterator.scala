package java.text

trait CharacterIterator extends Cloneable {
  def first(): Char

  def last(): Char

  def current(): Char

  def next(): Char

  def previous(): Char

  def setIndex(position: Int): Char

  def getBeginIndex(): Int

  def getEndIndex(): Int

  def getIndex(): Int
}

object CharacterIterator {
  val DONE: Char = '\uFFFF'
}
