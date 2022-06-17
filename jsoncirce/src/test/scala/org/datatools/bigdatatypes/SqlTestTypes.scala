package org.datatools.bigdatatypes

import org.datatools.bigdatatypes.basictypes.*
import org.datatools.bigdatatypes.basictypes.SqlType.*
import org.datatools.bigdatatypes.basictypes.SqlTypeMode.*

import java.sql.{Date, Timestamp}

/** Case Classes and their SqlType representations
  * This should be used to test SqlTypeConversion and all reverse conversions from other modules
  */
object SqlTestTypes {

  /** Used for case classes, nested or others */
  val basicFields: List[(String, SqlType)] =
    List(
      ("myInt", SqlDecimal(Required)),
      ("myLong", SqlDecimal(Required)),
      ("myFloat", SqlDecimal(Required)),
      ("myDouble", SqlDecimal(Required)),
      ("myDecimal", SqlDecimal(Required)),
      ("myBoolean", SqlBool(Required)),
      ("myString", SqlString(Required))
    )

  val basicOption: SqlStruct = SqlStruct(
    List(
      ("myString", SqlString(Required)),
      ("myOptionalString", SqlString(Nullable))
    )
  )

  val basicTypes: SqlStruct = SqlStruct(basicFields)

  val basicOptionTypes: SqlStruct = SqlStruct(
    List(
      ("myInt", SqlDecimal(Nullable)),
      ("myLong", SqlDecimal(Nullable)),
      ("myFloat", SqlDecimal(Nullable)),
      ("myDouble", SqlDecimal(Nullable)),
      ("myDecimal", SqlDecimal(Nullable)),
      ("myBoolean", SqlBool(Nullable)),
      ("myString", SqlString(Nullable))
    )
  )

  val basicWithList: SqlStruct = SqlStruct(
    List(
      ("myInt", SqlDecimal(Required)),
      ("myList", SqlDecimal(Repeated))
    )
  )

  val basicNested: SqlStruct = SqlStruct(
    List(
      ("myInt", SqlDecimal(Required)),
      ("myStruct", SqlStruct(basicFields, Required))
    )
  )

  val basicOptionalNested: SqlStruct = SqlStruct(
    List(
      ("myInt", SqlDecimal(Required)),
      ("myStruct", SqlStruct(basicFields, Nullable))
    )
  )

  val basicNestedWithList: SqlStruct = SqlStruct(
    List(
      (
        "matrix",
        SqlStruct(
          List(
            ("x", SqlDecimal(Required)),
            ("y", SqlDecimal(Required))
          ),
          Repeated
        )
      )
    )
  )

  val extendedTypes: SqlStruct = SqlStruct(
    List(
      ("myInt", SqlDecimal(Required)),
      ("myTimestamp", SqlTimestamp(Required)),
      ("myDate", SqlDate(Required))
    )
  )
}
