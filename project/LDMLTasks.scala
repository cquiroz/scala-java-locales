import sbt._
import Keys._

import locales.ScalaLocaleCodeGen

object LDMLTasks {
  val cldrVersion = settingKey[String]("The version of CLDR used.")

  def generateLocaleData(base: File, data: File): Seq[File] = {
    ScalaLocaleCodeGen.generateDataSourceCode(base, data)
  }
}
