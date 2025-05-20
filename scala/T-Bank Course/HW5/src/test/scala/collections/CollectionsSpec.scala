package collections

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class CollectionsSpec extends AnyFlatSpec with Matchers {

  "findGaps" should "return None when there are no gaps" in {
    Collections.findGaps(Seq(1, 2, 3, 4)) should be(None)
  }

  it should "return Some(Seq((2,8))) for Seq(1, 2, 8)" in {
    Collections.findGaps(Seq(1, 2, 8)) should be(Some(Seq((2, 8))))
  }

  it should "return Some(Seq((3,5), (5,7))) for Seq(3, 5, 7)" in {
    Collections.findGaps(Seq(3, 5, 7)) should be(Some(Seq((3, 5), (5, 7))))
  }

  it should "throw an exception for an empty list" in {
    an[UnsupportedOperationException] should be thrownBy {
      Collections.findGaps(Seq())
    }
  }

  "minFold" should "return None for empty map" in {
    Collections.minFold(Map.empty[String, Int]) should be(None)
  }

  it should "return the only element for a singleton map" in {
    val map = Map("a" -> 1)
    Collections.minFold(map) should be(Some("a" -> 1))
  }

  it should "return the minimum element for a map with multiple elements" in {
    val map = Map("a" -> 3, "b" -> 1, "c" -> 2)
    Collections.minFold(map) should be(Some("b" -> 1))
  }

  "minReduce" should "return None for empty map" in {
    Collections.minReduce(Map.empty[String, Int]) should be(None)
  }

  it should "return the minimum element for a map with multiple elements" in {
    val map = Map("a" -> 3, "b" -> 1, "c" -> 2)
    Collections.minReduce(map) should be(Some("b" -> 1))
  }

  "minRecursion" should "return None for empty map" in {
    Collections.minRecursion(Map.empty[String, Int]) should be(None)
  }

  it should "return the minimum element for a map with multiple elements" in {
    val map = Map("a" -> 3, "b" -> 1, "c" -> 2)
    Collections.minRecursion(map) should be(Some("b" -> 1))
  }

  "All min methods" should "return the same result for the same input" in {
    val maps = Seq(
      Map.empty[String, Int],
      Map("a" -> 1),
      Map("a" -> 3, "b" -> 1, "c" -> 2),
      Map("a" -> 2, "b" -> 2)
    )
    for (map <- maps) {
      val minFoldResult = Collections.minFold(map)
      val minReduceResult = Collections.minReduce(map)
      val minRecursionResult = Collections.minRecursion(map)

      minFoldResult should be(minReduceResult)
      minFoldResult should be(minRecursionResult)
    }
  }

  "scanLeft" should "behave like built-in scanLeft with addition" in {
    val list = Seq(1, 2, 3, 4)
    val zero = 0
    val f = (a: Int, b: Int) => a + b
    Collections.scanLeft(zero)(list)(f) should be(list.scanLeft(zero)(f))
  }

  it should "behave like built-in scanLeft with multiplication" in {
    val list = Seq(1, 2, 3, 4)
    val zero = 1
    val f = (a: Int, b: Int) => a * b
    Collections.scanLeft(zero)(list)(f) should be(list.scanLeft(zero)(f))
  }

  it should "behave like built-in scanLeft with string concatenation" in {
    val list = Seq("a", "b", "c")
    val zero = ""
    val f = (a: String, b: String) => a + b
    Collections.scanLeft(zero)(list)(f) should be(list.scanLeft(zero)(f))
  }

  // Тесты для метода count
  "count" should "count characters correctly in a simple string" in {
    val s = "aabbc"
    Collections.count(s).toMap should be(Map('a' -> 2, 'b' -> 2, 'c' -> 1))
  }

  it should "return empty list for empty string" in {
    Collections.count("") should be(List())
  }

  it should "count characters correctly with mixed case" in {
    val s = "AaBbCc"
    Collections.count(s).toMap should be(Map('A' -> 1, 'a' -> 1, 'B' -> 1, 'b' -> 1, 'C' -> 1, 'c' -> 1))
  }
}
