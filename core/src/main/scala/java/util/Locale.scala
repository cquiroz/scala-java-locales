package java.util

import locales.BCP47
import scala.collection.{ Map => SMap, Set => SSet }
import scala.collection.JavaConverters._
import scala.util.matching.Regex
import locales.BCP47.{ GrandfatheredTag, LanguageTag, PrivateUseTag }
import locales.cldr.LocalesProvider
import locales.cldr.CLDRMetadata
import locales.cldr.LDML
import locales.cldr.NumberingSystem
import org.portablescala.reflect._

private[java] object LocalesDb {

  val provider: LocalesProvider =
    Reflect
      .lookupLoadableModuleClass("locales.cldr.data.LocalesProvider$", null)
      .getOrElse(sys.error("Needs a locale provider"))
      .loadModule
      .asInstanceOf[LocalesProvider]

  val root: LDML = provider.root

  val latn: NumberingSystem = provider.latn

  val ldmls = provider.ldmls

  val metadata: CLDRMetadata = provider.metadata

  val currencydata = provider.currencyData

  /**
    * Attempts to give a Locale for the given tag if available
    */
  def localeForLanguageTag(languageTag: String): Option[Locale] =
    // TODO Support alternative tags for the same locale
    if (languageTag == "und") {
      Some(Locale.ROOT)
    } else
      provider.ldmls.get(languageTag).map(_.toLocale)

  /**
    * Returns the ldml for the given locale
    */
  def ldml(locale: Locale): Option[LDML] = {
    val tag =
      if (locale.toLanguageTag() == "zh-CN") "zh-Hans-CN"
      else if (locale.toLanguageTag() == "zh-TW") "zh-Hant-TW"
      else locale.toLanguageTag()
    provider.ldmls.get(tag)
  }
}

object Locale {
  import LocalesDb._

  // Default locales required by the specs
  lazy val ENGLISH: Locale  = localeForLanguageTag("en").getOrElse(ROOT)
  lazy val FRENCH: Locale   = localeForLanguageTag("fr").getOrElse(ROOT)
  lazy val GERMAN: Locale   = localeForLanguageTag("de").getOrElse(ROOT)
  lazy val ITALIAN: Locale  = localeForLanguageTag("it").getOrElse(ROOT)
  lazy val JAPANESE: Locale = localeForLanguageTag("ja").getOrElse(ROOT)
  lazy val KOREAN: Locale   = localeForLanguageTag("ko").getOrElse(ROOT)
  lazy val CHINESE: Locale  = localeForLanguageTag("zh").getOrElse(ROOT)
  lazy val SIMPLIFIED_CHINESE: Locale = {
    val l = ldmls.getOrElse("zh-Hans-CN", root)
    l.copy(locale = l.locale.copy(script = None)).toLocale
  }
  lazy val TRADITIONAL_CHINESE: Locale = {
    val l = ldmls.getOrElse("zh-Hant-TW", root)
    l.copy(locale = l.locale.copy(script = None)).toLocale
  }
  lazy val FRANCE: Locale        = localeForLanguageTag("fr-FR").getOrElse(ROOT)
  lazy val GERMANY: Locale       = localeForLanguageTag("de-DE").getOrElse(ROOT)
  lazy val ITALY: Locale         = localeForLanguageTag("it-IT").getOrElse(ROOT)
  lazy val JAPAN: Locale         = localeForLanguageTag("ja-JP").getOrElse(ROOT)
  lazy val KOREA: Locale         = localeForLanguageTag("ko-KR").getOrElse(ROOT)
  lazy val CHINA: Locale         = SIMPLIFIED_CHINESE
  lazy val PRC: Locale           = SIMPLIFIED_CHINESE
  lazy val TAIWAN: Locale        = TRADITIONAL_CHINESE
  lazy val UK: Locale            = localeForLanguageTag("en-GB").getOrElse(ROOT)
  lazy val US: Locale            = localeForLanguageTag("en-US").getOrElse(ROOT)
  lazy val CANADA: Locale        = localeForLanguageTag("en-CA").getOrElse(ROOT)
  lazy val CANADA_FRENCH: Locale = localeForLanguageTag("fr-CA").getOrElse(ROOT)
  lazy val ROOT: Locale          = root.toLocale

  val PRIVATE_USE_EXTENSION: Char    = 'x'
  val UNICODE_LOCALE_EXTENSION: Char = 'u'

