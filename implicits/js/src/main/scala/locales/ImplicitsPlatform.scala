package locales

import java.util.Locale

object ImplicitsPlatform {
  final class RichString(val str: String) extends AnyVal {
    def toUpperCase(locale: Locale): String = ???
    def toLowerCase(locale: Locale): String = ???
  }

  // final class RichCharacterObj(val obj: java.lang.Character.type) extends AnyVal {
  //   def toTitleCase(ch: Char): String = ???
  //   def toTitleCase(codePoint: Int): String = ???
    
  //   def toLowerCase(ch: Char): String = ???
  //   def toLowerCase(codePoint: Int): String = ???

  //   def toUpperCase(ch: Char): String = ???
  //   def toUpperCase(codePoint: Int): String = ???
  // }
}

/** JS Platform-specific implementations for Implicit companion object */
trait ImplicitsPlatform {
  import ImplicitsPlatform._
  // implicit def toRichCharacterObj(obj: java.lang.Character.type): RichCharacterObj = new RichCharacterObj(obj)
  implicit def RichString(str: String): RichString = new RichString(str)
}