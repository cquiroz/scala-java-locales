package org.scalajs.testsuite.javalib.util

import java.util.Calendar

import org.junit.Test
import org.junit.Assert._

class CalendarTest {
  @Test def test_day_fields(): Unit = {
    assertEquals(1, Calendar.SUNDAY)
    assertEquals(2, Calendar.MONDAY)
    assertEquals(3, Calendar.TUESDAY)
    assertEquals(4, Calendar.WEDNESDAY)
    assertEquals(5, Calendar.THURSDAY)
    assertEquals(6, Calendar.FRIDAY)
    assertEquals(7, Calendar.SATURDAY)
  }
}
