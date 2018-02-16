package testsuite.javalib.text

import java.text.AttributedCharacterIterator.Attribute

import org.junit.Assert._
import org.junit.Test

class AttributedCharacterIteratorTest {
  @Test def test_static_value_to_string(): Unit = {
    assertEquals("java.text.AttributedCharacterIterator$Attribute(language)",
                 Attribute.LANGUAGE.toString)
    assertEquals("java.text.AttributedCharacterIterator$Attribute(reading)",
                 Attribute.READING.toString)
    assertEquals(
      "java.text.AttributedCharacterIterator$Attribute(input_method_segment)",
      Attribute.INPUT_METHOD_SEGMENT.toString)
  }

  @Test def test_equals(): Unit = {
    assertEquals(Attribute.LANGUAGE, Attribute.LANGUAGE)
    assertFalse(Attribute.READING == Attribute.LANGUAGE)
  }
}
