package org.datatools.bigdatatypes.bigquery

import com.google.cloud.bigquery.{FieldList, StandardTableDefinition}
import org.datatools.bigdatatypes.UnitSpec
import org.datatools.bigdatatypes.bigquery.BigQueryFields.getFieldNames


class BigQueryDefinitionsSpec extends UnitSpec {

  behavior of "BigQueryDefinitionsSpec"

  case class Simple(id: String, number: Int)
  case class CamelCase(myId: String, myNumber: Int)


  "Simple definition without partition" should "generate a Table Definition" in {
    //implicit val f: Formats = DefaultFormats
    import org.datatools.bigdatatypes.formats.Formats.implicitDefaultFormats
    val table: StandardTableDefinition = BigQueryDefinitions.generateTableDefinition[Simple](None)
    val names: List[String] = getFieldNames(table.getSchema.getFields)
    names should contain.only("id", "number")
  }

  "Keys" should "remain equal with identity" in {
    import org.datatools.bigdatatypes.formats.Formats.implicitDefaultFormats
    val table: StandardTableDefinition = BigQueryDefinitions.generateTableDefinition[CamelCase](None)
    val names: List[String] = getFieldNames(table.getSchema.getFields)
    names should contain.only("myId", "myNumber")
  }

  "Keys" should "be snakified" in {
    import org.datatools.bigdatatypes.formats.Formats.implicitSnakifyFormats
    val table: StandardTableDefinition = BigQueryDefinitions.generateTableDefinition[CamelCase](None)
    val names: List[String] = getFieldNames(table.getSchema.getFields)
    names should contain.only("my_id", "my_number")
  }

  it should "generateTimePartitionColumn" in {}

  it should "addPartitionToBuilder" in {}

  it should "generateSchema" in {}

}
