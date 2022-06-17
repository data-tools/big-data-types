package org.datatools.bigdatatypes

import com.google.cloud.bigquery.Field.Mode
import com.google.cloud.bigquery.{Field, Schema, StandardSQLTypeName}
import io.circe.Json
import org.datatools.bigdatatypes.bigquery.JavaConverters.toJava
import org.datatools.bigdatatypes.bigquery.SqlInstanceToBigQuery.{InstanceSchemaSyntax, InstanceSyntax}
import org.datatools.bigdatatypes.cassandra.CassandraTables
import org.datatools.bigdatatypes.cassandra.CassandraTables.AsCassandraInstanceSyntax
import org.datatools.bigdatatypes.circe.CirceTypeConversion.circeJsonType
import org.datatools.bigdatatypes.formats.Formats.implicitDefaultFormats

class CirceToOthers extends UnitSpec {

  behavior of "Circe types to Cassandra"

  val circeJson: Json = CirceTestTypes.basicTypes

  "Json from Circe" should "be converted into Cassandra Table" in {
    CassandraTables.table[Json](CirceTestTypes.basicTypes, "testTable", "myLong").toString shouldBe
      "CREATE TABLE testtable (myint decimal,mylong decimal PRIMARY KEY,myfloat decimal,mydouble decimal,mydecimal decimal,myboolean boolean,mystring text)"
  }

  it should "be converted into Cassandra Table using extension method" in {
    CirceTestTypes.basicTypes.asCassandra("testTable", "myLong").toString shouldBe
      "CREATE TABLE testtable (myint decimal,mylong decimal PRIMARY KEY,myfloat decimal,mydouble decimal,mydecimal decimal,myboolean boolean,mystring text)"
  }

  behavior of "Circe to BigQuery"

  val fields: List[Field] = List(
    Field.newBuilder("id", StandardSQLTypeName.STRING).setMode(Mode.REQUIRED).build(),
    Field.newBuilder("foo", StandardSQLTypeName.STRING).setMode(Mode.REQUIRED).build(),
    Field.newBuilder("bar", StandardSQLTypeName.NUMERIC).setMode(Mode.REQUIRED).build()
  )
  val bqSchema: Schema = Schema.of(toJava(fields))

  val circe: Json = Json.fromFields(
    List(
      ("id", Json.fromString("test")),
      ("foo", Json.fromString("test")),
      ("bar", Json.fromInt(1))
    )
  )

  it should "be converted into BigQuery Schema" in {
    circe.asBigQuery.schema shouldBe bqSchema
  }

}
