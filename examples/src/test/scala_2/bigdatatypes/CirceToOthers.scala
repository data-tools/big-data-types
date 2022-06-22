package bigdatatypes

import io.circe.Json
import org.apache.spark.sql.types.{DataTypes, StringType, StructField, StructType}
import org.datatools.bigdatatypes.UnitSpec
import org.datatools.bigdatatypes.circe.CirceTypeConversion.circeJsonType
import org.datatools.bigdatatypes.formats.Formats.implicitDefaultFormats
import org.datatools.bigdatatypes.spark.SparkSchemas
import org.datatools.bigdatatypes.spark.SqlInstanceToSpark.InstanceSyntax

class CirceToOthers extends UnitSpec {

  behavior of "Circe to Spark types"

  val sparkSchema: StructType = StructType(
    List(
      StructField("id", StringType, nullable = false),
      StructField("foo", StringType, nullable = false),
      StructField("bar", DataTypes.createDecimalType, nullable = false)
    )
  )

  val circe: Json = Json.fromFields(
    List(
      ("id", Json.fromString("test")),
      ("foo", Json.fromString("test")),
      ("bar", Json.fromInt(1))
    )
  )

  "Circe Json" should "be converted into Spark Schema" in {
    SparkSchemas.schema[Json](circe) shouldBe sparkSchema
  }

  it should "be converted into Spark Schema using extension method" in {
    circe.asSparkSchema shouldBe sparkSchema
  }

}
