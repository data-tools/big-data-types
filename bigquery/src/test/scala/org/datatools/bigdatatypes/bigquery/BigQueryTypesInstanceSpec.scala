package org.datatools.bigdatatypes.bigquery

import com.google.cloud.bigquery.Field
import org.datatools.bigdatatypes.TestTypes._
import org.datatools.bigdatatypes.conversions.SqlTypeConversion
import org.datatools.bigdatatypes.conversions.SqlTypeConversion._
import org.datatools.bigdatatypes.formats.Formats.implicitDefaultFormats
import org.datatools.bigdatatypes.types.basic.SqlType
import org.datatools.bigdatatypes.{BigQueryTestTypes, UnitSpec}

/** These tests defines how to convert an SqlType instance into BigQueryFields
  * Not all the possible `TestTypes` are tested as they use the same code tested in [[BigQueryTypesSpec]]
  * Cross tests examples in Example module should define how to transform other types into BigQueryFields
  */
class BigQueryTypesInstanceSpec extends UnitSpec {

  behavior of "BigQueryTypesInstanceSpec"

  "basic case class" should "be converted into BigQueryFields" in {
    val sql: SqlType = SqlTypeConversion[BasicTypes].getType
    val fields: Seq[Field] = BigQueryTypesInstance[SqlType].bigQueryFields(sql)
    fields shouldBe BigQueryTestTypes.basicTypes
  }

  "Case Class with Option" should "be converted into BigQueryFields with nullable" in {
    val sql: SqlType = SqlTypeConversion[BasicOption].getType
    val fields: Seq[Field] = BigQueryTypesInstance[SqlType].bigQueryFields(sql)
    fields shouldBe BigQueryTestTypes.basicOption
  }

  "Case class with Struct List" should "be converted into Repeated BigQueryFields" in {
    val sql: SqlType = SqlTypeConversion[ListOfStruct].getType
    val fields: Seq[Field] = BigQueryTypesInstance[SqlType].bigQueryFields(sql)
    fields shouldBe BigQueryTestTypes.basicNestedWithList
  }
}
