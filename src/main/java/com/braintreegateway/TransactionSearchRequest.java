package com.braintreegateway;

public class TransactionSearchRequest extends SearchRequest {
    public TextNode<TransactionSearchRequest> id() {
        return textNode("id");
    }

    public TextNode<TransactionSearchRequest> billingCompany() {
        return textNode("billing_company");
    }

    public TextNode<TransactionSearchRequest> billingCountryName() {
        return textNode("billing_country_name");
    }

    public TextNode<TransactionSearchRequest> billingExtendedAddress() {
        return textNode("billing_extended_address");
    }

    public TextNode<TransactionSearchRequest> billingFirstName() {
        return textNode("billing_first_name");
    }

    public TextNode<TransactionSearchRequest> billingLastName() {
        return textNode("billing_last_name");
    }

    public TextNode<TransactionSearchRequest> billingLocality() {
        return textNode("billing_locality");
    }

    public TextNode<TransactionSearchRequest> billingPostalCode() {
        return textNode("billing_postal_code");
    }

    public TextNode<TransactionSearchRequest> billingRegion() {
        return textNode("billing_region");
    }

    public TextNode<TransactionSearchRequest> billingStreetAddress() {
        return textNode("billing_street_address");
    }

    public TextNode<TransactionSearchRequest> creditCardCardholderName() {
        return textNode("credit_card_cardholder_name");
    }

    public TextNode<TransactionSearchRequest> creditCardUniqueIdentifier() {
        return textNode("credit_card_unique_identifier");
    }

    public EqualityNode<TransactionSearchRequest> creditCardExpirationDate() {
        return new EqualityNode<TransactionSearchRequest>("credit_card_expiration_date", this);
    }

    public PartialMatchNode<TransactionSearchRequest> creditCardNumber() {
        return new PartialMatchNode<TransactionSearchRequest>("credit_card_number", this);
    }

    public TextNode<TransactionSearchRequest> currency() {
        return textNode("currency");
    }

    public TextNode<TransactionSearchRequest> customerCompany() {
        return textNode("customer_company");
    }

    public TextNode<TransactionSearchRequest> customerEmail() {
        return textNode("customer_email");
    }

    public TextNode<TransactionSearchRequest> customerFax() {
        return textNode("customer_fax");
    }

    public TextNode<TransactionSearchRequest> customerFirstName() {
        return textNode("customer_first_name");
    }

    public TextNode<TransactionSearchRequest> customerId() {
        return textNode("customer_id");
    }

    public TextNode<TransactionSearchRequest> customerLastName() {
        return textNode("customer_last_name");
    }

    public TextNode<TransactionSearchRequest> customerPhone() {
        return textNode("customer_phone");
    }

    public TextNode<TransactionSearchRequest> customerWebsite() {
        return textNode("customer_website");
    }

    public MultipleValueNode<TransactionSearchRequest, String> ids() {
        return multiTypeNode("ids");
    }

    public MultipleValueNode<TransactionSearchRequest, String> user() {
        return multiTypeNode("user");
    }

    public TextNode<TransactionSearchRequest> orderId() {
        return textNode("order_id");
    }

    public TextNode<TransactionSearchRequest> paymentMethodToken() {
        return textNode("payment_method_token");
    }

    public TextNode<TransactionSearchRequest> paypalPaymentId() {
        return textNode("paypal_payment_id");
    }

    public TextNode<TransactionSearchRequest> paypalPayerEmail() {
        return textNode("paypal_payer_email");
    }

    public TextNode<TransactionSearchRequest> paypalAuthorizationId() {
        return textNode("paypal_authorization_id");
    }

    public TextNode<TransactionSearchRequest> processorAuthorizationCode() {
        return textNode("processor_authorization_code");
    }

    public TextNode<TransactionSearchRequest> settlementBatchId() {
        return textNode("settlement_batch_id");
    }

    public TextNode<TransactionSearchRequest> sepaDirectDebitPayPalV2OrderId() {
        return textNode("sepa_debit_paypal_v2_order_id");
    }

    public TextNode<TransactionSearchRequest> shippingCompany() {
        return textNode("shipping_company");
    }

    public TextNode<TransactionSearchRequest> shippingCountryName() {
        return textNode("shipping_country_name");
    }

    public TextNode<TransactionSearchRequest> shippingExtendedAddress() {
        return textNode("shipping_extended_address");
    }

    public TextNode<TransactionSearchRequest> shippingFirstName() {
        return textNode("shipping_first_name");
    }

