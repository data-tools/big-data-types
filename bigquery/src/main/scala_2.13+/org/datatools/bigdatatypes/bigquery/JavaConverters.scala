package org.datatools.bigdatatypes.bigquery

import java.lang
import scala.jdk.CollectionConverters.{IterableHasAsJava, IterableHasAsScala}

object JavaConverters {

  def toJava[A](value: List[A]): lang.Iterable[A] = value.asJava
  def toScala[A](value: lang.Iterable[A]): List[A] = value.asScala.toList
}
