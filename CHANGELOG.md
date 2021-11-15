## 3.13.0
* Make `junit-vintage` a test dependency (thanks @jamesbroadhead)
* Add plan create/update/find API endpoint
* Add support for `TransactionReview` webhook notification

## 3.12.0
* Add `exchangeRateQuoteId` to `TransactionRequest`
* Add `EXCHANGE_RATE_QUOTE_ID_TOO_LONG` to `ValidationErrorCode`
* Add the following fields to `AndroidPayCard` and `ApplePayCard`:
  * `commercial`
  * `debit`
  * `durbinRegulated`
  * `healthcare`
  * `payroll`
  * `prepaid`
  * `productId`
  * `countryOfIssuance`
  * `issuingBank`
* Add the following fields to `PayPalDetails`:
  * `taxId`
  * `taxIdType`
* Add support for `localPaymentFunded` and `localPaymentExpired` webhook notications
* Sanitize `encryptedCardData` in logs

## 3.11.0
* Add error code `TRANSACTION_TAX_AMOUNT_IS_REQUIRED_FOR_AIB_SWEDISH` for attribute `tax-amount` in `transaction` key for AIB:Domestic transactions in sweden

## 3.10.0
* Add `dataOnlyRequested` to `ThreeDSecureLookupRequest`
* Add `paymentReaderCardDetails` to `TransactionCreditCardRequest`
* Add `chargebackProtectionLevel` to `Dispute` and `DisputeSearchRequest`
* Add `skipAdvancedFraudChecking` to `CreditCardOptionsRequest` and `PaymentMethodOptionsRequest`

## 3.9.0
* Add `getPaypalMessages` to `Dispute`
* Add webhook sample for `GrantedPaymentMethodRevoked`
* Add `taxIdentifier` to `CustomerRequest`

## 3.8.0
* Add support for `LocalPaymentReversed` webhook notifications
* Add `storeId` and `storeIds` to `TransactionSearchRequest`
* Add `merchantAccountId` to `TransactionRefundRequest`
* Add `Transaction.adjustAuthorization` method to support for multiple authorizations for a single transaction

## 3.7.0
* Add `decision_reasons` and `transaction_risk_score` to `RiskData`

## 3.6.0
* Add `scaExemption` to `TransactionRequest`
* Add `scaExemptionRequested` to `Transaction`
* Add validation error code:
  * `TRANSACTION_SCA_EXEMPTION_IS_INVALID`
* Pattern / regexp usage optimization : Re-use patterns / Avoid compiling / creating patterns when not needed (thanks @benbenw!)

## 3.5.0
* Add `currencyIsoCode` to `Transaction`
* Add `verificationCurrencyIsoCode` to `CreditCard`, `PaymentMethod`, and `Customer`
* Add validation error codes:
  * `CREDIT_CARD_OPTIONS_VERIFICATION_INVALID_PRESENTMENT_CURRENCY`
  * `TRANSACTION_INVALID_PRESENTMENT_CURRENCY`

## 3.4.0
* Add `installments` to `TransactionRequest`
* Add `count` to `InstallmentRequest`
* Add `installment` and `refunded_installments` to `Transaction`
* Add `adjustment` to `Installment`
* Fix bug in `ThreeDSecureInfo` loading auth data from wrong XML node (thanks @iainjames88!)
* Avoid allocating memory / constructing object if the message is not going to be logged (thanks @benbenw!)
* Avoid multiple node traversal in webhooks parsing (thanks @benbenw!)
* No need to do a complex status message parsing if the server response is a success (thanks @benbenw!)

## 3.3.0
* Add `acquirerReferenceNumber` to `Transaction`
* Add `billingAgreementId` to `PayPalDetails`
* Add `isRecurring` to Transaction class
* Deprecate `getRecurring` in Transaction class (use `isRecurring` instead)
* Deprecate `recurring` in TransactionRequest