    public TextNode<TransactionSearchRequest> shippingLastName() {
        return textNode("shipping_last_name");
    }

    public TextNode<TransactionSearchRequest> shippingLocality() {
        return textNode("shipping_locality");
    }

    public TextNode<TransactionSearchRequest> shippingPostalCode() {
        return textNode("shipping_postal_code");
    }

    public TextNode<TransactionSearchRequest> shippingRegion() {
        return textNode("shipping_region");
    }

    public TextNode<TransactionSearchRequest> shippingStreetAddress() {
        return textNode("shipping_street_address");
    }

    public MultipleValueNode<TransactionSearchRequest, String> paymentInstrumentType() {
        return multiTypeNode("payment_instrument_type");
    }

    public MultipleValueNode<TransactionSearchRequest, Transaction.CreatedUsing> createdUsing() {
        return multiTypeNode("created_using");
    }

    public MultipleValueNode<TransactionSearchRequest, CreditCard.CustomerLocation> creditCardCustomerLocation() {
        return multiTypeNode("credit_card_customer_location");
    }

    public MultipleValueNode<TransactionSearchRequest, String> merchantAccountId() {
        return multiTypeNode("merchant_account_id");
    }

    public MultipleValueNode<TransactionSearchRequest, CreditCard.CardType> creditCardCardType() {
        return multiTypeNode("credit_card_card_type");
    }

    public MultipleValueNode<TransactionSearchRequest, Transaction.Status> status() {
        return multiTypeNode("status");
    }

    public MultipleValueNode<TransactionSearchRequest, Transaction.Source> source() {
        return multiTypeNode("source");
    }

    public MultipleValueNode<TransactionSearchRequest, Transaction.Type> type() {
        return multiTypeNode("type");
    }

    public KeyValueNode<TransactionSearchRequest> refund() {
        return new KeyValueNode<TransactionSearchRequest>("refund", this);
    }

    public RangeNode<TransactionSearchRequest> amount() {
        return new RangeNode<TransactionSearchRequest>("amount", this);
    }

    public DateRangeNode<TransactionSearchRequest> authorizationExpiredAt() {
        return dateRange("authorization_expired_at");
    }

    public DateRangeNode<TransactionSearchRequest> authorizedAt() {
        return dateRange("authorized_at");
    }

    public DateRangeNode<TransactionSearchRequest> createdAt() {
        return dateRange("created_at");
    }

    public DateRangeNode<TransactionSearchRequest> failedAt() {
        return dateRange("failed_at");
    }

    public DateRangeNode<TransactionSearchRequest> gatewayRejectedAt() {
        return dateRange("gateway_rejected_at");
    }

    public DateRangeNode<TransactionSearchRequest> processorDeclinedAt() {
        return dateRange("processor_declined_at");
    }

    public DateRangeNode<TransactionSearchRequest> settledAt() {
        return dateRange("settled_at");
    }

    public DateRangeNode<TransactionSearchRequest> submittedForSettlementAt() {
        return dateRange("submitted_for_settlement_at");
    }

    public DateRangeNode<TransactionSearchRequest> voidedAt() {
        return dateRange("voided_at");
    }

    public DateRangeNode<TransactionSearchRequest> disbursementDate() {
        return dateRange("disbursement_date");
    }

    public DateRangeNode<TransactionSearchRequest> disputeDate() {
        return dateRange("dispute_date");
    }

    private DateRangeNode<TransactionSearchRequest> dateRange(String fieldName) {
        return new DateRangeNode<TransactionSearchRequest>(fieldName, this);
    }

    private TextNode<TransactionSearchRequest> textNode(String fieldName) {
        return new TextNode<TransactionSearchRequest>(fieldName, this);
    }

    public TextNode<TransactionSearchRequest> storeId() {
        return textNode("store_id");
    }

    public MultipleValueNode<TransactionSearchRequest, String> storeIds() {
        return multiTypeNode("store_ids");
    }

    public MultipleValueNode<TransactionSearchRequest, String> reasonCodes() {
        return multiTypeNode("reason_code");
    }

    public TextNode<TransactionSearchRequest> reasonCode() {
        return textNode("reason_code");
    }

    public DateRangeNode<TransactionSearchRequest> achReturnResponsesCreatedAt() {
        return dateRange("ach_return_responses_created_at");
    }

    private <T> MultipleValueNode<TransactionSearchRequest, T> multiTypeNode(String type) {
        return new MultipleValueNode<TransactionSearchRequest, T>(type, this);
    }
}
