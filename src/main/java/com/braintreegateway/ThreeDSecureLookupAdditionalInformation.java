package com.braintreegateway;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ThreeDSecureLookupAdditionalInformation {
    private String accountAgeIndicator;
    private String accountChangeDate;
    private String accountChangeIndicator;
    private String accountCreateDate;
    private String accountId;
    private String accountPurchases;
    private String accountPwdChangeDate;
    private String accountPwdChangeIndicator;
    private String addCardAttempts;
    private String addressMatch;
    private String authenticationIndicator;
    private String deliveryEmail;
    private String deliveryTimeframe;
    private String fraudActivity;
    private String giftCardAmount;
    private String giftCardCount;
    private String giftCardCurrencyCode;
    private String installment;
    private String ipAddress;
    private String orderDescription;
    private String paymentAccountAge;
    private String paymentAccountIndicator;
    private String preorderDate;
    private String preorderIndicator;
    private String productCode;
    private String purchaseDate;
    private String recurringEnd;
    private String recurringFrequency;
    private String reorderIndicator;
    private ThreeDSecureLookupAddress shippingAddress;
    private String shippingAddressUsageDate;
    private String shippingAddressUsageIndicator;
    private String shippingMethodIndicator;
    private String shippingNameIndicator;
    private String taxAmount;
    private String transactionCountDay;
    private String transactionCountYear;
    private String userAgent;

    public ThreeDSecureLookupAdditionalInformation() {

    }

    public ThreeDSecureLookupAdditionalInformation shippingAddress(ThreeDSecureLookupAddress shippingAddress) {
        this.shippingAddress = shippingAddress;
        return this;
    }

    public ThreeDSecureLookupAdditionalInformation shippingMethodIndicator(String shippingMethodIndicator) {
        this.shippingMethodIndicator = shippingMethodIndicator;
        return this;
    }

    public ThreeDSecureLookupAdditionalInformation productCode(String productCode) {
        this.productCode = productCode;
        return this;
    }

    public ThreeDSecureLookupAdditionalInformation deliveryTimeframe(String deliveryTimeframe) {
        this.deliveryTimeframe = deliveryTimeframe;
        return this;
    }

    public ThreeDSecureLookupAdditionalInformation deliveryEmail(String deliveryEmail) {
        this.deliveryEmail = deliveryEmail;
        return this;
    }

    public ThreeDSecureLookupAdditionalInformation reorderIndicator(String reorderIndicator) {
        this.reorderIndicator = reorderIndicator;
        return this;
    }

    public ThreeDSecureLookupAdditionalInformation preorderIndicator(String preorderIndicator) {
        this.preorderIndicator = preorderIndicator;
        return this;
    }

    public ThreeDSecureLookupAdditionalInformation preorderDate(String preorderDate) {
        this.preorderDate = preorderDate;
        return this;
    }

    public ThreeDSecureLookupAdditionalInformation giftCardAmount(String giftCardAmount) {
        this.giftCardAmount = giftCardAmount;
        return this;
    }

    public ThreeDSecureLookupAdditionalInformation giftCardCurrencyCode(String giftCardCurrencyCode) {
        this.giftCardCurrencyCode = giftCardCurrencyCode;
        return this;
    }

    public ThreeDSecureLookupAdditionalInformation giftCardCount(String giftCardCount) {
        this.giftCardCount = giftCardCount;
        return this;
    }

    public ThreeDSecureLookupAdditionalInformation accountAgeIndicator(String accountAgeIndicator) {
        this.accountAgeIndicator = accountAgeIndicator;
        return this;
    }

    public ThreeDSecureLookupAdditionalInformation accountCreateDate(String accountCreateDate) {
        this.accountCreateDate = accountCreateDate;
        return this;
    }

    public ThreeDSecureLookupAdditionalInformation accountChangeIndicator(String accountChangeIndicator) {
        this.accountChangeIndicator = accountChangeIndicator;
        return this;
    }

    public ThreeDSecureLookupAdditionalInformation accountChangeDate(String accountChangeDate) {
        this.accountChangeDate = accountChangeDate;
        return this;
    }

    public ThreeDSecureLookupAdditionalInformation accountPwdChangeIndicator(String accountPwdChangeIndicator) {
        this.accountPwdChangeIndicator = accountPwdChangeIndicator;
        return this;
    }

    public ThreeDSecureLookupAdditionalInformation accountPwdChangeDate(String accountPwdChangeDate) {
        this.accountPwdChangeDate = accountPwdChangeDate;
        return this;
    }

    public ThreeDSecureLookupAdditionalInformation shippingAddressUsageIndicator(String shippingAddressUsageIndicator) {
        this.shippingAddressUsageIndicator = shippingAddressUsageIndicator;
        return this;
    }

    public ThreeDSecureLookupAdditionalInformation shippingAddressUsageDate(String shippingAddressUsageDate) {
        this.shippingAddressUsageDate = shippingAddressUsageDate;
        return this;
    }

    public ThreeDSecureLookupAdditionalInformation transactionCountDay(String transactionCountDay) {
        this.transactionCountDay = transactionCountDay;
        return this;
    }

    public ThreeDSecureLookupAdditionalInformation transactionCountYear(String transactionCountYear) {
        this.transactionCountYear = transactionCountYear;
        return this;
    }

    public ThreeDSecureLookupAdditionalInformation addCardAttempts(String addCardAttempts) {
        this.addCardAttempts = addCardAttempts;
        return this;
    }

    public ThreeDSecureLookupAdditionalInformation accountPurchases(String accountPurchases) {
        this.accountPurchases = accountPurchases;
        return this;
    }

    public ThreeDSecureLookupAdditionalInformation fraudActivity(String fraudActivity) {
        this.fraudActivity = fraudActivity;
        return this;
    }

    public ThreeDSecureLookupAdditionalInformation shippingNameIndicator(String shippingNameIndicator) {
        this.shippingNameIndicator = shippingNameIndicator;
        return this;
    }

    public ThreeDSecureLookupAdditionalInformation paymentAccountIndicator(String paymentAccountIndicator) {
        this.paymentAccountIndicator = paymentAccountIndicator;
        return this;
    }

    public ThreeDSecureLookupAdditionalInformation paymentAccountAge(String paymentAccountAge) {
        this.paymentAccountAge = paymentAccountAge;
        return this;
    }

    public ThreeDSecureLookupAdditionalInformation addressMatch(String addressMatch) {
        this.addressMatch = addressMatch;
        return this;
    }

    public ThreeDSecureLookupAdditionalInformation accountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    public ThreeDSecureLookupAdditionalInformation ipAddress(String ipAddress) {
        this.ipAddress = ipAddress;
        return this;
    }

    public ThreeDSecureLookupAdditionalInformation orderDescription(String orderDescription) {
        this.orderDescription = orderDescription;
        return this;
    }

    public ThreeDSecureLookupAdditionalInformation taxAmount(String taxAmount) {
        this.taxAmount = taxAmount;
        return this;
    }

    public ThreeDSecureLookupAdditionalInformation userAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public ThreeDSecureLookupAdditionalInformation authenticationIndicator(String authenticationIndicator) {
        this.authenticationIndicator = authenticationIndicator;
        return this;
    }

    public ThreeDSecureLookupAdditionalInformation installment(String installment) {
        this.installment = installment;
        return this;
    }

    public ThreeDSecureLookupAdditionalInformation purchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
        return this;
    }

    public ThreeDSecureLookupAdditionalInformation recurringEnd(String recurringEnd) {
        this.recurringEnd = recurringEnd;
        return this;
    }

    public ThreeDSecureLookupAdditionalInformation recurringFrequency(String recurringFrequency) {
        this.recurringFrequency = recurringFrequency;
        return this;
    }

    public String getShippingMethodIndicator() {
        return shippingMethodIndicator;
    }

    public String getProductCode() {
        return productCode;
    }

    public String getDeliveryTimeframe() {
        return deliveryTimeframe;
    }

    public String getDeliveryEmail() {
        return deliveryEmail;
    }

    public String getReorderIndicator() {
        return reorderIndicator;
    }

    public String getPreorderIndicator() {
        return preorderIndicator;
    }

    public String getPreorderDate() {
        return preorderDate;
    }

    public String getGiftCardAmount() {
        return giftCardAmount;
    }

    public String getGiftCardCurrencyCode() {
        return giftCardCurrencyCode;
    }

    public String getGiftCardCount() {
        return giftCardCount;
    }

    public String getAccountAgeIndicator() {
        return accountAgeIndicator;
    }

    public String getAccountCreateDate() {
        return accountCreateDate;
    }

    public String getAccountChangeIndicator() {
        return accountChangeIndicator;
    }

    public String getAccountChangeDate() {
        return accountChangeDate;
    }

    public String getAccountPwdChangeIndicator() {
        return accountPwdChangeIndicator;
    }

    public String getAccountPwdChangeDate() {
        return accountPwdChangeDate;
    }

    public String getShippingAddressUsageIndicator() {
        return shippingAddressUsageIndicator;
    }

    public String getShippingAddressUsageDate() {
        return shippingAddressUsageDate;
    }

    public String getTransactionCountDay() {
        return transactionCountDay;
    }

    public String getTransactionCountYear() {
        return transactionCountYear;
    }

    public String getAddCardAttempts() {
        return addCardAttempts;
    }

    public String getAccountPurchases() {
        return accountPurchases;
    }

    public String getFraudActivity() {
        return fraudActivity;
    }

    public String getShippingNameIndicator() {
        return shippingNameIndicator;
    }

    public String getPaymentAccountIndicator() {
        return paymentAccountIndicator;
    }

    public String getPaymentAccountAge() {
        return paymentAccountAge;
    }

    public String getAddressMatch() {
        return addressMatch;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getOrderDescription() {
        return orderDescription;
    }

    public String getTaxAmount() {
        return taxAmount;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public String getAuthenticationIndicator() {
        return authenticationIndicator;
    }

    public String getInstallment() {
        return installment;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public String getRecurringEnd() {
        return recurringEnd;
    }

    public String getRecurringFrequency() {
        return recurringFrequency;
    }

    public ThreeDSecureLookupAddress getShippingAddress() {
        return shippingAddress;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> jsonMap = new HashMap<String, Object>();

        if (shippingAddress != null) {
            jsonMap.put("shipping_given_name", shippingAddress.getGivenName());
            jsonMap.put("shipping_surname", shippingAddress.getSurname());
            jsonMap.put("shipping_phone", shippingAddress.getPhoneNumber());
            jsonMap.put("shipping_line1", shippingAddress.getStreetAddress());
            jsonMap.put("shipping_line2", shippingAddress.getExtendedAddress());
            jsonMap.put("shipping_line3", shippingAddress.getLine3());
            jsonMap.put("shipping_city", shippingAddress.getLocality());
            jsonMap.put("shipping_state", shippingAddress.getRegion());
            jsonMap.put("shipping_postal_code", shippingAddress.getPostalCode());
            jsonMap.put("shipping_country_code", shippingAddress.getCountryCodeAlpha2());
        }

        jsonMap.put("shipping_method_indicator", getShippingMethodIndicator());
        jsonMap.put("product_code", getProductCode());
        jsonMap.put("delivery_timeframe", getDeliveryTimeframe());
        jsonMap.put("delivery_email", getDeliveryEmail());
        jsonMap.put("reorder_indicator", getReorderIndicator());
        jsonMap.put("preorder_indicator", getPreorderIndicator());
        jsonMap.put("preorder_date", getPreorderDate());
        jsonMap.put("gift_card_amount", getGiftCardAmount());
        jsonMap.put("gift_card_currency_code", getGiftCardCurrencyCode());
        jsonMap.put("gift_card_count", getGiftCardCount());
        jsonMap.put("account_age_indicator", getAccountAgeIndicator());
        jsonMap.put("account_change_indicator", getAccountChangeIndicator());
        jsonMap.put("account_create_date", getAccountCreateDate());
        jsonMap.put("account_change_date", getAccountChangeDate());
        jsonMap.put("account_pwd_change_indicator", getAccountPwdChangeIndicator());
        jsonMap.put("account_pwd_change_date", getAccountPwdChangeDate());
        jsonMap.put("shipping_address_usage_indicator", getShippingAddressUsageIndicator());
        jsonMap.put("shipping_address_usage_date", getShippingAddressUsageDate());
        jsonMap.put("transaction_count_day", getTransactionCountDay());
        jsonMap.put("transaction_count_year", getTransactionCountYear());
        jsonMap.put("add_card_attempts", getAddCardAttempts());
        jsonMap.put("account_purchases", getAccountPurchases());
        jsonMap.put("fraud_activity", getFraudActivity());
        jsonMap.put("shipping_name_indicator", getShippingNameIndicator());
        jsonMap.put("payment_account_indicator", getPaymentAccountIndicator());
        jsonMap.put("payment_account_age", getPaymentAccountAge());
        jsonMap.put("address_match", getAddressMatch());
        jsonMap.put("account_id", getAccountId());
        jsonMap.put("ip_address", getIpAddress());
        jsonMap.put("order_description", getOrderDescription());
        jsonMap.put("tax_amount", getTaxAmount());
        jsonMap.put("user_agent", getUserAgent());
        jsonMap.put("authentication_indicator", getAuthenticationIndicator());
        jsonMap.put("installment", getInstallment());
        jsonMap.put("purchase_date", getPurchaseDate());
        jsonMap.put("recurring_end", getRecurringEnd());
        jsonMap.put("recurring_frequency", getRecurringFrequency());
        jsonMap.values().removeAll(Collections.singleton(null));

        return jsonMap;
    }
}
