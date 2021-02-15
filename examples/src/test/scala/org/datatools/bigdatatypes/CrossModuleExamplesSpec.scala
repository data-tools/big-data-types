package org.datatools.bigdatatypes

import com.google.cloud.bigquery.Field.Mode
import org.apache.spark.sql.types.StructType
import org.datatools.bigdatatypes.TestTypes._
import org.datatools.bigdatatypes.bigquery.BigQueryTypesInstance
import org.datatools.bigdatatypes.bigquery.BigQueryTypesInstance._
import org.datatools.bigdatatypes.formats.Formats.implicitDefaultFormats
import org.datatools.bigdatatypes.spark.SparkTypes
import org.datatools.bigdatatypes.types.basic.SqlType
import org.datatools.bigdatatypes.spark.SqlTypeConversionSpark._
import com.google.cloud.bigquery.{Field, StandardSQLTypeName}

class CrossModuleExamplesSpec extends UnitSpec {

  behavior of "FromSparkToBigQuerySpec"

  val bqFieldsExpected = List(
    Field.newBuilder("myInt", StandardSQLTypeName.INT64).setMode(Mode.REQUIRED).build(),
    Field.newBuilder("myLong", StandardSQLTypeName.INT64).setMode(Mode.REQUIRED).build(),
    Field.newBuilder("myFloat", StandardSQLTypeName.FLOAT64).setMode(Mode.REQUIRED).build(),
    Field.newBuilder("myDouble", StandardSQLTypeName.FLOAT64).setMode(Mode.REQUIRED).build(),
    Field.newBuilder("myDecimal", StandardSQLTypeName.NUMERIC).setMode(Mode.REQUIRED).build(),
    Field.newBuilder("myBoolean", StandardSQLTypeName.BOOL).setMode(Mode.REQUIRED).build(),
    Field.newBuilder("myString", StandardSQLTypeName.STRING).setMode(Mode.REQUIRED).build()
  )

  "Sparck Schema" should "be converted into BigQuery Fields" in {
    val schema: StructType = SparkTypes[BasicTypes].sparkSchema
    val bq = BigQueryTypesInstance[StructType].bigQueryFields(schema)
    bq shouldBe bqFieldsExpected
  }

  it should "have a method to get BigQuery Fields" in {
    val schema: StructType = SparkTypes[BasicTypes].sparkSchema
    schema.bigQueryFields shouldBe bqFieldsExpected
  }

  //TODO fix something here, sparkSchema uses StructType as main object bug SqlType don't
  it should "be converted into SqlType Instance" in {
    val schema: StructType = SparkTypes[BasicTypes].sparkSchema
    schema.getType shouldBe TestTypes.basicFields
  }

  "SqlType instance" should "converted into BigQuery Fields" in {
    val sql: SqlType = SparkTypes[BasicTypes].sparkSchema.getType
    BigQueryTypesInstance[SqlType].bigQueryFields(sql) shouldBe bqFieldsExpected
  }

}
