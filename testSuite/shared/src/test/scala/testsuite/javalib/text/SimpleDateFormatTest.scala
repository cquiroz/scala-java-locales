package testsuite.javalib.text

import java.util.Date
import java.text.SimpleDateFormat

import org.junit.Assert._
import org.junit.Test

class SimpleDateFormatTest {
  @Test def test_year_format(): Unit = {
    val f = new SimpleDateFormat("yyyy")

    val d = new Date(1491381282242L)
    assertEquals("2017", f.format(d))
  }

  @Test def test_month_format(): Unit = {
    val f = new SimpleDateFormat("MM")

    val d = new Date(1491381282242L)
    assertEquals("04", f.format(d))
  }

  @Test def test_day_format(): Unit = {
    val f = new SimpleDateFormat("dd")

    val d = new Date(1491381282242L)
    assertEquals("05", f.format(d))
  }

  @Test def test_hour_format(): Unit = {
    val f = new SimpleDateFormat("HH")

    val d = new Date(1491381282242L)
    assertEquals("08", f.format(d))
  }

  @Test def test_minutes_format(): Unit = {
    val f = new SimpleDateFormat("mm")

    val d = new Date(1491381282242L)
    assertEquals("34", f.format(d))
  }

  @Test def test_seconds_format(): Unit = {
    val f = new SimpleDateFormat("ss")

    val d = new Date(1491381282242L)
    assertEquals("42", f.format(d))
  }

  @Test def test_millis_format(): Unit = {
    val f = new SimpleDateFormat("SSS")

    val d = new Date(1491381282242L)
    assertEquals("242", f.format(d))
  }

  @Test def test_full_date_format(): Unit = {
    val f = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:SSS")

    val d = new Date(1491381282242L)
    assertEquals("05/04/2017 08:34:42:242", f.format(d))
  }
}
