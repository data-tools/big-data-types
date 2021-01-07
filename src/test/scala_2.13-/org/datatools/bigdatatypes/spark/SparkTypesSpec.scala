package org.datatools.bigdatatypes.spark

import org.apache.spark.sql.types.{StructField, _}
import org.datatools.bigdatatypes.UnitSpec
import org.datatools.bigdatatypes.formats.TransformKeys.defaultFormats
import org.datatools.bigdatatypes.spark.SparkTypes._

class SparkTypesSpec extends UnitSpec {

  behavior of "SparkTypesSpec"

  case class Dummy(myInt: Int, myString: String)
  val fieldList: Seq[StructField] =
    List(
      StructField("myInt", IntegerType, nullable = false),
      StructField("myString", StringType, nullable = false)
    )
  val expectedSchema: StructType = StructType(fieldList)

  "A Case Class instance" should "return BigQuery Fields" in {
    val dummy = Dummy(1, "test")
    dummy.sparkSchema shouldBe expectedSchema
  }

  "A Case Class type" should "return BigQuery Fields" in {
    SparkTypes[Dummy].sparkFields shouldBe expectedSchema
  }

}
