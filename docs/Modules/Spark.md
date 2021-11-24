---
sidebar_position: 4
---
# Spark

## Spark Schema from Case Class

With Spark module, Spark Schemas can be created from Case Classes or from any other type of the library.
::: INFO
Spark is not available for Scala 3, so, this module only works with Scala 2.12 and Scala 2.13
:::

```scala
import org.apache.spark.sql.types.StructType
import org.datatools.bigdatatypes.spark.SparkSchemas
//an implicit Formats class is needed, defaultFormats does no transformations
//it can be created as implicit val instead of using this import
import org.datatools.bigdatatypes.formats.Formats.implicitDefaultFormats

case class MyModel(myInt: Integer, myString: String)
val schema: StructType = SparkSchemas.schema[MyModel]
```
It works for Options, Sequences and any level of nested objects

Also, a Spark Schema can be extracted from a Case Class instance
```scala
val model = MyModel(1, "test")
model.asSparkSchema
```

### Create a Dataframe
```scala
case class Dummy(myInt: Int, myString: String)

implicit val default: Formats = DefaultFormats
val schema = SparkSchemas.schema[Dummy]
val df = spark.read.schema(schema).json("dummy.json")
df.show(4)
/*
+-----+--------+
|myInt|myString|
+-----+--------+
|    1|    test|
|    2|   test2|
|    3|   test3|
|    4|   test4|
+-----+--------+
*/
```

### Spark Schema from Multiple Case Classes
Also, a schema can be created from multiple case classes.
As an example, it could be useful for those cases where we read data using a Case Class,
and we want to append some metadata fields, but we don't want to create another Case Class with exactly the same fields plus a few more.
```scala
import java.sql.Timestamp
import org.apache.spark.sql.types.StructType
import org.datatools.bigdatatypes.spark.SparkSchemas
import org.datatools.bigdatatypes.formats.Formats.implicitDefaultFormats
 
case class MyModel(myInt: Integer, myString: String)
case class MyMetadata(updatedAt: Timestamp, version: Int)
val schema: StructType = SparkSchemas.schema[MyModel, MyMetadata]
/*
schema =
 List(
    StructField(myInt, IntegerType, false), 
    StructField(myString, StringType, false)
    StructField(updatedAt, TimestampType, false)
    StructField(version, IntegerType, false)
   )
*/
```

Another example, creating a Dataframe

```scala
case class Dummy(myInt: Int, myString: String)
case class Append(myTimestamp: Timestamp)

implicit val default: Formats = DefaultFormats
val schema = SparkSchemas.schema[Dummy, Append]
val df = spark.read.schema(schema).json("my_file.json")
df.show(4)
/*
+------+---------+-------------------+
|my_int|my_string|       my_timestamp|
+------+---------+-------------------+
|     1|     test|2021-01-24 10:07:39|
|     2|    test2|2021-01-24 10:07:39|
|     3|    test3|2021-01-24 10:07:39|
|     4|    test4|2021-01-24 10:07:39|
+------+---------+-------------------+
*/
```

### Spark Schema from other types

```scala
val myBigQuerySchema: Schema = ???
val schema: StructType = myBigQuerySchema.asSparkSchema
```
::: TIP
There are a few imports that have to be included in order to use this kind of transformations, depending on the types. 

IDEs should be able to find them.
:::

## Field transformations
Also, custom transformations can be applied to field names, something that usually is quite hard to do with Spark Datasets.
For example, working with CamelCase Case Classes but using snake_case field names in Spark Schema.

```scala
import org.apache.spark.sql.types.StructType
import org.datatools.bigdatatypes.spark.SparkSchemas
//implicit formats for transform keys to snake_case
import org.datatools.bigdatatypes.formats.Formats.implicitSnakifyFormats

case class MyModel(myInt: Integer, myString: String)
val schema: StructType = SparkSchemas.schema[MyModel]
/*
schema =
 List(
    StructField(my_int, IntegerType, false), 
    StructField(my_string, StringType, false)
   )
*/
```

---
