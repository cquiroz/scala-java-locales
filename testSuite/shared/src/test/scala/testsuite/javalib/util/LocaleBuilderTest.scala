package testsuite.javalib.util

import java.util.{ IllformedLocaleException, Locale }

import testsuite.utils.Platform
import testsuite.utils.AssertThrows.expectThrows

class LocaleBuilderTest extends munit.FunSuite {
  test("build_with_language") {
    val b1      = new Locale.Builder()
    val locale1 = b1.setLanguage("en").build
    assertEquals("en", locale1.getLanguage)

    // Null should reset
    val b2      = new Locale.Builder()
    val locale2 = b2.setLanguage(null).build
    assertEquals("", locale2.getLanguage)

    val b3      = new Locale.Builder()
    val locale3 = b3.setLanguage("en").setLanguage("").build
    assertEquals("", locale3.getLanguage)

    // Check for compliance
    expectThrows(
      classOf[IllformedLocaleException],
      new Locale.Builder().setLanguage("toolongtobevalid")
    )
  }

  test("build_language_canonicalization") {
    val b1     = new Locale.Builder()
    val locale = b1.setLanguage("En").build
    assertEquals("en", locale.getLanguage)
  }

  test("build_with_script") {
    val b1     = new Locale.Builder()
    val locale = b1.setScript("Cyrl").build
    assertEquals("Cyrl", locale.getScript)

    // null resets
    val b2      = new Locale.Builder()
    val locale2 = b2.setScript(null).build
    assertEquals("", locale2.getScript)

    val b3      = new Locale.Builder()
    val locale3 = b3.setScript("Cyrl").setScript("").build
    assertEquals("", locale3.getScript)

    // Check for compliance, too long
    expectThrows(
      classOf[IllformedLocaleException],
      new Locale.Builder().setScript("toolongtobevalid")
    )

    // Check for compliance, too short
    expectThrows(classOf[IllformedLocaleException], new Locale.Builder().setScript("ts"))
  }

  test("build_script_canonicalization") {
    val b1     = new Locale.Builder()
    val locale = b1.setScript("cyrl").build
    assertEquals("Cyrl", locale.getScript)
  }

  test("build_with_region") {
    val b1     = new Locale.Builder()
    val locale = b1.setRegion("US").build
    assertEquals("US", locale.getCountry)

    // null resets
    val b2      = new Locale.Builder()
    val locale2 = b2.setRegion(null).build
    assertEquals("", locale2.getCountry)

    val b3      = new Locale.Builder()
    val locale3 = b3.setRegion("US").setRegion("").build
    assertEquals("", locale3.getCountry)

    val b4      = new Locale.Builder()
    val locale4 = b4.setRegion("029").build
    assertEquals("029", locale4.getCountry)

    // Check for compliance, too long
    expectThrows(
      classOf[IllformedLocaleException],
      new Locale.Builder().setRegion("toolongtobevalid")
    )
    expectThrows(classOf[IllformedLocaleException], new Locale.Builder().setRegion("1234"))

    // Check for compliance, too short
    expectThrows(classOf[IllformedLocaleException], new Locale.Builder().setRegion("t"))
    expectThrows(classOf[IllformedLocaleException], new Locale.Builder().setRegion("1"))
  }

  test("build_with_variant") {
    val b1     = new Locale.Builder()
    val locale = b1.setVariant("polyton").build
    assertEquals("polyton", locale.getVariant)

    // null resets
    val b2      = new Locale.Builder()
    val locale2 = b2.setVariant(null).build
    assertEquals("", locale2.getVariant)

    // Some examples taken from IANA Subtag registry
    val cases = List("1606nict", "1901", "baku1926", "fonxsamp", "luna1918")
    cases.foreach { v =>
      val b      = new Locale.Builder()
      val locale = b.setVariant(v).build
      assertEquals(v, locale.getVariant)
    }

    // Multiple variants are allowed
    cases.zip(cases).foreach {
      case (c1, c2) =>
        val b       = new Locale.Builder()
        val locale1 = b.setVariant(s"$c1-$c2").build
        assertEquals(s"${c1}_$c2", locale1.getVariant)

        val locale2 = b.setVariant(s"${c1}_$c2").build
        assertEquals(s"${c1}_$c2", locale2.getVariant)
    }

    val b4      = new Locale.Builder()
    val locale4 = b4.setVariant("VALENCIA").build
    assertEquals("VALENCIA", locale4.getVariant)

    // Check for compliance, too long
    expectThrows(classOf[IllformedLocaleException], new Locale.Builder().setVariant("four"))
    expectThrows(classOf[IllformedLocaleException], new Locale.Builder().setVariant("abcde!"))

    // Check for compliance, too short
    expectThrows(classOf[IllformedLocaleException], new Locale.Builder().setVariant("t"))
    expectThrows(classOf[IllformedLocaleException], new Locale.Builder().setVariant("1"))
  }

  test("build_with_extensions") {
    val b1     = new Locale.Builder()
    val locale = b1.setExtension('a', "ca-japanese").build
    assertEquals("ca-japanese", locale.getExtension('a'))

    // null resets
    val b2 = new Locale.Builder()
    val locale2 =
      b2.setExtension('a', "ca-japanese").setExtension('a', "").build
    assert(null == locale2.getExtension('a'))

    // Check for compliance on the keys
    expectThrows(classOf[IllformedLocaleException], new Locale.Builder().setExtension('!', "abc"))

    // Check for compliance on the value
    expectThrows(classOf[IllformedLocaleException], new Locale.Builder().setExtension('a', "a"))
    expectThrows(
      classOf[IllformedLocaleException],
      new Locale.Builder().setExtension('a', "abcdefghi")
    )
  }

