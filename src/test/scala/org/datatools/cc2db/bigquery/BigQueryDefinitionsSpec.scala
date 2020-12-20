package org.datatools.cc2db.bigquery

import com.google.cloud.bigquery.{FieldList, StandardTableDefinition}
import org.datatools.cc2db.bigquery.BigQueryDefinitions
import org.datatools.cc2db.types.UnitSpec

class BigQueryDefinitionsSpec extends UnitSpec {

  behavior of "BigQueryDefinitionsSpec"

  case class Simple(id: String, number: Int)

  "Simple definition without partition" should "generate a Table Definition" in {
    val test: StandardTableDefinition = BigQueryDefinitions.generateTableDefinition[Simple](None)
    val test2: FieldList = test.getSchema.getFields
    val test3 = test2.forEach(_.getName)
    //should contain only("id", "number")
  }

  it should "generateTimePartitionColumn" in {}

  it should "addPartitionToBuilder" in {}

  it should "generateSchema" in {}

}
