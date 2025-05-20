package fibonacci

import java.io.{
  BufferedReader,
  BufferedWriter,
  FileOutputStream,
  InputStreamReader,
  OutputStreamWriter
}
import scala.annotation.tailrec

object Fibonacci {

  def fibonacci(limit: Long): BigInt = {
    assert(limit >= 0, throw IllegalArgumentException())
    def matrixMultiply(
      a: ((BigInt, BigInt), (BigInt, BigInt)),
      b: ((BigInt, BigInt), (BigInt, BigInt))
    ): ((BigInt, BigInt), (BigInt, BigInt)) =
      (
        (a._1._1 * b._1._1 + a._1._2 * b._2._1, a._1._1 * b._1._2 + a._1._2 * b._2._2),
        (a._2._1 * b._1._1 + a._2._2 * b._2._1, a._2._1 * b._1._2 + a._2._2 * b._2._2)
      )
    @tailrec
    def fib_temp(
      limit: Long,
      num: Long = 1,
      mul: ((BigInt, BigInt), (BigInt, BigInt)) = ((1, 0), (0, 1)),
      fib: ((BigInt, BigInt), (BigInt, BigInt)) = ((1, 1), (1, 0))
    ): BigInt = {
      if (num > limit) mul._2._1
      else {
        val newFib =
          matrixMultiply(fib, fib)
        if ((num & limit) != 0) {
          val newMul = matrixMultiply(mul, fib)
          fib_temp(limit, num << 1, newMul, newFib)
        } else
          fib_temp(limit, num << 1, mul, newFib)
      }
    }
    if (limit == 0) 0
    else fib_temp(limit)
  }

  def main(args: Array[String]): Unit = {
    val x: String = fibonacci(10000000).toString()
    println(x.length)
    val writer: BufferedWriter = new BufferedWriter(
      new OutputStreamWriter(
        new FileOutputStream(
          "C:\\Users\\crazy\\IdeaProjects\\HW2\\src\\main\\scala\\fibonacci\\porno.txt"
        ),
        "UTF-8"
      )
    )
    writer.write(x)
    writer.close()
  }

}
