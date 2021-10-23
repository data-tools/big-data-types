package org.datatools.bigdatatypes.bigquery

import com.google.cloud.bigquery.{Field, FieldList, LegacySQLTypeName, Schema, StandardSQLTypeName}
import org.datatools.bigdatatypes.basictypes.SqlType.*
import org.datatools.bigdatatypes.basictypes.SqlTypeMode.*
import org.datatools.bigdatatypes.basictypes.*
import org.datatools.bigdatatypes.bigquery.JavaConverters.toScala
import org.datatools.bigdatatypes.conversions.{SqlInstanceConversion, SqlTypeConversion}


/** Using SqlTypeConversion and SqlInstanceConversion type classes,
  * here are defined all the conversions to transform BigQuery Tables into [[SqlType]]s
  */
object BigQueryTypeConversion {

  type Record = (String, SqlType)

  /** SqlTypeConversion type class specifications for simple types
    */
  implicit val intType: SqlTypeConversion[StandardSQLTypeName.INT64.type] = SqlTypeConversion.instance(SqlLong())
  implicit val floatType: SqlTypeConversion[StandardSQLTypeName.FLOAT64.type] = SqlTypeConversion.instance(SqlFloat())
  implicit val bigDecimalType: SqlTypeConversion[StandardSQLTypeName.NUMERIC.type] =  SqlTypeConversion.instance(SqlDecimal())
  implicit val booleanType: SqlTypeConversion[StandardSQLTypeName.BOOL.type] = SqlTypeConversion.instance(SqlBool())
  implicit val stringType: SqlTypeConversion[StandardSQLTypeName.STRING.type] = SqlTypeConversion.instance(SqlString())

  // Extended types
  implicit val timestampType: SqlTypeConversion[StandardSQLTypeName.TIMESTAMP.type] = SqlTypeConversion.instance(SqlTimestamp())
  implicit val dateType: SqlTypeConversion[StandardSQLTypeName.DATE.type] = SqlTypeConversion.instance(SqlDate())

  /** SqlInstanceConversion type class specifications for Field and Schema instances
    */
  implicit val schema: SqlInstanceConversion[Schema] =
    (value: Schema) => SqlStruct(loopSchemaType(value.getFields))

  implicit val field: SqlInstanceConversion[Field] =
    (value: Field) => SqlStruct(loopSchemaType(value.getSubFields))


  /** Extension methods for BigQuery Schemas and Fields into SqlTypes */

  /** Extension method. Enables val myInstance: Field -> myInstance.asSqlType syntax
    */
  implicit class FieldTypeSyntax(value: Field) {
    def asSqlType: SqlType = SqlInstanceConversion[Field].getType(value)
  }

  /** Extension method. Enables myBQTable: Schema -> myBQTable.asSqlType */
  implicit class SchemaFieldSyntax(value: Schema) {
    def asSqlType: SqlType = SqlInstanceConversion[Schema].getType(value)
  }

  /** Given a BigQuery Field, converts it into a SqlType
    */
  private def convertBigQueryType(field: Field): SqlType =
    field.getType match {
      case LegacySQLTypeName.INTEGER   => SqlLong(findMode(field.getMode)) //BigQuery only has Int64
      case LegacySQLTypeName.FLOAT     => SqlFloat(findMode(field.getMode))
      case LegacySQLTypeName.NUMERIC   => SqlDecimal(findMode(field.getMode))
      case LegacySQLTypeName.BOOLEAN   => SqlBool(findMode(field.getMode))
      case LegacySQLTypeName.STRING    => SqlString(findMode(field.getMode))
      case LegacySQLTypeName.TIMESTAMP => SqlTimestamp(findMode(field.getMode))
      case LegacySQLTypeName.DATE      => SqlDate(findMode(field.getMode))
      case LegacySQLTypeName.RECORD    => SqlStruct(loopSchemaType(field.getSubFields), findMode(field.getMode))
    }

  /** Maps Field.Mode to our custom Mode
    */
  private def findMode(mode: Field.Mode): SqlTypeMode =
    mode match {
      case Field.Mode.NULLABLE => Nullable
      case Field.Mode.REQUIRED => Required
      case Field.Mode.REPEATED => Repeated
    }

  /** Given a FieldList (from a [[Field]] or from a [[Schema]]), converts it into a List[Record] to be used in a SqlStruct
    */
  private def loopSchemaType(fields: FieldList): List[Record] =
    toScala(fields).map { field =>
      field.getName -> convertBigQueryType(field)
    }
}
