---
sidebar_position: 6
---
# Circe (JSON)

[Circe](https://circe.github.io/circe/) is a JSON library for Scala.

The Circe module of this library allows to convert `Json` objects (from [Circe](https://circe.github.io/circe/)) to any other type in the library.
:::caution
For now only conversions from Circe to other types are available. Other types to Circe are not ready yet.
:::

:::info
Json objects do not have very concrete types, meaning that `number` is a type, 
but more specific types like `integer`, `float` or others do not exists. 
Because of that, any conversion between types will convert `number` into `Decimal` types, 
as `Decimal` is the only one that can ensure the precision of any arbitrary number 
:::
<details>
    <summary>About Circe and private types</summary>
    <p>
    Circe has more specific types than `JNumber`, like `JLong`, `JDouble` and others, 
    but all of them are private to Circe itself, so we can not use them, not even for matching types during conversions. 
    In any case, even if we were able to use them, when parsing a JSON string (probably most of the cases) 
    we can not detect the specific types, we could only guess them from the data.
    </p>
</details>


```scala
import io.Circe.Json
import org.datatools.bigdatatypes.circe.CirceTypeConversion.*
import org.datatools.bigdatatypes.formats.Formats.implicitDefaultFormats
import org.datatools.bigdatatypes.spark.SqlInstanceToSpark.InstanceSyntax
import org.datatools.bigdatatypes.bigquery.SqlInstanceToBigQuery.*

  val circeJson: Json = Json.fromFields(List(
    ("id", Json.fromString("test")),
    ("foo", Json.fromString("test")),
    ("bar", Json.fromInt(1))
  ))

  val sparkSchema: StructType = circeJson.asSparkSchema
  val bqSchema: Schema = circeJson.asBigQuery.schema
```

Or if you do it from a JSON parsed using Circe:
```scala
import io.circe._, io.circe.parser._

import org.datatools.bigdatatypes.circe.CirceTypeConversion.*
import org.datatools.bigdatatypes.formats.Formats.implicitDefaultFormats
import org.datatools.bigdatatypes.spark.SqlInstanceToSpark.InstanceSyntax
import org.datatools.bigdatatypes.bigquery.SqlInstanceToBigQuery.*

val rawJson: String = """
{
  "foo": "bar",
  "baz": 123,
  "list of stuff": [ 4, 5, 6 ]
}
"""
val parseResult = parse(rawJson)
// parseResult: Either[ParsingFailure, Json]
val sparkSchema = parseResult.map(j => j.asSparkSchema)
// sparkSchema: Either[ParsingFailure, StructType]
val bqSchema = parseResult.map(j => j.asBigQuery.schema)
// bqSchema: Either[ParsingFailure, Schema]
```

---
