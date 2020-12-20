package org.datatools.bigdatatypes.bigquery

import com.google.cloud.bigquery.{Field, FieldList, StandardTableDefinition}
import org.datatools.bigdatatypes.UnitSpec
import org.datatools.bigdatatypes.bigquery.BigQueryDefinitions

class BigQueryDefinitionsSpec extends UnitSpec {

  behavior of "BigQueryDefinitionsSpec"

  case class Simple(id: String, number: Int)


  "Simple definition without partition" should "generate a Table Definition" in {
    val test: StandardTableDefinition = BigQueryDefinitions.generateTableDefinition[Simple](None)
    //val test2: Seq[Field] = test.getSchema.getFields.toArray().toList
    //val test3 = test2.map(_.getName)
    //test3 should contain only("id", "number")
  }

  it should "generateTimePartitionColumn" in {}

  it should "addPartitionToBuilder" in {}

  it should "generateSchema" in {}

}
