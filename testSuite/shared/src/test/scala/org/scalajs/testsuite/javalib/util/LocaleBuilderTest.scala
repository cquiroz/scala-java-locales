package org.scalajs.testsuite.javalib.util

import java.util.{IllformedLocaleException, Locale}

import org.junit.Test
import org.junit.Assert._

import org.scalajs.testsuite.utils.AssertThrows._
import org.scalajs.testsuite.utils.Platform

class LocaleBuilderTest {
  @Test def test_build_with_language(): Unit = {
    val b1 = new Locale.Builder()
    val locale1 = b1.setLanguage("en").build
    assertEquals("en", locale1.getLanguage)

    // Null should reset
    val b2 = new Locale.Builder()
    val locale2 = b2.setLanguage(null).build
    assertEquals("", locale2.getLanguage)

    val b3 = new Locale.Builder()
    val locale3 = b3.setLanguage("en").setLanguage("").build
    assertEquals("", locale3.getLanguage)

    // Check for compliance
    expectThrows(classOf[IllformedLocaleException],
      new Locale.Builder().setLanguage("toolongtobevalid"))
  }

  @Test def test_build_language_canonicalization(): Unit = {
    val b1 = new Locale.Builder()
    val locale = b1.setLanguage("En").build
    assertEquals("en", locale.getLanguage)
  }

  @Test def test_build_with_script(): Unit = {
    val b1 = new Locale.Builder()
    val locale = b1.setScript("Cyrl").build
    assertEquals("Cyrl", locale.getScript)

    // null resets
    val b2 = new Locale.Builder()
    val locale2 = b2.setScript(null).build
    assertEquals("", locale2.getScript)

    val b3 = new Locale.Builder()
    val locale3 = b3.setScript("Cyrl").setScript("").build
    assertEquals("", locale3.getScript)

    // Check for compliance, too long
    expectThrows(classOf[IllformedLocaleException],
      new Locale.Builder().setScript("toolongtobevalid"))

    // Check for compliance, too short
    expectThrows(classOf[IllformedLocaleException],
      new Locale.Builder().setScript("ts"))
  }

  @Test def test_build_script_canonicalization(): Unit = {
    val b1 = new Locale.Builder()
    val locale = b1.setScript("cyrl").build
    assertEquals("Cyrl", locale.getScript)
  }

  @Test def test_build_with_region(): Unit = {
    val b1 = new Locale.Builder()
    val locale = b1.setRegion("US").build
    assertEquals("US", locale.getCountry)

    // null resets
    val b2 = new Locale.Builder()
    val locale2 = b2.setRegion(null).build
    assertEquals("", locale2.getCountry)

    val b3 = new Locale.Builder()
    val locale3 = b3.setRegion("US").setRegion("").build
    assertEquals("", locale3.getCountry)

    val b4 = new Locale.Builder()
    val locale4 = b4.setRegion("029").build
    assertEquals("029", locale4.getCountry)

    // Check for compliance, too long
    expectThrows(classOf[IllformedLocaleException],
      new Locale.Builder().setRegion("toolongtobevalid"))
    expectThrows(classOf[IllformedLocaleException],
      new Locale.Builder().setRegion("1234"))

    // Check for compliance, too short
    expectThrows(classOf[IllformedLocaleException],
      new Locale.Builder().setRegion("t"))
    expectThrows(classOf[IllformedLocaleException],
      new Locale.Builder().setRegion("1"))
  }

  @Test def test_build_with_variant(): Unit = {
    val b1 = new Locale.Builder()
    val locale = b1.setVariant("polyton").build
    assertEquals("polyton", locale.getVariant)

    // null resets
    val b2 = new Locale.Builder()
    val locale2 = b2.setVariant(null).build
    assertEquals("", locale2.getVariant)

    // Some examples taken from IANA Subtag registry
    val cases = List("1606nict", "1901", "baku1926", "fonxsamp", "luna1918")
    cases.foreach { v =>
      val b = new Locale.Builder()
      val locale = b.setVariant(v).build
      assertEquals(v, locale.getVariant)
    }

    // Multiple variants are allowed
    cases.zip(cases).foreach { case (c1, c2) =>
      val b = new Locale.Builder()
      val locale1 = b.setVariant(s"$c1-$c2").build
      assertEquals(s"${c1}_$c2", locale1.getVariant)

      val locale2 = b.setVariant(s"${c1}_$c2").build
      assertEquals(s"${c1}_$c2", locale2.getVariant)
    }

    val b4 = new Locale.Builder()
    val locale4 = b4.setVariant("VALENCIA").build
    assertEquals("VALENCIA", locale4.getVariant)

    // Check for compliance, too long
    expectThrows(classOf[IllformedLocaleException],
      new Locale.Builder().setVariant("four"))
    expectThrows(classOf[IllformedLocaleException],
      new Locale.Builder().setVariant("abcde!"))

    // Check for compliance, too short
    expectThrows(classOf[IllformedLocaleException],
      new Locale.Builder().setVariant("t"))
    expectThrows(classOf[IllformedLocaleException],
      new Locale.Builder().setVariant("1"))
  }

  @Test def test_build_with_extensions(): Unit = {
    val b1 = new Locale.Builder()
    val locale = b1.setExtension('a', "ca-japanese").build
    assertEquals("ca-japanese", locale.getExtension('a'))

    // null resets
    val b2 = new Locale.Builder()
    val locale2 = b2.setExtension('a', "ca-japanese").setExtension('a', "")
        .build
    assertNull(locale2.getExtension('a'))

    // Check for compliance on the keys
    expectThrows(classOf[IllformedLocaleException],
      new Locale.Builder().setExtension('!', "abc"))

    // Check for compliance on the value
    expectThrows(classOf[IllformedLocaleException],
      new Locale.Builder().setExtension('a', "a"))
    expectThrows(classOf[IllformedLocaleException],
      new Locale.Builder().setExtension('a', "abcdefghi"))
  }

