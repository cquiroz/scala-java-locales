import org.scalajs.sbtplugin.cross.CrossProject
import sbt.Keys._

val commonSettings: Seq[Setting[_]] = Seq(
  version := "0.1.0-SNAPSHOT",
  organization := "com.github.cquiroz.scala-js",
  scalaVersion := "2.11.8",
  crossScalaVersions := Seq("2.10.4", "2.11.8"),
  scalacOptions ++= Seq("-deprecation", "-feature", "-Xfatal-warnings"),
  /*mappings in (Compile, packageBin) ~= {
    _.filter(!_._2.endsWith(".class"))
  },*/
  exportJars := true,

  publishMavenStyle := true,
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases" at nexus + "service/local/staging/deploy/maven2")
  },
  pomExtra :=
      <developers>
        <developer>
          <id>cquiroz</id>
          <name>Carlos Quiroz</name>
          <url>https://github.com/cquiroz/</url>
        </developer>
      </developers>
  ,
  pomIncludeRepository := { _ => false }
)

lazy val root: Project = project.in(file("."))
  .settings(commonSettings)
  .settings(
      publish := {},
      publishLocal := {}
  )
  .aggregate(coreJS, coreJVM, testSuiteJS, testSuiteJVM)

lazy val core = crossProject.crossType(CrossType.Pure).
  settings(commonSettings: _*).
  settings(
    name := "Scala.js java locale"
  ).
  jsConfigure(_.enablePlugins(ScalaJSJUnitPlugin))

lazy val coreJS = core.js
lazy val coreJVM = core.jvm

lazy val testSuite = CrossProject(
  jvmId = "testSuiteJVM",
  jsId = "testSuite",
  base = file("testSuite"),
  crossType = CrossType.Full).
  jsConfigure(_.enablePlugins(ScalaJSJUnitPlugin).dependsOn(coreJS).dependsOn(coreJVM)).
  settings(commonSettings: _*).
  settings(
    testOptions +=
      Tests.Argument(TestFramework("com.novocode.junit.JUnitFramework"), "-v", "-a")
  ).
  jsSettings(
    name := "java locale testSuite on JS"
  ).
  jsConfigure(_.dependsOn(coreJS % "compile->compile;test->test")).
  jvmConfigure(_.dependsOn(coreJVM)).
  jvmSettings(
    name := "java locale testSuite on JVM",
    libraryDependencies +=
      "com.novocode" % "junit-interface" % "0.9" % "test"
  ).dependsOn(core)

lazy val testSuiteJS = testSuite.js
  .dependsOn(coreJS)

lazy val testSuiteJVM = testSuite.jvm

val cldrVersion = settingKey[String]("The version of CLDR used.")
lazy val downloadFromZip = taskKey[Unit]("Download the sbt zip and extract it to ./temp")

lazy val localegen = CrossProject(
  jvmId = "locale-generator-JVM",
  jsId = "locales",
  base = file("locale-generator"),
  crossType = CrossType.Full).
  settings(commonSettings: _*).
  jvmSettings(
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
      "com.eed3si9n" %% "treehugger" % "0.4.1",
      "com.geirsson" %% "scalafmt" % "0.2.5"
    ),
    cldrVersion := "29",
    downloadFromZip := {
      if(java.nio.file.Files.notExists((resourceDirectory in Compile).value.toPath)) {
        println("CLDR files missing, downloading...")
        IO.unzipURL(new URL(s"http://unicode.org/Public/cldr/${cldrVersion.value}/core.zip"), (resourceDirectory in Compile).value)
      } else {
        println("CLDR files already available")
      }
    },
    compile in Compile <<= (compile in Compile).dependsOn(downloadFromZip)
  ).
  jsSettings(
    // Never add dependencies
  ).
  jsConfigure(_.dependsOn(root))

lazy val localegenJVM = localegen.jvm
lazy val localegenJS = localegen.js
