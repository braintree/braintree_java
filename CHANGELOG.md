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

