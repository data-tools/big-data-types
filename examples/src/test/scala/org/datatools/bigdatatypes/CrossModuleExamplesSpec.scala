package org.datatools.bigdatatypes

import org.apache.spark.sql.types.StructType
import org.datatools.bigdatatypes.TestTypes.BasicTypes
import org.datatools.bigdatatypes.bigquery.SqlInstanceToBigQuery.InstanceSyntax
import org.datatools.bigdatatypes.bigquery.{SqlInstanceToBigQuery, SqlTypeToBigQuery}
import org.datatools.bigdatatypes.formats.Formats.implicitDefaultFormats
import org.datatools.bigdatatypes.spark.SparkSchemas
import org.datatools.bigdatatypes.spark.SparkTypeConversion.{StructTypeSyntax, structType}
import org.datatools.bigdatatypes.types.basic.SqlType

class CrossModuleExamplesSpec extends UnitSpec {

  behavior of "CrossModuleExamplesSpec"

  "Spark Schema" should "be converted into BigQuery Fields" in {
    val schema: StructType = SparkSchemas.schema[BasicTypes]
    SqlInstanceToBigQuery[StructType].bigQueryFields(schema) shouldBe BigQueryTestTypes.basicFields
  }

  "Case Class" should "be converted into BigQuery Fields" in {
    val bq = SqlTypeToBigQuery[BasicTypes].bigQueryFields
    bq shouldBe BigQueryTestTypes.basicFields
  }

  "Spark to SqlType instance" should "be converted into BigQuery Fields" in {
    val sql: SqlType = SparkSchemas.schema[BasicTypes].getType
    SqlInstanceToBigQuery[SqlType].bigQueryFields(sql) shouldBe BigQueryTestTypes.basicFields
  }


}
