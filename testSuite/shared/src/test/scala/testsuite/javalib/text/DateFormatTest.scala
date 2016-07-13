package testsuite.javalib.text

import java.text.DateFormat

import org.junit.Assert._
import org.junit.Test

class DateFormatTest {
  @Test def test_constants(): Unit = {
    assertEquals(0, DateFormat.ERA_FIELD)
    assertEquals(1, DateFormat.YEAR_FIELD)
    assertEquals(2, DateFormat.MONTH_FIELD)
    assertEquals(3, DateFormat.DATE_FIELD)
    assertEquals(4, DateFormat.HOUR_OF_DAY1_FIELD)
    assertEquals(5, DateFormat.HOUR_OF_DAY0_FIELD)
    assertEquals(6, DateFormat.MINUTE_FIELD)
    assertEquals(7, DateFormat.SECOND_FIELD)
    assertEquals(8, DateFormat.MILLISECOND_FIELD)
    assertEquals(9, DateFormat.DAY_OF_WEEK_FIELD)
    assertEquals(10, DateFormat.DAY_OF_YEAR_FIELD)
    assertEquals(11, DateFormat.DAY_OF_WEEK_IN_MONTH_FIELD)
    assertEquals(12, DateFormat.WEEK_OF_YEAR_FIELD)
    assertEquals(13, DateFormat.WEEK_OF_MONTH_FIELD)
    assertEquals(14, DateFormat.AM_PM_FIELD)
    assertEquals(15, DateFormat.HOUR1_FIELD)
    assertEquals(16, DateFormat.HOUR0_FIELD)
    assertEquals(17, DateFormat.TIMEZONE_FIELD)

    assertEquals(0, DateFormat.FULL)
    assertEquals(1, DateFormat.LONG)
    assertEquals(2, DateFormat.MEDIUM)
    assertEquals(3, DateFormat.SHORT)
    assertEquals(2, DateFormat.DEFAULT)
  }
}
