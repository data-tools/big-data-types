package org.datatools.bigdatatypes.conversions

import scala.deriving.*
import scala.compiletime.*
import org.datatools.bigdatatypes.basictypes.SqlType
import org.datatools.bigdatatypes.basictypes.SqlType._
import org.datatools.bigdatatypes.basictypes.SqlTypeMode._

import java.sql.{Date, Timestamp}

/** Type class to convert any Scala type to an [[SqlType]]
  *
  * @tparam A is a Scala type
  */
trait SqlTypeConversion[A] {

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
  inline def apply[A](using a: SqlTypeConversion[A]): SqlTypeConversion[A] = a

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
  given [A](using cnv: SqlTypeConversion[A]): SqlTypeConversion[List[A]] =
    instance(cnv.getType.changeMode(Repeated))

  given [A](using cnv: SqlTypeConversion[A]): SqlTypeConversion[Seq[A]] =
    instance(cnv.getType.changeMode(Repeated))

  //given emptyTuple: SqlTypeConversion[EmptyTuple] = instance(SqlStruct(List.empty[(String, SqlType)]))

  /** Creates an SqlStruct for the given Product
    * @param s Mirror
    * @param elems List of tuples with names and SqlTypeConversions
    * @tparam T a Product
    */
  def instanceStructProduct[T](s: Mirror.ProductOf[T], elems: List[(String, SqlTypeConversion[_])]): SqlTypeConversion[T] = {
    new SqlTypeConversion[T] {
      def getType: SqlType = {
        val tuples = elems.map((name, t) => (name -> t.getType))
        tuples match {
          case head::tail => SqlStruct(head :: tail)
          case Nil => SqlStruct(List.empty[(String, SqlType)])
        }
      }
    }
  }

  //inline def labelFromMirror[A](using m: Mirror.Of[A]): String = constValue[m.MirroredLabel]

  /** Used to get a list of names of the values inside of a product
    */
  inline def getElemLabels[A <: Tuple]: List[String] = inline erasedValue[A] match {
    case _: EmptyTuple => Nil // stop condition - the tuple is empty
    case _: (head *: tail) =>  // yes, in scala 3 we can match on tuples head and tail to deconstruct them step by step
      val headElementLabel = constValue[head].toString // bring the head label to value space
      val tailElementLabels = getElemLabels[tail] // recursive call to get the labels from the tail
      headElementLabel :: tailElementLabels // concat head + tail
  }

  /** Derives anything asked as SqlTypeConversion. Only products are implemented
    */
  inline given derived[T](using m: Mirror.Of[T]): SqlTypeConversion[T] = {
    lazy val elemInstances = summonAll[m.MirroredElemLabels, m.MirroredElemTypes]
    val labels = getElemLabels[m.MirroredElemLabels]
    val zip = labels zip elemInstances
    inline m match
      case s: Mirror.SumOf[T] => ??? //the library only works for products
      case p: Mirror.ProductOf[T] => instanceStructProduct(p, zip)
  }

  inline def summonAll[Names <: Tuple, Types <: Tuple]: List[SqlTypeConversion[_]] = {
    inline (erasedValue[Names], erasedValue[Types]) match {
      case _: (_, EmptyTuple) => Nil
      case _: ((n *: ns), (t *: ts)) =>
        summonInline[SqlTypeConversion[t]] :: summonAll[ns, ts]
    }
  }

}