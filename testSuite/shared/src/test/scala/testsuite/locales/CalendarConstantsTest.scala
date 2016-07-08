package testsuite.locales

import locales.CalendarConstants
import org.junit.Assert._
import org.junit.Test

class CalendarConstantsTest {
  @Test def test_day_fields(): Unit = {
    assertEquals(1, CalendarConstants.SUNDAY)
    assertEquals(2, CalendarConstants.MONDAY)
    assertEquals(3, CalendarConstants.TUESDAY)
    assertEquals(4, CalendarConstants.WEDNESDAY)
    assertEquals(5, CalendarConstants.THURSDAY)
    assertEquals(6, CalendarConstants.FRIDAY)
    assertEquals(7, CalendarConstants.SATURDAY)
  }
}
