package org.datatools.bigdatatypes

import com.datastax.oss.driver.api.core.`type`.{DataType, DataTypes}

object CassandraTestTypes {

  val basicFields: Seq[(String, DataType)] =
    List(
      ("myInt", DataTypes.INT),
      ("myLong", DataTypes.BIGINT),
      ("myFloat", DataTypes.FLOAT),
      ("myDouble", DataTypes.DOUBLE),
      ("myDecimal", DataTypes.DECIMAL),
      ("myBoolean", DataTypes.BOOLEAN),
      ("myString", DataTypes.TEXT)
    )

}
