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
        } catch (Exception e) {}
        return numberFormat.format(getSum());
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Money)) return false;
        else {
            return this.getSum() == ((Money) o).getSum()
                    && this.getCurrency().getCurrencyCode().equals(((Money) o).getCurrency().getCurrencyCode());
        }
    }
}