  final class Category private (name: String, ordinal: Int) extends Enum[Category](name, ordinal)

  object Category {
    val DISPLAY: Category = new Category("DISPLAY", 0)
    val FORMAT: Category  = new Category("FORMAT", 1)

    private lazy val categories = Array(DISPLAY, FORMAT)

    def values(): Array[Category] = categories

    def valueOf(name: String): Category =
      categories.find(_.name == name).getOrElse {
        if (name == null)
          throw new NullPointerException("Argument cannot be null")
        else
          throw new IllegalArgumentException(s"No such category: $name")
      }
  }

  private[util] def checkRegex(regex: Regex, s: String): Boolean =
    s match {
      case regex() => true
      case _       => false
    }

  private[util] def checkLanguage(l: String): Boolean =
    checkRegex("[a-zA-Z]{2,8}".r, l)

  private[util] def checkScript(l: String): Boolean =
    checkRegex("[a-zA-Z]{4}".r, l)

  private[util] def checkRegion(l: String): Boolean =
    checkRegex("[a-zA-Z]{2}".r, l) || checkRegex("[0-9]{3}".r, l)

  private[util] def checkAcceptableVariantSegment(l: String): Boolean =
    checkRegex("[0-9a-zA-Z]{1,8}".r, l)

  private[util] def checkVariantSegment(l: String): Boolean =
    checkRegex("[0-9][0-9a-zA-Z]{3}".r, l) ||
      checkRegex("[0-9a-zA-Z]{5,8}".r, l)

  private[util] def checkVariant(l: String): Boolean = {
    val parts = l.split("-|_")
    parts.forall(checkVariantSegment)
  }

  private[util] def checkExtKey(key: Char): Boolean = key.isLetterOrDigit

  private[util] def checkExtValue(value: String): Boolean =
    value.split("-").forall(checkRegex("[0-9a-zA-Z]{2,8}".r, _))

  private[util] def checkUnicodeKey(l: String): Boolean =
    checkRegex("[a-zA-Z]{2}".r, l)

  private[util] def checkUnicodeType(l: String): Boolean =
    l.isEmpty || l.split("-").forall(checkRegex("[0-9a-zA-Z]{3,8}".r, _))

  private[util] def checkAttribute(l: String): Boolean =
    checkRegex("[0-9a-zA-Z]{3,8}".r, l)

  class Builder() {
    private[this] var builder = LocaleBuilder()

    // TODO Implement
    //def setLocale(locale: Locale): Builder = ???

    //def setLanguageTag(languageTag: String): Builder = ???

    def setLanguage(language: String): Builder = {
      builder = builder
        .language(language)
        .fold(throw new IllformedLocaleException(s"Invalid language $language"))(identity)
      this
    }

    def setScript(script: String): Builder = {
      builder = builder
        .script(script)
        .fold(throw new IllformedLocaleException(s"Invalid script $script"))(identity)
      this
    }

    def setRegion(region: String): Builder = {
      builder = builder
        .region(region)
        .fold(throw new IllformedLocaleException(s"Invalid region $region"))(identity)
      this
    }

    def setVariant(variant: String): Builder = {
      builder = builder
        .variant(variant)
        .fold(throw new IllformedLocaleException(s"Invalid variant $variant"))(identity)
      this
    }

    def setExtension(key: Char, value: String): Builder = {
      builder = builder
        .extension(key, value)
        .fold(throw new IllformedLocaleException(s"Invalid extension $key: $value"))(identity)
      this
    }

    def setUnicodeLocaleKeyword(key: String, _type: String): Builder = {
      if (key == null) {
        throw new NullPointerException("Null unicode extension key")
      }
      builder = builder
        .unicodeLocaleKeyword(key, _type)
        .fold(throw new IllformedLocaleException(s"Invalid unicode keyword $key: ${_type}"))(
          identity
        )
      this
    }

    def addUnicodeLocaleAttribute(attribute: String): Builder = {
      if (attribute == null) {
        throw new NullPointerException("Null unicode attribute")
      }
      builder = builder
        .addUnicodeLocaleAttribute(attribute)
        .fold(throw new IllformedLocaleException(s"Invalid unicode attribute $attribute"))(identity)
      this
    }

