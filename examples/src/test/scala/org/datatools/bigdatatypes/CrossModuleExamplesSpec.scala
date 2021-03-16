package org.datatools.bigdatatypes

import org.datatools.bigdatatypes.TestTypes.BasicTypes
import org.datatools.bigdatatypes.bigquery.SqlTypeToBigQuery
import org.datatools.bigdatatypes.conversions.SqlTypeConversion
import org.datatools.bigdatatypes.formats.Formats.implicitDefaultFormats
import org.datatools.bigdatatypes.types.basic.SqlType

class CrossModuleExamplesSpec extends UnitSpec {

  behavior of "SqlToBigQuerySpec"

  "Case Class" should "be converted into BigQuery Fields" in {
    val bq = SqlTypeToBigQuery[BasicTypes].bigQueryFields
    bq shouldBe BigQueryTestTypes.basicFields
  }



}
