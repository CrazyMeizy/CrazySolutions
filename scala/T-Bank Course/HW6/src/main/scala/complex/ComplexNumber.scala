package complex

import scala.language.implicitConversions

// DO NOT CHANGE ANYTHING BELOW
final case class ComplexNumber(real: Double, imaginary: Double) {
  def *(other: ComplexNumber): ComplexNumber =
    ComplexNumber(
      (real * other.real) - (imaginary * other.imaginary),
      (real * other.imaginary) + (imaginary * other.real)
    )

  def +(other: ComplexNumber): ComplexNumber =
    ComplexNumber(real + other.real, imaginary + other.imaginary)

  def ~=(o: ComplexNumber): Boolean =
    (real - o.real).abs < 1e-6 && (imaginary - o.imaginary).abs < 1e-6
}

object ComplexNumber {
  // DO NOT CHANGE ANYTHING ABOVE
//  implicit def numericToComplex[T : Numeric](value: T): ComplexNumber = {
//    ComplexNumber(Numeric[T].toDouble(value), 0)
//  }
  given [T](using Numeric[T]): Conversion[T, ComplexNumber] with {
    def apply(value: T): ComplexNumber =
      ComplexNumber(Numeric[T].toDouble(value), 0)
  }

  extension(value: Double) {
    def i: ComplexNumber = ComplexNumber(0, value)
  }

  extension(curr: ComplexNumber) {

    def toPolarCoords: PolarForm = {
      val radius = math.hypot(curr.real, curr.imaginary)
      val angle = math.atan2(curr.imaginary, curr.real)
      PolarForm(radius, angle)
    }

    def conjugate: ComplexNumber = ComplexNumber(curr.real, -curr.imaginary)

    def getSquareRadius: Double =
      math.pow(curr.real, 2) + math.pow(curr.imaginary, 2)

    def -(other: ComplexNumber): ComplexNumber =
      ComplexNumber(curr.real - other.real, curr.imaginary - other.imaginary)

    def /(other: ComplexNumber): ComplexNumber = {
      val denominator = other.getSquareRadius
      if (denominator == 0) throw new ArithmeticException("Division by zero")
      val numerator = curr * other.conjugate
      ComplexNumber(numerator.real / denominator, numerator.imaginary / denominator)
    }
  }

  given Numeric[ComplexNumber] with {
    def plus(x: ComplexNumber, y: ComplexNumber): ComplexNumber = x + y

    def minus(x: ComplexNumber, y: ComplexNumber): ComplexNumber = x - y

    def times(x: ComplexNumber, y: ComplexNumber): ComplexNumber = x * y

    def negate(x: ComplexNumber): ComplexNumber = ComplexNumber(-x.real, -x.imaginary)

    def fromInt(x: Int): ComplexNumber = ComplexNumber(x, 0)

    def toInt(x: ComplexNumber): Int = x.real.toInt

    def toLong(x: ComplexNumber): Long = x.real.toLong

    def toFloat(x: ComplexNumber): Float = x.real.toFloat

    def toDouble(x: ComplexNumber): Double = x.real

    def compare(x: ComplexNumber, y: ComplexNumber): Int = {
      val xMagnitude = x.getSquareRadius
      val yMagnitude = y.getSquareRadius
      java.lang.Double.compare(xMagnitude, yMagnitude)
    }

    def parseString(str: String): Option[ComplexNumber] = {
      val complexPattern = """\s*([+-]?\d+(\.\d*)?)\s*([+-])\s*(\d+(\.\d*)?)i""".r
      str match {
        case complexPattern(real, _, sign, imaginary, _) =>
          val imaginaryPart = (if (sign == "-") -1 else 1) * imaginary.toDouble
          Some(ComplexNumber(real.toDouble, imaginaryPart))
        case _ => None
      }
    }
  }
}

final case class PolarForm(radius: Double, angle: Double)

