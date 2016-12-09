package locales

import java.io.{File, FileInputStream, InputStreamReader}
import java.nio.charset.Charset
import java.nio.file.Files
import java.util.function.IntPredicate
import javax.xml.parsers.SAXParserFactory
import scala.collection.JavaConverters._
import scala.collection.breakOut
import scala.xml.{XML, _}

object ScalaLocaleCodeGen {
  def writeGeneratedTree(base: File, file: String, tree: treehugger.forest.Tree):File = {
    val dataPath = base.toPath.resolve("locales").resolve("cldr").resolve("data")
    val path = dataPath.resolve(s"$file.scala")

    path.getParent.toFile.mkdirs()
    println(s"Write to $path")

    Files.write(path, treehugger.forest.treeToString(tree)
      .getBytes(Charset.forName("UTF8")))
    path.toFile
  }

  val unicodeIgnorable = new IntPredicate {
    override def test(value: Int): Boolean = !Character.isIdentifierIgnorable(value)
  }

  def readCalendarData(xml: Node): Option[CalendarSymbols] = {
    def readEntries(mc: Node, itemParent: String, entryName: String, width: String): Seq[String] =
      for {
        w <- mc \ itemParent
        if (w \ "@type").text == width
        m <- w \\ entryName
      } yield m.text

    // read the months context
    val months = (for {
      mc <- xml \\ "monthContext"
      if (mc \ "@type").text == "format"
    } yield {
      val wideMonths = readEntries(mc, "monthWidth", "month", "wide")
      val shortMonths = readEntries(mc, "monthWidth", "month", "abbreviated")
      MonthSymbols(wideMonths, shortMonths)
    }).headOption

    // read the weekdays context
    val weekdays = (for {
      mc <- xml \\ "dayContext"
      if (mc \ "@type").text == "format"
    } yield {
      val weekdays = readEntries(mc, "dayWidth", "day", "wide")
      val shortWeekdays = readEntries(mc, "dayWidth", "day", "abbreviated")
      WeekdaysSymbols(weekdays, shortWeekdays)
    }).headOption

    def readPeriod(n: Node, name: String): Option[String] =
      (for {
        p <- n \ "dayPeriod"
        if (p \ "@type").text == name && (p \ "@alt").text != "variant"
      } yield p.text).headOption

    // read the day periods
    val amPm = (for {
      dpc <- xml \\ "dayPeriods" \ "dayPeriodContext"
      if (dpc \ "@type").text == "format"
      dpw <- dpc \\ "dayPeriodWidth"
      if (dpw \ "@type").text == "wide"
    } yield {
      val am = readPeriod(dpw, "am")
      val pm = readPeriod(dpw, "pm")
      // This is valid because by the spec am and pm must appear together
      // http://www.unicode.org/reports/tr35/tr35-dates.html#Day_Period_Rules
      AmPmSymbols(List(am, pm).flatten)
    }).headOption

    def readEras(n: Node, idx: String): Option[String] =  {
      (for {
        p <- n \ "eraAbbr" \\ "era"
        if (p \ "@type").text == idx && (p \ "@alt").text != "variant"
      } yield p.text).headOption
    }

    val eras = (for {
      n <- xml \ "eras"
    } yield {
      val bc = readEras(n, "0")
      val ad = readEras(n, "1")
      EraSymbols(List(bc, ad).flatten)
    }).headOption

    if (List(months, weekdays, amPm, eras).exists(_.isDefined)) {
      Some(CalendarSymbols(months.getOrElse(MonthSymbols.zero),
        weekdays.getOrElse(WeekdaysSymbols.zero), amPm.getOrElse(AmPmSymbols.zero),
        eras.getOrElse(EraSymbols.zero)))
    } else {
      None
    }
  }

  def readCalendarPatterns(xml: Node): Option[CalendarPatterns] = {
    def readPatterns(n: Node, sub: String, formatType: String): Seq[DateTimePattern] =
      for {
        ft <- n \ formatType
        p  <- ft \ sub \ "pattern"
        if (p \ "@alt").text != "variant"
      } yield DateTimePattern((ft \ "@type").text, p.text)

    val datePatterns = (for {
      df <- xml \\ "dateFormats"
    } yield {
      readPatterns(df, "dateFormat", "dateFormatLength")
    }).headOption.map(_.toList)

    val timePatterns = (for {
      df <- xml \\ "timeFormats"
    } yield {
      readPatterns(df, "timeFormat", "timeFormatLength")
    }).headOption.map(_.toList)

    Some(CalendarPatterns(datePatterns.getOrElse(Nil), timePatterns.getOrElse(Nil)))
  }

