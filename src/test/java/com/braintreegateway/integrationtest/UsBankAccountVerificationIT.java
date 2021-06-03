package com.braintreegateway.integrationtest;

import com.braintreegateway.*;
import com.braintreegateway.testhelpers.TestHelper;

import com.braintreegateway.testhelpers.MerchantAccountTestConstants;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UsBankAccountVerificationIT extends IntegrationTestNew {

    @Test
    public void findById() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());

        Customer customer = customerResult.getTarget();

        String nonce = TestHelper.generateValidUsBankAccountNonce(gateway);
        PaymentMethodRequest request = new PaymentMethodRequest()
            .customerId(customer.getId())
            .paymentMethodNonce(nonce)
            .options()
                .verificationMerchantAccountId(MerchantAccountTestConstants.US_BANK_MERCHANT_ACCOUNT)
            .done();

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);

        assertTrue(result.isSuccess());

        UsBankAccount usBankAccount = (UsBankAccount) result.getTarget();

        assertNotNull(usBankAccount.getVerifications());
        assertEquals(1, usBankAccount.getVerifications().size());

        UsBankAccountVerification createdVerification = usBankAccount.getVerifications().get(0);
        UsBankAccountVerification foundVerification = gateway.usBankAccountVerification().find(createdVerification.getId());

        assertEquals(createdVerification.getId(), foundVerification.getId());
    }

    @Test
    public void searchOnTextFields() {
        Result<Customer> customerResult = gateway.customer().create(
            new CustomerRequest().email("dan.schulman@example.com")
        );
        assertTrue(customerResult.isSuccess());

        Customer customer = customerResult.getTarget();

        String nonce = TestHelper.generateValidUsBankAccountNonce(gateway);
        PaymentMethodRequest request = new PaymentMethodRequest()
            .customerId(customer.getId())
            .paymentMethodNonce(nonce)
            .options()
                .verificationMerchantAccountId(MerchantAccountTestConstants.US_BANK_MERCHANT_ACCOUNT)
            .done();

        Result<? extends PaymentMethod> paymentMethodResult = gateway.paymentMethod().create(request);
        assertTrue(paymentMethodResult.isSuccess());

        UsBankAccount usBankAccount = (UsBankAccount) paymentMethodResult.getTarget();
        UsBankAccountVerification createdVerification = usBankAccount.getVerifications().get(0);

        UsBankAccountVerificationSearchRequest searchRequest = new UsBankAccountVerificationSearchRequest()
            .customerId().is(customerResult.getTarget().getId())
            .accountHolderName().is("Dan Schulman")
            .customerEmail().is("dan.schulman@example.com")
            .paymentMethodToken()
            .is(usBankAccount.getToken());

        ResourceCollection<UsBankAccountVerification> collection = gateway.usBankAccountVerification().search(searchRequest);

        assertEquals(createdVerification.getId(), collection.getFirst().getId());
    }

    @Test
    public void searchOnMultipleValueFields() {
        Result<Customer> customerResult = gateway.customer().create(
            new CustomerRequest().email("dan.schulman@example.com")
        );
        assertTrue(customerResult.isSuccess());

        Customer customer = customerResult.getTarget();

        String nonce = TestHelper.generateValidUsBankAccountNonce(gateway);
        PaymentMethodRequest request = new PaymentMethodRequest()
            .customerId(customer.getId())
            .paymentMethodNonce(nonce)
            .options()
                .verificationMerchantAccountId(MerchantAccountTestConstants.US_BANK_MERCHANT_ACCOUNT)
            .done();

        Result<? extends PaymentMethod> paymentMethodResult = gateway.paymentMethod().create(request);
        assertTrue(paymentMethodResult.isSuccess());

        UsBankAccount usBankAccount = (UsBankAccount) paymentMethodResult.getTarget();
        UsBankAccountVerification createdVerification = usBankAccount.getVerifications().get(0);

        UsBankAccountVerificationSearchRequest searchRequest = new UsBankAccountVerificationSearchRequest()
            .ids().in(createdVerification.getId())
            .status().in(UsBankAccountVerification.Status.VERIFIED)
            .verificationMethod().in(UsBankAccountVerification.VerificationMethod.INDEPENDENT_CHECK);

        ResourceCollection<UsBankAccountVerification> collection = gateway.usBankAccountVerification().search(searchRequest);

        assertEquals(createdVerification.getId(), collection.getFirst().getId());
    }

    @Test
    public void searchOnAccountNumber() {
        Result<Customer> customerResult = gateway.customer().create(
            new CustomerRequest().email("dan.schulman@example.com")
        );
        assertTrue(customerResult.isSuccess());

        Customer customer = customerResult.getTarget();

        String nonce = TestHelper.generateValidUsBankAccountNonce(gateway);
        PaymentMethodRequest request = new PaymentMethodRequest()
            .customerId(customer.getId())
            .paymentMethodNonce(nonce)
            .options()
                .verificationMerchantAccountId(MerchantAccountTestConstants.US_BANK_MERCHANT_ACCOUNT)
            .done();

        Result<? extends PaymentMethod> paymentMethodResult = gateway.paymentMethod().create(request);
        assertTrue(paymentMethodResult.isSuccess());

        UsBankAccount usBankAccount = (UsBankAccount) paymentMethodResult.getTarget();
        UsBankAccountVerification createdVerification = usBankAccount.getVerifications().get(0);

        UsBankAccountVerificationSearchRequest searchRequest = new UsBankAccountVerificationSearchRequest()
            .accountNumber().endsWith(usBankAccount.getLast4());

        ResourceCollection<UsBankAccountVerification> collection = gateway.usBankAccountVerification().search(searchRequest);

        assertEquals(createdVerification.getId(), collection.getFirst().getId());
    }

    @Test
    public void successfullyConfirmMicroTransferAmountsSettled() {
        BraintreeGateway gateway = new BraintreeGateway(
            Environment.DEVELOPMENT,
            "integration2_merchant_id",
            "integration2_public_key",
            "integration2_private_key"
        );

        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());

        Customer customer = customerResult.getTarget();

        String nonce = TestHelper.generateValidUsBankAccountNonce(gateway, "1000000000");
        PaymentMethodRequest request = new PaymentMethodRequest()
            .customerId(customer.getId())
            .paymentMethodNonce(nonce)
            .options()
                .verificationMerchantAccountId(MerchantAccountTestConstants.ANOTHER_US_BANK_MERCHANT_ACCOUNT)
                .usBankAccountVerificationMethod(UsBankAccountVerification.VerificationMethod.MICRO_TRANSFERS)
            .done();

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);

        assertTrue(result.isSuccess());

        UsBankAccount usBankAccount = (UsBankAccount) result.getTarget();

        assertNotNull(usBankAccount.getVerifications());
        assertEquals(1, usBankAccount.getVerifications().size());
        assertFalse(usBankAccount.isVerified());

        UsBankAccountVerification verification = usBankAccount.getVerifications().get(0);
        assertEquals(UsBankAccountVerification.VerificationMethod.MICRO_TRANSFERS, verification.getVerificationMethod());

        ArrayList<Integer> depositAmounts = new ArrayList<Integer>();
        depositAmounts.add(17);
        depositAmounts.add(29);

        UsBankAccountVerificationConfirmRequest confirmRequest = new UsBankAccountVerificationConfirmRequest()
            .depositAmounts(depositAmounts);

        Result<UsBankAccountVerification> confirmResult = gateway.usBankAccountVerification().confirmMicroTransferAmounts(verification.getId(), confirmRequest);
        assertTrue(confirmResult.isSuccess());

        verification = confirmResult.getTarget();
        assertEquals(UsBankAccountVerification.Status.VERIFIED, verification.getStatus());

        usBankAccount = gateway.usBankAccount().find(verification.getUsBankAccount().getToken());
        assertTrue(usBankAccount.isVerified());
    }

    @Test
    public void successfullyConfirmMicroTransferAmountsUnsettled() {
        BraintreeGateway gateway = new BraintreeGateway(
            Environment.DEVELOPMENT,
            "integration2_merchant_id",
            "integration2_public_key",
            "integration2_private_key"
        );

        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());

        Customer customer = customerResult.getTarget();

        String nonce = TestHelper.generateValidUsBankAccountNonce(gateway, "1000000001");
        PaymentMethodRequest request = new PaymentMethodRequest()
            .customerId(customer.getId())
            .paymentMethodNonce(nonce)
            .options()
                .verificationMerchantAccountId(MerchantAccountTestConstants.ANOTHER_US_BANK_MERCHANT_ACCOUNT)
                .usBankAccountVerificationMethod(UsBankAccountVerification.VerificationMethod.MICRO_TRANSFERS)
            .done();

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);

        assertTrue(result.isSuccess());

        UsBankAccount usBankAccount = (UsBankAccount) result.getTarget();

        assertNotNull(usBankAccount.getVerifications());
        assertEquals(1, usBankAccount.getVerifications().size());
        assertFalse(usBankAccount.isVerified());

        UsBankAccountVerification verification = usBankAccount.getVerifications().get(0);
        assertEquals(UsBankAccountVerification.VerificationMethod.MICRO_TRANSFERS, verification.getVerificationMethod());

        ArrayList<Integer> depositAmounts = new ArrayList<Integer>();
        depositAmounts.add(17);
        depositAmounts.add(29);

        UsBankAccountVerificationConfirmRequest confirmRequest = new UsBankAccountVerificationConfirmRequest()
            .depositAmounts(depositAmounts);

        Result<UsBankAccountVerification> confirmResult = gateway.usBankAccountVerification().confirmMicroTransferAmounts(verification.getId(), confirmRequest);
        assertTrue(confirmResult.isSuccess());

        verification = confirmResult.getTarget();
        assertEquals(UsBankAccountVerification.Status.PENDING, verification.getStatus());

        usBankAccount = gateway.usBankAccount().find(verification.getUsBankAccount().getToken());
        assertFalse(usBankAccount.isVerified());
    }

    @Test
    public void attemptConfirmMicroTransferAmounts() {
        BraintreeGateway gateway = new BraintreeGateway(
            Environment.DEVELOPMENT,
            "integration2_merchant_id",
            "integration2_public_key",
            "integration2_private_key"
        );

        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());

        Customer customer = customerResult.getTarget();

        String nonce = TestHelper.generateValidUsBankAccountNonce(gateway);
        PaymentMethodRequest request = new PaymentMethodRequest()
            .customerId(customer.getId())
            .paymentMethodNonce(nonce)
            .options()
                .verificationMerchantAccountId(MerchantAccountTestConstants.ANOTHER_US_BANK_MERCHANT_ACCOUNT)
                .usBankAccountVerificationMethod(UsBankAccountVerification.VerificationMethod.MICRO_TRANSFERS)
            .done();

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);

        assertTrue(result.isSuccess());

        UsBankAccount usBankAccount = (UsBankAccount) result.getTarget();

        assertNotNull(usBankAccount.getVerifications());
        assertEquals(1, usBankAccount.getVerifications().size());
        assertFalse(usBankAccount.isVerified());

        UsBankAccountVerification verification = usBankAccount.getVerifications().get(0);
        assertEquals(UsBankAccountVerification.VerificationMethod.MICRO_TRANSFERS, verification.getVerificationMethod());

        ArrayList<Integer> depositAmounts = new ArrayList<Integer>();
        depositAmounts.add(1);
        depositAmounts.add(1);

        UsBankAccountVerificationConfirmRequest confirmRequest = new UsBankAccountVerificationConfirmRequest()
            .depositAmounts(depositAmounts);

        Result<UsBankAccountVerification> confirmResult = gateway.usBankAccountVerification().confirmMicroTransferAmounts(verification.getId(), confirmRequest);

        assertFalse(confirmResult.isSuccess());

        assertEquals(ValidationErrorCode.US_BANK_ACCOUNT_VERIFICATION_AMOUNTS_DO_NOT_MATCH,
            confirmResult.getErrors().forObject("usBankAccountVerification").onField("base").get(0).getCode());

        verification = confirmResult.getUsBankAccountVerification();

        assertEquals(UsBankAccountVerification.Status.PENDING, verification.getStatus());
    }

    @Test
    public void exceedMicroTransfersConfirmationAttemptThreshold() {
        BraintreeGateway gateway = new BraintreeGateway(
            Environment.DEVELOPMENT,
            "integration2_merchant_id",
            "integration2_public_key",
            "integration2_private_key"
        );

        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());

        Customer customer = customerResult.getTarget();

        String nonce = TestHelper.generateValidUsBankAccountNonce(gateway);
        PaymentMethodRequest request = new PaymentMethodRequest()
            .customerId(customer.getId())
            .paymentMethodNonce(nonce)
            .options()
                .verificationMerchantAccountId(MerchantAccountTestConstants.ANOTHER_US_BANK_MERCHANT_ACCOUNT)
                .usBankAccountVerificationMethod(UsBankAccountVerification.VerificationMethod.MICRO_TRANSFERS)
            .done();

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);

        assertTrue(result.isSuccess());

        UsBankAccount usBankAccount = (UsBankAccount) result.getTarget();

        assertNotNull(usBankAccount.getVerifications());
        assertEquals(1, usBankAccount.getVerifications().size());
        assertFalse(usBankAccount.isVerified());

        UsBankAccountVerification verification = usBankAccount.getVerifications().get(0);
        assertEquals(UsBankAccountVerification.VerificationMethod.MICRO_TRANSFERS, verification.getVerificationMethod());

        ArrayList<Integer> depositAmounts = new ArrayList<Integer>();
        depositAmounts.add(1);
        depositAmounts.add(1);

        UsBankAccountVerificationConfirmRequest confirmRequest = new UsBankAccountVerificationConfirmRequest()
            .depositAmounts(depositAmounts);

        for (int i = 0; i < 4; i++) {
            Result<UsBankAccountVerification> confirmResult = gateway.usBankAccountVerification().confirmMicroTransferAmounts(verification.getId(), confirmRequest);

            assertFalse(confirmResult.isSuccess());

            assertEquals(ValidationErrorCode.US_BANK_ACCOUNT_VERIFICATION_AMOUNTS_DO_NOT_MATCH,
                confirmResult.getErrors().forObject("usBankAccountVerification").onField("base").get(0).getCode());

            verification = confirmResult.getUsBankAccountVerification();

            assertEquals(UsBankAccountVerification.Status.PENDING, verification.getStatus());
        }

        Result<UsBankAccountVerification> confirmResult = gateway.usBankAccountVerification().confirmMicroTransferAmounts(verification.getId(), confirmRequest);

        assertFalse(confirmResult.isSuccess());

        assertEquals(ValidationErrorCode.US_BANK_ACCOUNT_VERIFICATION_TOO_MANY_CONFIRMATION_ATTEMPTS,
            confirmResult.getErrors().forObject("usBankAccountVerification").onField("base").get(0).getCode());

        verification = confirmResult.getUsBankAccountVerification();

        assertEquals(UsBankAccountVerification.Status.GATEWAY_REJECTED, verification.getStatus());
    }
}
