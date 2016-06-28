package org.scalajs.testsuite.javalib.text

import java.text.DateFormatSymbols
import java.util.Locale

import org.junit.Assert._
import org.junit.{Before, Ignore, Test}
import org.scalajs.testsuite.utils.AssertThrows.expectThrows
import org.scalajs.testsuite.utils.LocaleTestSetup
import org.scalajs.testsuite.utils.Platform

import scala.scalajs.locale.LocaleRegistry
import scala.scalajs.locale.ldml.data.all._

class DateFormatSymbolsTest extends LocaleTestSetup {
  // Clean up the locale database, there are different implementations for
  // the JVM and JS
  @Before def cleanup: Unit = super.cleanDatabase

  @Test def test_setters(): Unit = {
    val dfs = new DateFormatSymbols()
    expectThrows(classOf[NullPointerException], dfs.setEras(null))
    val eras = Array("a", "b")
    dfs.setEras(eras)
    assertArrayEquals(Array[AnyRef](eras: _*), Array[AnyRef](dfs.getEras(): _*))
    // Check that the passed array is copied as a reference
    eras(0) = "c"
    //assertEquals("c", eras(0))
    assertEquals("a", dfs.getEras()(0))
/*    assertEquals('1', dfs.getZeroDigit)
    dfs.setGroupingSeparator('1')
    assertEquals('1', dfs.getGroupingSeparator)
    dfs.setDecimalSeparator('1')
    assertEquals('1', dfs.getDecimalSeparator)
    dfs.setPerMill('1')
    assertEquals('1', dfs.getPerMill)
    dfs.setPercent('1')
    assertEquals('1', dfs.getPercent)
    dfs.setDigit('1')
    assertEquals('1', dfs.getDigit)
    dfs.setPatternSeparator('1')
    assertEquals('1', dfs.getPatternSeparator)
    dfs.setMinusSign('1')
    assertEquals('1', dfs.getMinusSign)

    dfs.setInfinity(null)
    assertNull(dfs.getInfinity)
    dfs.setInfinity("Inf")
    assertEquals("Inf", dfs.getInfinity)

    dfs.setNaN(null)
    assertNull(dfs.getNaN)
    dfs.setNaN("nan")
    assertEquals("nan", dfs.getNaN)

    expectThrows(classOf[NullPointerException], dfs.setExponentSeparator(null))
    dfs.setExponentSeparator("exp")
    assertEquals("exp", dfs.getExponentSeparator)*/
  }

/*  @Test def test_clone(): Unit = {
    val dfs = new DateFormatSymbols()
    assertEquals(dfs, dfs.clone())
    assertNotSame(dfs, dfs.clone())
  }

  @Test def test_equals(): Unit = {
    val dfs = new DateFormatSymbols()
    assertEquals(dfs, dfs)
    assertSame(dfs, dfs)
    assertFalse(dfs.equals(null))
    assertFalse(dfs.equals(1))
    val dfs2 = new DateFormatSymbols()
    assertEquals(dfs, dfs2)
    assertNotSame(dfs, dfs2)
    dfs2.setDigit('i')
    assertFalse(dfs.equals(dfs2))
  }

  @Ignore @Test def test_hash_code(): Unit = {
    val dfs = new DateFormatSymbols()
    assertEquals(dfs.hashCode, dfs.hashCode)
    val dfs2 = new DateFormatSymbols()
    assertEquals(dfs.hashCode, dfs2.hashCode)
    dfs2.setExponentSeparator("abc")
    // These tests should fail but they pass on the JVM
    assertEquals(dfs.hashCode, dfs2.hashCode)
    standardLocalesData.filter(_._1 != Locale.ROOT).foreach {
      case (l, symbols) =>
        val df = DateFormatSymbols.getInstance(l)
        assertFalse(dfs.hashCode.equals(df.hashCode))
    }
  }*/
}