    def removeUnicodeLocaleAttribute(attribute: String): Builder = {
      if (attribute == null) {
        throw new NullPointerException("Null unicode attribute")
      }
      builder = builder
        .removeUnicodeLocaleAttribute(attribute)
        .fold(throw new IllformedLocaleException(s"Invalid unicode attribute $attribute"))(identity)
      this
    }

    def clear(): Builder = {
      builder = LocaleBuilder()
      this
    }

    def clearExtensions(): Builder = {
      builder = builder.clearExtensions
      this
    }

    def build(): Locale = builder.build
  }

  private case class LocaleBuilder(
    strict:            Boolean              = true,
    language:          Option[String]       = None,
    region:            Option[String]       = None,
    variant:           Option[String]       = None,
    script:            Option[String]       = None,
    extensions:        SMap[Char, String]   = SMap.empty,
    unicodeExtensions: SMap[String, String] = SMap.empty,
    unicodeAttributes: SSet[String]         = SSet.empty
  ) {

    def language(language: String): Option[LocaleBuilder] =
      if (language == null || language.isEmpty) {
        Some(copy(language = None))
      } else if (!strict || checkLanguage(language)) {
        Some(copy(language = Some(language.toLowerCase)))
      } else {
        None
      }

    def script(script: String): Option[LocaleBuilder] =
      if (script == null || script.isEmpty) {
        Some(copy(script = None))
      } else if (!strict || checkScript(script)) {
        // Script must be canonicalized
        Some(copy(script = Some(script.charAt(0).toUpper + script.substring(1))))
      } else {
        None
      }

    def region(region: String): Option[LocaleBuilder] =
      if (region == null || region.isEmpty) {
        Some(copy(region = None))
      } else if (!strict || checkRegion(region)) {
        Some(copy(region = Some(region.toUpperCase)))
      } else {
        None
      }

    def addVariant(v: String): Option[LocaleBuilder] =
      Some(copy(variant = this.variant.map(_ + "_" + v).orElse(Some(v))))

    def variant(variant: String): Option[LocaleBuilder] =
      if (variant == null || variant.isEmpty) {
        Some(copy(variant = None))
      } else if (!strict || checkVariant(variant)) {
        Some(copy(variant = Some(variant.replace("-", "_"))))
      } else {
        None
      }

    def extension(key: Char, value: String): Option[LocaleBuilder] =
      if (extensions.contains(key) || (value == null || value.isEmpty)) {
        // remove
        Some(copy(extensions = extensions.filter { case (k, _) => k != key }))
      } else if (key == UNICODE_LOCALE_EXTENSION) {
        // replace all unicode extensions
        Some(
          copy(extensions = extensions + (key -> value.toLowerCase), unicodeExtensions = SMap.empty)
        )
      } else if (!strict || checkExtKey(key) && checkExtValue(value)) {
        Some(copy(extensions = extensions + (key -> value.toLowerCase)))
      } else {
        None
      }

    def unicodeLocaleKeyword(key: String, _type: String): Option[LocaleBuilder] =
      if (!strict || checkUnicodeKey(key) && checkUnicodeType(_type)) {
        Some(copy(unicodeExtensions = unicodeExtensions + (key -> _type)))
      } else {
        None
      }

    def addUnicodeLocaleAttribute(attribute: String): Option[LocaleBuilder] =
      if (!strict || checkAttribute(attribute)) {
        Some(copy(unicodeAttributes = unicodeAttributes + attribute))
      } else {
        None
      }

    def removeUnicodeLocaleAttribute(attribute: String): Option[LocaleBuilder] =
      if (!strict || checkAttribute(attribute)) {
        Some(copy(unicodeAttributes = unicodeAttributes.filterNot(_.equalsIgnoreCase(attribute))))
      } else {
        None
      }

    def clearExtensions: LocaleBuilder =
      copy(extensions = SMap.empty)

    def build: Locale =
      new Locale(
        language.getOrElse(""),
        region.getOrElse(""),
        variant.getOrElse(""),
        script,
        extensions,
        unicodeExtensions,
        unicodeAttributes
      )
  }

  private var defaultLocale: Locale = LocalesDb.root.toLocale
  private var defaultPerCategory: SMap[Locale.Category, Option[Locale]] =
    Locale.Category.values().map(_ -> Some(defaultLocale)).toMap

  private def default: Locale = defaultLocale

