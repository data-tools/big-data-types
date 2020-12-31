package org.datatools.bigdatatypes.bigquery

import com.google.cloud.bigquery.Field.Mode
import com.google.cloud.bigquery.{Field, StandardSQLTypeName}
import org.datatools.bigdatatypes.UnitSpec
import org.datatools.bigdatatypes.bigquery.BigQueryTypes._
import org.datatools.bigdatatypes.formats.TransformKeys.defaultFormats

class BigQueryTypesSpec extends UnitSpec {

  behavior of "BigQueryTypesSpec"

  case class Dummy(myInt: Int, myString: String)
  val expected = List(
    Field.newBuilder("myInt", StandardSQLTypeName.INT64).setMode(Mode.REQUIRED).build(),
    Field.newBuilder("myString", StandardSQLTypeName.STRING).setMode(Mode.REQUIRED).build()
  )

  "A Case Class instance" should "return BigQuery Fields" in {
    val dummy = Dummy(1, "test")
    dummy.getBigQueryFields shouldBe expected
  }

  "A Case Class type" should "return BigQuery Fields" in {
    BigQueryTypes[Dummy].getBigQueryFields shouldBe expected
  }

}
