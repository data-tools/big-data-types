package org.datatools.bigdatatypes.spark

import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.datatools.bigdatatypes.UnitSpec
import org.datatools.bigdatatypes.formats.Formats.implicitDefaultFormats
import org.datatools.bigdatatypes.types.basic.{Nullable, SqlInt, SqlString, SqlStruct}

class SqlTypeConversionSparkSpec extends UnitSpec {

  "Simple Spark DataType" should "be converted into SqlType" in {
    SqlTypeConversionSpark[IntegerType].getType shouldBe SqlInt()
  }

  "StructField" should "be converted into SqlType" in {
    val sf = StructField("myInt", IntegerType, nullable = false)
    SqlTypeConversionSpark(sf).getType shouldBe SqlInt()
  }

  "StructType" should "be converted into SqlStruct" in {
    val sf = StructField("myInt", IntegerType, nullable = false)
    val sf2 = StructField("myString", StringType, nullable = false)
    val st = StructType(List(sf, sf2))
    val expected = SqlStruct(List(("myInt", SqlInt()), ("myString", SqlString())), Nullable)
    SqlTypeConversionSpark(st).getType shouldBe expected
  }

  "SparkSchema from Case Class" should "be converted into SqlStruct" in {
    case class Dummy(myInt: Int, myString: String)
    val expected = SqlStruct(List(("myInt", SqlInt()), ("myString", SqlString())), Nullable)
    SqlTypeConversionSpark(SparkTypes[Dummy].sparkSchema).getType shouldBe expected
  }

}
