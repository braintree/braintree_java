package com.braintreegateway;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.csv.CSVRecord;

/**
 * @see TransactionLevelFeeReport#getCSVRecords()
 */
@Deprecated
public class TransactionLevelFeeReportRow {
    // Shared fields.
    private String merchantAccountId;
    private String transactionId;
    private String originalTransactionId;
    private String transactionType;
    private String presentmentCurrency;
    private String settlementCurrency;
    private BigDecimal settlementAmount;
    private Calendar settlementDate;
    private Calendar disbursementDate;
    private String paymentInstrument;
    private String cardBrand;
    private String cardType;
    private String first6OfCreditCard;
    private String issuingBank;
    private BigDecimal refundedAmount;
    private BigDecimal exchangeRate;
    private String orderId;
    private String acquirerReferenceNumber;
    private String cardIssuingCountry;
    private BigDecimal discount;
    private BigDecimal discountCredit;
    private BigDecimal perTransactionFee;
    private BigDecimal perTransactionFeeCredit;

    // IC+ fields.
    private String interchangeDescription;
    private String interchangeCurrency;
    private BigDecimal estInterchangeRate;
    private BigDecimal estInterchangeRateCredit;
    private BigDecimal estInterchangeFixed;
    private BigDecimal estInterchangeFixedCredit;
    private BigDecimal estInterchangeTotalAmount;
    private BigDecimal estTotalFeeAmount;
    private BigDecimal braintreeTotalAmount;

    // Flat-fee fields.
    private BigDecimal originalSaleAmount;
    private String refundType;
    private BigDecimal multicurrencyFeeAmount;
    private BigDecimal multicurrencyFeeCredit;
    private BigDecimal totalFeeAmount;

    public TransactionLevelFeeReportRow(CSVRecord record) throws ParseException {
        Map<String, String> recordMap = record.toMap();
        this.merchantAccountId = getFirstOf(recordMap, "Merchant Account ID", "Merchant Account Token");
        this.transactionId = recordMap.get("Transaction ID");
        this.originalTransactionId = recordMap.get("Original Transaction ID");
        this.transactionType = recordMap.get("Transaction Type");
        this.presentmentCurrency = recordMap.get("Presentment Currency");
        this.settlementCurrency = recordMap.get("Settlement Currency");
        this.settlementAmount = new BigDecimal(recordMap.get("Settlement Amount"));
        this.settlementDate = parseDate(recordMap.get("Settlement Date"));
        this.disbursementDate = parseDate(recordMap.get("Disbursement Date"));
        this.paymentInstrument = recordMap.get("Payment Instrument");
        this.cardBrand = recordMap.get("Card Brand");
        this.cardType = recordMap.get("Card Type");
        this.first6OfCreditCard = recordMap.get("First 6 of CC");
        this.issuingBank = recordMap.get("Issuing Bank");
        this.refundedAmount = maybeParseBigDecimal(recordMap.get("Refunded Amount"));
        this.exchangeRate = new BigDecimal(recordMap.get("Exchange Rate"));
        this.orderId = recordMap.get("Order ID");
        this.acquirerReferenceNumber = recordMap.get("Acquirer Reference Number");
        this.cardIssuingCountry = recordMap.get("Card Issuing Country");
        this.discount = new BigDecimal(recordMap.get("Discount"));
        this.discountCredit = new BigDecimal(recordMap.get("Discount Credit"));
        this.perTransactionFee = new BigDecimal(recordMap.get("Per Transaction Fee"));
        this.perTransactionFeeCredit = new BigDecimal(recordMap.get("Per Transaction Fee Credit"));
        this.braintreeTotalAmount = maybeParseBigDecimal(recordMap.get("Braintree Total Amount"));

        this.interchangeDescription = recordMap.get("Interchange Description");
        this.interchangeCurrency = recordMap.get("Interchange Currency");
        this.estInterchangeRate = maybeParseBigDecimal(recordMap.get("Est. Interchange Rate"));
        this.estInterchangeRateCredit = maybeParseBigDecimal(recordMap.get("Est. Interchange Rate Credit"));
        this.estInterchangeFixed = maybeParseBigDecimal(recordMap.get("Est. Interchange Fixed"));
        this.estInterchangeFixedCredit = maybeParseBigDecimal(recordMap.get("Est. Interchange Fixed Credit"));
        this.estInterchangeTotalAmount = maybeParseBigDecimal(recordMap.get("Est. Interchange Total Amount"));
        this.estTotalFeeAmount = maybeParseBigDecimal(recordMap.get("Est. Total Fee Amount"));

        this.originalSaleAmount = maybeParseBigDecimal(recordMap.get("Original Sale Amount"));
        this.refundType = recordMap.get("Refund Type");
        this.multicurrencyFeeAmount = maybeParseBigDecimal(recordMap.get("Multicurrency Fee Amount"));
        this.multicurrencyFeeCredit = maybeParseBigDecimal(recordMap.get("Multicurrency Fee Credit"));
        this.totalFeeAmount = maybeParseBigDecimal(recordMap.get("Total Fee Amount"));
    }

