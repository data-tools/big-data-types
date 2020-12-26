package org.datatools.bigdatatypes.bigquery

import java.lang

import scala.collection.JavaConverters.asJavaIterableConverter

object JavaConverters {

  def toJava[A](value: List[A]): lang.Iterable[A] = value.asJava
}
