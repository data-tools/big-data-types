package org.datatools.bigdatatypes.conversions

import org.datatools.bigdatatypes.types.basic._

/** Type class to convert instances into [[SqlType]]
  *
  * @tparam A is a Scala type
  */
trait SqlInstanceConversion[-A] {

  /**
    * @param value an instance that implements SqlInstanceConversion
    * @return the [[SqlType]] representation of [[A]]
    */
  def getType(value: A): SqlType
}

object SqlInstanceConversion {

  /** Summoner method
    */
  def apply[A](implicit a: SqlInstanceConversion[A]): SqlInstanceConversion[A] = a

  /** Factory constructor - allows easier construction of instances
    */
  def instance[A](f: A => SqlType): SqlInstanceConversion[A] =
    new SqlInstanceConversion[A] {
      def getType(value: A): SqlType = f(value)
    }
}

