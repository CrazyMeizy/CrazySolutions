package ventilation

import scala.annotation.tailrec

sealed trait Stack {
  self =>
  def push(value: Int): Stack = {
    Node(value, self)
  }
  def pop(): Stack = self match {
    case EmptyStack    => EmptyStack
    case Node(_, tail) => tail
  }
  def max_peak(): Int = self match {
    case EmptyStack    => Int.MinValue
    case Node(head, _) => head
  }
}
case object EmptyStack extends Stack

case class Node(head: Int, tail: Stack) extends Stack

object Ventilation {
  def solve1(list: Seq[Int], width: Int): Seq[Int] = {
    list.sliding(width).map(_.max).toList
  }
  def solve2(list: Seq[Int], width: Int): Seq[Int] = {

    @tailrec
    def makeWindow(stack: Stack, list: Seq[Int], width: Int, rMax: Int): (Stack, Seq[Int], Int) = width match {
      case x if x > 0 => makeWindow(stack.push(list.head), list.tail, width - 1, math.max(rMax, list.head))
      case _          => (stack, list, rMax)
    }
    @tailrec
    def throwOver(lStack: Stack, rStack: Stack, lMax: Int): Stack = rStack match {
      case EmptyStack     => lStack.pop()
      case Node(value, _) => throwOver(lStack.push(math.max(value, lMax)), rStack.pop(), math.max(lMax, value))
    }
    @tailrec
    def findMaxes(lStack: Stack, rStack: Stack, rMax: Int, list: Seq[Int], ansList: Seq[Int]): Seq[Int] = {
      list match {
        case Nil => ansList
        case _ =>
          lStack match {
            case EmptyStack =>
              val newLStack = throwOver(EmptyStack, rStack, Int.MinValue)
              findMaxes(
                newLStack,
                EmptyStack.push(list.head),
                list.head,
                list.tail,
                ansList.appended(math.max(list.head, newLStack.max_peak()))
              )
            case _ =>
              val newLstack = lStack.pop()
              val lastMin = newLstack.max_peak()
              val newRMax = math.max(list.head, rMax)
              findMaxes(
                newLstack,
                rStack.push(list.head),
                newRMax,
                list.tail,
                ansList.appended(math.max(newRMax, lastMin))
              )
          }
      }
    }
    list match {
      case Nil => Nil
      case _ =>
        val (rStack, newList, rMax) = makeWindow(EmptyStack, list, width, Int.MinValue)
        findMaxes(EmptyStack, rStack, rMax, newList, List(rMax))
    }
  }

  def main(args: Array[String]): Unit = {

    val degrees = List(3, 1, 4, 1, 5)
    println(solve1(degrees, 1))
    println(solve2(degrees, 1))
  }
}
