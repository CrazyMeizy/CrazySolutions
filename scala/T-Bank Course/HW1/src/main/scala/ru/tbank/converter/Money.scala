package ru.tbank.converter

import scala.annotation.targetName
import Errors.*

case class Money private (amount: BigDecimal, currency: String) {
  def +(other: Money): Money = {
    if (other.currency != currency) throw CurrencyMismatchException()
    Money(amount + other.amount, currency)
  }

  def -(other: Money): Money = {
    if (other.currency != currency) throw CurrencyMismatchException()
    if (amount - other.amount < 0) throw MoneyAmountShouldBeNonNegativeException()
    Money(amount - other.amount, currency)
  }

  def isSameCurrency(other: Money): Boolean = other.currency == currency
}

object Money {
  def apply(amount: BigDecimal, currency: String): Money = {
    if (!Currencies.SupportedCurrencies.contains(currency)) throw UnsupportedCurrencyException()
    if (amount < 0) throw MoneyAmountShouldBeNonNegativeException()
    new Money(amount, currency)
  }
}
