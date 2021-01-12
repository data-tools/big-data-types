package org.datatools.bigdatatypes.spark

import org.apache.spark.sql.types._
import org.datatools.bigdatatypes.TestTypes._
import org.datatools.bigdatatypes.UnitSpec
import org.datatools.bigdatatypes.formats.{DefaultFormats, Formats, SnakifyFormats}
import org.datatools.bigdatatypes.spark.SparkTypes._

class SparkTypesSpec extends UnitSpec {

  behavior of "SparkTypesSpec"
  implicit val defaultFormats: Formats = DefaultFormats

  case class Dummy(myInt: Int, myString: String)

  val expectedFields: Seq[StructField] =
    List(
      StructField("myInt", IntegerType, nullable = false),
      StructField("myString", StringType, nullable = false)
    )
  val expectedSchema: StructType = StructType(expectedFields)

  "A Case Class instance" should "return Spark Fields" in {
    val dummy = Dummy(1, "test")
    dummy.sparkSchema shouldBe expectedSchema
    dummy.sparkFields shouldBe expectedFields
  }

  "A Case Class type" should "return Spark Fields" in {
    SparkTypes[Dummy].sparkSchema shouldBe expectedSchema
    SparkTypes[Dummy].sparkFields shouldBe expectedFields
    SparkSchemas.schema[Dummy] shouldBe expectedSchema
    SparkSchemas.fields[Dummy] shouldBe expectedFields
  }

  "Basic types" should "create an Spark Schema" in {
    val fieldList: Seq[StructField] =
      List(
        StructField("myInt", IntegerType, nullable = false),
        StructField("myLong", LongType, nullable = false),
        StructField("myFloat", FloatType, nullable = false),
        StructField("myDecimal", DataTypes.createDecimalType, nullable = false),
        StructField("myBoolean", BooleanType, nullable = false),
        StructField("myString", StringType, nullable = false)
      )
    val expectedSchema: StructType = StructType(fieldList)
    SparkSchemas.fields[BasicTypes] shouldBe fieldList
    SparkSchemas.fields[BasicTypes] shouldBe expectedSchema
  }

  "Basic Optional types" should "create an Spark Schema" in {
    val fieldList: Seq[StructField] =
      List(
        StructField("myInt", IntegerType, nullable = true),
        StructField("myLong", LongType, nullable = true),
        StructField("myFloat", FloatType, nullable = true),
        StructField("myDecimal", DataTypes.createDecimalType, nullable = true),
        StructField("myBoolean", BooleanType, nullable = true),
        StructField("myString", StringType, nullable = true)
      )
    SparkSchemas.fields[BasicOptionTypes] shouldBe fieldList
  }

  "A List field" should "be converted into Spark Array type" in {
    val fieldList: Seq[StructField] =
      List(
        StructField("myInt", IntegerType, nullable = false),
        StructField("myList", ArrayType(IntegerType), nullable = true)
      )
    SparkSchemas.fields[BasicList] shouldBe fieldList
  }

  "Nested field" should "be converted into Spark Nested field" in {
    val fieldList: Seq[StructField] =
      List(
        StructField("myInt", IntegerType, nullable = false),
        StructField(
          "myStruct",
          StructType(
            List(
              StructField("myInt", IntegerType, nullable = false),
              StructField("myLong", LongType, nullable = false),
              StructField("myFloat", FloatType, nullable = false),
              StructField("myDecimal", DataTypes.createDecimalType, nullable = false),
              StructField("myBoolean", BooleanType, nullable = false),
              StructField("myString", StringType, nullable = false)
            )
          ),
          nullable = false
        )
      )
    SparkSchemas.fields[BasicStruct] shouldBe fieldList
  }
  "Optional Nested field" should "be converted into nullable Spark Nested field" in {
    val fieldList: Seq[StructField] =
      List(
        StructField("myInt", IntegerType, nullable = false),
        StructField(
          "myStruct",
          StructType(
            List(
              StructField("myInt", IntegerType, nullable = false),
              StructField("myLong", LongType, nullable = false),
              StructField("myFloat", FloatType, nullable = false),
              StructField("myDecimal", DataTypes.createDecimalType, nullable = false),
              StructField("myBoolean", BooleanType, nullable = false),
              StructField("myString", StringType, nullable = false)
            )
          ),
          nullable = true
        )
      )
    SparkSchemas.fields[BasicOptionalStruct] shouldBe fieldList
  }

  "List of nested objects (matrix)" should "be converted into Spark Nested Array" in {
    val fieldList: Seq[StructField] =
      List(
        StructField(
          "matrix",
          ArrayType(
            StructType(
              List(
                StructField("x", IntegerType, nullable = false),
                StructField("y", IntegerType, nullable = false)
              )
            )
          ),
          nullable = true
        )
      )
    SparkSchemas.fields[ListOfStruct] shouldBe fieldList
  }

  "Extended types" should "create an Spark Schema" in {
    val fieldList: Seq[StructField] =
      List(
        StructField("myInt", IntegerType, nullable = false),
        StructField("myTimestamp", TimestampType, nullable = false),
        StructField("myDate", DateType, nullable = false)
      )
    SparkSchemas.fields[ExtendedTypes] shouldBe fieldList
  }

  "Multiple Case Classes" should "create an Spark Schema with appended fields" in {
    case class Append1(myAppend1: Int)
    val fieldList: Seq[StructField] =
      List(
        StructField("myInt", IntegerType, nullable = false),
        StructField("myString", StringType, nullable = false),
        StructField("myAppend1", IntegerType, nullable = false)
      )
    SparkSchemas.fields[Dummy, Append1] shouldBe fieldList
  }

}

class SparkTypesSnakifiedSpec extends UnitSpec {

  behavior of "SparkTypesSnakifiedSpec"

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
    println(SparkSchemas.fields[Dummy])
    SparkSchemas.fields[Dummy] shouldBe expectedFields
  }
}
