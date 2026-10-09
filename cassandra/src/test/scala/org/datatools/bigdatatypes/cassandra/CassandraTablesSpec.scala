package org.datatools.bigdatatypes.cassandra

import com.datastax.oss.driver.api.querybuilder.schema.CreateTable
import com.datastax.oss.driver.api.core.`type`.{DataType, DataTypes}
import org.datatools.bigdatatypes.TestTypes.BasicTypes
import org.datatools.bigdatatypes.UnitSpec
import org.datatools.bigdatatypes.basictypes.SqlType
import org.datatools.bigdatatypes.basictypes.SqlType._
import org.datatools.bigdatatypes.basictypes.SqlTypeMode.Required
import org.datatools.bigdatatypes.cassandra.CassandraTables.AsCassandraProductSyntax
import org.datatools.bigdatatypes.conversions.SqlTypeConversion
import org.datatools.bigdatatypes.formats.Formats.implicitDefaultFormats

class CassandraTablesSpec extends UnitSpec {

  "A CreateTable" should "be created from SqlType" in {
    val table: CreateTable = CassandraTables.table[BasicTypes]("TestTable", "myLong")
    table.toString shouldBe "CREATE TABLE testtable (myint int,mylong bigint PRIMARY KEY,myfloat float,mydouble double,mydecimal decimal,myboolean boolean,mystring text)"
  }

  it should "be created from SqlType instance" in {
    val sql: SqlType = SqlTypeConversion[BasicTypes].getType
    val table: CreateTable = CassandraTables.table[SqlType](sql, "TestTable", "myLong")
    table.toString shouldBe "CREATE TABLE testtable (myint int,mylong bigint PRIMARY KEY,myfloat float,mydouble double,mydecimal decimal,myboolean boolean,mystring text)"
  }

  it should "be created from case class instance using extension method" in {
    val instance = BasicTypes(1, 1, 1, 1, 1, myBoolean = true, "test")
    val table = instance.asCassandra("TestTable", "myLong")
    table.toString shouldBe "CREATE TABLE testtable (myint int,mylong bigint PRIMARY KEY,myfloat float,mydouble double,mydecimal decimal,myboolean boolean,mystring text)"
  }

  it should "create the primary key as Text when it is missing" in {
    val table: CreateTable = CassandraTables.table[BasicTypes]("TestTable", "unknownKey")
    table.toString should include("unknownkey text")
    table.toString should include("PRIMARY KEY")
  }

  it should "be created from tuple schema" in {
    import CassandraTypeConversion.cassandraTupleType
    val table: CreateTable =
      CassandraTables.table[(String, DataType)](("myLong", DataTypes.BIGINT), "TestTable", "myLong")
    table.toString shouldBe "CREATE TABLE testtable (mylong bigint PRIMARY KEY)"
  }

  it should "be created from instance using extension method" in {
    import CassandraTables.AsCassandraInstanceSyntax
    import CassandraTypeConversion.cassandraCreateTable
    val source: CreateTable = CassandraTables.table[BasicTypes]("TestTable", "myLong")
    val table = source.asCassandra("TestTable2", "myLong")
    table.toString should include("testtable2")
    table.toString should include("mylong bigint")
  }

  it should "convert a CreateTable back into SqlType" in {
    val table: CreateTable = CassandraTables.table[BasicTypes]("TestTable", "myLong")
    // CQL identifiers are lowercased by the driver
    val expected = SqlStruct(
      List(
        ("myint", SqlInt(Required)),
        ("mylong", SqlLong(Required)),
        ("myfloat", SqlFloat(Required)),
        ("mydouble", SqlDouble(Required)),
        ("mydecimal", SqlDecimal(Required)),
        ("myboolean", SqlBool(Required)),
        ("mystring", SqlString(Required))
      )
    )
    CassandraTypeConversion.cassandraCreateTable.getType(table) shouldBe expected
  }
}
