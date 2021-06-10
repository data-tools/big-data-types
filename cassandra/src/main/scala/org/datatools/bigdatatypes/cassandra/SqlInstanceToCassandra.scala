package org.datatools.bigdatatypes.cassandra

import com.datastax.oss.driver.api.core.`type`.DataType

/** Type class to convert generic SqlTypes received as instance into Cassandra specific fields
  * This uses [[SqlTypeToCassandra]] to create Cassandra Fields
  *
  * @tparam A the type we want to obtain an schema from
  */
trait SqlInstanceToCassandra[A] {

  /** @param value an instance of [[A]]
    * @return a list of [[(String, DataType)]]s that represents [[A]]
    */
  def cassandraFields(value: A): List[(String, DataType)]
}

//TODO: Continue here, we should implement the type class