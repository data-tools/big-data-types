package org.datatools.bigdatatypes.cassandra

import com.datastax.oss.driver.api.core.`type`.DataTypes
import com.datastax.oss.driver.api.querybuilder.schema.CreateTable
import org.datatools.bigdatatypes.TestTypes.*
import org.datatools.bigdatatypes.basictypes.*
import org.datatools.bigdatatypes.basictypes.SqlType.*
import org.datatools.bigdatatypes.cassandra.CassandraTypeConversion.*
import org.datatools.bigdatatypes.conversions.SqlTypeConversion
import org.datatools.bigdatatypes.formats.Formats
import org.datatools.bigdatatypes.formats.Formats.implicitDefaultFormats
import org.datatools.bigdatatypes.{CassandraTestTypes, UnitSpec}

/** Reverse conversion, from Cassandra types to [[SqlType]]s
  */
class CassandraTypeConversionSpec extends UnitSpec {

  behavior of "Common Test Types for Cassandra"

  "Simple Cassandra DataType" should "be converted into SqlType" in {
    SqlTypeConversion[DataTypes.INT.type].getType shouldBe SqlInt()
  }

  "Cassandra Tuple" should "be converted into SqlType" in {
    val t = ("myInt", DataTypes.INT)
    t.asSqlType shouldBe SqlStruct(List(("myInt", SqlInt())))
  }

  "basic Cassandra schema" should "be converted into SqlTypes" in {
    CassandraTestTypes.basicFields.asSqlType shouldBe SqlTypeConversion[BasicTypes].getType
  }

  "Cassandra schema with extended types" should "be converted into Struct with extended types" in {
    CassandraTestTypes.extendedTypes.asSqlType shouldBe SqlTypeConversion[ExtendedTypes].getType
  }

  "Cassandra schema with Repeated types" should "be converted into Struct with repeated types" in {
    CassandraTestTypes.basicWithList.asSqlType
  }

  "Cassandra Create Table" should "be converted into SqlTypes, using extension method" in {
    val table: CreateTable = CassandraTables.table[BasicTypes]("TestTable", "myLong")
    table.asSqlType shouldBe SqlStruct(
      List(
        ("myint", SqlInt()),
        ("mylong", SqlLong()),
        ("myfloat", SqlFloat()),
        ("mydouble", SqlDouble()),
        ("mydecimal", SqlDecimal()),
        ("myboolean", SqlBool()),
        ("mystring", SqlString())
      )
    )
  }

}
