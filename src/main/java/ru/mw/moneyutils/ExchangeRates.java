package ru.mw.moneyutils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by nixan on 01.02.14.
 */
public class ExchangeRates {


    private final HashMap<CurrencyPair, BigDecimal> mRates
            = new HashMap<CurrencyPair, BigDecimal>();


    /**
     * Adds a conversion rate from one currency to another. <br /><i>Note that rate from one
     * currency to another doesn't equals to the reverse currencies order.</i><br /><i>Note that if
     * storage already has a
     * rate for these currencies than it will be swapped with the new one.</i>
     *
     * @param fromCurrency currency to convert from
     * @param toCurrency   currency to convert to
     * @param rate         exchange rate
     */
    public void putRate(Currency fromCurrency, Currency toCurrency, BigDecimal rate) {
        mRates.put(new CurrencyPair(fromCurrency, toCurrency), rate);
    }

    /**
     * Returns the available exchange rate from one currency to another.
     *
     * @param fromCurrency currency to convert from
     * @param toCurrency   currency to convert to
     * @throws ru.mw.moneyutils.RateUnavailableException if provided pair of rates is not stored.
     */
    public BigDecimal getRate(Currency fromCurrency, Currency toCurrency) {
        BigDecimal rate = mRates.get(new CurrencyPair(fromCurrency, toCurrency));
        if (rate == null) {
            throw new RateUnavailableException();
        } else {
            return rate;
        }
    }

    /**
     * Converts one amount into another with the specified target currency.
     * Default implementation rounds the sum up.
     *
     * @param convertFrom amount to convert from
     * @param convertTo   currency to convert to
     * @see Money
     * @see java.math.RoundingMode#UP
     */
    public Money convert(Money convertFrom, Currency convertTo) {
        return convert(convertFrom, convertTo, 2, RoundingMode.UP);
    }

    /**
     * Converts one amount into another with the specified target currency.
     *
     * @param convertFrom amount to convert from
     * @param convertTo   currency to convert to
     * @see Money
     * @see java.math.RoundingMode
     * @see java.math.BigDecimal#setScale(int, java.math.RoundingMode)
     */
    public Money convert(Money convertFrom, Currency convertTo, int roundingScale,
            RoundingMode roundingMode) {
        BigDecimal rate = getRate(convertFrom.getCurrency(), convertTo);
        BigDecimal convertedSum = convertFrom.getSum().multiply(rate)
                .setScale(roundingScale, roundingMode);
        return new Money(convertedSum, convertTo);
    }

    /**
     * Returns a list of available currencies to convert to for the specified source currency.
     *
     * @param fromCurrency currency to convert from
     */
    public ArrayList<Currency> getAvailableCurrenciesToConvertTo(Currency fromCurrency) {
        ArrayList<Currency> result = new ArrayList<Currency>();
        Iterator<CurrencyPair> iterator = mRates.keySet().iterator();
        while (iterator.hasNext()) {
            CurrencyPair currencyPair = iterator.next();
            if (currencyPair.mFromCurrency.equals(fromCurrency)) {
                result.add(currencyPair.mToCurrency);
            }
        }
        return result;
    }

    /**
     * Returns a list of available currencies to convert from for the specified target currency.
     *
     * @param toCurrency currency to convert to
     */
    public ArrayList<Currency> getAvailableCurrenciesToConvertFrom(Currency toCurrency) {
        ArrayList<Currency> result = new ArrayList<Currency>();
        Iterator<CurrencyPair> iterator = mRates.keySet().iterator();
        while (iterator.hasNext()) {
            CurrencyPair currencyPair = iterator.next();
            if (currencyPair.mToCurrency.equals(toCurrency)) {
                result.add(currencyPair.mFromCurrency);
            }
        }
        return result;
    }

    private static final class CurrencyPair {

        private final Currency mFromCurrency;

        private final Currency mToCurrency;

        public CurrencyPair(Currency fromCurrency, Currency toCurrency) {
            if (fromCurrency == null || toCurrency == null) {
                throw new NullPointerException("Currencies cannot be empty!");
            }
            mFromCurrency = fromCurrency;
            mToCurrency = toCurrency;
        }

        @Override
        public String toString() {
            return String.format("%s => %s", mFromCurrency.getCurrencyCode(),
                    mToCurrency.getCurrencyCode());
        }
    }

}
