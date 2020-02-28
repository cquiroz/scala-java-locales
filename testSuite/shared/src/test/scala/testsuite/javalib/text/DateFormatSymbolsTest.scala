package testsuite.javalib.text

import java.text.DateFormatSymbols
import java.util.Locale
import locales.CalendarConstants
import testsuite.utils.Platform
import testsuite.utils.AssertThrows.expectThrows

class DateFormatSymbolsTest extends munit.FunSuite {
  Locale.setDefault(Locale.US)

  case class LocaleTestItem(
    tag:           String,
    cldr21:        Boolean,
    months:        List[String],
    shortMonths:   List[String],
    weekdays:      List[String],
    shortWeekdays: List[String],
    amPm:          List[String],
    eras:          List[String]
  )

  val root = Locale.ROOT

  // Note that in the JVM the months array is always 13 long :S
  // http://bugs.java.com/bugdatabase/view_bug.do?bug_id=4146173
  val standardLocalesData = List(
    Locale.ENGLISH -> LocaleTestItem(
      "en",
      cldr21 = false,
      List(
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December",
        ""
      ),
      List("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", ""),
      List("", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"),
      List("", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"),
      List("AM", "PM"),
      List("BC", "AD")
    ),
    Locale.FRENCH -> LocaleTestItem(
      "fr",
      cldr21 = false,
      List(
        "janvier",
        "février",
        "mars",
        "avril",
        "mai",
        "juin",
        "juillet",
        "août",
        "septembre",
        "octobre",
        "novembre",
        "décembre",
        ""
      ),
      List(
        "janv.",
        "févr.",
        "mars",
        "avr.",
        "mai",
        "juin",
        "juil.",
        "août",
        "sept.",
        "oct.",
        "nov.",
        "déc.",
        ""
      ),
      List("", "dimanche", "lundi", "mardi", "mercredi", "jeudi", "vendredi", "samedi"),
      List("", "dim.", "lun.", "mar.", "mer.", "jeu.", "ven.", "sam."),
      List("AM", "PM"),
      List("av. J.-C.", "ap. J.-C.")
    ),
    Locale.FRANCE -> LocaleTestItem(
      "fr_FR",
      cldr21 = false,
      List(
        "janvier",
        "février",
        "mars",
        "avril",
        "mai",
        "juin",
        "juillet",
        "août",
        "septembre",
        "octobre",
        "novembre",
        "décembre",
        ""
      ),
      List(
        "janv.",
        "févr.",
        "mars",
        "avr.",
        "mai",
        "juin",
        "juil.",
        "août",
        "sept.",
        "oct.",
        "nov.",
        "déc.",
        ""
      ),
      List("", "dimanche", "lundi", "mardi", "mercredi", "jeudi", "vendredi", "samedi"),
      List("", "dim.", "lun.", "mar.", "mer.", "jeu.", "ven.", "sam."),
      List("AM", "PM"),
      List("av. J.-C.", "ap. J.-C.")
    ),
    Locale.TAIWAN -> LocaleTestItem(
      "zh_Hant_TW",
      cldr21 = true,
      List("1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月", ""),
      List("1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月", ""),
      List("", "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"),
      List("", "週日", "週一", "週二", "週三", "週四", "週五", "週六"),
      List("上午", "下午"),
      List("西元前", "西元")
    ),
    Locale.US -> LocaleTestItem(
      "en_US",
      cldr21 = false,
      List(
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December",
        ""
      ),
      List("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", ""),
      List("", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"),
      List("", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"),
      List("AM", "PM"),
      List("BC", "AD")
    )
  )

  val standardLocalesDataDiff = List(
    Locale.ROOT -> LocaleTestItem(
      "",
      cldr21 = true,
      List("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", ""),
      List("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", ""),
      List("", "1", "2", "3", "4", "5", "6", "7"),
      List("", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"),
      List("AM", "PM"),
      List("BCE", "CE")
    ),
    Locale.ROOT -> LocaleTestItem(
      "",
      cldr21 = false,
      List("M01", "M02", "M03", "M04", "M05", "M06", "M07", "M08", "M09", "M10", "M11", "M12", ""),
      List("", "", "", "", "", "", "", "", "", "", "", "", ""),
      List("", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"),
      List("", "", "", "", "", "", "", ""),
      List("", ""),
      List("BCE", "CE")
    ),
    Locale.GERMAN -> LocaleTestItem(
      "de",
      cldr21 = true,
      List(
        "Januar",
        "Februar",
        "März",
        "April",
        "Mai",
        "Juni",
        "Juli",
        "August",
        "September",
        "Oktober",
        "November",
        "Dezember",
        ""
      ),
      List("Jan", "Feb", "Mär", "Apr", "Mai", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dez", ""),
      List("", "Sonntag", "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag"),
      List("", "So.", "Mo.", "Di.", "Mi.", "Do.", "Fr.", "Sa."),
      List("vorm.", "nachm."),
      List("v. Chr.", "n. Chr.")
    ),
    Locale.GERMAN -> LocaleTestItem(
      "de",
      cldr21 = false,
      List(
        "Januar",
        "Februar",
        "März",
        "April",
        "Mai",
        "Juni",
        "Juli",
        "August",
        "September",
        "Oktober",
        "November",
        "Dezember",
        ""
      ),
      List(
        "Jan.",
        "Feb.",
        "März",
        "Apr.",
        "Mai",
        "Juni",
        "Juli",
        "Aug.",
        "Sept.",
        "Okt.",
        "Nov.",
        "Dez.",
        ""
      ),
      List("", "Sonntag", "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag"),
      List("", "So.", "Mo.", "Di.", "Mi.", "Do.", "Fr.", "Sa."),
      List("", ""),
      List("v. Chr.", "n. Chr.")
    ),
    Locale.GERMANY -> LocaleTestItem(
      "de_DE",
      cldr21 = true,
      List(
        "Januar",
        "Februar",
        "März",
        "April",
        "Mai",
        "Juni",
        "Juli",
        "August",
        "September",
        "Oktober",
        "November",
        "Dezember",
        ""
      ),
      List("Jan", "Feb", "Mär", "Apr", "Mai", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dez", ""),
      List("", "Sonntag", "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag"),
      List("", "So.", "Mo.", "Di.", "Mi.", "Do.", "Fr.", "Sa."),
      List("vorm.", "nachm."),
      List("v. Chr.", "n. Chr.")
    ),
    Locale.GERMANY -> LocaleTestItem(
      "de_DE",
      cldr21 = false,
      List(
        "Januar",
        "Februar",
        "März",
        "April",
        "Mai",
        "Juni",
        "Juli",
        "August",
        "September",
        "Oktober",
        "November",
        "Dezember",
        ""
      ),
      List(
        "Jan.",
        "Feb.",
        "März",
        "Apr.",
        "Mai",
        "Juni",
        "Juli",
        "Aug.",
        "Sept.",
        "Okt.",
        "Nov.",
        "Dez.",
        ""
      ),
      List("", "Sonntag", "Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag"),
      List("", "So.", "Mo.", "Di.", "Mi.", "Do.", "Fr.", "Sa."),
      List("", ""),
      List("v. Chr.", "n. Chr.")
    ),
    Locale.ITALIAN -> LocaleTestItem(
      "it",
      cldr21 = true,
      List(
        "gennaio",
        "febbraio",
        "marzo",
        "aprile",
        "maggio",
        "giugno",
        "luglio",
        "agosto",
        "settembre",
        "ottobre",
        "novembre",
        "dicembre",
        ""
      ),
      List("gen", "feb", "mar", "apr", "mag", "giu", "lug", "ago", "set", "ott", "nov", "dic", ""),
      List("", "domenica", "lunedì", "martedì", "mercoledì", "giovedì", "venerdì", "sabato"),
      List("", "dom", "lun", "mar", "mer", "gio", "ven", "sab"),
      List("m.", "p."),
      List("aC", "dC")
    ),
    Locale.ITALIAN -> LocaleTestItem(
      "it",
      cldr21 = false,
      List(
        "gennaio",
        "febbraio",
        "marzo",
        "aprile",
        "maggio",
        "giugno",
        "luglio",
        "agosto",
        "settembre",
        "ottobre",
        "novembre",
        "dicembre",
        ""
      ),
      List("gen", "feb", "mar", "apr", "mag", "giu", "lug", "ago", "set", "ott", "nov", "dic", ""),
      List("", "domenica", "lunedì", "martedì", "mercoledì", "giovedì", "venerdì", "sabato"),
      List("", "dom", "lun", "mar", "mer", "gio", "ven", "sab"),
      List("AM", "PM"),
      List("a.C.", "d.C.")
    ),
    Locale.ITALY -> LocaleTestItem(
      "it_IT",
      cldr21 = true,
      List(
        "gennaio",
        "febbraio",
        "marzo",
        "aprile",
        "maggio",
        "giugno",
        "luglio",
        "agosto",
        "settembre",
        "ottobre",
        "novembre",
        "dicembre",
        ""
      ),
      List("gen", "feb", "mar", "apr", "mag", "giu", "lug", "ago", "set", "ott", "nov", "dic", ""),
      List("", "domenica", "lunedì", "martedì", "mercoledì", "giovedì", "venerdì", "sabato"),
      List("", "dom", "lun", "mar", "mer", "gio", "ven", "sab"),
      List("m.", "p."),
      List("aC", "dC")
    ),
    Locale.ITALY -> LocaleTestItem(
      "it_IT",
      cldr21 = false,
      List(
        "gennaio",
        "febbraio",
        "marzo",
        "aprile",
        "maggio",
        "giugno",
        "luglio",
        "agosto",
        "settembre",
        "ottobre",
        "novembre",
        "dicembre",
        ""
      ),
      List("gen", "feb", "mar", "apr", "mag", "giu", "lug", "ago", "set", "ott", "nov", "dic", ""),
      List("", "domenica", "lunedì", "martedì", "mercoledì", "giovedì", "venerdì", "sabato"),
      List("", "dom", "lun", "mar", "mer", "gio", "ven", "sab"),
      List("AM", "PM"),
      List("a.C.", "d.C.")
    ),
    Locale.JAPAN -> LocaleTestItem(
      "ja",
      cldr21 = true,
      List("1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月", ""),
      List("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", ""),
      List("", "日曜日", "月曜日", "火曜日", "水曜日", "木曜日", "金曜日", "土曜日"),
      List("", "日", "月", "火", "水", "木", "金", "土"),
      List("午前", "午後"),
      List("BC", "AD")
    ),
    Locale.JAPAN -> LocaleTestItem(
      "ja",
      cldr21 = false,
      List("1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月", ""),
      List("1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月", ""),
      List("", "日曜日", "月曜日", "火曜日", "水曜日", "木曜日", "金曜日", "土曜日"),
      List("", "日", "月", "火", "水", "木", "金", "土"),
      List("午前", "午後"),
      List("紀元前", "西暦")
    ),
    Locale.KOREA -> LocaleTestItem(
      "ko_KR",
      cldr21 = true,
      List("1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월", ""),
      List("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", ""),
      List("", "일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"),
      List("", "일", "월", "화", "수", "목", "금", "토"),
      List("오전", "오후"),
      List("기원전", "서기")
    ),
    Locale.KOREA -> LocaleTestItem(
      "ko_KR",
      cldr21 = false,
      List("1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월", ""),
      List("1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월", ""),
      List("", "일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"),
      List("", "일", "월", "화", "수", "목", "금", "토"),
      List("오전", "오후"),
      List("BC", "AD")
    ),
    Locale.KOREAN -> LocaleTestItem(
      "ko",
      cldr21 = true,
      List("1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월", ""),
      List("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", ""),
      List("", "일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"),
      List("", "일", "월", "화", "수", "목", "금", "토"),
      List("오전", "오후"),
      List("기원전", "서기")
    ),
    Locale.KOREAN -> LocaleTestItem(
      "ko",
      cldr21 = false,
      List("1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월", ""),
      List("1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월", ""),
      List("", "일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"),
      List("", "일", "월", "화", "수", "목", "금", "토"),
      List("오전", "오후"),
      List("BC", "AD")
    ),
    Locale.CHINESE -> LocaleTestItem(
      "zh",
      cldr21 = true,
      List("1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月", ""),
      List("1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月", ""),
      List("", "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"),
      List("", "周日", "周一", "周二", "周三", "周四", "周五", "周六"),
      List("上午", "下午"),
      List("公元前", "公元")
    ),
    Locale.CHINESE -> LocaleTestItem(
      "zh",
      cldr21 = false,
      List("一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月", ""),
      List("1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月", ""),
      List("", "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"),
      List("", "周日", "周一", "周二", "周三", "周四", "周五", "周六"),
      List("上午", "下午"),
      List("公元前", "公元")
    ),
    Locale.CHINA -> LocaleTestItem(
      "zh_Hans_CN",
      cldr21 = true,
      List("1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月", ""),
      List("1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月", ""),
      List("", "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"),
      List("", "周日", "周一", "周二", "周三", "周四", "周五", "周六"),
      List("上午", "下午"),
      List("公元前", "公元")
    ),
    Locale.CHINA -> LocaleTestItem(
      "zh_Hans_CN",
      cldr21 = false,
      List("一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月", ""),
      List("1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月", ""),
      List("", "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"),
      List("", "周日", "周一", "周二", "周三", "周四", "周五", "周六"),
      List("上午", "下午"),
      List("公元前", "公元")
    ),
    Locale.PRC -> LocaleTestItem(
      "zh_Hans_CN",
      cldr21 = true,
      List("1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月", ""),
      List("1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月", ""),
      List("", "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"),
      List("", "周日", "周一", "周二", "周三", "周四", "周五", "周六"),
      List("上午", "下午"),
      List("公元前", "公元")
    ),
    Locale.PRC -> LocaleTestItem(
      "zh_Hans_CN",
      cldr21 = false,
      List("一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月", ""),
      List("1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月", ""),
      List("", "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"),
      List("", "周日", "周一", "周二", "周三", "周四", "周五", "周六"),
      List("上午", "下午"),
      List("公元前", "公元")
    ),
    Locale.UK -> LocaleTestItem(
      "en_GB",
      cldr21 = true,
      List(
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December",
        ""
      ),
      List("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", ""),
      List("", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"),
      List("", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"),
      List("AM", "PM"),
      List("BC", "AD")
    ),
    Locale.UK -> LocaleTestItem(
      "en_GB",
      cldr21 = false,
      List(
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December",
        ""
      ),
      List("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", ""),
      List("", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"),
      List("", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"),
      List("am", "pm"),
      List("BC", "AD")
    ),
    Locale.CANADA_FRENCH -> LocaleTestItem(
      "fr_CA",
      cldr21 = true,
      List(
        "janvier",
        "février",
        "mars",
        "avril",
        "mai",
        "juin",
        "juillet",
        "août",
        "septembre",
        "octobre",
        "novembre",
        "décembre",
        ""
      ),
      List(
        "janv.",
        "févr.",
        "mars",
        "avr.",
        "mai",
        "juin",
        "juil.",
        "août",
        "sept.",
        "oct.",
        "nov.",
        "déc.",
        ""
      ),
      List("", "dimanche", "lundi", "mardi", "mercredi", "jeudi", "vendredi", "samedi"),
      List("", "dim.", "lun.", "mar.", "mer.", "jeu.", "ven.", "sam."),
      List("AM", "PM"),
      List("av. J.-C.", "ap. J.-C.")
    ),
    Locale.CANADA_FRENCH -> LocaleTestItem(
      "fr_CA",
      cldr21 = false,
      List(
        "janvier",
        "février",
        "mars",
        "avril",
        "mai",
        "juin",
        "juillet",
        "août",
        "septembre",
        "octobre",
        "novembre",
        "décembre",
        ""
      ),
      List(
        "janv.",
        "févr.",
        "mars",
        "avr.",
        "mai",
        "juin",
        "juill.",
        "août",
        "sept.",
        "oct.",
        "nov.",
        "déc.",
        ""
      ),
      List("", "dimanche", "lundi", "mardi", "mercredi", "jeudi", "vendredi", "samedi"),
      List("", "dim.", "lun.", "mar.", "mer.", "jeu.", "ven.", "sam."),
      List("a.m.", "p.m."),
      List("av. J.-C.", "ap. J.-C.")
    ),
    Locale.CANADA -> LocaleTestItem(
      "en_CA",
      cldr21 = true,
      List(
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December",
        ""
      ),
      List("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", ""),
      List("", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"),
      List("", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"),
      List("AM", "PM"),
      List("BC", "AD")
    ),
    Locale.CANADA -> LocaleTestItem(
      "en_CA",
      cldr21 = false,
      List(
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December",
        ""
      ),
      List(
        "Jan.",
        "Feb.",
        "Mar.",
        "Apr.",
        "Jun.",
        "Jul.",
        "Aug.",
        "Sep.",
        "Oct.",
        "Nov.",
        "Dec.",
        "",
        ""
      ),
      List("", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"),
      List("", "Sun.", "Mon.", "Tue.", "Wed.", "Thu.", "Fri.", "Sat."),
      List("a.m.", "p.m."),
      List("BC", "AD")
    )
  )

  val extraLocalesData = List(
    LocaleTestItem(
      "af",
      cldr21 = true,
      List(
        "Januarie",
        "Februarie",
        "Maart",
        "April",
        "Mei",
        "Junie",
        "Julie",
        "Augustus",
        "September",
        "Oktober",
        "November",
        "Desember",
        ""
      ),
      List("Jan", "Feb", "Mar", "Apr", "Mei", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Des", ""),
      List("", "Sondag", "Maandag", "Dinsdag", "Woensdag", "Donderdag", "Vrydag", "Saterdag"),
      List("", "So", "Ma", "Di", "Wo", "Do", "Vr", "Sa"),
      List("vm.", "nm."),
      List("v.C.", "n.C.")
    ),
    LocaleTestItem(
      "af",
      cldr21 = false,
      List(
        "Januarie",
        "Februarie",
        "Maart",
        "April",
        "Mei",
        "Junie",
        "Julie",
        "Augustus",
        "September",
        "Oktober",
        "November",
        "Desember",
        ""
      ),
      List(
        "Jan.",
        "Feb.",
        "Mrt.",
        "Apr.",
        "Mei",
        "Jun.",
        "Jul.",
        "Aug.",
        "Sep.",
        "Okt.",
        "Nov.",
        "Des.",
        ""
      ),
      List("", "Sondag", "Maandag", "Dinsdag", "Woensdag", "Donderdag", "Vrydag", "Saterdag"),
      List("", "So.", "Ma.", "Di.", "Wo.", "Do.", "Vr.", "Sa."),
      List("vm.", "nm."),
      List("v.C.", "n.C.")
    ),
    LocaleTestItem(
      "az",
      cldr21 = true,
      List(
        "Yanvar",
        "Fevral",
        "Mart",
        "Aprel",
        "May",
        "İyun",
        "İyul",
        "Avqust",
        "Sentyabr",
        "Oktyabr",
        "Noyabr",
        "Dekabr",
        ""
      ),
      List("yan", "fev", "mar", "apr", "may", "iyn", "iyl", "avq", "sen", "okt", "noy", "dek", ""),
      List(
        "",
        "bazar",
        "bazar ertəsi",
        "çərşənbə axşamı",
        "çərşənbə",
        "cümə axşamı",
        "cümə",
        "şənbə"
      ),
      List("", "B.", "B.E.", "Ç.A.", "Ç.", "C.A.", "C", "Ş."),
      List("AM", "PM"),
      List("e.ə.", "b.e.")
    ),
    LocaleTestItem(
      "az",
      cldr21 = false,
      List(
        "yanvar",
        "fevral",
        "mart",
        "aprel",
        "may",
        "iyun",
        "iyul",
        "avqust",
        "sentyabr",
        "oktyabr",
        "noyabr",
        "dekabr",
        ""
      ),
      List("yan", "fev", "mar", "apr", "may", "iyn", "iyl", "avq", "sen", "okt", "noy", "dek", ""),
      List(
        "",
        "bazar",
        "bazar ertəsi",
        "çərşənbə axşamı",
        "çərşənbə",
        "cümə axşamı",
        "cümə",
        "şənbə"
      ),
      List("", "B.", "B.e.", "Ç.a.", "Ç.", "C.a.", "C.", "Ş."),
      List("AM", "PM"),
      List("e.ə.", "y.e.")
    ),
    LocaleTestItem(
      "az-Cyrl",
      cldr21 = true,
      List(
        "јанвар",
        "феврал",
        "март",
        "апрел",
        "май",
        "ијун",
        "ијул",
        "август",
        "сентјабр",
        "октјабр",
        "нојабр",
        "декабр",
        ""
      ),
      List("yan", "fev", "mar", "apr", "may", "iyn", "iyl", "avq", "sen", "okt", "noy", "dek", ""),
      List(
        "",
        "базар",
        "базар ертәси",
        "чәршәнбә ахшамы",
        "чәршәнбә",
        "ҹүмә ахшамы",
        "ҹүмә",
        "шәнбә"
      ),
      List("", "B.", "B.E.", "Ç.A.", "Ç.", "C.A.", "C", "Ş."),
      List("AM", "PM"),
      List("e.ə.", "b.e.")
    ),
    LocaleTestItem(
      "az-Cyrl",
      cldr21 = false,
      List(
        "јанвар",
        "феврал",
        "март",
        "апрел",
        "май",
        "ијун",
        "ијул",
        "август",
        "сентјабр",
        "октјабр",
        "нојабр",
        "декабр",
        ""
      ),
      List("јан", "фев", "мар", "апр", "май", "ијн", "ијл", "авг", "сен", "окт", "ној", "дек", ""),
      List(
        "",
        "базар",
        "базар ертәси",
        "чәршәнбә ахшамы",
        "чәршәнбә",
        "ҹүмә ахшамы",
        "ҹүмә",
        "шәнбә"
      ),
      List("", "Б.", "Б.Е.", "Ч.А.", "Ч.", "Ҹ.А.", "Ҹ.", "Ш."),
      List("АМ", "ПМ"),
      List("е.ә.", "ј.е.")
    ),
    LocaleTestItem(
      "bn",
      cldr21 = true,
      List(
        "জানুয়ারী",
        "ফেব্রুয়ারী",
        "মার্চ",
        "এপ্রিল",
        "মে",
        "জুন",
        "জুলাই",
        "আগস্ট",
        "সেপ্টেম্বর",
        "অক্টোবর",
        "নভেম্বর",
        "ডিসেম্বর",
        ""
      ),
      List("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", ""),
      List("", "রবিবার", "সোমবার", "মঙ্গলবার", "বুধবার", "বৃহষ্পতিবার", "শুক্রবার", "শনিবার"),
      List("", "রবি", "সোম", "মঙ্গল", "বুধ", "বৃহস্পতি", "শুক্র", "শনি"),
      List("পূর্বাহ্ণ", "অপরাহ্ণ"),
      List("খৃষ্টপূর্ব", "খৃষ্টাব্দ")
    ),
    LocaleTestItem(
      "bn",
      cldr21 = false,
      List(
        "জানুয়ারী",
        "ফেব্রুয়ারী",
        "মার্চ",
        "এপ্রিল",
        "মে",
        "জুন",
        "জুলাই",
        "আগস্ট",
        "সেপ্টেম্বর",
        "অক্টোবর",
        "নভেম্বর",
        "ডিসেম্বর",
        ""
      ),
      List(
        "জানু",
        "ফেব",
        "মার্চ",
        "এপ্রিল",
        "মে",
        "জুন",
        "জুলাই",
        "আগস্ট",
        "সেপ্টেম্বর",
        "অক্টোবর",
        "নভেম্বর",
        "ডিসেম্বর",
        ""
      ),
      List("", "রবিবার", "সোমবার", "মঙ্গলবার", "বুধবার", "বৃহস্পতিবার", "শুক্রবার", "শনিবার"),
      List("", "রবি", "সোম", "মঙ্গল", "বুধ", "বৃহস্পতি", "শুক্র", "শনি"),
      List("AM", "PM"),
      List("খ্রিস্টপূর্ব", "খৃষ্টাব্দ")
    ),
    LocaleTestItem(
      "it-CH",
      cldr21 = true,
      List(
        "gennaio",
        "febbraio",
        "marzo",
        "aprile",
        "maggio",
        "giugno",
        "luglio",
        "agosto",
        "settembre",
        "ottobre",
        "novembre",
        "dicembre",
        ""
      ),
      List("gen", "feb", "mar", "apr", "mag", "giu", "lug", "ago", "set", "ott", "nov", "dic", ""),
      List("", "domenica", "lunedì", "martedì", "mercoledì", "giovedì", "venerdì", "sabato"),
      List("", "dom", "lun", "mar", "mer", "gio", "ven", "sab"),
      List("m.", "p."),
      List("aC", "dC")
    ),
    LocaleTestItem(
      "it-CH",
      cldr21 = false,
      List(
        "gennaio",
        "febbraio",
        "marzo",
        "aprile",
        "maggio",
        "giugno",
        "luglio",
        "agosto",
        "settembre",
        "ottobre",
        "novembre",
        "dicembre",
        ""
      ),
      List("gen", "feb", "mar", "apr", "mag", "giu", "lug", "ago", "set", "ott", "nov", "dic", ""),
      List("", "domenica", "lunedì", "martedì", "mercoledì", "giovedì", "venerdì", "sabato"),
      List("", "dom", "lun", "mar", "mer", "gio", "ven", "sab"),
      List("AM", "PM"),
      List("a.C.", "d.C.")
    ),
    LocaleTestItem(
      "zh",
      cldr21 = true,
      List("1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月", ""),
      List("1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月", ""),
      List("", "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"),
      List("", "周日", "周一", "周二", "周三", "周四", "周五", "周六"),
      List("上午", "下午"),
      List("公元前", "公元")
    ),
    LocaleTestItem(
      "zh",
      cldr21 = false,
      List("一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月", ""),
      List("1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月", ""),
      List("", "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"),
      List("", "周日", "周一", "周二", "周三", "周四", "周五", "周六"),
      List("上午", "下午"),
      List("公元前", "公元")
    ),
    LocaleTestItem(
      "zh-Hant",
      cldr21 = true,
      List("1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月", ""),
      List("1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月", ""),
      List("", "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"),
      List("", "週日", "週一", "週二", "週三", "週四", "週五", "週六"),
      List("上午", "下午"),
      List("西元前", "西元")
    ),
    LocaleTestItem(
      "zh-Hant",
      cldr21 = false,
      List("1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月", ""),
      List("1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月", ""),
      List("", "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"),
      List("", "週日", "週一", "週二", "週三", "週四", "週五", "週六"),
      List("上午", "下午"),
      List("西元前", "西元")
    ),
    LocaleTestItem(
      "ar",
      cldr21 = true,
      List(
        "يناير",
        "فبراير",
        "مارس",
        "أبريل",
        "مايو",
        "يونيو",
        "يوليو",
        "أغسطس",
        "سبتمبر",
        "أكتوبر",
        "نوفمبر",
        "ديسمبر",
        ""
      ),
      List("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", ""),
      List("", "الأحد", "الاثنين", "الثلاثاء", "الأربعاء", "الخميس", "الجمعة", "السبت"),
      List("", "الأحد", "الاثنين", "الثلاثاء", "الأربعاء", "الخميس", "الجمعة", "السبت"),
      List("ص", "م"),
      List("ق.م", "م")
    ),
    LocaleTestItem(
      "ar",
      cldr21 = false,
      List(
        "يناير",
        "فبراير",
        "مارس",
        "أبريل",
        "مايو",
        "يونيو",
        "يوليو",
        "أغسطس",
        "سبتمبر",
        "أكتوبر",
        "نوفمبر",
        "ديسمبر",
        ""
      ),
      List(
        "يناير",
        "فبراير",
        "مارس",
        "أبريل",
        "مايو",
        "يونيو",
        "يوليو",
        "أغسطس",
        "سبتمبر",
        "أكتوبر",
        "نوفمبر",
        "ديسمبر",
        ""
      ),
      List("", "الأحد", "الاثنين", "الثلاثاء", "الأربعاء", "الخميس", "الجمعة", "السبت"),
      List("", "الأحد", "الاثنين", "الثلاثاء", "الأربعاء", "الخميس", "الجمعة", "السبت"),
      List("ص", "م"),
      List("ق.م", "م")
    ),
    LocaleTestItem(
      "fa",
      cldr21 = true,
      List(
        "ژانویهٔ",
        "فوریهٔ",
        "مارس",
        "آوریل",
        "مهٔ",
        "ژوئن",
        "ژوئیهٔ",
        "اوت",
        "سپتامبر",
        "اکتبر",
        "نوامبر",
        "دسامبر",
        ""
      ),
      List(
        "ژانویهٔ",
        "فوریهٔ",
        "مارس",
        "آوریل",
        "مهٔ",
        "ژوئن",
        "ژوئیهٔ",
        "اوت",
        "سپتامبر",
        "اکتبر",
        "نوامبر",
        "دسامبر",
        ""
      ),
      List("", "یکشنبه", "دوشنبه", "سه‌شنبه", "چهارشنبه", "پنجشنبه", "جمعه", "شنبه"),
      List("", "یکشنبه", "دوشنبه", "سه‌شنبه", "چهارشنبه", "پنجشنبه", "جمعه", "شنبه"),
      List("قبل‌ازظهر", "بعدازظهر"),
      List("ق.م.", "م.")
    ),
    LocaleTestItem(
      "fa",
      cldr21 = false,
      List(
        "ژانویهٔ",
        "فوریهٔ",
        "مارس",
        "آوریل",
        "مهٔ",
        "ژوئن",
        "ژوئیهٔ",
        "اوت",
        "سپتامبر",
        "اکتبر",
        "نوامبر",
        "دسامبر",
        ""
      ),
      List(
        "ژانویهٔ",
        "فوریهٔ",
        "مارس",
        "آوریل",
        "مهٔ",
        "ژوئن",
        "ژوئیهٔ",
        "اوت",
        "سپتامبر",
        "اکتبر",
        "نوامبر",
        "دسامبر",
        ""
      ),
      List("", "یکشنبه", "دوشنبه", "سه‌شنبه", "چهارشنبه", "پنجشنبه", "جمعه", "شنبه"),
      List("", "یکشنبه", "دوشنبه", "سه‌شنبه", "چهارشنبه", "پنجشنبه", "جمعه", "شنبه"),
      List("قبل‌ازظهر", "بعدازظهر"),
      List("ق.م.", "م.")
    ),
    LocaleTestItem(
      "fi-FI",
      cldr21 = true,
      List(
        "tammikuuta",
        "helmikuuta",
        "maaliskuuta",
        "huhtikuuta",
        "toukokuuta",
        "kesäkuuta",
        "heinäkuuta",
        "elokuuta",
        "syyskuuta",
        "lokakuuta",
        "marraskuuta",
        "joulukuuta",
        ""
      ),
      List(
        "tammikuuta",
        "helmikuuta",
        "maaliskuuta",
        "huhtikuuta",
        "toukokuuta",
        "kesäkuuta",
        "heinäkuuta",
        "elokuuta",
        "syyskuuta",
        "lokakuuta",
        "marraskuuta",
        "joulukuuta",
        ""
      ),
      List(
        "",
        "sunnuntaina",
        "maanantaina",
        "tiistaina",
        "keskiviikkona",
        "torstaina",
        "perjantaina",
        "lauantaina"
      ),
      List("", "su", "ma", "ti", "ke", "to", "pe", "la"),
      List("ap.", "ip."),
      List("eKr.", "jKr.")
    ),
    LocaleTestItem(
      "fi-FI",
      cldr21 = false,
      List(
        "tammikuuta",
        "helmikuuta",
        "maaliskuuta",
        "huhtikuuta",
        "toukokuuta",
        "kesäkuuta",
        "heinäkuuta",
        "elokuuta",
        "syyskuuta",
        "lokakuuta",
        "marraskuuta",
        "joulukuuta",
        ""
      ),
      List(
        "tammik.",
        "helmik.",
        "maalisk.",
        "huhtik.",
        "toukok.",
        "kesäk.",
        "heinäk.",
        "elok.",
        "syysk.",
        "lokak.",
        "marrask.",
        "jouluk.",
        ""
      ),
      List(
        "",
        "sunnuntaina",
        "maanantaina",
        "tiistaina",
        "keskiviikkona",
        "torstaina",
        "perjantaina",
        "lauantaina"
      ),
      List("", "su", "ma", "ti", "ke", "to", "pe", "la"),
      List("ap.", "ip."),
      List("eKr.", "jKr.")
    ),
    LocaleTestItem(
      "ka",
      cldr21 = true,
      List(
        "იანვარი",
        "თებერვალი",
        "მარტი",
        "აპრილი",
        "მაისი",
        "ივნისი",
        "ივლის",
        "აგვისტო",
        "სექტემბერი",
        "ოქტომბერი",
        "ნოემბერი",
        "დეკემბერი",
        ""
      ),
      List("იან", "თებ", "მარ", "აპრ", "მაი", "ივნ", "ივლ", "აგვ", "სექ", "ოქტ", "ნოე", "დეკ", ""),
      List("", "კვირა", "2", "სამშაბათი", "ოთხშაბათი", "ხუთშაბათი", "პარასკევი", "შაბათი"),
      List("", "", "ორშ", "სამ", "ოთხ", "ხუთ", "პარ", "შაბ"),
      List("AM", "PM"),
      List("ჩვენს წელთაღრიცხვამდე", "ჩვენი წელთაღრიცხვით")
    ),
    LocaleTestItem(
      "ka",
      cldr21 = false,
      List(
        "იანვარი",
        "თებერვალი",
        "მარტი",
        "აპრილი",
        "მაისი",
        "ივნისი",
        "ივლისი",
        "აგვისტო",
        "სექტემბერი",
        "ოქტომბერი",
        "ნოემბერი",
        "დეკემბერი",
        ""
      ),
      List("იან", "თებ", "მარ", "აპრ", "მაი", "ივნ", "ივლ", "აგვ", "სექ", "ოქტ", "ნოე", "დეკ", ""),
      List("", "კვირა", "ორშაბათი", "სამშაბათი", "ოთხშაბათი", "ხუთშაბათი", "პარასკევი", "შაბათი"),
      List("", "კვი", "ორშ", "სამ", "ოთხ", "ხუთ", "პარ", "შაბ"),
      List("AM", "PM"),
      List("ძვ. წ.", "ახ. წ.")
    ),
    LocaleTestItem(
      "lv",
      cldr21 = true,
      List(
        "janvāris",
        "februāris",
        "marts",
        "aprīlis",
        "maijs",
        "jūnijs",
        "jūlijs",
        "augusts",
        "septembris",
        "oktobris",
        "novembris",
        "decembris",
        ""
      ),
      List(
        "janv.",
        "febr.",
        "marts",
        "apr.",
        "maijs",
        "jūn.",
        "jūl.",
        "aug.",
        "sept.",
        "okt.",
        "nov.",
        "dec.",
        ""
      ),
      List(
        "",
        "svētdiena",
        "pirmdiena",
        "otrdiena",
        "trešdiena",
        "ceturtdiena",
        "piektdiena",
        "sestdiena"
      ),
      List("", "Sv", "Pr", "Ot", "Tr", "Ce", "Pk", "Se"),
      List("priekšpusdienā", "pēcpusdienā"),
      List("p.m.ē.", "m.ē.")
    ),
    LocaleTestItem(
      "lv",
      cldr21 = false,
      List(
        "janvāris",
        "februāris",
        "marts",
        "aprīlis",
        "maijs",
        "jūnijs",
        "jūlijs",
        "augusts",
        "septembris",
        "oktobris",
        "novembris",
        "decembris",
        ""
      ),
      List(
        "janv.",
        "febr.",
        "marts",
        "apr.",
        "maijs",
        "jūn.",
        "jūl.",
        "aug.",
        "sept.",
        "okt.",
        "nov.",
        "dec.",
        ""
      ),
      List(
        "",
        "svētdiena",
        "pirmdiena",
        "otrdiena",
        "trešdiena",
        "ceturtdiena",
        "piektdiena",
        "sestdiena"
      ),
      List("", "svētd.", "pirmd.", "otrd.", "trešd.", "ceturtd.", "piektd.", "sestd."),
      List("priekšpusdienā", "pēcpusdienā"),
      List("p.m.ē.", "m.ē.")
    ),
    LocaleTestItem(
      "my",
      cldr21 = true,
      List(
        "ဇန်နဝါရီ",
        "ဖေဖော်ဝါရီ",
        "မတ်",
        "ဧပြီ",
        "မေ",
        "ဇွန်",
        "ဇူလိုင်",
        "ဩဂုတ်",
        "စက်တင်ဘာ",
        "အောက်တိုဘာ",
        "နိုဝင်ဘာ",
        "ဒီဇင်ဘာ",
        ""
      ),
      List("ဇန်", "ဖေ", "မတ်", "ဧ", "မေ", "ဇွန်", "ဇူ", "ဩ", "စက်", "အောက်", "နို", "ဒီ", ""),
      List("", "တနင်္ဂနွေ", "တနင်္လာ", "အင်္ဂါ", "ဗုဒ္ဓဟူး", "ကြာသပတေး", "သောကြာ", "စနေ"),
      List("", "နွေ", "လာ", "ဂါ", "ဟူး", "တေး", "ကြာ", "နေ"),
      List("နံနက်", "ညနေ"),
      List("ဘီစီ", "အေဒီ")
    ),
    LocaleTestItem(
      "my",
      cldr21 = false,
      List(
        "ဇန်နဝါရီ",
        "ဖေဖော်ဝါရီ",
        "မတ်",
        "ဧပြီ",
        "မေ",
        "ဇွန်",
        "ဇူလိုင်",
        "ဩဂုတ်",
        "စက်တင်ဘာ",
        "အောက်တိုဘာ",
        "နိုဝင်ဘာ",
        "ဒီဇင်ဘာ",
        ""
      ),
      List("ဇန်", "ဖေ", "မတ်", "ဧ", "မေ", "ဇွန်", "ဇူ", "ဩ", "စက်", "အောက်", "နို", "ဒီ", ""),
      List("", "တနင်္ဂနွေ", "တနင်္လာ", "အင်္ဂါ", "ဗုဒ္ဓဟူး", "ကြာသပတေး", "သောကြာ", "စနေ"),
      List("", "တနင်္ဂနွေ", "တနင်္လာ", "အင်္ဂါ", "ဗုဒ္ဓဟူး", "ကြာသပတေး", "သောကြာ", "စနေ"),
      List("နံနက်", "ညနေ"),
      List("ဘီစီ", "အဒေီ")
    ),
    LocaleTestItem(
      "smn",
      cldr21 = true,
      List("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", ""),
      List("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", ""),
      List("", "1", "2", "3", "4", "5", "6", "7"),
      List("", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"),
      List("AM", "PM"),
      List("BCE", "CE")
    ),
    LocaleTestItem(
      "smn",
      cldr21 = false,
      List(
        "uđđâivemáánu",
        "kuovâmáánu",
        "njuhčâmáánu",
        "cuáŋuimáánu",
        "vyesimáánu",
        "kesimáánu",
        "syeinimáánu",
        "porgemáánu",
        "čohčâmáánu",
        "roovvâdmáánu",
        "skammâmáánu",
        "juovlâmáánu",
        ""
      ),
      List(
        "uđiv",
        "kuovâ",
        "njuhčâ",
        "cuáŋui",
        "vyesi",
        "kesi",
        "syeini",
        "porge",
        "čohčâ",
        "roovvâd",
        "skammâ",
        "juovlâ",
        ""
      ),
      List(
        "",
        "pasepeeivi",
        "vuossaargâ",
        "majebaargâ",
        "koskoho",
        "tuorâstuv",
        "vástuppeeivi",
        "lávurduv"
      ),
      List("", "pas", "vuo", "maj", "kos", "tuo", "vás", "láv"),
      List("ip.", "ep."),
      List("oKr.", "mKr.")
    ),
    LocaleTestItem(
      "smn-FI",
      cldr21 = true,
      List("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", ""),
      List("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", ""),
      List("", "1", "2", "3", "4", "5", "6", "7"),
      List("", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"),
      List("AM", "PM"),
      List("BCE", "CE")
    ),
    LocaleTestItem(
      "smn-FI",
      cldr21 = false,
      List(
        "uđđâivemáánu",
        "kuovâmáánu",
        "njuhčâmáánu",
        "cuáŋuimáánu",
        "vyesimáánu",
        "kesimáánu",
        "syeinimáánu",
        "porgemáánu",
        "čohčâmáánu",
        "roovvâdmáánu",
        "skammâmáánu",
        "juovlâmáánu",
        ""
      ),
      List(
        "uđiv",
        "kuovâ",
        "njuhčâ",
        "cuáŋui",
        "vyesi",
        "kesi",
        "syeini",
        "porge",
        "čohčâ",
        "roovvâd",
        "skammâ",
        "juovlâ",
        ""
      ),
      List(
        "",
        "pasepeeivi",
        "vuossaargâ",
        "majebaargâ",
        "koskoho",
        "tuorâstuv",
        "vástuppeeivi",
        "lávurduv"
      ),
      List("", "pas", "vuo", "maj", "kos", "tuo", "vás", "láv"),
      List("ip.", "ep."),
      List("oKr.", "mKr.")
    ),
    LocaleTestItem(
      "ru-RU",
      cldr21 = true,
      List(
        "января",
        "февраля",
        "марта",
        "апреля",
        "мая",
        "июня",
        "июля",
        "августа",
        "сентября",
        "октября",
        "ноября",
        "декабря",
        ""
      ),
      List(
        "янв.",
        "февр.",
        "марта",
        "апр.",
        "мая",
        "июня",
        "июля",
        "авг.",
        "сент.",
        "окт.",
        "нояб.",
        "дек.",
        ""
      ),
      List("", "воскресенье", "понедельник", "вторник", "среда", "четверг", "пятница", "суббота"),
      List("", "вс", "пн", "вт", "ср", "чт", "пт", "сб"),
      List("до полудня", "после полудня"),
      List("до н.э.", "н.э.")
    ),
    LocaleTestItem(
      "ru-RU",
      cldr21 = false,
      List(
        "января",
        "февраля",
        "марта",
        "апреля",
        "мая",
        "июня",
        "июля",
        "августа",
        "сентября",
        "октября",
        "ноября",
        "декабря",
        ""
      ),
      List(
        "янв.",
        "февр.",
        "мар.",
        "апр.",
        "мая",
        "июн.",
        "июл.",
        "авг.",
        "сент.",
        "окт.",
        "нояб.",
        "дек.",
        ""
      ),
      List("", "воскресенье", "понедельник", "вторник", "среда", "четверг", "пятница", "суббота"),
      List("", "вс", "пн", "вт", "ср", "чт", "пт", "сб"),
      List("AM", "PM"),
      List("до н. э.", "н. э.")
    )
  )

  def test_dfs(s: DateFormatSymbols, t: LocaleTestItem): Unit =
    //assertArrayEquals(Array[AnyRef](t.months: _*), Array[AnyRef](s.getMonths(): _*))
    //assertArrayEquals(Array[AnyRef](t.shortMonths: _*), Array[AnyRef](s.getShortMonths(): _*))
    for {
      d <- CalendarConstants.SUNDAY to CalendarConstants.SATURDAY
    } {
      assertEquals(t.weekdays(d), s.getWeekdays()(d))
      assertEquals(t.shortWeekdays(d), s.getShortWeekdays()(d))
    }
  //assertArrayEquals(Array[AnyRef](t.amPm: _*), Array[AnyRef](s.getAmPmStrings(): _*))
  //assertArrayEquals(Array[AnyRef](t.eras: _*), Array[AnyRef](s.getEras(): _*))

  test("default_locales_date_format_symbol") {
    standardLocalesData.foreach {
      case (l, t @ LocaleTestItem(_, _, _, _, _, _, _, _)) =>
        val dfs = DateFormatSymbols.getInstance(l)
        test_dfs(dfs, t)
    }
  }

  test("default_locales_date_format_symbol_with_cldr21") {
    standardLocalesDataDiff.foreach {
      case (l, t @ LocaleTestItem(_, cldr21, _, _, _, _, _, _)) =>
        val dfs = DateFormatSymbols.getInstance(l)
        if (Platform.executingInJVM && cldr21) {
          test_dfs(dfs, t)
        }
        if (!Platform.executingInJVM && !cldr21) {
          test_dfs(dfs, t)
        }
    }
  }

  test("extra_locales_date_format_symbols") {
    extraLocalesData.foreach {
      case t @ LocaleTestItem(m, cldr21, _, _, _, _, _, _) =>
        if (Platform.executingInJVM && cldr21) {
          val dfs = DateFormatSymbols.getInstance(Locale.forLanguageTag(m))
          test_dfs(dfs, t)
        }
        if (!Platform.executingInJVM && !cldr21) {
          val dfs = DateFormatSymbols.getInstance(Locale.forLanguageTag(m))
          test_dfs(dfs, t)
        }
    }
  }

  def test_setter(
    get: DateFormatSymbols => Array[String],
    set: (DateFormatSymbols, Array[String]) => Unit
  ): Unit = {
    val dfs = new DateFormatSymbols()
    expectThrows(classOf[NullPointerException], set(dfs, null))
    val value = Array("a", "b")
    set(dfs, value)
    // Check that the passed array is copied
    value(0) = "c"
    assertEquals("a", get(dfs)(0))
  }

  test("zone_strings") {
    val dfs = new DateFormatSymbols()
    expectThrows(classOf[NullPointerException], dfs.setZoneStrings(null))
    val zonesTooFew = Array(Array("a", "b"), Array("c", "d"))
    expectThrows(classOf[IllegalArgumentException], dfs.setZoneStrings(zonesTooFew))
    val zones =
      Array(Array("a", "b", "c", "d", "e"), Array("f", "g", "h", "i", "j"))
    dfs.setZoneStrings(zones)
    // Check that the passed array is copied
    zones(0)(0) = "e"
    assertEquals("a", dfs.getZoneStrings()(0)(0))
  }

  test("setters") {
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

  test("equals") {
    val dfs = new DateFormatSymbols()
    assertEquals(dfs, dfs)
    assertEquals(dfs, new DateFormatSymbols())
    dfs.setEras(Array("a", "b"))
    assert(!dfs.equals(new DateFormatSymbols()))
    assert(!dfs.equals(null))
    assert(!dfs.equals(1))
  }

  test("hash_code") {
    val dfs = new DateFormatSymbols()
    assertEquals(dfs.hashCode, dfs.hashCode)
    assertEquals(dfs.hashCode, new DateFormatSymbols().hashCode)
    dfs.setEras(Array("a", "b"))
    assert(!dfs.hashCode.equals(new DateFormatSymbols().hashCode))
  }

  test("clone") {
    val dfs    = new DateFormatSymbols()
    val cloned = dfs.clone
    assertEquals(dfs, cloned.asInstanceOf[DateFormatSymbols])
    assert(dfs ne cloned)
  }

  test("bad_tag_matches_root_dfs") {
    val l   = Locale.forLanguageTag("no_NO")
    val dfs = DateFormatSymbols.getInstance(l)
    standardLocalesDataDiff.foreach {
      case (_, t @ LocaleTestItem(r, cldr21, _, _, _, _, _, _)) if r == root =>
        if (Platform.executingInJVM && cldr21) {
          test_dfs(dfs, t)
        }
        if (!Platform.executingInJVM && !cldr21) {
          test_dfs(dfs, t)
        }
      case (_, _) =>
    }
  }

}
