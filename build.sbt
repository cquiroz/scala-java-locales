enablePlugins(ScalaJSPlugin)

name := "ScalaJsLocaleGen"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.8"

persistLauncher in Compile := true

persistLauncher in Test := false

val cldrVersion = settingKey[String]("The version of CLDR used.")
lazy val downloadFromZip = taskKey[Unit]("Download the sbt zip and extract it to ./temp")

cldrVersion := "29"

downloadFromZip := {
    IO.unzipURL(new URL(s"http://unicode.org/Public/cldr/${cldrVersion.value}/core.zip"), (resourceDirectory in Compile).value)
}
