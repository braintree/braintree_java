## Unreleased

* Add iDEAL webhook support
* Add `IDEAL_PAYMENT` to `PaymentInstrumentType`
* Add document upload API
* Add AuthorizationAdjustment class and `authorizationAdjustments` to Transaction
* Coinbase is no longer a supported payment method. `PAYMENT_METHOD_NO_LONGER_SUPPORTED` will be returned for Coinbase operations.
* Add facilitated transaction details to Transaction if present
* Add `bin` to `ApplePayCard`
* Add `submitForSettlement` to `SubscriptionGateway.retryCharge`
* Add `options` -> `paypal` -> `description` for creating and updating subscriptions
* Add `Dispute#accept`
* Add `Dispute#addTextEvidence`
* Add `Dispute#addFileEvidence`
* Add `Dispute#finalize`
* Add `Dispute#find`
* Add `Dispute#removeEvidence`

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
* Added ability to specify country using countryName, countryCodeAlpha2, countryCodeAlpha3, or countryCodeNumeric (see [ISO_3166-1](http://en.wikipedia.org/wiki/ISO_3166-1))
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