## 3.2.0
* Deprecate `MasterpassCard` payment method
* Deprecate `AmexExpressCheckoutCard` payment method
* Deprecate `deviceSessionId` and `fraudMerchantId` in `CreditCardRequest`, `CustomerRequest`, `PaymentMethodRequest`, and `TransactionRequest` classes
* Fix issue where risk data was not included in `CustomerRequest` (fixes #85)
* Add `getInstallmentCount()` method to `dispute.getTransaction()` on dispute webhooks
* Update `lookup` method on `ThreeDSecureGateway` to return a `Result` object
* Add `implicitlyVaultedPaymentMethodToken` and `implicitlyVaultedPaymentMethodGlobalId` to `PayPalDetails`

## 3.1.0
* Add `RISK_THRESHOLD` to `GatewayRejectionReason`
* Add `networkTransactionId` to `CreditCardVerification`
* Add `productSku` to `TransactionRequest`
* Add `shippingMethod` and `phoneNumber` to `AddressRequest`, `TransactionAddressRequest`
* Add `customerDeviceId`, `customerLocationZip`, and `customerTenure` to `AddressRequest`, `TransactionAddressRequest`
* Add validation error codes:
  * `TRANSACTION_PRODUCT_SKU_IS_INVALID`
  * `TRANSACTION_SHIPPING_PHONE_NUMBER_IS_INVALID`
  * `TRANSACTION_BILLING_PHONE_NUMBER_IS_INVALID`
  * `RISK_DATA_CUSTOMER_BROWSER_IS_TOO_LONG`
  * `RISK_DATA_CUSTOMER_DEVICE_ID_IS_TOO_LONG`
  * `RISK_DATA_CUSTOMER_LOCATION_ZIP_INVALID_CHARACTERS`
  * `RISK_DATA_CUSTOMER_LOCATION_ZIP_IS_INVALID`
  * `RISK_DATA_CUSTOMER_LOCATION_ZIP_IS_TOO_LONG`
  * `RISK_DATA_CUSTOMER_TENURE_IS_TOO_LONG`
* Add `isProcessedWithNetworkToken` to `Transaction`
* Add `isNetworkTokenized` to `CreditCard`

## 3.0.0
* Add `RequestTimeoutException` and `GatewayTimeoutException`
* Breaking Changes:
  * Stop sending validation errors for declined refunds, instead send the transaction object
  * Remove deprecated Coinbase references
  * Remove deprecated Transparent Redirect, Ideal, SEPA modules, error codes, and functions
  * Remove deprecated PaymentMethodForwardRequest class
  * Remove deprecated methods from `Dispute`
    * Remove `getForwardedComments` (use `getProcessorComments`)
    * Remove `getTransactionDetails` (use `getTransaction`)
  * Remove deprecated `getTag` (use `getCategory`) from `DisputeEvidence`
  * Remove deprecated `paypalVaultWithoutUpgrade` from `PaymentMethodRequest`
  * Remove deprecated parameters from `SearchRequest`
    * Remove `transparentRedirectURLForCreate`
    * Remove `creditTrData`
  * Remove deprecated `getNextBillAmount` from `Subscription`
  * Remove deprecated `tag` (use `category`) from `TextEvidenceRequest`
  * Remove deprecated methods from `Transaction`
    * Remove `getRefundId` (use `getRefundIds`)
    * Remove `getSubscription` (use `getSubscriptionDetails`)
  * Remove deprecated method `getRows` (use `TransactionLevelFeeReport#getCSVRecords`) from `TransactionLevelFeeReport`
  * Remove deprecated validation error codes
    * `CUSTOMER_ID_IS_INVAILD`
    * `CUSTOMER_ID_IS_INVALID`
    * `TRANSACTION_LINE_ITEM_DISCOUNT_AMOUNT_MUST_BE_GREATER_THAN_ZERO`
    * `TRANSACTION_LINE_ITEM_UNIT_TAX_AMOUNT_MUST_BE_GREATER_THAN_ZERO`
    * `TRANSACTION_LINE_ITEM_TAX_AMOUNT_MUST_BE_GREATER_THAN_ZERO`
    * `EUROPE_BANK_ACCOUNT_ACCOUNT_HOLDER_NAME_IS_REQUIRED`
    * `EUROPE_BANK_ACCOUNT_BIC_IS_REQUIRED`
    * `EUROPE_BANK_ACCOUNT_IBAN_IS_REQUIRED`
    * `SEPA_MANDATE_ACCOUNT_HOLDER_NAME_IS_REQUIRED`
    * `SEPA_MANDATE_BIC_INVALID_CHARACTER`
    * `SEPA_MANDATE_BIC_IS_REQUIRED`
    * `SEPA_MANDATE_BIC_LENGTH_IS_INVALID`
    * `SEPA_MANDATE_BIC_UNSUPPORTED_COUNTRY`
    * `SEPA_MANDATE_BILLING_ADDRESS_CONFLICT`
    * `SEPA_MANDATE_BILLING_ADDRESS_ID_IS_INVALID`
    * `SEPA_MANDATE_IBAN_INVALID_CHARACTER`
    * `SEPA_MANDATE_IBAN_INVALID_FORMAT`
    * `SEPA_MANDATE_IBAN_IS_REQUIRED`
    * `SEPA_MANDATE_IBAN_UNSUPPORTED_COUNTRY`
    * `SEPA_MANDATE_TYPE_IS_REQUIRED`
    * `SEPA_MANDATE_TYPE_IS_INVALID`
    * `TRANSACTION_AMOUNT_DOES_NOT_MATCH_IDEAL_PAYMENT_AMOUNT`
    * `TRANSACTION_IDEAL_PAYMENT_NOT_COMPLETE`
    * `TRANSACTION_IDEAL_PAYMENTS_CANNOT_BE_VAULTED`
    * `TRANSACTION_MERCHANT_ACCOUNT_DOES_NOT_MATCH_IDEAL_PAYMENT_MERCHANT_ACCOUNT`
    * `TRANSACTION_MERCHANT_ACCOUNT_NAME_IS_INVALID`
    * `TRANSACTION_ORDER_ID_DOES_NOT_MATCH_IDEAL_PAYMENT_ORDER_ID`
    * `TRANSACTION_ORDER_ID_IS_REQUIRED_WITH_IDEAL_PAYMENT`
    * `UNKOWN_VALIDATION_ERROR` (changed to `UNKNOWN_VALIDATION_ERROR`)
  * Error codes cannot be modified
  * Remove `GRANTED_PAYMENT_INSTRUMENT_UPDATE` Webhook notification
  * Remove deprecated `greaterThanOrEqual` method for searches (use `greaterThanOrEqualTo`)
  * Remove deprecated `lessThanOrEqual` method for searches (use `lessThanOrEqualTo`)
  * `DownForMaintenance` exception renamed to `ServiceUnavailable` Exception
  * Transaction searches throw `UnexpectedException` instead of `DownForMaintenance` when search response yields unexpected results
  * Remove `recurringCustomerConsent` and `recurringMaxAmount` parameters from `authenticationInsightOptions` in `PaymentMethodNonce.create()`

## 2.109.0
* Add `threeDSecurePassThru` to `CreditCard.create()`, `CreditCard.update()`, `Customer.create()`, `Customer.update()`, `PaymentMethod.create()` and `PaymentMethod.update()` 
* Add missing `paymentMethodToken` search to `CreditCardVerificationSearchRequest`
* Add `recurringCustomerConsent` and `recurringMaxAmount` parameters to `authenticationInsightOptions` in `PaymentMethodNonce.create()`
* Add `DOCUMENT_UPLOAD_FILE_IS_EMPTY` error code
* Add `getBillingAddress` method to `PaymentMethodNonceDetailsPayerInfo` (#83 thanks @eliasjpr)
* Add `getShippingAddress` method to `PaymentMethodNonceDetailsPayerInfo` (#83 thanks @eliasjpr)

## 2.108.0
* Fix null pointer exception when 3DS lookup returns a `422`
* Add ThreeDSecure test payment method nonces
* Add test `AuthenticationId`s
* Add `DISPUTE_ACCEPTED`, `DISPUTE_DISPUTED`, and `DISPUTE_EXPIRED` webhook constants
* Fix parsing for 3DS response authenticate fields

## 2.107.0
* Add `TRANSACTION_REFUND_AUTH_HARD_DECLINED` and `TRANSACTION_REFUND_AUTH_SOFT_DECLINED` validation errors
* Add `isNetworkTokenized` field to `AndroidPayCard` and `AndroidPayDetails`
* Add GraphQL ID to `CreditCardVerification`, `Customer`, `Dispute`, and `Transaction`
* Add `retrievalReferenceNumber` field to `Transaction`

## 2.106.0
* Add `PROCESSOR_DOES_NOT_SUPPORT_MOTO_FOR_CARD_TYPE` to validation errors
* Update `jackson-jr` to v2.9.9 to fix CVE-2018-11307 (#75 thanks @sehrope)
* Add `threeDSecureAuthenticationId` to `ThreeDSecureInfo`
* Add `acsTransactionId` to `ThreeDSecureInfo`
* Add `paresStatus` to `ThreeDSecureInfo`
* Add `threeDSecureServerTransactionId` to `ThreeDSecureInfo`
* Add `threeDSecureLookupInfo` to `ThreeDSecureInfo`
* Add `threeDSecureAuthenticateInfo` to `ThreeDSecureInfo`

## 2.105.0
* Add `merchantAccountId` setter to `ThreeDSecureLookupRequest`

## 2.104.0
* Add `getProcessorComments` to `Dispute`
* Add `TRANSACTION_AMOUNT_NOT_SUPPORTED_BY_PROCESSOR` to validation errors
* Deprecate `getForwardedComments` from `Dispute`
* Deprecate `getRows` from `TransactionLevelFeeReport` in favor of `getCSVRecords`

## 2.103.0
* Add `getUniqueNumberIdentifier` to `CustomActionsPaymentMethodDetails`

## 2.102.1
* Fix issue where billing address was not applied correctly in `ThreeDSecureLookupRequest`

## 2.102.0
* Add `networkResponseCode` and `networkResponseText` fields to `Transaction` and `CreditCardVerification`
* Add `graphQLClient` to the `BraintreeGateway` class
* Add `threeDSecureInfo` to `CreditCardVerification`
* Add `xid`, `cavv`, `eciFlag`, `dsTransactionId`, and `threeDSecureVersion` to `ThreeDSecureInfo`
* Add `PayPalHereDetails` to `Transaction`

## 2.101.0
* Add more Venmo error codes to `ValidationErrorCode`

## 2.100.0
* Add `CustomActionsPaymentMethod` support

## 2.99.0
* Add Venmo error codes to `ValidationErrorCode`

## 2.98.0
* Avoid throwing an exception when Refunded Amount is empty in the Transaction-Level Fee Report
* Add `captureId` field to `LocalPaymentDetail`
* Add `refundId` field to `LocalPaymentDetail`
* Add `debugId` field to `LocalPaymentDetail`
* Add `transactionFeeAmount` field to `LocalPaymentDetail`
* Add `transactionFeeCurrencyIsoCode` field to `LocalPaymentDetail`
* Add `refundFromTransactionFeeAmount` field to `LocalPaymentDetail`
* Add `refundFromTransactionFeeCurrencyIsoCode` field to `LocalPaymentDetail`
* Add `dsTransactionId` to `ThreeDSecurePassthruRequest`

## 2.97.0
* Fix serialization of `ThreeDSecureLookupAdditionalInformation`
* Return `AuthenticationInsight` when requested on `PaymentMethodNonce.create`
* Add `roomTax` field to `TransactionIndustryDataRequest`
* Add `noShow` field to `TransactionIndustryDataRequest`
* Add `advancedDeposit` field to `TransactionIndustryDataRequest`
* Add `fireSafe` field to `TransactionIndustryDataRequest`
* Add `propertyPhone` field to `TransactionIndustryDataRequest`
* Add `additionalChargeRequests` field to `TransactionIndustryDataRequest`
* Add `payerInfo` to `PaymentMethodNonceDetails` class
* Add `PostalCodeIsRequiredForCardBrandAndProcessor` to validation errors

## 2.96.0
* Add `ThreeDSecureLookup`

## 2.95.0
* Add `revokedAt` field to `PayPalAccount`
* Add support for `PAYMENT_METHOD_REVOKED_BY_CUSTOMER` webhook
* Add `payment_method_nonce` field to `LocalPaymentCompleted` webhook
* Add `transaction` field to `LocalPaymentCompleted` webhook
* Add `LocalPaymentDetails` to transactions

## 2.94.0
* Add `refundFromTransactionFeeAmount` field to `PayPalDetails`
* Add `refundFromTransactionFeeCurrencyIsoCode` field to `PayPalDetails`
* Add `token_issuance` gateway reject status to support Venmo transactions
* Add `threeDSecureVersion` to `ThreeDSecurePassthruRequest`

## 2.93.0
* Add `accountType` to `Transaction`, `CreditCard`, `PaymentMethod`, and `CreditCardVerification`

## 2.93.0
* Add `accountType` to `Transaction`, `CreditCard`, `PaymentMethod`, and `CreditCardVerification`

## 2.92.0
* Deprecate `GRANTED_PAYMENT_INSTRUMENT_UPDATE` and add `GRANTOR_UPDATED_GRANTED_PAYMENT_METHOD` and `RECIPIENT_UPDATED_GRANTED_PAYMENT_METHOD`
* Fix bug where dates in the Transaction-Level Fee Report could be parsed incorrectly due to timezone differences
* Add External Vault and Risk Data to `CreditCardVerificationRequest`
* Add `prepaid` field to `AndroidPayDetails`
* Add `healthcare` field to `AndroidPayDetails`
* Add `debit` field to `AndroidPayDetails`
* Add `durbinRegulated` field to `AndroidPayDetails`
* Add `commercial` field to `AndroidPayDetails`
* Add `payroll` field to `AndroidPayDetails`
* Add `issuingBank` field to `AndroidPayDetails`
* Add `countryOfIssuance` field to `AndroidPayDetails`
* Add `productId` field to `AndroidPayDetails`
* Add `globalId` field to `AndroidPayDetails`

## 2.91.0
* Add `bin` field to `PaymentMethodNonceDetails`
* Add `prepaid` field to `ApplePayDetails`
* Add `healthcare` field to `ApplePayDetails`
* Add `debit` field to `ApplePayDetails`
* Add `durbinRegulated` field to `ApplePayDetails`
* Add `commercial` field to `ApplePayDetails`
* Add `payroll` field to `ApplePayDetails`
* Add `issuingBank` field to `ApplePayDetails`
* Add `countryOfIssuance` field to `ApplePayDetails`
* Add `productId` field to `ApplePayDetails`
* Add `bin` field to `ApplePayDetails`
* Add `globalId` field to `ApplePayDetails`
* Add Error indicating pdf uploads too long for dispute evidence.

## 2.90.0
* Add `fraudServiceProvider` field to `riskData`

## 2.89.0
* Allow PayPal payment ID and payer ID to be passed during transaction create
* Fix bug where SDK expects `granted_payment_instrument_revoked` instead of `granted_payment_method_revoked`

## 2.88.0
* Add `travel_flight` support to industry-specific data

## 2.87.0
* Add `GrantedPaymentMethodRevoked` webhook response objects
* Add missing `getCardholderName` method to ApplePayCard class
* Add missing nonce detail params for non-credit card nonces

## 2.86.0
* Add `authorizationExpiresAt` to `Transaction`

## 2.85.0
* Add venmo account details to payment method nonce model
* Add `ProcessorResponseType` to `Transaction`, `AuthorizationAdjustment`, and `CreditCardVerification`.

## 2.84.0
* Add `lastFour` to `PaymentMethodNonceDetails`
* Remove final specification from `MerchantAccountGateway` (#64)
* Fix dispute results in transactions not showing the correct status sometimes
* Pass response message to http error (#66)
* Add `getNetworkTransactionId` as new field on subfield transaction response.
* Add support for `ExternalVaultRequest` for TransactionRequest
* Add support for `LocalPaymentCompleted` webhook notifications.
* Close FileInputStream in `addFilePart` if an exception occurs when reading.
* Add `ProcessorResponseType` to `Transaction`, `AuthorizationAdjustment`, and `CreditCardVerification`.

## 2.83.1
* Restore Javadoc

## 2.83.0
* Add subscription charged unsuccessfully sample webhook to webhook testing gateway
* Add initial support for GraphQL API's
* Add Transaction-Level Fee Report API
* Add support for Samsung Pay

## 2.81.0
* Allow payee ID to be passed in options params for transaction create
* Add `processor_response_code` and `processor_response_text` to authorization adjustments subfield in transaction response.
* Add `getMerchantId` to `ConnectedMerchantStatusTransitioned` and `ConnectedMerchantPayPalStatusChanged`, and `getOauthApplicationClientId` to OAuthAccessRevocation webhooks
* Fix webhook testing sample xml for dispute webhooks to include `amount-won` and `amount-disputed`

## 2.80.0
* Add support for US Bank Account verifications API

## 2.79.0
* Update `jackson-jr` to v2.9.5 to fix an incompatibility with Java 9
* Fix issue where multiple search criteria was not allowed. [#62](https://github.com/braintree/braintree_java/issues/62).
* Add support for `SubscriptionDetails` and deprecate `Subscription` in `Transaction`

## 2.78.0
* Add support for `OAUTH_ACCESS_REVOKED` in `WebhookNotification`s
* Add support for dispute search by `customerId`, `disbursementDate`, and `effectiveDate`
* Add `payerId` accessor in `PayPalAccount`
* Add support for VCR compelling evidence dispute representment

## 2.77.0
* Fix possible NullPointerException in UsBankAccount
* Add support for `association_filter_id` in `Customer#find`

## 2.76.0
* Deprecated `TRANSACTION_LINE_ITEM_DISCOUNT_AMOUNT_MUST_BE_GREATER_THAN_ZERO` error in favor of `TRANSACTION_LINE_ITEM_DISCOUNT_AMOUNT_CANNOT_BE_NEGATIVE`
* Deprecated `TRANSACTION_LINE_ITEM_UNIT_TAX_AMOUNT_MUST_BE_GREATER_THAN_ZERO` error in favor of `TRANSACTION_LINE_ITEM_UNIT_TAX_AMOUNT_CANNOT_BE_NEGATIVE`
* Deprecated `TRANSACTION_LINE_ITEM_TAX_AMOUNT_MUST_BE_GREATER_THAN_ZERO` error in favor of `TRANSACTION_LINE_ITEM_TAX_AMOUNT_CANNOT_BE_NEGATIVE`

## 2.75.0
* Update https certificate bundle
* Add `getSourceMerchantId()` to `WebhookNotification`s
* Add support for taxAmount field on transaction lineItems
* Swap out `org.json` package for `jackson-jr` to fix licensing issues (#58,#59)
* Add support for `profile_id` in Transaction#create options for VenmoAccounts

## 2.74.1
* Add support for line_items
* Add support for tagged evidence in DisputeGateway#addTextEvidence (Beta release)
* Add support for raw Apple Pay processing
* Add support for setting connect timeout (#56)

## 2.74.0

* Add support for upgrading a PayPal future payment refresh token to a billing agreement
* Add loginOnly parameter to OAuth connect URL
* Add support for Granted Payment Instrument Update webhook
* Add ability to create a transaction from a shared nonce
* Fix spec to expect PayPal transaction to settle immediately after successful capture
* Add `options` -> `paypal` -> `shipping` for creating & updating customers as well as creating payment methods
* Add `imageUrl` to `ApplePayDetails`
* Deprecate `OAuthGateway::computeSignature`
* Fix spec to expect PayPal transactions to move to settling rather than settled
* Add `deviceDataCaptured` field to `RiskData`
* Add `binData` to `PaymentMethodNonce`
* Throw error if `signature` or `payload` in webhook is null
* Fix `receivedDate`/`replyByDate` by returning `DateRangeNode` in `DisputeSearchRequest` (Note: this is a breaking change for limited relese disputes API)

## 2.73.0

* Add iDEAL webhook support
* Add `IDEAL_PAYMENT` to `PaymentInstrumentType`
* Add document upload API
* Add AuthorizationAdjustment class and `authorizationAdjustments` to Transaction
* Coinbase is no longer a supported payment method. `PAYMENT_METHOD_NO_LONGER_SUPPORTED` will be returned for Coinbase operations
* Add facilitated transaction details to Transaction if present
* Add `bin` to `ApplePayCard`
* Add `submitForSettlement` to `SubscriptionGateway.retryCharge`
* Add `options` -> `paypal` -> `description` for creating and updating subscriptions
* Add `accept` method for the Dispute API
* Add `addTextEvidence` method for the Dispute API
* Add `addFileEvidence` method for the Dispute API
* Add `finalize` method for the Dispute API
* Add `find` method for the Dispute API
* Add `removeEvidence` method for the Dispute API
* Add `search` method for the Dispute API

## 2.72.1

* Add additional properties to `PaymentMethodNonce`

## 2.72.0

* Add `skipAvs` and `skipCvv` options to `TransactionOptionsRequest`
* `CreditCardVerification` now contains `amount` and `currencyIsoCode`
* Add iDEAL support
* Add Masterpass support
* Add Visa Checkout support
* Add support for additional PayPal options when vaulting a PayPal Order
* Stop sending `account_description` field from us bank accounts
* Add `ConnectedMerchantStatusTransitioned` and `ConnectedMerchantPayPalStatusChanged` Auth webhooks

## 2.71.0
* Stop sending account_description field from us bank accounts

## 2.70.0
* Add functionality to list all merchant accounts for a merchant with `MerchantAccount.all`.
* Update UsBank tests to use legal routing numbers.
* Add option `skip_advanced_fraud_check` for transaction flows.
* Raise an exception if fetching pages of results times out during a transaction search.

## 2.69.0
* Fix `UsBankAccount` support for `Customer`s
* Throw `ConfigurationException` for missing credentials
* Refactor payment method grant and revoke APIs to accept `PaymentMethodGrantRequest` and `PaymentMethodGrantRevokeRequest` objects as parameters

## 2.68.0
* Add 'UsBankAccount' payment method

## 2.67.0
* Use TLS v1.2 by default and fallback to TLS
* Add getPlanId() to SubscriptionStatusEvent
* Add createdAt to subscription search

## 2.66.0
* Log encoding error instead of printing stack trace
* Reuse SSLSocketFactory for http requests
* Allow proxy objects to be used to specify proxy config
* Add currency iso code
* Expose credit card product ID
* Add validation error for verifications with submerchants

## 2.65.0
* Allow passing OAuth scopes to `Merchant#create`
* Support passing `transaction_source` for setting moto or recurring ECI flag
* Add support for passing risk data
* Allow updating default_payment_method on Customer

## 2.64.0
* Add OrderId to refund
* Add 3DS Pass thru support
* Add verificiation amount option to Payment Method request
* Expose ids in resource collections

## 2.63.0
* Add method of revoking OAuth access tokens.

## 2.62.0
* Add transaction `UpdateDetails`
* Add ssnLast4 to merchant account individual details
* Support for Too Many Requests response codes

## 2.61.0
* Add new landing_page param to OAuthConnectUrlRequest

## 2.60.0
* Add getSubscriptions method to PaymentMethod interface (Thanks @singhalkul!)
* Add establishedOn attribute to partner business data
* Allow passing companyName and currencies to `Merchant#create`

## 2.59.0
* Remove java.util.logging.ConsoleHandler
* Add isInvalid error code for addresses

## 2.58.0
* Add timeout attribute to Configuration
* Adds shared vault parameters

## 2.57.0
* Add support for logging. Thanks @yatsenko-ihor!

## 2.56.0
* Add AccountUpdaterDailyReport webhook parsing

## 2.55.0
* Add support for OSGi bundling. Thanks, @lburgazzoli!

## 2.54.0
* Add verification create API
* Add support for options in `submit_for_settlement` transaction flows

## 2.53.0
* Include Coinbase Accounts in `Customer#getPaymentMethods` (Thanks @danmidwood)
* Add VenmoAccount

## 2.52.0
* Adds `CHECK` webhook kind

## 2.51.0
* Fixes broken maven release

## 2.50.0
* Add kind, openedDate, and wonDate to Dispute
* Add support for partial settlement transactions
* Add transaction data to subsription charged successfully webhooks

## 2.49.0
* Add sourceDescription attribute to Android Pay and Apple Pay
* Add new Android Pay test nonces
* Add support for amex rewards transactions

## 2.48.0
* Add new test payment method nonces
* Allow passing description on PayPal transactions
* Expose transaction fee details on PayPal transactions

## 2.47.0
* Add support for PayPal billing agreements

## 2.46.0
* Add oauth support

## 2.45.0
* Add support for Android Pay

## 2.44.0
* Validate webhook challenge payload

## 2.43.0
* Fix ThreeDSecureInfo#find to be more consistent

## 2.42.0
* Add 3DS info to the server side

## 2.41.2
* Simplified SEPA workflow

## 2.41.1
## 2.41.0
* Close InputStreams after use
* Add additional PayPalDetails getters

## 2.40.0
* Surface Apple Pay payment instrument name in responses
* Support making 3DSecure required
* Add support for Coinbase

## 2.39.2
* Explicitly disconnect the HttpURLConnection after use (Thanks, @gsharma)

## 2.39.1
* New validation error constants
* Add ApplePayDetails#getLast4
* More searches from CreditCardVerificationSearchRequest

## 2.38.0
* Allow payee_email in transaction.options.paypal
* Allow PayPal custom field on transaction create
* Add support for Retrieval dispute reasons.

## 2.37.0

* Add risk_data to Transaction and Verification with Kount decision and id
* Add verification_amount an option when creating a credit card
* Add TravelCruise industry type to Transaction
* Add room_rate to Lodging industry type
* Add CreditCard#verification as the latest verification on that credit card
* Add ApplePay support to all endpoints that may return ApplePayCard objects
* Add prefix to sample Webhook to simulate webhook query params

## 2.36.0

* ApplePay support

## 2.35.0

* Allow descriptor to be passed in Funding Details options params for Merchant Account create and update.

## 2.34.0

* Add additionalProcessorResponse to transaction

## 2.33.1

* Allow payee_email to be passed in options params for Transaction create

## 2.33.0

* Added paypal specific fields to transaction calls
* Added SettlementPending, SettlementDeclined transaction statuses

## 2.32.0

* Add descriptor url support

## 2.31.0

* Allow credit card verification options to be passed outside of the nonce for PaymentMethod.create
* Allow billing_address parameters and billing_address_id to be passed outside of the nonce for PaymentMethod.create
* Add Subscriptions to paypal accounts
* Add PaymentMethod.update
* Add fail_on_duplicate_payment_method option to PaymentMethod.create
* Add support for dispute webhooks

## 2.30.1
* Support for v.zero SDKs.

## 2.29.1

* Make webhook parsing more robust with newlines
* Add messages to InvalidSignature exceptions

## 2.29.0

* Include Dispute information on Transaction
* Search for Transactions disputed on a certain date

## 2.28.0

* Disbursement Webhooks

## 2.27.0
* Merchant account find API

## 2.26.0
* Merchant account update API
* Merchant account create API v2

## 2.25.1
* Use new Braintree Gateway API endpoints

## 2.25.0
* Adds support for Partnerships

## 2.24.1
* Adds fraud to gatewayRejectionReason, unrecognized to all enums missing it.

## 2.24.0

* Adds holdInEscrow method
* Add error codes for verification not supported error
* Add companyName and taxId to ApplicantDetailsRequest
* Adds cancelRelease method
* Adds releaseFromEscrow functionality
* Adds merchant account phone error code.

## 2.23.0

* Adds device data to transactions, customers, and credit cards.
* Lots of cleanups [thanks to https://github.com/steve-nester-uk]

## 2.22.1

* Bumps version in maven pom.xml

## 2.22.0

* Adds disbursement details to transactions.
* Adds image url to transactions.

## 2.21.0

* Adds Venmo Touch support.

## 2.20.0

* Adds channel field to transactions.

## 2.19.0

* Adds country of issuance and issuing bank

## 2.18.0

* Adds verification search

## 2.17.0

* Additional card information, such as prepaid, debit, commercial, Durbin regulated, healthcare, and payroll, are returned on credit card responses
* Allows transactions to be specified as recurring

## 2.16.0

* Adds prepaid attribute on credit cards (possible values: Yes, No, Unknown)

## 2.15.0

* Adds webhook gateways for parsing, verifying, and testing incoming notifications

## 2.14.0

* Adds search for duplicate credit cards given a payment method token
* Adds flag to fail saving credit card to vault if card is duplicate

## 2.13.4

* Exposes plan_id on transactions

## 2.13.3

* Adds new certificates for sandbox environment

## 2.13.2

* Added error code for invalid purchase order number

## 2.13.1

* Added error message for merchant accounts that do not support refunds

## 2.13.0

* Added ability to retrieve all Plans, Addons, and Discounts
* Added Transaction cloning

## 2.12.1

* Update README to include Maven repository documentation

## 2.12.0

* Implemented new NodeWrapper (SimpleNodeWrapper) that is much faster than the previous xpath implementation.

## 2.11.1

* Fixed a bug in SettlementBatchSummary that may have requested the wrong date when using a timezone other than UTC.

## 2.11.0

* Added SettlementBatchSummary

## 2.10.1

* Correctly report PAST_DUE status on Subscriptions.

## 2.10.0

* Added subscription to Transaction
* Added flag to store in vault only when a transaction is successful
* Added new error code

## 2.9.0

* Added a new transaction state, AUTHORIZATION_EXPIRED.
* Enabled searching by authorizationExpiredAt.

## 2.8.0

* Added nextBillingDate and transactionId to subscription search
* Added addressCountryName to customer search
* Added new error codes

## 2.7.0

* Added Customer search
* Added dynamic descriptors to Subscriptions and Transactions
* Added level 2 fields to Transactions:
  * tax_amount
  * tax_exempt
  * purchase_order_number

## 2.6.1

* Added billingAddressId to CreditCardRequest
* Allow searching on Subscriptions that are currently in a trial period using inTrialPeriod

## 2.6.0

* Added ability to perform multiple partial refunds on Transactions
* Deprecated Transaction getRefundId in favor of getRefundIds
* Added revertSubscriptionOnProrationFailure flag to Subscription update that specifies how a Subscription should react to a failed proration charge
* Deprecated Subscription getNextBillAmount in favor of getNextBillingPeriodAmount
* Added new properties to Subscription:
  * balance
  * paidThroughDate
  * nextBillingPeriodAmount

## 2.5.0

* Added AddOns/Discounts
* Enhanced Subscription search
* Enhanced Transaction search
* Added AddOn/Discount details to Transactions that were created from a Subscription
* Added an enum for CreditCardVerification statuses
* Added EXPIRED and PENDING statuses to Subscription
* Allowed prorateCharges to be specified on Subscription update
* Renamed search methods greaterThanOrEqual and lessThanOrEqual to greaterThanOrEqualTo and lessThanOrEqualTo -- deprecated old methods
* All Braintree Exceptions now inherit from BraintreeException superclass
* Added new properties to Subscription:
  * billingDayOfMonth
  * daysPastDue
  * firstBillingDate
  * neverExpires
  * numberOfBillingCycles

## 2.4.0

* Added unified message to result objects
* Added ability to specify country using countryName, countryCodeAlpha2, countryCodeAlpha3, or countryCodeNumeric (see [ISO_3166-1](https://en.wikipedia.org/wiki/ISO_3166-1))
* Added gatewayRejectionReason to Transaction and Verification
* When creating a Subscription, return failed transaction on the Result if the initial transaction is not successful

## 2.3.1

* Fixed a bug in confirming TransparentRedirect when returning an Error result

## 2.3.0

* Added unified TransparentRedirect url and confirm methods and deprecated old methods
* Added methods to CreditCardGateway to allow searching on expiring and expired credit cards
* Allow credit card verification against a specified merchant account
* Added ability to update a customer, credit card, and billing address in one request
* Allow updating the payment method token on a subscription
* Added methods to navigate between a Transaction and its refund (in both directions)

## 2.2.1

* Read VERSION from a constant instead of a VERSION file, which can conflict with other VERSION files in load path.

## 2.2.0

* Prevent race condition when pulling back collection results -- search results represent the state of the data at the time the query was run
* Rename ResourceCollection's getApproximateSize to getMaximumSize because items that no longer match the query will not be returned in the result set
* Correctly handle HTTP error 426 (Upgrade Required) -- the error code is returned when the client library version is no longer compatible with the gateway

## 2.1.0

* Added transaction advanced search
* Added ability to partially refund transactions
* Added ability to manually retry past-due subscriptions
* Added new transaction error codes
* Allow merchant account to be specified when creating transactions
* Allow creating a transaction with a vault customer and new credit card
* Allow existing billing address to be updated when updating credit card

## 2.0.0

* Updated isSuccess() on transaction results to return false on declined transactions
* Search results now implement Iterable and will automatically paginate data
* Added getCardholderName() to CreditCard

## 1.2.0

* Added option to change default credit card for a customer
* Added subscription search
* Return associated subscriptions when finding credit cards
* Updated forObject to return an empty ValidationErrors object instead of null if there are no errors
* Raise down for maintenance exception instead of forged query string when down for maintenance

## 1.1.4

* Added java 1.5 compatibility

## 1.1.3

* Updated production URL

## 1.1.2

* Added processorAuthorizationCode() to Transaction

## 1.1.1

* Fixed a bug preventing TransactionRequest objects with nested Customer objects from building properly
* Added getMaskedNumber() to CreditCard to return the masked credit card number

## 1.1.0

* Added recurring billing support

## 1.0.0

* Initial release
