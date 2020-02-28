package testsuite.javalib.text

import java.text.{ DateFormat, SimpleDateFormat }
import java.util.Locale
import testsuite.utils.Platform

class DateFormatTest extends munit.FunSuite {
  Locale.setDefault(Locale.ENGLISH)
  val root = Locale.ROOT

  case class TestCase(
    tag:         String,
    l:           Locale,
    cldr21:      Boolean,
    dateFormats: Map[Int, String],
    timeFormats: Map[Int, String]
  )

  test("constants") {
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

  test("available_locales") {
    assert(DateFormat.getAvailableLocales.contains(Locale.ENGLISH))
  }

  test("default_date_format") {
    assertEquals(
      "EEEE, MMMM d, y",
      DateFormat
        .getDateInstance(DateFormat.FULL)
        .asInstanceOf[SimpleDateFormat]
        .toPattern()
    )
    assertEquals(
      "MMMM d, y",
      DateFormat
        .getDateInstance(DateFormat.LONG)
        .asInstanceOf[SimpleDateFormat]
        .toPattern()
    )
    assertEquals(
      "MMM d, y",
      DateFormat
        .getDateInstance(DateFormat.MEDIUM)
        .asInstanceOf[SimpleDateFormat]
        .toPattern()
    )
    assertEquals(
      "M/d/yy",
      DateFormat
        .getDateInstance(DateFormat.SHORT)
        .asInstanceOf[SimpleDateFormat]
        .toPattern()
    )

    assertEquals(
      "h:mm:ss a zzzz",
      DateFormat
        .getTimeInstance(DateFormat.FULL)
        .asInstanceOf[SimpleDateFormat]
        .toPattern()
    )
    assertEquals(
      "h:mm:ss a z",
      DateFormat
        .getTimeInstance(DateFormat.LONG)
        .asInstanceOf[SimpleDateFormat]
        .toPattern()
    )
    assertEquals(
      "h:mm:ss a",
      DateFormat
        .getTimeInstance(DateFormat.MEDIUM)
        .asInstanceOf[SimpleDateFormat]
        .toPattern()
    )
    assertEquals(
      "h:mm a",
      DateFormat
        .getTimeInstance(DateFormat.SHORT)
        .asInstanceOf[SimpleDateFormat]
        .toPattern()
    )

    assertEquals(
      "EEEE, MMMM d, y h:mm:ss a zzzz",
      DateFormat
        .getDateTimeInstance(DateFormat.FULL, DateFormat.FULL)
        .asInstanceOf[SimpleDateFormat]
        .toPattern()
    )
    assertEquals(
      "EEEE, MMMM d, y h:mm:ss a z",
      DateFormat
        .getDateTimeInstance(DateFormat.FULL, DateFormat.LONG)
        .asInstanceOf[SimpleDateFormat]
        .toPattern()
    )
    assertEquals(
      "EEEE, MMMM d, y h:mm:ss a",
      DateFormat
        .getDateTimeInstance(DateFormat.FULL, DateFormat.MEDIUM)
        .asInstanceOf[SimpleDateFormat]
        .toPattern()
    )
    assertEquals(
      "EEEE, MMMM d, y h:mm a",
      DateFormat
        .getDateTimeInstance(DateFormat.FULL, DateFormat.SHORT)
        .asInstanceOf[SimpleDateFormat]
        .toPattern()
    )
    assertEquals(
      "MMMM d, y h:mm:ss a zzzz",
      DateFormat
        .getDateTimeInstance(DateFormat.LONG, DateFormat.FULL)
        .asInstanceOf[SimpleDateFormat]
        .toPattern()
    )
    assertEquals(
      "MMMM d, y h:mm:ss a z",
      DateFormat
        .getDateTimeInstance(DateFormat.LONG, DateFormat.LONG)
        .asInstanceOf[SimpleDateFormat]
        .toPattern()
    )
    assertEquals(
      "MMMM d, y h:mm:ss a",
      DateFormat
        .getDateTimeInstance(DateFormat.LONG, DateFormat.MEDIUM)
        .asInstanceOf[SimpleDateFormat]
        .toPattern()
    )
    assertEquals(
      "MMMM d, y h:mm a",
      DateFormat
        .getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT)
        .asInstanceOf[SimpleDateFormat]
        .toPattern()
    )
    assertEquals(
      "MMM d, y h:mm:ss a zzzz",
      DateFormat
        .getDateTimeInstance(DateFormat.MEDIUM, DateFormat.FULL)
        .asInstanceOf[SimpleDateFormat]
        .toPattern()
    )
    assertEquals(
      "MMM d, y h:mm:ss a z",
      DateFormat
        .getDateTimeInstance(DateFormat.MEDIUM, DateFormat.LONG)
        .asInstanceOf[SimpleDateFormat]
        .toPattern()
    )
    assertEquals(
      "MMM d, y h:mm:ss a",
      DateFormat
        .getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM)
        .asInstanceOf[SimpleDateFormat]
        .toPattern()
    )
    assertEquals(
      "MMM d, y h:mm a",
      DateFormat
        .getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
        .asInstanceOf[SimpleDateFormat]
        .toPattern()
    )
    assertEquals(
      "M/d/yy h:mm:ss a zzzz",
      DateFormat
        .getDateTimeInstance(DateFormat.SHORT, DateFormat.FULL)
        .asInstanceOf[SimpleDateFormat]
        .toPattern()
    )
    assertEquals(
      "M/d/yy h:mm:ss a z",
      DateFormat
        .getDateTimeInstance(DateFormat.SHORT, DateFormat.LONG)
        .asInstanceOf[SimpleDateFormat]
        .toPattern()
    )
    assertEquals(
      "M/d/yy h:mm:ss a",
      DateFormat
        .getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM)
        .asInstanceOf[SimpleDateFormat]
        .toPattern()
    )
    assertEquals(
      "M/d/yy h:mm a",
      DateFormat
        .getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
        .asInstanceOf[SimpleDateFormat]
        .toPattern()
    )
  }

  val stdLocales = List(
    TestCase(
      "",
      Locale.ENGLISH,
      cldr21 = false,
      Map(
        DateFormat.FULL -> "EEEE, MMMM d, y",
        DateFormat.LONG -> "MMMM d, y",
        DateFormat.MEDIUM -> "MMM d, y",
        DateFormat.SHORT -> "M/d/yy"
      ),
      Map(
        DateFormat.FULL -> "h:mm:ss a zzzz",
        DateFormat.LONG -> "h:mm:ss a z",
        DateFormat.MEDIUM -> "h:mm:ss a",
        DateFormat.SHORT -> "h:mm a"
      )
    ),
    TestCase(
      "",
      Locale.US,
      cldr21 = false,
      Map(
        DateFormat.FULL -> "EEEE, MMMM d, y",
        DateFormat.LONG -> "MMMM d, y",
        DateFormat.MEDIUM -> "MMM d, y",
        DateFormat.SHORT -> "M/d/yy"
      ),
      Map(
        DateFormat.FULL -> "h:mm:ss a zzzz",
        DateFormat.LONG -> "h:mm:ss a z",
        DateFormat.MEDIUM -> "h:mm:ss a",
        DateFormat.SHORT -> "h:mm a"
      )
    )
  )

  val stdLocalesDiff = List(
    TestCase(
      "und",
      Locale.ROOT,
      cldr21 = true,
      Map( // JVM
          DateFormat.FULL -> "EEEE, y MMMM dd",
          DateFormat.LONG -> "y MMMM d",
          DateFormat.MEDIUM -> "y MMM d",
          DateFormat.SHORT -> "yyyy-MM-dd"),
      Map(
        DateFormat.FULL -> "HH:mm:ss zzzz",
        DateFormat.LONG -> "HH:mm:ss z",
        DateFormat.MEDIUM -> "HH:mm:ss",
        DateFormat.SHORT -> "HH:mm"
      )
    ),
    TestCase(
      "und",
      Locale.ROOT,
      cldr21 = false,
      Map( // JS
          DateFormat.FULL -> "y MMMM d, EEEE",
          DateFormat.LONG -> "y MMMM d",
          DateFormat.MEDIUM -> "y MMM d",
          DateFormat.SHORT -> "y-MM-dd"),
      Map(
        DateFormat.FULL -> "HH:mm:ss zzzz",
        DateFormat.LONG -> "HH:mm:ss z",
        DateFormat.MEDIUM -> "HH:mm:ss",
        DateFormat.SHORT -> "HH:mm"
      )
    ),
    TestCase(
      "fr",
      Locale.FRENCH,
      cldr21 = true,
      Map( // JVM
          DateFormat.FULL -> "EEEE d MMMM y",
          DateFormat.LONG -> "d MMMM y",
          DateFormat.MEDIUM -> "d MMM y",
          DateFormat.SHORT -> "dd/MM/yy"),
      Map(
        DateFormat.FULL -> "HH:mm:ss zzzz",
        DateFormat.LONG -> "HH:mm:ss z",
        DateFormat.MEDIUM -> "HH:mm:ss",
        DateFormat.SHORT -> "HH:mm"
      )
    ),
    TestCase(
      "fr",
      Locale.FRENCH,
      cldr21 = false,
      Map( // JS
          DateFormat.FULL -> "EEEE d MMMM y",
          DateFormat.LONG -> "d MMMM y",
          DateFormat.MEDIUM -> "d MMM y",
          DateFormat.SHORT -> "dd/MM/y"),
      Map(
        DateFormat.FULL -> "HH:mm:ss zzzz",
        DateFormat.LONG -> "HH:mm:ss z",
        DateFormat.MEDIUM -> "HH:mm:ss",
        DateFormat.SHORT -> "HH:mm"
      )
    ),
    TestCase(
      "de",
      Locale.GERMAN,
      cldr21 = true,
      Map(
        DateFormat.FULL -> "EEEE, d. MMMM y",
        DateFormat.LONG -> "d. MMMM y",
        DateFormat.MEDIUM -> "dd.MM.yyyy",
        DateFormat.SHORT -> "dd.MM.yy"
      ),
      Map(
        DateFormat.FULL -> "HH:mm:ss zzzz",
        DateFormat.LONG -> "HH:mm:ss z",
        DateFormat.MEDIUM -> "HH:mm:ss",
        DateFormat.SHORT -> "HH:mm"
      )
    ),
    TestCase(
      "de",
      Locale.GERMAN,
      cldr21 = false,
      Map(
        DateFormat.FULL -> "EEEE, d. MMMM y",
        DateFormat.LONG -> "d. MMMM y",
        DateFormat.MEDIUM -> "dd.MM.y",
        DateFormat.SHORT -> "dd.MM.yy"
      ),
      Map(
        DateFormat.FULL -> "HH:mm:ss zzzz",
        DateFormat.LONG -> "HH:mm:ss z",
        DateFormat.MEDIUM -> "HH:mm:ss",
        DateFormat.SHORT -> "HH:mm"
      )
    ),
    TestCase(
      "it",
      Locale.ITALIAN,
      cldr21 = false,
      Map(
        DateFormat.FULL -> "EEEE d MMMM y",
        DateFormat.LONG -> "d MMMM y",
        DateFormat.MEDIUM -> "d MMM y",
        DateFormat.SHORT -> "dd/MM/yy"
      ),
      Map(
        DateFormat.FULL -> "HH:mm:ss zzzz",
        DateFormat.LONG -> "HH:mm:ss z",
        DateFormat.MEDIUM -> "HH:mm:ss",
        DateFormat.SHORT -> "HH:mm"
      )
    ),
    TestCase(
      "",
      Locale.JAPANESE,
      cldr21 = true,
      Map(
        DateFormat.FULL -> "y年M月d日EEEE",
        DateFormat.LONG -> "y年M月d日",
        DateFormat.MEDIUM -> "yyyy/MM/dd",
        DateFormat.SHORT -> "yyyy/MM/dd"
      ),
      Map(
        DateFormat.FULL -> "H時mm分ss秒 zzzz",
        DateFormat.LONG -> "H:mm:ss z",
        DateFormat.MEDIUM -> "H:mm:ss",
        DateFormat.SHORT -> "H:mm"
      )
    ),
    TestCase(
      "",
      Locale.JAPANESE,
      cldr21 = false,
      Map(
        DateFormat.FULL -> "y年M月d日EEEE",
        DateFormat.LONG -> "y年M月d日",
        DateFormat.MEDIUM -> "y/MM/dd",
        DateFormat.SHORT -> "y/MM/dd"
      ),
      Map(
        DateFormat.FULL -> "H時mm分ss秒 zzzz",
        DateFormat.LONG -> "H:mm:ss z",
        DateFormat.MEDIUM -> "H:mm:ss",
        DateFormat.SHORT -> "H:mm"
      )
    ),
    TestCase(
      "",
      Locale.KOREAN,
      cldr21 = true,
      Map(
        DateFormat.FULL -> "y년 M월 d일 EEEE",
        DateFormat.LONG -> "y년 M월 d일",
        DateFormat.MEDIUM -> "yyyy. M. d.",
        DateFormat.SHORT -> "yy. M. d."
      ),
      Map(
        DateFormat.FULL -> "a h시 m분 s초 zzzz",
        DateFormat.LONG -> "a h시 m분 s초 z",
        DateFormat.MEDIUM -> "a h:mm:ss",
        DateFormat.SHORT -> "a h:mm"
      )
    ),
    TestCase(
      "",
      Locale.KOREAN,
      cldr21 = false,
      Map(
        DateFormat.FULL -> "y년 M월 d일 EEEE",
        DateFormat.LONG -> "y년 M월 d일",
        DateFormat.MEDIUM -> "y. M. d.",
        DateFormat.SHORT -> "yy. M. d."
      ),
      Map(
        DateFormat.FULL -> "a h시 m분 s초 zzzz",
        DateFormat.LONG -> "a h시 m분 s초 z",
        DateFormat.MEDIUM -> "a h:mm:ss",
        DateFormat.SHORT -> "a h:mm"
      )
    ),
    TestCase(
      "",
      Locale.CHINESE,
      cldr21 = true,
      Map(
        DateFormat.FULL -> "y年M月d日EEEE",
        DateFormat.LONG -> "y年M月d日",
        DateFormat.MEDIUM -> "yyyy-M-d",
        DateFormat.SHORT -> "yy-M-d"
      ),
      Map(
        DateFormat.FULL -> "zzzzah时mm分ss秒",
        DateFormat.LONG -> "zah时mm分ss秒",
        DateFormat.MEDIUM -> "ah:mm:ss",
        DateFormat.SHORT -> "ah:mm"
      )
    ),
    TestCase(
      "",
      Locale.CHINESE,
      cldr21 = false,
      Map(
        DateFormat.FULL -> "y年M月d日EEEE",
        DateFormat.LONG -> "y年M月d日",
        DateFormat.MEDIUM -> "y年M月d日",
        DateFormat.SHORT -> "y/M/d"
      ),
      Map(
        DateFormat.FULL -> "zzzz ah:mm:ss",
        DateFormat.LONG -> "z ah:mm:ss",
        DateFormat.MEDIUM -> "ah:mm:ss",
        DateFormat.SHORT -> "ah:mm"
      )
    ),
    TestCase(
      "",
      Locale.SIMPLIFIED_CHINESE,
      cldr21 = true,
      Map(
        DateFormat.FULL -> "y年M月d日EEEE",
        DateFormat.LONG -> "y年M月d日",
        DateFormat.MEDIUM -> "yyyy-M-d",
        DateFormat.SHORT -> "yy-M-d"
      ),
      Map(
        DateFormat.FULL -> "zzzzah时mm分ss秒",
        DateFormat.LONG -> "zah时mm分ss秒",
        DateFormat.MEDIUM -> "ah:mm:ss",
        DateFormat.SHORT -> "ah:mm"
      )
    ),
    TestCase(
      "",
      Locale.SIMPLIFIED_CHINESE,
      cldr21 = false,
      Map(
        DateFormat.FULL -> "y年M月d日EEEE",
        DateFormat.LONG -> "y年M月d日",
        DateFormat.MEDIUM -> "y年M月d日",
        DateFormat.SHORT -> "y/M/d"
      ),
      Map(
        DateFormat.FULL -> "zzzz ah:mm:ss",
        DateFormat.LONG -> "z ah:mm:ss",
        DateFormat.MEDIUM -> "ah:mm:ss",
        DateFormat.SHORT -> "ah:mm"
      )
    ),
    TestCase(
      "",
      Locale.TRADITIONAL_CHINESE,
      cldr21 = true,
      Map(
        DateFormat.FULL -> "y年M月d日EEEE",
        DateFormat.LONG -> "y年M月d日",
        DateFormat.MEDIUM -> "yyyy/M/d",
        DateFormat.SHORT -> "y/M/d"
      ),
      Map(
        DateFormat.FULL -> "zzzzah時mm分ss秒",
        DateFormat.LONG -> "zah時mm分ss秒",
        DateFormat.MEDIUM -> "ah:mm:ss",
        DateFormat.SHORT -> "ah:mm"
      )
    ),
    TestCase(
      "",
      Locale.TRADITIONAL_CHINESE,
      cldr21 = false,
      Map(
        DateFormat.FULL -> "y年M月d日 EEEE",
        DateFormat.LONG -> "y年M月d日",
        DateFormat.MEDIUM -> "y年M月d日",
        DateFormat.SHORT -> "y/M/d"
      ),
      Map(
        DateFormat.FULL -> "ah:mm:ss [zzzz]",
        DateFormat.LONG -> "ah:mm:ss [z]",
        DateFormat.MEDIUM -> "ah:mm:ss",
        DateFormat.SHORT -> "ah:mm"
      )
    ),
    TestCase(
      "",
      Locale.FRANCE,
      cldr21 = true,
      Map( // JVM
          DateFormat.FULL -> "EEEE d MMMM y",
          DateFormat.LONG -> "d MMMM y",
          DateFormat.MEDIUM -> "d MMM y",
          DateFormat.SHORT -> "dd/MM/yy"),
      Map(
        DateFormat.FULL -> "HH:mm:ss zzzz",
        DateFormat.LONG -> "HH:mm:ss z",
        DateFormat.MEDIUM -> "HH:mm:ss",
        DateFormat.SHORT -> "HH:mm"
      )
    ),
    TestCase(
      "",
      Locale.FRANCE,
      cldr21 = false,
      Map( // JS
          DateFormat.FULL -> "EEEE d MMMM y",
          DateFormat.LONG -> "d MMMM y",
          DateFormat.MEDIUM -> "d MMM y",
          DateFormat.SHORT -> "dd/MM/y"),
      Map(
        DateFormat.FULL -> "HH:mm:ss zzzz",
        DateFormat.LONG -> "HH:mm:ss z",
        DateFormat.MEDIUM -> "HH:mm:ss",
        DateFormat.SHORT -> "HH:mm"
      )
    ),
    TestCase(
      "",
      Locale.GERMANY,
      cldr21 = true,
      Map(
        DateFormat.FULL -> "EEEE, d. MMMM y",
        DateFormat.LONG -> "d. MMMM y",
        DateFormat.MEDIUM -> "dd.MM.yyyy",
        DateFormat.SHORT -> "dd.MM.yy"
      ),
      Map(
        DateFormat.FULL -> "HH:mm:ss zzzz",
        DateFormat.LONG -> "HH:mm:ss z",
        DateFormat.MEDIUM -> "HH:mm:ss",
        DateFormat.SHORT -> "HH:mm"
      )
    ),
    TestCase(
      "",
      Locale.GERMANY,
      cldr21 = false,
      Map(
        DateFormat.FULL -> "EEEE, d. MMMM y",
        DateFormat.LONG -> "d. MMMM y",
        DateFormat.MEDIUM -> "dd.MM.y",
        DateFormat.SHORT -> "dd.MM.yy"
      ),
      Map(
        DateFormat.FULL -> "HH:mm:ss zzzz",
        DateFormat.LONG -> "HH:mm:ss z",
        DateFormat.MEDIUM -> "HH:mm:ss",
        DateFormat.SHORT -> "HH:mm"
      )
    ),
    TestCase(
      "",
      Locale.ITALY,
      cldr21 = true,
      Map(
        DateFormat.FULL -> "EEEE d MMMM y",
        DateFormat.LONG -> "dd MMMM y",
        DateFormat.MEDIUM -> "dd/MMM/y",
        DateFormat.SHORT -> "dd/MM/yy"
      ),
      Map(
        DateFormat.FULL -> "HH:mm:ss zzzz",
        DateFormat.LONG -> "HH:mm:ss z",
        DateFormat.MEDIUM -> "HH:mm:ss",
        DateFormat.SHORT -> "HH:mm"
      )
    ),
    TestCase(
      "",
      Locale.ITALY,
      cldr21 = false,
      Map(
        DateFormat.FULL -> "EEEE d MMMM y",
        DateFormat.LONG -> "d MMMM y",
        DateFormat.MEDIUM -> "d MMM y",
        DateFormat.SHORT -> "dd/MM/yy"
      ),
      Map(
        DateFormat.FULL -> "HH:mm:ss zzzz",
        DateFormat.LONG -> "HH:mm:ss z",
        DateFormat.MEDIUM -> "HH:mm:ss",
        DateFormat.SHORT -> "HH:mm"
      )
    ),
    TestCase(
      "",
      Locale.JAPAN,
      cldr21 = true,
      Map(
        DateFormat.FULL -> "y年M月d日EEEE",
        DateFormat.LONG -> "y年M月d日",
        DateFormat.MEDIUM -> "yyyy/MM/dd",
        DateFormat.SHORT -> "yyyy/MM/dd"
      ),
      Map(
        DateFormat.FULL -> "H時mm分ss秒 zzzz",
        DateFormat.LONG -> "H:mm:ss z",
        DateFormat.MEDIUM -> "H:mm:ss",
        DateFormat.SHORT -> "H:mm"
      )
    ),
    TestCase(
      "",
      Locale.JAPAN,
      cldr21 = false,
      Map(
        DateFormat.FULL -> "y年M月d日EEEE",
        DateFormat.LONG -> "y年M月d日",
        DateFormat.MEDIUM -> "y/MM/dd",
        DateFormat.SHORT -> "y/MM/dd"
      ),
      Map(
        DateFormat.FULL -> "H時mm分ss秒 zzzz",
        DateFormat.LONG -> "H:mm:ss z",
        DateFormat.MEDIUM -> "H:mm:ss",
        DateFormat.SHORT -> "H:mm"
      )
    ),
    TestCase(
      "",
      Locale.KOREA,
      cldr21 = true,
      Map(
        DateFormat.FULL -> "y년 M월 d일 EEEE",
        DateFormat.LONG -> "y년 M월 d일",
        DateFormat.MEDIUM -> "yyyy. M. d.",
        DateFormat.SHORT -> "yy. M. d."
      ),
      Map(
        DateFormat.FULL -> "a h시 m분 s초 zzzz",
        DateFormat.LONG -> "a h시 m분 s초 z",
        DateFormat.MEDIUM -> "a h:mm:ss",
        DateFormat.SHORT -> "a h:mm"
      )
    ),
    TestCase(
      "",
      Locale.KOREA,
      cldr21 = false,
      Map(
        DateFormat.FULL -> "y년 M월 d일 EEEE",
        DateFormat.LONG -> "y년 M월 d일",
        DateFormat.MEDIUM -> "y. M. d.",
        DateFormat.SHORT -> "yy. M. d."
      ),
      Map(
        DateFormat.FULL -> "a h시 m분 s초 zzzz",
        DateFormat.LONG -> "a h시 m분 s초 z",
        DateFormat.MEDIUM -> "a h:mm:ss",
        DateFormat.SHORT -> "a h:mm"
      )
    ),
    TestCase(
      "",
      Locale.CHINA,
      cldr21 = true,
      Map(
        DateFormat.FULL -> "y年M月d日EEEE",
        DateFormat.LONG -> "y年M月d日",
        DateFormat.MEDIUM -> "yyyy-M-d",
        DateFormat.SHORT -> "yy-M-d"
      ),
      Map(
        DateFormat.FULL -> "zzzzah时mm分ss秒",
        DateFormat.LONG -> "zah时mm分ss秒",
        DateFormat.MEDIUM -> "ah:mm:ss",
        DateFormat.SHORT -> "ah:mm"
      )
    ),
    TestCase(
      "",
      Locale.CHINA,
      cldr21 = false,
      Map(
        DateFormat.FULL -> "y年M月d日EEEE",
        DateFormat.LONG -> "y年M月d日",
        DateFormat.MEDIUM -> "y年M月d日",
        DateFormat.SHORT -> "y/M/d"
      ),
      Map(
        DateFormat.FULL -> "zzzz ah:mm:ss",
        DateFormat.LONG -> "z ah:mm:ss",
        DateFormat.MEDIUM -> "ah:mm:ss",
        DateFormat.SHORT -> "ah:mm"
      )
    ),
    TestCase(
      "",
      Locale.TAIWAN,
      cldr21 = true,
      Map(
        DateFormat.FULL -> "y年M月d日EEEE",
        DateFormat.LONG -> "y年M月d日",
        DateFormat.MEDIUM -> "yyyy/M/d",
        DateFormat.SHORT -> "y/M/d"
      ),
      Map(
        DateFormat.FULL -> "zzzzah時mm分ss秒",
        DateFormat.LONG -> "zah時mm分ss秒",
        DateFormat.MEDIUM -> "ah:mm:ss",
        DateFormat.SHORT -> "ah:mm"
      )
    ),
    TestCase(
      "",
      Locale.TAIWAN,
      cldr21 = false,
      Map(
        DateFormat.FULL -> "y年M月d日 EEEE",
        DateFormat.LONG -> "y年M月d日",
        DateFormat.MEDIUM -> "y年M月d日",
        DateFormat.SHORT -> "y/M/d"
      ),
      Map(
        DateFormat.FULL -> "ah:mm:ss [zzzz]",
        DateFormat.LONG -> "ah:mm:ss [z]",
        DateFormat.MEDIUM -> "ah:mm:ss",
        DateFormat.SHORT -> "ah:mm"
      )
    ),
    TestCase(
      "",
      Locale.UK,
      cldr21 = true,
      Map(
        DateFormat.FULL -> "EEEE, d MMMM y",
        DateFormat.LONG -> "d MMMM y",
        DateFormat.MEDIUM -> "d MMM y",
        DateFormat.SHORT -> "dd/MM/yyyy"
      ),
      Map(
        DateFormat.FULL -> "HH:mm:ss zzzz",
        DateFormat.LONG -> "HH:mm:ss z",
        DateFormat.MEDIUM -> "HH:mm:ss",
        DateFormat.SHORT -> "HH:mm"
      )
    ),
    TestCase(
      "",
      Locale.UK,
      cldr21 = false,
      Map(
        DateFormat.FULL -> "EEEE, d MMMM y",
        DateFormat.LONG -> "d MMMM y",
        DateFormat.MEDIUM -> "d MMM y",
        DateFormat.SHORT -> "dd/MM/y"
      ),
      Map(
        DateFormat.FULL -> "HH:mm:ss zzzz",
        DateFormat.LONG -> "HH:mm:ss z",
        DateFormat.MEDIUM -> "HH:mm:ss",
        DateFormat.SHORT -> "HH:mm"
      )
    ),
    TestCase(
      "",
      Locale.CANADA,
      cldr21 = true,
      Map(
        DateFormat.FULL -> "EEEE, d MMMM, y",
        DateFormat.LONG -> "d MMMM, y",
        DateFormat.MEDIUM -> "yyyy-MM-dd",
        DateFormat.SHORT -> "yy-MM-dd"
      ),
      Map(
        DateFormat.FULL -> "h:mm:ss a zzzz",
        DateFormat.LONG -> "h:mm:ss a z",
        DateFormat.MEDIUM -> "h:mm:ss a",
        DateFormat.SHORT -> "h:mm a"
      )
    ),
    TestCase(
      "",
      Locale.CANADA,
      cldr21 = false,
      Map(
        DateFormat.FULL -> "EEEE, MMMM d, y",
        DateFormat.LONG -> "MMMM d, y",
        DateFormat.MEDIUM -> "MMM d, y",
        DateFormat.SHORT -> "y-MM-dd"
      ),
      Map(
        DateFormat.FULL -> "h:mm:ss a zzzz",
        DateFormat.LONG -> "h:mm:ss a z",
        DateFormat.MEDIUM -> "h:mm:ss a",
        DateFormat.SHORT -> "h:mm a"
      )
    ),
    TestCase(
      "",
      Locale.CANADA_FRENCH,
      cldr21 = true,
      Map( // JVM
          DateFormat.FULL -> "EEEE d MMMM y",
          DateFormat.LONG -> "d MMMM y",
          DateFormat.MEDIUM -> "yyyy-MM-dd",
          DateFormat.SHORT -> "yy-MM-dd"),
      Map(
        DateFormat.FULL -> "HH 'h' mm 'min' ss 's' zzzz",
        DateFormat.LONG -> "HH:mm:ss z",
        DateFormat.MEDIUM -> "HH:mm:ss",
        DateFormat.SHORT -> "HH:mm"
      )
    ),
    TestCase(
      "ca_fr",
      Locale.CANADA_FRENCH,
      cldr21 = false,
      Map( // JS
          DateFormat.FULL -> "EEEE d MMMM y",
          DateFormat.LONG -> "d MMMM y",
          DateFormat.MEDIUM -> "d MMM y",
          DateFormat.SHORT -> "y-MM-dd"),
      Map(
        DateFormat.FULL -> "HH 'h' mm 'min' ss 's' zzzz",
        DateFormat.LONG -> "HH 'h' mm 'min' ss 's' z",
        DateFormat.MEDIUM -> "HH 'h' mm 'min' ss 's'",
        DateFormat.SHORT -> "HH 'h' mm"
      )
    )
  )

  // Test cases by language tag where the JVM gives the same as JS
  val localesByTag = List(
    TestCase(
      "bn",
      Locale.ROOT,
      cldr21 = true,
      Map( // JVM
          DateFormat.FULL -> "EEEE, d MMMM, y",
          DateFormat.LONG -> "d MMMM, y",
          DateFormat.MEDIUM -> "d MMM, y",
          DateFormat.SHORT -> "d/M/yy"),
      Map(
        DateFormat.FULL -> "h:mm:ss a zzzz",
        DateFormat.LONG -> "h:mm:ss a z",
        DateFormat.MEDIUM -> "h:mm:ss a",
        DateFormat.SHORT -> "h:mm a"
      )
    ),
    TestCase(
      "lv",
      Locale.ROOT,
      cldr21 = false,
      Map( // JS
        DateFormat.FULL -> "EEEE, y. 'gada' d. MMMM",
        DateFormat.LONG -> "y. 'gada' d. MMMM",
        DateFormat.MEDIUM -> "y. 'gada' d. MMM",
        DateFormat.SHORT -> "dd.MM.yy"
      ),
      Map(
        DateFormat.FULL -> "HH:mm:ss zzzz",
        DateFormat.LONG -> "HH:mm:ss z",
        DateFormat.MEDIUM -> "HH:mm:ss",
        DateFormat.SHORT -> "HH:mm"
      )
    )
  )

  // Test cases by language tag where the JVM differs from JS
  val localesByTagDiff = List(
    TestCase(
      "af",
      Locale.ROOT,
      cldr21 = true,
      Map( // JVM
          DateFormat.FULL -> "EEEE dd MMMM y",
          DateFormat.LONG -> "dd MMMM y",
          DateFormat.MEDIUM -> "dd MMM y",
          DateFormat.SHORT -> "yyyy-MM-dd"),
      Map(
        DateFormat.FULL -> "h:mm:ss a zzzz",
        DateFormat.LONG -> "h:mm:ss a z",
        DateFormat.MEDIUM -> "h:mm:ss a",
        DateFormat.SHORT -> "h:mm a"
      )
    ),
    TestCase(
      "af",
      Locale.ROOT,
      cldr21 = false,
      Map( // JS
          DateFormat.FULL -> "EEEE dd MMMM y",
          DateFormat.LONG -> "dd MMMM y",
          DateFormat.MEDIUM -> "dd MMM y",
          DateFormat.SHORT -> "y-MM-dd"),
      Map(
        DateFormat.FULL -> "HH:mm:ss zzzz",
        DateFormat.LONG -> "HH:mm:ss z",
        DateFormat.MEDIUM -> "HH:mm:ss",
        DateFormat.SHORT -> "HH:mm"
      )
    ),
    TestCase(
      "az",
      Locale.ROOT,
      cldr21 = true,
      Map( // JVM
          DateFormat.FULL -> "EEEE, d, MMMM, y",
          DateFormat.LONG -> "d MMMM , y",
          DateFormat.MEDIUM -> "d MMM, y",
          DateFormat.SHORT -> "yyyy-MM-dd"),
      Map(
        DateFormat.FULL -> "HH:mm:ss zzzz",
        DateFormat.LONG -> "HH:mm:ss z",
        DateFormat.MEDIUM -> "HH:mm:ss",
        DateFormat.SHORT -> "HH:mm"
      )
    ),
    TestCase(
      "az",
      Locale.ROOT,
      cldr21 = false,
      Map( // JS
          DateFormat.FULL -> "d MMMM y, EEEE",
          DateFormat.LONG -> "d MMMM y",
          DateFormat.MEDIUM -> "d MMM y",
          DateFormat.SHORT -> "dd.MM.yy"),
      Map(
        DateFormat.FULL -> "HH:mm:ss zzzz",
        DateFormat.LONG -> "HH:mm:ss z",
        DateFormat.MEDIUM -> "HH:mm:ss",
        DateFormat.SHORT -> "HH:mm"
      )
    ),
    TestCase(
      "az-Cyrl",
      Locale.ROOT,
      cldr21 = true,
      Map( // JVM
          DateFormat.FULL -> "EEEE, d, MMMM, y",
          DateFormat.LONG -> "d MMMM , y",
          DateFormat.MEDIUM -> "d MMM, y",
          DateFormat.SHORT -> "yyyy-MM-dd"),
      Map(
        DateFormat.FULL -> "HH:mm:ss zzzz",
        DateFormat.LONG -> "HH:mm:ss z",
        DateFormat.MEDIUM -> "HH:mm:ss",
        DateFormat.SHORT -> "HH:mm"
      )
    ),
    TestCase(
      "az-Cyrl",
      Locale.ROOT,
      cldr21 = false,
      Map( // JS
          DateFormat.FULL -> "d MMMM y, EEEE",
          DateFormat.LONG -> "d MMMM y",
          DateFormat.MEDIUM -> "d MMM y",
          DateFormat.SHORT -> "dd.MM.yy"),
      Map(
        DateFormat.FULL -> "HH:mm:ss zzzz",
        DateFormat.LONG -> "HH:mm:ss z",
        DateFormat.MEDIUM -> "HH:mm:ss",
        DateFormat.SHORT -> "HH:mm"
      )
    ),
    TestCase(
      "es-CL",
      Locale.ROOT,
      cldr21 = true,
      Map( // JVM
          DateFormat.FULL -> "EEEE, d 'de' MMMM 'de' y",
          DateFormat.LONG -> "d 'de' MMMM 'de' y",
          DateFormat.MEDIUM -> "dd-MM-yyyy",
          DateFormat.SHORT -> "dd-MM-yy"),
      Map(
        DateFormat.FULL -> "HH:mm:ss zzzz",
        DateFormat.LONG -> "H:mm:ss z",
        DateFormat.MEDIUM -> "H:mm:ss",
        DateFormat.SHORT -> "H:mm"
      )
    ),
    TestCase(
      "es-CL",
      Locale.ROOT,
      cldr21 = false,
      Map( // JS
          DateFormat.FULL -> "EEEE, d 'de' MMMM 'de' y",
          DateFormat.LONG -> "d 'de' MMMM 'de' y",
          DateFormat.MEDIUM -> "dd-MM-y",
          DateFormat.SHORT -> "dd-MM-yy"),
      Map(
        DateFormat.FULL -> "HH:mm:ss zzzz",
        DateFormat.LONG -> "HH:mm:ss z",
        DateFormat.MEDIUM -> "HH:mm:ss",
        DateFormat.SHORT -> "HH:mm"
      )
    ),
    TestCase(
      "it-CH",
      Locale.ROOT,
      cldr21 = true,
      Map( // JVM
          DateFormat.FULL -> "EEEE, d MMMM y",
          DateFormat.LONG -> "d MMMM y",
          DateFormat.MEDIUM -> "d-MMM-y",
          DateFormat.SHORT -> "dd.MM.yy"),
      Map(
        DateFormat.FULL -> "HH.mm:ss 'h' zzzz",
        DateFormat.LONG -> "HH:mm:ss z",
        DateFormat.MEDIUM -> "HH:mm:ss",
        DateFormat.SHORT -> "HH:mm"
      )
    ),
    TestCase(
      "it-CH",
      Locale.ROOT,
      cldr21 = false,
      Map( // JS
          DateFormat.FULL -> "EEEE, d MMMM y",
          DateFormat.LONG -> "d MMMM y",
          DateFormat.MEDIUM -> "d MMM y",
          DateFormat.SHORT -> "dd.MM.yy"),
      Map(
        DateFormat.FULL -> "HH:mm:ss zzzz",
        DateFormat.LONG -> "HH:mm:ss z",
        DateFormat.MEDIUM -> "HH:mm:ss",
        DateFormat.SHORT -> "HH:mm"
      )
    ),
    TestCase(
      "zh",
      Locale.ROOT,
      cldr21 = true,
      Map( // JVM
          DateFormat.FULL -> "y年M月d日EEEE",
          DateFormat.LONG -> "y年M月d日",
          DateFormat.MEDIUM -> "yyyy-M-d",
          DateFormat.SHORT -> "yy-M-d"),
      Map(
        DateFormat.FULL -> "zzzzah时mm分ss秒",
        DateFormat.LONG -> "zah时mm分ss秒",
        DateFormat.MEDIUM -> "ah:mm:ss",
        DateFormat.SHORT -> "ah:mm"
      )
    ),
    TestCase(
      "zh",
      Locale.ROOT,
      cldr21 = false,
      Map( // JS
          DateFormat.FULL -> "y年M月d日EEEE",
          DateFormat.LONG -> "y年M月d日",
          DateFormat.MEDIUM -> "y年M月d日",
          DateFormat.SHORT -> "y/M/d"),
      Map(
        DateFormat.FULL -> "zzzz ah:mm:ss",
        DateFormat.LONG -> "z ah:mm:ss",
        DateFormat.MEDIUM -> "ah:mm:ss",
        DateFormat.SHORT -> "ah:mm"
      )
    ),
    TestCase(
      "zh-Hant",
      Locale.ROOT,
      cldr21 = true,
      Map( // JVM
          DateFormat.FULL -> "y年M月d日EEEE",
          DateFormat.LONG -> "y年M月d日",
          DateFormat.MEDIUM -> "yyyy/M/d",
          DateFormat.SHORT -> "y/M/d"),
      Map(
        DateFormat.FULL -> "zzzzah時mm分ss秒",
        DateFormat.LONG -> "zah時mm分ss秒",
        DateFormat.MEDIUM -> "ah:mm:ss",
        DateFormat.SHORT -> "ah:mm"
      )
    ),
    TestCase(
      "zh-Hant",
      Locale.ROOT,
      cldr21 = false,
      Map( // JS
          DateFormat.FULL -> "y年M月d日 EEEE",
          DateFormat.LONG -> "y年M月d日",
          DateFormat.MEDIUM -> "y年M月d日",
          DateFormat.SHORT -> "y/M/d"),
      Map(
        DateFormat.FULL -> "ah:mm:ss [zzzz]",
        DateFormat.LONG -> "ah:mm:ss [z]",
        DateFormat.MEDIUM -> "ah:mm:ss",
        DateFormat.SHORT -> "ah:mm"
      )
    ),
    TestCase(
      "ar",
      Locale.ROOT,
      cldr21 = true,
      Map( // JVM
          DateFormat.FULL -> "EEEE، d MMMM، y",
          DateFormat.LONG -> "d MMMM، y",
          DateFormat.MEDIUM -> "dd‏/MM‏/yyyy",
          DateFormat.SHORT -> "d‏/M‏/yyyy"),
      Map(
        DateFormat.FULL -> "zzzz h:mm:ss a",
        DateFormat.LONG -> "z h:mm:ss a",
        DateFormat.MEDIUM -> "h:mm:ss a",
        DateFormat.SHORT -> "h:mm a"
      )
    ),
    TestCase(
      "ar",
      Locale.ROOT,
      cldr21 = false,
      Map( // JS
          DateFormat.FULL -> "EEEE، d MMMM y",
          DateFormat.LONG -> "d MMMM y",
          DateFormat.MEDIUM -> "dd‏/MM‏/y",
          DateFormat.SHORT -> "d‏/M‏/y"),
      Map(
        DateFormat.FULL -> "h:mm:ss a zzzz",
        DateFormat.LONG -> "h:mm:ss a z",
        DateFormat.MEDIUM -> "h:mm:ss a",
        DateFormat.SHORT -> "h:mm a"
      )
    ),
    TestCase(
      "fa",
      Locale.ROOT,
      cldr21 = true,
      Map( // JVM
          DateFormat.FULL -> "EEEE d MMMM y",
          DateFormat.LONG -> "d MMMM y",
          DateFormat.MEDIUM -> "d MMM y",
          DateFormat.SHORT -> "yyyy/M/d"),
      Map(
        DateFormat.FULL -> "H:mm:ss (zzzz)",
        DateFormat.LONG -> "H:mm:ss (z)",
        DateFormat.MEDIUM -> "H:mm:ss",
        DateFormat.SHORT -> "H:mm"
      )
    ),
    TestCase(
      "fa",
      Locale.ROOT,
      cldr21 = false,
      Map( // JS
          DateFormat.FULL -> "EEEE d MMMM y",
          DateFormat.LONG -> "d MMMM y",
          DateFormat.MEDIUM -> "d MMM y",
          DateFormat.SHORT -> "y/M/d"),
      Map(
        DateFormat.FULL -> "H:mm:ss (zzzz)",
        DateFormat.LONG -> "H:mm:ss (z)",
        DateFormat.MEDIUM -> "H:mm:ss",
        DateFormat.SHORT -> "H:mm"
      )
    ),
    TestCase(
      "fi-FI",
      Locale.ROOT,
      cldr21 = true,
      Map( // JVM
          DateFormat.FULL -> "EEEE, d. MMMM y",
          DateFormat.LONG -> "d. MMMM y",
          DateFormat.MEDIUM -> "d.M.yyyy",
          DateFormat.SHORT -> "d.M.yyyy"),
      Map(
        DateFormat.FULL -> "H.mm.ss zzzz",
        DateFormat.LONG -> "H.mm.ss z",
        DateFormat.MEDIUM -> "H.mm.ss",
        DateFormat.SHORT -> "H.mm"
      )
    ),
    TestCase(
      "fi-FI",
      Locale.ROOT,
      cldr21 = false,
      Map( // JS
          DateFormat.FULL -> "cccc d. MMMM y",
          DateFormat.LONG -> "d. MMMM y",
          DateFormat.MEDIUM -> "d.M.y",
          DateFormat.SHORT -> "d.M.y"),
      Map(
        DateFormat.FULL -> "H.mm.ss zzzz",
        DateFormat.LONG -> "H.mm.ss z",
        DateFormat.MEDIUM -> "H.mm.ss",
        DateFormat.SHORT -> "H.mm"
      )
    ),
    TestCase(
      "ka",
      Locale.ROOT,
      cldr21 = true,
      Map( // JVM
          DateFormat.FULL -> "EEEE, y MMMM dd",
          DateFormat.LONG -> "y MMMM d",
          DateFormat.MEDIUM -> "y MMM d",
          DateFormat.SHORT -> "yyyy-MM-dd"),
      Map(
        DateFormat.FULL -> "HH:mm:ss zzzz",
        DateFormat.LONG -> "HH:mm:ss z",
        DateFormat.MEDIUM -> "HH:mm:ss",
        DateFormat.SHORT -> "HH:mm"
      )
    ),
    TestCase(
      "ka",
      Locale.ROOT,
      cldr21 = false,
      Map( // JS
          DateFormat.FULL -> "EEEE, dd MMMM, y",
          DateFormat.LONG -> "d MMMM, y",
          DateFormat.MEDIUM -> "d MMM. y",
          DateFormat.SHORT -> "dd.MM.yy"),
      Map(
        DateFormat.FULL -> "HH:mm:ss zzzz",
        DateFormat.LONG -> "HH:mm:ss z",
        DateFormat.MEDIUM -> "HH:mm:ss",
        DateFormat.SHORT -> "HH:mm"
      )
    ),
    TestCase(
      "my",
      Locale.ROOT,
      cldr21 = true,
      Map( // JVM
          DateFormat.FULL -> "EEEE, y MMMM dd",
          DateFormat.LONG -> "y MMMM d",
          DateFormat.MEDIUM -> "y MMM d",
          DateFormat.SHORT -> "yy/MM/dd"),
      Map(
        DateFormat.FULL -> "HH:mm:ss zzzz",
        DateFormat.LONG -> "HH:mm:ss z",
        DateFormat.MEDIUM -> "HH:mm:ss",
        DateFormat.SHORT -> "HH:mm"
      )
    ),
    TestCase(
      "my",
      Locale.ROOT,
      cldr21 = false,
      Map( // JS
          DateFormat.FULL -> "y၊ MMMM d၊ EEEE",
          DateFormat.LONG -> "y၊ d MMMM",
          DateFormat.MEDIUM -> "y၊ MMM d",
          DateFormat.SHORT -> "dd-MM-yy"),
      Map(
        DateFormat.FULL -> "zzzz HH:mm:ss",
        DateFormat.LONG -> "z HH:mm:ss",
        DateFormat.MEDIUM -> "B HH:mm:ss",
        DateFormat.SHORT -> "B H:mm"
      )
    ),
    TestCase(
      "ru-RU",
      Locale.ROOT,
      cldr21 = true,
      Map( // JVM
          DateFormat.FULL -> "EEEE, d MMMM y\u00A0'г'.",
          DateFormat.LONG -> "d MMMM y\u00A0'г'.",
          DateFormat.MEDIUM -> "dd.MM.yyyy",
          DateFormat.SHORT -> "dd.MM.yy"),
      Map(
        DateFormat.FULL -> "H:mm:ss zzzz",
        DateFormat.LONG -> "H:mm:ss z",
        DateFormat.MEDIUM -> "H:mm:ss",
        DateFormat.SHORT -> "H:mm"
      )
    ),
    TestCase(
      "ru-RU",
      Locale.ROOT,
      cldr21 = false,
      Map( // JS
          DateFormat.FULL -> "EEEE, d MMMM y 'г'.",
          DateFormat.LONG -> "d MMMM y 'г'.",
          DateFormat.MEDIUM -> "d MMM y 'г'.",
          DateFormat.SHORT -> "dd.MM.y"),
      Map(
        DateFormat.FULL -> "HH:mm:ss zzzz",
        DateFormat.LONG -> "HH:mm:ss z",
        DateFormat.MEDIUM -> "HH:mm:ss",
        DateFormat.SHORT -> "HH:mm"
      )
    ),
    TestCase(
      "nb-NO",
      Locale.ROOT,
      cldr21 = false,
      Map( // JS
          DateFormat.FULL -> "EEEE d. MMMM y",
          DateFormat.LONG -> "d. MMMM y",
          DateFormat.MEDIUM -> "d. MMM y",
          DateFormat.SHORT -> "dd.MM.y"),
      Map(
        DateFormat.FULL -> "HH:mm:ss zzzz",
        DateFormat.LONG -> "HH:mm:ss z",
        DateFormat.MEDIUM -> "HH:mm:ss",
        DateFormat.SHORT -> "HH:mm"
      )
    )
  )

  test("standard_locales") {
    stdLocales.foreach { tc =>
      for {
        df <- tc.dateFormats
      } yield {
        assertEquals(
          df._2,
          DateFormat
            .getDateInstance(df._1, tc.l)
            .asInstanceOf[SimpleDateFormat]
            .toPattern()
        )
      }

      for {
        tf <- tc.timeFormats
      } yield {
        assertEquals(
          tf._2,
          DateFormat
            .getTimeInstance(tf._1, tc.l)
            .asInstanceOf[SimpleDateFormat]
            .toPattern()
        )
      }

      for {
        df <- tc.dateFormats
        tf <- tc.timeFormats
      } yield {
        assertEquals(
          s"${df._2} ${tf._2}",
          DateFormat
            .getDateTimeInstance(df._1, tf._1, tc.l)
            .asInstanceOf[SimpleDateFormat]
            .toPattern()
        )
      }
    }
  }

  test("standard_locales_diff") {
    stdLocalesDiff
      .filter(tc =>
        (Platform.executingInJVM && tc.cldr21) || (!Platform.executingInJVM && !tc.cldr21)
      )
      .foreach { tc =>
        for {
          df <- tc.dateFormats
        } yield {
          assertEquals(
            df._2,
            DateFormat
              .getDateInstance(df._1, tc.l)
              .asInstanceOf[SimpleDateFormat]
              .toPattern()
          )
        }

        for {
          tf <- tc.timeFormats
        } yield {
          assertEquals(
            tf._2,
            DateFormat
              .getTimeInstance(tf._1, tc.l)
              .asInstanceOf[SimpleDateFormat]
              .toPattern()
          )
        }

        for {
          df <- tc.dateFormats
          tf <- tc.timeFormats
        } yield {
          assertEquals(
            s"${df._2} ${tf._2}",
            DateFormat
              .getDateTimeInstance(df._1, tf._1, tc.l)
              .asInstanceOf[SimpleDateFormat]
              .toPattern()
          )
        }
      }
  }

  test("extra_locales") {
    localesByTag.foreach { tc =>
      val locale = Locale.forLanguageTag(tc.tag)

      for {
        df <- tc.dateFormats
      } yield {
        assertEquals(
          df._2,
          DateFormat
            .getDateInstance(df._1, locale)
            .asInstanceOf[SimpleDateFormat]
            .toPattern()
        )
      }

      for {
        tf <- tc.timeFormats
      } yield {
        assertEquals(
          tf._2,
          DateFormat
            .getTimeInstance(tf._1, locale)
            .asInstanceOf[SimpleDateFormat]
            .toPattern()
        )
      }

      for {
        df <- tc.dateFormats
        tf <- tc.timeFormats
      } yield {
        assertEquals(
          s"${df._2} ${tf._2}",
          DateFormat
            .getDateTimeInstance(df._1, tf._1, locale)
            .asInstanceOf[SimpleDateFormat]
            .toPattern()
        )
      }
    }
  }

  test("extra_locales_diff") {
    localesByTagDiff
      .filter(tc =>
        (Platform.executingInJVM && tc.cldr21) || (!Platform.executingInJVM && !tc.cldr21)
      )
      .foreach { tc =>
        val locale = Locale.forLanguageTag(tc.tag)

        for {
          df <- tc.dateFormats
        } yield {
          assertEquals(
            df._2,
            DateFormat
              .getDateInstance(df._1, locale)
              .asInstanceOf[SimpleDateFormat]
              .toPattern()
          )
        }

        for {
          tf <- tc.timeFormats
        } yield {
          assertEquals(
            tf._2,
            DateFormat
              .getTimeInstance(tf._1, locale)
              .asInstanceOf[SimpleDateFormat]
              .toPattern()
          )
        }

        for {
          df <- tc.dateFormats
          tf <- tc.timeFormats
        } yield {
          if (!tc.cldr21) {
            assertEquals(
              s"${df._2} ${tf._2}",
              DateFormat
                .getDateTimeInstance(df._1, tf._1, locale)
                .asInstanceOf[SimpleDateFormat]
                .toPattern()
            )
          }
        }
      }
  }

  test("bad_tag_matches_root_dfs") {
    val l = Locale.forLanguageTag("no_NO")
    stdLocalesDiff.foreach {
      case tc @ TestCase(_, lo, cldr21, _, _)
          if lo == Locale.ROOT && ((Platform.executingInJVM && cldr21) || (!Platform.executingInJVM && !cldr21)) =>
        for {
          df <- tc.dateFormats
        } yield {
          assertEquals(
            df._2,
            DateFormat
              .getDateInstance(df._1, l)
              .asInstanceOf[SimpleDateFormat]
              .toPattern()
          )
        }

        for {
          tf <- tc.timeFormats
        } yield {
          assertEquals(
            tf._2,
            DateFormat
              .getTimeInstance(tf._1, l)
              .asInstanceOf[SimpleDateFormat]
              .toPattern()
          )
        }

        for {
          df <- tc.dateFormats
          tf <- tc.timeFormats
        } yield {
          if (!tc.cldr21) {
            assertEquals(
              s"${df._2} ${tf._2}",
              DateFormat
                .getDateTimeInstance(df._1, tf._1, l)
                .asInstanceOf[SimpleDateFormat]
                .toPattern()
            )
          }
        }
      case _ =>
    }
  }

}
