package ventilation

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class VentilationSpec extends AnyFlatSpec with Matchers {

  // SOLUTION 1 TESTING
  "solve1" should "return the maximum of each sublist of size K" in {
    val degrees = List(1, 2, 1, 4, 5, 4)
    val k = 2
    val expected = List(2, 2, 4, 5, 5)
    Ventilation.solve1(degrees, k) should be(expected)
  }

  it should "handle an empty list" in {
    val degrees = List[Int]()
    val k = 2
    val expected = List()
    Ventilation.solve1(degrees, k) should be(expected)
  }

  it should "handle K = 1" in {
    val degrees = List(3, 1, 4, 1, 5)
    val k = 1
    val expected = List(3, 1, 4, 1, 5)
    Ventilation.solve1(degrees, k) should be(expected)
  }

  it should "handle K = N" in {
    val degrees = List(3, 1, 4, 1, 5)
    val k = 5
    val expected = List(5)
    Ventilation.solve1(degrees, k) should be(expected)
  }

  it should "throw IllegalArgumentException when K <= 0" in {
    val degrees = List(1, 2, 3)
    val k = 0
    an[IllegalArgumentException] should be thrownBy {
      Ventilation.solve1(degrees, k)
    }
  }

  // SOLUTION 2 TESTING
  "solve2" should "return the maximum of each sublist of size K" in {
    val degrees = List(1, 2, 1, 4, 5, 4)
    val k = 2
    val expected = List(2, 2, 4, 5, 5)
    Ventilation.solve2(degrees, k) should be(expected)
  }

  it should "handle an empty list" in {
    val degrees = List[Int]()
    val k = 2
    val expected = List()
    Ventilation.solve2(degrees, k) should be(expected)
  }

  it should "handle K = 1" in {
    val degrees = List(3, 1, 4, 1, 5)
    val k = 1
    val expected = List(3, 1, 4, 1, 5)
    Ventilation.solve2(degrees, k) should be(expected)
  }

  it should "handle K = N" in {
    val degrees = List(3, 1, 4, 1, 5)
    val k = 5
    val expected = List(5)
    Ventilation.solve2(degrees, k) should be(expected)
  }

  it should "produce the same output as solve1 for random inputs" in {
    val degrees = List(1, 3, 2, 5, 1, 6, 4)
    val k = 3
    Ventilation.solve2(degrees, k) should be(Ventilation.solve1(degrees, k))
  }

  it should "handle single element list" in {
    val degrees = List(42)
    val k = 1
    val expected = List(42)
    Ventilation.solve2(degrees, k) should be(expected)
  }
}
