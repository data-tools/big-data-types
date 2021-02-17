package org.datatools.bigdatatypes.spark

import org.apache.spark.sql.types._
import org.datatools.bigdatatypes.TestTypes._
import org.datatools.bigdatatypes.{SparkTestTypes, UnitSpec}
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
    SparkSchemas.fields[BasicTypes] shouldBe SparkTestTypes.basicFields
    SparkSchemas.fields[BasicTypes] shouldBe SparkTestTypes.basicTypes
  }

  "Basic Optional types" should "create an Spark Schema" in {
    SparkSchemas.fields[BasicOptionTypes] shouldBe SparkTestTypes.basicOptionTypes
  }

  "A List field" should "be converted into Spark Array type" in {
    SparkSchemas.fields[BasicList] shouldBe SparkTestTypes.basicWithList
  }

  "Nested field" should "be converted into Spark Nested field" in {
    SparkSchemas.fields[BasicStruct] shouldBe SparkTestTypes.basicNested
  }

  "Optional Nested field" should "be converted into nullable Spark Nested field" in {
    SparkSchemas.fields[BasicOptionalStruct] shouldBe SparkTestTypes.basicOptionalNested
  }

  "List of nested objects (matrix)" should "be converted into Spark Nested Array" in {
    SparkSchemas.fields[ListOfStruct] shouldBe SparkTestTypes.basicNestedWithList
  }

  "Extended types" should "create an Spark Schema" in {
    SparkSchemas.fields[ExtendedTypes] shouldBe SparkTestTypes.extendedTypes
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
