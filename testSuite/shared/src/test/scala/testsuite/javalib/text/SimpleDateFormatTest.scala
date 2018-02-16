package testsuite.javalib.text

import java.util.Date
import java.text.SimpleDateFormat

import org.junit.Assert._
import org.junit.Test

import io.github.cquiroz.utils.JVMDate
import scala.language.reflectiveCalls

class SimpleDateFormatTest {
  final val time = 1491381282242L
  val date = new Date(time)
  val jvmDate = JVMDate.get(time)
  val year = totalSize((1900 + jvmDate.year), 4)
  val month = totalSize(1 + jvmDate.month, 2)
  val day = totalSize(jvmDate.date, 2)
  val hours = totalSize(jvmDate.hours, 2)
  val minutes = totalSize(jvmDate.minutes, 2)
  val seconds = totalSize(jvmDate.seconds, 2)
  val millis = totalSize(date.getTime - Date.UTC(date.getYear,
     date.getMonth,
     date.getDay,
     date.getHours,
     date.getMinutes,
     date.getSeconds), 3)

  def totalSize(num: Long, size: Int): String = {
    val s: String = num.toString

    if (s.size > size) s.substring(s.size - size)
    else s.reverse.padTo(size, '0').reverse
  }

  @Test def test_year_format(): Unit = {
    val f = new SimpleDateFormat("yyyy")
    assertEquals(year, f.format(date))
  }

  @Test def test_month_format(): Unit = {
    val f = new SimpleDateFormat("MM")
    assertEquals(month, f.format(date))
  }

  @Test def test_day_format(): Unit = {
    val f = new SimpleDateFormat("dd")
    assertEquals(day, f.format(date))
  }

  @Test def test_hour_format(): Unit = {
    val f = new SimpleDateFormat("HH")
    assertEquals(hours, f.format(date))
  }

  @Test def test_minutes_format(): Unit = {
    val f = new SimpleDateFormat("mm")
    assertEquals(minutes, f.format(date))
  }

  @Test def test_seconds_format(): Unit = {
    val f = new SimpleDateFormat("ss")
    assertEquals(seconds, f.format(date))
  }

  @Test def test_millis_format(): Unit = {
    val f = new SimpleDateFormat("SSS")
    assertEquals(millis, f.format(date))
  }

  @Test def test_full_date_format(): Unit = {
    val f = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:SSS")
    assertEquals(s"$day/$month/$year $hours:$minutes:$seconds:$millis",
      f.format(date))
  }
}
