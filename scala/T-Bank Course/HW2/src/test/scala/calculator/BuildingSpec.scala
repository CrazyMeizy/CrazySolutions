package calculator

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class BuildingSpec extends AnyFlatSpec with Matchers {

  "Building.Economy smart-constructor" should
    "throw IllegalArgumentException if any of its arguments is not greater than zero" in {
      an[IllegalArgumentException] should be thrownBy Building.Economy(0, 1, 1, 1)
      an[IllegalArgumentException] should be thrownBy Building.Economy(1, -1, 1, 1)
      an[IllegalArgumentException] should be thrownBy Building.Economy(1, 1, 0, 1)
      an[IllegalArgumentException] should be thrownBy Building.Economy(1, 1, 1, -1)
    }

  "Building.Premium smart-constructor" should
    "throw IllegalArgumentException if any of its arguments is not greater than zero" in {
      an[IllegalArgumentException] should be thrownBy Building.Premium(0, 1, 1, 1)
      an[IllegalArgumentException] should be thrownBy Building.Premium(1, -1, 1, 1)
      an[IllegalArgumentException] should be thrownBy Building.Premium(1, 1, 0, 1)
      an[IllegalArgumentException] should be thrownBy Building.Premium(1, 1, 1, -1)
    }

}
