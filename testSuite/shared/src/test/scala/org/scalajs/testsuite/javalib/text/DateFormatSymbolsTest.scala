package org.scalajs.testsuite.javalib.text

import java.text.DateFormatSymbols
import java.util.Locale

import org.junit.Assert._
import org.junit.{Before, Test}
import org.scalajs.testsuite.utils.AssertThrows._
import org.scalajs.testsuite.utils.LocaleTestSetup
import org.scalajs.testsuite.utils.Platform

import scala.scalajs.locale.cldr.LDML
import scala.scalajs.locale.cldr.data._

class DateFormatSymbolsTest extends LocaleTestSetup {
  // Clean up the locale database, there are different implementations for
  // the JVM and JS
  @Before def cleanup: Unit = super.cleanDatabase

  case class LocaleTestItem(ldml: LDML, tag: String, cldr21: Boolean, months: List[String], shortMonths: List[String])

  // Note that in the JVM the months array is always 13 long :S
  // http://bugs.java.com/bugdatabase/view_bug.do?bug_id=4146173
  val standardLocalesData = List(
    Locale.ENGLISH -> LocaleTestItem(en, "en", cldr21 = false,
      List("January", "February", "March", "April", "May", "June", "July", "August",
        "September", "October", "November", "December", ""),
      List("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", "")),
    Locale.FRENCH -> LocaleTestItem(fr, "fr", cldr21 = false,
      List("janvier", "février", "mars", "avril", "mai", "juin", "juillet", "août", "septembre",
        "octobre", "novembre", "décembre", ""),
      List("janv.", "févr.", "mars", "avr.", "mai", "juin", "juil.", "août", "sept.", "oct.", "nov.", "déc.", "")),
    Locale.ITALIAN -> LocaleTestItem(it, "it", cldr21 = false,
      List("gennaio", "febbraio", "marzo", "aprile", "maggio", "giugno", "luglio", "agosto", "settembre", "ottobre", "novembre", "dicembre", ""),
      List("gen", "feb", "mar", "apr", "mag", "giu", "lug", "ago", "set", "ott", "nov", "dic", ""))
  )

  val standardLocalesDataDiff = List(
    Locale.ROOT -> LocaleTestItem(root, "", cldr21 = true,
      List("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", ""),
      List("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", "")),
    Locale.ROOT -> LocaleTestItem(root, "", cldr21 = false,
      List("M01", "M02", "M03", "M04", "M05", "M06", "M07", "M08", "M09", "M10", "M11", "M12", ""),
      List("", "", "", "", "", "", "", "", "", "", "", "", "")),
    Locale.GERMAN -> LocaleTestItem(de, "de", cldr21 = true,
      List("Januar", "Februar", "März", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Dezember", ""),
      List("Jan", "Feb", "Mär", "Apr", "Mai", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dez", "")),
    Locale.GERMAN -> LocaleTestItem(de, "de", cldr21 = false,
      List("Januar", "Februar", "März", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Dezember", ""),
      List("Jan.", "Feb.", "März", "Apr.", "Mai", "Juni", "Juli", "Aug.", "Sep.", "Okt.", "Nov.", "Dez.", "")),
    Locale.KOREAN -> LocaleTestItem(ko, "ko", cldr21 = true,
      List("1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월", ""),
      List("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", "")),
    Locale.KOREAN -> LocaleTestItem(ko, "ko", cldr21 = false,
      List("1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월", ""),
      List("1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월", "")),
    Locale.CHINESE -> LocaleTestItem(it, "it", cldr21 = true,
      List("1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月", ""),
      List("1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月", "")),
    Locale.CHINESE -> LocaleTestItem(it, "it", cldr21 = false,
      List("一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月", ""),
      List("1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月", ""))
  )

  def test_dfs(s: DateFormatSymbols, months: List[String], shortMonths: List[String]): Unit = {
    println(months.mkString("-"))
    assertArrayEquals(Array[AnyRef](months: _*), Array[AnyRef](s.getMonths(): _*))
    println(shortMonths.mkString("-"))
    assertArrayEquals(Array[AnyRef](shortMonths: _*), Array[AnyRef](s.getShortMonths(): _*))
  }

  @Test def test_default_locales_date_format_symbol(): Unit = {
    standardLocalesData.foreach {
      case (l, LocaleTestItem(ldml, t, _, months, shortMonths)) =>
        val dfs = DateFormatSymbols.getInstance(l)
        test_dfs(dfs, months, shortMonths)
    }
  }

  @Test def test_default_locales_date_format_symbol_with_cldr21(): Unit = {
    standardLocalesDataDiff.foreach {
      case (l, LocaleTestItem(ldml, t, cldr21, months, shortMonths)) =>
        val dfs = DateFormatSymbols.getInstance(l)
        if (Platform.executingInJVM && cldr21) {
          test_dfs(dfs, months, shortMonths)
        }
        if (!Platform.executingInJVM && !cldr21) {
          test_dfs(dfs, months, shortMonths)
        }
    }
  }

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
