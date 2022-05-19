package demo

import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.Locale

object DemoApp {
  def main(args: Array[String]): Unit = {
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
