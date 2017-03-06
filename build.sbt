import org.scalajs.sbtplugin.cross.CrossProject
import sbt.Keys._
import LDMLTasks._

val cldrVersion = settingKey[String]("The version of CLDR used.")
lazy val downloadFromZip: TaskKey[Unit] =
  taskKey[Unit]("Download the sbt zip and extract it")

val commonSettings: Seq[Setting[_]] = Seq(
  cldrVersion        := "30",
  version            := s"0.5.1-cldr${cldrVersion.value}-SNAPSHOT",
  organization       := "io.github.cquiroz",
  scalaVersion       := "2.11.8",
  crossScalaVersions := Seq("2.10.4", "2.11.8", "2.12.1"),
  scalacOptions      ++= Seq("-deprecation", "-feature", "-Xfatal-warnings"),
  javaOptions        ++= Seq("-Dfile.encoding=UTF8"),
  mappings in (Compile, packageBin) ~= {
    // Exclude CLDR files...
    _.filter(!_._2.contains("core"))
  },
  exportJars              := true,
  publishMavenStyle       := true,
  publishArtifact in Test := false,
  publishTo               := {
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
    <contributors>
      <contributor>
        <name>Eric Peters</name>
        <url>https://github.com/er1c</url>
      </contributor>
      <contributor>
        <name>A. Alonso Dominguez</name>
        <url>https://github.com/alonsodomin</url>
      </contributor>
      <contributor>
        <name>Marius B. Kotsbak</name>
        <url>https://github.com/mkotsbak</url>
      </contributor>
      <contributor>
        <name>Timothy Klim</name>
        <url>https://github.com/TimothyKlim</url>
      </contributor>
    </contributors>
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

lazy val core: CrossProject = crossProject.crossType(CrossType.Pure).
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
        if(isSnapshot.value) sys.process.Process("git rev-parse HEAD").lines_!.head
        else version.value
      (sourceDirectories in Compile).value.map { dir =>
        val a = dir.toURI.toString
        val g = "https://raw.githubusercontent.com/cquiroz/scala-java-locales/" + tagOrHash + "/core/src/main/scala"
        s"-P:scalajs:mapSourceURI:$a->$g/"
      }
    }
  )

lazy val coreJVM: Project = core.jvm

lazy val testSuite: CrossProject = CrossProject(
  jvmId = "testSuiteJVM",
  jsId = "testSuite",
  base = file("testSuite"),
  crossType = CrossType.Full).
  jsConfigure(_.enablePlugins(ScalaJSJUnitPlugin)).
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
    name := "scala-java-locales testSuite on JS"
  ).
  jsConfigure(_.dependsOn(coreJS)).
  jvmSettings(
    // Fork the JVM test to ensure that the custom flags are set
    fork in Test := true,
    // Use CLDR provider for locales
    // https://docs.oracle.com/javase/8/docs/technotes/guides/intl/enhancements.8.html#cldr
    javaOptions in Test ++= Seq("-Duser.language=en", "-Duser.country=", "-Djava.locale.providers=CLDR", "-Dfile.encoding=UTF8"),
    name := "scala-java-locales testSuite on JVM",
    libraryDependencies +=
      "com.novocode" % "junit-interface" % "0.9" % "test"
  ).
  jvmConfigure(_.dependsOn(coreJVM))

lazy val testSuiteJS: Project = testSuite.js
lazy val testSuiteJVM: Project = testSuite.jvm
