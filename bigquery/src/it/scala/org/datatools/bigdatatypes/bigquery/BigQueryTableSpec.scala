package org.datatools.bigdatatypes.bigquery

import org.datatools.bigdatatypes.IntegrationSpec
import org.datatools.bigdatatypes.DummyModels._
import org.datatools.bigdatatypes.conversions.SqlTypeConversion
import org.datatools.bigdatatypes.formats.Formats.implicitSnakifyFormats
import org.datatools.bigdatatypes.types.basic.SqlType

import scala.util.Right

class BigQueryTableSpec extends IntegrationSpec {

  behavior of "BigQueryTableSpec"

  val dataset = "github_actions_ci"

  "An error" should "be returned in Left" in {
    BigQueryTable.createTable[Simple]("nonExistingDataset", "simple").isLeft shouldBe true
  }

  "A Simple Case Class" should "create a table" in {
    BigQueryTable.createTable[Simple](dataset, "simple").isRight shouldBe true
  }

  "An existing table" should "be returned instead of created" in {
    BigQueryTable.createTable[Simple](dataset, "existingTable").isRight shouldBe true
    BigQueryTable.createTable[Simple](dataset, "existingTable").isRight shouldBe true
  }

  "Basic types" should "create a table" in {
    BigQueryTable.createTable[BasicTypes](dataset, "basicTypes").isRight shouldBe true
  }

  "Basic Option" should "create a table" in {
    BigQueryTable.createTable[BasicOption](dataset, "basicOption").isRight shouldBe true
  }

  "Basic List (repeated)" should "create a table" in {
    BigQueryTable.createTable[BasicList](dataset, "basicList").isRight shouldBe true
  }

  "Basic Struct (nested fields)" should "create a table" in {
    BigQueryTable.createTable[BasicStruct](dataset, "basicStruct").isRight shouldBe true
  }

  "List of Structs (nested repeated fields)" should "create a table" in {
    BigQueryTable.createTable[ListOfStruct](dataset, "listOfStructs").isRight shouldBe true
  }

  "Two case classes" should "create a table" in {
    BigQueryTable.createTable[Simple, Append1](dataset, "simpleAppend1").isRight shouldBe true
  }

  "Three case classes" should "create a table" in {
    BigQueryTable.createTable[Simple, Append1, Append2](dataset, "simpleAppend2").isRight shouldBe true
  }

  "Four case classes" should "create a table" in {
    BigQueryTable.createTable[Simple, Append1, Append2, Append3](dataset, "simpleAppend3").isRight shouldBe true
  }

  "Five case classes" should "create a table" in {
    BigQueryTable
      .createTable[Simple, Append1, Append2, Append3, Append4](dataset, "simpleAppend4")
      .isRight shouldBe true
  }

  "Complex appends" should "create a table" in {
    BigQueryTable.createTable[BasicTypes, ListOfStruct](dataset, "simpleAppend5").isRight shouldBe true
  }

  "Java SQL Timestamp type" should "create a table" in {
    BigQueryTable.createTable[DummyTimestampTypes](dataset, "extendedTimestamp").isRight shouldBe true
  }

  "Time partitioned table with Timestamp Field" should "create a partitioned table" in {
    BigQueryTable
      .createTable[DummyTimestampTypes](dataset, "partitionedTimestamp", "my_timestamp")
      .isRight shouldBe true
  }

  "Time partitioned table with Date Field" should "create a partitioned table" in {
    BigQueryTable.createTable[DummyDateTypes](dataset, "partitionedDate", "my_date").isRight shouldBe true
  }

  "Time partitioned table with two case classes " should "create a partitioned table" in {
    BigQueryTable.createTable[Simple, DummyTimestampTypes](
      dataset,
      "partitionedTimestampAppend1",
      "my_timestamp"
    ) contains Right
  }
  "Time partitioned table with three case classes " should "create a partitioned table" in {
    BigQueryTable
      .createTable[Simple, Append1, DummyTimestampTypes](dataset, "partitionedTimestampAppend2", "my_timestamp")
      .isRight shouldBe true
  }
  "Time partitioned table with four case classes " should "create a partitioned table" in {
    BigQueryTable
      .createTable[Simple, Append1, Append2, DummyTimestampTypes](
        dataset,
        "partitionedTimestampAppend3",
        "my_timestamp"
      )
      .isRight shouldBe true
  }

  "Time partitioned table with five case classes " should "create a partitioned table" in {
    BigQueryTable
      .createTable[Simple, Append1, Append2, Append3, DummyTimestampTypes](
        dataset,
        "partitionedTimestampAppend4",
        "my_timestamp"
      )
      .isRight shouldBe true
  }

  "SqlType instance" should "create a BigQuery Table" in {
    val sql = SqlTypeConversion[BasicTypes].getType
    BigQueryTable.createTable(sql, dataset, "sqlType_table").isRight shouldBe true
  }
}
