package org.scalajs.testsuite.javalib.text

import java.text.DateFormatSymbols

import org.junit.Assert._
import org.junit.{Before, Test}
import org.scalajs.testsuite.utils.AssertThrows._
import org.scalajs.testsuite.utils.LocaleTestSetup
import org.scalajs.testsuite.utils.Platform

class DateFormatSymbolsTest extends LocaleTestSetup {
  // Clean up the locale database, there are different implementations for
  // the JVM and JS
  @Before def cleanup: Unit = super.cleanDatabase

  def test_setter(get: DateFormatSymbols => Array[String],
      set: (DateFormatSymbols, Array[String]) => Unit): Unit = {
    val dfs = new DateFormatSymbols()
    expectThrows(classOf[NullPointerException], set(dfs, null))
    val value = Array("a", "b")
    set(dfs, value)
    assertArrayEquals(Array[AnyRef](value: _*), Array[AnyRef](get(dfs): _*))
    // Check that the passed array is copied
    value(0) = "c"
    assertEquals("a", get(dfs)(0))
  }

  @Test def test_zone_strings(): Unit = {
    val dfs = new DateFormatSymbols()
    expectThrows(classOf[NullPointerException], dfs.setZoneStrings(null))
    val zonesTooFew = Array(Array("a", "b"), Array("c", "d"))
    expectThrows(classOf[IllegalArgumentException],
                 dfs.setZoneStrings(zonesTooFew))
    val zones =
      Array(Array("a", "b", "c", "d", "e"), Array("f", "g", "h", "i", "j"))
    dfs.setZoneStrings(zones)
    assertArrayEquals(Array[AnyRef](zones: _*),
                      Array[AnyRef](dfs.getZoneStrings(): _*))
    // Check that the passed array is copied
    zones(0)(0) = "e"
    assertEquals("a", dfs.getZoneStrings()(0)(0))
  }

  @Test def test_setters(): Unit = {
    test_setter(_.getEras, _.setEras(_))
    test_setter(_.getMonths, _.setMonths(_))
    test_setter(_.getShortMonths, _.setShortMonths(_))
    test_setter(_.getWeekdays(), _.setWeekdays(_))
    test_setter(_.getShortWeekdays(), _.setShortWeekdays(_))
    test_setter(_.getAmPmStrings(), _.setAmPmStrings(_))

    val dfs = new DateFormatSymbols()
    dfs.setLocalPatternChars("abc")
    assertEquals("abc", dfs.getLocalPatternChars())
    expectThrows(classOf[NullPointerException], dfs.setLocalPatternChars(null))
  }

  @Test def test_equals(): Unit = {
    val dfs = new DateFormatSymbols()
    assertEquals(dfs, dfs)
    assertEquals(dfs, new DateFormatSymbols())
    dfs.setEras(Array("a", "b"))
    assertFalse(dfs.equals(new DateFormatSymbols()))
    assertFalse(dfs.equals(null))
    assertFalse(dfs.equals(1))
  }

  @Test def test_hash_code(): Unit = {
    val dfs = new DateFormatSymbols()
    assertEquals(dfs.hashCode, dfs.hashCode)
    assertEquals(dfs.hashCode, new DateFormatSymbols().hashCode)
    dfs.setEras(Array("a", "b"))
    assertFalse(dfs.hashCode.equals(new DateFormatSymbols().hashCode))
  }

  @Test def test_clone(): Unit = {
    val dfs = new DateFormatSymbols()
    val cloned = dfs.clone
    assertEquals(dfs, cloned)
    assertNotSame(dfs, cloned)
  }
}
