package org.datatools.bigdatatypes.bigquery

import java.lang
import scala.collection.JavaConverters.{asJavaIterableConverter, iterableAsScalaIterableConverter}

object JavaConverters {

  def toJava[A](value: Seq[A]): lang.Iterable[A] = value.asJava
  def toScala[A](value: lang.Iterable[A]): List[A] = value.asScala.toList
}
