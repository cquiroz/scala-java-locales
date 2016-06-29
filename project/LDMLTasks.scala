import sbt._
import Keys._

import scalajs.locale.ScalaLocaleCodeGen

object LDMLTasks {
  val cldrVersion = settingKey[String]("The version of CLDR used.")

  def generateLocaleData(base: File, data: File): Seq[File] = {
    ScalaLocaleCodeGen.generateDataSourceCode(base, data)
  }
}
