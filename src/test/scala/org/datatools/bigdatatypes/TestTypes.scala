package org.datatools.bigdatatypes

import java.sql.{Date, Timestamp}

object TestTypes {

  case class BasicTypes(myInt: Int, myLong: Long, myFloat: BigDecimal, myBoolean: Boolean, myString: String)
  case class BasicOptionTypes(myInt: Option[Int],
                              myLong: Option[Long],
                              myFloat: Option[BigDecimal],
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
}
