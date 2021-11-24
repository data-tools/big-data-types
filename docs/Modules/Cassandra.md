---
sidebar_position: 5
---
# Cassandra
Cassandra module is using the [Query builder in the DataStax Java Driver](https://github.com/datastax/java-driver/tree/4.x/manual/query_builder/schema)
to return `CreateTable` objects from product types such a Case Classes (or other types from the library, like Spark Schemas)
This module doesn't have anything to connect directly to a Cassandra instance,
this is only a bridge between Scala product types (and other types from the library) to the DataStax Query Builder

In other words, with this module you can use Case Classes (or any other type in the library) to create a `CreateTable`
object that you can use to interact with a real Cassandra instance

## Case Classes to Cassandra CreateTable
Quick example using Case Classes
```scala
case class MyTable(id: String, name: String, age: Int)
val table: CreateTable = CassandraTables.table[MyTable]("TableName", "id")
```

## Other types to Cassandra CreateTable
```scala
val sparkSchema = sparkDataFrame.schema
CassandraTables.table(sparkSchema, "tableName", "primaryKeyFieldName")
```

### Or from a product type (case class)
```scala
case class MyModel(a: Int, b: String)
val instance = MyModel(1, "test")
instance.asCassandra("TestTable", "primaryKey")
```

### Or from an instance of other types
```scala
val mySparkDataframe: Dataframe = ???
mySparkDataframe.schema.asCassandra("TestTable", "primaryKey")
```
