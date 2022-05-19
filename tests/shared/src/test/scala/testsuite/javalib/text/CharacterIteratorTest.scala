package testsuite.javalib.text

import java.text.CharacterIterator

class CharacterIteratorTest extends munit.FunSuite {
  test("done") {
    assertEquals('\uFFFF', CharacterIterator.DONE)
  }
}
