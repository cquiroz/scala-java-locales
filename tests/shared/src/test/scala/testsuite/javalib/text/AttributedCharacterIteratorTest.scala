package testsuite.javalib.text

import java.text.AttributedCharacterIterator.Attribute

class AttributedCharacterIteratorTest extends munit.FunSuite {
  test("static_value_to_string") {
    assertEquals(
      s"java.text.AttributedCharacterIterator$$Attribute(language)",
      Attribute.LANGUAGE.toString
    )
    assertEquals(
      s"java.text.AttributedCharacterIterator$$Attribute(reading)",
      Attribute.READING.toString
    )
    assertEquals(
      s"java.text.AttributedCharacterIterator$$Attribute(input_method_segment)",
      Attribute.INPUT_METHOD_SEGMENT.toString
    )
  }

  test("equals") {
    assertEquals(Attribute.LANGUAGE, Attribute.LANGUAGE)
    assert(Attribute.READING != Attribute.LANGUAGE)
  }
}
