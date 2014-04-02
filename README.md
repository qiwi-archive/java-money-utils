#java-money-utils

Library that is designed to simplify tasks that involve a subject of money. It consists of a few classes:
* [`Money`](https://github.com/qiwi/java-money-utils/blob/master/src/main/java/ru/mw/moneyutils/Money.java) is a Currency and BigDecimal pair which holds an amount of money.
* [`ExchangeRates`](https://github.com/qiwi/java-money-utils/blob/master/src/main/java/ru/mw/moneyutils/ExchangeRates.java) is a map of available exchange rates from one currency to another.
* [`CurrencyUtils`](https://github.com/qiwi/java-money-utils/blob/master/src/main/java/ru/mw/moneyutils/CurrencyUtils.java) provides a functionality of converting between [ISO4217](http://en.wikipedia.org/wiki/ISO4217) alpha and numeric codes.

___

###Examples:

```java
//Adding RUB to USD to get result in RUB
Money roubles = new Money(Currency.getInstance("RUB"), new BigDecimal(100)); // 100.00 RUB
Money dollars = new Money(Currency.getInstance("USD"), new BigDecimal(10.5d)); // 10.50 USD

ExchangeRates exchangeRates = new ExchangeRates();
exchangeRates.putRate(Currency.getInstance("USD"), Currency.getInstance("RUB"), new BigDecimal(35.15)); // Adding an exchange rate from USD to RUB so that 1 USD == 35.15 RUB

Money sum = Money.addition(Currency.getInstance("RUB"), exchangeRates, roubles, dollars); // 469.08 RUB
```

```java
//Currencies from ISO4217 number form
Money moneyUSD = new Money(CurrencyUtils.getCurrencyForNumericCode(840), new BigDecimal(100)); // 100.00 USD 
Money moneyRUB = new Money(CurrencyUtils.getCurrencyForNumericCode(643), new BigDecimal(1488)); // 1488.00 RUB
Money moneyEUR = new Money(CurrencyUtils.getCurrencyForNumericCode(978), new BigDecimal(666.69d)); // 666.69 EUR
```

```java
//Rounding and empty currency
Money money = new Money(CurrencyUtils.getCurrencyForAlphaCode("XXX"), new BigDecimal(1000000)); // 1000000.00 XXX - empty currency

Money moneyRoundingModeUP = Money.division(money, new BigDecimal(3), 6, RoundingMode.UP); // 333333.33 XXX
BigDecimal underlyingAmount = moneyRoundingModeUP.getSum(); // 333333.333334
```
