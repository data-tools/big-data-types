package org.datatools.bigdatatypes.spark

import org.apache.spark.sql.types._
import org.datatools.bigdatatypes.formats.{DefaultFormats, Formats}
import org.datatools.bigdatatypes.spark.SparkTypes._
import org.datatools.bigdatatypes.UnitSpec

/** Testing multiple case classes for conversions
  */
class SparkTypesMultipleClassesSpec extends UnitSpec {

  implicit val defaultFormats: Formats = DefaultFormats

  case class Dummy(myInt: Int, myString: String)

  behavior of "SparkTypesSpec for Multiple Case Classes"

  "Multiple Case Classes" should "create an Spark Schema with appended fields" in {
    case class Append1(myAppend1: Int)
    val fieldList: Seq[StructField] =
      List(
        StructField("myInt", IntegerType, nullable = false),
        StructField("myString", StringType, nullable = false),
        StructField("myAppend1", IntegerType, nullable = false)
      )
    SparkSchemas.fields[Dummy, Append1] shouldBe fieldList
    SparkSchemas.schema[Dummy, Append1] shouldBe StructType(fieldList)
  }

  "3 Case Classes" should "create an Spark Schema with appended fields" in {
    case class Append1(myAppend1: Int)
    case class Append2(myAppend2: String)
    val fieldList: Seq[StructField] =
      List(
        StructField("myInt", IntegerType, nullable = false),
        StructField("myString", StringType, nullable = false),
        StructField("myAppend1", IntegerType, nullable = false),
        StructField("myAppend2", StringType, nullable = false)
      )
    SparkSchemas.fields[Dummy, Append1, Append2] shouldBe fieldList
    SparkSchemas.schema[Dummy, Append1, Append2] shouldBe StructType(fieldList)
  }

  "4 Case Classes" should "create an Spark Schema with appended fields" in {
    case class Append1(myAppend1: Int)
    case class Append2(myAppend2: String)
    case class Append3(myAppend3: String)
    val fieldList: Seq[StructField] =
      List(
        StructField("myInt", IntegerType, nullable = false),
        StructField("myString", StringType, nullable = false),
        StructField("myAppend1", IntegerType, nullable = false),
        StructField("myAppend2", StringType, nullable = false),
        StructField("myAppend3", StringType, nullable = false)
      )
    SparkSchemas.fields[Dummy, Append1, Append2, Append3] shouldBe fieldList
    SparkSchemas.schema[Dummy, Append1, Append2, Append3] shouldBe StructType(fieldList)
  }

  "5 Case Classes" should "create an Spark Schema with appended fields" in {
    case class Append1(myAppend1: Int)
    case class Append2(myAppend2: String)
    case class Append3(myAppend3: String)
    case class Append4(myAppend4: String)
    val fieldList: Seq[StructField] =
      List(
        StructField("myInt", IntegerType, nullable = false),
        StructField("myString", StringType, nullable = false),
        StructField("myAppend1", IntegerType, nullable = false),
        StructField("myAppend2", StringType, nullable = false),
        StructField("myAppend3", StringType, nullable = false),
        StructField("myAppend4", StringType, nullable = false)
      )
    SparkSchemas.fields[Dummy, Append1, Append2, Append3, Append4] shouldBe fieldList
    SparkSchemas.schema[Dummy, Append1, Append2, Append3, Append4] shouldBe StructType(fieldList)
  }

}

