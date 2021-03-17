This is a guide on how to add a new type to the library

- [How to develop a new type](#how-to-develop-a-new-type)
  * [How it works](#how-it-works)
    + [SqlType ADT](#sqltype-adt)
    + [Conversion / Reverse Conversion](#conversion---reverse-conversion)
      - [Conversion](#conversion)
      - [Reverse Conversion](#reverse-conversion)
  * [How to do it](#how-to-do-it)
    + [Create a new subproject in SBT](#create-a-new-subproject-in-sbt)
    + [Conversion: Type Class - SqlType -> New Type](#conversion--type-class---sqltype----new-type)
      - [Defining the syntax](#defining-the-syntax)
      - [Implementing the Type Class](#implementing-the-type-class)
        * [Mode inside Types](#mode-inside-types)
          + [Everything together](#everything-together)
    + [Conversion: Type Class - SqlInstance -> New Type](#conversion--type-class---sqlinstance----new-type)
    + [Reverse conversion, Type Class implementation](#reverse-conversion--type-class-implementation)
    + [Type Conversion](#type-conversion)


# How to develop a new type

Adding a new type to the library will allow conversions from any developed type into the new one 
and from the new one into any of the others

## How it works

There is an ADT (sealed trait) called `SqlType` that is used as a generic type for any transformation. 
It works as a bridge, so any developed type can be transformed into SqlType and SqlType can be converted into specific types.

By doing so, when we add a new type, we don't need to implement conversions between all the types, we only need to implement
`Conversion` from our type to `SqlType` and `Reverse Conversion` from `SqlType` to our type, 
and **we will get automatically conversion from / to the rest of the types in the library**.

An important note of nomenclature:

- We call `Conversion` to anything that converts `into the geneic SqlType`
- We call `Reverse Conversion` to anything that converts `from generic SqlType into specific`

### SqlType ADT
[SqlType](https://github.com/data-tools/big-data-types/blob/main/core/src/main/scala/org/datatools/bigdatatypes/types/basic/SqlType.scala) 
is an Algebraic Data Type that aims to generalize all the cases that we can have from different types. 

It is used internally for all the conversions.

It consists in a few case class with a `Mode` (Nullable, Required or Repeated) and one SqlStruct that contains a 
map of `String` and `SqlType`, being the String the name of the "field"


### Conversion / Reverse Conversion
There are 2 type of conversions that we should cover

- `Conversion` = Generic SqlType -> Our Type
- `Reverse Conversion` = Our type -> Generic SqlType

#### Conversion 
`SqlType -> our type` = `All types -> our type`
For the _normal_ _Conversion_ we have to create two new _Type Classes_ 
The **Core** of the library already have a _Type Class_ that converts Case Classes into `SqlTypes` so our new _Type Classes_ only need to derive from there.

- `SqlTypeToOurType` for only types (converting a Case Class into our new type) -> e.g: `Conversion[A].myNewType`
- `SqlInstanceToOurType` for convert instances into our new type -> e.g: `Conversion[A](myInstance: A).myNewType` 
(`A` could be an instance of any implemented type in the library)

One will be based on the other one, so don't worry, one will be really short.

#### Reverse Conversion
`our type -> SqlType` = `our type -> all types`

In order to implement the conversion from our new type to `SqlType` we have to implement 
an existing _Type Class_ called `SqlTypeConversion`

By doing this, we will get automatically conversion to the rest of the types of the library


## How to do it

As covered in [Conversion](#conversion), we have to implement 2 types classes, one for types, another for instances.
Both will derive `SqlTypeConversion` type class into our specific type and by doing so, we will get automatically all conversions into our new type

### Create a new subproject in SBT
For a new type, we need a new submodule in SBT, by doing so, we don't force anyone to import all implemented types in the library, so anyone can pick only the types that is interested in

An example from Spark. This defines a new module `big-data-types-spark`.
Options can be changed as desired:
- publishSettings seems to be obvious as we want to publish the new type
- crossScalaVersions specify the different Scala versions that the artifact will be created. The more the better
- crossVersionSharedSources is a method to specify different code for Scala 2.13- or 2.13+
- Dependencies, defined in a variable, the ones that we need
- dependsOn for depend on Core for compile and for tests. (There are a few useful classes in Core/Tests that we can use to test our new type)

```scala
lazy val spark = (project in file("spark"))
  .settings(
    name := projectName + "-spark",
    publishSettings,
    crossScalaVersions := List(scala212),
    crossVersionSharedSources,
    libraryDependencies ++= sparkDependencies
  )
  .dependsOn(core % "test->test;compile->compile")
```

Add the dependency to the root project, inside `aggregate`:

```scala
//Project settings
lazy val root = (project in file("."))
  .configs(IntegrationTest)
  .settings(noPublishSettings)
  .aggregate(
    core,
    bigquery,
    spark,
    examples
  )
```

Now, you can create a new root folder with your type name with the typical structure (src/main/scala_ ...)

### Conversion: Type Class - SqlType -> New Type

#### Defining the syntax
- First, create a new package called something like `org.datatools.bigdatatypes.{mynewtype}`
- Add the Type Class (trait) with a method `() => A` being `A` our new type/schema
For example:
```scala
trait SqlTypeToSpark[A] {

  /** @return a list of [[StructField]]s that represents [[A]]
    */
  def sparkFields: List[StructField]
}
```
In this case, `sparkFields` will be the name of the method that we will use to obtain our new type. 
Pick a representative name but don't worry too much, at the end we can create a wrapper class that we'll use our _Type Class_.

#### Implementing the Type Class
We can start by creating a companion object with `apply` and a factory constructor that will make easier construction of instances
````scala
object SqlTypeToSpark {

  /** Summoner method */
  def apply[A](implicit instance: SqlTypeToSpark[A]): SqlTypeToSpark[A] = instance

  /** Factory constructor - allows easier construction of instances */
  def instance[A](fs: List[StructField]): SqlTypeToSpark[A] =
    new SqlTypeToSpark[A] {
      def sparkFields: List[StructField] = fs
    }
}
````
Then, the code will depend a lot on the type that we are constructing, but we have to think that we are converting `SqlType`s into our type so,
we should do a method with like `SqlType => ourType`

As the types usually can be recursive (nested objects) we can start defining a method for the recursion that:
- Will take an SqlType 
- Will use implicit `Formats` as an optional key transformation
- Will return our desired type

```scala
  /** Creates the schema (list of fields)
    * Applies an implicit [[Formats.transformKeys]] in the process
    * @param sqlType [[SqlType]]
    * @param f [[Formats]] to apply while constructing the schema
    * @return List of [[StructField]] representing the schema of the given type
    */
  private def getSchema(sqlType: SqlType)(implicit f: Formats): List[StructField] = sqlType match {
    case SqlStruct(Nil, _) => Nil
    case SqlStruct((name, sqlType) :: records, mode) =>
      getSchemaWithName(f.transformKeys(name), sqlType) :: getSchema(SqlStruct(records, mode))
  }
```
And another method (`getSchemaWithName` in this example) to specify the specific types:
In this case, we are showing an example from BigQuery as it seems simpler to understand:
```scala
/** Basic SqlTypes conversions to BigQuery Fields
    */
  private def getSchemaWithName(name: String, sqlType: SqlType)(implicit f: Formats): Field = sqlType match {
    case SqlInt(mode) =>
      Field.newBuilder(name, StandardSQLTypeName.INT64).setMode(sqlModeToBigQueryMode(mode)).build()
    case SqlLong(mode) =>
      Field.newBuilder(name, StandardSQLTypeName.INT64).setMode(sqlModeToBigQueryMode(mode)).build()
    case SqlFloat(mode) =>
      Field.newBuilder(name, StandardSQLTypeName.FLOAT64).setMode(sqlModeToBigQueryMode(mode)).build()
 ...
 ...
}
```
Same example from Spark:
```scala
  /** Basic SqlTypes conversions to Spark Types
    */
  private def getSchemaWithName(name: String, sqlType: SqlType)(implicit f: Formats): StructField = sqlType match {
    case SqlInt(mode) =>
      StructField(name, sparkType(mode, IntegerType), isNullable(mode))
    case SqlLong(mode) =>
      StructField(name, sparkType(mode, LongType), isNullable(mode))
    case SqlFloat(mode) =>
      StructField(name, sparkType(mode, FloatType), isNullable(mode))
  ...
  ...
  }
```

We have to understand `Mode` at this point.

##### Mode inside Types
There are different ways to handle Arrays or repeated values in a structure and two are the most common.
- Define a Mode that can be `Nullable`, `Required` or `Repeated` (this how `SqlType` works).
-- An Array will look like a normal type with `Repeated` mode -> e.g: `SqlInt(mode = Repeated)`
- Define an `Array` structure that has another type inside
-- An Array will look like `Array(Int)` or similar -> e.g: Spark -> `ArrayType(IntegerType)`

So, in BigQuery example, we use `sqlModeToBigQueryMode` method as following:
```scala
  private def sqlModeToBigQueryMode(sqlTypeMode: SqlTypeMode): Mode = sqlTypeMode match {
    case Nullable => Mode.NULLABLE
    case Repeated => Mode.REPEATED
    case Required => Mode.REQUIRED
  }
```
Pretty straight forward.

In case of structs with Arrays like Spark, we use something like:
```scala
  private def sparkType(mode: SqlTypeMode, sparkType: DataType): DataType = mode match {
    case Repeated => ArrayType(sparkType, containsNull = isNullable(mode))
    case _        => sparkType
  }
```
Where if the mode is not repeated, we return the value, if it's repeated, we create an array with the value.

###### Everything together
Finally, we create a method that derives the instance from `SqlType` into our type, using our new methods. As simple as:
```scala
  /** Instance derivation via SqlTypeConversion.
    */
  implicit def fieldsFromSqlTypeConversion[A: SqlTypeConversion](implicit f: Formats): SqlTypeToSpark[A] =
    instance(getSchema(SqlTypeConversion[A].getType))
```
In order to understand it, 
- `SqlTypeConversion[A].getType` will return the `SqlType` for any type that implements `SqlTypeConversion`,
so we don't need to care about other types.
- `getSchema` is our new method that generates the complete type
- `instance` is our factory constructor that makes easier to construct the type class implementation

And, that's it! Now **we can convert any Case Class into our new type**!

To do it, e.g:
```scala
SqlTypeToOurType[MyCaseClass].getMyNewType
```
Again, if you wonder how this happens, it's because [SqlTypeConversion](https://github.com/data-tools/big-data-types/blob/main/core/src/main/scala/org/datatools/bigdatatypes/conversions/SqlTypeConversion.scala) 
Type Class derives any Case Class into `SqlType` using [Shapeless](https://github.com/milessabin/shapeless)


**But**, one piece is still missing. Converting types is really cool, and it happens on compiler time, 
but we also want to convert other types that live only in the running phase, like when we have an instance. 
e.g: An Spark Schema is not just a type, it's an instance of StructType, so we need to pass an instance to our new converter


### Conversion: Type Class - SqlInstance -> New Type

This will be quick as we already have methods that convert an `SqlType` into our new type, so we only need to extend them to accept an instance as argument

We should create a new Type Class called `SqlInstanceToOurType`

As before, we create the syntax (for making it easier, we should have the same method name as before)
```scala
/** Type class to convert generic SqlTypes received as instance into BigQuery specific fields
  * This uses [[SqlTypeToBigQuery]] to create BigQuery Fields
  *
  * @tparam A the type we want to obtain an schema from
  */
trait SqlInstanceToBigQuery[A] {

  /** @param value an instance of [[A]]
    * @return a list of [[Field]]s that represents [[A]]
    */
  def bigQueryFields(value: A): List[Field]
}
```
In this case, `bigQueryFields` expects an input parameter A 

Next, we create the companion object with a few methods, starting by the summoner (apply)
```scala
object SqlInstanceToBigQuery {

  /** Summoner method
    */
  def apply[A](implicit a: SqlInstanceToBigQuery[A]): SqlInstanceToBigQuery[A] = a
```

then, we add a method that will derive an instance of `SqlInstance` into our new type
```scala
  /** Instance derivation via SqlTypeConversion. It uses `getSchema` from BigQueryTypes Type Class
    */
  implicit def fieldsFromSqlInstanceConversion[A: SqlInstanceConversion](implicit f: Formats): SqlInstanceToBigQuery[A] =
    new SqlInstanceToBigQuery[A] {

      override def bigQueryFields(value: A): List[Field] =
        SqlTypeToBigQuery.getSchema(SqlInstanceConversion[A].getType(value))
    }
```
**Note!**: This is the full code of the implementation but in Scala we can reduce a lot the syntax. As there is only one method inside our Type Class, 
Scala can understand that and there is no need to write all the code, so it can be written as follows:
```scala
  implicit def fieldsFromSqlInstanceConversion[A: SqlInstanceConversion](implicit f: Formats): SqlInstanceToBigQuery[A] =
    (value: A) => SqlTypeToBigQuery.getSchema(SqlInstanceConversion[A].getType(value))
```
both codes are equivalent!

Another (optional) method more, to be able to use instances of `SqlType` directly:
```scala
  implicit def fieldsFromSqlType(implicit f: Formats): SqlInstanceToBigQuery[SqlType] =
    (value: SqlType) => SqlTypeToBigQuery.getSchema(value)
```

And that's it! We can convert an instance of any type in the library into our new type, e.g: An Spark schema into our new type by doing:
```scala
SqlInstanceToOurType[StructType](mySparkSchema).getMyNewType
```

Finally, an extension method could help to improve the syntax. (Note: Scala 3 is improving the syntax for [extension methods](http://dotty.epfl.ch/docs/reference/contextual/extension-methods.html))

Inside the same object, we create an implicit class with our new syntax:
```scala
  /** Allows the syntax myInstance.bigQueryFields for any instance of type A: SqlInstanceConversion
    */
  implicit class InstanceSyntax[A: SqlInstanceToBigQuery](value: A) {
    def bigQueryFields: List[Field] = SqlInstanceToBigQuery[A].bigQueryFields(value)
  }
```
And, we will win a new syntax like:
```scala
anyInstance.myNewType
mySparkSchema.myNewType
```


### Reverse conversion, Type Class implementation
Implement `SqlTypeConversion` type class to have conversion from the new type to `SqlType` 

### Type Conversion


Work in progress ... sorry about that



