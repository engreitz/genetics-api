package models

import reflect.runtime.universe._

object Functions {
  val defaultPaginationSize: Option[Int] = Some(500000)

  /** the indexation of the pagination starts at page number 0 set by pageIndex and takes pageSize chunks
    * each time. The default pageSize is defaultPaginationSize
    * @param pageIndex ordinal of the pages chunked by pageSize. It 0-start based
    * @param pageSize the number of elements to get per page. default number defaultPaginationSize
    * @return Clickhouse SQL dialect string to be used when you want to paginate
    */
  def parsePaginationTokens(pageIndex: Option[Int], pageSize: Option[Int] = defaultPaginationSize): String = {
    val pair = List(pageIndex, pageSize).map(_.map(_.abs).getOrElse(0))

    pair match {
      case List(0, 0) => s"LIMIT ${defaultPaginationSize.get}"
      case List(0, s) => s"LIMIT $s"
      case List(i, 0)  => s"LIMIT ${i*defaultPaginationSize.get}, ${defaultPaginationSize.get}"
      case List(i, s) => s"LIMIT ${i*s} , $s"
    }
  }

  /** https://stackoverflow.com/questions/12218641/scala-what-is-a-typetag-and-how-do-i-use-it/12232195#12232195 */
  def toSeqString(s: String): Seq[String] = {
    if (s.length > 2)
      s.slice(1, s.length - 1).split(",").map(t => t.slice(1, t.length - 1))
    else
      Seq.empty
  }

  def toSeqDouble(s: String): Seq[Double] = {
    if (s.length > 2)
      s.slice(1, s.length - 1).split(",").map(_.toDouble)
    else
      Seq.empty
  }

  def toSeqInt(s: String): Seq[Int] = {
    if (s.length > 2)
      s.slice(1, s.length - 1).split(",").map(_.toInt)
    else
      Seq.empty
  }
}