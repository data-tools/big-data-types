package org.datatools.bigdatatypes.bigquery

import org.datatools.bigdatatypes.types.UnitSpec

class BigQueryTableSpec extends UnitSpec {

  behavior of "BigQueryTableSpec"

  //TODO move tests that uses bigquery to Integration tests or mock a BigQuery
  val dataset = "github_actions_ci"

  case class Simple(id: String, version: Int)

  "An error" should "be returned in Left" in {
    BigQueryTable.createTable[Simple]("nonExistingDataset", "simple").isLeft shouldBe true
  }

  "A Simple Case Class" should "create a table" in {
    println(BigQueryTable.createTable[Simple](dataset, "simple").leftSide)
    BigQueryTable.createTable[Simple](dataset, "simple").isRight shouldBe true
  }

}