  // Pass in currency types into this, so we can augment the CurrencyData to include the master code list
  def parseCurrencyData(xml: Node, currencyTypes: Seq[CurrencyType]): CurrencyData = {
    def toOptionInt(node: NodeSeq): Option[Int] =
      if (node.nonEmpty && node.text.trim.nonEmpty) Some(node.text.trim.toInt) else None

    // TODO: After we have better simpledateformat/parsing support, parse it to a Date rather than keeping it as a string
    def toOptionDate(node: NodeSeq): Option[String] = {
      if (node.nonEmpty && node.text.trim.nonEmpty) Some(node.text.trim) /*Some(sdf.parse(node.text.trim))*/ else None
    }

    def toOptionBoolean(node: NodeSeq): Option[Boolean] =
      if (node.nonEmpty && node.text.trim.nonEmpty) Some(node.text.trim.toBoolean) else None

    // Lookup the currencyCode => numericCode mappings first so we can augment the fractions info
    val numericCodes = for {
        codes <- xml \ "codeMappings" \\ "currencyCodes"
      } yield {
        CurrencyNumericCode((codes \ "@type").text.toUpperCase, (codes \ "@numeric").text.toInt)
      }

    val fractions = for {
      info <- xml \ "currencyData" \ "fractions" \\ "info"
    } yield {
      val currencyCode = (info \ "@iso4217").text.toUpperCase
      val digits = (info \ "@digits").text.toInt
      val rounding = (info \ "@rounding").text.toInt
      val cashDigits = toOptionInt(info \ "@cashDigits")
      val cashRounding = toOptionInt(info \ "@cashRounding")
      CurrencyDataFractionsInfo(
        currencyCode,
        digits,
        rounding,
        cashDigits,
        cashRounding
      )
    }

    val regions = (for {
      region <- xml \ "currencyData" \\ "region"
      currency <- region \\ "currency"
    } yield {
      val countryCode = (region \ "@iso3166").text

      val currencyCode = (currency \ "@iso4217").text
      val from = toOptionDate(currency \ "@from")
      val to = toOptionDate(currency \ "@to")
      val tender = toOptionBoolean(currency \ "@tender")

      countryCode -> CurrencyDataRegionCurrency(currencyCode, from, to, tender)
    }).groupBy{ _._1 }.map{ case (countryCode: String, currencies: Seq[(String, CurrencyDataRegionCurrency)]) =>
      CurrencyDataRegion(countryCode, currencies.map{ _._2 })
    }.toSeq

    CurrencyData(currencyTypes = currencyTypes, fractions = fractions, regions = regions, numericCodes = numericCodes)
  }

  def parseCurrencyTypes(xml: Node): Seq[CurrencyType] =
    for {
      keys         <- xml \ "keyword" \ """key"""
      currencyKeys <- keys.filter { n => (n \ "@name").text == "cu" }
      currencyType <- currencyKeys \\ "type"
    } yield {
      CurrencyType((currencyType \ "@name").text.toUpperCase, (currencyType \ "@description").text)
    }

