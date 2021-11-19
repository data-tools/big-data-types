package org.datatools.bigdatatypes.cassandra.parser

//TODO: This could be moved to a new Parsing module when more modules need a parser
//make in it private for now to avoid breaking changes in the future

private[parser] sealed trait ParsingError

private[parser] object ParsingError {
  case class ErrorParsingTable(msg: String) extends ParsingError
  case class ErrorParsingField(fieldName: String, msg: String) extends ParsingError
  case class ParsingErrors(errors: List[ParsingError]) extends ParsingError

  /**
    * Given a list of errors, transform them into a [[ParsingErrors]]
    * This could be useful when we want to accumulate all parsing errors
    * @param errors a list of [ParsingError]
    * @return [[ParsingError]] with all errors inside
    */
  def compactErrors(errors: Seq[ParsingError]): ParsingErrors = {
    val acc: ParsingErrors  = ParsingErrors(List.empty[ParsingError])
    errors.foldLeft(acc)((e1, e2) => ParsingErrors(e1.errors :+ e2))
  }

  implicit class ExtensionForError(error: ParsingError) {
    /**
      * Add an error to a [[ParsingError]]. It converts the error into a [[ParsingErrors]]
      * @param newError the error to be added
      * @return [[ParsingErrors]] with current errors + the new one
      */
    def addError(newError: ParsingError): ParsingErrors = {
      error match {
        case ParsingErrors(errors) => ParsingErrors(errors :+ newError)
        case _ => ParsingErrors(List(newError))
      }
    }
  }
}