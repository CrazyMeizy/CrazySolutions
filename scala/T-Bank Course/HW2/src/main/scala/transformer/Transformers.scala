package transformer

object Transformers {

  val duplicate: String => String = _.repeat(2)

  val divide: String => String = str => str.take(str.length / 2)

  val revert: String => String = _.reverse

  val closure: String => (String => String) => String = str => func => func(str)

}
