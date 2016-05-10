package com.github.cquiroz.scalajs.locale

import java.nio.file.{Files, Path, Paths}

import scala.collection.JavaConverters._

object ScalaLocaleCodeGen extends App {
  val files = Files.newDirectoryStream(Paths.get("jvm/src/main/resources/common/main")).iterator().asScala
  files.foreach(println)
}
