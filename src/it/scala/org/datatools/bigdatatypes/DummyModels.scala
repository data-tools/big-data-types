package org.datatools.bigdatatypes

object DummyModels {

  case class Simple(id: String, version: Int)

  case class BasicTypes(myInt: Int, myLong: Long, myFloat: BigDecimal, myBoolean: Boolean, myString: String)

  case class BasicOption(myString: String, myOptionalString: Option[String])

  case class BasicList(myInt: Int, myList: List[Int])

  case class BasicStruct(myInt: Int, myStruct: BasicTypes)

  case class Point(x: Int, y: Int)

  case class ListOfStruct(matrix: List[Point])

  case class TimestampType(id: String, version: Int)

  case class Append1(newField1: Int)

  case class Append2(newFieldTest: Int)

  case class Append3(newField3: Int)

  case class Append4(newField4: Int)


}
