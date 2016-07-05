package java.text

import java.util.{Arrays, Calendar, Locale}

import scala.scalajs.locale.LocaleRegistry
import scala.scalajs.locale.cldr.LDML

object DateFormatSymbols {

  def getAvailableLocales(): Array[Locale] = Locale.getAvailableLocales

  def getInstance(): DateFormatSymbols =
    getInstance(Locale.getDefault(Locale.Category.FORMAT))

  def getInstance(locale: Locale): DateFormatSymbols =
    initialize(locale, new DateFormatSymbols(locale))

  private def initialize(locale: Locale,
                         dfs: DateFormatSymbols): DateFormatSymbols = {

    LocaleRegistry.ldml(locale).map(l => dfs).getOrElse(dfs)
    LocaleRegistry
      .ldml(locale)
      .map(l => toDFS(locale, dfs, l))
      .getOrElse(dfs)
  }

  private def copyAndPad(m: List[String], size: Int, v: String): Array[String] = {
    val p = Arrays.copyOf[String](m.toArray[String], size)
    (m.length until size).foreach(p(_) = v)
    p
  }

  private def padAndCopyDays(m: List[String], size: Int, v: String): Array[String] = {
    // Days in the JVM are stored starting on index 1, and 0 is empty
    val v = new Array[String](size)
    (0 until size).foreach(i => if (i == 0 || i > m.length) v(i) = "" else v(i) = m(i - 1))
    v
  }

  private def toDFS(locale: Locale, dfs: DateFormatSymbols, ldml: LDML): DateFormatSymbols = {
    ldml.calendar.foreach { c =>
      // Irritatingly the JVM uses a fixed 13 length array for months and 8 for weekdays
      // http://bugs.java.com/bugdatabase/view_bug.do?bug_id=4146173
      dfs.setMonths(copyAndPad(c.months, 13, ""))
      dfs.setShortMonths(copyAndPad(c.shortMonths, 13, ""))
      dfs.setWeekdays(padAndCopyDays(c.weekdays, 8, ""))
      dfs.setShortWeekdays(padAndCopyDays(c.shortWeekdays, 8, ""))
    }
    dfs
  }
}

class DateFormatSymbols(private[this] val locale: Locale) extends Cloneable {
  private[this] var eras: Array[String] = Array()
  private[this] var months: Array[String] = Array()
  private[this] var shortMonths: Array[String] = Array()
  private[this] var weekdays: Array[String] = Array()
  private[this] var shortWeekdays: Array[String] = Array()
  private[this] var amPmStrings: Array[String] = Array()
  private[this] var zoneStrings: Array[Array[String]] = Array()
  private[this] var localPatternChars: String = ""

  DateFormatSymbols.initialize(locale, this)

  def this() = this(Locale.getDefault(Locale.Category.FORMAT))

  def getEras(): Array[String] = eras

  def setEras(eras: Array[String]): Unit = {
    if (eras == null) throw new NullPointerException()
    this.eras = Array[String](eras: _*)
  }

  def getMonths(): Array[String] = months

  def setMonths(months: Array[String]): Unit = {
    if (months == null) throw new NullPointerException()
    this.months = Array[String](months: _*)
  }

  def getShortMonths(): Array[String] = shortMonths

  def setShortMonths(shortMonths: Array[String]): Unit = {
    if (shortMonths == null) throw new NullPointerException()
    this.shortMonths = Array[String](shortMonths: _*)
  }

  def getWeekdays(): Array[String] = weekdays

  def setWeekdays(weekdays: Array[String]): Unit = {
    if (weekdays == null) throw new NullPointerException()
    this.weekdays = Array[String](weekdays: _*)
  }

  def getShortWeekdays(): Array[String] = shortWeekdays

  def setShortWeekdays(shortWeekdays: Array[String]): Unit = {
    if (shortWeekdays == null) throw new NullPointerException()
    this.shortWeekdays = Array[String](shortWeekdays: _*)
  }

  def getAmPmStrings(): Array[String] = amPmStrings

  def setAmPmStrings(amPmStrings: Array[String]): Unit = {
    if (amPmStrings == null) throw new NullPointerException()
    this.amPmStrings = Array[String](amPmStrings: _*)
  }

  def getZoneStrings(): Array[Array[String]] = zoneStrings

  def setZoneStrings(zoneStrings: Array[Array[String]]): Unit = {
    if (zoneStrings == null) throw new NullPointerException()
    if (zoneStrings.exists(_.length < 5))
      throw new IllegalArgumentException()
    val copy = zoneStrings.map(Array[String](_: _*))
    this.zoneStrings = Array[Array[String]](copy: _*)
  }

  def getLocalPatternChars(): String = localPatternChars

  def setLocalPatternChars(localPatternChars: String): Unit = {
    if (localPatternChars == null) throw new NullPointerException()
    this.localPatternChars = localPatternChars
  }

  override def clone(): AnyRef = {
    val dfs = new DateFormatSymbols()
    dfs.setEras(getEras())
    dfs.setMonths(getMonths())
    dfs.setShortMonths(getShortMonths())
    dfs.setWeekdays(getWeekdays())
    dfs.setShortWeekdays(getShortWeekdays())
    dfs.setAmPmStrings(getAmPmStrings())
    dfs.setZoneStrings(getZoneStrings())
    dfs.setLocalPatternChars(getLocalPatternChars())
    dfs
  }

  override def hashCode(): Int = {
    val state = Seq(Array[AnyRef](eras: _*),
                    Array[AnyRef](months: _*),
                    Array[AnyRef](shortMonths: _*),
                    Array[AnyRef](weekdays: _*),
                    Array[AnyRef](shortWeekdays: _*),
                    Array[AnyRef](amPmStrings: _*))
    val s = state.map(Arrays.hashCode).foldLeft(0)((a, b) => 31 * a + b)
    val zs = zoneStrings.map(Array[AnyRef](_: _*))
    val zsc = Array[AnyRef](zs: _*)

    31 * (31 * s + Arrays.hashCode(zsc)) + localPatternChars.hashCode
  }

  override def equals(other: Any): Boolean = other match {
    case that: DateFormatSymbols =>
      eras.sameElements(that.getEras) &&
        months.sameElements(that.getMonths) &&
        shortMonths.sameElements(that.getShortMonths) &&
        weekdays.sameElements(that.getWeekdays) &&
        shortWeekdays.sameElements(that.getShortWeekdays) &&
        amPmStrings.sameElements(that.getAmPmStrings) &&
        zoneStrings.sameElements(that.getZoneStrings) &&
        localPatternChars == that.getLocalPatternChars
    case _ => false
  }
}
