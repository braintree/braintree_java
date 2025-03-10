package com.braintreegateway.test;

// NEXT_MAJOR_VERSION - Remove all SamsungPay references
// SamsungPay has been deprecated
public class Nonce {
  public static final String Transactable = "fake-valid-nonce";
  public static final String Consumed = "fake-consumed-nonce";
  public static final String PayPalOneTimePayment = "fake-paypal-one-time-nonce";
  // NEXT_MAJOR_VERSION - no longer supported in the Gateway, remove this constant
  public static final String PayPalFuturePayment = "fake-paypal-future-nonce";
  public static final String ApplePayVisa = "fake-apple-pay-visa-nonce";
  public static final String ApplePayMastercard = "fake-apple-pay-mastercard-nonce";
  public static final String ApplePayAmex = "fake-apple-pay-amex-nonce";
  public static final String AbstractTransactable = "fake-abstract-transactable-nonce";
  public static final String PayPalBillingAgreement = "fake-paypal-billing-agreement-nonce";
  public static final String AndroidPay = "fake-android-pay-nonce";
  public static final String AndroidPayDiscover = "fake-android-pay-discover-nonce";
  public static final String AndroidPayVisa = "fake-android-pay-visa-nonce";
  public static final String AndroidPayMasterCard = "fake-android-pay-mastercard-nonce";
  public static final String AndroidPayAmEx = "fake-android-pay-amex-nonce";
  public static final String ApplePayMpan = "fake-apple-pay-mpan-nonce";
  @Deprecated
  public static final String AmexExpressCheckout = "fake-amex-express-checkout-nonce";
  public static final String VenmoAccount = "fake-venmo-account-nonce";
  public static final String LocalPayment = "fake-local-payment-method-nonce";
  public static final String SepaDebit = "fake-sepa-direct-debit-nonce";
  @Deprecated
  public static final String MasterpassAmEx = "fake-masterpass-amex-nonce";
  @Deprecated
  public static final String MasterpassDiscover = "fake-masterpass-discover-nonce";
  @Deprecated
  public static final String MasterpassMaestro = "fake-masterpass-maestro-nonce";
  @Deprecated
  public static final String MasterpassMasterCard = "fake-masterpass-mastercard-nonce";
  @Deprecated
  public static final String MasterpassVisa = "fake-masterpass-visa-nonce";
  public static final String MetaCheckoutCard = "fake-meta-checkout-card-nonce";
  public static final String MetaCheckoutToken = "fake-meta-checkout-token-nonce";
  public static final String VisaCheckoutAmEx = "fake-visa-checkout-amex-nonce";
  public static final String VisaCheckoutDiscover = "fake-visa-checkout-discover-nonce";
  public static final String VisaCheckoutMasterCard = "fake-visa-checkout-mastercard-nonce";
  public static final String VisaCheckoutVisa = "fake-visa-checkout-visa-nonce";
  public static final String ThreeDSecureVisaFullAuthentication = "fake-three-d-secure-visa-full-authentication-nonce";
  public static final String ThreeDSecureVisaLookupTimeout = "fake-three-d-secure-visa-lookup-timeout-nonce";
  public static final String ThreeDSecureVisaFailedSignature = "fake-three-d-secure-visa-failed-signature-nonce";
  public static final String ThreeDSecureVisaFailedAuthentication = "fake-three-d-secure-visa-failed-authentication-nonce";
  public static final String ThreeDSecureVisaAttemptsNonParticipating = "fake-three-d-secure-visa-attempts-non-participating-nonce";
  public static final String ThreeDSecureVisaNoteEnrolled = "fake-three-d-secure-visa-not-enrolled-nonce";
  public static final String ThreeDSecureVisaUnavailable = "fake-three-d-secure-visa-unavailable-nonce";
  public static final String ThreeDSecureVisaMPILookupError = "fake-three-d-secure-visa-mpi-lookup-error-nonce";
  public static final String ThreeDSecureVisaMPIAuthenticateError = "fake-three-d-secure-visa-mpi-authenticate-error-nonce";
  public static final String ThreeDSecureVisaAuthenticationUnavailable = "fake-three-d-secure-visa-authentication-unavailable-nonce";
  public static final String ThreeDSecureVisaBypassedAuthentication = "fake-three-d-secure-visa-bypassed-authentication-nonce";
  public static final String ThreeDSecureTwoVisaSuccessfulFrictionlessAuthentication = "fake-three-d-secure-two-visa-successful-frictionless-authentication-nonce";
  public static final String ThreeDSecureTwoVisaSuccessfulStepUpAuthentication = "fake-three-d-secure-two-visa-successful-step-up-authentication-nonce";
  public static final String ThreeDSecureTwoVisaErrorOnLookup = "fake-three-d-secure-two-visa-error-on-lookup-nonce";
  public static final String ThreeDSecureTwoVisaTimeoutOnLookup = "fake-three-d-secure-two-visa-timeout-on-lookup-nonce";
  public static final String TransactableVisa = "fake-valid-visa-nonce";
  public static final String TransactableAmEx = "fake-valid-amex-nonce";
  public static final String TransactableMasterCard = "fake-valid-mastercard-nonce";
  public static final String TransactableDiscover = "fake-valid-discover-nonce";
  public static final String TransactableJCB = "fake-valid-jcb-nonce";
  public static final String TransactableMaestro = "fake-valid-maestro-nonce";
  public static final String TransactableDinersClub = "fake-valid-dinersclub-nonce";
  public static final String TransactablePrepaid = "fake-valid-prepaid-nonce";
  public static final String TransactablePrepaidReloadable = "fake-valid-prepaid-reloadable-nonce";
  public static final String TransactableCommercial = "fake-valid-commercial-nonce";
  public static final String TransactableDurbinRegulated = "fake-valid-durbin-regulated-nonce";
  public static final String TransactableHealthcare = "fake-valid-healthcare-nonce";
  public static final String TransactableDebit = "fake-valid-debit-nonce";
  public static final String TransactablePayroll = "fake-valid-payroll-nonce";
  public static final String TransactablePinlessDebitVisa = "fake-pinless-debit-visa-nonce";
  public static final String TransactableNoIndicators = "fake-valid-no-indicators-nonce";
  public static final String TransactableUnknownIndicators = "fake-valid-unknown-indicators-nonce";
  public static final String TransactableCountryOfIssuanceUSA = "fake-valid-country-of-issuance-usa-nonce";
  public static final String TransactableCountryOfIssuanceCAD = "fake-valid-country-of-issuance-cad-nonce";
  public static final String TransactableIssuingBankNetworkOnly = "fake-valid-issuing-bank-network-only-nonce";
  public static final String ProcessorDeclinedVisa = "fake-processor-declined-visa-nonce";
  public static final String ProcessorDeclinedMasterCard = "fake-processor-declined-mastercard-nonce";
  public static final String ProcessorDeclinedAmEx = "fake-processor-declined-amex-nonce";
  public static final String ProcessorDeclinedDiscover = "fake-processor-declined-discover-nonce";
  public static final String ProcessorFailureJCB = "fake-processor-failure-jcb-nonce";
  public static final String LuhnInvalid = "fake-luhn-invalid-nonce";
  public static final String PayPalFuturePaymentRefreshToken = "fake-paypal-future-refresh-token-nonce";
  public static final String GatewayRejectedFraud = "fake-gateway-rejected-fraud-nonce";
  public static final String GatewayRejectedTokenIssuance = "fake-token-issuance-error-venmo-account-nonce";
  @Deprecated
  public static final String SamsungPayAmEx = "tokensam_fake_american_express";
  @Deprecated
  public static final String SamsungPayDiscover = "tokensam_fake_discover";
  @Deprecated
  public static final String SamsungPayMasterCard = "tokensam_fake_mastercard";
  @Deprecated
  public static final String SamsungPayVisa = "tokensam_fake_visa";
  public static final String UsBankAccount = "fake-us-bank-account-nonce";
}
