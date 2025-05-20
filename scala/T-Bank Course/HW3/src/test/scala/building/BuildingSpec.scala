package building

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class BuildingSpec extends AnyFlatSpec with Matchers {

  // Импортируем необходимые объекты из вашего кода

  import Building._

  // specs on constructor validations

  "Building" should "return NegativeAgeError if a resident has non-positive age" in {
    val invalidResident = Resident(-5, Male)
    val validResident   = Resident(30, Female)
    val floor = ResidentialFloor(
      floorNumber = 1,
      nextFloor = CommonAttic(floorNumber = 2),
      resident1 = invalidResident,
      resident2 = validResident
    )
    val buildingResult = Building(Address("123 Main St"), floor)
    buildingResult shouldBe Left(NegativeAgeError(1, 1, -5))
  }

  it should "return InvalidFloorNumberError if floor numbers are incorrect" in {
    val resident1 = Resident(25, Male)
    val resident2 = Resident(28, Female)
    val floor1 = ResidentialFloor(
      floorNumber = 2, // Ошибка: ожидалось 1
      nextFloor = CommonAttic(floorNumber = 2),
      resident1 = resident1,
      resident2 = resident2
    )
    val buildingResult = Building(Address("456 Elm St"), floor1)
    buildingResult shouldBe Left(InvalidFloorNumberError(1, 2))
  }

  it should "return EmptyCommercialEntitiesError if commercial floor has no entities" in {
    val floor = CommercialFloor(
      floorNumber = 1,
      nextFloor = CommonAttic(floorNumber = 2),
      commercialEntities = Array.empty
    )
    val buildingResult = Building(Address("789 Oak St"), floor)
    buildingResult shouldBe Left(EmptyCommercialEntitiesError(1))
  }

  // specs on methods

  "countOldManFloors" should "return the number of floors with men older than the specified age" in {
    val resident1 = Resident(60, Male)
    val resident2 = Resident(25, Female)
    val floor = ResidentialFloor(
      floorNumber = 1,
      nextFloor = CommonAttic(floorNumber = 2),
      resident1 = resident1,
      resident2 = resident2
    )
    val building = Building(Address("123 Main St"), floor).toOption.get
    val count    = countOldManFloors(building, 50)
    count shouldEqual 1
  }

  it should "return 0 if there are no men older than the specified age" in {
    val resident1 = Resident(40, Male)
    val resident2 = Resident(35, Female)
    val floor = ResidentialFloor(
      floorNumber = 1,
      nextFloor = CommonAttic(floorNumber = 2),
      resident1 = resident1,
      resident2 = resident2
    )
    val building = Building(Address("456 Elm St"), floor).toOption.get
    val count    = countOldManFloors(building, 50)
    count shouldEqual 0
  }

  it should "return 0 if there are no men at all in the building" in {
    val resident1 = Resident(30, Female)
    val resident2 = Resident(28, Female)
    val floor = ResidentialFloor(
      floorNumber = 1,
      nextFloor = CommonAttic(floorNumber = 2),
      resident1 = resident1,
      resident2 = resident2
    )
    val building = Building(Address("789 Oak St"), floor).toOption.get
    val count    = countOldManFloors(building, 20)
    count shouldEqual 0
  }

  "womanMaxAge" should "find age of the oldest woman in the building" in {
    val resident1 = Resident(30, Female)
    val resident2 = Resident(45, Female)
    val floor = ResidentialFloor(
      floorNumber = 1,
      nextFloor = CommonAttic(floorNumber = 2),
      resident1 = resident1,
      resident2 = resident2
    )
    val building = Building(Address("123 Main St"), floor).toOption.get
    val maxAge   = womanMaxAge(building)
    maxAge shouldEqual Some(45)
  }

  it should "return None if there are no women in the building" in {
    val resident1 = Resident(35, Male)
    val resident2 = Resident(40, Male)
    val floor = ResidentialFloor(
      floorNumber = 1,
      nextFloor = CommonAttic(floorNumber = 2),
      resident1 = resident1,
      resident2 = resident2
    )
    val building = Building(Address("456 Elm St"), floor).toOption.get
    val maxAge   = womanMaxAge(building)
    maxAge shouldEqual None
  }

  "countCommercial" should "return number of commercial establishments in the building" in {
    val commercial1 = Commercial("Shop")
    val commercial2 = Commercial("Cafe")
    val floor = CommercialFloor(
      floorNumber = 1,
      nextFloor = CommonAttic(floorNumber = 2),
      commercialEntities = Array(commercial1, commercial2)
    )
    val building = Building(Address("789 Oak St"), floor).toOption.get
    val count    = countCommercial(building)
    println(count)
    count shouldEqual 2
  }

  it should "return 0 if there are no commercial establishments in the building" in {
    val floor = ResidentialFloor(
      floorNumber = 1,
      nextFloor = CommonAttic(floorNumber = 2),
      resident1 = Resident(30, Male),
      resident2 = Resident(25, Female)
    )
    val building = Building(Address("321 Pine St"), floor).toOption.get
    val count    = countCommercial(building)
    count shouldEqual 0
  }

  "countCommercialAvg" should "return average commercial establishments through array of buildings" in {
    val commercial1 = Commercial("Shop")
    val commercial2 = Commercial("Cafe")
    val floor1 = CommercialFloor(
      floorNumber = 1,
      nextFloor = CommonAttic(floorNumber = 2),
      commercialEntities = Array(commercial1)
    )
    val building1 = Building(Address("123 Main St"), floor1).toOption.get

    val floor2 = CommercialFloor(
      floorNumber = 1,
      nextFloor = CommonAttic(floorNumber = 2),
      commercialEntities = Array(commercial1, commercial2)
    )
    val building2 = Building(Address("456 Elm St"), floor2).toOption.get

    val buildings = Array(building1, building2)
    val avg       = countCommercialAvg(buildings)

    avg shouldEqual 1.5
  }

  it should "return 0 if there are no commercial establishments in the buildings" in {
    val floor1 = ResidentialFloor(
      floorNumber = 1,
      nextFloor = CommonAttic(floorNumber = 2),
      resident1 = Resident(30, Male),
      resident2 = Resident(25, Female)
    )
    val building1 = Building(Address("789 Oak St"), floor1).toOption.get

    val floor2 = ResidentialFloor(
      floorNumber = 1,
      nextFloor = CommonAttic(floorNumber = 2),
      resident1 = Resident(35, Male),
      resident2 = Resident(28, Female)
    )
    val building2 = Building(Address("321 Pine St"), floor2).toOption.get

    val buildings = Array(building1, building2)
    val avg       = countCommercialAvg(buildings)
    avg shouldEqual 0.0
  }

  it should "return 0 if there are no buildings" in {
    val buildings = Array.empty[Building]
    val avg       = countCommercialAvg(buildings)
    avg shouldEqual 0.0
  }

  "evenFloorsMenAvg" should "return average count of men on even floors in the building" in {
    val floor3 = ResidentialFloor(
      floorNumber = 3,
      nextFloor = CommonAttic(floorNumber = 4),
      resident1 = Resident(28, Male),
      resident2 = Resident(30, Female)
    )
    val floor2 = ResidentialFloor(
      floorNumber = 2,
      nextFloor = floor3,
      resident1 = Resident(40, Male),
      resident2 = Resident(35, Male)
    )
    val floor1 = ResidentialFloor(
      floorNumber = 1,
      nextFloor = floor2,
      resident1 = Resident(25, Female),
      resident2 = Resident(22, Female)
    )
    val building = Building(Address("123 Main St"), floor1).toOption.get
    val avg      = evenFloorsMenAvg(building)
    avg shouldEqual 2.0 // На втором этаже 2 мужчины
  }

  it should "return 0 if there are no men in the building" in {
    val floor2 = ResidentialFloor(
      floorNumber = 2,
      nextFloor = CommonAttic(floorNumber = 3),
      resident1 = Resident(30, Female),
      resident2 = Resident(35, Female)
    )
    val floor1 = ResidentialFloor(
      floorNumber = 1,
      nextFloor = floor2,
      resident1 = Resident(25, Female),
      resident2 = Resident(22, Female)
    )
    val building = Building(Address("456 Elm St"), floor1).toOption.get
    val avg      = evenFloorsMenAvg(building)
    avg shouldEqual 0.0
  }

}
