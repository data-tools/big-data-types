package org.datatools.bigdatatypes.conversions

import org.datatools.bigdatatypes.basictypes.SqlType

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
  def apply[A](using a: SqlInstanceConversion[A]): SqlInstanceConversion[A] = a

}

