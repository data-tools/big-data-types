package org.datatools.bigdatatypes.conversions

import compiletime.package$package.summonAll
import deriving.Mirror
import org.datatools.bigdatatypes.basictypes.SqlType
import org.datatools.bigdatatypes.basictypes.SqlType._
import org.datatools.bigdatatypes.basictypes.SqlTypeMode._

import java.sql.{Date, Timestamp}

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
  def apply[A](using a: SqlTypeConversion[A]): SqlTypeConversion[A] = a

  /** Factory constructor - allows easier construction of instances. e.g:
    * {{{
    *   val instance = SqlTypeConversion.instance[Option[Int]](SqlInt(Nullable))
    * }}}
    */
  def instance[A](sqlType: SqlType): SqlTypeConversion[A] =
    new SqlTypeConversion[A] {
      def getType: SqlType = sqlType
    }

  /*
  implicit val intType: SqlTypeConversion[Int] =
    new SqlTypeConversion[Int] {
      def getType: SqlType = ???
    }


  given SqlTypeConversion[Int] with {
    def getType: SqlType = SqlInt()
  }

  given SqlTypeConversion[Int] =
    new SqlTypeConversion[Int] {
      def getType: SqlType = ???
    }
  */


  // Basic types
  given SqlTypeConversion[Int] = instance(SqlInt())
  given SqlTypeConversion[Long] = instance(SqlLong())
  given SqlTypeConversion[Double] = instance(SqlDouble())
  given SqlTypeConversion[Float] = instance(SqlFloat())
  given SqlTypeConversion[BigDecimal] = instance(SqlDecimal())
  given SqlTypeConversion[Boolean] = instance(SqlBool())
  given SqlTypeConversion[String] = instance(SqlString())
  // Extended types
  given SqlTypeConversion[Timestamp] = instance(SqlTimestamp())
  given SqlTypeConversion[Date] = instance(SqlDate())


  /** type class derivation for Option
    */
  given [A](using cnv: SqlTypeConversion[A]): SqlTypeConversion[Option[A]] =
    instance(cnv.getType.changeMode(Nullable))

  /** Type class derivation for Repeated / Iterable types
    */
  given [A](using cnv: SqlTypeConversion[A]): SqlTypeConversion[Iterable[A]] =
    instance(cnv.getType.changeMode(Repeated))

  /*
  /** Generic derivation of this type class, allows recursive conversions
    */
  implicit def genericType[A, H](implicit
      generic: LabelledGeneric.Aux[A, H],
      hEncoder: Lazy[SqlStructTypeConversion[H]]
  ): SqlTypeConversion[A] =
    instance(hEncoder.value.getType)
*/
  given hlistField2[A <: Product](using struct: SqlStructTypeConversion[A]): SqlTypeConversion[A] =
  instance(struct.getType)


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
  def apply[A](using instance: SqlStructTypeConversion[A]): SqlStructTypeConversion[A] = instance

  /** Factory constructor */
  def instance[A](record: SqlStruct): SqlStructTypeConversion[A] =
    new SqlStructTypeConversion[A] {
      def getType: SqlStruct = record
    }

  /** HNil instance */
  given hnilConversion2: SqlStructTypeConversion[EmptyTuple] = instance(SqlStruct(List.empty[(String, SqlType)]))

  /*
  given hlistField2[A <: Product](using m: Mirror.ProductOf[A], struct: SqlStructTypeConversion[A]): SqlStructTypeConversion[A] =
    instance(SqlStruct((m.fromProduct(struct.getType).productElementName(0) -> struct.getType) :: struct.getType.records))
*/

  inline given [A <: Product] (using m: Mirror.ProductOf[A])(using struct: SqlStructTypeConversion[A]): SqlStructTypeConversion[A] =
    new SqlStructTypeConversion[A]:
      type ElemTransformers = Tuple.Map[m.MirroredElemTypes, SqlStructTypeConversion]
      val fields = summonAll[ElemTransformers]
      val elements = summonAll[ElemTransformers].toList.asInstanceOf[List[SqlStructTypeConversion[Any]]]
      val head = elements.head
      println(elements.head)

      def getType: SqlStruct =
        SqlStruct((m.fromProduct(struct.getType).productElementName(0) -> elements.head.getType) :: elements.head.getType.records)


  /*
  /** HList instance derivation */
  implicit def hlistField[K <: Symbol, H, T <: HList](implicit
      witness: Witness.Aux[K],
      hField: Lazy[SqlTypeConversion[H]],
      tField: SqlStructTypeConversion[T]
  ): SqlStructTypeConversion[FieldType[K, H] :: T] =
    instance(SqlStruct((witness.value.name -> hField.value.getType) :: tField.getType.records))
  */

}