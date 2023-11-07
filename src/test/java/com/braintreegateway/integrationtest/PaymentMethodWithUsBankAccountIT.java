package com.braintreegateway.integrationtest;

import com.braintreegateway.testhelpers.MerchantAccountTestConstants;

import java.util.regex.Pattern;
import com.braintreegateway.*;
import com.braintreegateway.testhelpers.TestHelper;
import com.braintreegateway.test.Nonce;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PaymentMethodWithUsBankAccountIT extends IntegrationTest {
    @Test
    public void createUsBankAccountFromNonce() {
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

        PaymentMethod paymentMethod = result.getTarget();
        assertNotNull(paymentMethod.getToken());

        UsBankAccount usBankAccount = (UsBankAccount) paymentMethod;
        assertEquals("021000021", usBankAccount.getRoutingNumber());
        assertEquals("1234", usBankAccount.getLast4());
        assertEquals("checking", usBankAccount.getAccountType());
        assertEquals("Dan Schulman", usBankAccount.getAccountHolderName());
        assertTrue(Pattern.compile(".*CHASE.*").matcher(usBankAccount.getBankName()).matches());
    }

    @Test
    public void doesNotCreateUsBankAccountFromInvalidNonce() {
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();

        String nonce = TestHelper.generateInvalidUsBankAccountNonce();
        PaymentMethodRequest request = new PaymentMethodRequest()
            .customerId(customer.getId())
            .paymentMethodNonce(nonce)
            .options()
                .verificationMerchantAccountId(MerchantAccountTestConstants.US_BANK_MERCHANT_ACCOUNT)
            .done();

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);

        assertFalse(result.isSuccess());
        assertEquals(
                ValidationErrorCode.PAYMENT_METHOD_PAYMENT_METHOD_NONCE_UNKNOWN,
                result.getErrors().forObject("payment_method").onField("payment_method_nonce").get(0).getCode()
        );
    }

    @Test
    public void updatesBankAccountWithVerification() {
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
        PaymentMethodRequest createRequest = new PaymentMethodRequest()
            .customerId(customer.getId())
            .paymentMethodNonce(nonce)
            .options()
                .verificationMerchantAccountId(MerchantAccountTestConstants.ANOTHER_US_BANK_MERCHANT_ACCOUNT)
            .done();

        Result<? extends PaymentMethod> createResult = gateway.paymentMethod().create(createRequest);
        assertTrue(createResult.isSuccess());

        UsBankAccount createdUsBankAccount = (UsBankAccount) createResult.getTarget();

        assertEquals(0, createdUsBankAccount.getVerifications().size());

        PaymentMethodRequest updateRequest = new PaymentMethodRequest()
            .customerId(customer.getId())
            .options()
                .verificationMerchantAccountId(MerchantAccountTestConstants.ANOTHER_US_BANK_MERCHANT_ACCOUNT)
                .usBankAccountVerificationMethod(UsBankAccountVerification.VerificationMethod.INDEPENDENT_CHECK)
            .done();

        Result<? extends PaymentMethod> updateResult = gateway.paymentMethod().update(
            createdUsBankAccount.getToken(),
            updateRequest
        );

        UsBankAccount updatedUsBankAccount = (UsBankAccount) updateResult.getTarget();

        assertEquals(1, updatedUsBankAccount.getVerifications().size());

        UsBankAccountVerification verification = updatedUsBankAccount.getVerifications().get(0);

        assertEquals(
            UsBankAccountVerification.VerificationMethod.INDEPENDENT_CHECK,
            verification.getVerificationMethod()
        );

        assertEquals(UsBankAccountVerification.Status.VERIFIED, verification.getStatus());
    }

    @Test
    public void createsMicroTransfersVerification() {
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

        assertEquals(1, usBankAccount.getVerifications().size());

        UsBankAccountVerification verification = usBankAccount.getVerifications().get(0);

        assertEquals(UsBankAccountVerification.VerificationMethod.MICRO_TRANSFERS, verification.getVerificationMethod());
        assertEquals(UsBankAccountVerification.Status.PENDING, verification.getStatus());
    }

    @Test
    public void createsNetworkCheckVerification() {
        BraintreeGateway gateway = new BraintreeGateway(
            Environment.DEVELOPMENT,
            "integration2_merchant_id",
            "integration2_public_key",
            "integration2_private_key"
        );

        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();
        String nonce = Nonce.UsBankAccount;
        PaymentMethodRequest request = new PaymentMethodRequest()
            .customerId(customer.getId())
            .paymentMethodNonce(nonce)
            .options()
                .verificationMerchantAccountId(MerchantAccountTestConstants.ANOTHER_US_BANK_MERCHANT_ACCOUNT)
                .usBankAccountVerificationMethod(UsBankAccountVerification.VerificationMethod.NETWORK_CHECK)
            .done();

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);
        assertTrue(result.isSuccess());

        UsBankAccount usBankAccount = (UsBankAccount) result.getTarget();

        assertEquals("0000", usBankAccount.getLast4());
        assertEquals("checking", usBankAccount.getAccountType());
        assertEquals("Wells Fargo", usBankAccount.getBankName());
        assertEquals("Dan Schulman", usBankAccount.getAccountHolderName());
        assertEquals(1, usBankAccount.getVerifications().size());

        UsBankAccountVerification verification = usBankAccount.getVerifications().get(0);

        assertEquals(UsBankAccountVerification.VerificationMethod.NETWORK_CHECK, verification.getVerificationMethod());
        assertEquals("1000", verification.getProcessorResponseCode());
        assertEquals(UsBankAccountVerification.Status.VERIFIED, verification.getStatus());
    }

    @Test
    public void createsNetworkCheckVerificationWithAddOns() {
        BraintreeGateway gateway = new BraintreeGateway(
            Environment.DEVELOPMENT,
            "integration2_merchant_id",
            "integration2_public_key",
            "integration2_private_key"
        );

        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();

        String nonce = Nonce.UsBankAccount;
        PaymentMethodRequest request = new PaymentMethodRequest()
            .customerId(customer.getId())
            .paymentMethodNonce(nonce)
            .options()
                .verificationMerchantAccountId(MerchantAccountTestConstants.ANOTHER_US_BANK_MERCHANT_ACCOUNT)
                .usBankAccountVerificationMethod(UsBankAccountVerification.VerificationMethod.NETWORK_CHECK)
                .verificationAddOns(UsBankAccountVerification.VerificationAddOns.CUSTOMER_VERIFICATION)
            .done();

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);
        assertTrue(result.isSuccess());

        UsBankAccount usBankAccount = (UsBankAccount) result.getTarget();

        assertEquals("0000", usBankAccount.getLast4());
        assertEquals("checking", usBankAccount.getAccountType());
        assertEquals("Wells Fargo", usBankAccount.getBankName());
        assertEquals("Dan Schulman", usBankAccount.getAccountHolderName());
        assertEquals(1, usBankAccount.getVerifications().size());

        UsBankAccountVerification verification = usBankAccount.getVerifications().get(0);

        assertEquals(UsBankAccountVerification.VerificationMethod.NETWORK_CHECK, verification.getVerificationMethod());
        assertEquals("1000", verification.getProcessorResponseCode());
        assertEquals(UsBankAccountVerification.Status.VERIFIED, verification.getStatus());
    }

    @Test
    public void returnsAdditionalProcessorResponseForNetworkCheck() {
        BraintreeGateway gateway = new BraintreeGateway(
            Environment.DEVELOPMENT,
            "integration2_merchant_id",
            "integration2_public_key",
            "integration2_private_key"
        );

        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();

        String nonce = TestHelper.generateValidUsBankAccountNonce(gateway, "1000000005");
        PaymentMethodRequest request = new PaymentMethodRequest()
            .customerId(customer.getId())
            .paymentMethodNonce(nonce)
            .options()
                .verificationMerchantAccountId(MerchantAccountTestConstants.ANOTHER_US_BANK_MERCHANT_ACCOUNT)
                .usBankAccountVerificationMethod(UsBankAccountVerification.VerificationMethod.NETWORK_CHECK)
            .done();

        Result<? extends PaymentMethod> result = gateway.paymentMethod().create(request);
        assertTrue(result.isSuccess());

        UsBankAccount usBankAccount = (UsBankAccount) result.getTarget();

        assertEquals("0005", usBankAccount.getLast4());
        assertEquals("checking", usBankAccount.getAccountType());
        assertEquals("JPMORGAN CHASE", usBankAccount.getBankName());
        assertEquals("Dan Schulman", usBankAccount.getAccountHolderName());
        assertEquals(1, usBankAccount.getVerifications().size());

        UsBankAccountVerification verification = usBankAccount.getVerifications().get(0);

        assertEquals(UsBankAccountVerification.VerificationMethod.NETWORK_CHECK, verification.getVerificationMethod());
        assertEquals("2061", verification.getProcessorResponseCode());
        assertEquals("Invalid routing number", verification.getAdditionalProcessorResponse());
        assertEquals(UsBankAccountVerification.Status.PROCESSOR_DECLINED, verification.getStatus());
    }
}
