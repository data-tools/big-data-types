package org.datatools.bigdatatypes.spark

import org.apache.spark.sql.types.*
import org.datatools.bigdatatypes.formats.{Formats, SnakifyFormats}
import org.datatools.bigdatatypes.spark.SqlTypeToSpark.*
import org.datatools.bigdatatypes.UnitSpec

/** Testing implicit formats
  */
class SqlTypeToSparkImplicitFormatsSpec extends UnitSpec {

  behavior of "SparkTypesImplicitFormatsSpec"

  "Field Transformations" should "be applied to field names" in {
    case class Dummy(myInt: Int, myString: String)

    val expectedFields: Seq[StructField] =
      List(
        StructField("my_int", IntegerType, nullable = false),
        StructField("my_string", StringType, nullable = false)
      )
    val expectedSchema: StructType = StructType(expectedFields)

    implicit val formats: Formats = SnakifyFormats
    SparkSchemas.fields[Dummy] shouldBe expectedSchema
    SparkSchemas.fields[Dummy] shouldBe expectedFields
  }
}
