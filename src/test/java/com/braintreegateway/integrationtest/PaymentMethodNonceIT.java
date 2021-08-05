package com.braintreegateway.integrationtest;

import com.braintreegateway.*;
import com.braintreegateway.CreditCard.Commercial;
import com.braintreegateway.CreditCard.DurbinRegulated;
import com.braintreegateway.CreditCard.Debit;
import com.braintreegateway.CreditCard.Healthcare;
import com.braintreegateway.CreditCard.Payroll;
import com.braintreegateway.CreditCard.Prepaid;
import com.braintreegateway.testhelpers.TestHelper;
import com.braintreegateway.testhelpers.MerchantAccountTestConstants;
import com.braintreegateway.exceptions.NotFoundException;
import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PaymentMethodNonceIT extends IntegrationTest {

    @Test
    public void createFromExistingPaymentMethod() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        Customer customer = customerResult.getTarget();

        String nonce = TestHelper.generateUnlockedNonce(gateway, null, SandboxValues.CreditCardNumber.VISA.number);
        PaymentMethodRequest request = new PaymentMethodRequest().
            customerId(customer.getId()).
            paymentMethodNonce(nonce);

        Result<? extends PaymentMethod> paymentMethodResult = gateway.paymentMethod().create(request);
        PaymentMethod paymentMethod = paymentMethodResult.getTarget();

        Result<PaymentMethodNonce> result = gateway.paymentMethodNonce().create(paymentMethod.getToken());
        assertTrue(result.isSuccess());

        PaymentMethodNonce newNonce = result.getTarget();
        assertNotNull(newNonce);
        assertNotNull(newNonce.getNonce());
    }

    @Test
    public void createReturnsAuthenticationInsightWhenRequested() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        Customer customer = customerResult.getTarget();

        String nonce = TestHelper.generateUnlockedNonce(gateway, null, SandboxValues.CreditCardNumber.VISA.number);
        PaymentMethodRequest request = new PaymentMethodRequest().
            customerId(customer.getId()).
            paymentMethodNonce(nonce);

        Result<? extends PaymentMethod> paymentMethodResult = gateway.paymentMethod().create(request);
        PaymentMethod paymentMethod = paymentMethodResult.getTarget();

        PaymentMethodNonceRequest createRequest = new PaymentMethodNonceRequest().
            paymentMethodToken(paymentMethod.getToken()).
            merchantAccountId(MerchantAccountTestConstants.DEFAULT_MERCHANT_ACCOUNT_ID).
            authenticationInsight(Boolean.TRUE);

        Result<PaymentMethodNonce> result = gateway.paymentMethodNonce().create(createRequest);
        assertTrue(result.isSuccess());

        PaymentMethodNonce newNonce = result.getTarget();
        assertNotNull(newNonce);
        assertNotNull(newNonce.getNonce());
        assertNotNull(newNonce.getAuthenticationInsight());
        assertNotNull(newNonce.getAuthenticationInsight().getRegulationEnvironment());
        assertNull(newNonce.getAuthenticationInsight().getScaIndicator());
    }

    @Test
    public void createReturnsAuthenticationInsightWithScaRequirementWhenGivenAmount() {
        PaymentMethodNonceRequest createRequest = new PaymentMethodNonceRequest().
            paymentMethodToken("india_visa_credit").
            merchantAccountId(MerchantAccountTestConstants.INDIA_THREE_D_SECURE_MERCHANT_ACCOUNT_ID).
            authenticationInsight(Boolean.TRUE).
            authenticationInsightOptions().
                amount(new BigDecimal("2500")).
                done();

        Result<PaymentMethodNonce> result = gateway.paymentMethodNonce().create(createRequest);
        assertTrue(result.isSuccess());

        PaymentMethodNonce newNonce = result.getTarget();
        assertNotNull(newNonce);
        assertNotNull(newNonce.getNonce());
        assertNotNull(newNonce.getAuthenticationInsight());
        assertEquals("rbi", newNonce.getAuthenticationInsight().getRegulationEnvironment());
        assertEquals("sca_required", newNonce.getAuthenticationInsight().getScaIndicator());
    }

    @Test
    public void createReturnsAuthenticationInsightWithAnotherScaIndicator() {
        PaymentMethodNonceRequest createRequest = new PaymentMethodNonceRequest().
            paymentMethodToken("india_visa_credit").
            merchantAccountId(MerchantAccountTestConstants.INDIA_THREE_D_SECURE_MERCHANT_ACCOUNT_ID).
            authenticationInsight(Boolean.TRUE);

        Result<PaymentMethodNonce> result = gateway.paymentMethodNonce().create(createRequest);
        assertTrue(result.isSuccess());

        PaymentMethodNonce newNonce = result.getTarget();
        assertNotNull(newNonce);
        assertNotNull(newNonce.getNonce());
        assertNotNull(newNonce.getAuthenticationInsight());
        assertEquals("rbi", newNonce.getAuthenticationInsight().getRegulationEnvironment());
        assertNotEquals("sca_required", newNonce.getAuthenticationInsight().getScaIndicator());
        assertNotNull(newNonce.getAuthenticationInsight().getScaIndicator());
    }

    @Test
    public void createRaisesIfNotFound() {
        try {
            gateway.paymentMethodNonce().create("not-a-token");
            fail("Should throw NotFoundException");
        } catch (NotFoundException e) {
        }
    }

    @Test
    public void findCreditCardNonceReturnsValidValues() {
        String nonceString = "fake-valid-nonce";
        PaymentMethodNonce nonce = gateway.paymentMethodNonce().find(nonceString);
        assertNotNull(nonce);
        assertEquals(nonceString, nonce.getNonce());
        assertEquals(false, nonce.isConsumed());
        assertEquals(false, nonce.isDefault());
        assertNotNull(nonce.getDetails());
        assertEquals("401288", nonce.getDetails().getBin());
        assertEquals("81", nonce.getDetails().getLastTwo());
        assertEquals("Visa", nonce.getDetails().getCardType());
    }

    @Test
    public void findVenmoAccountNonceReturnsValidValues() {
        String nonceString = "fake-venmo-account-nonce";
        PaymentMethodNonce nonce = gateway.paymentMethodNonce().find(nonceString);
        assertNotNull(nonce);
        assertEquals(nonceString, nonce.getNonce());
        assertEquals(false, nonce.isConsumed());
        assertEquals(false, nonce.isDefault());
        assertNotNull(nonce.getDetails());
        assertEquals("99", nonce.getDetails().getLastTwo());
        assertEquals("venmojoe", nonce.getDetails().getUsername());
        assertEquals("Venmo-Joe-1", nonce.getDetails().getVenmoUserId());
    }

    @Test
    public void findApplePayCardNonceReturnsValidValues() {
        String nonceString = "fake-apple-pay-visa-nonce";
        PaymentMethodNonce nonce = gateway.paymentMethodNonce().find(nonceString);
        assertNotNull(nonce);
        assertEquals(nonceString, nonce.getNonce());
        assertEquals(false, nonce.isConsumed());
        assertEquals(false, nonce.isDefault());
        assertNotNull(nonce.getDetails());
        assertEquals("Visa", nonce.getDetails().getCardType());
        assertEquals("Visa Apple Pay Cardholder", nonce.getDetails().getCardholderName());
        assertEquals("Visa 8886", nonce.getDetails().getPaymentInstrumentName());
        assertEquals("81", nonce.getDetails().getDpanLastTwo());
    }

    @Test
    public void findPayPalAccountNonceReturnsValidValues() {
        String nonceString = "fake-paypal-billing-agreement-nonce";
        PaymentMethodNonce nonce = gateway.paymentMethodNonce().find(nonceString);
        assertNotNull(nonce);
        assertEquals(nonceString, nonce.getNonce());
        assertEquals(false, nonce.isConsumed());
        assertEquals(false, nonce.isDefault());
        assertNotNull(nonce.getDetails());
        assertEquals("jane.doe@paypal.com", nonce.getDetails().getEmail());
    }

    @Test
    public void findPayPalAccountNonceWithValidPayerInfoValues() {
        String nonceString = "fake-paypal-one-time-nonce";
        PaymentMethodNonce nonce = gateway.paymentMethodNonce().find(nonceString);
        assertNotNull(nonce);
        assertEquals(nonceString, nonce.getNonce());
        assertEquals(false, nonce.isConsumed());
        assertEquals(false, nonce.isDefault());
        assertNotNull(nonce.getDetails().getPayerInfo());

        PaymentMethodNonceDetailsPayerInfo payerInfo = nonce.getDetails().getPayerInfo();
        assertNotNull(payerInfo.getEmail());
        assertNotNull(payerInfo.getPayerId());
        assertNotNull(payerInfo.getBillingAddress().getRecipientName());
        assertNotNull(payerInfo.getBillingAddress().getStreetAddress());
        assertNotNull(payerInfo.getBillingAddress().getLocality());
        assertNotNull(payerInfo.getBillingAddress().getRegion());
        assertNotNull(payerInfo.getBillingAddress().getPostalCode());
        assertNotNull(payerInfo.getBillingAddress().getCountryCodeAlpha2());
        assertNotNull(payerInfo.getShippingAddress().getRecipientName());
        assertNotNull(payerInfo.getShippingAddress().getStreetAddress());
        assertNotNull(payerInfo.getShippingAddress().getLocality());
        assertNotNull(payerInfo.getShippingAddress().getRegion());
        assertNotNull(payerInfo.getShippingAddress().getPostalCode());
        assertNotNull(payerInfo.getShippingAddress().getCountryCodeAlpha2());
    }

    @Test
    public void findCreditCardNonceReturnsValidBinDataPrepaidValue() {
        String nonceString = "fake-valid-prepaid-nonce";
        PaymentMethodNonce nonce = gateway.paymentMethodNonce().find(nonceString);
        assertNotNull(nonce);
        assertEquals(nonceString, nonce.getNonce());
        assertEquals(Prepaid.YES, nonce.getBinData().getPrepaid());
    }

    @Test
    public void findCreditCardNonceReturnsValidBinDataCommercialValue() {
        String nonceString = "fake-valid-commercial-nonce";
        PaymentMethodNonce nonce = gateway.paymentMethodNonce().find(nonceString);
        assertNotNull(nonce);
        assertEquals(nonceString, nonce.getNonce());
        assertEquals(Commercial.YES, nonce.getBinData().getCommercial());
    }

    @Test
    public void findCreditCardNonceReturnsValidBinDataDurbinRegulatedValue() {
        String nonceString = "fake-valid-durbin-regulated-nonce";
        PaymentMethodNonce nonce = gateway.paymentMethodNonce().find(nonceString);
        assertNotNull(nonce);
        assertEquals(nonceString, nonce.getNonce());
        assertEquals(DurbinRegulated.YES, nonce.getBinData().getDurbinRegulated());
    }

    @Test
    public void findCreditCardNonceReturnsValidBinDataHealthcareValue() {
        String nonceString = "fake-valid-healthcare-nonce";
        PaymentMethodNonce nonce = gateway.paymentMethodNonce().find(nonceString);
        assertNotNull(nonce);
        assertEquals(nonceString, nonce.getNonce());
        assertEquals(Healthcare.YES, nonce.getBinData().getHealthcare());
        assertEquals("J3", nonce.getBinData().getProductId());
    }

    @Test
    public void findCreditCardNonceReturnsValidBinDataDebitValue() {
        String nonceString = "fake-valid-debit-nonce";
        PaymentMethodNonce nonce = gateway.paymentMethodNonce().find(nonceString);
        assertNotNull(nonce);
        assertEquals(nonceString, nonce.getNonce());
        assertEquals(Debit.YES, nonce.getBinData().getDebit());
    }

    @Test
    public void findCreditCardNonceReturnsValidBinDataPayrollValue() {
        String nonceString = "fake-valid-payroll-nonce";
        PaymentMethodNonce nonce = gateway.paymentMethodNonce().find(nonceString);
        assertNotNull(nonce);
        assertEquals(nonceString, nonce.getNonce());
        assertEquals(Payroll.YES, nonce.getBinData().getPayroll());
        assertEquals("MSA", nonce.getBinData().getProductId());
    }

    @Test
    public void findCreditCardNonceReturnsValidBinDataCountryOfIssuanceValue() {
        String nonceString = "fake-valid-country-of-issuance-cad-nonce";
        PaymentMethodNonce nonce = gateway.paymentMethodNonce().find(nonceString);
        assertNotNull(nonce);
        assertEquals(nonceString, nonce.getNonce());
        assertEquals("CAN", nonce.getBinData().getCountryOfIssuance());
    }

    @Test
    public void findCreditCardNonceReturnsValidBinDataIssuingBankValue() {
        String nonceString = "fake-valid-issuing-bank-network-only-nonce";
        PaymentMethodNonce nonce = gateway.paymentMethodNonce().find(nonceString);
        assertNotNull(nonce);
        assertEquals(nonceString, nonce.getNonce());
        assertEquals("NETWORK ONLY", nonce.getBinData().getIssuingBank());
    }

    @Test
    public void findCreditCardNonceReturnsValidBinDataUnknownValues() {
        String nonceString = "fake-valid-unknown-indicators-nonce";
        PaymentMethodNonce nonce = gateway.paymentMethodNonce().find(nonceString);
        assertNotNull(nonce);
        assertEquals(nonceString, nonce.getNonce());
        assertEquals(Commercial.UNKNOWN, nonce.getBinData().getCommercial());
        assertEquals(DurbinRegulated.UNKNOWN, nonce.getBinData().getDurbinRegulated());
        assertEquals(Debit.UNKNOWN, nonce.getBinData().getDebit());
        assertEquals(Healthcare.UNKNOWN, nonce.getBinData().getHealthcare());
        assertEquals(Payroll.UNKNOWN, nonce.getBinData().getPayroll());
        assertEquals(Prepaid.UNKNOWN, nonce.getBinData().getPrepaid());
        assertEquals("Unknown", nonce.getBinData().getProductId());
        assertEquals("Unknown", nonce.getBinData().getCountryOfIssuance());
        assertEquals("Unknown", nonce.getBinData().getIssuingBank());
    }

    @Test
    public void findReturnsPaymentMethodNonceWith3DSDetails() {
        String nonceString = "fake-three-d-secure-visa-full-authentication-nonce";
        PaymentMethodNonce foundNonce = gateway.paymentMethodNonce().find(nonceString);
        ThreeDSecureInfo info = foundNonce.getThreeDSecureInfo();

        assertEquals(nonceString, foundNonce.getNonce());
        assertTrue(info.isLiabilityShifted());
        assertTrue(info.isLiabilityShiftPossible());
        assertNotNull(info.getStatus());
        assertEquals("cavv_value", info.getCAVV());
        assertEquals("xid_value", info.getXID());
        assertEquals("05", info.getECIFlag());
        assertEquals("1.0.2", info.getThreeDSecureVersion());
        assertEquals((String)null, info.getDsTransactionId());
        assertTrue(info.getThreeDSecureLookupInfo() instanceof ThreeDSecureLookupInfo);
        assertTrue(info.getThreeDSecureAuthenticateInfo() instanceof ThreeDSecureAuthenticateInfo);
}

    @Test
    public void findReturnsNull3DSDetailsIfNotPresent() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        Customer customer = customerResult.getTarget();
        String nonce = TestHelper.generateUnlockedNonce(gateway, null, SandboxValues.CreditCardNumber.VISA.number);

        PaymentMethodNonce foundNonce = gateway.paymentMethodNonce().find(nonce);

        assertNull(foundNonce.getThreeDSecureInfo());
    }

    @Test
    public void findReturnsNullAuthenticationInsight() {
        String nonceString = "fake-valid-nonce";
        PaymentMethodNonce nonce = gateway.paymentMethodNonce().find(nonceString);
        assertNotNull(nonce);
        assertEquals(nonceString, nonce.getNonce());
        assertNull(nonce.getAuthenticationInsight());
    }

    @Test
    public void findRaisesIfNotFound() {
        try {
            gateway.paymentMethodNonce().find("not-a-nonce");
            fail("Should throw NotFoundException");
        } catch (NotFoundException e) {
        }
    }
}
