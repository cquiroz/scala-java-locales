import sbt.Keys._
import LDMLTasks._

val cldrVersion = settingKey[String]("The version of CLDR used.")
lazy val downloadFromZip: TaskKey[Unit] =
  taskKey[Unit]("Download the sbt zip and extract it")

val commonSettings: Seq[Setting[_]] = Seq(
  cldrVersion := "31",
  version := s"0.3.6-cldr${cldrVersion.value}",
  organization := "io.github.cquiroz",
  scalaVersion := "2.11.11",
  crossScalaVersions := Seq("2.10.6", "2.11.11", "2.12.2"),
    scalacOptions ++= Seq("-deprecation", "-feature"),
  scalacOptions := {
    CrossVersion.partialVersion(scalaVersion.value) match {
      case Some((2, scalaMajor)) if scalaMajor >= 11 =>
        scalacOptions.value ++ Seq("-deprecation:false", "-Xfatal-warnings")
      case Some((2, 10)) =>
        scalacOptions.value
    }
  },
  mappings in (Compile, packageBin) ~= {
    // Exclude CLDR files...
    _.filter(!_._2.contains("core"))
  },
  useGpg := true,
  exportJars := true,
  publishMavenStyle := true,
  publishArtifact in Test := false,
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases" at nexus + "service/local/staging/deploy/maven2")
  },
  pomExtra :=
    <url>https://github.com/cquiroz/scala-java-locales</url>
    <licenses>
      <license>
        <name>BSD-style</name>
        <url>http://www.opensource.org/licenses/bsd-license.php</url>
        <distribution>repo</distribution>
      </license>
    </licenses>
    <scm>
      <url>git@github.com:cquiroz/scala-java-locales.git</url>
      <connection>scm:git:git@github.com:cquiroz/scala-java-locales.git</connection>
    </scm>
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

lazy val scalajs_locales: Project = project.in(file("."))
  .settings(commonSettings: _*)
  .settings(
    name := "scala-java-locales",
    publish := {},
    publishLocal := {}
  )
  .aggregate(coreJS, coreJVM, testSuiteJS, testSuiteJVM)

lazy val core = crossProject.
  crossType(CrossType.Pure).
  settings(commonSettings: _*).
  settings(
    name := "scala-java-locales",
    downloadFromZip := {
      val xmlFiles = (resourceDirectory in Compile).value / "core"
      if (java.nio.file.Files.notExists(xmlFiles.toPath)) {
        println(s"CLDR files missing, downloading version ${cldrVersion.value} ...")
        IO.unzipURL(
          new URL(s"http://unicode.org/Public/cldr/${cldrVersion.value}/core.zip"),
          xmlFiles)
      } else {
        println("CLDR files already available")
      }
    },
    compile in Compile := (compile in Compile).dependsOn(downloadFromZip).value,
    sourceGenerators in Compile += Def.task {
      generateLocaleData((sourceManaged in Compile).value,
        (resourceDirectory in Compile).value / "core")
    }.taskValue
  ).
  jsConfigure(_.enablePlugins(ScalaJSJUnitPlugin))

lazy val coreJS: Project = core.js
  .settings(
    scalacOptions ++= {
      val tagOrHash =
        if (isSnapshot.value) sys.process.Process("git rev-parse HEAD").lines_!.head
        else s"v${version.value}"
      (sourceDirectories in Compile).value.map { dir =>
        val a = dir.toURI.toString
        val g = "https://raw.githubusercontent.com/cquiroz/scala-java-locales/" + tagOrHash + "/core/src/main/scala"
        s"-P:scalajs:mapSourceURI:$a->$g/"
      }
    }
  )

lazy val coreJVM: Project = core.jvm

lazy val testSuite = crossProject.
  settings(commonSettings: _*).
  settings(
    publish := {},
    publishLocal := {},
    publishArtifact := false,
    testOptions +=
      Tests.Argument(TestFramework("com.novocode.junit.JUnitFramework"),
        "-v", "-a")
  ).
  jsSettings(
    parallelExecution in Test := false,
    name := "scala-java-locales testSuite on JS",
    libraryDependencies ++= Seq(
      "com.novocode" % "junit-interface" % "0.9" % "test",
      "io.github.cquiroz" %% "macroutils" % "0.0.1" % "provided"
    )
  ).
  jsConfigure(_.dependsOn(coreJS, macroUtils)).
  jvmSettings(
    // Fork the JVM test to ensure that the custom flags are set
    fork in Test := true,
    // Use CLDR provider for locales
    // https://docs.oracle.com/javase/8/docs/technotes/guides/intl/enhancements.8.html#cldr
    javaOptions in Test ++= Seq("-Duser.language=en", "-Duser.country=", "-Djava.locale.providers=CLDR"),
    name := "scala-java-locales testSuite on JVM",
    libraryDependencies ++= Seq(
      "com.novocode" % "junit-interface" % "0.9" % "test",
      "io.github.cquiroz" %% "macroutils" % "0.0.1" % "provided"
    )
  ).
  jvmConfigure(_.dependsOn(coreJVM, macroUtils))

lazy val macroUtils = project.in(file("macroUtils")).
  settings(commonSettings).
  settings(
    name := "macroutils",
    organization := "io.github.cquiroz",
    version := "0.0.1",
    libraryDependencies := {
      Seq("org.scala-lang" % "scala-reflect" % scalaVersion.value) ++ {
        CrossVersion.partialVersion(scalaVersion.value) match {
          // if Scala 2.11+ is used, quasiquotes are available in the standard distribution
          case Some((2, scalaMajor)) if scalaMajor >= 11 =>
            libraryDependencies.value
          // in Scala 2.10, quasiquotes are provided by macro paradise
          case Some((2, 10)) =>
            libraryDependencies.value ++ Seq(
              compilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full),
              "org.scalamacros" %% "quasiquotes" % "2.1.0" cross CrossVersion.binary)
        }
      }
    }
  )

lazy val testSuiteJS: Project = testSuite.js
lazy val testSuiteJVM: Project = testSuite.jvm
