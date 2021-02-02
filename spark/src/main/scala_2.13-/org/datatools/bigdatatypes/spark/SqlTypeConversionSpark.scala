package org.datatools.bigdatatypes.spark

import java.sql.{Date, Timestamp}

import org.apache.spark.sql.types.{BooleanType, DataType, DateType, DecimalType, DoubleType, FloatType, IntegerType, LongType, StringType, StructField, StructType, TimestampType}
import org.datatools.bigdatatypes.conversions.{SqlStructTypeConversion, SqlTypeConversion}
import org.datatools.bigdatatypes.spark.SqlStructTypeConversionSpark.{convertSparkType, instance, structTypeConversion}
import org.datatools.bigdatatypes.types.basic.{Nullable, Repeated, SqlBool, SqlDate, SqlDecimal, SqlFloat, SqlInt, SqlLong, SqlString, SqlStruct, SqlTimestamp, SqlType}
import shapeless.labelled.FieldType
import shapeless.syntax.std.tuple.productTupleOps
import shapeless.{::, Generic, HList, HNil, Lazy, Witness}

import scala.annotation.tailrec

trait SqlTypeConversionSpark[-A] extends SqlTypeConversion[A]

object SqlTypeConversionSpark {

  /** Summoner method. Allows the syntax
    * {{{
    *   val intType = SqlTypeConversion[Int]
    *   val
    * }}}
    */
  def apply[A](implicit a: SqlTypeConversionSpark[A]): SqlTypeConversionSpark[A] = a
  def apply(sf: StructField): SqlTypeConversionSpark[StructField] = structFieldConversion(sf)
  def apply(st: StructType): SqlStructTypeConversionSpark[StructType] = structTypeConversion(st)

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

  //TODO change Nullable for a method that converts boolean to Nullable or not
  def structFieldConversion[A](sf: StructField): SqlTypeConversionSpark[StructField] =
    instance(convertSparkType(sf.dataType))

}

trait SqlStructTypeConversionSpark[A] extends SqlStructTypeConversion[A]

object SqlStructTypeConversionSpark {
  type Record = (String, SqlType)

  /** Summoner method */
  def apply[A](implicit instance: SqlStructTypeConversionSpark[A]): SqlStructTypeConversionSpark[A] = instance

  //TODO add the rest of the types
  def convertSparkType(dataType: DataType): SqlType = dataType match {
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

  /** Factory constructor */
  def instance[A](record: SqlStruct): SqlStructTypeConversionSpark[A] =
    new SqlStructTypeConversionSpark[A] {
      def getType: SqlStruct = record
    }

  /**
    * Without implicits for StructTypes, this converts a StructType (or Spark schema) into a
    * SqlStructTypeConversionSpark[StructType]
    */
  def structTypeConversion(st: StructType): SqlStructTypeConversionSpark[StructType] = instance(SqlStruct(loopStructType(st), Nullable))


  /**
    * Given a StructType, convert it into a List[Record] to be used in a SqlStruct
    */
  private def loopStructType(st: StructType): List[Record] =
    st.toList match {
      case head +: Seq() => List(head.name -> convertSparkType(head.dataType))
      case head +: tail  => List(head.name -> convertSparkType(head.dataType)) ++ loopStructType(StructType(tail))
    }
}
