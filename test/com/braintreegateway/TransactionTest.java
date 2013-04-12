package com.braintreegateway;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.braintreegateway.testhelpers.TestHelper;
import com.braintreegateway.testhelpers.CalendarTestUtils;
import com.braintreegateway.util.NodeWrapperFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.braintreegateway.test.CreditCardNumbers;
import com.braintreegateway.test.VenmoSdk;
import com.braintreegateway.SandboxValues.CreditCardNumber;
import com.braintreegateway.SandboxValues.TransactionAmount;
import com.braintreegateway.exceptions.ForgedQueryStringException;
import com.braintreegateway.exceptions.NotFoundException;

import static org.junit.Assert.*;

public class TransactionTest {

    private BraintreeGateway gateway;

    @Before
    public void createGateway() {
        this.gateway = new BraintreeGateway(Environment.DEVELOPMENT, "integration_merchant_id", "integration_public_key", "integration_private_key");
    }

    @SuppressWarnings("deprecation")
    @Test
    public void transparentRedirectURLForCreate() {
        Assert.assertEquals(gateway.baseMerchantURL() + "/transactions/all/create_via_transparent_redirect_request",
                gateway.transaction().transparentRedirectURLForCreate());
    }

    @Test
    public void trData() {
        String trData = gateway.trData(new TransactionRequest(), "http://example.com");
        TestHelper.assertValidTrData(gateway.getConfiguration(), trData);
    }

    @Test
    public void saleTrData() {
        String trData = gateway.transaction().saleTrData(new TransactionRequest(), "http://example.com");
        TestHelper.assertValidTrData(gateway.getConfiguration(), trData);
        Assert.assertTrue(trData.contains("sale"));
    }

    @Test
    public void creditTrData() {
        String trData = gateway.transaction().creditTrData(new TransactionRequest(), "http://example.com");
        TestHelper.assertValidTrData(gateway.getConfiguration(), trData);
        Assert.assertTrue(trData.contains("credit"));
    }

    @SuppressWarnings("deprecation")
    @Test
    public void createViaTransparentRedirect() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            options().
                storeInVault(true).
                done();

        TransactionRequest trParams = new TransactionRequest().
            type(Transaction.Type.SALE);

