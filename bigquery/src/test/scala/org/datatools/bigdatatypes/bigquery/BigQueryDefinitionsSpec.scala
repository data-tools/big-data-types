package org.datatools.bigdatatypes.bigquery

import com.google.cloud.bigquery.{Schema, TimePartitioning}
import org.datatools.bigdatatypes.TestTypes.BasicTypes
import org.datatools.bigdatatypes.UnitSpec
import org.datatools.bigdatatypes.bigquery.BigQueryDefinitions.{
  generateTableDefinition,
  generateTimePartitionColumn
}
import org.datatools.bigdatatypes.bigquery.JavaConverters.toJava
import org.datatools.bigdatatypes.formats.Formats.implicitDefaultFormats

class BigQueryDefinitionsSpec extends UnitSpec {

  private val schema: Schema = Schema.of(toJava(SqlTypeToBigQuery[BasicTypes].bigQueryFields))

  "generateTimePartitionColumn" should "create a DAY partition on the column" in {
    val partition = generateTimePartitionColumn("myDate")
    partition.getField shouldBe "myDate"
    partition.getType shouldBe TimePartitioning.Type.DAY
  }

  "generateTableDefinition" should "keep the schema without partition" in {
    generateTableDefinition(schema, None).getSchema shouldBe schema
  }

  it should "add the partition column when defined" in {
    val definition = generateTableDefinition(schema, Some("myDate"))
    definition.getSchema shouldBe schema
    definition.getTimePartitioning.getField shouldBe "myDate"
  }

}
