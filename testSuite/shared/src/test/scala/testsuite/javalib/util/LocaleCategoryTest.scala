package testsuite.javalib.util

import java.util.Locale

import testsuite.utils.AssertThrows.expectThrows

class LocaleCategoryTest extends munit.FunSuite {
  import Locale.Category

  test("getOrdinal") {
    assertEquals(0, Category.DISPLAY.ordinal)
    assertEquals(1, Category.FORMAT.ordinal)
  }

  test("getValues") {
    assertEquals(2, Category.values().length)
    assertEquals(Category.DISPLAY, Category.values()(0))
    assertEquals(Category.FORMAT, Category.values()(1))
  }

  test("valueOf") {
    assertEquals(Category.DISPLAY, Category.valueOf("DISPLAY"))
    assertEquals(Category.FORMAT, Category.valueOf("FORMAT"))

    expectThrows(classOf[IllegalArgumentException], Category.valueOf(""))
    expectThrows(classOf[NullPointerException], Category.valueOf(null))
  }

}
