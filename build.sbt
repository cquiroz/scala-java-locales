enablePlugins(ScalaJSPlugin)

name := "scalajs-locale-generator"

organization := "com.github.cquiroz.locale-gen"

version := "0.1-SNAPSHOT"

persistLauncher in Compile := true

persistLauncher in Test := false

val cldrVersion = settingKey[String]("The version of CLDR used.")
lazy val downloadFromZip = taskKey[Unit]("Download the sbt zip and extract it to ./temp")

cldrVersion := "29"

downloadFromZip := {
  if(java.nio.file.Files.notExists((resourceDirectory in Compile).value.toPath)) {
    println("CLDR files missing, downloading...")
    IO.unzipURL(new URL(s"http://unicode.org/Public/cldr/${cldrVersion.value}/core.zip"), (resourceDirectory in Compile).value)
  } else {
    println("CLDR files already available")
  }
}

lazy val root = project.in(file(".")).
  aggregate(localegenJS, localegenJVM).
  settings(
    publish := {},
    publishLocal := {}
  )

lazy val localegen = crossProject.in(file(".")).
  settings(
    scalaVersion := "2.11.8"
  )
  .jvmSettings(
    libraryDependencies := {
      CrossVersion.partialVersion(scalaVersion.value) match {
        // if scala 2.11+ is used, add dependency on scala-xml module
        case Some((2, scalaMajor)) if scalaMajor >= 11 =>
          libraryDependencies.value ++ Seq(
            "org.scala-lang.modules" %% "scala-xml" % "1.0.5")
        case _ =>
          libraryDependencies.value
      }
    },
    libraryDependencies ++= Seq(
      "com.lihaoyi" %% "pprint" % "0.4.0",
      "com.eed3si9n" %% "treehugger" % "0.4.1"
    )
  )
  .jsSettings(
    // Never add dependencies
  )

lazy val localegenJVM = localegen.jvm
lazy val localegenJS = localegen.js

compile in Compile <<= (compile in Compile).dependsOn(downloadFromZip)