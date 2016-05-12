package com.github.cquiroz.scalajs.locale

import java.io.File
import java.nio.charset.Charset
import java.nio.file.{Files, Paths}
import javax.xml.parsers.SAXParserFactory

import scala.collection.JavaConverters._
import scala.scalajs.locale.ldml.{LDML, LDMLLocale}
import scala.xml._

object ScalaLocaleCodeGen extends App {

  def constructClass(f: File, xml: Elem): (File, LDML) = {
    val language = (xml \ "identity" \ "language" \ "@type").text
    val territory = Option((xml \ "identity" \ "territory" \ "@type").text).filter(_.nonEmpty)
    val variant = Option((xml \ "identity" \ "variant" \ "@type").text).filter(_.nonEmpty)
    val script = Option((xml \ "identity" \ "script" \ "@type").text).filter(_.nonEmpty)
    (f, LDML(LDMLLocale(language, territory, variant, script)))
  }

  def treeHugIt(ldmls: List[LDML]):treehugger.forest.Tree = {
    import treehugger.forest._
    import definitions._
    import treehuggerDSL._

    BLOCK (
      IMPORT("scala.scalajs.locale.ldml.LDML"),
      IMPORT("scala.scalajs.locale.ldml.LDMLLocale"),
      PACKAGEOBJECTDEF("locales") := BLOCK(
        ldmls.map(treeHugIt)
      )
    ) inPackage "scala.scalajs.locale.ldml"
  }

  def treeHugIt(ldml: LDML):treehugger.forest.Tree = {
    import treehugger.forest._
    import definitions._
    import treehuggerDSL._

    val ldmlSym = getModule("LDML")
    val ldmlLocaleSym = getModule("LDMLLocale")

    val ldmlLocaleTree = Apply(ldmlLocaleSym, LIT(ldml.locale.language), ldml.locale.territory.fold(NONE)(t => SOME(LIT(t))), ldml.locale.variant.fold(NONE)(v => SOME(LIT(v))), ldml.locale.script.fold(NONE)(s => SOME(LIT(s))))

    VAL(ldml.scalaSafeName, "LDML") := Apply(ldmlSym, ldmlLocaleTree)
  }

  val parser: SAXParser = {
    // Use a non validating parser for speed
    val f = SAXParserFactory.newInstance()
    f.setNamespaceAware(false)
    f.setValidating(false)
    f.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)
    f.newSAXParser()
  }

  val files = Files.newDirectoryStream(Paths.get("jvm/src/main/resources/common/main")).iterator().asScala

  val clazzes = for {
    f <- files.map(_.toFile)
  } yield constructClass(f, XML.withSAXParser(parser).loadFile(f))

  val tree = treeHugIt(clazzes.map(_._2).toList)

  val path = Paths.get("js/src/main/scala/scala/scalajs/locale/ldml/locales.scala")
  path.getParent.toFile.mkdirs()
  Files.write(path, treehugger.forest.treeToString(tree).getBytes(Charset.forName("UTF8")))

}

