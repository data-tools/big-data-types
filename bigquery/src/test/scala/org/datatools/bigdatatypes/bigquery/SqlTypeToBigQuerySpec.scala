package org.datatools.bigdatatypes.bigquery

import com.google.cloud.bigquery.Field
import org.datatools.bigdatatypes.TestTypes._
import org.datatools.bigdatatypes.{BigQueryTestTypes, UnitSpec}
import org.datatools.bigdatatypes.bigquery.SqlTypeToBigQuery._
import org.datatools.bigdatatypes.formats.Formats.implicitDefaultFormats
import org.datatools.bigdatatypes.conversions.SqlTypeConversion._

/** These tests aims to test all the possible scenarios using `TestTypes`
  */
class SqlTypeToBigQuerySpec extends UnitSpec {

  behavior of "BigQueryTypesSpec"

  "basic case class" should "be converted into BigQueryFields" in {
    val fields: Seq[Field] = SqlTypeToBigQuery[BasicTypes].bigQueryFields
    fields shouldBe BigQueryTestTypes.basicTypes
  }

  "Case Class with Option" should "be converted into BigQueryFields with nullable" in {
    val fields: Seq[Field] = SqlTypeToBigQuery[BasicOption].bigQueryFields
    fields shouldBe BigQueryTestTypes.basicOption
  }

  "Case Class with basic options types" should "be converted into nullable BigQueryFields" in {
    val fields: Seq[Field] = SqlTypeToBigQuery[BasicOptionTypes].bigQueryFields
    fields shouldBe BigQueryTestTypes.basicOptionTypes
  }

  "Case class with List" should "be converted into Repeated BigQueryFields" in {
    val fields: Seq[Field] = SqlTypeToBigQuery[BasicList].bigQueryFields
    fields shouldBe BigQueryTestTypes.basicWithList
  }

  "case class with nested object" should "be converted into BigQueryFields" in {
    val fields: Seq[Field] = SqlTypeToBigQuery[BasicStruct].bigQueryFields
    fields shouldBe BigQueryTestTypes.basicNested
  }

  "case class with optional nested object" should "be converted into BigQueryFields" in {
    val fields: Seq[Field] = SqlTypeToBigQuery[BasicOptionalStruct].bigQueryFields
    fields shouldBe BigQueryTestTypes.basicOptionalNested
  }

  "Case class with Struct List" should "be converted into Repeated BigQueryFields" in {
    val fields: Seq[Field] = SqlTypeToBigQuery[ListOfStruct].bigQueryFields
    fields shouldBe BigQueryTestTypes.basicNestedWithList
  }

  "Case class with extended types" should "be converted into Struct with extended types" in {
    val fields: Seq[Field] = SqlTypeToBigQuery[ExtendedTypes].bigQueryFields
    fields shouldBe BigQueryTestTypes.extendedTypes
  }

  "bigQueryFields method for Case Classes" should "return BigQuery Fields" in {
    val instance = BasicTypes(1, 1, 1, 1, 1, true, "test")
    instance.bigQueryFields shouldBe BigQueryTestTypes.basicTypes
  }

}
