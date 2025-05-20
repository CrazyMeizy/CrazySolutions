package task2

import cats.Eval

object EvalTricks {

  def fib(n: Int): Eval[BigInt] = {
    def helpFunc(acc1: BigDecimal, acc2: BigDecimal, k: Int): Eval[BigDecimal] = {
      k match {
        case 0 => Eval.now(acc2)
        case _ => Eval.defer(helpFunc(acc2 + acc1, acc1, k - 1))
      }
    }
    Eval.now(helpFunc(1, 0, n).value.toBigInt)
  }
  def foldRight[A, B](as: List[A], acc: Eval[B])(fn: (A, Eval[B]) => Eval[B]): Eval[B] = {
    as match {
      case Nil          => acc
      case head :: tail => Eval.defer(fn(head, foldRight(tail, acc)(fn)))
    }
  }
}
