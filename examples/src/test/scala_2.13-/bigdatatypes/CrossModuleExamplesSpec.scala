package bigdatatypes

import org.apache.spark.sql.types.StructType
import org.datatools.bigdatatypes.TestTypes._
import org.datatools.bigdatatypes.{BigQueryTestTypes, TestTypes, UnitSpec}
import org.datatools.bigdatatypes.bigquery.{BigQueryTable, BigQueryTypesInstance}
import org.datatools.bigdatatypes.bigquery.BigQueryTypesInstance._
import org.datatools.bigdatatypes.formats.Formats.implicitDefaultFormats
import org.datatools.bigdatatypes.spark.SparkTypes
import org.datatools.bigdatatypes.spark.SqlTypeConversionSpark._
import org.datatools.bigdatatypes.types.basic.SqlType

class CrossModuleExamplesSpec extends UnitSpec {

  behavior of "FromSparkToBigQuerySpec"

  "Sparck Schema" should "be converted into BigQuery Fields" in {
    val schema: StructType = SparkTypes[BasicTypes].sparkSchema
    val bq = BigQueryTypesInstance[StructType].bigQueryFields(schema)
    bq shouldBe BigQueryTestTypes.basicFields
  }

  it should "have a method to get BigQuery Fields" in {
    val schema: StructType = SparkTypes[BasicTypes].sparkSchema
    schema.bigQueryFields shouldBe BigQueryTestTypes.basicFields
  }

  it should "be converted into SqlType Instance" in {
    val schema: StructType = SparkTypes[BasicTypes].sparkSchema
    schema.getType shouldBe TestTypes.basicTypes
  }

  "Spark to SqlType instance" should "be converted into BigQuery Fields" in {
    val sql: SqlType = SparkTypes[BasicTypes].sparkSchema.getType
    BigQueryTypesInstance[SqlType].bigQueryFields(sql) shouldBe BigQueryTestTypes.basicFields
  }

  "Spark Schema" should "create a Big Query Table" in {
    val schema = SparkTypes[BasicTypes].sparkSchema
    //Just an example, it will be left as there is no BigQuery Environment set up
    BigQueryTable.createTable(schema, "dataset", "table").isLeft shouldBe true
  }




}
