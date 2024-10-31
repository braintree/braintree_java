package com.braintreegateway.unittest;

import com.braintreegateway.*;
import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.util.NodeWrapper;
import com.braintreegateway.util.NodeWrapperFactory;
import com.braintreegateway.util.Http;

import org.mockito.Mockito;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class PaymentMethodGatewayTest {
    private BraintreeGateway gateway;

    @BeforeEach
    public void createGateway() {
        this.gateway = new BraintreeGateway(
                Environment.DEVELOPMENT,
                "integration_merchant_id",
                "integration_public_key",
                "integration_private_key"
        );
    }

    @Test
    public void parseResultReturnsAmexExpressCheckoutCard() {
        String xml = "<amex-express-checkout-card><token>foo</token></amex-express-checkout-card>";
        NodeWrapper response = NodeWrapperFactory.instance.create(xml);
        Result<? extends PaymentMethod> result = this.gateway.paymentMethod().parseResponse(response);

        assertEquals("foo", result.getTarget().getToken());
        assertTrue(result.getTarget() instanceof AmexExpressCheckoutCard);
    }

    @Test
    public void parseResultReturnsAndroidPayCard() {
        String xml = "<android-pay-card><token>foo</token></android-pay-card>";
        NodeWrapper response = NodeWrapperFactory.instance.create(xml);
        Result<? extends PaymentMethod> result = this.gateway.paymentMethod().parseResponse(response);

        assertEquals("foo", result.getTarget().getToken());
        assertTrue(result.getTarget() instanceof AndroidPayCard);
    }

    @Test
    public void parseResultReturnsApplePayCard() {
        String xml = "<apple-pay-card><token>foo</token></apple-pay-card>";
        NodeWrapper response = NodeWrapperFactory.instance.create(xml);
        Result<? extends PaymentMethod> result = this.gateway.paymentMethod().parseResponse(response);

        assertEquals("foo", result.getTarget().getToken());
        assertTrue(result.getTarget() instanceof ApplePayCard);
    }

    @Test
    public void parseResultReturnsCreditCard() {
        String xml = "<credit-card><token>foo</token></credit-card>";
        NodeWrapper response = NodeWrapperFactory.instance.create(xml);
        Result<? extends PaymentMethod> result = this.gateway.paymentMethod().parseResponse(response);

        assertEquals("foo", result.getTarget().getToken());
        assertTrue(result.getTarget() instanceof CreditCard);
    }

    @Test
    public void parseResultReturnsCustomActions() {
        String xml = "<custom-actions-payment-method><token>foo</token></custom-actions-payment-method>";
        NodeWrapper response = NodeWrapperFactory.instance.create(xml);
        Result<? extends PaymentMethod> result = this.gateway.paymentMethod().parseResponse(response);

        assertEquals("foo", result.getTarget().getToken());
        assertTrue(result.getTarget() instanceof CustomActionsPaymentMethod);
    }

    @Test
    public void parseResultReturnsMasterpassCard() {
        String xml = "<masterpass-card><token>foo</token></masterpass-card>";
        NodeWrapper response = NodeWrapperFactory.instance.create(xml);
        Result<? extends PaymentMethod> result = this.gateway.paymentMethod().parseResponse(response);

        assertEquals("foo", result.getTarget().getToken());
        assertTrue(result.getTarget() instanceof MasterpassCard);
    }

    @Test
    public void parseResultReturnsPayPalAccount() {
        String xml = "<paypal-account><token>foo</token></paypal-account>";
        NodeWrapper response = NodeWrapperFactory.instance.create(xml);
        Result<? extends PaymentMethod> result = this.gateway.paymentMethod().parseResponse(response);

        assertEquals("foo", result.getTarget().getToken());
        assertTrue(result.getTarget() instanceof PayPalAccount);
    }

    // NEXT_MAJOR_VERSION remove this test
    // SamsungPay has been deprecated
    @Test
    public void parseResultReturnsSamsungPayCard() {
        String xml = "<samsung-pay-card><token>foo</token></samsung-pay-card>";
        NodeWrapper response = NodeWrapperFactory.instance.create(xml);
        Result<? extends PaymentMethod> result = this.gateway.paymentMethod().parseResponse(response);

        assertEquals("foo", result.getTarget().getToken());
        assertTrue(result.getTarget() instanceof SamsungPayCard);
    }

    @Test
    public void parseResultReturnsSaepaDirectDebitAccount() {
        String xml = "<sepa-debit-account><token>foo</token></sepa-debit-account>";
        NodeWrapper response = NodeWrapperFactory.instance.create(xml);
        Result<? extends PaymentMethod> result = this.gateway.paymentMethod().parseResponse(response);

        assertEquals("foo", result.getTarget().getToken());
        assertTrue(result.getTarget() instanceof SepaDirectDebitAccount);
    }

    @Test
    public void parseResultReturnsUsBandAccount() {
        String xml = "<us-bank-account><token>foo</token></us-bank-account>";
        NodeWrapper response = NodeWrapperFactory.instance.create(xml);
        Result<? extends PaymentMethod> result = this.gateway.paymentMethod().parseResponse(response);

        assertEquals("foo", result.getTarget().getToken());
        assertTrue(result.getTarget() instanceof UsBankAccount);
    }

    @Test
    public void parseResultReturnsVenmoAccount() {
        String xml = "<venmo-account><token>foo</token></venmo-account>";
        NodeWrapper response = NodeWrapperFactory.instance.create(xml);
        Result<? extends PaymentMethod> result = this.gateway.paymentMethod().parseResponse(response);

        assertEquals("foo", result.getTarget().getToken());
        assertTrue(result.getTarget() instanceof VenmoAccount);
    }

    @Test
    public void parseResultReturnsVistCheckoutCard() {
        String xml = "<visa-checkout-card><token>foo</token></visa-checkout-card>";
        NodeWrapper response = NodeWrapperFactory.instance.create(xml);
        Result<? extends PaymentMethod> result = this.gateway.paymentMethod().parseResponse(response);

        assertEquals("foo", result.getTarget().getToken());
        assertTrue(result.getTarget() instanceof VisaCheckoutCard);
    }

    @Test
    public void parseResultReturnsUnknownPaymentMethodInElseCase() {
        String xml = "<monopoly-money><token>foo</token></monopoly-money>";
        NodeWrapper response = NodeWrapperFactory.instance.create(xml);
        Result<? extends PaymentMethod> result = this.gateway.paymentMethod().parseResponse(response);

        assertEquals("foo", result.getTarget().getToken());
        assertTrue(result.getTarget() instanceof UnknownPaymentMethod);
    }

    @Test
    public void findThrowsNotFoundExceptionOnNullPointer() {
        PaymentMethodGateway paymentMethodGateway = this.gateway.paymentMethod();
        assertThrows(NotFoundException.class, () -> {
            paymentMethodGateway.find(null);
        });
    }
    
    @Test
    public void deleteAndRevokeAllGrants() {
        Http http = Mockito.mock(Http.class);
        BraintreeGateway gateway = new BraintreeGateway("development", "merchant_id", "public_key", "private_key");
        PaymentMethodGateway paymentMethodGateway = new PaymentMethodGateway(http, gateway.getConfiguration());
        String token = "some_token";
        PaymentMethodDeleteRequest request = new PaymentMethodDeleteRequest().revokeAllGrants(true);
        paymentMethodGateway.delete(token, request);
        Mockito.verify(http).delete("/merchants/merchant_id/payment_methods/any/some_token?revoke_all_grants=true");
    }
}
