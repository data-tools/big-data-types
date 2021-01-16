package org.datatools.bigdatatypes.bigquery

import com.google.cloud.bigquery.FieldList

import scala.collection.JavaConverters.asScalaIteratorConverter

object BigQueryFields {

  /** Used to get a List of field names from a FieldList */
  def getFieldNames(fields: FieldList): List[String] = fields.iterator().asScala.toList.map(_.getName)

}
