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

