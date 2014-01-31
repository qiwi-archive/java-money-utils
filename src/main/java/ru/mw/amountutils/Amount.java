package ru.mw.amountutils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Currency;

/**
 * Created by nixan on 21.01.14.
 */
public class Amount {

    private static final String UNKNOWN_CURRENCY_ISO4217_CODE = "XXX";

    private final Currency mCurrency;

    private final BigDecimal mSum;

    public Amount(BigDecimal sum, Currency currency) {
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
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        if (!getCurrency().getCurrencyCode().equals(UNKNOWN_CURRENCY_ISO4217_CODE)) {
            numberFormat.setCurrency(getCurrency());
        }
        return numberFormat.format(getSum());
    }
}
