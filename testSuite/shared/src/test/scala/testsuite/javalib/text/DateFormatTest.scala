package testsuite.javalib.text

import java.text.{DateFormat, SimpleDateFormat}

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

  @Test def test_default_date_format(): Unit = {
    assertEquals("EEEE, MMMM d, y", DateFormat.getDateInstance(DateFormat.FULL).asInstanceOf[SimpleDateFormat].toPattern())
    assertEquals("MMMM d, y", DateFormat.getDateInstance(DateFormat.LONG).asInstanceOf[SimpleDateFormat].toPattern())
    assertEquals("MMM d, y", DateFormat.getDateInstance(DateFormat.MEDIUM).asInstanceOf[SimpleDateFormat].toPattern())
    assertEquals("M/d/yy", DateFormat.getDateInstance(DateFormat.SHORT).asInstanceOf[SimpleDateFormat].toPattern())

    assertEquals("h:mm:ss a zzzz", DateFormat.getTimeInstance(DateFormat.FULL).asInstanceOf[SimpleDateFormat].toPattern())
    assertEquals("h:mm:ss a z", DateFormat.getTimeInstance(DateFormat.LONG).asInstanceOf[SimpleDateFormat].toPattern())
    assertEquals("h:mm:ss a", DateFormat.getTimeInstance(DateFormat.MEDIUM).asInstanceOf[SimpleDateFormat].toPattern())
    assertEquals("h:mm a", DateFormat.getTimeInstance(DateFormat.SHORT).asInstanceOf[SimpleDateFormat].toPattern())

    assertEquals("EEEE, MMMM d, y h:mm:ss a zzzz", DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL).asInstanceOf[SimpleDateFormat].toPattern())
    assertEquals("EEEE, MMMM d, y h:mm:ss a z", DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.LONG).asInstanceOf[SimpleDateFormat].toPattern())
    assertEquals("EEEE, MMMM d, y h:mm:ss a", DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.MEDIUM).asInstanceOf[SimpleDateFormat].toPattern())
    assertEquals("EEEE, MMMM d, y h:mm a", DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.SHORT).asInstanceOf[SimpleDateFormat].toPattern())
    assertEquals("MMMM d, y h:mm:ss a zzzz", DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.FULL).asInstanceOf[SimpleDateFormat].toPattern())
    assertEquals("MMMM d, y h:mm:ss a z", DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).asInstanceOf[SimpleDateFormat].toPattern())
    assertEquals("MMMM d, y h:mm:ss a", DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.MEDIUM).asInstanceOf[SimpleDateFormat].toPattern())
    assertEquals("MMMM d, y h:mm a", DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT).asInstanceOf[SimpleDateFormat].toPattern())
    assertEquals("MMM d, y h:mm:ss a zzzz", DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.FULL).asInstanceOf[SimpleDateFormat].toPattern())
    assertEquals("MMM d, y h:mm:ss a z", DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.LONG).asInstanceOf[SimpleDateFormat].toPattern())
    assertEquals("MMM d, y h:mm:ss a", DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM).asInstanceOf[SimpleDateFormat].toPattern())
    assertEquals("MMM d, y h:mm a", DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).asInstanceOf[SimpleDateFormat].toPattern())
    assertEquals("M/d/yy h:mm:ss a zzzz", DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.FULL).asInstanceOf[SimpleDateFormat].toPattern())
    assertEquals("M/d/yy h:mm:ss a z", DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG).asInstanceOf[SimpleDateFormat].toPattern())
    assertEquals("M/d/yy h:mm:ss a", DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM).asInstanceOf[SimpleDateFormat].toPattern())
    assertEquals("M/d/yy h:mm a", DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).asInstanceOf[SimpleDateFormat].toPattern())
  }
}
