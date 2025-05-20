package task2

import EvalTricks._
import cats.Eval
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class EvalTricksSpec extends AnyFlatSpec with Matchers {
  "fib" should "not overflow the stack" in {
    fib(2_000_000).value
  }

  val fibonacciTestCases: Map[Int, BigInt] = Map(
    0 -> 0,
    1 -> 1,
    2 -> 1,
    3 -> 2,
    4 -> 3,
    5 -> 5,
    6 -> 8,
    7 -> 13,
    8 -> 21,
    9 -> 34,
    10 -> 55,
    11 -> 89,
    12 -> 144,
    13 -> 233,
    14 -> 377,
    15 -> 610,
    16 -> 987,
    17 -> 1597,
    18 -> 2584,
    19 -> 4181,
    20 -> 6765,
    21 -> 10946,
    22 -> 17711,
    23 -> 28657,
    24 -> 46368,
    25 -> 75025,
    26 -> 121393,
    27 -> 196418,
    28 -> 317811,
    29 -> 514229,
    30 -> 832040,
    31 -> 1346269,
    32 -> 2178309,
    33 -> 3524578,
    34 -> 5702887,
    35 -> 9227465,
    36 -> 14930352,
    37 -> 24157817,
    38 -> 39088169,
    39 -> 63245986,
    40 -> 102334155
  )

  fibonacciTestCases.foreach { case (n, fibn) =>
    registerTest(s"fib should return correct value for n = $n") {
      fib(n).value shouldBe fibn
    }
  }

  "foldRight" should "not overflow the stack" in {
    foldRight((1 to 2_000_000).toList, Eval.now(0))((a, b) => b.map(_ + a)).value
  }

  it should "return the correct value" in {
    val list = (1 to 100).toList
    foldRight(list, Eval.now(0))((a, b) => b.map(a - _)).value shouldBe list.foldRight(0)(_ - _)
  }
}
