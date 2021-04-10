package org.datatools.bigdatatypes.conversions

import scala.deriving.*
import scala.compiletime.{erasedValue, summonInline}
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

  //given emptyTuple: SqlTypeConversion[EmptyTuple] = instance(SqlStruct(List.empty[(String, SqlType)]))


  /*
  given hlistField2[A <: Product](using m: Mirror.ProductOf[A], struct: SqlStructTypeConversion[A]): SqlStructTypeConversion[A] =
    instance(SqlStruct((m.fromProduct(struct.getType).productElementName(0) -> struct.getType) :: struct.getType.records))
*/
  inline given derived[T](using m: Mirror.Of[T]): SqlTypeConversion[T] = {
    lazy val elemInstances = summonAll[m.MirroredElemTypes] // (1)
    inline m match {
      //case s: Mirror.SumOf[T] => instanceStruct(s, elemInstances)
      case p: Mirror.ProductOf[T] => instanceStructProduct(p, elemInstances)
    }
  }

  inline def summonAll[T <: Tuple]: List[SqlTypeConversion[_]] = {
    inline erasedValue[T] match {
      case _: EmptyTuple => Nil
      case _: (t *: ts) => summonInline[SqlTypeConversion[t]] :: summonAll[ts]
    }
  }

  def instanceStruct[T](s: Mirror.SumOf[T], elems: List[SqlTypeConversion[_]]): SqlTypeConversion[T] = {
    new SqlTypeConversion[T] {
      def getType: SqlType = {
        val tuples = elems.map(v => ("test" -> v.getType))
        tuples match {
          case head::tail => SqlStruct(head :: tail)
          case Nil => SqlStruct(List.empty[(String, SqlType)])
        }
      }
    }
  }

  def instanceStructProduct[T](s: Mirror.ProductOf[T], elems: List[SqlTypeConversion[_]]): SqlTypeConversion[T] = {
    new SqlTypeConversion[T] {
      def getType: SqlType = {
        val tuples = elems.map(v => ("test" -> v.getType))
        tuples match {
          case head::tail => SqlStruct(head :: tail)
          case Nil => SqlStruct(List.empty[(String, SqlType)])
        }
      }
    }
  }

/*
  inline given [A <: Product] (using m: Mirror.ProductOf[A]): SqlTypeConversion[A] =
    new SqlTypeConversion[A] {
      type ElemTransformers = Tuple.Map[m.MirroredElemTypes, SqlTypeConversion]
      //val fields = summonAll[ElemTransformers]
      //all elements inside the Product as SqlTypeConversion
      val elements = summonAll[ElemTransformers].toList.asInstanceOf[List[SqlTypeConversion[Any]]]
      val tuples = elements.map(v => ("test" -> v.getType))

      def getType: SqlType = {
        tuples match {
          case head::tail => SqlStruct(head :: tail)
          case Nil => SqlStruct(List.empty[(String, SqlType)])
        }
      }
    }
  */


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