object Main {
  import ComplexNumber._
  def main(args: Array[String]): Unit = {
    val v1 = ComplexNumber(1, 2)
    val v2 = ComplexNumber(0, 2)

    println("-" * 100)
    println("Checking operations:")
    println(s"$v1 - $v2 = ${v1 - v2}")
    println(s"$v1 + $v2 = ${v1 + v2}")
    println(s"$v1 * $v2 = ${v1 * v2}")
    println(s"$v1 / $v2 = ${v1 / v2}")
    println(s"$v1 ~= $v2 = ${v1 ~= v2}")
    println(s"$v2.toPolarCoords = ${v2.toPolarCoords}")
    println(s"$v1 + 34 = ${v1 + 34}")
    println(s"$v1 + 2L = ${v1 + 2L}")
    println(s"3.14 + $v2 = ${3.14 + v2}")
    println(s"BigInt(\"32432\") + $v1 = ${BigInt("32432") + v1}")
    println(s"32L + 432.5.i = ${32L + 432.5.i}")
    println("-" * 100)

    println("Checking Numeric methods usage:")
    val complexList = List(ComplexNumber(1, 2), ComplexNumber(3, 4), ComplexNumber(-1, 0))
    val sum = complexList.sum
    val product = complexList.product
    val max = complexList.max
    val sortedList = complexList.sorted
    val difference = complexList.reduce(_ - _)
    println(s"sum = ${sum}")
    println(s"product = ${product}")
    println(s"max = ${max}")
    println(s"sortedList = ${sortedList}")
    println(s"difference = ${difference}")
    println("-" * 100)

    val complexNumeric = implicitly[Numeric[ComplexNumber]]
    println("Checking parsing from String to ComplexNumber:")
    println(s"parseString( - 1.32 - 2.23i) = ${complexNumeric.parseString(" - 1.32 - 2.23i")}")
    println(s"parseString( -1.0 - 2.2i) = ${complexNumeric.parseString(" -1.0 - 2.2i")}")
    println(s"parseString( -1.3- 2.0i) = ${complexNumeric.parseString(" -1.3- 2.0i")}")
    println(s"parseString(1 - 2i) = ${complexNumeric.parseString("1 - 2i")}")
    println("-" * 100)

//    Output:
//    ----------------------------------------------------------------------------------------------------
//    Checking operations :
//    ComplexNumber(1.0, 2.0) - ComplexNumber(0.0, 2.0) = ComplexNumber(1.0, 0.0)
//    ComplexNumber(1.0, 2.0) + ComplexNumber(0.0, 2.0) = ComplexNumber(1.0, 4.0)
//    ComplexNumber(1.0, 2.0) * ComplexNumber(0.0, 2.0) = ComplexNumber(-4.0, 2.0)
//    ComplexNumber(1.0, 2.0) / ComplexNumber(0.0, 2.0) = ComplexNumber(1.0, -0.5)
//    ComplexNumber(1.0, 2.0) ~= ComplexNumber(0.0, 2.0) = false
//    ComplexNumber(0.0, 2.0).toPolarCoords = PolarForm(2.0, 1.5707963267948966)
//    ComplexNumber(1.0, 2.0) + 2L = ComplexNumber(3.0, 2.0)
//    3.14 + ComplexNumber(0.0, 2.0) = ComplexNumber(3.14, 2.0)
//    32L + 432.5.i = ComplexNumber(32.0, 432.5)
//    ----------------------------------------------------------------------------------------------------
//    Checking Numeric methods usage :
//    sum = ComplexNumber(3.0, 6.0)
//    product = ComplexNumber(5.0, -10.0)
//    max = ComplexNumber(3.0, 4.0)
//    sortedList = List(ComplexNumber(-1.0, 0.0), ComplexNumber(1.0, 2.0), ComplexNumber(3.0, 4.0))
//    difference = ComplexNumber(-1.0, -2.0)
//    ----------------------------------------------------------------------------------------------------
//    Checking parsing from String to ComplexNumber :
//    parseString(-1.32 - 2.23 i) = None
//    parseString(-1.0 - 2.2 i) = Some(ComplexNumber(-1.0, -2.2))
//    parseString(-1.3 - 2.0 i) = Some(ComplexNumber(-1.3, -2.0))
//    parseString(1 - 2 i) = Some(ComplexNumber(1.0, -2.0))
//    ----------------------------------------------------------------------------------------------------
  }
}
