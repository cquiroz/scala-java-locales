package testsuite.locales

import locales.DecimalFormatUtil
import org.junit.Test
import org.junit.Assert._

class DecimalFormatUtilTest {
  @Test def test_decimal_patterns(): Unit = {
    val p1 = DecimalFormatUtil.toDecimalPatterns("#,##0.00;(#,##0.00)")
    assertEquals("#,##0.00", p1.positive.pattern)
    assertEquals("", p1.positive.prefix)
    assertEquals("", p1.positive.suffix)
    assertEquals("#,##0.00", p1.negative.pattern)
    assertEquals("(", p1.negative.prefix)
    assertEquals(")", p1.negative.suffix)

    val p2 = DecimalFormatUtil.toDecimalPatterns("#,##0.00")
    assertEquals("", p2.positive.prefix)
    assertEquals("", p2.negative.prefix)
    assertEquals("#,##0.00", p2.positive.pattern)
    assertEquals("#,##0.00", p2.negative.pattern)
    assertEquals("", p2.positive.suffix)
    assertEquals("", p2.negative.suffix)

    val p3 = DecimalFormatUtil.toDecimalPatterns("")
    assertEquals("", p3.positive.prefix)
    assertEquals("", p3.negative.prefix)
    assertEquals("", p3.positive.pattern)
    assertEquals("", p3.negative.pattern)
    assertEquals("", p3.positive.suffix)
    assertEquals("", p3.negative.suffix)

    val p4 = DecimalFormatUtil.toDecimalPatterns("ABC #,##0.00 cde")
    assertEquals("ABC ", p4.positive.prefix)
    assertEquals("ABC ", p4.negative.prefix)
    assertEquals("#,##0.00", p4.positive.pattern)
    assertEquals("#,##0.00", p4.negative.pattern)
    assertEquals(" cde", p4.positive.suffix)
    assertEquals(" cde", p4.negative.suffix)

    val p5 = DecimalFormatUtil.toDecimalPatterns("\u2030 #,##0.00 %")
    assertEquals("\u2030 ", p5.positive.prefix)
    assertEquals("\u2030 ", p5.negative.prefix)
    assertEquals("#,##0.00", p5.positive.pattern)
    assertEquals("#,##0.00", p5.negative.pattern)
    assertEquals(" %", p5.positive.suffix)
    assertEquals(" %", p5.positive.suffix)

    val p6 = DecimalFormatUtil.toDecimalPatterns("'' #,##0.00")
    assertEquals("' ", p6.positive.prefix)
    assertEquals("' ", p6.negative.prefix)
    assertEquals("#,##0.00", p6.positive.pattern)
    assertEquals("#,##0.00", p6.negative.pattern)
    assertEquals("", p6.positive.suffix)
    assertEquals("", p6.positive.suffix)

    val p7 = DecimalFormatUtil.toDecimalPatterns("'#' #,##0.00")
    assertEquals("# ", p7.positive.prefix)
    assertEquals("# ", p7.negative.prefix)
    assertEquals("#,##0.00", p7.positive.pattern)
    assertEquals("#,##0.00", p7.negative.pattern)
    assertEquals("", p7.positive.suffix)
    assertEquals("", p7.positive.suffix)

    val p8 = DecimalFormatUtil.toDecimalPatterns("###,#'#'")
    assertEquals("", p8.positive.prefix)
    assertEquals("", p8.negative.prefix)
    assertEquals("###,#", p8.positive.pattern)
    assertEquals("###,#", p8.negative.pattern)
    assertEquals("#", p8.positive.suffix)
    assertEquals("#", p8.positive.suffix)
  }
}