  private def default(category: Locale.Category): Locale =
    if (category == null) {
      throw new NullPointerException("Argument cannot be null")
    } else {
      defaultPerCategory
        .get(category)
        .flatten
        .getOrElse(throw new IllegalStateException(s"No default locale set for category $category"))
    }

  def setDefault(newLocale: Locale): Unit = {
    if (newLocale == null) {
      throw new NullPointerException("Argument cannot be null")
    }
    defaultLocale      = newLocale
    defaultPerCategory = Locale.Category.values().map(_ -> Some(newLocale)).toMap
  }

  def setDefault(category: Locale.Category, newLocale: Locale): Unit =
    if (category == null || newLocale == null) {
      throw new NullPointerException("Argument cannot be null")
    } else {
      defaultPerCategory = defaultPerCategory + (category -> Some(newLocale))
    }

  def getDefault(): Locale = default

  def getDefault(category: Category): Locale = default(category)

  def getAvailableLocales(): Array[Locale] =
    LocalesDb.provider.ldmls.map(_._2.toLocale).toArray

  def getISOCountries(): Array[String] = metadata.isoCountries

  def getISOLanguages(): Array[String] = metadata.isoLanguages

  private def parseLanguageTag(tag: String): Option[Locale] = {
    // grandfathered mapping
    val grandfathered = SMap(
      "art-lojban" -> "jbo",
      "i-ami" -> "ami",
      "i-bnn" -> "bnn",
      "i-hak" -> "hak",
      "i-klingon" -> "tlh",
      "i-lux" -> "lb",
      "i-hak" -> "hak",
      "i-navajo" -> "nv",
      "i-pwn" -> "pwn",
      "i-tao" -> "tao",
      "i-tay" -> "tay",
      "i-tsu" -> "tsu",
      "no-bok" -> "nb",
      "no-nyn" -> "nn",
      "sgn-BE-FR" -> "sfb",
      "sgn-BE-NL" -> "vgt",
      "sgn-CH-DE" -> "sgg",
      "zh-guoyu" -> "cmn",
      "zh-hakka" -> "hak",
      "zh-min-nan" -> "nan",
      "zh-xiang" -> "hsn",
      "cel-gaulish" -> "xtg",
      "en-GB-oed" -> "en-GB-x-oed",
      "i-default" -> "en-x-i-default",
      "i-enochian" -> "und-x-i-enochian",
      "i-mingo" -> "see-x-i-mingo",
      "zh-min" -> "nan-x-zh-min"
    )

    def sanitizePrivateExtension(b: LocaleBuilder, p: Option[String]): LocaleBuilder = {
      val lvariantRegex    = "lvariant-(.*)".r
      val longVariantRegex = "(.*)-lvariant-(.*)".r
      p.collect {
          case longVariantRegex(x, y) =>
            for {
              b1 <- b.addVariant(y.replace("-", "_"))
              b2 <- b1.extension('x', x)
            } yield b2

          case lvariantRegex(x) => b.addVariant(x.replace("-", "_"))
          case x                => b.extension('x', x)
        }
        .flatten
        .getOrElse(b)
    }

    BCP47.parseTag(tag) match {
      case Some(LanguageTag(l, e, s, r, v, x, p)) =>
        // By the javadocs the builder is lenient
        val builder = LocaleBuilder(strict = false)

        // "und" defaults to the empty string
        val la = if (l == "und") "" else l

        // Spilt the extensions
        val extRegex = "([0-9A-WY-Za-wy-z])-([A-Za-z0-9]{2,8})+".r
        val exts = x.collect {
          case extRegex(c, xv) => c.charAt(0) -> xv
        }
        val b = for {
          b1 <- builder.language(e.flatMap(_.split("-").headOption).getOrElse(la))
          b2 <- b1.script(s.getOrElse(""))
          b3 <- b2.region(r.getOrElse(""))
          b4 <- b3.variant(v.mkString("_"))
          b5 <- exts.foldLeft(Option(b4)) {
            case (bu, (c, xv)) => bu.flatMap(_.extension(c, xv))
          }
        } yield sanitizePrivateExtension(b5, p)
        b.map(_.build)

      case Some(GrandfatheredTag(g)) =>
        val default = new Locale(g)
        grandfathered.get(g).fold(Option(default))(parseLanguageTag)

      case Some(PrivateUseTag(p)) =>
        val default =
          LocaleBuilder(strict = false).extension('x', p).fold(ROOT)(_.build)
        grandfathered.get(p).fold(Option(default))(parseLanguageTag)

      case None =>
        // Last ditch attempt to parse, Javadocs don't define this case well
        val split = tag.split("-").toList
        split match {
          case l :: Nil if checkLanguage(l)    => Some(new Locale(l))
          case l :: c :: _ if checkLanguage(l) => Some(new Locale(l, c))
          case _                               => None
        }
    }
  }

