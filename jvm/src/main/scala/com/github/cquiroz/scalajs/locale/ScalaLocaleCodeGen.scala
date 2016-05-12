package com.github.cquiroz.scalajs.locale

import java.io.File
import java.nio.file.{Files, Paths}
import javax.xml.parsers.SAXParserFactory

import scala.collection.JavaConverters._
import scala.scalajs.locale.{LDML, LDMLLocale}
import scala.xml._

object ScalaLocaleCodeGen extends App {

  def constructClass(f: File, xml: Elem): (File, LDML) = {
    val language = (xml \ "identity" \ "language" \ "@type").text
    val territory = Option((xml \ "identity" \ "territory" \ "@type").text).filter(_.nonEmpty)
    val variant = Option((xml \ "identity" \ "variant" \ "@type").text).filter(_.nonEmpty)
    (f, LDML(LDMLLocale(language, territory, variant)))
  }

  def treeHugIt(ldml: LDML) = {
    import treehugger.forest._
    import definitions._
    import treehuggerDSL._

    val ldmlSym = getModule("LDML")
    val ldmlLocaleSym = getModule("LDMLocale")

    val ldmlLocaleTree = Apply(ldmlLocaleSym, LIT(ldml.locale.language), ldml.locale.territory.fold(NONE)(t => SOME(LIT(t))), ldml.locale.variant.fold(NONE)(v => SOME(LIT(v))))

    val tree = VAL("foo", "LDML") := Apply(ldmlSym, ldmlLocaleTree)
    treeToString(tree)
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

  clazzes.foreach {
    case (f, l) =>
      print(f.getName + " -> ")
      pprint.pprintln(l)
      pprint.pprintln(treeHugIt(l))
  }

}

