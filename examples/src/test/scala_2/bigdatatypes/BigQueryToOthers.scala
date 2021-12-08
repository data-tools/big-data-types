package bigdatatypes

import com.google.cloud.bigquery.Field.Mode
import com.google.cloud.bigquery.{Field, Schema, StandardSQLTypeName}
import org.apache.spark.sql.types.*
import org.datatools.bigdatatypes.UnitSpec
import org.datatools.bigdatatypes.bigquery.BigQueryTypeConversion.schema
import org.datatools.bigdatatypes.bigquery.JavaConverters.toJava
import org.datatools.bigdatatypes.formats.Formats.implicitDefaultFormats
import org.datatools.bigdatatypes.spark.SparkSchemas
import org.datatools.bigdatatypes.spark.SqlInstanceToSpark.InstanceSyntax

class BigQueryToOthers extends UnitSpec {

  /** Spark Schema */
  val sparkSchema: StructType = StructType(
    List(
      StructField("myLong", LongType, nullable = false),
      StructField("myFloat", FloatType, nullable = false),
      StructField("myDecimal", DataTypes.createDecimalType, nullable = false),
      StructField("myBoolean", BooleanType, nullable = false),
      StructField("myString", StringType, nullable = false)
    )
  )

  /** BigQuery Schema */
  val bqSchema: Schema = Schema.of(
    toJava(
      List(
        Field.newBuilder("myLong", StandardSQLTypeName.INT64).setMode(Mode.REQUIRED).build(),
        Field.newBuilder("myFloat", StandardSQLTypeName.FLOAT64).setMode(Mode.REQUIRED).build(),
        Field.newBuilder("myDecimal", StandardSQLTypeName.NUMERIC).setMode(Mode.REQUIRED).build(),
        Field.newBuilder("myBoolean", StandardSQLTypeName.BOOL).setMode(Mode.REQUIRED).build(),
        Field.newBuilder("myString", StandardSQLTypeName.STRING).setMode(Mode.REQUIRED).build()
      )
    )
  )

  behavior of "BigQueryToOthers"

  /** This is BigQuery to Spark mainly as Spark is only available in Scala 2.12 */

  "BigQuery Schema" should "be converted into Spark Schema" in {
    SparkSchemas.schema[Schema](bqSchema) shouldBe sparkSchema
  }

  it should "be converted into Spark Fields" in {
    SparkSchemas.fields[Schema](bqSchema) shouldBe sparkSchema
  }

  it should "be converted into Spark Schema using extension method" in {
    bqSchema.asSparkSchema shouldBe sparkSchema
  }

}
