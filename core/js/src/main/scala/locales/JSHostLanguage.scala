package locales

import scala.scalajs.LinkingInfo
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

/** Reads the language of the JavaScript host (`window.navigator.language`).
  *
  * The lookup is wrapped in `LinkingInfo.linkTimeIf`, so on a WebAssembly target without a
  * JavaScript host (for example a Wasm component) the JS interop code is not linked at all and
  * `Locale.getDefault` keeps linking. On every target with a JavaScript host (JS, and Wasm with the
  * `ESModule` module kind) nothing changes.
  *
  * `linkTimeIf` needs Scala.js >= 1.20 with Scala 2, and Scala >= 3.8.0 with Scala 3 (an older
  * Scala 3 compiler emits a plain call to the `linkTimeIf` stub, which throws at run time). This is
  * why the Scala 3 artifact is built with Scala 3.9.
  */
object JSHostLanguage {

  def window: Window = js.Dynamic.global.window.asInstanceOf[Window]

  /** The language of the JS host, or `None` if there is no JS host or it does not tell. */
  def navigatorLanguage(): Option[String] =
    LinkingInfo.linkTimeIf[Option[String]](
      !LinkingInfo.isWebAssembly ||
        LinkingInfo.moduleKind == LinkingInfo.ModuleKind.ESModule ||
        LinkingInfo.moduleKind == LinkingInfo.ModuleKind.CommonJSModule ||
        LinkingInfo.moduleKind == LinkingInfo.ModuleKind.NoModule
    ) {
      try
        // Attempt to read locale from the platform
        Some(window.navigator.language)
      catch {
        case _: Throwable => None
      }
    } {
      None
    }
}
