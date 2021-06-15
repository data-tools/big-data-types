package org.datatools.bigdatatypes.cassandra

import org.datatools.bigdatatypes.TestTypes.BasicTypes
import org.datatools.bigdatatypes.basictypes.SqlType
import org.datatools.bigdatatypes.formats.{DefaultFormats, Formats}
import org.datatools.bigdatatypes.{CassandraTestTypes, UnitSpec}
import org.datatools.bigdatatypes.cassandra.SqlInstanceToCassandra._
import org.datatools.bigdatatypes.conversions.SqlTypeConversion

class SqlInstanceToCassandraSpec extends UnitSpec {

  implicit val defaultFormats: Formats = DefaultFormats

  behavior of "SparkTypesSpec for all TestTypes"

  "Extension method for Cassandra fields" should "create Cassandra tuples" in {
    val sql = SqlTypeToCassandra[BasicTypes]
    sql.cassandraFields shouldBe CassandraTestTypes.basicFields
  }

  "SqlType instance" should "be converted into Cassandra tuples" in {
    val sql = SqlTypeConversion[BasicTypes].getType
    SqlInstanceToCassandra[SqlType].cassandraFields(sql) shouldBe CassandraTestTypes.basicFields
  }

}