  /**
   * Parse the xml into an XMLLDML object
   */
  def constructLDMLDescriptor(f: File, xml: Elem, latn: NumericSystem,
                              ns: Map[String, NumericSystem]): XMLLDML = {
    // Parse locale components
    val language = (xml \ "identity" \ "language" \ "@type").text
    val territory = Option((xml \ "identity" \ "territory" \ "@type").text)
      .filter(_.nonEmpty)
    val variant = Option((xml \ "identity" \ "variant" \ "@type").text)
      .filter(_.nonEmpty)
    val script = Option((xml \ "identity" \ "script" \ "@type").text)
      .filter(_.nonEmpty)

    val gregorian = for {
      n <- xml \ "dates" \\ "calendar"
      if (n \ "@type").text == "gregorian"
      if n.text.nonEmpty
    } yield readCalendarData(n)

    val gregorianDatePatterns = for {
      n <- xml \ "dates" \\ "calendar"
      if (n \ "@type").text == "gregorian"
      if n.text.nonEmpty
    } yield readCalendarPatterns(n)

    val decimalPatterns = (for {
        n <- xml \ "numbers" \\ "decimalFormats"
        if (n \ "@numberSystem").text == "latn"
        d <- n \ "decimalFormatLength"
        if (d \ "@type").text.isEmpty
      } yield d \ "decimalFormat" \ "pattern").headOption.map(_.text)

    val percentagePatterns = (for {
        n <- xml \ "numbers" \\ "percentFormats"
        if (n \ "@numberSystem").text == "latn"
        d <- n \ "percentFormatLength"
        if (d \ "@type").text.isEmpty
      } yield d \ "percentFormat" \ "pattern").headOption.map(_.text)

    val currencyPatterns = (for {
      n <- xml \ "numbers" \\ "currencyFormats"
      if (n \ "@numberSystem").text == "latn"
      d <- n \ "currencyFormatLength"
      if ((d \ "@type").text.isEmpty || (d \ "@type").text == "standard")
    } yield d \ "currencyFormat" \ "pattern").headOption.map(_.text)

    // Find out the default numeric system
    val defaultNS = Option((xml \ "numbers" \ "defaultNumberingSystem").text)
      .filter(_.nonEmpty).filter(ns.contains)
    def optionalString(n: NodeSeq): Option[String] = if (n.isEmpty) None else Some(n.text)

    def symbolC(n: NodeSeq): Option[Char] = if (n.isEmpty) None else {
      // Filter out the ignorable code points, e.g. RTL and LTR marks
      val charInt = n.text.codePoints().filter(unicodeIgnorable).findFirst().orElse(0)
      Some(charInt.toChar)
    }

    val symbols = (xml \ "numbers" \\ "symbols").flatMap { s =>
      // http://www.unicode.org/reports/tr35/tr35-numbers.html#Numbering_Systems
      // By default, number symbols without a specific numberSystem attribute
      // are assumed to be used for the "latn" numbering system, which uses
      // western (ASCII) digits
      val nsAttr = Option((s \ "@numberSystem").text).filter(_.nonEmpty)
      val sns = nsAttr.flatMap(ns.get).getOrElse(latn)
      // TODO process aliases
      val nsSymbols = s.collect {
        case s @ <symbols>{_*}</symbols> if (s \ "alias").isEmpty =>
          // elements may not be present and they could be the empty string
          val decimal = symbolC(s \ "decimal")
          val group = symbolC(s \ "group")
          val list = symbolC(s \ "list")
          val percentSymbol = symbolC(s \ "percentSign")
          val plusSign = symbolC(s \ "plusSign")
          val minusSign = symbolC(s \ "minusSign")
          val perMilleSign = symbolC(s \ "perMille")
          val infiniteSign = optionalString(s \ "infinity")
          val nan = optionalString(s \ "nan")
          val exp = optionalString(s \ "exponential")
          val sym = NumberSymbols(sns, None, decimal, group, list,
            percentSymbol, plusSign, minusSign, perMilleSign, infiniteSign, nan, exp)
          sns -> sym

        case s @ <symbols>{_*}</symbols> =>
          // We take advantage that all aliases on CLDR are to latn
          sns -> NumberSymbols.alias(sns, latn)
      }
      nsSymbols
    }

    val currencies = (xml \ "numbers" \ "currencies" \\ "currency").map { c =>
      val currencyCode = (c \ "@type").text
      val symbols = (c \\ "symbol").map { s =>
        CurrencySymbol(s.text, optionalString(s \ "@alt"))
      }
      val displayNames = (c \\ "displayName").map { n =>
        CurrencyDisplayName(n.text, optionalString(n \ "@count"))
      }

      NumberCurrency(currencyCode, symbols, displayNames)
    }

    val fileName = f.getName.substring(0, f.getName.lastIndexOf("."))

    XMLLDML(XMLLDMLLocale(language, territory, variant, script), fileName,
      defaultNS.flatMap(ns.get), symbols.toMap, gregorian.flatten.headOption,
      gregorianDatePatterns.flatten.headOption, currencies,
      NumberPatterns(decimalPatterns, percentagePatterns, currencyPatterns))
  }

