package building

import scala.annotation.tailrec

/** Здание должно иметь:
  *   - строковый адрес
  *   - этажи (сходящиеся к первому этажу) Этаж может быть жилым, коммерческим, либо чердаком
  *     (который сам может быть коммерческим). На каждом жилом этаже живет 2 человека и есть
  *     лестница(ссылка) ведущая на следующий этаж У каждого человека есть возраст (>0) и пол На
  *     коммерческом этаже может быть несколько заведений (используйте Array), но не меньше 1.
  *     Здание всегда должно заканчиваться чердаком На чердаке никто не живет, но это может быть и
  *     коммерческое помещение (но только 1).
  */
sealed trait Sex

case object Male extends Sex

case object Female extends Sex

case class Commercial(name: String)

case class Resident(age: Int, sex: Sex)

sealed trait Floor {
  def floorNumber: Int
}

sealed trait NonAtticFloor extends Floor {
  def nextFloor: Floor
}

case class ResidentialFloor(
  floorNumber: Int,
  nextFloor: Floor,
  resident1: Resident,
  resident2: Resident
) extends NonAtticFloor

case class CommercialFloor(
  floorNumber: Int,
  nextFloor: Floor,
  commercialEntities: Array[Commercial]
) extends NonAtticFloor

sealed trait Attic extends Floor

case class CommonAttic(floorNumber: Int) extends Attic

case class CommercialAttic(floorNumber: Int, commercialEntity: Commercial) extends Attic

// для scala 3.5.0 без оверхеда
//type Address = Address.T
//
//object Address {
//  opaque type T = String
//
//  def apply(v: String): T = v
//}
case class Address(v: String)

case class Building(address: Address, firstFloor: Floor)

object Building {

  def apply(address: Address, firstFloor: Floor): Either[BuildingError, Building] =
    validate_floors(firstFloor, 1).map(_ => new Building(address, firstFloor))

  @tailrec
  def validate_floors(floor: Floor, expectedFloorNumber: Int): Either[BuildingError, Unit] = {
    if (floor.floorNumber != expectedFloorNumber)
      Left(InvalidFloorNumberError(expectedFloorNumber, floor.floorNumber))
    else
      floor match {
        case ResidentialFloor(_, nextFloor, resident1, resident2) =>
          if (resident1.age <= 0)
            Left(NegativeAgeError(floor.floorNumber, 1, resident1.age))
          else if (resident2.age <= 0)
            Left(NegativeAgeError(floor.floorNumber, 2, resident2.age))
          else
            validate_floors(nextFloor, expectedFloorNumber + 1)

        case CommercialFloor(_, nextFloor, commercialEntities) =>
          // Вот такая проверка Array на null нормальная? Нам говорили null не использовать, но непонятно как без него
          if (commercialEntities == null || commercialEntities.length < 1)
            Left(EmptyCommercialEntitiesError(floor.floorNumber))
          else
            validate_floors(nextFloor, expectedFloorNumber + 1)

        case attic: Attic => Right(())
      }
  }

  /** Проходится по зданию снизу в вверх, применяя функцию [[f]] на каждом жилом этаже с начальным
    * аккумулятором [[accumulator]]
    */
  def fold[A](building: Building, accumulator: A)(f: (A, Floor) => A): A = {
    def foldFloors(floor: Floor, acc: A): A = {
      val newAcc = f(acc, floor)
      floor match {
        case nonAttic: NonAtticFloor =>
          foldFloors(nonAttic.nextFloor, newAcc)
        case _: Attic =>
          newAcc
      }
    }

    foldFloors(building.firstFloor, accumulator)
  }

  /** Подсчитывает количество этаже, на которых живет хотя бы один мужчина старше [[olderThan]].
    * Используйте [[fold]]
    */
  def countOldManFloors(building: Building, olderThan: Int): Int = {
    fold[Int](building, 0) { (acc: Int, floor: Floor) =>
      {
        floor match {
          case ResidentialFloor(_, _, resident1, resident2) =>
            if (
              resident1.age > olderThan && resident1.sex == Male ||
              resident2.age > olderThan && resident2.sex == Male
            )
              acc + 1
            else
              acc
          case _ => acc
        }
      }
    }
  }

  /** Находит наибольший возраст женщины, проживающей в здании. Используйте [[fold]] */
  def womanMaxAge(building: Building): Option[Int] = {
    val maxAge = fold[Int](building, -1) { (acc: Int, floor: Floor) =>
      {
        floor match {
          case ResidentialFloor(_, _, resident1, resident2) =>
            //            //красивое решение
            //            val ages = Seq(resident1, resident2).filter(_.sex == Female).map(_.age)
            //            if (ages.isEmpty) acc else math.max(acc, ages.max)
            // интуитивно понятное решение
            if (resident1.sex == Female && resident2.sex == Female)
              math.max(acc, math.max(resident2.age, resident1.age))
            else if (resident1.sex == Female)
              math.max(acc, resident1.age)
            else if (resident2.sex == Female)
              math.max(acc, resident2.age)
            else
              acc
          case _ => acc
        }
      }
    }
    if (maxAge == -1) None
    else Some(maxAge)
  }

  /** Находит кол-во коммерческих заведений в здании. Используйте [[fold]] */
  def countCommercial(building: Building): Int =
    fold[Int](building, 0) {
      case (acc, CommercialFloor(_, _, commercialEntities)) => acc + commercialEntities.length
      case (acc, CommercialAttic(_, _))                     => acc + 1
      case (acc, _)                                         => acc
    }

  /** Находит среднее кол-во коммерческих заведений зданиях. Реализуйте свою функцию, похожую на
    * [[fold]] для прохода по зданию
    */
  def countCommercialAvg(buildings: Array[Building]): Double =
    if (buildings.isEmpty) 0.0
    else {
      val totalCommercials = buildings.foldLeft(0) { (accCommercials, building) =>
        accCommercials + countCommercial(building)
      }
      totalCommercials.toDouble / buildings.length
    }

  /** Находит среднее кол-во мужчин на четных этажах. Реализуйте свою функцию, похожую на [[fold]]
    * для прохода по зданию
    */
  def evenFloorsMenAvg(building: Building): Double = {
    val (totalMen, evenFloorCount) = fold[(Int, Int)](building, (0, 0)) {
      case ((menAcc, floorAcc), ResidentialFloor(floorNumber, _, resident1, resident2))
          if floorNumber % 2 == 0 =>
        val menCount = Seq(resident1, resident2).count(_.sex == Male)
        (menAcc + menCount, floorAcc + 1)
      case ((menAcc, floorAcc), _) =>
        (menAcc, floorAcc)
    }
    if (evenFloorCount == 0) 0.0 else totalMen.toDouble / evenFloorCount
  }

}
