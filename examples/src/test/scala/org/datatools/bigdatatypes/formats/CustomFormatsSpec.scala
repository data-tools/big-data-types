package org.datatools.bigdatatypes.formats

import java.sql.{Date, Timestamp}

import com.google.cloud.bigquery.Field.Mode
import com.google.cloud.bigquery.{Field, FieldList, StandardSQLTypeName}
import org.datatools.bigdatatypes.BigQueryTestTypes.basicFields
import org.datatools.bigdatatypes.TestTypes.BasicTypes
import org.datatools.bigdatatypes.bigquery.SqlTypeToBigQuery
import org.datatools.bigdatatypes.{BigQueryTestTypes, UnitSpec}

class CustomFormatsSpec extends UnitSpec {

  behavior of "CustomFormatsSpec"

  case class Dummy(myInt: Int, active: Boolean, created: Date, updated: Timestamp)
  val fields: Seq[Field] = List(
    Field.newBuilder("myInt", StandardSQLTypeName.INT64).setMode(Mode.REQUIRED).build(),
    Field.newBuilder("is_active", StandardSQLTypeName.BOOL).setMode(Mode.REQUIRED).build(),
    Field.newBuilder("created_at", StandardSQLTypeName.DATE).setMode(Mode.REQUIRED).build(),
    Field.newBuilder("updated_at", StandardSQLTypeName.TIMESTAMP).setMode(Mode.REQUIRED).build()
  )


  "Key Transformation based on type" should "be converted" in {
    implicit val formats: Formats = KeyTypeExampleFormats
    val bq = SqlTypeToBigQuery[Dummy].bigQueryFields
    bq shouldBe fields
  }

  it should "be converted using Nested objects" in {
    case class DummyNested(myString: String, myDummy: Dummy)
    implicit val formats: Formats = KeyTypeExampleFormats

    val expected: Seq[Field] =
      List(
        Field.newBuilder("myString", StandardSQLTypeName.STRING).setMode(Mode.REQUIRED).build(),
        Field
          .newBuilder(
            "myDummy",
            StandardSQLTypeName.STRUCT,
            FieldList.of(
              fields: _*
            )
          )
          .setMode(Mode.REQUIRED)
          .build()
      )

    val bq = SqlTypeToBigQuery[DummyNested].bigQueryFields
    bq shouldBe expected
  }
}
