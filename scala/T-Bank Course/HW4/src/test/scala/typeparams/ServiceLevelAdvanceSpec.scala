package typeparams

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ServiceLevelAdvanceTest extends AnyFlatSpec with Matchers {

  it should "allow advancing from Economy to UpgradedEconomy and not vice versa" in {
    val service1 = new ServiceLevelAdvance[Economy]
    val service2 = new ServiceLevelAdvance[UpgradedEconomy]
    "val result: ServiceLevelAdvance[UpgradedEconomy] = service1.advanceTo[UpgradedEconomy]" should compile
    "val result: ServiceLevelAdvance[Economy] = service2.advanceTo[Economy]" shouldNot typeCheck
  }

  it should "allow advancing from UpgradedEconomy to Special1b and not vice versa" in {
    val service1 = new ServiceLevelAdvance[UpgradedEconomy]
    val service2 = new ServiceLevelAdvance[Special1b]

    "val result: ServiceLevelAdvance[Special1b] = service1.advanceTo[Special1b]" should compile
    "val result: ServiceLevelAdvance[UpgradedEconomy] = service2.advanceTo[UpgradedEconomy]" shouldNot typeCheck
  }

  it should "allow advancing from ExtendedEconomy to Business and not vice versa" in {
    val service1 = new ServiceLevelAdvance[ExtendedEconomy]
    val service2 = new ServiceLevelAdvance[Business]

    "val result: ServiceLevelAdvance[Business] = service1.advanceTo[Business]" should compile
    "val result: ServiceLevelAdvance[ExtendedEconomy] = service2.advanceTo[ExtendedEconomy]" shouldNot typeCheck
  }

  it should "allow advancing from Business to Elite and not vice versa" in {
    val service1 = new ServiceLevelAdvance[ExtendedEconomy]
    val service2 = new ServiceLevelAdvance[Elite]

    "val result: ServiceLevelAdvance[Elite] = service1.advanceTo[Elite]" should compile
    "val result: ServiceLevelAdvance[ExtendedEconomy] = service2.advanceTo[ExtendedEconomy]" shouldNot typeCheck
  }

  "ServiceLevelAdvance" should "not allow advancing  to not parent level" in {
    val service1 = new ServiceLevelAdvance[Platinum]
    val service2 = new ServiceLevelAdvance[Business]
    val service3 = new ServiceLevelAdvance[ExtendedEconomy]

    "val result: ServiceLevelAdvance[Elite] = service1.advanceTo[Elite]" shouldNot typeCheck
    "val result: ServiceLevelAdvance[Special1b] = service2.advanceTo[Special1b]" shouldNot typeCheck
    "val result: ServiceLevelAdvance[UpgradedEconomy] = service3.advanceTo[UpgradedEconomy]" shouldNot typeCheck
  }
}
