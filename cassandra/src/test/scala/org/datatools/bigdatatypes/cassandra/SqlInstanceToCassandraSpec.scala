package org.datatools.bigdatatypes.cassandra

import org.datatools.bigdatatypes.TestTypes.BasicTypes
import org.datatools.bigdatatypes.basictypes.SqlType
import org.datatools.bigdatatypes.formats.{DefaultFormats, Formats}
import org.datatools.bigdatatypes.{CassandraTestTypes, UnitSpec}
import org.datatools.bigdatatypes.cassandra.SqlInstanceToCassandra.*
import org.datatools.bigdatatypes.conversions.SqlTypeConversion

class SqlInstanceToCassandraSpec extends UnitSpec {

  implicit val defaultFormats: Formats = DefaultFormats

  behavior of "SparkTypesSpec for all TestTypes"

  "SqlTypeToCassandra" should "create Cassandra tuples" in {
    SqlTypeToCassandra[BasicTypes].cassandraFields shouldBe CassandraTestTypes.basicFields
  }

  "SqlType instance" should "be converted into Cassandra tuples" in {
    val sql = SqlTypeConversion[BasicTypes].getType
    SqlInstanceToCassandra[SqlType].cassandraFields(sql) shouldBe CassandraTestTypes.basicFields
  }

  it should "be converted into Cassandra tuples using extension method" in {
    val sql = SqlTypeConversion[BasicTypes].getType
    sql.asCassandra shouldBe CassandraTestTypes.basicFields
  }



}
