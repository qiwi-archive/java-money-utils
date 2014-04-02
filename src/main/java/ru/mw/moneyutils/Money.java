package ru.mw.moneyutils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

/**
 * Created by nixan on 21.01.14.
 */
public class Money implements Serializable {

    private static final String UNKNOWN_CURRENCY_ISO4217_CODE = "XXX";

    private final Currency mCurrency;

    private final BigDecimal mSum;

    public Money(Currency currency, BigDecimal sum) {
        mSum = sum;
        mCurrency = currency;
    }

    public BigDecimal getSum() {
        return mSum;
    }

    public Currency getCurrency() {
        return mCurrency;
    }

    @Override
    public String toString() {
        return toString(Locale.getDefault());
    }

    public String toString(Locale locale) {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        if (!getCurrency().getCurrencyCode().equals(UNKNOWN_CURRENCY_ISO4217_CODE)) {
            numberFormat.setCurrency(getCurrency());
        }
        try {
            NumberFormat.class.getMethod("setRoundingMode", RoundingMode.class)
                    .invoke(numberFormat, RoundingMode.HALF_UP);
        } catch (Exception e) {
        }
        return numberFormat.format(getSum());
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Money)) {
            return false;
        } else {
            return this.getSum() == ((Money) o).getSum() && this.getCurrency().getCurrencyCode()
                    .equals(((Money) o).getCurrency().getCurrencyCode());
        }
    }

    /**
     * Sums supplied amounts and returns a new one with the desired currency.
     *
     * @param targetCurrency currency of the result
     * @param exchangeRates  exchange rate to be used in case supplied amounts currency is not
     *                       equal
     *                       to the currency of the desired result
     * @param money          amounts to be added one to each other
     * @return a sum of passed amounts converted to the desired currency
     * @see #subtraction(java.util.Currency, ExchangeRates, Money...)
     */
    public static Money addition(Currency targetCurrency, ExchangeRates exchangeRates,
            Money... money) {
        if (exchangeRates == null) {
            /**
             * Setting the empty exchange rate in case user only wanted to get the sum of the amounts with the same currencies
             */
            exchangeRates = new ExchangeRates();
        }
        Money[] convertedAmounts = convertMoneyArray(targetCurrency, exchangeRates, money);
        BigDecimal sum = BigDecimal.ZERO;
        for (Money convertedMoney : convertedAmounts) {
            sum = sum.add(convertedMoney.getSum());
        }
        return new Money(targetCurrency, sum);
    }

    /**
     * Subtracts supplied amounts and returns a new one with the desired currency.
     *
     * @param targetCurrency currency of the result
     * @param exchangeRates  exchange rate to be used in case supplied amounts currency is not
     *                       equal
     *                       to the currency of the desired result
     * @param money          amounts to be subtracted from the first one in the set
     * @return result of subtraction of passed amounts converted to the desired currency
     * @see #addition(java.util.Currency, ExchangeRates, Money...)
     */
    public static Money subtraction(Currency targetCurrency, ExchangeRates exchangeRates,
            Money... money) {
        if (exchangeRates == null) {
            /**
             * Setting the empty exchange rate in case user only wanted to get the subtraction of the amounts with the same currencies
             */
            exchangeRates = new ExchangeRates();
        }
        Money[] convertedAmounts = convertMoneyArray(targetCurrency, exchangeRates, money);
        BigDecimal subtraction = convertedAmounts[0].getSum();
        for (int i = 1; i < convertedAmounts.length; i++) {
            subtraction = subtraction.subtract(convertedAmounts[i].getSum());
        }
        return new Money(targetCurrency, subtraction);
    }

    /**
     * Muliplies amount with the supplied multiplier
     *
     * @param money amount to be multiplied
     */
    public static Money multiplication(Money money, BigDecimal multiplier) {
        Currency resultCurrency = money.getCurrency();
        BigDecimal resultAmount = money.getSum();
        resultAmount = resultAmount.multiply(multiplier);
        return new Money(resultCurrency, resultAmount);
    }

    /**
     * Divides amount by supplied divisor
     *
     * @param money amount to be divided
     * @see #division(Money, java.math.BigDecimal, int, java.math.RoundingMode)
     */
    public static Money division(Money money, BigDecimal divisor) {
        return division(money, divisor, 0, null);
    }

    /**
     * Divides amount by supplied divisor
     *
     * @param money amount to be divided
     */
    public static Money division(Money money, BigDecimal divisor, int roundingScale,
            RoundingMode roundingMode) {
        Currency resultCurrency = money.getCurrency();
        BigDecimal resultAmount = money.getSum();
        if (roundingMode == null) {
            resultAmount = resultAmount.divide(divisor);
        } else {
            resultAmount = resultAmount.divide(divisor, roundingScale, roundingMode);
        }
        return new Money(resultCurrency, resultAmount);
    }

    private static final Money[] convertMoneyArray(Currency targetCurrency,
            ExchangeRates exchangeRates, Money... money) {
        if (targetCurrency == null) {
            throw new IllegalArgumentException("Set currency for the result");
        }
        if (exchangeRates == null) {
            throw new IllegalArgumentException(
                    "Must set the exchange rate to convert from one currency to another");
        }
        if (money.length < 2) {
            throw new IllegalArgumentException(
                    "To perform operation there must be at least two money amounts");
        }
        Money[] convertedAmounts = new Money[money.length];
        for (int i = 0; i < money.length; i++) {
            convertedAmounts[i] = exchangeRates.convert(money[i], targetCurrency);
        }
        return convertedAmounts;
    }
}
