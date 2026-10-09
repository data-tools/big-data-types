package org.datatools.bigdatatypes.formats

import org.datatools.bigdatatypes.UnitSpec
import org.datatools.bigdatatypes.basictypes.SqlType._
import org.datatools.bigdatatypes.basictypes.SqlTypeMode._

class FormatsSpec extends UnitSpec {

  behavior of "FormatsSpec"

  "Default Formats" should "leave keys unchanged" in {
    DefaultFormats.transformKey("myValue", SqlString()) shouldBe "myValue"
    DefaultFormats.transformKey("myValue", SqlInt()) shouldBe "myValue"
  }

  "Default Formats" should "use BigDecimal precision 10 scale 0" in {
    DefaultFormats.bigDecimal shouldBe DefaultFormats.BigDecimalPrecision(10, 0)
  }

  "Base Formats" should "leave keys unchanged by default" in {
    val formats = new Formats {}
    formats.transformKey("myValue", SqlString()) shouldBe "myValue"
  }

  "Snakify Formats" should "convert camelCase to snake_case" in {
    SnakifyFormats.transformKey("myValue", SqlString()) shouldBe "my_value"
  }

  "Snakify Formats" should "handle acronyms" in {
    SnakifyFormats.transformKey("myURLValue", SqlString()) shouldBe "my_url_value"
  }

  "KeyTypeExample Formats" should "prefix booleans with is_" in {
    KeyTypeExampleFormats.transformKey("active", SqlBool()) shouldBe "is_active"
  }

  "KeyTypeExample Formats" should "suffix dates and timestamps with _at" in {
    KeyTypeExampleFormats.transformKey("birth", SqlDate()) shouldBe "birth_at"
    KeyTypeExampleFormats.transformKey("created", SqlTimestamp()) shouldBe "created_at"
  }

  "KeyTypeExample Formats" should "leave other types unchanged" in {
    KeyTypeExampleFormats.transformKey("name", SqlString()) shouldBe "name"
    KeyTypeExampleFormats.transformKey("count", SqlInt()) shouldBe "count"
  }

}
