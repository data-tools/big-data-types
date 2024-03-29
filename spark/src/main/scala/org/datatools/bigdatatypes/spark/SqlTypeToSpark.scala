package org.datatools.bigdatatypes.spark

import org.apache.spark.sql.types.*
import org.datatools.bigdatatypes.basictypes.*
import org.datatools.bigdatatypes.basictypes.SqlType.*
import org.datatools.bigdatatypes.basictypes.SqlTypeMode.*
import org.datatools.bigdatatypes.conversions.SqlTypeConversion
import org.datatools.bigdatatypes.formats.Formats

/** Type class to convert generic SqlTypes into Spark specific fields
  * In Spark, an schema is made with a Struct of fields so as an example:
  * a case class will be converted into SqlTypes and then into a Struct of Spark fields
  *
  * @tparam A the type we want to obtain an schema from
  */
trait SqlTypeToSpark[A] {

  /** @return a list of [[StructField]]s that represents [[A]]
    */
  def sparkFields: List[StructField]

  /** Returns the Spark Schema
    * @return [[StructType]] with the schema to be used in Spark
    */
  def sparkSchema: StructType = StructType(sparkFields)
}

object SqlTypeToSpark {

  /** Summoner method. Allows the syntax */
  def apply[A](implicit instance: SqlTypeToSpark[A]): SqlTypeToSpark[A] = instance

  /** Factory constructor - allows easier construction of instances */
  def instance[A](fs: List[StructField]): SqlTypeToSpark[A] =
    new SqlTypeToSpark[A] {
      def sparkFields: List[StructField] = fs
    }

  /** Instance derivation via SqlTypeConversion.
    */
  implicit def fieldsFromSqlTypeConversion[A: SqlTypeConversion](implicit f: Formats): SqlTypeToSpark[A] =
    instance(getSchema(SqlTypeConversion[A].getType))

  /** Creates the schema (list of fields)
    * Applies an implicit [[Formats.transformKey]] in the process
    *
    * @param sqlType [[SqlType]]
    * @param f [[Formats]] to apply while constructing the schema
    * @return List of [[StructField]] representing the schema of the given type
    */
  private[spark] def getSchema(sqlType: SqlType)(implicit f: Formats): List[StructField] = sqlType match {
    case SqlStruct(Nil, _) => Nil
    case SqlStruct((name, sqlType) :: records, mode) =>
      getSchemaWithName(f.transformKey(name, sqlType), sqlType) :: getSchema(SqlStruct(records, mode))
  }

  /** Basic SqlTypes conversions to Spark Types
    */
  private def getSchemaWithName(name: String, sqlType: SqlType)(implicit f: Formats): StructField = sqlType match {
    case SqlInt(mode) =>
      StructField(name, sparkType(mode, IntegerType), isNullable(mode))
    case SqlLong(mode) =>
      StructField(name, sparkType(mode, LongType), isNullable(mode))
    case SqlFloat(mode) =>
      StructField(name, sparkType(mode, FloatType), isNullable(mode))
    case SqlDouble(mode) =>
      StructField(name, sparkType(mode, DoubleType), isNullable(mode))
    case SqlDecimal(mode) =>
      StructField(
        name,
        sparkType(mode, DataTypes.createDecimalType(f.bigDecimal.precision, f.bigDecimal.scale)),
        isNullable(mode)
      )
    case SqlBool(mode) =>
      StructField(name, sparkType(mode, BooleanType), isNullable(mode))
    case SqlString(mode) =>
      StructField(name, sparkType(mode, StringType), isNullable(mode))
    case SqlTimestamp(mode) =>
      StructField(name, sparkType(mode, TimestampType), isNullable(mode))
    case SqlDate(mode) =>
      StructField(name, sparkType(mode, DateType), isNullable(mode))
    case SqlStruct(subType, mode) =>
      StructField(name, sparkType(mode, StructType(getSchema(SqlStruct(subType, mode)))), isNullable(mode))
  }

  /** Find if a type has to be ArrayType or Basic type
    * @param mode [[SqlTypeMode]] needed to check repeated or not
    * @param sparkType valid [[DataType]] from Spark
    * @return Spark [[DataType]]
    */
  private def sparkType(mode: SqlTypeMode, sparkType: DataType): DataType = mode match {
    case Repeated => ArrayType(sparkType, containsNull = isNullable(mode))
    case _        => sparkType
  }

  /** Check if a field has to be nullable or not based on its [[SqlTypeMode]]
    * Repeated is marked as nullable
    * @param sqlTypeMode [[SqlTypeMode]]
    * @return [[Boolean]] if field has to be nullable, else if not
    */
  private def isNullable(sqlTypeMode: SqlTypeMode): Boolean = sqlTypeMode match {
    case Nullable => true
    case Repeated => true
    case Required => false
  }

  /** Extension method, allows syntax .asSparkSchema and .asSparkFields for case classes instances
    * @param value not used, needed for implicit
    * @tparam A is a Case Class
    */
  implicit class SparkSchemaSyntax[A <: Product](value: A) {
    def asSparkSchema(implicit a: SqlTypeToSpark[A]): StructType = a.sparkSchema
    def asSparkFields(implicit a: SqlTypeToSpark[A]): List[StructField] = a.sparkFields
  }
}