  @Test def test_extensions_canonalization(): Unit = {
    val b1 = new Locale.Builder()
    val locale = b1.setExtension('a', "Ca-Japanese").build
    assertEquals("ca-japanese", locale.getExtension('a'))
  }

  @Test def test_build_with_unicode_extensions(): Unit = {
    val b1 = new Locale.Builder()
    val locale = b1.setUnicodeLocaleKeyword("nu", "thai")
        .setUnicodeLocaleKeyword("ok", "").build
    assertTrue(locale.getUnicodeLocaleKeys.contains("nu"))
    assertTrue(locale.getUnicodeLocaleKeys.contains("ok"))
    assertEquals("thai", locale.getUnicodeLocaleType("nu"))
    assertEquals("", locale.getUnicodeLocaleType("ok"))
    assertNull(locale.getUnicodeLocaleType("ko"))

    // Check that the extensions is propagated to the general extension
    assertTrue(locale.getExtensionKeys
        .contains(Locale.UNICODE_LOCALE_EXTENSION))
    assertEquals("nu-thai-ok", locale
        .getExtension(Locale.UNICODE_LOCALE_EXTENSION))

    // Check for compliance on the keys
    expectThrows(classOf[NullPointerException],
      new Locale.Builder().setUnicodeLocaleKeyword(null, "thai"))
    // key too short
    expectThrows(classOf[IllformedLocaleException],
      new Locale.Builder().setUnicodeLocaleKeyword("a", "thai"))
    // value too short
    expectThrows(classOf[IllformedLocaleException],
      new Locale.Builder().setUnicodeLocaleKeyword("nu", "th"))
    // value too long
    expectThrows(classOf[IllformedLocaleException],
      new Locale.Builder().setUnicodeLocaleKeyword("nu", "toolongvalue"))
  }

  @Test def test_replace_unicode_extensions(): Unit = {
    val b1 = new Locale.Builder()
    val locale1 = b1.setUnicodeLocaleKeyword("nu", "thai")
        .setUnicodeLocaleKeyword("ok", "").build
    assertEquals("thai", locale1.getUnicodeLocaleType("nu"))
    val locale2 = b1.setExtension(Locale.UNICODE_LOCALE_EXTENSION, "newvalue")
        .build
    assertTrue(locale2.getUnicodeLocaleKeys.isEmpty)
    assertNull(locale2.getUnicodeLocaleType("nu"))

    // Check that the extensions is propagated to the general extension
    assertTrue(locale2.getExtensionKeys
        .contains(Locale.UNICODE_LOCALE_EXTENSION))
    assertEquals("newvalue", locale2
        .getExtension(Locale.UNICODE_LOCALE_EXTENSION))
  }

  @Test def test_build_with_unicode_attributes(): Unit = {
    val b1 = new Locale.Builder()
    val locale1 = b1.addUnicodeLocaleAttribute("attr").build
    assertTrue(locale1.getUnicodeLocaleAttributes.contains("attr"))

    // Check for compliance on the attribute
    expectThrows(classOf[NullPointerException],
      new Locale.Builder().addUnicodeLocaleAttribute(null))
    expectThrows(classOf[IllformedLocaleException],
      new Locale.Builder().addUnicodeLocaleAttribute("toolongvalue"))
  }

  @Test def test_remove_unicode_attribute(): Unit = {
    val b1 = new Locale.Builder()
    val locale1 = b1.addUnicodeLocaleAttribute("attr")
        .removeUnicodeLocaleAttribute("attr").build
    assertTrue(locale1.getUnicodeLocaleAttributes.isEmpty)

    // Check for compliance on the attribute
    if (Platform.executingInJVM) {
      // Against the javadocs the JVM throws an IllformedLocaleException
      expectThrows(classOf[IllformedLocaleException],
        new Locale.Builder().removeUnicodeLocaleAttribute(null))
    } else {
      // Scala.js follows the spec
      expectThrows(classOf[NullPointerException],
        new Locale.Builder().removeUnicodeLocaleAttribute(null))
    }
    expectThrows(classOf[IllformedLocaleException],
      new Locale.Builder().removeUnicodeLocaleAttribute("toolongvalue"))
  }

  @Test def test_clear(): Unit = {
    val b = new Locale.Builder()
    val locale1 = b.setLanguage("fi").setRegion("FI").setVariant("POSIX").build
    assertEquals("fi", locale1.getLanguage)
    assertEquals("FI", locale1.getCountry)
    assertEquals("POSIX", locale1.getVariant)

    val locale2 = b.clear().build
    assertEquals("", locale2.getLanguage)
    assertEquals("", locale2.getCountry)
    assertEquals("", locale2.getVariant)
  }

  @Test def test_clear_extensions():Unit = {
    val b = new Locale.Builder()
    val locale1 = b.setLanguage("es").setRegion("CL")
        .setExtension('a', "ca-japanese").build
    assertEquals("ca-japanese", locale1.getExtension('a'))
    assertEquals("es", locale1.getLanguage)
    assertEquals("CL", locale1.getCountry)
    val locale2 = b.clearExtensions().build
    assertNull(locale2.getExtension('a'))
    assertEquals("es", locale1.getLanguage)
    assertEquals("CL", locale1.getCountry)
  }

}
