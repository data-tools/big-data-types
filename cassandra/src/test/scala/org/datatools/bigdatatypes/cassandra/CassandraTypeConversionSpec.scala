package org.datatools.bigdatatypes.cassandra

import com.datastax.oss.driver.api.core.`type`.{DataType, DataTypes}
import org.datatools.bigdatatypes.TestTypes.*
import org.datatools.bigdatatypes.basictypes.*
import org.datatools.bigdatatypes.basictypes.SqlType.*
import org.datatools.bigdatatypes.cassandra.CassandraTypeConversion.*
import org.datatools.bigdatatypes.conversions.{SqlInstanceConversion, SqlTypeConversion}
import org.datatools.bigdatatypes.{CassandraTestTypes, UnitSpec}

/** Reverse conversion, from Cassandra types to [[SqlType]]s
  */
class CassandraTypeConversionSpec extends UnitSpec {

  behavior of "Common Test Types for Spark"

  "Simple Cassandra DataType" should "be converted into SqlType" in {
    SqlTypeConversion[DataTypes.INT.type].getType shouldBe SqlInt()
  }

  "Cassandra Tuple" should "be converted into SqlType" in {
    val t = ("myInt", DataTypes.INT)
    t.asSqlType shouldBe SqlInt()
    SqlInstanceConversion[(String, DataType)].getType(t) shouldBe SqlInt()
  }

  behavior of "Common Test Types for Spark"

  "basic Cassandra schema" should "be converted into SqlTypes" in {
    CassandraTestTypes.basicFields.asSqlType shouldBe SqlTypeConversion[BasicTypes].getType
  }

  "Cassandra schema with extended types" should "be converted into Struct with extended types" in {
    CassandraTestTypes.extendedTypes.asSqlType shouldBe SqlTypeConversion[ExtendedTypes].getType
  }

  "Cassandra schema with Repeated types" should "be converted into Struct with repeated types" in {
    CassandraTestTypes.basicWithList.asSqlType
  }

}
