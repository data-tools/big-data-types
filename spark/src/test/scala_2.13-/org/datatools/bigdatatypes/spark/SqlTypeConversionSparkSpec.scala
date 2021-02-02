package org.datatools.bigdatatypes.spark

import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.datatools.bigdatatypes.UnitSpec
import org.datatools.bigdatatypes.formats.Formats.implicitDefaultFormats

class SqlTypeConversionSparkSpec extends UnitSpec {

  "test" should "test" in {

    case class Dummy(myInt: Int, myString: String)
    println(SqlTypeConversionSpark[IntegerType].getType)
    val sf = StructField("myString", StringType, nullable = false)
    val sf2 = StructField("myInt", IntegerType, nullable = true)
    val st = StructType(List(sf))
    val st2 = StructType(List(sf, sf))
    val st3 = StructType(List(sf, sf2))
    println(SqlTypeConversionSpark[IntegerType].getType)
    println(SqlTypeConversionSpark(sf).getType)
    println(SqlTypeConversionSpark(st).getType)
    println(SqlTypeConversionSpark(st2).getType)
    println(SqlTypeConversionSpark(st3).getType)
    println(SqlTypeConversionSpark(SparkTypes[Dummy].sparkSchema).getType)
  }

}
