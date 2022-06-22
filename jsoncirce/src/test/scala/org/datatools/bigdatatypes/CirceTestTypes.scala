package org.datatools.bigdatatypes

import io.circe.Json

/** Test types from [[TestTypes]] converted to Circe. They can be used in multiple tests
  */
object CirceTestTypes {

  val basicFields: Seq[(String, Json)] =
    List(
      ("myInt", Json.fromInt(1)),
      ("myLong", Json.fromLong(1)),
      ("myFloat", Json.fromFloat(1).get),
      ("myDouble", Json.fromDouble(1).get),
      ("myDecimal", Json.fromBigDecimal(1)),
      ("myBoolean", Json.fromBoolean(true)),
      ("myString", Json.fromString(""))
    )

  val basicTypes: Json = Json.fromFields(basicFields)

  val basicWithList: Json = Json.fromFields(
    List(
      ("myInt", Json.fromInt(1)),
      (
        "myList",
        Json.fromValues(
          List(
            Json.fromInt(1)
          )
        )
      )
    )
  )

  val basicNested: Json = Json.fromFields(
    List(
      ("myInt", Json.fromInt(1)),
      ("myStruct", Json.fromFields(basicFields))
    )
  )

  val basicNestedWithList: Json = Json.fromFields(
    List(
      (
        "matrix",
        Json.fromValues(
          List(
            Json.fromFields(
              List(
                ("x", Json.fromInt(1)),
                ("y", Json.fromInt(1))
              )
            )
          )
        )
      )
    )
  )

}
