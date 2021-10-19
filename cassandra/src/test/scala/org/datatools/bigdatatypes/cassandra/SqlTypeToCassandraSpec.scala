package org.datatools.bigdatatypes.cassandra

import com.datastax.oss.driver.api.querybuilder.schema.CreateTable
import org.datatools.bigdatatypes.TestTypes.{BasicList, BasicOptionTypes, BasicTypes, ExtendedTypes}
import org.datatools.bigdatatypes.cassandra.SqlTypeToCassandra.AsCassandraSyntax
import org.datatools.bigdatatypes.{CassandraTestTypes, UnitSpec}
import org.datatools.bigdatatypes.formats.{DefaultFormats, Formats}

class SqlTypeToCassandraSpec extends UnitSpec {

  implicit val defaultFormats: Formats = DefaultFormats

  behavior of "SparkTypesSpec for all TestTypes"

  "Basic types" should "create Cassandra tuples" in {
    SqlTypeToCassandra[BasicTypes].cassandraFields shouldBe CassandraTestTypes.basicFields
  }

  "Basic Optional types" should "create an Cassandra tuples" in {
    SqlTypeToCassandra[BasicOptionTypes].cassandraFields shouldBe CassandraTestTypes.basicFields
  }

  "A List field" should "be converted into Cassandra repeated" in {
    SqlTypeToCassandra[BasicList].cassandraFields shouldBe CassandraTestTypes.basicWithList
  }

  "Extended types" should "create a Cassandra Schema" in {
    SqlTypeToCassandra[ExtendedTypes].cassandraFields shouldBe CassandraTestTypes.extendedTypes
  }

  "A Product instance" should "return a Cassandra Schema" in {
    val instance = BasicTypes(1, 1, 1, 1, 1, myBoolean = true, "test")
    instance.asCassandra shouldBe CassandraTestTypes.basicFields
  }

}
