package org.datatools.bigdatatypes.bigquery

import com.google.cloud.bigquery.Field.Mode
import com.google.cloud.bigquery.{Field, StandardSQLTypeName}
import org.datatools.bigdatatypes.bigquery.BigQueryTypes.instance
import org.datatools.bigdatatypes.conversions._
import org.datatools.bigdatatypes.formats.Formats
import org.datatools.bigdatatypes.types.basic
import org.datatools.bigdatatypes.types.basic._
import org.datatools.bigdatatypes.conversions.SqlTypeConversion._

/** Type class to convert generic SqlTypes into BigQuery specific fields
  * In BigQuery, a table is made with a List of fields so as an example:
  * a case class will be converted into SqlTypes and then into a List of BigQuery fields
  *
  * @tparam A the type we want to obtain an schema from
  */
trait BigQueryTypes[A] {

  /** @return a list of [[Field]]s that represents [[A]]
    */
  def bigQueryFields: List[Field]
}

object BigQueryTypes {

  /** Summoner method. Allows the syntax */
  def apply[A](implicit instance: BigQueryTypes[A]): BigQueryTypes[A] = instance

  /** Factory constructor - allows easier construction of instances */
  def instance[A](fs: List[Field]): BigQueryTypes[A] =
    new BigQueryTypes[A] {
      def bigQueryFields: List[Field] = fs
    }

  /** Instance derivation via SqlTypeConversion.
    */
  implicit def fieldsFromSqlTypeConversion[A: SqlTypeConversion](implicit f: Formats): BigQueryTypes[A] =
    instance(getSchema(SqlTypeConversion[A].getType))

  //TODO improving this and adding all the SqlType options will remove a warning and will allow a syntax like:
  //TODO val myInt: Int = 5  -> BigQueryTypesInstance[Int].getBigQueryFields(myInt)
  def getSchema(sqlType: SqlType)(implicit f: Formats): List[Field] = sqlType match {
    case SqlStruct(Nil, _) => Nil
    case SqlStruct((name, sqlType) :: records, mode) =>
      getSchemaWithName(f.transformKeys(name), sqlType) :: getSchema(basic.SqlStruct(records, mode))
  }

  /** Basic SqlTypes conversions to BigQuery Fields
    */
  private def getSchemaWithName(name: String, sqlType: SqlType)(implicit f: Formats): Field = sqlType match {
    case SqlInt(mode) =>
      Field.newBuilder(name, StandardSQLTypeName.INT64).setMode(sqlModeToBigQueryMode(mode)).build()
    case SqlLong(mode) =>
      Field.newBuilder(name, StandardSQLTypeName.INT64).setMode(sqlModeToBigQueryMode(mode)).build()
    case SqlFloat(mode) =>
      Field.newBuilder(name, StandardSQLTypeName.FLOAT64).setMode(sqlModeToBigQueryMode(mode)).build()
    case SqlDouble(mode) =>
      Field.newBuilder(name, StandardSQLTypeName.FLOAT64).setMode(sqlModeToBigQueryMode(mode)).build()
    case SqlDecimal(mode) =>
      Field.newBuilder(name, StandardSQLTypeName.NUMERIC).setMode(sqlModeToBigQueryMode(mode)).build()
    case SqlBool(mode) =>
      Field.newBuilder(name, StandardSQLTypeName.BOOL).setMode(sqlModeToBigQueryMode(mode)).build()
    case SqlString(mode) =>
      Field.newBuilder(name, StandardSQLTypeName.STRING).setMode(sqlModeToBigQueryMode(mode)).build()
    case SqlTimestamp(mode) =>
      Field.newBuilder(name, StandardSQLTypeName.TIMESTAMP).setMode(sqlModeToBigQueryMode(mode)).build()
    case SqlDate(mode) =>
      Field.newBuilder(name, StandardSQLTypeName.DATETIME).setMode(sqlModeToBigQueryMode(mode)).build()
    case SqlStruct(subType, mode) =>
      Field
        .newBuilder(name, StandardSQLTypeName.STRUCT, getSchema(SqlStruct(subType)): _*)
        .setMode(sqlModeToBigQueryMode(mode))
        .build()
  }

  private def sqlModeToBigQueryMode(sqlTypeMode: SqlTypeMode): Mode = sqlTypeMode match {
    case Nullable => Mode.NULLABLE
    case Repeated => Mode.REPEATED
    case Required => Mode.REQUIRED
  }

  /**
   * Allows syntax .getBigQueryFields for case classes instances
   * @param value not used, needed for implicit
   * @tparam A is a Case Class
   */
  implicit class BigQueryFieldSyntax[A <: Product](value: A) {
    def getBigQueryFields(implicit a: BigQueryTypes[A]): List[Field] = a.bigQueryFields
  }
}
