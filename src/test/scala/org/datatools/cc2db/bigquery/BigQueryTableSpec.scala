package org.datatools.cc2db.bigquery

import org.datatools.cc2db.types.UnitSpec

class BigQueryTableSpec extends UnitSpec {

  behavior of "BigQueryTableSpec"

  val dataset = "tests"

  case class Simple(id: String, version: Int)

  "A Simple Case Class" should "create a table" in {
    BigQueryTable.createTable[Simple](dataset, "simple")
  }

}