  // Note this must be a def or there could be issues with concurrency
  def parser: SAXParser = {
    val f = SAXParserFactory.newInstance()
    // Don't validate to speed up generation
    f.setNamespaceAware(false)
    f.setValidating(false)
    f.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)
    f.newSAXParser()
  }

  def parseNumberingSystems(xml: Elem): Seq[NumericSystem] = {
    val ns = xml \ "numberingSystems" \\ "numberingSystem"

    for {
      n <- ns
      if (n \ "@type").text == "numeric" // ignore algorithmic ns
    } yield {
      val id = (n \ "@id").text
      val digits = (n \ "@digits").text
      NumericSystem(id, digits)
    }
  }

  def readNumericSystems(data: File): Seq[NumericSystem] = {
    // Parse the numeric systems
    val numberingSystemsFile = data.toPath.resolve("common")
      .resolve("supplemental").resolve("numberingSystems.xml").toFile
    parseNumberingSystems(XML.withSAXParser(parser).loadFile(numberingSystemsFile))
  }

  def generateNumericSystemsFile(base: File, numericSystems: Seq[NumericSystem]): File = {
    // Generate numeric systems source code
    writeGeneratedTree(base, "numericsystems",
      CodeGenerator.numericSystems(numericSystems))
  }

  def parseCalendars(xml: Elem): Seq[Calendar] = {
    val ns = xml \ "calendarData" \\ "calendar"

    for {
      n <- ns
    } yield {
      val id = (n \ "@type").text
      Calendar(id)
    }
  }

  def readCalendars(data: File): Seq[Calendar] = {
    // Parse the numeric systems
    val calendarsSupplementalData = data.toPath.resolve("common")
      .resolve("supplemental").resolve("supplementalData.xml").toFile
    parseCalendars(XML.withSAXParser(parser).loadFile(calendarsSupplementalData))
  }

  // Let's augment the "CurrencyData" in supplemental data with the master bcp47 type list
  def readCurrencyData(data: File): CurrencyData = {
    val currencySupplementalData = data.toPath.resolve("common")
      .resolve("supplemental").resolve("supplementalData.xml").toFile
    parseCurrencyData(XML.withSAXParser(parser).loadFile(currencySupplementalData), readCurrencyTypes(data))
  }

  def readCurrencyTypes(data: File): Seq[CurrencyType] = {
    val currencyTypesData = data.toPath.resolve("common")
      .resolve("bcp47").resolve("currency.xml").toFile
    parseCurrencyTypes(XML.withSAXParser(parser).loadFile(currencyTypesData))
  }

  def parseParentLocales(xml: Elem): Map[String, List[String]] = {
    val ns = xml \ "parentLocales" \\ "parentLocale"

    val p = for {
      n <- ns
    } yield {
      val parent = (n \ "@parent").text
      val locales = (n \ "@locales").text
      parent -> locales.split("\\s").toList
    }
    p.toMap
  }

  def readParentLocales(data: File): Map[String, List[String]] = {
    // Parse the parent locales
    val parentLocalesSupplementalData = data.toPath.resolve("common")
      .resolve("supplemental").resolve("supplementalData.xml").toFile
    parseParentLocales(XML.withSAXParser(parser).loadFile(parentLocalesSupplementalData))
  }

  def generateCalendarsFile(base: File, calendars: Seq[Calendar]): File = {
    // Generate numeric systems source code
    writeGeneratedTree(base, "calendars",
      CodeGenerator.calendars(calendars))
  }

  def buildLDMLDescriptors(data: File, numericSystemsMap: Map[String, NumericSystem],
                           latnNS: NumericSystem): List[XMLLDML] = {
    // All files under common/main
    val files = Files.newDirectoryStream(data.toPath.resolve("common")
      .resolve("main")).iterator().asScala.toList

    for {
      f <- files.map(k => k.toFile)
      //if f.getName == "en.xml" || f.getName == "root.xml"
      r = new InputStreamReader(new FileInputStream(f), "UTF-8")
    } yield constructLDMLDescriptor(f, XML.withSAXParser(parser).load(r),
      latnNS, numericSystemsMap)
  }

  def generateLocalesFile(base: File, clazzes: List[XMLLDML], parentLocales: Map[String, List[String]]): File = {
    val names = clazzes.map(_.scalaSafeName)

    // Generate locales code
    val stdTree = CodeGenerator.buildClassTree("data", clazzes, names, parentLocales)
    writeGeneratedTree(base, "data", stdTree)
  }

  def generateMetadataFile(base: File, clazzes: List[XMLLDML]): File = {
    val isoCountryCodes = clazzes.flatMap(_.locale.territory).distinct
      .filter(_.length == 2).sorted
    val isoLanguages = clazzes.map(_.locale.language).distinct
      .filter(_.length == 2).sorted
    val scripts = clazzes.flatMap(_.locale.script).distinct.sorted
    // Generate metadata source code
    writeGeneratedTree(base, "metadata",
      CodeGenerator.metadata(isoCountryCodes, isoLanguages, scripts))
  }

  def generateCurrencyDataFile(base: File, currencyData: CurrencyData): File = {
    writeGeneratedTree(base, "currencydata",
      CodeGenerator.currencyData(currencyData))
  }

  def generateDataSourceCode(base: File, data: File): Seq[File] = {
    val nanos = System.nanoTime()
    val numericSystems = readNumericSystems(data)
    val f1 = generateNumericSystemsFile(base, numericSystems)

    val calendars = readCalendars(data)
    val parentLocales = readParentLocales(data)
    val f2 = generateCalendarsFile(base, calendars)

    val numericSystemsMap: Map[String, NumericSystem] =
      numericSystems.map(n => n.id -> n)(breakOut)
    // latn NS must exist, break if not found
    val latnNS = numericSystemsMap("latn")

    val ldmls = buildLDMLDescriptors(data, numericSystemsMap, latnNS)

    val f3 = generateMetadataFile(base, ldmls)
    val f4 = generateLocalesFile(base, ldmls, parentLocales)

    val currencyData = readCurrencyData(data)
    val f5 = generateCurrencyDataFile(base, currencyData)

    println("Generation took " + (System.nanoTime() - nanos) / 1000000 + " [ms]")
    Seq(f1, f2, f3, f4, f5)
  }
}
