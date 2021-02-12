package org.datatools.bigdatatypes.spark

import org.apache.spark.sql.types._
import org.datatools.bigdatatypes.conversions.{SqlInstanceConversion, SqlTypeConversion}
import org.datatools.bigdatatypes.conversions.SqlTypeConversion
import org.datatools.bigdatatypes.types.basic._

import scala.annotation.tailrec

object SqlTypeConversionSpark {

  type Record = (String, SqlType)

  /**
    * SqlTypeConversion type class specifications for simple types
    */
  implicit val intType: SqlTypeConversion[IntegerType] = SqlTypeConversion.instance(SqlInt())
  implicit val longType: SqlTypeConversion[LongType] = SqlTypeConversion.instance(SqlLong())
  implicit val doubleType: SqlTypeConversion[DoubleType] = SqlTypeConversion.instance(SqlDouble())
  implicit val floatType: SqlTypeConversion[FloatType] = SqlTypeConversion.instance(SqlFloat())
  //TODO use implicit Formats for default Decimal precision
  implicit val bigDecimalType: SqlTypeConversion[BigDecimal] = SqlTypeConversion.instance(SqlDecimal())
  implicit val booleanType: SqlTypeConversion[BooleanType] = SqlTypeConversion.instance(SqlBool())
  implicit val stringType: SqlTypeConversion[StringType] = SqlTypeConversion.instance(SqlString())
  // Extended types
  implicit val timestampType: SqlTypeConversion[TimestampType] = SqlTypeConversion.instance(SqlTimestamp())
  implicit val dateType: SqlTypeConversion[DateType] = SqlTypeConversion.instance(SqlDate())

  /**
    * SqlInstanceConversion type class specifications for struct instances
    */
  implicit val structField: SqlInstanceConversion[StructField] =
    (value: StructField) => convertSparkType(value.dataType, value.nullable)

  implicit val structType: SqlInstanceConversion[StructType] =
    (value: StructType) => SqlStruct(loopStructType(value))


  /** Enables val myInstance: StructType -> myInstance.getType syntax and DataFrame.schema.getType syntax
    * @param value in a StructType (Spark Schema)
    */
  implicit class StructTypeSyntax(value: StructType) {
    def getType: SqlType = SqlInstanceConversion[StructType].getType(value)
  }
  /** Enables myField: StructField -> myField.getType */
  implicit class StructFieldSyntax(value: StructField) {
    def getType: SqlType = SqlInstanceConversion[StructField].getType(value)
  }

  //TODO use implicit Formats for default Decimal precision
  /** Given a Spark DataType, converts it into a SqlType
    */
  @tailrec
  private def convertSparkType(dataType: DataType,
                               nullable: Boolean,
                               inheritMode: Option[SqlTypeMode] = None
  ): SqlType = dataType match {
    case IntegerType             => SqlInt(inheritMode.getOrElse(isNullable(nullable)))
    case LongType                => SqlLong(inheritMode.getOrElse(isNullable(nullable)))
    case DoubleType              => SqlDouble(inheritMode.getOrElse(isNullable(nullable)))
    case FloatType               => SqlFloat(inheritMode.getOrElse(isNullable(nullable)))
    case DecimalType()           => SqlDecimal(inheritMode.getOrElse(isNullable(nullable)))
    case BooleanType             => SqlBool(inheritMode.getOrElse(isNullable(nullable)))
    case StringType              => SqlString(inheritMode.getOrElse(isNullable(nullable)))
    case TimestampType           => SqlTimestamp(inheritMode.getOrElse(isNullable(nullable)))
    case DateType                => SqlDate(inheritMode.getOrElse(isNullable(nullable)))
    case ArrayType(basicType, _) => convertSparkType(basicType, nullable, Some(Repeated))
    case StructType(fields) =>
      SqlStruct(loopStructType(StructType(fields)), inheritMode.getOrElse(isNullable(nullable)))
  }

  /** From Boolean to Nullable or Required Mode
    */
  private def isNullable(nullable: Boolean): SqlTypeMode =
    if (nullable) {
      Nullable
    }
    else {
      Required
    }

  /** Given a StructType, convert it into a List[Record] to be used in a SqlStruct
    */
  private def loopStructType(st: StructType): List[Record] =
    st.map { x =>
      x.name -> convertSparkType(x.dataType, x.nullable)
    }.toList

}
