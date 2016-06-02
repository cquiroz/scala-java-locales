package org.scalajs.testsuite.javalib.util

import org.junit.Test
import org.junit.Assert._
import java.util.Locale

import org.scalajs.testsuite.utils.AssertThrows._

class LocaleCategoryTest {
  import Locale.Category

  @Test def test_getOrdinal(): Unit = {
    assertTrue(0 == Category.DISPLAY.ordinal)
    assertTrue(1 == Category.FORMAT.ordinal)
  }

  @Test def test_getValues(): Unit = {
    assertTrue(2 == Category.values().length)
    assertEquals(Category.DISPLAY, Category.values()(0))
    assertEquals(Category.FORMAT, Category.values()(1))
  }

  @Test def test_valueOf(): Unit = {
    assertEquals(Category.DISPLAY, Category.valueOf("DISPLAY"))
    assertEquals(Category.FORMAT, Category.valueOf("FORMAT"))

    expectThrows(classOf[IllegalArgumentException], Category.valueOf(""))
    expectThrows(classOf[NullPointerException], Category.valueOf(null))
  }
}