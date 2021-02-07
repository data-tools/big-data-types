package org.datatools.bigdatatypes

import java.sql.{Date, Timestamp}

import org.datatools.bigdatatypes.types.basic.{
  Nullable,
  Repeated,
  Required,
  SqlBool,
  SqlDate,
  SqlDecimal,
  SqlFloat,
  SqlInt,
  SqlLong,
  SqlString,
  SqlStruct,
  SqlTimestamp,
  SqlType
}

/** Case Classes and their SqlType representations
  * This should be used to test SqlTypeConversion and all reverse conversions from other modules
  */
object TestTypes {

  case class BasicTypes(myInt: Int,
                        myLong: Long,
                        myFloat: Float,
                        myDecimal: BigDecimal,
                        myBoolean: Boolean,
                        myString: String
  )

  case class BasicOptionTypes(myInt: Option[Int],
                              myLong: Option[Long],
                              myFloat: Option[Float],
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
      ("myInt", SqlInt(Required)),
      ("myLong", SqlLong(Required)),
      ("myFloat", SqlFloat(Required)),
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
      ("myInt", SqlInt(Nullable)),
      ("myLong", SqlLong(Nullable)),
      ("myFloat", SqlFloat(Nullable)),
      ("myDecimal", SqlDecimal(Nullable)),
      ("myBoolean", SqlBool(Nullable)),
      ("myString", SqlString(Nullable))
    )
  )

  val basicWithList: SqlStruct = SqlStruct(
    List(
      ("myInt", SqlInt(Required)),
      ("myList", SqlInt(Repeated))
    )
  )

  val basicNested: SqlStruct = SqlStruct(
    List(
      ("myInt", SqlInt(Required)),
      ("myStruct", SqlStruct(basicFields, Required))
    )
  )

  val basicOptionalNested: SqlStruct = SqlStruct(
    List(
      ("myInt", SqlInt(Required)),
      ("myStruct", SqlStruct(basicFields, Nullable))
    )
  )

  val basicNestedWithList: SqlStruct = SqlStruct(
    List(
      (
        "matrix",
        SqlStruct(
          List(
            ("x", SqlInt(Required)),
            ("y", SqlInt(Required))
          ),
          Repeated
        )
      )
    )
  )

  val extendedTypes: SqlStruct = SqlStruct(
    List(
      ("myInt", SqlInt(Required)),
      ("myTimestamp", SqlTimestamp(Required)),
      ("myDate", SqlDate(Required))
    )
  )
}
