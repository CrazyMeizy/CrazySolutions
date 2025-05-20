package calculator

sealed trait Building(length: Int, width: Int, height: Int, floorNumber: Int)

object Building {

  final case class Economy private (length: Int, width: Int, height: Int, floorNumber: Int)
      extends Building(length, width, height, floorNumber)

  object Economy:

    def apply(length: Int, width: Int, height: Int, floorNumber: Int): Building = {
      assert(
        length > 0 && width > 0 && height > 0 && floorNumber > 0,
        throw IllegalArgumentException()
      )
      new Economy(length, width, height, floorNumber)
    }

  end Economy

  final case class Premium(length: Int, width: Int, height: Int, floorNumber: Int)
      extends Building(length, width, height, floorNumber) {}

  object Premium:

    def apply(length: Int, width: Int, height: Int, floorNumber: Int): Building = {
      assert(
        length > 0 && width > 0 && height > 0 && floorNumber > 0,
        throw IllegalArgumentException()
      )
      new Premium(length, width, height, floorNumber)
    }

  end Premium

}
