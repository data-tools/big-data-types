package org.datatools.bigdatatypes.bigquery

import com.google.cloud.bigquery.Field
import org.datatools.bigdatatypes.TestTypes._
import org.datatools.bigdatatypes.{BigQueryTestTypes, UnitSpec}
import org.datatools.bigdatatypes.bigquery.BigQueryTypes._
import org.datatools.bigdatatypes.formats.Formats.implicitDefaultFormats
import org.datatools.bigdatatypes.conversions.SqlTypeConversion._

class BigQueryTypesSpec extends UnitSpec {

  behavior of "BigQueryTypesSpec"

  "basic case class" should "be converted into BigQueryFields" in {
    val fields: Seq[Field] = BigQueryTypes[BasicTypes].bigQueryFields
    fields shouldBe BigQueryTestTypes.basicTypes
  }

  "Case Class with Option" should "be converted into BigQueryFields with nullable" in {
    val fields: Seq[Field] = BigQueryTypes[BasicOption].bigQueryFields
    fields shouldBe BigQueryTestTypes.basicOption
  }

  "Case Class with basic options types" should "be converted into nullable BigQueryFields" in {
    val fields: Seq[Field] = BigQueryTypes[BasicOptionTypes].bigQueryFields
    fields shouldBe BigQueryTestTypes.basicOptionTypes
  }

  "Case class with List" should "be converted into Repeated BigQueryFields" in {
    val fields: Seq[Field] = BigQueryTypes[BasicList].bigQueryFields
    fields shouldBe BigQueryTestTypes.basicWithList
  }

  "case class with nested object" should "be converted into BigQueryFields" in {
    val fields: Seq[Field] = BigQueryTypes[BasicStruct].bigQueryFields
    fields shouldBe BigQueryTestTypes.basicNested
  }

  /** shouldBe fails for a reason in this test */
  "case class with optional nested object" should "be converted into BigQueryFields" in {
    val fields: Seq[Field] = BigQueryTypes[BasicOptionalStruct].bigQueryFields
    fields should be equals BigQueryTestTypes.basicOptionalNested
  }

  "Case class with Struct List" should "be converted into Repeated BigQueryFields" in {
    val fields: Seq[Field] = BigQueryTypes[ListOfStruct].bigQueryFields
    fields shouldBe BigQueryTestTypes.basicNestedWithList
  }

  "Case class with extended types" should "be converted into Struct with extended types" in {
    val fields: Seq[Field] = BigQueryTypes[ExtendedTypes].bigQueryFields
    fields shouldBe BigQueryTestTypes.extendedTypes
  }

}
