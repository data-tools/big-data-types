package org.datatools.bigdatatypes.cassandra

import com.datastax.oss.driver.api.querybuilder.schema.CreateTable
import org.datatools.bigdatatypes.TestTypes.BasicTypes
import org.datatools.bigdatatypes.{CassandraTestTypes, UnitSpec}

class CassandraTablesSpec extends UnitSpec {

  "A CreateTable" should "be created from SqlType" in {
    val table: CreateTable = CassandraTables.table[BasicTypes]("TestTable", "myLong")
    table shouldBe CassandraTestTypes.basicFields
  }
}
