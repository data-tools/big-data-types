package bigdatatypes

import com.google.cloud.bigquery.Schema
import org.datatools.bigdatatypes.BigQueryTestTypes.basicFields
import org.datatools.bigdatatypes.{SparkTestTypes, UnitSpec}
import org.datatools.bigdatatypes.bigquery.JavaConverters.toJava
import org.datatools.bigdatatypes.spark.SparkSchemas

class BigQueryToSpark extends UnitSpec {

  behavior of "BigQuery types to other types"

  //basicFields is a object that we have defined in every module
  val bqSchema: Schema = Schema.of(toJava(basicFields.toList))

  "BigQuery Schema" should "be converted into Spark Schema" in {
    SparkSchemas.schema[Schema](bqSchema) shouldBe SparkTestTypes.basicTypes
  }
 /*
  it should "be converted into Spark Fields" in {
    SparkSchemas.fields[Schema](bqSchema) shouldBe SparkTestTypes.basicFields
  }

  it should "be converted into Spark Schema using extension method" in {
    bqSchema.sparkSchema shouldBe SparkTestTypes.basicTypes
  }

  it should "be converted into Spark Fields using extension method" in {
    bqSchema.sparkFields shouldBe SparkTestTypes.basicFields
  }
  */




}
