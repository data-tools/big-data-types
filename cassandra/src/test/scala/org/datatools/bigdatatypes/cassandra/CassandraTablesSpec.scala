package org.datatools.bigdatatypes.cassandra

import com.datastax.oss.driver.api.querybuilder.schema.CreateTable
import org.datatools.bigdatatypes.TestTypes.BasicTypes
import org.datatools.bigdatatypes.formats.Formats.implicitDefaultFormats
import org.datatools.bigdatatypes.{CassandraTestTypes, UnitSpec}

import java.util

class CassandraTablesSpec extends UnitSpec {

  "A CreateTable" should "be created from SqlType" in {
    val table: CreateTable = CassandraTables.table[BasicTypes]("TestTable", "myLong")
    table.toString shouldBe "CREATE TABLE testtable (myint int,mylong bigint PRIMARY KEY,myfloat float,mydouble double,mydecimal decimal,myboolean boolean,mystring text)"
  }
}
