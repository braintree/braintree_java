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

