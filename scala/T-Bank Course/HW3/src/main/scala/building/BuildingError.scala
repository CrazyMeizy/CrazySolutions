package building

sealed trait BuildingError

case class NegativeAgeError(floorNumber: Int, residentIndex: Int, age: Int) extends BuildingError

case class InvalidFloorNumberError(expectedFloorNumber: Int, actualFloorNumber: Int)
    extends BuildingError

case class EmptyCommercialEntitiesError(floorNumber: Int) extends BuildingError

case object BuildingDoesNotEndWithAtticError extends BuildingError
