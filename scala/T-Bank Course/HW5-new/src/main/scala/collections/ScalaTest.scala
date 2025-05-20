package collections

//trait Animal
//trait Cat extends Animal
//
//object Cat1 extends Animal
object ScalaTest {
  def main(args: Array[String]): Unit = {
    sealed trait Option2[-T]

    case class Some2[T](value: T) extends Option2[T]
    case object None2 extends Option2[Any]

    def getOption2: Option2[String] = ???

    getOption2 match {
      case Some2(value) =>
      // what is the type of `value`?
      case None2 =>
    }

  }
}