        String queryString = TestHelper.simulateFormPostForTR(gateway, trParams, request, gateway.transaction().transparentRedirectURLForCreate());
        Result<Transaction> result = gateway.transaction().confirmTransparentRedirect(queryString);
        Assert.assertTrue(result.isSuccess());
    }

    @SuppressWarnings("deprecation")
    @Test(expected = ForgedQueryStringException.class)
    public void createViaTransparentRedirectThrowsWhenQueryStringHasBeenTamperedWith() {
        String queryString = TestHelper.simulateFormPostForTR(gateway, new TransactionRequest(), new TransactionRequest(), gateway.transaction().transparentRedirectURLForCreate());
        gateway.transaction().confirmTransparentRedirect(queryString + "this make it invalid");
    }

    @Test
    public void cloneTransaction() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            orderId("123").
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            customer().
                firstName("Dan").
                done().
            billingAddress().
                firstName("Carl").
                done().
            shippingAddress().
                firstName("Andrew").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        Assert.assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        TransactionCloneRequest cloneRequest = new TransactionCloneRequest().
            amount(new BigDecimal("123.45")).
            channel("MyShoppingCartProvider").
            options().
              submitForSettlement(false).
              done();
        Result<Transaction> cloneResult = gateway.transaction().cloneTransaction(transaction.getId(), cloneRequest);
        Assert.assertTrue(cloneResult.isSuccess());
        Transaction cloneTransaction = cloneResult.getTarget();

        Assert.assertEquals(new BigDecimal("123.45"), cloneTransaction.getAmount());
        Assert.assertEquals("MyShoppingCartProvider", cloneTransaction.getChannel());
        Assert.assertEquals("123", cloneTransaction.getOrderId());
        Assert.assertEquals("411111******1111", cloneTransaction.getCreditCard().getMaskedNumber());
        Assert.assertEquals("Dan", cloneTransaction.getCustomer().getFirstName());
        Assert.assertEquals("Carl", cloneTransaction.getBillingAddress().getFirstName());
        Assert.assertEquals("Andrew", cloneTransaction.getShippingAddress().getFirstName());
    }

    @Test
    public void cloneTransactionAndSubmitForSettlement() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            orderId("123").
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        Assert.assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        TransactionCloneRequest cloneRequest = new TransactionCloneRequest().amount(new BigDecimal("123.45")).options().submitForSettlement(true).done();
        Result<Transaction> cloneResult = gateway.transaction().cloneTransaction(transaction.getId(), cloneRequest);
        Assert.assertTrue(cloneResult.isSuccess());
        Transaction cloneTransaction = cloneResult.getTarget();

        Assert.assertEquals(Transaction.Status.SUBMITTED_FOR_SETTLEMENT, cloneTransaction.getStatus());
    }

    @Test
    public void cloneTransactionWithValidationErrors() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done();

        Result<Transaction> result = gateway.transaction().credit(request);
        Assert.assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        TransactionCloneRequest cloneRequest = new TransactionCloneRequest().amount(new BigDecimal("123.45"));
        Result<Transaction> cloneResult = gateway.transaction().cloneTransaction(transaction.getId(), cloneRequest);
        Assert.assertFalse(cloneResult.isSuccess());


        Assert.assertEquals(ValidationErrorCode.TRANSACTION_CANNOT_CLONE_CREDIT,
                cloneResult.getErrors().forObject("transaction").onField("base").get(0).getCode());
    }

    @Test
    public void sale() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        Assert.assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        Assert.assertEquals(new BigDecimal("1000.00"), transaction.getAmount());
        Assert.assertEquals("USD", transaction.getCurrencyIsoCode());
        Assert.assertNotNull(transaction.getProcessorAuthorizationCode());
        Assert.assertEquals(Transaction.Type.SALE, transaction.getType());
        Assert.assertEquals(Transaction.Status.AUTHORIZED, transaction.getStatus());
        Assert.assertEquals(Calendar.getInstance().get(Calendar.YEAR), transaction.getCreatedAt().get(Calendar.YEAR));
        Assert.assertEquals(Calendar.getInstance().get(Calendar.YEAR), transaction.getUpdatedAt().get(Calendar.YEAR));

        CreditCard creditCard = transaction.getCreditCard();
        Assert.assertEquals("411111", creditCard.getBin());
        Assert.assertEquals("1111", creditCard.getLast4());
        Assert.assertEquals("05", creditCard.getExpirationMonth());
        Assert.assertEquals("2009", creditCard.getExpirationYear());
        Assert.assertEquals("05/2009", creditCard.getExpirationDate());
    }

    @Test
    public void saleWithCardTypeIndicators() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumbers.CardTypeIndicators.Prepaid.getValue()).
                expirationDate("05/2012").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        Assert.assertTrue(result.isSuccess());

        CreditCard card = result.getTarget().getCreditCard();

        Assert.assertEquals(CreditCard.Prepaid.YES, card.getPrepaid());
        Assert.assertEquals(CreditCard.Healthcare.UNKNOWN, card.getHealthcare());
        Assert.assertEquals(CreditCard.Payroll.UNKNOWN, card.getPayroll());
        Assert.assertEquals(CreditCard.Debit.UNKNOWN, card.getDebit());
        Assert.assertEquals(CreditCard.DurbinRegulated.UNKNOWN, card.getDurbinRegulated());
        Assert.assertEquals(CreditCard.Commercial.UNKNOWN, card.getCommercial());
        Assert.assertEquals("Unknown", card.getCountryOfIssuance());
        Assert.assertEquals("Unknown", card.getIssuingBank());
    }

    @Test
    public void saleWithAllAttributes() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            channel("MyShoppingCartProvider").
            orderId("123").
            creditCard().
                cardholderName("The Cardholder").
                number(CreditCardNumber.VISA.number).
                cvv("321").
                expirationDate("05/2009").
                done().
            customer().
                firstName("Dan").
                lastName("Smith").
                company("Braintree Payment Solutions").
                email("dan@example.com").
                phone("419-555-1234").
                fax("419-555-1235").
                website("http://braintreepayments.com").
                done().
            billingAddress().
                firstName("Carl").
                lastName("Jones").
                company("Braintree").
                streetAddress("123 E Main St").
                extendedAddress("Suite 403").
                locality("Chicago").
                region("IL").
                postalCode("60622").
                countryName("United States of America").
                countryCodeAlpha2("US").
                countryCodeAlpha3("USA").
                countryCodeNumeric("840").
                done().
            shippingAddress().
                firstName("Andrew").
                lastName("Mason").
                company("Braintree Shipping").
                streetAddress("456 W Main St").
                extendedAddress("Apt 2F").
                locality("Bartlett").
                region("MA").
                postalCode("60103").
                countryName("Mexico").
                countryCodeAlpha2("MX").
                countryCodeAlpha3("MEX").
                countryCodeNumeric("484").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        Assert.assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        Assert.assertEquals(new BigDecimal("1000.00"), transaction.getAmount());
        Assert.assertEquals(Transaction.Status.AUTHORIZED, transaction.getStatus());
        Assert.assertEquals("MyShoppingCartProvider", transaction.getChannel());
        Assert.assertEquals("123", transaction.getOrderId());
        Assert.assertNull(transaction.getVaultCreditCard(gateway));
        Assert.assertNull(transaction.getVaultCustomer(gateway));
        Assert.assertNull(transaction.getAvsErrorResponseCode());
        Assert.assertEquals("M", transaction.getAvsPostalCodeResponseCode());
        Assert.assertEquals("M", transaction.getAvsStreetAddressResponseCode());
        Assert.assertEquals("M", transaction.getCvvResponseCode());
        Assert.assertFalse(transaction.isTaxExempt());
        Assert.assertNull(transaction.getVaultCreditCard(gateway));
        CreditCard creditCard = transaction.getCreditCard();
        Assert.assertEquals("411111", creditCard.getBin());
        Assert.assertEquals("1111", creditCard.getLast4());
        Assert.assertEquals("05", creditCard.getExpirationMonth());
        Assert.assertEquals("2009", creditCard.getExpirationYear());
        Assert.assertEquals("05/2009", creditCard.getExpirationDate());
        Assert.assertEquals("The Cardholder", creditCard.getCardholderName());

        Assert.assertNull(transaction.getVaultCustomer(gateway));
        Customer customer = transaction.getCustomer();
        Assert.assertEquals("Dan", customer.getFirstName());
        Assert.assertEquals("Smith", customer.getLastName());
        Assert.assertEquals("Braintree Payment Solutions", customer.getCompany());
        Assert.assertEquals("dan@example.com", customer.getEmail());
        Assert.assertEquals("419-555-1234", customer.getPhone());
        Assert.assertEquals("419-555-1235", customer.getFax());
        Assert.assertEquals("http://braintreepayments.com", customer.getWebsite());

        Assert.assertNull(transaction.getVaultBillingAddress(gateway));
        Address billing = transaction.getBillingAddress();
        Assert.assertEquals("Carl", billing.getFirstName());
        Assert.assertEquals("Jones", billing.getLastName());
        Assert.assertEquals("Braintree", billing.getCompany());
        Assert.assertEquals("123 E Main St", billing.getStreetAddress());
        Assert.assertEquals("Suite 403", billing.getExtendedAddress());
        Assert.assertEquals("Chicago", billing.getLocality());
        Assert.assertEquals("IL", billing.getRegion());
        Assert.assertEquals("60622", billing.getPostalCode());
        Assert.assertEquals("United States of America", billing.getCountryName());
        Assert.assertEquals("US", billing.getCountryCodeAlpha2());
        Assert.assertEquals("USA", billing.getCountryCodeAlpha3());
        Assert.assertEquals("840", billing.getCountryCodeNumeric());

        Assert.assertNull(transaction.getVaultShippingAddress(gateway));
        Address shipping = transaction.getShippingAddress();
        Assert.assertEquals("Andrew", shipping.getFirstName());
        Assert.assertEquals("Mason", shipping.getLastName());
        Assert.assertEquals("Braintree Shipping", shipping.getCompany());
        Assert.assertEquals("456 W Main St", shipping.getStreetAddress());
        Assert.assertEquals("Apt 2F", shipping.getExtendedAddress());
        Assert.assertEquals("Bartlett", shipping.getLocality());
        Assert.assertEquals("MA", shipping.getRegion());
        Assert.assertEquals("60103", shipping.getPostalCode());
        Assert.assertEquals("Mexico", shipping.getCountryName());
        Assert.assertEquals("MX", shipping.getCountryCodeAlpha2());
        Assert.assertEquals("MEX", shipping.getCountryCodeAlpha3());
        Assert.assertEquals("484", shipping.getCountryCodeNumeric());
    }

    @Test
    public void saleWithSpecifyingMerchantAccountId() {
        TransactionRequest request = new TransactionRequest().
            merchantAccountId(MerchantAccount.NON_DEFAULT_MERCHANT_ACCOUNT_ID).
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        Assert.assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        Assert.assertEquals(MerchantAccount.NON_DEFAULT_MERCHANT_ACCOUNT_ID, transaction.getMerchantAccountId());
    }

    @Test
    public void saleWithoutSpecifyingMerchantAccountIdFallsBackToDefault() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        Assert.assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        Assert.assertEquals(MerchantAccount.DEFAULT_MERCHANT_ACCOUNT_ID, transaction.getMerchantAccountId());
    }

    @Test
    public void saleWithStoreInVaultAndSpecifyingToken() {
        String customerId = String.valueOf(new Random().nextInt());
        String paymentToken = String.valueOf(new Random().nextInt());

        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                token(paymentToken).
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            customer().
                id(customerId).
                firstName("Jane").
                done().
            options().
                storeInVault(true).
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        Assert.assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        CreditCard creditCard = transaction.getCreditCard();
        Assert.assertEquals(paymentToken, creditCard.getToken());
        Assert.assertEquals("05/2009", transaction.getVaultCreditCard(gateway).getExpirationDate());

        Customer customer = transaction.getCustomer();
        Assert.assertEquals(customerId, customer.getId());
        Assert.assertEquals("Jane", transaction.getVaultCustomer(gateway).getFirstName());

    }

    @Test
    public void saleWithStoreInVaultWithoutSpecifyingToken() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            customer().
                firstName("Jane").
                done().
            options().
                storeInVault(true).
            done();

        Result<Transaction> result = gateway.transaction().sale(request);
        Assert.assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        CreditCard creditCard = transaction.getCreditCard();
        Assert.assertNotNull(creditCard.getToken());
        Assert.assertEquals("05/2009", transaction.getVaultCreditCard(gateway).getExpirationDate());

        Customer customer = transaction.getCustomer();
        Assert.assertNotNull(customer.getId());
        Assert.assertEquals("Jane", transaction.getVaultCustomer(gateway).getFirstName());
    }

    @Test
    public void saleWithStoreInVaultOnSuccessWhenTransactionSucceeds() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            customer().
                firstName("Jane").
                done().
            options().
                storeInVaultOnSuccess(true).
            done();

        Result<Transaction> result = gateway.transaction().sale(request);
        Assert.assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        CreditCard creditCard = transaction.getCreditCard();
        Assert.assertNotNull(creditCard.getToken());
        Assert.assertEquals("05/2009", transaction.getVaultCreditCard(gateway).getExpirationDate());

        Customer customer = transaction.getCustomer();
        Assert.assertNotNull(customer.getId());
        Assert.assertEquals("Jane", transaction.getVaultCustomer(gateway).getFirstName());
    }

    @Test
    public void saleWithStoreInVaultOnSuccessWhenTransactionFails() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.DECLINE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            customer().
                firstName("Jane").
                done().
            options().
                storeInVaultOnSuccess(true).
            done();

        Result<Transaction> result = gateway.transaction().sale(request);
        Assert.assertFalse(result.isSuccess());
        Transaction transaction = result.getTransaction();

        CreditCard creditCard = transaction.getCreditCard();
        Assert.assertNull(creditCard.getToken());
        Assert.assertNull(transaction.getVaultCreditCard(gateway));

        Customer customer = transaction.getCustomer();
        Assert.assertNull(customer.getId());
        Assert.assertNull(transaction.getVaultCustomer(gateway));
    }

    @Test
    public void saleWithStoreInVaultForBillingAndShipping() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            billingAddress().
                firstName("Carl").
                done().
            shippingAddress().
                firstName("Andrew").
                done().
            options().
                storeInVault(true).
                addBillingAddressToPaymentMethod(true).
                storeShippingAddressInVault(true).
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        Assert.assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        CreditCard creditCard = transaction.getVaultCreditCard(gateway);
        Assert.assertEquals("Carl", creditCard.getBillingAddress().getFirstName());
        Assert.assertEquals("Carl", transaction.getVaultBillingAddress(gateway).getFirstName());
        Assert.assertEquals("Andrew", transaction.getVaultShippingAddress(gateway).getFirstName());

        Customer customer = transaction.getVaultCustomer(gateway);
        Assert.assertEquals(2, customer.getAddresses().size());

        List<Address> addresses = new ArrayList<Address>(customer.getAddresses());
        Collections.sort(addresses, new Comparator<Address>() {
            public int compare(Address left, Address right) {
                return left.getFirstName().compareTo(right.getFirstName());
            }
        });
        Assert.assertEquals("Andrew", addresses.get(0).getFirstName());
        Assert.assertEquals("Carl", addresses.get(1).getFirstName());

        Assert.assertNotNull(transaction.getBillingAddress().getId());
        Assert.assertNotNull(transaction.getShippingAddress().getId());
    }

    @Test
     public void saleWithVaultCustomerAndNewCreditCard() {
        Customer customer = gateway.customer().create(new CustomerRequest().
                firstName("Michael").
                lastName("Angelo").
                company("Some Company")
         ).getTarget();

        TransactionRequest request = new TransactionRequest().
            amount(SandboxValues.TransactionAmount.AUTHORIZE.amount).
            customerId(customer.getId()).
            creditCard().
                    cardholderName("Bob the Builder").
                    number(SandboxValues.CreditCardNumber.VISA.number).
                    expirationDate("05/2009").
                    done();

        Result<Transaction> result = gateway.transaction().sale(request);
        Assert.assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        Assert.assertEquals("Bob the Builder", transaction.getCreditCard().getCardholderName());
        Assert.assertNull(transaction.getVaultCreditCard(gateway));
     }

     @Test
     public void saleWithVaultCustomerAndNewCreditCardStoresInVault() {
         Customer customer = gateway.customer().create(new CustomerRequest().
                 firstName("Michael").
                 lastName("Angelo").
                 company("Some Company")
          ).getTarget();

         TransactionRequest request = new TransactionRequest().
             amount(SandboxValues.TransactionAmount.AUTHORIZE.amount).
             customerId(customer.getId()).
             creditCard().
                     cardholderName("Bob the Builder").
                     number(SandboxValues.CreditCardNumber.VISA.number).
                     expirationDate("05/2009").
                     done().
              options().
                  storeInVault(true).
                  done();

         Result<Transaction> result = gateway.transaction().sale(request);
         Assert.assertTrue(result.isSuccess());
         Transaction transaction = result.getTarget();

         Assert.assertEquals("Bob the Builder", transaction.getCreditCard().getCardholderName());
         Assert.assertEquals("Bob the Builder", transaction.getVaultCreditCard(gateway).getCardholderName());
     }

     @Test
    public void saleDeclined() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.DECLINE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        Assert.assertFalse(result.isSuccess());
        Transaction transaction = result.getTransaction();

        Assert.assertEquals(new BigDecimal("2000.00"), transaction.getAmount());
        Assert.assertEquals(Transaction.Status.PROCESSOR_DECLINED, transaction.getStatus());
        Assert.assertEquals("2000", transaction.getProcessorResponseCode());
        Assert.assertNotNull(transaction.getProcessorResponseText());

        CreditCard creditCard = transaction.getCreditCard();
        Assert.assertEquals("411111", creditCard.getBin());
        Assert.assertEquals("1111", creditCard.getLast4());
        Assert.assertEquals("05", creditCard.getExpirationMonth());
        Assert.assertEquals("2009", creditCard.getExpirationYear());
        Assert.assertEquals("05/2009", creditCard.getExpirationDate());
    }

    @Test
    public void saleWithCustomFields() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            customField("storeMe", "custom value").
            customField("another_stored_field", "custom value2").
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done();
        Result<Transaction> result = gateway.transaction().sale(request);
        Assert.assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        Map<String, String> expected = new HashMap<String, String>();
        expected.put("store_me", "custom value");
        expected.put("another_stored_field", "custom value2");

        Assert.assertEquals(expected, transaction.getCustomFields());
    }

    @Test
    public void saleWithRecurringFlag(){
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            recurring(true).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done();
        Result<Transaction> result = gateway.transaction().sale(request);
        Assert.assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();
        Assert.assertTrue(transaction.getRecurring());
    }

    @Test
    public void saleWithValidationErrorsOnAddress() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.DECLINE.amount).
            customField("unkown_custom_field", "custom value").
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            billingAddress().
                countryName("No such country").
                countryCodeAlpha2("zz").
                countryCodeAlpha3("zzz").
                countryCodeNumeric("000").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        Assert.assertFalse(result.isSuccess());

        Assert.assertEquals(ValidationErrorCode.ADDRESS_COUNTRY_NAME_IS_NOT_ACCEPTED,
                result.getErrors().forObject("transaction").forObject("billing").onField("countryName").get(0).getCode());
        Assert.assertEquals(ValidationErrorCode.ADDRESS_COUNTRY_CODE_ALPHA2_IS_NOT_ACCEPTED,
                result.getErrors().forObject("transaction").forObject("billing").onField("countryCodeAlpha2").get(0).getCode());
        Assert.assertEquals(ValidationErrorCode.ADDRESS_COUNTRY_CODE_ALPHA3_IS_NOT_ACCEPTED,
            result.getErrors().forObject("transaction").forObject("billing").onField("countryCodeAlpha3").get(0).getCode());
        Assert.assertEquals(ValidationErrorCode.ADDRESS_COUNTRY_CODE_NUMERIC_IS_NOT_ACCEPTED,
            result.getErrors().forObject("transaction").forObject("billing").onField("countryCodeNumeric").get(0).getCode());
    }

    @Test
    public void saleWithUnregisteredCustomField() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.DECLINE.amount).
            customField("unkown_custom_field", "custom value").
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done();
        Result<Transaction> result = gateway.transaction().sale(request);
        Assert.assertFalse(result.isSuccess());

        Assert.assertEquals(ValidationErrorCode.TRANSACTION_CUSTOM_FIELD_IS_INVALID,
                result.getErrors().forObject("transaction").onField("customFields").get(0).getCode());
    }

    @Test
    public void saleWithMultipleValidationErrorsOnSameField() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            paymentMethodToken("foo").
            customerId("5").
            creditCard().
                number(CreditCardNumber.VISA.number).
                cvv("321").
                expirationDate("04/2009").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        Assert.assertFalse(result.isSuccess());
        List<ValidationError> errros = result.getErrors().forObject("transaction").onField("base");

        Assert.assertNull(result.getTransaction());
        Assert.assertNull(result.getCreditCardVerification());
        Assert.assertEquals(2, errros.size());

        List<ValidationErrorCode> validationErrorCodes = new ArrayList<ValidationErrorCode>();
        validationErrorCodes.add(errros.get(0).getCode());
        validationErrorCodes.add(errros.get(1).getCode());
        Assert.assertTrue(validationErrorCodes.contains(ValidationErrorCode.TRANSACTION_PAYMENT_METHOD_CONFLICT_WITH_VENMO_SDK));
        Assert.assertTrue(validationErrorCodes.contains(ValidationErrorCode.TRANSACTION_PAYMENT_METHOD_DOES_NOT_BELONG_TO_CUSTOMER));
    }

    @Test
    public void saleWithCustomerId() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest creditCardRequest = new CreditCardRequest().
            customerId(customer.getId()).
            cvv("123").
            number("5105105105105100").
            expirationDate("05/12");
        CreditCard creditCard = gateway.creditCard().create(creditCardRequest).getTarget();

        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            paymentMethodToken(creditCard.getToken());

        Result<Transaction> result = gateway.transaction().sale(request);
        Assert.assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        Assert.assertEquals(creditCard.getToken(), transaction.getCreditCard().getToken());
        Assert.assertEquals("510510", transaction.getCreditCard().getBin());
        Assert.assertEquals("05/2012", transaction.getCreditCard().getExpirationDate());
    }

    @Test
    public void saleWithPaymentMethodTokenAndCvv() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest creditCardRequest = new CreditCardRequest().
            customerId(customer.getId()).
            number("5105105105105100").
            expirationDate("05/12");
        CreditCard creditCard = gateway.creditCard().create(creditCardRequest).getTarget();

        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            paymentMethodToken(creditCard.getToken()).
            creditCard().cvv("301").done();

        Result<Transaction> result = gateway.transaction().sale(request);
        Assert.assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        Assert.assertEquals(creditCard.getToken(), transaction.getCreditCard().getToken());
        Assert.assertEquals("510510", transaction.getCreditCard().getBin());
        Assert.assertEquals("05/2012", transaction.getCreditCard().getExpirationDate());
        Assert.assertEquals("S", transaction.getCvvResponseCode());
    }


    @Test
    public void saleUsesShippingAddressFromVault() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();

        gateway.creditCard().create(new CreditCardRequest().
            customerId(customer.getId()).
            cvv("123").
            number("5105105105105100").
            expirationDate("05/12")).getTarget();

        Address shippingAddress = gateway.address().create(customer.getId(),
                new AddressRequest().firstName("Carl")).getTarget();

        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            customerId(customer.getId()).
            shippingAddressId(shippingAddress.getId());

        Result<Transaction> result = gateway.transaction().sale(request);
        Assert.assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        Assert.assertEquals(shippingAddress.getId(), transaction.getShippingAddress().getId());
        Assert.assertEquals("Carl", transaction.getShippingAddress().getFirstName());
    }

    @Test
    public void saleWithValidationError() {
        TransactionRequest request = new TransactionRequest().
            amount(null).
            creditCard().
                expirationMonth("05").
                expirationYear("2010").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        Assert.assertFalse(result.isSuccess());
        Assert.assertNull(result.getTarget());
        Assert.assertEquals(ValidationErrorCode.TRANSACTION_AMOUNT_IS_REQUIRED, result.getErrors().forObject("transaction").onField("amount").get(0).getCode());

        Map<String, String> parameters = result.getParameters();
        Assert.assertEquals(null, parameters.get("transaction[amount]"));
        Assert.assertEquals("05", parameters.get("transaction[credit_card][expiration_month]"));
        Assert.assertEquals("2010", parameters.get("transaction[credit_card][expiration_year]"));
    }

    @Test
    public void saleWithSubmitForSettlement() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            options().
                submitForSettlement(true).
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        Assert.assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        Assert.assertEquals(Transaction.Status.SUBMITTED_FOR_SETTLEMENT, transaction.getStatus());
    }

    @Test
    public void saleWithDescriptor() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            descriptor().
                name("123*123456789012345678").
                phone("3334445555").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        Assert.assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        Assert.assertEquals("123*123456789012345678", transaction.getDescriptor().getName());
        Assert.assertEquals("3334445555", transaction.getDescriptor().getPhone());
    }

    @Test
    public void saleWithDescriptorValidation() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            descriptor().
                name("badcompanyname12*badproduct12").
                phone("%bad4445555").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        Assert.assertFalse(result.isSuccess());

        Assert.assertEquals(ValidationErrorCode.DESCRIPTOR_NAME_FORMAT_IS_INVALID,
            result.getErrors().forObject("transaction").forObject("descriptor").onField("name").get(0).getCode());

        Assert.assertEquals(ValidationErrorCode.DESCRIPTOR_PHONE_FORMAT_IS_INVALID,
            result.getErrors().forObject("transaction").forObject("descriptor").onField("phone").get(0).getCode());
    }

    @Test
    public void saleWithLevel2() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            taxAmount(new BigDecimal("10.00")).
            taxExempt(true).
            purchaseOrderNumber("12345");

        Result<Transaction> result = gateway.transaction().sale(request);
        Assert.assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        Assert.assertEquals(new BigDecimal("10.00"), transaction.getTaxAmount());
        Assert.assertTrue(transaction.isTaxExempt());
        Assert.assertEquals("12345", transaction.getPurchaseOrderNumber());
    }

    @Test
    public void saleWithTooLongPurchaseOrderNumber() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            purchaseOrderNumber("aaaaaaaaaaaaaaaaaa");

        Result<Transaction> result = gateway.transaction().sale(request);
        Assert.assertFalse(result.isSuccess());

        Assert.assertEquals(ValidationErrorCode.TRANSACTION_PURCHASE_ORDER_NUMBER_IS_TOO_LONG,
            result.getErrors().forObject("transaction").onField("purchaseOrderNumber").get(0).getCode());
    }

    @Test
    public void saleWithInvalidPurchaseOrderNumber() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            purchaseOrderNumber("\u00c3\u009f\u00c3\u00a5\u00e2\u0088\u0082");

        Result<Transaction> result = gateway.transaction().sale(request);
        Assert.assertFalse(result.isSuccess());

        Assert.assertEquals(ValidationErrorCode.TRANSACTION_PURCHASE_ORDER_NUMBER_IS_INVALID,
            result.getErrors().forObject("transaction").onField("purchaseOrderNumber").get(0).getCode());
    }

    @Test
    public void saleWithVenmoSdkPaymentMethodCode() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            venmoSdkPaymentMethodCode(VenmoSdk.PaymentMethodCode.Visa.code);

        Result<Transaction> result = gateway.transaction().sale(request);
        Assert.assertTrue(result.isSuccess());
        Assert.assertEquals("411111", result.getTarget().getCreditCard().getBin());
    }

    @Test
    public void createTransactionFromTransparentRedirectWithAddress() {
        TransactionRequest request = new TransactionRequest();

        TransactionRequest trParams = new TransactionRequest().
        amount(TransactionAmount.AUTHORIZE.amount).
            type(Transaction.Type.SALE).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            billingAddress().
                countryName("United States of America").
                countryCodeAlpha2("US").
                countryCodeAlpha3("USA").
                countryCodeNumeric("840").
                done();

        String queryString = TestHelper.simulateFormPostForTR(gateway, trParams, request, gateway.transparentRedirect().url());
        Result<Transaction> result = gateway.transparentRedirect().confirmTransaction(queryString);

        Assert.assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        Assert.assertEquals("United States of America", transaction.getBillingAddress().getCountryName());
        Assert.assertEquals("US", transaction.getBillingAddress().getCountryCodeAlpha2());
        Assert.assertEquals("USA", transaction.getBillingAddress().getCountryCodeAlpha3());
        Assert.assertEquals("840", transaction.getBillingAddress().getCountryCodeNumeric());
    }

    @Test
    public void createTransactionFromTransparentRedirectWithAddressWithErrors() {
        TransactionRequest request = new TransactionRequest();

        TransactionRequest trParams = new TransactionRequest().
        amount(TransactionAmount.AUTHORIZE.amount).
            type(Transaction.Type.SALE).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            billingAddress().
                countryName("Foo bar!").
                countryCodeAlpha2("zz").
                countryCodeAlpha3("zzz").
                countryCodeNumeric("000").
                done();

        String queryString = TestHelper.simulateFormPostForTR(gateway, trParams, request, gateway.transparentRedirect().url());
        Result<Transaction> result = gateway.transparentRedirect().confirmTransaction(queryString);

        Assert.assertFalse(result.isSuccess());

        Assert.assertEquals(ValidationErrorCode.ADDRESS_COUNTRY_NAME_IS_NOT_ACCEPTED,
                result.getErrors().forObject("transaction").forObject("billing").onField("countryName").get(0).getCode());
        Assert.assertEquals(ValidationErrorCode.ADDRESS_COUNTRY_CODE_ALPHA2_IS_NOT_ACCEPTED,
                result.getErrors().forObject("transaction").forObject("billing").onField("countryCodeAlpha2").get(0).getCode());
        Assert.assertEquals(ValidationErrorCode.ADDRESS_COUNTRY_CODE_ALPHA3_IS_NOT_ACCEPTED,
            result.getErrors().forObject("transaction").forObject("billing").onField("countryCodeAlpha3").get(0).getCode());
        Assert.assertEquals(ValidationErrorCode.ADDRESS_COUNTRY_CODE_NUMERIC_IS_NOT_ACCEPTED,
            result.getErrors().forObject("transaction").forObject("billing").onField("countryCodeNumeric").get(0).getCode());
    }

    @Test
    public void credit() {
        TransactionRequest request = new TransactionRequest().
            amount(SandboxValues.TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(SandboxValues.CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done();

        Result<Transaction> result = gateway.transaction().credit(request);
        Assert.assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        Assert.assertEquals(new BigDecimal("1000.00"), transaction.getAmount());
        Assert.assertEquals(Transaction.Type.CREDIT, transaction.getType());
        Assert.assertEquals(Transaction.Status.SUBMITTED_FOR_SETTLEMENT, transaction.getStatus());

        CreditCard creditCard = transaction.getCreditCard();
        Assert.assertEquals("411111", creditCard.getBin());
        Assert.assertEquals("1111", creditCard.getLast4());
        Assert.assertEquals("05", creditCard.getExpirationMonth());
        Assert.assertEquals("2009", creditCard.getExpirationYear());
        Assert.assertEquals("05/2009", creditCard.getExpirationDate());
    }

    @Test
    public void creditWithSpecifyingMerchantAccountId() {
        TransactionRequest request = new TransactionRequest().
            amount(SandboxValues.TransactionAmount.AUTHORIZE.amount).
            merchantAccountId(MerchantAccount.NON_DEFAULT_MERCHANT_ACCOUNT_ID).
            creditCard().
                number(SandboxValues.CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done();

        Result<Transaction> result = gateway.transaction().credit(request);
        Assert.assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        Assert.assertEquals(MerchantAccount.NON_DEFAULT_MERCHANT_ACCOUNT_ID, transaction.getMerchantAccountId());
    }

    @Test
    public void creditWithoutSpecifyingMerchantAccountIdFallsBackToDefault() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done();

        Result<Transaction> result = gateway.transaction().credit(request);
        Assert.assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        Assert.assertEquals(MerchantAccount.DEFAULT_MERCHANT_ACCOUNT_ID, transaction.getMerchantAccountId());
    }

    @Test
    public void creditWithCustomFields() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            customField("store_me", "custom value").
            customField("another_stored_field", "custom value2").
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done();
        Result<Transaction> result = gateway.transaction().credit(request);
        Assert.assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        Map<String, String> expected = new HashMap<String, String>();
        expected.put("store_me", "custom value");
        expected.put("another_stored_field", "custom value2");

        Assert.assertEquals(expected, transaction.getCustomFields());
    }

    @Test
    public void creditWithValidationError() {
        TransactionRequest request = new TransactionRequest().
            amount(null).
            creditCard().
                expirationMonth("05").
                expirationYear("2010").
                done();

        Result<Transaction> result = gateway.transaction().credit(request);
        Assert.assertFalse(result.isSuccess());
        Assert.assertNull(result.getTarget());
        Assert.assertEquals(ValidationErrorCode.TRANSACTION_AMOUNT_IS_REQUIRED, result.getErrors().forObject("transaction").onField("amount").get(0).getCode());

        Map<String, String> parameters = result.getParameters();
        Assert.assertEquals(null, parameters.get("transaction[amount]"));
        Assert.assertEquals("05", parameters.get("transaction[credit_card][expiration_month]"));
        Assert.assertEquals("2010", parameters.get("transaction[credit_card][expiration_year]"));
    }

    @Test
    public void find() {
        TransactionRequest request = new TransactionRequest().
                amount(TransactionAmount.AUTHORIZE.amount).
                creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2008").
                done();
        Transaction transaction = gateway.transaction().sale(request).getTarget();

        Transaction foundTransaction = gateway.transaction().find(transaction.getId());

        Assert.assertEquals(transaction.getId(), foundTransaction.getId());
        Assert.assertEquals(Transaction.Status.AUTHORIZED, foundTransaction.getStatus());
        Assert.assertEquals("05/2008", foundTransaction.getCreditCard().getExpirationDate());
    }

    @Test(expected = NotFoundException.class)
    public void findWithBadId() {
        gateway.transaction().find("badId");
    }

    @Test(expected = NotFoundException.class)
    public void findWithWhitespaceId() {
        gateway.transaction().find(" ");
    }

    @Test
    public void findWithDepositDetails() throws Exception {
        Calendar depositCalendar = CalendarTestUtils.date("2013-04-10");
        Calendar disbursedCalendar = CalendarTestUtils.dateTime("2013-04-09T00:00:00Z");

        Transaction foundTransaction = gateway.transaction().find("deposit_transaction");
        DepositDetail depositDetails = foundTransaction.getDepositDetails();

        assertEquals(true, foundTransaction.isDeposited());
        assertEquals(depositCalendar, depositDetails.getDepositDate());
        assertEquals(disbursedCalendar, depositDetails.getDisbursedAt());
        assertEquals("USD", depositDetails.getSettlementCurrencyIsoCode());
        assertEquals(false, depositDetails.isFundsHeld());
        assertEquals(new BigDecimal("1"), depositDetails.getSettlementCurrencyExchangeRate());
        assertEquals(new BigDecimal("100.00"), depositDetails.getSettlementAmount());
    }

    @Test
    public void voidVoidsTheTransaction() {
        TransactionRequest request = new TransactionRequest().
        amount(TransactionAmount.AUTHORIZE.amount).
        creditCard().
            number(CreditCardNumber.VISA.number).
            expirationDate("05/2008").
            done();
        Transaction transaction = gateway.transaction().sale(request).getTarget();
        Result<Transaction> result = gateway.transaction().voidTransaction(transaction.getId());
        Assert.assertTrue(result.isSuccess());

        Assert.assertEquals(transaction.getId(), result.getTarget().getId());
        Assert.assertEquals(Transaction.Status.VOIDED, result.getTarget().getStatus());
    }

    @Test(expected = NotFoundException.class)
    public void voidWithBadId() {
        gateway.transaction().voidTransaction("badId");
    }

    @Test
    public void voidWithBadStatus() {
        TransactionRequest request = new TransactionRequest().
        amount(TransactionAmount.AUTHORIZE.amount).
        creditCard().
            number(CreditCardNumber.VISA.number).
            expirationDate("05/2008").
            done();
        Transaction transaction = gateway.transaction().sale(request).getTarget();

        gateway.transaction().voidTransaction(transaction.getId());
        Result<Transaction> result = gateway.transaction().voidTransaction(transaction.getId());

        Assert.assertFalse(result.isSuccess());
        Assert.assertEquals(ValidationErrorCode.TRANSACTION_CANNOT_BE_VOIDED,
                result.getErrors().forObject("transaction").onField("base").get(0).getCode());
    }

    @Test
    public void statusHistoryReturnsCorrectStatusEvents() {
        TransactionRequest request = new TransactionRequest().
        amount(TransactionAmount.AUTHORIZE.amount).
        creditCard().
            number(CreditCardNumber.VISA.number).
            expirationDate("05/2008").
            done();
        Transaction transaction = gateway.transaction().sale(request).getTarget();

        Transaction settledTransaction = gateway.transaction().submitForSettlement(transaction.getId()).getTarget();

        Assert.assertEquals(2, settledTransaction.getStatusHistory().size());
        Assert.assertEquals(Transaction.Status.AUTHORIZED, settledTransaction.getStatusHistory().get(0).getStatus());
        Assert.assertEquals(Transaction.Status.SUBMITTED_FOR_SETTLEMENT, settledTransaction.getStatusHistory().get(1).getStatus());
    }

    @Test
    public void submitForSettlementWithoutAmount() {
        TransactionRequest request = new TransactionRequest().
        amount(TransactionAmount.AUTHORIZE.amount).
        creditCard().
            number(CreditCardNumber.VISA.number).
            expirationDate("05/2008").
            done();
        Transaction transaction = gateway.transaction().sale(request).getTarget();

        Result<Transaction> result = gateway.transaction().submitForSettlement(transaction.getId());

        Assert.assertTrue(result.isSuccess());
        Assert.assertEquals(Transaction.Status.SUBMITTED_FOR_SETTLEMENT, result.getTarget().getStatus());
        Assert.assertEquals(TransactionAmount.AUTHORIZE.amount, result.getTarget().getAmount());
    }

    @Test
    public void submitForSettlementWithAmount() {
        TransactionRequest request = new TransactionRequest().
        amount(TransactionAmount.AUTHORIZE.amount).
        creditCard().
            number(CreditCardNumber.VISA.number).
            expirationDate("05/2008").
            done();
        Transaction transaction = gateway.transaction().sale(request).getTarget();

        Result<Transaction> result = gateway.transaction().submitForSettlement(transaction.getId(), new BigDecimal("50.00"));

        Assert.assertTrue(result.isSuccess());
        Assert.assertEquals(Transaction.Status.SUBMITTED_FOR_SETTLEMENT, result.getTarget().getStatus());
        Assert.assertEquals(new BigDecimal("50.00"), result.getTarget().getAmount());
    }

    @Test
    public void submitForSettlementWithBadStatus() {
        TransactionRequest request = new TransactionRequest().
        amount(TransactionAmount.AUTHORIZE.amount).
        creditCard().
            number(CreditCardNumber.VISA.number).
            expirationDate("05/2008").
            done();
        Transaction transaction = gateway.transaction().sale(request).getTarget();

        gateway.transaction().submitForSettlement(transaction.getId());
        Result<Transaction> result = gateway.transaction().submitForSettlement(transaction.getId());

        Assert.assertFalse(result.isSuccess());
        Assert.assertEquals(ValidationErrorCode.TRANSACTION_CANNOT_SUBMIT_FOR_SETTLEMENT,
                result.getErrors().forObject("transaction").onField("base").get(0).getCode());
    }

    @Test(expected = NotFoundException.class)
    public void submitForSettlementWithBadId() {
        gateway.transaction().submitForSettlement("badId");
    }

    @Test
    public void searchOnAllTextFields()
    {
        String creditCardToken = String.valueOf(new Random().nextInt());
        String firstName = String.valueOf(new Random().nextInt());

        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("1000")).
            creditCard().
                number("4111111111111111").
                expirationDate("05/2012").
                cardholderName("Tom Smith").
                token(creditCardToken).
                done().
            billingAddress().
                company("Braintree").
                countryName("United States of America").
                extendedAddress("Suite 123").
                firstName(firstName).
                lastName("Smith").
                locality("Chicago").
                postalCode("12345").
                region("IL").
                streetAddress("123 Main St").
                done().
            customer().
                company("Braintree").
                email("smith@example.com").
                fax("5551231234").
                firstName("Tom").
                lastName("Smith").
                phone("5551231234").
                website("http://example.com").
                done().
            options().
                storeInVault(true).
                submitForSettlement(true).
                done().
            orderId("myorder").
            shippingAddress().
                company("Braintree P.S.").
                countryName("Mexico").
                extendedAddress("Apt 456").
                firstName("Thomas").
                lastName("Smithy").
                locality("Braintree").
                postalCode("54321").
                region("MA").
                streetAddress("456 Road").
                done();

        Transaction transaction = gateway.transaction().sale(request).getTarget();
        TestHelper.settle(gateway, transaction.getId());
        transaction = gateway.transaction().find(transaction.getId());

        TransactionSearchRequest searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            billingCompany().is("Braintree").
            billingCountryName().is("United States of America").
            billingExtendedAddress().is("Suite 123").
            billingFirstName().is(firstName).
            billingLastName().is("Smith").
            billingLocality().is("Chicago").
            billingPostalCode().is("12345").
            billingRegion().is("IL").
            billingStreetAddress().is("123 Main St").
            creditCardCardholderName().is("Tom Smith").
            creditCardExpirationDate().is("05/2012").
            creditCardNumber().is(CreditCardNumber.VISA.number).
            currency().is("USD").
            customerCompany().is("Braintree").
            customerEmail().is("smith@example.com").
            customerFax().is("5551231234").
            customerFirstName().is("Tom").
            customerId().is(transaction.getCustomer().getId()).
            customerLastName().is("Smith").
            customerPhone().is("5551231234").
            customerWebsite().is("http://example.com").
            orderId().is("myorder").
            paymentMethodToken().is(creditCardToken).
            processorAuthorizationCode().is(transaction.getProcessorAuthorizationCode()).
            settlementBatchId().is(transaction.getSettlementBatchId()).
            shippingCompany().is("Braintree P.S.").
            shippingCountryName().is("Mexico").
            shippingExtendedAddress().is("Apt 456").
            shippingFirstName().is("Thomas").
            shippingLastName().is("Smithy").
            shippingLocality().is("Braintree").
            shippingPostalCode().is("54321").
            shippingRegion().is("MA").
            shippingStreetAddress().is("456 Road");

        ResourceCollection<Transaction> collection = gateway.transaction().search(searchRequest);

        Assert.assertEquals(1, collection.getMaximumSize());
        Assert.assertEquals(transaction.getId(), collection.getFirst().getId());
    }

    @Test
    public void searchOnTextNodeOperators() {
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("1000")).
            creditCard().
                number("4111111111111111").
                expirationDate("05/2012").
                cardholderName("Tom Smith").
                done();
        Transaction transaction = gateway.transaction().sale(request).getTarget();

        TransactionSearchRequest searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            creditCardCardholderName().startsWith("Tom");

        ResourceCollection<Transaction> collection = gateway.transaction().search(searchRequest);
        Assert.assertEquals(1, collection.getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            creditCardCardholderName().endsWith("Smith");

        collection = gateway.transaction().search(searchRequest);
        Assert.assertEquals(1, collection.getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            creditCardCardholderName().contains("m Sm");

        collection = gateway.transaction().search(searchRequest);
        Assert.assertEquals(1, collection.getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            creditCardCardholderName().isNot("Tom Smith");

        collection = gateway.transaction().search(searchRequest);
        Assert.assertEquals(0, collection.getMaximumSize());
    }

    @Test
    public void searchOnNullValue() {
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("1000")).
            creditCard().
                number("4111111111111111").
                expirationDate("05/2012").
                cardholderName("Tom Smith").
                done();
        Transaction transaction = gateway.transaction().sale(request).getTarget();

        TransactionSearchRequest searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            creditCardCardholderName().is(null);

        ResourceCollection<Transaction> collection = gateway.transaction().search(searchRequest);
        Assert.assertEquals(1, collection.getMaximumSize());
    }

    @Test
    public void searchOnCreatedUsing() {
         TransactionRequest request = new TransactionRequest().
             amount(TransactionAmount.AUTHORIZE.amount).
             creditCard().
                 number(CreditCardNumber.VISA.number).
                 expirationDate("05/2010").
                 done();

         Transaction transaction = gateway.transaction().sale(request).getTarget();

         TransactionSearchRequest searchRequest = new TransactionSearchRequest().
             id().is(transaction.getId()).
             createdUsing().is(Transaction.CreatedUsing.FULL_INFORMATION);

         ResourceCollection<Transaction> collection = gateway.transaction().search(searchRequest);

         Assert.assertEquals(1, collection.getMaximumSize());

         searchRequest = new TransactionSearchRequest().
             id().is(transaction.getId()).
             createdUsing().in(Transaction.CreatedUsing.FULL_INFORMATION, Transaction.CreatedUsing.TOKEN);

         collection = gateway.transaction().search(searchRequest);

         Assert.assertEquals(1, collection.getMaximumSize());

         searchRequest = new TransactionSearchRequest().
             id().is(transaction.getId()).
             createdUsing().is(Transaction.CreatedUsing.TOKEN);

         collection = gateway.transaction().search(searchRequest);

         Assert.assertEquals(0, collection.getMaximumSize());
     }

    @Test
    public void searchOnCreditCardCustomerLocation() {
         TransactionRequest request = new TransactionRequest().
             amount(TransactionAmount.AUTHORIZE.amount).
             creditCard().
                 number(CreditCardNumber.VISA.number).
                 expirationDate("05/2010").
                 done();

         Transaction transaction = gateway.transaction().sale(request).getTarget();

         TransactionSearchRequest searchRequest = new TransactionSearchRequest().
             id().is(transaction.getId()).
             creditCardCustomerLocation().is(CreditCard.CustomerLocation.US);

         ResourceCollection<Transaction> collection = gateway.transaction().search(searchRequest);

         Assert.assertEquals(1, collection.getMaximumSize());

         searchRequest = new TransactionSearchRequest().
             id().is(transaction.getId()).
             creditCardCustomerLocation().in(CreditCard.CustomerLocation.US, CreditCard.CustomerLocation.INTERNATIONAL);

         collection = gateway.transaction().search(searchRequest);

         Assert.assertEquals(1, collection.getMaximumSize());

         searchRequest = new TransactionSearchRequest().
             id().is(transaction.getId()).
             creditCardCustomerLocation().is(CreditCard.CustomerLocation.INTERNATIONAL);

         collection = gateway.transaction().search(searchRequest);

         Assert.assertEquals(0, collection.getMaximumSize());
     }

    @Test
    public void searchOnMerchantAccountId() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2010").
                done();

        Transaction transaction = gateway.transaction().sale(request).getTarget();

        TransactionSearchRequest searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            merchantAccountId().is(transaction.getMerchantAccountId());

        ResourceCollection<Transaction> collection = gateway.transaction().search(searchRequest);

        Assert.assertEquals(1, collection.getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            merchantAccountId().in(transaction.getMerchantAccountId(), "badmerchantaccountid");

        collection = gateway.transaction().search(searchRequest);

        Assert.assertEquals(1, collection.getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            merchantAccountId().is("badmerchantaccountid");

        collection = gateway.transaction().search(searchRequest);

        Assert.assertEquals(0, collection.getMaximumSize());
    }

    @Test
    public void searchOnCreditCardType() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2010").
                done();

        Transaction transaction = gateway.transaction().sale(request).getTarget();

        TransactionSearchRequest searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            creditCardCardType().is(CreditCard.CardType.VISA);

        ResourceCollection<Transaction> collection = gateway.transaction().search(searchRequest);

        Assert.assertEquals(1, collection.getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            creditCardCardType().in(CreditCard.CardType.VISA, CreditCard.CardType.MASTER_CARD);

        collection = gateway.transaction().search(searchRequest);

        Assert.assertEquals(1, collection.getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            creditCardCardType().is(CreditCard.CardType.MASTER_CARD);

        collection = gateway.transaction().search(searchRequest);

        Assert.assertEquals(0, collection.getMaximumSize());
    }

    @Test
    public void searchOnStatus() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2010").
                done();

        Transaction transaction = gateway.transaction().sale(request).getTarget();

        TransactionSearchRequest searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            status().is(Transaction.Status.AUTHORIZED);

        ResourceCollection<Transaction> collection = gateway.transaction().search(searchRequest);

        Assert.assertEquals(1, collection.getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            status().in(Transaction.Status.AUTHORIZED, Transaction.Status.GATEWAY_REJECTED);

        collection = gateway.transaction().search(searchRequest);

        Assert.assertEquals(1, collection.getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            status().is(Transaction.Status.GATEWAY_REJECTED);

        collection = gateway.transaction().search(searchRequest);

        Assert.assertEquals(0, collection.getMaximumSize());
    }

    @Test
    public void searchOnAuthorizationExpiredStatus() {
        TransactionSearchRequest searchRequest = new TransactionSearchRequest().
            status().is(Transaction.Status.AUTHORIZATION_EXPIRED);
        ResourceCollection<Transaction> collection = gateway.transaction().search(searchRequest);

        Assert.assertTrue(collection.getMaximumSize() > 0);
        Assert.assertEquals(Transaction.Status.AUTHORIZATION_EXPIRED, collection.getFirst().getStatus());
    }

    @Test
    public void searchOnSource() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2010").
                done();

        Transaction transaction = gateway.transaction().sale(request).getTarget();

        TransactionSearchRequest searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            source().is(Transaction.Source.API);

        ResourceCollection<Transaction> collection = gateway.transaction().search(searchRequest);

        Assert.assertEquals(1, collection.getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            source().in(Transaction.Source.API, Transaction.Source.CONTROL_PANEL);

        collection = gateway.transaction().search(searchRequest);

        Assert.assertEquals(1, collection.getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            source().is(Transaction.Source.CONTROL_PANEL);

        collection = gateway.transaction().search(searchRequest);

        Assert.assertEquals(0, collection.getMaximumSize());
    }

    @Test
    public void searchOnType() {
        String name = String.valueOf(new Random().nextInt());

        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2010").
                cardholderName(name).
                done().
            options().
                submitForSettlement(true).
                done();

        Transaction creditTransaction = gateway.transaction().credit(request).getTarget();
        Transaction saleTransaction = gateway.transaction().sale(request).getTarget();
        TestHelper.settle(gateway, saleTransaction.getId());
        Transaction refundTransaction = gateway.transaction().refund(saleTransaction.getId()).getTarget();

        TransactionSearchRequest searchRequest = new TransactionSearchRequest().
            creditCardCardholderName().is(name).
            type().is(Transaction.Type.CREDIT);

        ResourceCollection<Transaction> collection = gateway.transaction().search(searchRequest);

        Assert.assertEquals(2, collection.getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            creditCardCardholderName().is(name).
            type().is(Transaction.Type.SALE);

        collection = gateway.transaction().search(searchRequest);

        Assert.assertEquals(1, collection.getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            creditCardCardholderName().is(name).
            type().is(Transaction.Type.CREDIT).
            refund().is(true);

        collection = gateway.transaction().search(searchRequest);

        Assert.assertEquals(1, collection.getMaximumSize());
        Assert.assertEquals(refundTransaction.getId(), collection.getFirst().getId());

        searchRequest = new TransactionSearchRequest().
            creditCardCardholderName().is(name).
            type().is(Transaction.Type.CREDIT).
            refund().is(false);

        collection = gateway.transaction().search(searchRequest);

        Assert.assertEquals(1, collection.getMaximumSize());
        Assert.assertEquals(creditTransaction.getId(), collection.getFirst().getId());
    }

    @Test
    public void searchOnAmount() {
        TransactionRequest request = new TransactionRequest().
        amount(new BigDecimal("1000")).
        creditCard().
            number(CreditCardNumber.VISA.number).
            expirationDate("05/2010").
            done();

         Transaction transaction = gateway.transaction().sale(request).getTarget();

         TransactionSearchRequest searchRequest = new TransactionSearchRequest().
             id().is(transaction.getId()).
             amount().between(new BigDecimal("500"), new BigDecimal("1500"));

         Assert.assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

         searchRequest = new TransactionSearchRequest().
             id().is(transaction.getId()).
             amount().greaterThanOrEqualTo(new BigDecimal("500"));

         Assert.assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

         searchRequest = new TransactionSearchRequest().
             id().is(transaction.getId()).
             amount().lessThanOrEqualTo(new BigDecimal("1500"));

         Assert.assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

         searchRequest = new TransactionSearchRequest().
             id().is(transaction.getId()).
             amount().between(new BigDecimal("1300"), new BigDecimal("1500"));

         Assert.assertEquals(0, gateway.transaction().search(searchRequest).getMaximumSize());
     }

    @Test
    public void searchOnDepositDate() throws ParseException {
        String transactionID = "deposit_transaction";

        Calendar depositTime = CalendarTestUtils.dateTime("2013-04-10T00:00:00Z");

        Calendar threeDaysEarlier = ((Calendar)depositTime.clone());
        threeDaysEarlier.add(Calendar.DAY_OF_MONTH, -3);

        Calendar oneDayEarlier = ((Calendar)depositTime.clone());
        oneDayEarlier.add(Calendar.DAY_OF_MONTH, -1);

        Calendar oneDayLater = ((Calendar)depositTime.clone());
        oneDayLater.add(Calendar.DAY_OF_MONTH, 1);

        TransactionSearchRequest searchRequest = new TransactionSearchRequest().
                id().is(transactionID).
                depositDate().between(oneDayEarlier, oneDayLater);

        Assert.assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
                id().is(transactionID).
                depositDate().greaterThanOrEqualTo(oneDayEarlier);

        Assert.assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
                id().is(transactionID).
                depositDate().lessThanOrEqualTo(oneDayLater);

        Assert.assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
                id().is(transactionID).
                depositDate().between(threeDaysEarlier, oneDayEarlier);

        Assert.assertEquals(0, gateway.transaction().search(searchRequest).getMaximumSize());
    }

    @Test
    public void searchOnDepositDateUsingLocalTime() throws ParseException {
        String transactionId = "deposit_transaction";

        Calendar oneDayEarlier = CalendarTestUtils.dateTime("2013-04-09T00:00:00Z", "CST");
        Calendar oneDayLater =   CalendarTestUtils.dateTime("2013-04-11T00:00:00Z", "CST");

        TransactionSearchRequest searchRequest = new TransactionSearchRequest().
                id().is(transactionId).
                depositDate().between(oneDayEarlier, oneDayLater);

        Assert.assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());
    }

    @Test
    public void searchOnCreatedAt() {
        TransactionRequest request = new TransactionRequest().
        amount(TransactionAmount.AUTHORIZE.amount).
        creditCard().
            number(CreditCardNumber.VISA.number).
            expirationDate("05/2010").
            done();

         Transaction transaction = gateway.transaction().sale(request).getTarget();

         Calendar createdAt = transaction.getCreatedAt();

         Calendar threeDaysEarlier = ((Calendar)createdAt.clone());
         threeDaysEarlier.add(Calendar.DAY_OF_MONTH, -3);

         Calendar oneDayEarlier = ((Calendar)createdAt.clone());
         oneDayEarlier.add(Calendar.DAY_OF_MONTH, -1);

         Calendar oneDayLater = ((Calendar)createdAt.clone());
         oneDayLater.add(Calendar.DAY_OF_MONTH, 1);

         TransactionSearchRequest searchRequest = new TransactionSearchRequest().
             id().is(transaction.getId()).
             createdAt().between(oneDayEarlier, oneDayLater);

         Assert.assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

         searchRequest = new TransactionSearchRequest().
             id().is(transaction.getId()).
             createdAt().greaterThanOrEqualTo(oneDayEarlier);

         Assert.assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

         searchRequest = new TransactionSearchRequest().
             id().is(transaction.getId()).
             createdAt().lessThanOrEqualTo(oneDayLater);

         Assert.assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

         searchRequest = new TransactionSearchRequest().
             id().is(transaction.getId()).
             createdAt().between(threeDaysEarlier, oneDayEarlier);

         Assert.assertEquals(0, gateway.transaction().search(searchRequest).getMaximumSize());
     }

    @Test
    public void searchOnCreatedAtUsingLocalTime() {
        TransactionRequest request = new TransactionRequest().
                amount(TransactionAmount.AUTHORIZE.amount).
                creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2010").
                done();

        Transaction transaction = gateway.transaction().sale(request).getTarget();

        Calendar oneDayEarlier = Calendar.getInstance();
        oneDayEarlier.add(Calendar.DAY_OF_MONTH, -1);

        Calendar oneDayLater = Calendar.getInstance();
        oneDayLater.add(Calendar.DAY_OF_MONTH, 1);

        TransactionSearchRequest searchRequest = new TransactionSearchRequest().
                id().is(transaction.getId()).
                createdAt().between(oneDayEarlier, oneDayLater);

        Assert.assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());
    }

    @Test
    public void searchOnAuthorizationExpiredAt() {
        Calendar threeDaysEarlier = Calendar.getInstance();
        threeDaysEarlier.add(Calendar.DAY_OF_MONTH, -3);

        Calendar oneDayEarlier = Calendar.getInstance();
        oneDayEarlier.add(Calendar.DAY_OF_MONTH, -1);

        Calendar oneDayLater = Calendar.getInstance();
        oneDayLater.add(Calendar.DAY_OF_MONTH, 1);

        TransactionSearchRequest searchRequest = new TransactionSearchRequest().
            authorizationExpiredAt().between(oneDayEarlier, oneDayLater);

        ResourceCollection<Transaction> results = gateway.transaction().search(searchRequest);

        Assert.assertTrue(results.getMaximumSize() > 0);
        Assert.assertEquals(Transaction.Status.AUTHORIZATION_EXPIRED, results.getFirst().getStatus());

        searchRequest = new TransactionSearchRequest().
            authorizationExpiredAt().between(threeDaysEarlier, oneDayEarlier);

        Assert.assertEquals(0, gateway.transaction().search(searchRequest).getMaximumSize());
    }

    @Test
    public void searchOnAuthorizedAt() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2010").
                done();

        Transaction transaction = gateway.transaction().sale(request).getTarget();

        Calendar threeDaysEarlier = Calendar.getInstance();
        threeDaysEarlier.add(Calendar.DAY_OF_MONTH, -3);

        Calendar oneDayEarlier = Calendar.getInstance();
        oneDayEarlier.add(Calendar.DAY_OF_MONTH, -1);

        Calendar oneDayLater = Calendar.getInstance();
        oneDayLater.add(Calendar.DAY_OF_MONTH, 1);

        TransactionSearchRequest searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            authorizedAt().between(oneDayEarlier, oneDayLater);

        Assert.assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            authorizedAt().greaterThanOrEqualTo(oneDayEarlier);

        Assert.assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            authorizedAt().lessThanOrEqualTo(oneDayLater);

        Assert.assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            authorizedAt().between(threeDaysEarlier, oneDayEarlier);

        Assert.assertEquals(0, gateway.transaction().search(searchRequest).getMaximumSize());
    }

    @Test
    public void searchOnFailedAt() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.FAILED.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2010").
                done();

        Transaction transaction = gateway.transaction().sale(request).getTransaction();

        Calendar threeDaysEarlier = Calendar.getInstance();
        threeDaysEarlier.add(Calendar.DAY_OF_MONTH, -3);

        Calendar oneDayEarlier = Calendar.getInstance();
        oneDayEarlier.add(Calendar.DAY_OF_MONTH, -1);

        Calendar oneDayLater = Calendar.getInstance();
        oneDayLater.add(Calendar.DAY_OF_MONTH, 1);

        TransactionSearchRequest searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            failedAt().between(oneDayEarlier, oneDayLater);

        Assert.assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            failedAt().greaterThanOrEqualTo(oneDayEarlier);

        Assert.assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            failedAt().lessThanOrEqualTo(oneDayLater);

        Assert.assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            failedAt().between(threeDaysEarlier, oneDayEarlier);

        Assert.assertEquals(0, gateway.transaction().search(searchRequest).getMaximumSize());
    }

    @Test
    public void searchOnGatewayRejectedAt() {
        BraintreeGateway processingRulesGateway = new BraintreeGateway(Environment.DEVELOPMENT, "processing_rules_merchant_id", "processing_rules_public_key", "processing_rules_private_key");
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                cvv("200").
                done();

        Transaction transaction = processingRulesGateway.transaction().sale(request).getTransaction();

        Calendar threeDaysEarlier = Calendar.getInstance();
        threeDaysEarlier.add(Calendar.DAY_OF_MONTH, -3);

        Calendar oneDayEarlier = Calendar.getInstance();
        oneDayEarlier.add(Calendar.DAY_OF_MONTH, -1);

        Calendar oneDayLater = Calendar.getInstance();
        oneDayLater.add(Calendar.DAY_OF_MONTH, 1);

        TransactionSearchRequest searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            gatewayRejectedAt().between(oneDayEarlier, oneDayLater);

        Assert.assertEquals(1, processingRulesGateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            gatewayRejectedAt().greaterThanOrEqualTo(oneDayEarlier);

        Assert.assertEquals(1, processingRulesGateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            gatewayRejectedAt().lessThanOrEqualTo(oneDayLater);

        Assert.assertEquals(1, processingRulesGateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            gatewayRejectedAt().between(threeDaysEarlier, oneDayEarlier);

        Assert.assertEquals(0, processingRulesGateway.transaction().search(searchRequest).getMaximumSize());
    }

    @Test
    public void searchOnProcessorDeclinedAt() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.DECLINE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2010").
                done();

        Transaction transaction = gateway.transaction().sale(request).getTransaction();

        Calendar threeDaysEarlier = Calendar.getInstance();
        threeDaysEarlier.add(Calendar.DAY_OF_MONTH, -3);

        Calendar oneDayEarlier = Calendar.getInstance();
        oneDayEarlier.add(Calendar.DAY_OF_MONTH, -1);

        Calendar oneDayLater = Calendar.getInstance();
        oneDayLater.add(Calendar.DAY_OF_MONTH, 1);

        TransactionSearchRequest searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            processorDeclinedAt().between(oneDayEarlier, oneDayLater);

        Assert.assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            processorDeclinedAt().greaterThanOrEqualTo(oneDayEarlier);

        Assert.assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            processorDeclinedAt().lessThanOrEqualTo(oneDayLater);

        Assert.assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            processorDeclinedAt().between(threeDaysEarlier, oneDayEarlier);

        Assert.assertEquals(0, gateway.transaction().search(searchRequest).getMaximumSize());
    }

    @Test
    public void searchOnSettledAt() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2010").
                done().
            options().
                submitForSettlement(true).
                done();

        Transaction transaction = gateway.transaction().sale(request).getTarget();
        TestHelper.settle(gateway, transaction.getId());
        transaction = gateway.transaction().find(transaction.getId());

        Calendar threeDaysEarlier = Calendar.getInstance();
        threeDaysEarlier.add(Calendar.DAY_OF_MONTH, -3);

        Calendar oneDayEarlier = Calendar.getInstance();
        oneDayEarlier.add(Calendar.DAY_OF_MONTH, -1);

        Calendar oneDayLater = Calendar.getInstance();
        oneDayLater.add(Calendar.DAY_OF_MONTH, 1);

        TransactionSearchRequest searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            settledAt().between(oneDayEarlier, oneDayLater);

        Assert.assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            settledAt().greaterThanOrEqualTo(oneDayEarlier);

        Assert.assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            settledAt().lessThanOrEqualTo(oneDayLater);

        Assert.assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            settledAt().between(threeDaysEarlier, oneDayEarlier);

        Assert.assertEquals(0, gateway.transaction().search(searchRequest).getMaximumSize());
    }

    @Test
    public void searchOnSubmittedForSettlementAt() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2010").
                done().
            options().
                submitForSettlement(true).
                done();

        Transaction transaction = gateway.transaction().sale(request).getTarget();

        Calendar threeDaysEarlier = Calendar.getInstance();
        threeDaysEarlier.add(Calendar.DAY_OF_MONTH, -3);

        Calendar oneDayEarlier = Calendar.getInstance();
        oneDayEarlier.add(Calendar.DAY_OF_MONTH, -1);

        Calendar oneDayLater = Calendar.getInstance();
        oneDayLater.add(Calendar.DAY_OF_MONTH, 1);

        TransactionSearchRequest searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            submittedForSettlementAt().between(oneDayEarlier, oneDayLater);

        Assert.assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            submittedForSettlementAt().greaterThanOrEqualTo(oneDayEarlier);

        Assert.assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            submittedForSettlementAt().lessThanOrEqualTo(oneDayLater);

        Assert.assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            submittedForSettlementAt().between(threeDaysEarlier, oneDayEarlier);

        Assert.assertEquals(0, gateway.transaction().search(searchRequest).getMaximumSize());
    }

    @Test
    public void searchOnVoidedAt() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2010").
                done();

        Transaction transaction = gateway.transaction().sale(request).getTarget();
        transaction = gateway.transaction().voidTransaction(transaction.getId()).getTarget();

        Calendar threeDaysEarlier = Calendar.getInstance();
        threeDaysEarlier.add(Calendar.DAY_OF_MONTH, -3);

        Calendar oneDayEarlier = Calendar.getInstance();
        oneDayEarlier.add(Calendar.DAY_OF_MONTH, -1);

        Calendar oneDayLater = Calendar.getInstance();
        oneDayLater.add(Calendar.DAY_OF_MONTH, 1);

        TransactionSearchRequest searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            voidedAt().between(oneDayEarlier, oneDayLater);

        Assert.assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            voidedAt().greaterThanOrEqualTo(oneDayEarlier);

        Assert.assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            voidedAt().lessThanOrEqualTo(oneDayLater);

        Assert.assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            voidedAt().between(threeDaysEarlier, oneDayEarlier);

        Assert.assertEquals(0, gateway.transaction().search(searchRequest).getMaximumSize());
    }

    @Test
    public void searchOnMultipleStatusAtFields() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2010").
                done().
            options().
                submitForSettlement(true).
                done();

        Transaction transaction = gateway.transaction().sale(request).getTarget();

        Calendar threeDaysEarlier = Calendar.getInstance();
        threeDaysEarlier.add(Calendar.DAY_OF_MONTH, -3);

        Calendar oneDayEarlier = Calendar.getInstance();
        oneDayEarlier.add(Calendar.DAY_OF_MONTH, -1);

        Calendar oneDayLater = Calendar.getInstance();
        oneDayLater.add(Calendar.DAY_OF_MONTH, 1);

        TransactionSearchRequest searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            authorizedAt().between(oneDayEarlier, oneDayLater).
            submittedForSettlementAt().between(oneDayEarlier, oneDayLater);

        Assert.assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            authorizedAt().between(threeDaysEarlier, oneDayEarlier).
            submittedForSettlementAt().between(threeDaysEarlier, oneDayEarlier);

        Assert.assertEquals(0, gateway.transaction().search(searchRequest).getMaximumSize());
    }

    @Test
    @SuppressWarnings("deprecation")
    public void refundTransaction() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2008").
                done().
            options().
                submitForSettlement(true).
                done();
        String transactionId = gateway.transaction().sale(request).getTarget().getId();
        TestHelper.settle(gateway, transactionId);

        Result<Transaction> result = gateway.transaction().refund(transactionId);
        Assert.assertTrue(result.isSuccess());

        Transaction refund = result.getTarget();
        Transaction originalTransaction = gateway.transaction().find(transactionId);

        Assert.assertEquals(Transaction.Type.CREDIT, refund.getType());
        Assert.assertEquals(originalTransaction.getAmount(), refund.getAmount());
        Assert.assertEquals(refund.getId(), originalTransaction.getRefundId());
        Assert.assertEquals(originalTransaction.getId(), refund.getRefundedTransactionId());
    }

    @Test
    public void refundTransactionWithPartialAmount() {
        TransactionRequest request = new TransactionRequest().
        amount(TransactionAmount.AUTHORIZE.amount).
        creditCard().
            number(CreditCardNumber.VISA.number).
            expirationDate("05/2008").
            done().
        options().
            submitForSettlement(true).
            done();
        Transaction transaction = gateway.transaction().sale(request).getTarget();
        TestHelper.settle(gateway, transaction.getId());

        Result<Transaction> result = gateway.transaction().refund(transaction.getId(), TransactionAmount.AUTHORIZE.amount.divide(new BigDecimal(2)));
        Assert.assertTrue(result.isSuccess());
        Assert.assertEquals(Transaction.Type.CREDIT, result.getTarget().getType());
        Assert.assertEquals(TransactionAmount.AUTHORIZE.amount.divide(new BigDecimal(2)), result.getTarget().getAmount());
    }

    @Test
    public void refundMultipleTransactionsWithPartialAmounts() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2008").
                done().
            options().
                submitForSettlement(true).
                done();
        Transaction transaction = gateway.transaction().sale(request).getTarget();
        TestHelper.settle(gateway, transaction.getId());

        Transaction refund1 = gateway.transaction().refund(transaction.getId(), TransactionAmount.AUTHORIZE.amount.divide(new BigDecimal(2))).getTarget();
        Assert.assertEquals(Transaction.Type.CREDIT, refund1.getType());
        Assert.assertEquals(TransactionAmount.AUTHORIZE.amount.divide(new BigDecimal(2)), refund1.getAmount());

        Transaction refund2 = gateway.transaction().refund(transaction.getId(), TransactionAmount.AUTHORIZE.amount.divide(new BigDecimal(2))).getTarget();
        Assert.assertEquals(Transaction.Type.CREDIT, refund2.getType());
        Assert.assertEquals(TransactionAmount.AUTHORIZE.amount.divide(new BigDecimal(2)), refund2.getAmount());

        transaction = gateway.transaction().find(transaction.getId());
        Assert.assertTrue(TestHelper.listIncludes(transaction.getRefundIds(), refund1.getId()));
        Assert.assertTrue(TestHelper.listIncludes(transaction.getRefundIds(), refund1.getId()));
    }

    @Test
    public void refundFailsWithNonSettledTransaction() {
        TransactionRequest request = new TransactionRequest().
        amount(TransactionAmount.AUTHORIZE.amount).
        creditCard().
            number(CreditCardNumber.VISA.number).
            expirationDate("05/2008").
            done();
        Transaction transaction = gateway.transaction().sale(request).getTarget();
        Assert.assertEquals(Transaction.Status.AUTHORIZED, transaction.getStatus());

        Result<Transaction> result = gateway.transaction().refund(transaction.getId());
        Assert.assertFalse(result.isSuccess());

        Assert.assertEquals(ValidationErrorCode.TRANSACTION_CANNOT_REFUND_UNLESS_SETTLED,
                result.getErrors().forObject("transaction").onField("base").get(0).getCode());
    }

    @Test
    public void unrecognizedStatus() {
        String xml = "<transaction><status>foobar</status><billing/><credit-card/><customer/><descriptor/><shipping/><subscription/><service-fee></service-fee><deposit-details/><type>sale</type></transaction>";
        Transaction transaction = new Transaction(NodeWrapperFactory.instance.create(xml));
        Assert.assertEquals(Transaction.Status.UNRECOGNIZED, transaction.getStatus());
    }

    @Test
    public void unrecognizedType() {
        String xml = "<transaction><type>foobar</type><billing/><credit-card/><customer/><descriptor/><shipping/><subscription/><service-fee></service-fee><deposit-details/><type>sale</type></transaction>";
        Transaction transaction = new Transaction(NodeWrapperFactory.instance.create(xml));
        Assert.assertEquals(Transaction.Type.UNRECOGNIZED, transaction.getType());
    }

    @Test
    public void gatewayRejectedOnCvv() {
        BraintreeGateway processingRulesGateway = new BraintreeGateway(Environment.DEVELOPMENT, "processing_rules_merchant_id", "processing_rules_public_key", "processing_rules_private_key");
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                cvv("200").
                done();

        Result<Transaction> result = processingRulesGateway.transaction().sale(request);
        Assert.assertFalse(result.isSuccess());
        Transaction transaction = result.getTransaction();

        Assert.assertEquals(Transaction.GatewayRejectionReason.CVV, transaction.getGatewayRejectionReason());
    }

    @Test
    public void gatewayRejectedOnAvs() {
        BraintreeGateway processingRulesGateway = new BraintreeGateway(Environment.DEVELOPMENT, "processing_rules_merchant_id", "processing_rules_public_key", "processing_rules_private_key");
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            billingAddress().
                postalCode("20001").
                done().
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done();

        Result<Transaction> result = processingRulesGateway.transaction().sale(request);
        Assert.assertFalse(result.isSuccess());
        Transaction transaction = result.getTransaction();

        Assert.assertEquals(Transaction.GatewayRejectionReason.AVS, transaction.getGatewayRejectionReason());
    }

    @Test
    public void gatewayRejectedOnAvsAndCvv() {
        BraintreeGateway processingRulesGateway = new BraintreeGateway(Environment.DEVELOPMENT, "processing_rules_merchant_id", "processing_rules_public_key", "processing_rules_private_key");
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            billingAddress().
                postalCode("20001").
                done().
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                cvv("200").
                done();

        Result<Transaction> result = processingRulesGateway.transaction().sale(request);
        Assert.assertFalse(result.isSuccess());
        Transaction transaction = result.getTransaction();

        Assert.assertEquals(Transaction.GatewayRejectionReason.AVS_AND_CVV, transaction.getGatewayRejectionReason());
    }

    @Test
    public void snapshotPlanIdAddOnsAndDiscountsFromSubscription() {
        CustomerRequest customerRequest = new CustomerRequest().
            creditCard().
                number("5105105105105100").
                expirationDate("05/12").
                done();
        CreditCard creditCard = gateway.customer().create(customerRequest).getTarget().getCreditCards().get(0);

        SubscriptionRequest request = new SubscriptionRequest().
            paymentMethodToken(creditCard.getToken()).
            planId(PlanFixture.PLAN_WITHOUT_TRIAL.getId()).
            addOns().
                add().
                    amount(new BigDecimal("11.00")).
                    inheritedFromId("increase_10").
                    numberOfBillingCycles(5).
                    quantity(2).
                    done().
                add().
                    amount(new BigDecimal("21.00")).
                    inheritedFromId("increase_20").
                    numberOfBillingCycles(6).
                    quantity(3).
                    done().
                done().
            discounts().
                add().
                    amount(new BigDecimal("7.50")).
                    inheritedFromId("discount_7").
                    neverExpires(true).
                    quantity(2).
                    done().
                done();

        Transaction transaction = gateway.subscription().create(request).getTarget().getTransactions().get(0);

        Assert.assertEquals(PlanFixture.PLAN_WITHOUT_TRIAL.getId(), transaction.getPlanId());

        List<AddOn> addOns = transaction.getAddOns();
        Collections.sort(addOns, new TestHelper.CompareModificationsById());

        Assert.assertEquals(2, addOns.size());

        Assert.assertEquals("increase_10", addOns.get(0).getId());
        Assert.assertEquals(new BigDecimal("11.00"), addOns.get(0).getAmount());
        Assert.assertEquals(new Integer(5), addOns.get(0).getNumberOfBillingCycles());
        Assert.assertEquals(new Integer(2), addOns.get(0).getQuantity());
        Assert.assertFalse(addOns.get(0).neverExpires());

        Assert.assertEquals("increase_20", addOns.get(1).getId());
        Assert.assertEquals(new BigDecimal("21.00"), addOns.get(1).getAmount());
        Assert.assertEquals(new Integer(6), addOns.get(1).getNumberOfBillingCycles());
        Assert.assertEquals(new Integer(3), addOns.get(1).getQuantity());
        Assert.assertFalse(addOns.get(1).neverExpires());

        List<Discount> discounts = transaction.getDiscounts();
        Assert.assertEquals(1, discounts.size());

        Assert.assertEquals("discount_7", discounts.get(0).getId());
        Assert.assertEquals(new BigDecimal("7.50"), discounts.get(0).getAmount());
        Assert.assertNull(discounts.get(0).getNumberOfBillingCycles());
        Assert.assertEquals(new Integer(2), discounts.get(0).getQuantity());
        Assert.assertTrue(discounts.get(0).neverExpires());
    }

    @Test
    public void serviceFee() {
    	TransactionRequest request = new TransactionRequest().
    		merchantAccountId(MerchantAccount.NON_DEFAULT_MERCHANT_ACCOUNT_ID).
	        amount(new BigDecimal("100.00")).
	        creditCard().
	        	number(CreditCardNumber.VISA.number).
	        	expirationDate("05/2009").
	        	done().
	        serviceFee().
	        	amount(new BigDecimal("1.00")).
	        	merchantAccountId(MerchantAccount.DEFAULT_MERCHANT_ACCOUNT_ID).
	        	done();

        Result<Transaction> result = gateway.transaction().sale(request);
        Assert.assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        Assert.assertEquals(new BigDecimal("1.00"), transaction.getServiceFee().getAmount());
        Assert.assertEquals(MerchantAccount.DEFAULT_MERCHANT_ACCOUNT_ID, transaction.getServiceFee().getMerchantAccountId());
    }
}
