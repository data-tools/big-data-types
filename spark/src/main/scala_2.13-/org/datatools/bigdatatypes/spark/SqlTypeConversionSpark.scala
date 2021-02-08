package org.datatools.bigdatatypes.spark

import org.apache.spark.sql.types._
import org.datatools.bigdatatypes.conversions.SqlTypeConversion
import org.datatools.bigdatatypes.conversions.SqlTypeConversion._
import org.datatools.bigdatatypes.types.basic._

import scala.annotation.tailrec

trait SqlTypeConversionSpark[-A] extends SqlTypeConversion[A]

object SqlTypeConversionSpark {

  type Record = (String, SqlType)

  /** Different apply expecting a parameter when working with Spark Schemas
    * as we can not work with type implicit resolutions
    * @param sf is a simple StructField from Spark
    */
  def apply(sf: StructField): SqlTypeConversion[StructField] = structFieldConversion(sf)
  def apply(st: StructType): SqlTypeConversion[StructType] = structTypeConversion(st)

  implicit val intType: SqlTypeConversion[IntegerType] = instance(SqlInt())
  implicit val longType: SqlTypeConversion[LongType] = instance(SqlLong())
  implicit val doubleType: SqlTypeConversion[DoubleType] = instance(SqlDouble())
  implicit val floatType: SqlTypeConversion[FloatType] = instance(SqlFloat())
  //TODO use implicit Formats for default Decimal precision
  implicit val bigDecimalType: SqlTypeConversion[BigDecimal] = instance(SqlDecimal())
  implicit val booleanType: SqlTypeConversion[BooleanType] = instance(SqlBool())
  implicit val stringType: SqlTypeConversion[StringType] = instance(SqlString())
  // Extended types
  implicit val timestampType: SqlTypeConversion[TimestampType] = instance(SqlTimestamp())
  implicit val dateType: SqlTypeConversion[DateType] = instance(SqlDate())

  /** Enables StructField.getType syntax
    * @param value is an instance of StructField
    */
  implicit class StructFieldConversion(value: StructField) {
    def getType: SqlType = structFieldConversion(value).getType
  }

  /** Enables StructType.getType syntax and DataFrame.schema.getType syntax
    * @param value in an StructType (Spark Schema)
    */
  implicit class StructTypeConversion(value: StructType) {
    def getType: SqlType = structTypeConversion(value).getType
  }

  /** When working with StructFields we already have an instance and not just a type so we need a parameter
    * StructField is being limited to just it's DataType so
    * a StructField("name", IntegerType) will be converted into SqlTypeConversionSpark[IntegerType]
    */
  private def structFieldConversion(sf: StructField): SqlTypeConversion[StructField] =
    instance(convertSparkType(sf.dataType, sf.nullable))

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

  /** When working with StructTypes we already have an instance and not just a type so we need a parameter,
    * this converts a StructType (or Spark schema) into a SqlStructTypeConversionSpark[StructType]
    */
  private def structTypeConversion(st: StructType): SqlTypeConversion[StructType] = instance(
    SqlStruct(loopStructType(st))
  )

  /** Given a StructType, convert it into a List[Record] to be used in a SqlStruct
    */
  private def loopStructType(st: StructType): List[Record] =
    st.map { x =>
      x.name -> convertSparkType(x.dataType, x.nullable)
    }.toList

}
