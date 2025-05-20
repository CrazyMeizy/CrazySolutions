package ru.tbank.converter

import Errors.UnsupportedCurrencyException

class CurrencyConverter(ratesDictionary: Map[String, Map[String, BigDecimal]]) {
  def exchange(money: Money, toCurrency: String): Money = {
    import Errors.*
    if (money.currency == toCurrency) throw SameCurrencyExchangeException()
    if (!Currencies.SupportedCurrencies.contains(toCurrency)) throw UnsupportedCurrencyException()
    Money(
      money.amount *
        ratesDictionary
          .getOrElse(money.currency, throw NoSuchRateException())
          .getOrElse(toCurrency, throw NoSuchRateException()),
      toCurrency
    )
  }
}

object CurrencyConverter {

  import Currencies.SupportedCurrencies

  def apply(ratesDictionary: Map[String, Map[String, BigDecimal]]): CurrencyConverter = {
    val fromCurrencies = ratesDictionary.keys
    val toCurrencies = ratesDictionary.values
    if (
      fromCurrencies.toSet
        .subsetOf(SupportedCurrencies) && toCurrencies.forall(_.keys.toSet.subsetOf(SupportedCurrencies))
    ) new CurrencyConverter(ratesDictionary)
    else throw new UnsupportedCurrencyException
  }
}
