package locales

import java.util.Locale
import scala.scalajs.js

class JSHostLanguageTest extends munit.FunSuite {

  test("no window on Node: no host language, default locale is en") {
    assert(js.isUndefined(js.Dynamic.global.globalThis.window))
    assertEquals(JSHostLanguage.navigatorLanguage(), None)
    assertEquals(DefaultLocale.platformLocale.toLanguageTag, "en")
  }

  test("window.navigator.language is read on a JS host") {
    // Fails if the JS branch of `linkTimeIf` was not selected, or if the compiler emitted a call to
    // the `linkTimeIf` stub (Scala 3 < 3.8.0) instead of a link-time condition.
    js.Dynamic.global.globalThis.window =
      js.Dynamic.literal(navigator = js.Dynamic.literal(language = "fr"))
    try {
      assertEquals(JSHostLanguage.navigatorLanguage(), Some("fr"))
      assertEquals(DefaultLocale.platformLocale, Locale.FRENCH)
    } finally js.special.delete(js.Dynamic.global.globalThis, "window")
  }
}
