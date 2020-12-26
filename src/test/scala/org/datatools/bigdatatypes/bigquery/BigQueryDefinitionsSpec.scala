package org.datatools.bigdatatypes.bigquery

import com.google.cloud.bigquery.{FieldList, StandardTableDefinition}
import org.datatools.bigdatatypes.UnitSpec
import org.datatools.bigdatatypes.bigquery.BigQueryFields.getFieldNames


class BigQueryDefinitionsSpec extends UnitSpec {

  behavior of "BigQueryDefinitionsSpec"

  case class Simple(id: String, number: Int)


  "Simple definition without partition" should "generate a Table Definition" in {
    val table: StandardTableDefinition = BigQueryDefinitions.generateTableDefinition[Simple](None)
    val names: List[String] = getFieldNames(table.getSchema.getFields)
    names should contain.only("id", "number")
  }

  it should "generateTimePartitionColumn" in {}

  it should "addPartitionToBuilder" in {}

  it should "generateSchema" in {}

}
