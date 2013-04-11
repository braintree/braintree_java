package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

import java.math.BigDecimal;
import java.util.Calendar;

public final class DepositDetail {
    private final Calendar depositDate;
    private final Calendar disbursedAt;
    private final String settlementCurrencyIsoCode;
    private final boolean fundsHeld;
    private final BigDecimal settlementCurrencyExchangeRate;
    private final BigDecimal settlementAmount;

    public DepositDetail(NodeWrapper node) {
        depositDate = node.findDate("deposit-date");
        disbursedAt = node.findDateTime("disbursed-at");
        settlementCurrencyIsoCode = node.findString("settlement-currency-iso-code");
        fundsHeld = node.findBoolean("funds-held");
        settlementCurrencyExchangeRate = node.findBigDecimal("settlement-currency-exchange-rate");
        settlementAmount = node.findBigDecimal("settlement-amount");
    }

    public Calendar getDepositDate() {
        return depositDate;
    }

    public Calendar getDisbursedAt() {
        return disbursedAt;
    }

    public String getSettlementCurrencyIsoCode() {
        return settlementCurrencyIsoCode;
    }

    public boolean isFundsHeld() {
        return fundsHeld;
    }

    public BigDecimal getSettlementCurrencyExchangeRate() {
        return settlementCurrencyExchangeRate;
    }

    public BigDecimal getSettlementAmount() {
        return settlementAmount;
    }

    public boolean isValid() {
        return getDepositDate() != null;
    }
}
