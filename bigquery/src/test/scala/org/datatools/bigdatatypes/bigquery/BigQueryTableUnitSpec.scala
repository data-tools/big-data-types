package org.datatools.bigdatatypes.bigquery

import org.datatools.bigdatatypes.DummyModels.BasicTypes
import org.datatools.bigdatatypes.UnitSpec
import org.datatools.bigdatatypes.conversions.SqlTypeConversion
import org.datatools.bigdatatypes.formats.Formats.implicitDefaultFormats
import org.datatools.bigdatatypes.types.basic.SqlType

class BigQueryTableUnitSpec extends UnitSpec {

  "TryTable" should "fail without Service Account" in {
    val sql = SqlTypeConversion[BasicTypes].getType
    BigQueryTable.createTable(sql, "test", "sqlType_table").isLeft shouldBe true
  }

}
