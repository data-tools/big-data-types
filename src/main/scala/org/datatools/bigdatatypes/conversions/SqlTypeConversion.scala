package org.datatools.bigdatatypes.conversions

import org.datatools.bigdatatypes.types.basic._
import shapeless._
import shapeless.labelled.FieldType

/** Type class to convert any Scala type to an [[SqlType]]
  *
  * @tparam A is a Scala type
  */
trait SqlTypeConversion[-A] {

  /** @return the [[SqlType]] representation of [[A]]
    */
  def getType: SqlType
}

object SqlTypeConversion {

  /** Summoner method. Allows the syntax
    * {{{
    *   val intType = SqlTypeConversion[Int]
    * }}}
    */
  def apply[A](implicit a: SqlTypeConversion[A]): SqlTypeConversion[A] = a

  /** Factory constructor - allows easier construction of instances. e.g:
    * {{{
    *   val instance = SqlTypeConversion.instance[Option[Int]](SqlInt(Nullable))
    * }}}
    */
  def instance[A](sqlType: SqlType): SqlTypeConversion[A] =
    new SqlTypeConversion[A] {
      def getType: SqlType = sqlType
    }

  // Basic types
  implicit val intType: SqlTypeConversion[Int] = instance(SqlInt())
  implicit val longType: SqlTypeConversion[Long] = instance(SqlLong())
  implicit val doubleType: SqlTypeConversion[Double] = instance(SqlFloat())
  implicit val floatType: SqlTypeConversion[Float] = instance(SqlFloat())
  implicit val bigDecimalType: SqlTypeConversion[BigDecimal] = instance(SqlDecimal())
  implicit val booleanType: SqlTypeConversion[Boolean] = instance(SqlBool())
  implicit val stringType: SqlTypeConversion[String] = instance(SqlString())

  /** type class derivation for Option
    */
  implicit def optionType[A](implicit cnv: SqlTypeConversion[A]): SqlTypeConversion[Option[A]] =
    instance(cnv.getType.changeMode(Nullable))

  /** Type class derivation for Repeated / Iterable types
    */
  implicit def listLikeType[A](implicit cnv: SqlTypeConversion[A]): SqlTypeConversion[Iterable[A]] =
    instance(cnv.getType.changeMode(Repeated))

  /** Generic derivation of this type class, allows recursive conversions
    */
  implicit def genericType[A, H](implicit
      generic: LabelledGeneric.Aux[A, H],
      hEncoder: Lazy[SqlStructTypeConversion[H]]
  ): SqlTypeConversion[A] =
    instance(hEncoder.value.getType)

}

/** Type class companion to the SqlTypeConversion to make derivation of struct types easier
  *
  * This shouldn't be used from outside but making it private makes some derivations to fail.
  */
trait SqlStructTypeConversion[A] extends SqlTypeConversion[A] {
  def getType: SqlStruct
}

object SqlStructTypeConversion {

  /** Summoner method */
  def apply[A](implicit instance: SqlStructTypeConversion[A]): SqlStructTypeConversion[A] = instance

  /** Factory constructor */
  def instance[A](record: SqlStruct): SqlStructTypeConversion[A] =
    new SqlStructTypeConversion[A] {
      def getType: SqlStruct = record
    }

  /** HNil instance */
  implicit val hnilConversion: SqlStructTypeConversion[HNil] = instance(SqlStruct(List.empty[(String, SqlType)]))

  /** HList instance derivation */
  implicit def hlistField[K <: Symbol, H, T <: HList](implicit
      witness: Witness.Aux[K],
      hField: Lazy[SqlTypeConversion[H]],
      tField: SqlStructTypeConversion[T]
  ): SqlStructTypeConversion[FieldType[K, H] :: T] =
    instance(SqlStruct((witness.value.name -> hField.value.getType) :: tField.getType.records))
}
