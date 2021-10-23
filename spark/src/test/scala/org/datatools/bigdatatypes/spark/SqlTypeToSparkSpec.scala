package org.datatools.bigdatatypes.spark

import org.apache.spark.sql.types.*
import org.datatools.bigdatatypes.TestTypes.*
import org.datatools.bigdatatypes.{SparkTestTypes, UnitSpec}
import org.datatools.bigdatatypes.formats.{DefaultFormats, Formats}
import org.datatools.bigdatatypes.spark.SqlTypeToSpark.*

/** All conversions from Case Class to Spark Schemas
  */
class SqlTypeToSparkSpec extends UnitSpec {

  implicit val defaultFormats: Formats = DefaultFormats

  behavior of "SparkTypesSpec for all TestTypes"

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

  behavior of "SparkTypesSpec for syntactic sugars"

  case class Dummy(myInt: Int, myString: String)

  val expectedFields: Seq[StructField] =
    List(
      StructField("myInt", IntegerType, nullable = false),
      StructField("myString", StringType, nullable = false)
    )
  val expectedSchema: StructType = StructType(expectedFields)

  "A Case Class instance" should "return Spark Fields" in {
    val dummy = Dummy(1, "test")
    dummy.asSparkSchema shouldBe expectedSchema
    dummy.asSparkFields shouldBe expectedFields
  }

  "A Case Class type" should "return Spark Fields" in {
    SqlTypeToSpark[Dummy].sparkSchema shouldBe expectedSchema
    SqlTypeToSpark[Dummy].sparkFields shouldBe expectedFields
    SparkSchemas.schema[Dummy] shouldBe expectedSchema
    SparkSchemas.fields[Dummy] shouldBe expectedFields
  }
}