    public String getMerchantAccountId() {
        return merchantAccountId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getOriginalTransactionId() {
        return originalTransactionId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public String getPresentmentCurrency() {
        return presentmentCurrency;
    }

    public String getSettlementCurrency() {
        return settlementCurrency;
    }

    public BigDecimal getSettlementAmount() {
        return settlementAmount;
    }

    public Calendar getSettlementDate() {
        return settlementDate;
    }

    public Calendar getDisbursementDate() {
        return disbursementDate;
    }

    public String getPaymentInstrument() {
        return paymentInstrument;
    }

    public String getCardBrand() {
        return cardBrand;
    }

    public String getCardType() {
        return cardType;
    }

    public String getFirst6OfCreditCard() {
        return first6OfCreditCard;
    }

    public String getIssuingBank() {
        return issuingBank;
    }

    public BigDecimal getRefundedAmount() {
        return refundedAmount;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getAcquirerReferenceNumber() {
        return acquirerReferenceNumber;
    }

    public String getCardIssuingCountry() {
        return cardIssuingCountry;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public BigDecimal getDiscountCredit() {
        return discountCredit;
    }

    public BigDecimal getPerTransactionFee() {
        return perTransactionFee;
    }

    public BigDecimal getPerTransactionFeeCredit() {
        return perTransactionFeeCredit;
    }

    public BigDecimal getBraintreeTotalAmount() {
        return braintreeTotalAmount;
    }

    public String getInterchangeDescription() {
        return interchangeDescription;
    }

    public String getInterchangeCurrency() {
        return interchangeCurrency;
    }

    public BigDecimal getEstInterchangeRate() {
        return estInterchangeRate;
    }

    public BigDecimal getEstInterchangeRateCredit() {
        return estInterchangeRateCredit;
    }

    public BigDecimal getEstInterchangeFixed() {
        return estInterchangeFixed;
    }

    public BigDecimal getEstInterchangeFixedCredit() {
        return estInterchangeFixedCredit;
    }

    public BigDecimal getEstInterchangeTotalAmount() {
        return estInterchangeTotalAmount;
    }

    public BigDecimal getEstTotalFeeAmount() {
        return estTotalFeeAmount;
    }

    public BigDecimal getOriginalSaleAmount() {
        return originalSaleAmount;
    }

    public String getRefundType() {
        return refundType;
    }

    public BigDecimal getMulticurrencyFeeAmount() {
        return multicurrencyFeeAmount;
    }

    public BigDecimal getMulticurrencyFeeCredit() {
        return multicurrencyFeeCredit;
    }

    public BigDecimal getTotalFeeAmount() {
        return totalFeeAmount;
    }

    static Calendar parseDate(String dateString) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getDefault());
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.setTime(dateFormat.parse(dateString));
        return calendar;
    }

    private BigDecimal maybeParseBigDecimal(String numberString) throws NumberFormatException {
        if (numberString == null || numberString.equals("")) {
            return null;
        }
        return new BigDecimal(numberString);
    }

    private String getFirstOf(Map<String, String> map, String... keys) {
        for (String key : keys) {
            if (map.containsKey(key)) {
                return map.get(key);
            }
        }
        return null;
    }
}
