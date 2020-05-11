package locales

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

object DefaultLocale {
  lazy val window: Window = js.Dynamic.global.window.asInstanceOf[Window]

  def platformLocale: Locale = {
    val lang =
      try
      // Attempt to read locale from the platform
      Some(window.navigator.language)
      catch {
        case _: Throwable => None
      }
    val l    = lang.filter(LocalesDb.ldmls.contains).getOrElse("en")
    LocalesDb.localeForLanguageTag(l).getOrElse(LocalesDb.root.toLocale)
  }

}
