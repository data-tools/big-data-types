package org.datatools.cc2db.bigquery

import org.datatools.cc2db.types.UnitSpec

class BigQueryTableSpec extends UnitSpec {

  behavior of "BigQueryTableSpec"

  //TODO move tests that uses bigquery to Integration tests or mock a BigQuery
  val dataset = "integration_tests_bigquery"

  case class Simple(id: String, version: Int)

  "An error" should "be returned in Left" in {
    BigQueryTable.createTable[Simple]("nonExistingDataset", "simple").isLeft shouldBe true
  }

  "A Simple Case Class" should "create a table" in {
    BigQueryTable.createTable[Simple](dataset, "simple").isRight shouldBe true
  }

}
