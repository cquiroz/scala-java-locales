package demo

import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.Locale
import scala.scalajs.js
import scala.scalajs.js.annotation._

@js.native
@JSGlobal
class Navigator extends js.Any {
  def language: String = js.native
}

@js.native
@JSGlobal
class Window extends js.Any {
  def navigator: Navigator = js.native
}

object DemoApp {
  lazy val window: Window = js.Dynamic.global.window.asInstanceOf[Window]

  def main(args: Array[String]): Unit = {
    try {
      println(window.navigator.language)
    } catch {
      case _: Throwable => println("Ignore")
    }
    println(Locale.getDefault.toLanguageTag)
    // Minimal demo used for sanity checks and for size improvements
    val fi_FI = Locale.forLanguageTag("fi-FI")
    val dfs   = DecimalFormatSymbols.getInstance(fi_FI)
    println(dfs)
    Locale.setDefault(fi_FI)
    println(NumberFormat.getNumberInstance().getMaximumFractionDigits())
    println(fi_FI.toLanguageTag())
  }
}
