package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

import java.math.BigDecimal;
import java.util.Calendar;

public final class DisbursementDetails {
    private final Calendar disbursementDate;
    private final String settlementCurrencyIsoCode;
    private final boolean fundsHeld;
    private final boolean success;
    private final BigDecimal settlementCurrencyExchangeRate;
    private final BigDecimal settlementAmount;

    public DisbursementDetails(NodeWrapper node) {
        disbursementDate = node.findDate("disbursement-date");
        settlementCurrencyIsoCode = node.findString("settlement-currency-iso-code");
        fundsHeld = node.findBoolean("funds-held");
        success = node.findBoolean("success");
        settlementCurrencyExchangeRate = node.findBigDecimal("settlement-currency-exchange-rate");
        settlementAmount = node.findBigDecimal("settlement-amount");
    }

    public Calendar getDisbursementDate() {
        return disbursementDate;
    }

    public String getSettlementCurrencyIsoCode() {
        return settlementCurrencyIsoCode;
    }

    public boolean isFundsHeld() {
        return fundsHeld;
    }

    public boolean isSuccess() {
        return success;
    }

    public BigDecimal getSettlementCurrencyExchangeRate() {
        return settlementCurrencyExchangeRate;
    }

    public BigDecimal getSettlementAmount() {
        return settlementAmount;
    }

    public boolean isValid() {
        return getDisbursementDate() != null;
    }
}
