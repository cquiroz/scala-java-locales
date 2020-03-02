package locales.cldr.fallback.data

import locales.cldr._
import locales.cldr.fallback.data.numericsystems.latn

object _en
    extends LDML(Some(_root),
                 LDMLLocale("en", None, None, None),
                 None,
                 List(
                   Symbols(latn,
                           None,
                           Some('.'),
                           Some(','),
                           Some(';'),
                           Some('%'),
                           Some('-'),
                           Some('‰'),
                           Some("∞"),
                           Some("NaN"),
                           Some("E"))
                 ),
                 Some(
                   CalendarSymbols(
                     List("January",
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
                          "December"),
                     List("Jan",
                          "Feb",
                          "Mar",
                          "Apr",
                          "May",
                          "Jun",
                          "Jul",
                          "Aug",
                          "Sep",
                          "Oct",
                          "Nov",
                          "Dec"),
                     List("Sunday",
                          "Monday",
                          "Tuesday",
                          "Wednesday",
                          "Thursday",
                          "Friday",
                          "Saturday"),
                     List("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"),
                     List("AM", "PM"),
                     List("BC", "AD")
                   )
                 ),
                 Some(
                   CalendarPatterns(
                     Map((0, "EEEE, MMMM d, y"), (1, "MMMM d, y"), (2, "MMM d, y"), (3, "M/d/yy")),
                     Map((0, "h:mm:ss a zzzz"), (1, "h:mm:ss a z"), (2, "h:mm:ss a"), (3, "h:mm a"))
                   )
                 ),
                 List(),
                 NumberPatterns(Some("#,##0.###"), Some("#,##0%"), Some("¤#,##0.00;(¤#,##0.00)")))

object _root
    extends LDML(None,
                 LDMLLocale("root", None, None, None),
                 Some(latn),
                 List(
                   Symbols(latn,
                           None,
                           Some('.'),
                           Some(','),
                           Some(';'),
                           Some('%'),
                           Some('-'),
                           Some('‰'),
                           Some("∞"),
                           Some("NaN"),
                           Some("E"))
                 ),
                 Some(
                   CalendarSymbols(
                     List("M01",
                          "M02",
                          "M03",
                          "M04",
                          "M05",
                          "M06",
                          "M07",
                          "M08",
                          "M09",
                          "M10",
                          "M11",
                          "M12"),
                     List(),
                     List("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"),
                     List(),
                     List(),
                     List("BCE", "CE")
                   )
                 ),
                 Some(
                   CalendarPatterns(
                     Map((0, "y MMMM d, EEEE"), (1, "y MMMM d"), (2, "y MMM d"), (3, "y-MM-dd")),
                     Map((0, "HH:mm:ss zzzz"), (1, "HH:mm:ss z"), (2, "HH:mm:ss"), (3, "HH:mm"))
                   )
                 ),
                 List(),
                 NumberPatterns(Some("#,##0.###"), Some("#,##0%"), Some("¤ #,##0.00")))

object _all_ {
  lazy val all: Array[LDML] = // Auto-generated code from CLDR definitions, don't edit
    Array(_en, _root)
}
