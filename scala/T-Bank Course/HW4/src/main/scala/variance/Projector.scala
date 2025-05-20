package variance

import scala.annotation.tailrec
import scala.util.Random

trait Converter[-S] {
  def convert(value: S): String
}

trait Slide[+R] {
  def read: (Option[R], Slide[R])
}

class Projector[R](converter: Converter[R]) {
  def project(screen: Slide[R]): String = {
    @tailrec
    def func(screen: Slide[R], text: StringBuilder): StringBuilder = {
      screen.read match {
        case (Some(value), screen) => func(screen, text.append(converter.convert(value)))
        case (None, _)             => text
      }
    }

    func(screen, new StringBuilder()).toString()
  }
}

class WordLine(val word: String)

object LineConverter extends Converter[WordLine] {
  override def convert(value: WordLine): String = value.word + "\n"
}

class RedactedWordLine(val redactionFactor: Double, word: String) extends WordLine(word)

object RedactedLineConverter extends Converter[RedactedWordLine] {
  private val symbol: String = "\u2588"

  override def convert(value: RedactedWordLine): String = Random.nextDouble() match {
    case x if x <= value.redactionFactor => value.word + '\n'
    case _                               => symbol * value.word.length + '\n'
  }
}

class HelloSlide[R <: WordLine](lines: Seq[R]) extends Slide[R] {
  self =>
  override def read: (Option[R], HelloSlide[R]) = lines match {
    case Nil => (None, self)
    case _   => (Some(lines.head), new HelloSlide(lines.tail))
  }
}