  def forLanguageTag(languageTag: String): Locale =
    localeForLanguageTag(languageTag)
      .orElse(parseLanguageTag(languageTag))
      .getOrElse(ROOT)

  //class LanguageRange

  //class FilteringMode

  //def filter(priorityList: List[LanguageRange], locales: Collection[Locale], mode: FilteringMode): List[Locale] = ???

  //def filter(priorityList: List[LanguageRange], locales: Collection[Locale]): List[Locale] = ???

  //def filter(priorityList: List[LanguageRange], tags: Collection[String], mode: FilteringMode): List[Locale] = ???

  //def filter(priorityList: List[LanguageRange], tags: Collection[String]): List[Locale] = ???

  //def lookupTag(priorityList: List[LanguageRange], tags: Collection[String]): String = ???
}

class Locale private[util] (
  private[this] val language:            String,
  private[this] val country:             String,
  private[this] val variant:             String,
  private[this] val script:              Option[String],
  private[this] val _extensions:         SMap[Char, String],
  private[this] val unicodeExtensions:   SMap[String, String],
  private[this] val unicodeAttributes:   SSet[String],
  private[this] val supportSpecialCases: Boolean = true
) {

  // Required by the javadocs
  if (language == null || country == null || variant == null)
    throw new NullPointerException("Null argument to constructor not allowed")

  // Handle 2 special cases jp_JP_JP and th_TH_TH
  private[this] val extensions = {
    if (((language, country, variant)) == (("ja", "JP", "JP")) &&
        supportSpecialCases) {
      _extensions + (Locale.UNICODE_LOCALE_EXTENSION -> "ca-japanese")
    } else if (((language, country, variant)) == (("th", "TH", "TH")) &&
               supportSpecialCases) {
      _extensions + (Locale.UNICODE_LOCALE_EXTENSION -> "nu-thai")
    } else {
      _extensions
    }
  }

  private def updateSpecialLanguages(l: String) = l match {
    case "iw" => "he"
    case "ji" => "yi"
    case "in" => "id"
    case _    => l
  }

  private def translateSpecialLanguages(l: String) = l match {
    case "he" => "iw"
    case "yi" => "ji"
    case "id" => "in"
    case _    => l
  }

  // public constructors
  def this(language: String, country: String, variant: String) =
    this(language, country, variant, None, SMap.empty, SMap.empty, SSet.empty)

  // Additional constructors
  def this(language: String, country: String) = this(language, country, "")

  def this(language: String) = this(language, "", "")

  def getLanguage(): String = translateSpecialLanguages(language.toLowerCase)

  def getScript(): String = script.getOrElse("")

  def getCountry(): String = country.toUpperCase

  def getVariant(): String = variant

  def hasExtensions(): Boolean =
    extensions.nonEmpty || unicodeExtensions.nonEmpty

  def stripExtensions(): Locale =
    new Locale(language, country, variant, script, SMap.empty, SMap.empty, SSet.empty, false)

  def getExtension(key: Char): String =
    if (key == Locale.UNICODE_LOCALE_EXTENSION && unicodeExtensions.nonEmpty) {
      unicodeExtensions
        .collect {
          case (k, v) if v.isEmpty => k
          case (k, v)              => s"$k-$v"
        }
        .mkString("-")
    } else extensions.get(key).orNull

  def getExtensionKeys(): Set[Char] = {
    if (unicodeExtensions.nonEmpty) {
      extensions.keySet + Locale.UNICODE_LOCALE_EXTENSION
    } else extensions.keySet
  }.asJava

  def getUnicodeLocaleAttributes(): Set[String] = unicodeAttributes.asJava

  def getUnicodeLocaleType(key: String): String =
    unicodeExtensions.get(key).orNull

  def getUnicodeLocaleKeys(): Set[String] = unicodeExtensions.keySet.asJava

  override def toString(): String = {
    // Rather complex toString following the specifications
    val hasVariant  = getVariant().nonEmpty
    val hasCountry  = getCountry().nonEmpty
    val hasLanguage = getLanguage().nonEmpty
    val hasScript   = getScript().nonEmpty

    val countryPart =
      if (hasCountry) s"_${getCountry()}"
      else if (hasVariant || hasScript) "_"
      else ""

    val variantPart =
      if (hasVariant) s"_${getVariant()}"
      else if (hasScript) "_"
      else ""

    val scriptPart =
      if (hasScript && hasExtensions()) s"#${getScript()}_"
      else if (hasScript) s"#${getScript()}"
      else if (hasExtensions()) "_#"
      else ""

    val extensionsPart = extensions
      .map { case (x, v) => s"$x-$v" }
      .mkString("")
    if (hasLanguage || hasCountry) {
      s"${getLanguage()}$countryPart$variantPart$scriptPart$extensionsPart"
    } else {
      ""
    }
  }

  def toLanguageTag(): String = {
    val language = {
      if (getLanguage().nonEmpty && Locale.checkLanguage(getLanguage))
        updateSpecialLanguages(getLanguage())
      else
        "und"
    }
    val country =
      if (Locale.checkRegion(getCountry())) s"-${getCountry()}"
      else ""
    val variantSegments = getVariant().split("-|_")
    val allSegmentsWellFormed =
      variantSegments.forall(Locale.checkVariantSegment)
    val allAcceptableSegments =
      variantSegments.forall(Locale.checkAcceptableVariantSegment)
    val variant = {
      if (allSegmentsWellFormed) {
        variantSegments.mkString("-", "-", "")
      } else if (allAcceptableSegments) {
        val (wellFormed, acceptable) =
          variantSegments.partition(Locale.checkVariantSegment)
        val pre =
          if (wellFormed.nonEmpty) wellFormed.take(1).mkString("-", "-", "-")
          else "-"
        pre + "x-lvariant" +
          (acceptable ++ wellFormed.drop(1)).mkString("-", "-", "")
      } else {
        ""
      }
    }
    val ext = {
      if (extensions.nonEmpty)
        extensions.map { case (x, v) => s"$x-$v" }.mkString("-", "-", "")
      else
        ""
    }
    val script = this.script.map(_ => s"-${getScript()}").getOrElse("")

    if (getLanguage == "no" && getCountry == "NO" && getVariant == "NY") "nn-NO"
    else s"$language$script$country$ext$variant"
  }

  def getISO3Country(): String =
    if (country.isEmpty) ""
    else
      LocalesDb.metadata.iso3Countries
        .getOrElse(
          country,
          throw new MissingResourceException(
            "Alpha-3 country code not found",
            "java.util.Locale",
            country
          )
        )

  def getISO3Language(): String =
    if (language.isEmpty) ""
    else if (language.lengthCompare(3) == 0) language
    else
      LocalesDb.metadata.iso3Languages
        .getOrElse(
          language,
          throw new MissingResourceException(
            "Alpha-3 language code not found",
            "java.util.Locale",
            language
          )
        )

  // TODO Implement
  //final def getDisplayLanguage(): String = ???

  //final def getDisplayLanguage(inLocale: Locale): String = ???

  //final def getDisplayScript(): String = ???

  //final def getDisplayScript(inLocale: Locale): String = ???

  //final def getDisplayCountry(): String = ???

  //final def getDisplayCountry(inLocale: Locale): String = ???

  //final def getDisplayVariant(): String = ???

  //final def getDisplayVariant(inLocale: Locale): String = ???

  //final def getDisplayName(): String = ???

  //final def getDisplayName(inLocale: Locale): String = ???

  override def clone(): AnyRef = this // Locale is immutable

  private def isEqual(l: Locale): Boolean =
    language == l.getLanguage && country == l.getCountry &&
      variant == l.getVariant && script.forall(_ == l.getScript) &&
      extensions.forall { case (k, v)        => l.getExtension(k) == v } &&
      unicodeExtensions.forall { case (k, v) => l.getUnicodeLocaleType(k) == v } &&
      unicodeAttributes == l.getUnicodeLocaleAttributes.asScala

  override def equals(x: Any): Boolean = x match {
    case l: Locale => isEqual(l)
    case _         => false
  }

  override def hashCode(): Int = toLanguageTag.hashCode
}
