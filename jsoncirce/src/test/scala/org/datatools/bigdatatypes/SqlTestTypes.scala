package org.datatools.bigdatatypes

import org.datatools.bigdatatypes.basictypes.*
import org.datatools.bigdatatypes.basictypes.SqlType.*
import org.datatools.bigdatatypes.basictypes.SqlTypeMode.*

import java.sql.{Date, Timestamp}

/** Case Classes and their SqlType representations
  * This should be used to test SqlTypeConversion and all reverse conversions from other modules
  */
object SqlTestTypes {

  case class BasicTypes(myInt: Int,
                        myLong: Long,
                        myFloat: Float,
                        myDouble: Double,
                        myDecimal: BigDecimal,
                        myBoolean: Boolean,
                        myString: String
  )

  case class BasicOptionTypes(myInt: Option[Int],
                              myLong: Option[Long],
                              myFloat: Option[Float],
                              myDouble: Option[Double],
                              myDecimal: Option[BigDecimal],
                              myBoolean: Option[Boolean],
                              myString: Option[String]
  )
  case class BasicOption(myString: String, myOptionalString: Option[String])
  case class BasicList(myInt: Int, myList: List[Int])
  case class BasicStruct(myInt: Int, myStruct: BasicTypes)
  case class BasicOptionalStruct(myInt: Int, myStruct: Option[BasicTypes])
  case class Point(x: Int, y: Int)
  case class ListOfStruct(matrix: List[Point])
  case class ExtendedTypes(myInt: Int, myTimestamp: Timestamp, myDate: Date)

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
