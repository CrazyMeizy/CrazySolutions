package calculator

import calculator.Building.*

object Parquet {

  def calculate(building: Building): Int = building match {
    case Premium(length, width, height, floorNumber) if floorNumber < 5 =>
      Math.pow(3, floorNumber).toInt * (length + width + height)
    case Premium(length, width, height, floorNumber) =>
      Math.pow(2, floorNumber).toInt * (length + width + height)
    case Economy(length, width, height, floorNumber) =>
      length * width * height + floorNumber * 10000

  }

}
