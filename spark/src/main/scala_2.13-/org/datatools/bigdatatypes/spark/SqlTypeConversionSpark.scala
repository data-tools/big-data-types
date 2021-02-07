package org.datatools.bigdatatypes.spark

import org.apache.spark.sql.types._
import org.datatools.bigdatatypes.conversions.SqlTypeConversion
import org.datatools.bigdatatypes.conversions.SqlTypeConversion._
import org.datatools.bigdatatypes.types.basic._

trait SqlTypeConversionSpark[-A] extends SqlTypeConversion[A]

object SqlTypeConversionSpark {

  type Record = (String, SqlType)

  /** Different apply expecting a parameter when working with Spark Schemas
    * as we can not work with type implicit resolutions
    * @param sf is a simple StructField from Spark
    */
  def apply(sf: StructField): SqlTypeConversion[StructField] = structFieldConversion(sf)
  def apply(st: StructType): SqlTypeConversion[StructType] = structTypeConversion(st)
  def apply(st: List[StructField]): SqlTypeConversion[StructType] = structTypeConversion(StructType(st))

  implicit val intType: SqlTypeConversion[IntegerType] = instance(SqlInt())
  implicit val longType: SqlTypeConversion[LongType] = instance(SqlLong())
  implicit val doubleType: SqlTypeConversion[DoubleType] = instance(SqlFloat())
  implicit val floatType: SqlTypeConversion[FloatType] = instance(SqlFloat())
  //TODO use implicit Formats for default Decimal precision
  //implicit val bigDecimalType: SqlTypeConversion[BigDecimal] = instance(SqlDecimal())
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

  //TODO make it tail recursive
  /** Given a Spark DataType, converts it into a SqlType
    */
  private def convertSparkType(dataType: DataType, nullable: Boolean): SqlType = dataType match {
    case IntegerType             => SqlInt(isNullable(nullable))
    case LongType                => SqlLong(isNullable(nullable))
    case DoubleType              => SqlFloat(isNullable(nullable))
    case FloatType               => SqlFloat(isNullable(nullable))
    case DecimalType()           => SqlDecimal(isNullable(nullable))
    case BooleanType             => SqlBool(isNullable(nullable))
    case StringType              => SqlString(isNullable(nullable))
    case TimestampType           => SqlTimestamp(isNullable(nullable))
    case DateType                => SqlDate(isNullable(nullable))
    case ArrayType(basicType, _) => convertSparkType(basicType, nullable).changeMode(Repeated)
    case StructType(fields)      => SqlStruct(loopStructType(StructType(fields)), isNullable(nullable))
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

  //TODO make it tail recursive
  /** Given a StructType, convert it into a List[Record] to be used in a SqlStruct
    */
  private def loopStructType(st: StructType): List[Record] =
    st.toList match {
      case head +: Seq() => List(head.name -> convertSparkType(head.dataType, head.nullable))
      case head +: tail =>
        List(head.name -> convertSparkType(head.dataType, head.nullable)) ++ loopStructType(StructType(tail))
    }

}
