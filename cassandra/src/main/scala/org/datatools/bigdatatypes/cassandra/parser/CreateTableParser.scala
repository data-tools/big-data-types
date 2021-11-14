package org.datatools.bigdatatypes.cassandra.parser

import cats.parse.Rfc5234.sp

object CreateTableParser extends App {

  import cats.data.NonEmptyList
  import cats.parse.Rfc5234.alpha
  import cats.parse.Parser

  val p: Parser[String] = alpha.rep.map((l: NonEmptyList[Char]) => l.toList.mkString)

  val p2: Parser[String] = alpha.rep.string
  val p3: Parser[String] = alpha.repAs[String]

  val parenthesis1: Parser[Unit] = Parser.char('(')
  val parenthesis2: Parser[Unit] = Parser.char(')')
  val comma: Parser[Unit] = Parser.char(',')

  val words = (alpha.rep ~ sp.rep0).rep
  val wordWithComma = (words ~ comma.rep0).rep

  val pk = Parser.string("PRIMARY KEY").rep0

  val test = words ~ parenthesis1 *> pk ~ wordWithComma.string <* pk *> wordWithComma.string <* parenthesis2
  val fieldsString: Parser[String] = words ~ parenthesis1 *> wordWithComma.string <* parenthesis2

  val fieldsString2 = words ~ parenthesis1 *> pk ~ wordWithComma.string <* pk *> wordWithComma.string <* parenthesis2

  val text =
    "CREATE TABLE testtable (myint int,mylong bigint PRIMARY KEY,myfloat float,mydouble double,mydecimal decimal,myboolean boolean,mystring text)"

  val a: Either[Parser.Error, Array[String]] = fieldsString.parse(text).map(s => s._2.split(","))

  a match {
    case Left(value)  => ???
    case Right(value) => value.foreach(println)
  }
  println(fieldsString.parse(text))

}

object Test extends App {

  val text =
    "CREATE TABLE testtable (myint int,mylong bigint PRIMARY KEY,myfloat float,mydouble double,mydecimal decimal,myboolean boolean,mystring text)"

  val regex = """\((.*)\)""".r
  val pk = " PRIMARY KEY"

  case class ValueType(value: String, t: String)

  val a: Option[Array[ValueType]] = regex
    .findFirstIn(text)
    .map(s => s.substring(1).substring(0, s.length - 1).replace(pk, ""))
    .map(s => s.split(','))
    .map(l => l.map{s =>
      val both = s.split(' ')
      ValueType(both.head, both.tail.head)
    })


}
