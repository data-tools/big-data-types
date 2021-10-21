package org.datatools.bigdatatypes.cassandra

import com.datastax.oss.driver.api.querybuilder.schema.CreateTable
import org.datatools.bigdatatypes.TestTypes.BasicTypes
import org.datatools.bigdatatypes.UnitSpec
import org.datatools.bigdatatypes.basictypes.SqlType
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
}
