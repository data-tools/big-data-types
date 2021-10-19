package org.datatools.bigdatatypes.bigquery

import com.google.cloud.bigquery.Field.Mode
import com.google.cloud.bigquery.{Field, StandardSQLTypeName}
import org.datatools.bigdatatypes.basictypes.SqlType._
import org.datatools.bigdatatypes.basictypes.SqlTypeMode._
import org.datatools.bigdatatypes.basictypes._
import org.datatools.bigdatatypes.conversions._
import org.datatools.bigdatatypes.formats.Formats

/** Type class to convert generic SqlTypes into BigQuery specific fields
  * In BigQuery, a table is made with a List of fields so as an example:
  * a case class will be converted into SqlTypes and then into a List of BigQuery fields
  *
  * @tparam A the type we want to obtain an schema from
  */
trait SqlTypeToBigQuery[A] {

  /** @return a list of [[Field]]s that represents [[A]]
    */
  def bigQueryFields: List[Field]
}

object SqlTypeToBigQuery {

  /** Summoner method. Allows the syntax */
  def apply[A](implicit instance: SqlTypeToBigQuery[A]): SqlTypeToBigQuery[A] = instance

  /** Factory constructor - allows easier construction of instances */
  def instance[A](fs: List[Field]): SqlTypeToBigQuery[A] =
    new SqlTypeToBigQuery[A] {
      def bigQueryFields: List[Field] = fs
    }

  /** Instance derivation via SqlTypeConversion.
    */
  implicit def fieldsFromSqlTypeConversion[A: SqlTypeConversion](implicit f: Formats): SqlTypeToBigQuery[A] =
    instance(getSchema(SqlTypeConversion[A].getType))

  //TODO improving this and adding all the SqlType options will remove a warning and will allow a syntax like:
  //TODO val myInt: Int = 5  -> SqlTypeToBigQuery[Int].bigQueryFields(myInt)
  def getSchema(sqlType: SqlType)(implicit f: Formats): List[Field] = sqlType match {
    case SqlStruct(Nil, _) => Nil
    case SqlStruct((name, sqlType) :: records, mode) =>
      getSchemaWithName(f.transformKey(name, sqlType), sqlType) :: getSchema(SqlStruct(records, mode))
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
      Field.newBuilder(name, StandardSQLTypeName.DATE).setMode(sqlModeToBigQueryMode(mode)).build()
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
   * Allows syntax .asBigQuery for case classes instances
   * @param value not used, needed for implicit
   * @tparam A is a Case Class (Product type)
   */
  implicit class BigQueryFieldSyntax[A <: Product](value: A) {
    def asBigQuery(implicit a: SqlTypeToBigQuery[A]): List[Field] = a.bigQueryFields
  }
}
