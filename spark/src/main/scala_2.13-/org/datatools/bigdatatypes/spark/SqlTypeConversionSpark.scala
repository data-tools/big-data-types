package org.datatools.bigdatatypes.spark

import org.apache.spark.sql.types._
import org.datatools.bigdatatypes.conversions.SqlTypeConversion
import org.datatools.bigdatatypes.types.basic._

trait SqlTypeConversionSpark[-A] extends SqlTypeConversion[A]

object SqlTypeConversionSpark {

  type Record = (String, SqlType)

  /** Summoner method. Allows the syntax
    * {{{
    *   val intType = SqlTypeConversion[Int]
    *   val
    * }}}
    */
  def apply[A](implicit a: SqlTypeConversionSpark[A]): SqlTypeConversionSpark[A] = a
  //def apply[A: SqlTypeConversionSpark](a: A): SqlTypeConversionSpark[A] = a.getType

  /** Different apply expecting a parameter when working with Spark Schemas
    * as we can not work with type implicit resolutions
    * @param sf is a simple StructField from Spark
    */
  def apply(sf: StructField): SqlTypeConversionSpark[StructField] = structFieldConversion(sf)
  def apply(st: StructType): SqlTypeConversionSpark[StructType] = structTypeConversion(st)
  def apply(st: List[StructField]): SqlTypeConversionSpark[StructType] = structTypeConversion(StructType(st))

  /** Factory constructor - allows easier construction of instances. e.g:
    * {{{
    *   val instance = SqlTypeConversion.instance[Option[Int]](SqlInt(Nullable))
    * }}}
    */
  def instance[A](sqlType: SqlType): SqlTypeConversionSpark[A] =
    new SqlTypeConversionSpark[A] {
      def getType: SqlType = sqlType
    }

  implicit val intType: SqlTypeConversionSpark[IntegerType] = instance(SqlInt())
  implicit val longType: SqlTypeConversionSpark[LongType] = instance(SqlLong())
  implicit val doubleType: SqlTypeConversionSpark[DoubleType] = instance(SqlFloat())
  implicit val floatType: SqlTypeConversionSpark[FloatType] = instance(SqlFloat())
  //implicit val bigDecimalType: SqlTypeConversion[BigDecimal] = instance(SqlDecimal())
  implicit val booleanType: SqlTypeConversionSpark[BooleanType] = instance(SqlBool())
  implicit val stringType: SqlTypeConversionSpark[StringType] = instance(SqlString())
  // Extended types
  implicit val timestampType: SqlTypeConversionSpark[TimestampType] = instance(SqlTimestamp())
  implicit val dateType: SqlTypeConversionSpark[DateType] = instance(SqlDate())

  implicit def listLikeType[A](implicit cnv: SqlTypeConversionSpark[A]): SqlTypeConversionSpark[Iterable[A]] =
    instance(cnv.getType.changeMode(Repeated))

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

  //TODO change Nullable for a method that converts boolean to Nullable or not
  /** When working with StructFields we already have an instance and not just a type so we need a parameter
    * StructField is being limited to just it's DataType so
    * a StructField("name", IntegerType) will be converted into SqlTypeConversionSpark[IntegerType]
    */
  private def structFieldConversion(sf: StructField): SqlTypeConversionSpark[StructField] =
    instance(convertSparkType(sf.dataType))

  //TODO add the rest of the types
  /** Given a Spark DataType, converts it into a SqlType
    */
  private def convertSparkType(dataType: DataType): SqlType = dataType match {
    case IntegerType => SqlInt()
    case LongType    => SqlLong()
    case DoubleType  => SqlFloat()
    case FloatType   => SqlFloat()
    //case DecimalType() => SqlDecimal()
    case BooleanType   => SqlBool()
    case StringType    => SqlString()
    case TimestampType => SqlTimestamp()
    case DateType      => SqlDate()
  }

  /** When working with StructTypes we already have an instance and not just a type so we need a parameter,
    * this converts a StructType (or Spark schema) into a SqlStructTypeConversionSpark[StructType]
    */
  private def structTypeConversion(st: StructType): SqlTypeConversionSpark[StructType] = instance(
    SqlStruct(loopStructType(st), Nullable)
  )

  /** Given a StructType, convert it into a List[Record] to be used in a SqlStruct
    */
  private def loopStructType(st: StructType): List[Record] =
    st.toList match {
      case head +: Seq() => List(head.name -> convertSparkType(head.dataType))
      case head +: tail  => List(head.name -> convertSparkType(head.dataType)) ++ loopStructType(StructType(tail))
    }

}
