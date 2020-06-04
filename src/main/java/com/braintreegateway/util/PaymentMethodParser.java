package com.braintreegateway.util;

import com.braintreegateway.Result;
import com.braintreegateway.PaymentMethod;
import com.braintreegateway.PayPalAccount;
import com.braintreegateway.CreditCard;
import com.braintreegateway.ApplePayCard;
import com.braintreegateway.AndroidPayCard;
import com.braintreegateway.AmexExpressCheckoutCard;
import com.braintreegateway.UsBankAccount;
import com.braintreegateway.VenmoAccount;
import com.braintreegateway.VisaCheckoutCard;
import com.braintreegateway.MasterpassCard;
import com.braintreegateway.SamsungPayCard;
import com.braintreegateway.CustomActionsPaymentMethod;
import com.braintreegateway.UnknownPaymentMethod;

public class PaymentMethodParser {

    public static Result<? extends PaymentMethod> parsePaymentMethod(NodeWrapper node) {
        if (node.getElementName() == "paypal-account") {
            return new Result<PayPalAccount>(node, PayPalAccount.class);
        } else if (node.getElementName() == "credit-card") {
            return new Result<CreditCard>(node, CreditCard.class);
        } else if (node.getElementName() == "apple-pay-card") {
            return new Result<ApplePayCard>(node, ApplePayCard.class);
        } else if (node.getElementName() == "android-pay-card") {
            return new Result<AndroidPayCard>(node, AndroidPayCard.class);
        } else if (node.getElementName() == "amex-express-checkout-card") {
            return new Result<AmexExpressCheckoutCard>(node, AmexExpressCheckoutCard.class);
        } else if (node.getElementName() == "us-bank-account") {
            return new Result<UsBankAccount>(node, UsBankAccount.class);
        } else if (node.getElementName() == "venmo-account") {
            return new Result<VenmoAccount>(node, VenmoAccount.class);
        } else if (node.getElementName() == "visa-checkout-card") {
            return new Result<VisaCheckoutCard>(node, VisaCheckoutCard.class);
        } else if (node.getElementName() == "masterpass-card") {
            return new Result<MasterpassCard>(node, MasterpassCard.class);
        } else if (node.getElementName() == "samsung-pay-card") {
            return new Result<SamsungPayCard>(node, SamsungPayCard.class);
        } else if (node.getElementName() == "custom-actions-payment-method") {
            return new Result<CustomActionsPaymentMethod>(node, CustomActionsPaymentMethod.class);
        } else {
            return new Result<UnknownPaymentMethod>(node, UnknownPaymentMethod.class);
        }
    }
}
