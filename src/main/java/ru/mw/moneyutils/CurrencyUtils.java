package ru.mw.moneyutils;

import java.util.Currency;
import java.util.Enumeration;
import java.util.ResourceBundle;

/**
 * Created by nixan on 01.02.14.
 */
public final class CurrencyUtils {

    /**
     * Only static methods
     */
    private CurrencyUtils() {

    }

    public static final Currency getCurrencyForAlphaCode(String iso4217alpha) {
        return Currency.getInstance(iso4217alpha);
    }

    public static final Currency getCurrencyForNumericCode(Integer iso4217numeric) {
        return Currency.getInstance(getAlphaCodeFromNumeric(iso4217numeric));
    }

    public static final String getAlphaCodeFromNumeric(Integer numeric) {
        return ISO4217AlphaToNumeric.getInstance().getAlpha(numeric);
    }

    public static final Integer getNumericCodeFromAlpha(String alpha) {
        return ISO4217AlphaToNumeric.getInstance().getNumeric(alpha);
    }

    private static class ISO4217AlphaToNumeric {

        private final BidirectionalMap<String, Integer> mAlphaToNumeric
                = new BidirectionalMap<String, Integer>();

        private static ISO4217AlphaToNumeric sInstance = new ISO4217AlphaToNumeric();

        public static ISO4217AlphaToNumeric getInstance() {
            return sInstance;
        }

        private ISO4217AlphaToNumeric() {
            ResourceBundle resourceBundle = ResourceBundle.getBundle("iso4217");
            Enumeration<String> alphaEnumeration = resourceBundle.getKeys();
            while (alphaEnumeration.hasMoreElements()) {
                String alpha = alphaEnumeration.nextElement();
                mAlphaToNumeric.put(alpha, Integer.parseInt(resourceBundle.getString(alpha)));
            }
        }

        public String getAlpha(Integer numeric) {
            return mAlphaToNumeric.getBackward(numeric);
        }

        public Integer getNumeric(String alpha) {
            return mAlphaToNumeric.getForward(alpha);
        }
    }
}
