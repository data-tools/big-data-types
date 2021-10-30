---
sidebar_position: 6
---
# Transformations
Transformations can be applied easily during conversions. For example, field names can be modified.

## Implicit Formats
Formats can handle different configurations that we want to apply to schemas, like transforming field names,
defining precision for numeric types and so on.

They can be used by creating an implicit val with a Formats class or by importing one of the available implicit vals in `Formats` object

### DefaultFormats
`DefaultFormats` is a trait that applies no transformation to field names
To use it, you can create an implicit val:
```scala
import org.datatools.bigdatatypes.formats.{Formats, DefaultFormats}
implicit val formats: Formats = DefaultFormats
```
or just import the one available:
```scala
import org.datatools.bigdatatypes.formats.Formats.implicitDefaultFormats
```


### SnakifyFormats
`SnakifyFormats` is a trait that converts camelCase field names to snake_case names
To use it, you can create an implicit val:
```scala
import org.datatools.bigdatatypes.formats.{Formats, SnakifyFormats}
implicit val formats: Formats = SnakifyFormats
```
or just import the one available:
```scala
import org.datatools.bigdatatypes.formats.Formats.implicitSnakifyFormats
```

### Creating a custom Formats
Formats can be extended, so if we want to transform keys differently, for example adding a suffix to all of our fields
```scala
import org.datatools.bigdatatypes.formats.Formats
trait SuffixFormats extends Formats {
  override def transformKey[A <: SqlType](name: String, t: A): String = key + "_at"
}
object SuffixFormats extends SuffixFormats
```
All your field names will have "_at" at the end.

`t` is the Type of the field so you can decide how to transform your keys based on the type

