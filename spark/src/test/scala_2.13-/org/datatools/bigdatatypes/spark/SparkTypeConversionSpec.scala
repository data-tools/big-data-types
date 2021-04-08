package org.datatools.bigdatatypes.spark

import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.datatools.bigdatatypes.TestTypes._
import org.datatools.bigdatatypes.UnitSpec
import org.datatools.bigdatatypes.basictypes.SqlType
import org.datatools.bigdatatypes.conversions.{SqlInstanceConversion, SqlTypeConversion}
import org.datatools.bigdatatypes.formats.Formats.implicitDefaultFormats
import org.datatools.bigdatatypes.spark.SparkTypeConversion._

/** Reverse conversion, from Spark types to [[SqlType]]s
  */
class SparkTypeConversionSpec extends UnitSpec {

  "Simple Spark DataType" should "be converted into SqlType" in {
    SqlTypeConversion[IntegerType].getType shouldBe SqlInt()
  }

  "StructField nullable" should "be converted into Nullable SqlType" in {
    val sf = StructField("myInt", IntegerType, nullable = true)
    sf.getType shouldBe SqlInt(Nullable)
    SqlInstanceConversion[StructField].getType(sf) shouldBe SqlInt(Nullable)
  }

  "StructField required" should "be converted into Required SqlType" in {
    val sf = StructField("myInt", IntegerType, nullable = false)
    sf.getType shouldBe SqlInt()
    SqlInstanceConversion[StructField].getType(sf) shouldBe SqlInt(Required)
  }

  "StructType" should "be converted into SqlStruct" in {
    val sf = StructField("myInt", IntegerType, nullable = false)
    val sf2 = StructField("myString", StringType, nullable = true)
    val st = StructType(List(sf, sf2))
    val expected = SqlStruct(List(("myInt", SqlInt()), ("myString", SqlString(Nullable))))
    SqlInstanceConversion[StructType].getType(st) shouldBe expected
    st.getType shouldBe expected
  }

  behavior of "Common Test Types for Spark"

  "Spark Schema with Option" should "be converted into SqlTypes with nullable" in {
    val sqlType: SqlType = SqlTypeToSpark[BasicOption].sparkSchema.getType
    sqlType shouldBe basicOption
  }

  "basic Spark Schema" should "be converted into SqlTypes" in {
    val sqlType: SqlType = SqlTypeToSpark[BasicTypes].sparkSchema.getType
    sqlType shouldBe basicTypes
  }

  "Spark Schema with basic options types" should "be converted into nullable SqlTypes" in {
    val sqlType: SqlType = SqlTypeToSpark[BasicOptionTypes].sparkSchema.getType
    sqlType shouldBe basicOptionTypes
  }

  "Spark Schema with List" should "be converted into Repeated type" in {
    val sqlType: SqlType = SqlTypeToSpark[BasicList].sparkSchema.getType
    sqlType shouldBe basicWithList
  }

  "Spark Schema with nested object" should "be converted into SqlTypes" in {
    val sqlType: SqlType = SqlTypeToSpark[BasicStruct].sparkSchema.getType
    sqlType shouldBe basicNested
  }

  "Spark Schema with optional nested object" should "be converted into SqlTypes" in {
    val sqlType: SqlType = SqlTypeToSpark[BasicOptionalStruct].sparkSchema.getType
    sqlType shouldBe basicOptionalNested
  }

  "Spark Schema with Struct List" should "be converted into Repeated Struct type" in {
    val sqlType: SqlType = SqlTypeToSpark[ListOfStruct].sparkSchema.getType
    sqlType shouldBe basicNestedWithList
  }

  "Spark Schema with extended types" should "be converted into Struct with extended types" in {
    val sqlType: SqlType = SqlTypeToSpark[ExtendedTypes].sparkSchema.getType
    sqlType shouldBe extendedTypes
  }

}
