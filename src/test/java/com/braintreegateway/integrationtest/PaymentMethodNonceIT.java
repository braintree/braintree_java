package com.braintreegateway.integrationtest;

import com.braintreegateway.*;
import com.braintreegateway.CreditCard.Commercial;
import com.braintreegateway.CreditCard.DurbinRegulated;
import com.braintreegateway.CreditCard.Debit;
import com.braintreegateway.CreditCard.Healthcare;
import com.braintreegateway.CreditCard.Payroll;
import com.braintreegateway.CreditCard.Prepaid;
import com.braintreegateway.testhelpers.TestHelper;
import com.braintreegateway.exceptions.NotFoundException;
import org.junit.Test;

import static org.junit.Assert.*;

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
        CreditCardRequest creditCardRequest = new CreditCardRequest().
            number(SandboxValues.CreditCardNumber.VISA.number).
            expirationMonth("12").
            expirationYear("2020");

        String nonce = TestHelper.generateThreeDSecureNonce(gateway, creditCardRequest);

        PaymentMethodNonce foundNonce = gateway.paymentMethodNonce().find(nonce);

        assertEquals(nonce, foundNonce.getNonce());
        assertTrue(foundNonce.getThreeDSecureInfo().isLiabilityShifted());
        assertTrue(foundNonce.getThreeDSecureInfo().isLiabilityShiftPossible());
        assertNotNull(foundNonce.getThreeDSecureInfo().getStatus());
        assertNotNull(foundNonce.getThreeDSecureInfo().getCAVV());
        assertNotNull(foundNonce.getThreeDSecureInfo().getXID());
        assertNotNull(foundNonce.getThreeDSecureInfo().getECIFlag());
        assertNotNull(foundNonce.getThreeDSecureInfo().getThreeDSecureVersion());
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
    public void findRaisesIfNotFound() {
        try {
            gateway.paymentMethodNonce().find("not-a-nonce");
            fail("Should throw NotFoundException");
        } catch (NotFoundException e) {
        }
    }
}