  test("extensions_canonalization") {
    val b1     = new Locale.Builder()
    val locale = b1.setExtension('a', "Ca-Japanese").build
    assertEquals("ca-japanese", locale.getExtension('a'))
  }

  test("build_with_unicode_extensions") {
    val b1 = new Locale.Builder()
    val locale = b1
      .setUnicodeLocaleKeyword("nu", "thai")
      .setUnicodeLocaleKeyword("ok", "")
      .build
    assert(locale.getUnicodeLocaleKeys.contains("nu"))
    assert(locale.getUnicodeLocaleKeys.contains("ok"))
    assertEquals("thai", locale.getUnicodeLocaleType("nu"))
    assertEquals("", locale.getUnicodeLocaleType("ok"))
    assert(null == locale.getUnicodeLocaleType("ko"))

    // Check that the extensions is propagated to the general extension
    assert(locale.getExtensionKeys.contains(Locale.UNICODE_LOCALE_EXTENSION))
    assertEquals("nu-thai-ok", locale.getExtension(Locale.UNICODE_LOCALE_EXTENSION))

    // Check for compliance on the keys
    expectThrows(
      classOf[NullPointerException],
      new Locale.Builder().setUnicodeLocaleKeyword(null, "thai")
    )
    // key too short
    expectThrows(
      classOf[IllformedLocaleException],
      new Locale.Builder().setUnicodeLocaleKeyword("a", "thai")
    )
    // value too short
    expectThrows(
      classOf[IllformedLocaleException],
      new Locale.Builder().setUnicodeLocaleKeyword("nu", "th")
    )
    // value too long
    expectThrows(
      classOf[IllformedLocaleException],
      new Locale.Builder().setUnicodeLocaleKeyword("nu", "toolongvalue")
    )
  }

  test("replace_unicode_extensions") {
    val b1 = new Locale.Builder()
    val locale1 = b1
      .setUnicodeLocaleKeyword("nu", "thai")
      .setUnicodeLocaleKeyword("ok", "")
      .build
    assertEquals("thai", locale1.getUnicodeLocaleType("nu"))
    val locale2 =
      b1.setExtension(Locale.UNICODE_LOCALE_EXTENSION, "newvalue").build
    assert(locale2.getUnicodeLocaleKeys.isEmpty)
    assert(null == locale2.getUnicodeLocaleType("nu"))

    // Check that the extensions is propagated to the general extension
    assert(locale2.getExtensionKeys.contains(Locale.UNICODE_LOCALE_EXTENSION))
    assertEquals("newvalue", locale2.getExtension(Locale.UNICODE_LOCALE_EXTENSION))
  }

  test("build_with_unicode_attributes") {
    val b1      = new Locale.Builder()
    val locale1 = b1.addUnicodeLocaleAttribute("attr").build
    assert(locale1.getUnicodeLocaleAttributes.contains("attr"))

    // Check for compliance on the attribute
    expectThrows(
      classOf[NullPointerException],
      new Locale.Builder().addUnicodeLocaleAttribute(null)
    )
    expectThrows(
      classOf[IllformedLocaleException],
      new Locale.Builder().addUnicodeLocaleAttribute("toolongvalue")
    )
  }

  test("remove_unicode_attribute") {
    val b1 = new Locale.Builder()
    val locale1 = b1
      .addUnicodeLocaleAttribute("attr")
      .removeUnicodeLocaleAttribute("attr")
      .build
    assert(locale1.getUnicodeLocaleAttributes.isEmpty)

    // Check for compliance on the attribute
    if (Platform.executingInJVM) {
      // Against the javadocs the JVM throws an IllformedLocaleException
      expectThrows(
        classOf[IllformedLocaleException],
        new Locale.Builder().removeUnicodeLocaleAttribute(null)
      )
    } else {
      // Scala.js follows the spec
      expectThrows(
        classOf[NullPointerException],
        new Locale.Builder().removeUnicodeLocaleAttribute(null)
      )
    }
    expectThrows(
      classOf[IllformedLocaleException],
      new Locale.Builder().removeUnicodeLocaleAttribute("toolongvalue")
    )
  }

  test("clear") {
    val b       = new Locale.Builder()
    val locale1 = b.setLanguage("fi").setRegion("FI").setVariant("POSIX").build
    assertEquals("fi", locale1.getLanguage)
    assertEquals("FI", locale1.getCountry)
    assertEquals("POSIX", locale1.getVariant)

    val locale2 = b.clear().build
    assertEquals("", locale2.getLanguage)
    assertEquals("", locale2.getCountry)
    assertEquals("", locale2.getVariant)
  }

  test("clear_extensions") {
    val b = new Locale.Builder()
    val locale1 = b
      .setLanguage("es")
      .setRegion("CL")
      .setExtension('a', "ca-japanese")
      .build
    assertEquals("ca-japanese", locale1.getExtension('a'))
    assertEquals("es", locale1.getLanguage)
    assertEquals("CL", locale1.getCountry)
    val locale2 = b.clearExtensions().build
    assert(null == locale2.getExtension('a'))
    assertEquals("es", locale1.getLanguage)
    assertEquals("CL", locale1.getCountry)
  }
}
