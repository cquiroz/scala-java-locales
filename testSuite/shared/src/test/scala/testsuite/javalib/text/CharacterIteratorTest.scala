package testsuite.javalib.text

import java.text.CharacterIterator

import org.junit.Test
import org.junit.Assert._

class CharacterIteratorTest {
  @Test def test_done(): Unit = {
    assertEquals('\uFFFF', CharacterIterator.DONE)
  }
}
