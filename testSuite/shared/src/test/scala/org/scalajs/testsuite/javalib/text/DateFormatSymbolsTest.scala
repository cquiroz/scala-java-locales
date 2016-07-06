package org.scalajs.testsuite.javalib.text

import java.text.DateFormatSymbols
import java.util.{Calendar, Locale}

import org.junit.{Before, Ignore, Test}
import org.junit.Assert._
import org.scalajs.testsuite.utils.AssertThrows._
import org.scalajs.testsuite.utils.LocaleTestSetup
import org.scalajs.testsuite.utils.Platform

import scala.scalajs.locale.cldr.LDML
import scala.scalajs.locale.cldr.data._

class DateFormatSymbolsTest extends LocaleTestSetup {
  // Clean up the locale database, there are different implementations for
  // the JVM and JS
  @Before def cleanup: Unit = super.cleanDatabase

  case class LocaleTestItem(ldml: LDML, tag: String, cldr21: Boolean,
      months: List[String], shortMonths: List[String], weekdays: List[String],
      shortWeekdays: List[String], amPm: List[String], eras: List[String])

  // Note that in the JVM the months array is always 13 long :S
  // http://bugs.java.com/bugdatabase/view_bug.do?bug_id=4146173
  val standardLocalesData = List(
    Locale.ENGLISH -> LocaleTestItem(en, "en", cldr21 = false,
      List("January", "February", "March", "April", "May", "June", "July", "August",
        "September", "October", "November", "December", ""),
      List("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", ""),
      List("", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"),
      List("", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"),
      List("AM", "PM"),
      List("BC", "AD")),
    Locale.FRENCH -> LocaleTestItem(fr, "fr", cldr21 = false,
      List("janvier", "février", "mars", "avril", "mai", "juin", "juillet", "août", "septembre",
        "octobre", "novembre", "décembre", ""),
      List("janv.", "févr.", "mars", "avr.", "mai", "juin", "juil.", "août", "sept.", "oct.", "nov.", "déc.", ""),
      List("", "dimanche", "lundi", "mardi", "mercredi", "jeudi", "vendredi", "samedi"),
      List("", "dim.", "lun.", "mar.", "mer.", "jeu.", "ven.", "sam."),
      List("AM", "PM"),
      List("av. J.-C.", "ap. J.-C.")),
    Locale.FRANCE -> LocaleTestItem(fr_FR, "fr_FR", cldr21 = false,
      List("janvier", "février", "mars", "avril", "mai", "juin", "juillet", "août", "septembre",
        "octobre", "novembre", "décembre", ""),
      List("janv.", "févr.", "mars", "avr.", "mai", "juin", "juil.", "août", "sept.", "oct.", "nov.", "déc.", ""),
      List("", "dimanche", "lundi", "mardi", "mercredi", "jeudi", "vendredi", "samedi"),
      List("", "dim.", "lun.", "mar.", "mer.", "jeu.", "ven.", "sam."),
      List("AM", "PM"),
      List("av. J.-C.", "ap. J.-C.")),
    Locale.TAIWAN -> LocaleTestItem(zh_Hant_TW, "zh_Hant_TW", cldr21 = true,
      List("1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月", ""),
      List("1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月", ""),
      List("", "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"),
      List("", "週日", "週一", "週二", "週三", "週四", "週五", "週六"),
      List("上午", "下午"),
      List("西元前", "西元")),
    Locale.US -> LocaleTestItem(en_US, "en_US", cldr21 = false,
      List("January", "February", "March", "April", "May", "June", "July", "August",
        "September", "October", "November", "December", ""),
      List("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", ""),
      List("", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"),
      List("", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"),
      List("AM", "PM"),
      List("BC", "AD")),
    Locale.CANADA -> LocaleTestItem(en_CA, "en_CA", cldr21 = false,
      List("January", "February", "March", "April", "May", "June", "July", "August",
        "September", "October", "November", "December", ""),
      List("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", ""),
      List("", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"),
      List("", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"),
      List("AM", "PM"),
      List("BC", "AD")),
    Locale.CANADA_FRENCH -> LocaleTestItem(fr_CA, "fr_CA", cldr21 = false,
      List("janvier", "février", "mars", "avril", "mai", "juin", "juillet", "août", "septembre",
        "octobre", "novembre", "décembre", ""),
      List("janv.", "févr.", "mars", "avr.", "mai", "juin", "juil.", "août", "sept.", "oct.", "nov.", "déc.", ""),
      List("", "dimanche", "lundi", "mardi", "mercredi", "jeudi", "vendredi", "samedi"),
      List("", "dim.", "lun.", "mar.", "mer.", "jeu.", "ven.", "sam."),
      List("AM", "PM"),
      List("av. J.-C.", "ap. J.-C."))
  )

  val standardLocalesDataDiff = List(
    Locale.ROOT -> LocaleTestItem(root, "", cldr21 = true,
      List("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", ""),
      List("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", ""),
      List("", "1", "2", "3", "4", "5", "6", "7"),
      List("", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"),
      List("AM", "PM"),
      List("BCE", "CE")),
    Locale.ROOT -> LocaleTestItem(root, "", cldr21 = false,
      List("M01", "M02", "M03", "M04", "M05", "M06", "M07", "M08", "M09", "M10", "M11", "M12", ""),
      List("", "", "", "", "", "", "", "", "", "", "", "", ""),
      List("", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"),
      List("", "", "", "", "", "", "", ""),
      List("", ""),
      List("BCE", "CE")),
    Locale.GERMAN -> LocaleTestItem(de, "de", cldr21 = true,
      List("Januar", "Februar", "März", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Dezember", ""),
      List("Jan", "Feb", "Mär", "Apr", "Mai", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dez", ""),
      List("", "Sonntag", "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag"),
      List("", "So.", "Mo.", "Di.", "Mi.", "Do.", "Fr.", "Sa."),
      List("vorm.", "nachm."),
      List("v. Chr.", "n. Chr.")),
    Locale.GERMAN -> LocaleTestItem(de, "de", cldr21 = false,
      List("Januar", "Februar", "März", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Dezember", ""),
      List("Jan.", "Feb.", "März", "Apr.", "Mai", "Juni", "Juli", "Aug.", "Sep.", "Okt.", "Nov.", "Dez.", ""),
      List("", "Sonntag", "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag"),
      List("", "So.", "Mo.", "Di.", "Mi.", "Do.", "Fr.", "Sa."),
      List("vorm.", "nachm."),
      List("v. Chr.", "n. Chr.")),
    Locale.GERMANY -> LocaleTestItem(de_DE, "de_DE", cldr21 = true,
      List("Januar", "Februar", "März", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Dezember", ""),
      List("Jan", "Feb", "Mär", "Apr", "Mai", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dez", ""),
      List("", "Sonntag", "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag"),
      List("", "So.", "Mo.", "Di.", "Mi.", "Do.", "Fr.", "Sa."),
      List("vorm.", "nachm."),
      List("v. Chr.", "n. Chr.")),
    Locale.GERMANY -> LocaleTestItem(de_DE, "de_DE", cldr21 = false,
      List("Januar", "Februar", "März", "April", "Mai", "Juni", "Juli", "August", "September", "Oktober", "November", "Dezember", ""),
      List("Jan.", "Feb.", "März", "Apr.", "Mai", "Juni", "Juli", "Aug.", "Sep.", "Okt.", "Nov.", "Dez.", ""),
      List("", "Sonntag", "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag"),
      List("", "So.", "Mo.", "Di.", "Mi.", "Do.", "Fr.", "Sa."),
      List("vorm.", "nachm."),
      List("v. Chr.", "n. Chr.")),
    Locale.ITALIAN -> LocaleTestItem(it, "it", cldr21 = true,
      List("gennaio", "febbraio", "marzo", "aprile", "maggio", "giugno", "luglio", "agosto", "settembre", "ottobre", "novembre", "dicembre", ""),
      List("gen", "feb", "mar", "apr", "mag", "giu", "lug", "ago", "set", "ott", "nov", "dic", ""),
      List("", "domenica", "lunedì", "martedì", "mercoledì", "giovedì", "venerdì", "sabato"),
      List("", "dom", "lun", "mar", "mer", "gio", "ven", "sab"),
      List("m.", "p."),
      List("aC", "dC")),
    Locale.ITALIAN -> LocaleTestItem(it, "it", cldr21 = false,
      List("gennaio", "febbraio", "marzo", "aprile", "maggio", "giugno", "luglio", "agosto", "settembre", "ottobre", "novembre", "dicembre", ""),
      List("gen", "feb", "mar", "apr", "mag", "giu", "lug", "ago", "set", "ott", "nov", "dic", ""),
      List("", "domenica", "lunedì", "martedì", "mercoledì", "giovedì", "venerdì", "sabato"),
      List("", "dom", "lun", "mar", "mer", "gio", "ven", "sab"),
      List("AM", "PM"),
      List("a.C.", "d.C.")),
    Locale.ITALY -> LocaleTestItem(it_IT, "it_IT", cldr21 = true,
      List("gennaio", "febbraio", "marzo", "aprile", "maggio", "giugno", "luglio", "agosto", "settembre", "ottobre", "novembre", "dicembre", ""),
      List("gen", "feb", "mar", "apr", "mag", "giu", "lug", "ago", "set", "ott", "nov", "dic", ""),
      List("", "domenica", "lunedì", "martedì", "mercoledì", "giovedì", "venerdì", "sabato"),
      List("", "dom", "lun", "mar", "mer", "gio", "ven", "sab"),
      List("m.", "p."),
      List("aC", "dC")),
    Locale.ITALY -> LocaleTestItem(it_IT, "it_IT", cldr21 = false,
      List("gennaio", "febbraio", "marzo", "aprile", "maggio", "giugno", "luglio", "agosto", "settembre", "ottobre", "novembre", "dicembre", ""),
      List("gen", "feb", "mar", "apr", "mag", "giu", "lug", "ago", "set", "ott", "nov", "dic", ""),
      List("", "domenica", "lunedì", "martedì", "mercoledì", "giovedì", "venerdì", "sabato"),
      List("", "dom", "lun", "mar", "mer", "gio", "ven", "sab"),
      List("AM", "PM"),
      List("a.C.", "d.C.")),
    Locale.KOREA -> LocaleTestItem(ko_KR, "ko_KR", cldr21 = true,
      List("1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월", ""),
      List("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", ""),
      List("", "일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"),
      List("", "일", "월", "화", "수", "목", "금", "토"),
      List("오전", "오후"),
      List("기원전", "서기")),
    Locale.KOREA -> LocaleTestItem(ko_KR, "ko_KR", cldr21 = false,
      List("1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월", ""),
      List("1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월", ""),
      List("", "일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"),
      List("", "일", "월", "화", "수", "목", "금", "토"),
      List("오전", "오후"),
      List("BC", "AD")),
    Locale.KOREAN -> LocaleTestItem(ko, "ko", cldr21 = true,
      List("1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월", ""),
      List("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", ""),
      List("", "일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"),
      List("", "일", "월", "화", "수", "목", "금", "토"),
      List("오전", "오후"),
      List("기원전", "서기")),
    Locale.KOREAN -> LocaleTestItem(ko, "ko", cldr21 = false,
      List("1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월", ""),
      List("1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월", ""),
      List("", "일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"),
      List("", "일", "월", "화", "수", "목", "금", "토"),
      List("오전", "오후"),
      List("BC", "AD")),
    Locale.CHINESE -> LocaleTestItem(zh, "zh", cldr21 = true,
      List("1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月", ""),
      List("1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月", ""),
      List("", "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"),
      List("", "周日", "周一", "周二", "周三", "周四", "周五", "周六"),
      List("上午", "下午"),
      List("公元前", "公元")),
    Locale.CHINESE -> LocaleTestItem(zh, "zh", cldr21 = false,
      List("一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月", ""),
      List("1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月", ""),
      List("", "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"),
      List("", "周日", "周一", "周二", "周三", "周四", "周五", "周六"),
      List("上午", "下午"),
      List("公元前", "公元")),
    Locale.CHINA -> LocaleTestItem(zh_Hans_CN, "zh_Hans_CN", cldr21 = true,
      List("1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月", ""),
      List("1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月", ""),
      List("", "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"),
      List("", "周日", "周一", "周二", "周三", "周四", "周五", "周六"),
      List("上午", "下午"),
      List("公元前", "公元")),
    Locale.CHINA -> LocaleTestItem(zh_Hans_CN, "zh_Hans_CN", cldr21 = false,
      List("一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月", ""),
      List("1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月", ""),
      List("", "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"),
      List("", "周日", "周一", "周二", "周三", "周四", "周五", "周六"),
      List("上午", "下午"),
      List("公元前", "公元")),
    Locale.PRC -> LocaleTestItem(zh_Hans_CN, "zh_Hans_CN", cldr21 = true,
      List("1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月", ""),
      List("1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月", ""),
      List("", "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"),
      List("", "周日", "周一", "周二", "周三", "周四", "周五", "周六"),
      List("上午", "下午"),
      List("公元前", "公元")),
    Locale.PRC -> LocaleTestItem(zh_Hans_CN, "zh_Hans_CN", cldr21 = false,
      List("一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月", ""),
      List("1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月", ""),
      List("", "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"),
      List("", "周日", "周一", "周二", "周三", "周四", "周五", "周六"),
      List("上午", "下午"),
      List("公元前", "公元")),
    Locale.UK -> LocaleTestItem(en_GB, "en_GB", cldr21 = true,
      List("January", "February", "March", "April", "May", "June", "July", "August",
        "September", "October", "November", "December", ""),
      List("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", ""),
      List("", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"),
      List("", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"),
      List("AM", "PM"),
      List("BC", "AD")),
    Locale.UK -> LocaleTestItem(en_GB, "en_GB", cldr21 = false,
      List("January", "February", "March", "April", "May", "June", "July", "August",
        "September", "October", "November", "December", ""),
      List("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", ""),
      List("", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"),
      List("", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"),
      List("am", "pm"),
      List("BC", "AD"))
  )

  def test_dfs(s: DateFormatSymbols, t: LocaleTestItem): Unit = {
    assertArrayEquals(Array[AnyRef](t.months: _*), Array[AnyRef](s.getMonths(): _*))
    assertArrayEquals(Array[AnyRef](t.shortMonths: _*), Array[AnyRef](s.getShortMonths(): _*))
    for {
      d <- Calendar.SUNDAY to Calendar.SATURDAY
    } {
      assertEquals(t.weekdays(d), s.getWeekdays()(d))
      assertEquals(t.shortWeekdays(d), s.getShortWeekdays()(d))
    }
    assertArrayEquals(Array[AnyRef](t.amPm: _*), Array[AnyRef](s.getAmPmStrings(): _*))
    assertArrayEquals(Array[AnyRef](t.eras: _*), Array[AnyRef](s.getEras(): _*))
  }

  @Test def test_default_locales_date_format_symbol(): Unit = {
    standardLocalesData.foreach {
      case (l, t @ LocaleTestItem(_, _, _, _, _, _, _, _, _)) =>
        val dfs = DateFormatSymbols.getInstance(l)
        test_dfs(dfs, t)
    }
  }

  @Test def test_default_locales_date_format_symbol_with_cldr21(): Unit = {
    standardLocalesDataDiff.foreach {
      case (l, t @ LocaleTestItem(_, m, cldr21, _, _, _, _, _, _)) =>
        val dfs = DateFormatSymbols.getInstance(l)
        if (Platform.executingInJVM && cldr21) {
          test_dfs(dfs, t)
        }
        if (!Platform.executingInJVM && !cldr21) {
          test_dfs(dfs, t)
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
