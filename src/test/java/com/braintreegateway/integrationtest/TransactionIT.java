package com.braintreegateway.integrationtest;

import com.braintreegateway.*;
import com.braintreegateway.SandboxValues.CreditCardNumber;
import com.braintreegateway.SandboxValues.TransactionAmount;
import com.braintreegateway.exceptions.DownForMaintenanceException;
import com.braintreegateway.exceptions.ForgedQueryStringException;
import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.test.CreditCardNumbers;
import com.braintreegateway.test.Nonce;
import com.braintreegateway.test.VenmoSdk;
import com.braintreegateway.testhelpers.CalendarTestUtils;
import com.braintreegateway.testhelpers.MerchantAccountTestConstants;
import com.braintreegateway.testhelpers.TestHelper;
import com.braintreegateway.testhelpers.ThreeDSecureRequestForTests;
import com.braintreegateway.util.NodeWrapperFactory;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class TransactionIT extends IntegrationTest implements MerchantAccountTestConstants {

    public static final String DISBURSEMENT_TRANSACTION_ID = "deposittransaction";
    public static final String DISPUTED_TRANSACTION_ID = "disputedtransaction";
    public static final String TWO_DISPUTE_TRANSACTION_ID = "2disputetransaction";
    public static final String AUTH_ADJUSTMENT_TRANSACTION_ID = "authadjustmenttransaction";
    public static final String AUTH_ADJUSTMENT_SOFT_DECLINED_TRANSACTION_ID = "authadjustmenttransactionsoftdeclined";
    public static final String AUTH_ADJUSTMENT_HARD_DECLINED_TRANSACTION_ID = "authadjustmenttransactionharddeclined";

    @SuppressWarnings("deprecation")
    @Test
    public void transparentRedirectURLForCreate() {
        Configuration configuration = gateway.getConfiguration();
        assertEquals(configuration.getBaseURL() + configuration.getMerchantPath() + "/transactions/all/create_via_transparent_redirect_request",
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
        assertTrue(trData.contains("sale"));
    }

    @Test
    public void creditTrData() {
        String trData = gateway.transaction().creditTrData(new TransactionRequest(), "http://example.com");
        TestHelper.assertValidTrData(gateway.getConfiguration(), trData);
        assertTrue(trData.contains("credit"));
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
        assertTrue(result.isSuccess());
    }

    @SuppressWarnings("deprecation")
    @Test(expected = ForgedQueryStringException.class)
    public void createViaTransparentRedirectThrowsWhenQueryStringHasBeenTamperedWith() {
        String queryString = TestHelper.simulateFormPostForTR(gateway, new TransactionRequest(), new TransactionRequest(), gateway.transaction().transparentRedirectURLForCreate());
        gateway.transaction().confirmTransparentRedirect(queryString + "this make it invalid");
    }

    @Test
    public void createWithPaymentMethodNonce() {
        String nonce = TestHelper.generateUnlockedNonce(gateway);
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            paymentMethodNonce(nonce);
        Result<Transaction> result = gateway.transaction().sale(request);
        assert(result.isSuccess());
    }

    @Test
    public void createWithAccountTypeCredit() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            merchantAccountId("hiper_brl").
            creditCard().
                number(CreditCardNumber.HIPER.number).
                expirationDate("05/2009").
                done().
            options().
                creditCard().
                    accountType("credit").
                    done().
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();
        assertEquals("credit", transaction.getCreditCard().getAccountType());
    }

    @Test
    public void createWithAccountTypeDebit() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            merchantAccountId("hiper_brl").
            creditCard().
                number(CreditCardNumber.HIPER.number).
                expirationDate("05/2009").
                done().
            options().
                submitForSettlement(true).
                creditCard().
                    accountType("debit").
                    done().
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();
        assertEquals("debit", transaction.getCreditCard().getAccountType());
    }

    @Test
    public void createWithErrorAccountTypeInvalid() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            merchantAccountId("hiper_brl").
            creditCard().
                number(CreditCardNumber.HIPER.number).
                expirationDate("05/2009").
                done().
            options().
                submitForSettlement(true).
                creditCard().
                    accountType("ach").
                    done().
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());
        assertEquals(ValidationErrorCode.TRANSACTION_OPTIONS_CREDIT_CARD_ACCOUNT_TYPE_IS_INVALID,
                result.getErrors().forObject("transaction").forObject("options").forObject("creditCard").onField("accountType").get(0).getCode());
    }

    @Test
    public void createWithErrorAccountTypeNotSupported() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            options().
                submitForSettlement(true).
                creditCard().
                    accountType("credit").
                    done().
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());
        assertEquals(ValidationErrorCode.TRANSACTION_OPTIONS_CREDIT_CARD_ACCOUNT_TYPE_NOT_SUPPORTED,
                result.getErrors().forObject("transaction").forObject("options").forObject("creditCard").onField("accountType").get(0).getCode());
    }

    @Test
    public void createWithErrorAccountTypeDebitDoesNotSupportAuths() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            merchantAccountId("hiper_brl").
            creditCard().
                number(CreditCardNumber.HIPER.number).
                expirationDate("05/2009").
                done().
            options().
                creditCard().
                    accountType("debit").
                    done().
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());
        assertEquals(ValidationErrorCode.TRANSACTION_OPTIONS_CREDIT_CARD_ACCOUNT_TYPE_DEBIT_DOES_NOT_SUPPORT_AUTHS,
                result.getErrors().forObject("transaction").forObject("options").forObject("creditCard").onField("accountType").get(0).getCode());
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
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        TransactionCloneRequest cloneRequest = new TransactionCloneRequest().
            amount(new BigDecimal("123.45")).
            channel("MyShoppingCartProvider").
            options().
                submitForSettlement(false).
                done();
        Result<Transaction> cloneResult = gateway.transaction().cloneTransaction(transaction.getId(), cloneRequest);
        assertTrue(cloneResult.isSuccess());
        Transaction cloneTransaction = cloneResult.getTarget();

        assertEquals(new BigDecimal("123.45"), cloneTransaction.getAmount());
        assertEquals("MyShoppingCartProvider", cloneTransaction.getChannel());
        assertEquals("123", cloneTransaction.getOrderId());
        assertEquals("411111******1111", cloneTransaction.getCreditCard().getMaskedNumber());
        assertEquals("Dan", cloneTransaction.getCustomer().getFirstName());
        assertEquals("Carl", cloneTransaction.getBillingAddress().getFirstName());
        assertEquals("Andrew", cloneTransaction.getShippingAddress().getFirstName());
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
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        TransactionCloneRequest cloneRequest = new TransactionCloneRequest().amount(new BigDecimal("123.45")).options().submitForSettlement(true).done();
        Result<Transaction> cloneResult = gateway.transaction().cloneTransaction(transaction.getId(), cloneRequest);
        assertTrue(cloneResult.isSuccess());
        Transaction cloneTransaction = cloneResult.getTarget();

        assertEquals(Transaction.Status.SUBMITTED_FOR_SETTLEMENT, cloneTransaction.getStatus());
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
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        TransactionCloneRequest cloneRequest = new TransactionCloneRequest().amount(new BigDecimal("123.45"));
        Result<Transaction> cloneResult = gateway.transaction().cloneTransaction(transaction.getId(), cloneRequest);
        assertFalse(cloneResult.isSuccess());


        assertEquals(ValidationErrorCode.TRANSACTION_CANNOT_CLONE_CREDIT,
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
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        assertEquals(new BigDecimal("1000.00"), transaction.getAmount());
        assertEquals("USD", transaction.getCurrencyIsoCode());
        assertNotNull(transaction.getProcessorAuthorizationCode());
        assertEquals(Transaction.Type.SALE, transaction.getType());
        assertEquals(Transaction.Status.AUTHORIZED, transaction.getStatus());
        assertEquals("1000", transaction.getProcessorResponseCode());
        assertEquals("Approved", transaction.getProcessorResponseText());
        assertEquals(ProcessorResponseType.APPROVED, transaction.getProcessorResponseType());
        assertNotNull(transaction.getAuthorizationExpiresAt());
        assertEquals(Calendar.getInstance().get(Calendar.YEAR), transaction.getCreatedAt().get(Calendar.YEAR));
        assertEquals(Calendar.getInstance().get(Calendar.YEAR), transaction.getUpdatedAt().get(Calendar.YEAR));

        CreditCard creditCard = transaction.getCreditCard();
        assertEquals("411111", creditCard.getBin());
        assertEquals("1111", creditCard.getLast4());
        assertEquals("05", creditCard.getExpirationMonth());
        assertEquals("2009", creditCard.getExpirationYear());
        assertEquals("05/2009", creditCard.getExpirationDate());
    }

    @Test
    public void saleWithEloCard() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            merchantAccountId(ADYEN_MERCHANT_ACCOUNT_ID).
            creditCard().
                number(CreditCardNumber.ELO.number).
                expirationDate("10/2020").
                cvv("737").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        assertEquals(new BigDecimal("1000.00"), transaction.getAmount());
        assertEquals("USD", transaction.getCurrencyIsoCode());
        assertNotNull(transaction.getProcessorAuthorizationCode());
        assertEquals(Transaction.Type.SALE, transaction.getType());
        assertEquals(Transaction.Status.AUTHORIZED, transaction.getStatus());
        assertNotNull(transaction.getAuthorizationExpiresAt());
        assertEquals(Calendar.getInstance().get(Calendar.YEAR), transaction.getCreatedAt().get(Calendar.YEAR));
        assertEquals(Calendar.getInstance().get(Calendar.YEAR), transaction.getUpdatedAt().get(Calendar.YEAR));

        CreditCard creditCard = transaction.getCreditCard();
        assertEquals("506699", creditCard.getBin());
        assertEquals("1118", creditCard.getLast4());
        assertEquals("10", creditCard.getExpirationMonth());
        assertEquals("2020", creditCard.getExpirationYear());
        assertEquals("10/2020", creditCard.getExpirationDate());
    }

    @Test
    public void saleWithAccessToken() {
        BraintreeGateway oauthGateway = new BraintreeGateway("client_id$development$integration_client_id", "client_secret$development$integration_client_secret");

        String code = TestHelper.createOAuthGrant(oauthGateway, "integration_merchant_id", "read_write");

        OAuthCredentialsRequest oauthRequest = new OAuthCredentialsRequest().
             code(code).
             scope("read_write");

        Result<OAuthCredentials> accessTokenResult = oauthGateway.oauth().createTokenFromCode(oauthRequest);

        BraintreeGateway gateway = new BraintreeGateway(accessTokenResult.getTarget().getAccessToken());

        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        assertEquals(new BigDecimal("1000.00"), transaction.getAmount());
        assertEquals("USD", transaction.getCurrencyIsoCode());
        assertNotNull(transaction.getProcessorAuthorizationCode());
        assertEquals(Transaction.Type.SALE, transaction.getType());
        assertEquals(Transaction.Status.AUTHORIZED, transaction.getStatus());
    }

    @Test
    public void saleReturnsRiskData() {
        createAdvancedFraudMerchantGateway();
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            deviceSessionId("abc123").
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        RiskData riskData = transaction.getRiskData();
        assertEquals("Approve", riskData.getDecision());
        assertFalse(riskData.getDeviceDataCaptured());
        assertNotNull(riskData.getId());
        assertNotNull(riskData.getFraudServiceProvider());
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
        assertTrue(result.isSuccess());

        CreditCard card = result.getTarget().getCreditCard();

        assertEquals(CreditCard.Prepaid.YES, card.getPrepaid());
        assertEquals(CreditCard.Healthcare.UNKNOWN, card.getHealthcare());
        assertEquals(CreditCard.Payroll.UNKNOWN, card.getPayroll());
        assertEquals(CreditCard.Debit.UNKNOWN, card.getDebit());
        assertEquals(CreditCard.DurbinRegulated.UNKNOWN, card.getDurbinRegulated());
        assertEquals(CreditCard.Commercial.UNKNOWN, card.getCommercial());
        assertEquals("Unknown", card.getCountryOfIssuance());
        assertEquals("Unknown", card.getIssuingBank());
        assertEquals("Unknown", card.getProductId());
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
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        assertEquals(new BigDecimal("1000.00"), transaction.getAmount());
        assertEquals(Transaction.Status.AUTHORIZED, transaction.getStatus());
        assertEquals("MyShoppingCartProvider", transaction.getChannel());
        assertEquals("123", transaction.getOrderId());
        assertNull(transaction.getVaultCreditCard(gateway));
        assertNull(transaction.getVaultCustomer(gateway));
        assertNull(transaction.getAvsErrorResponseCode());
        assertEquals("M", transaction.getAvsPostalCodeResponseCode());
        assertEquals("M", transaction.getAvsStreetAddressResponseCode());
        assertEquals("M", transaction.getCvvResponseCode());
        assertFalse(transaction.isTaxExempt());
        assertNull(transaction.getVaultCreditCard(gateway));
        CreditCard creditCard = transaction.getCreditCard();
        assertEquals("411111", creditCard.getBin());
        assertEquals("1111", creditCard.getLast4());
        assertEquals("05", creditCard.getExpirationMonth());
        assertEquals("2009", creditCard.getExpirationYear());
        assertEquals("05/2009", creditCard.getExpirationDate());
        assertEquals("The Cardholder", creditCard.getCardholderName());
        assertNull(transaction.getVoiceReferralNumber());

        assertNull(transaction.getVaultCustomer(gateway));
        Customer customer = transaction.getCustomer();
        assertEquals("Dan", customer.getFirstName());
        assertEquals("Smith", customer.getLastName());
        assertEquals("Braintree Payment Solutions", customer.getCompany());
        assertEquals("dan@example.com", customer.getEmail());
        assertEquals("419-555-1234", customer.getPhone());
        assertEquals("419-555-1235", customer.getFax());
        assertEquals("http://braintreepayments.com", customer.getWebsite());

        assertNull(transaction.getVaultBillingAddress(gateway));
        Address billing = transaction.getBillingAddress();
        assertEquals("Carl", billing.getFirstName());
        assertEquals("Jones", billing.getLastName());
        assertEquals("Braintree", billing.getCompany());
        assertEquals("123 E Main St", billing.getStreetAddress());
        assertEquals("Suite 403", billing.getExtendedAddress());
        assertEquals("Chicago", billing.getLocality());
        assertEquals("IL", billing.getRegion());
        assertEquals("60622", billing.getPostalCode());
        assertEquals("United States of America", billing.getCountryName());
        assertEquals("US", billing.getCountryCodeAlpha2());
        assertEquals("USA", billing.getCountryCodeAlpha3());
        assertEquals("840", billing.getCountryCodeNumeric());

        assertNull(transaction.getVaultShippingAddress(gateway));
        Address shipping = transaction.getShippingAddress();
        assertEquals("Andrew", shipping.getFirstName());
        assertEquals("Mason", shipping.getLastName());
        assertEquals("Braintree Shipping", shipping.getCompany());
        assertEquals("456 W Main St", shipping.getStreetAddress());
        assertEquals("Apt 2F", shipping.getExtendedAddress());
        assertEquals("Bartlett", shipping.getLocality());
        assertEquals("MA", shipping.getRegion());
        assertEquals("60103", shipping.getPostalCode());
        assertEquals("Mexico", shipping.getCountryName());
        assertEquals("MX", shipping.getCountryCodeAlpha2());
        assertEquals("MEX", shipping.getCountryCodeAlpha3());
        assertEquals("484", shipping.getCountryCodeNumeric());
    }

    @Test
    public void saleWithSpecifyingMerchantAccountId() {
        TransactionRequest request = new TransactionRequest().
            merchantAccountId(NON_DEFAULT_MERCHANT_ACCOUNT_ID).
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        assertEquals(NON_DEFAULT_MERCHANT_ACCOUNT_ID, transaction.getMerchantAccountId());
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
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        assertEquals(DEFAULT_MERCHANT_ACCOUNT_ID, transaction.getMerchantAccountId());
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
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        CreditCard creditCard = transaction.getCreditCard();
        assertEquals(paymentToken, creditCard.getToken());
        assertEquals("05/2009", transaction.getVaultCreditCard(gateway).getExpirationDate());

        Customer customer = transaction.getCustomer();
        assertEquals(customerId, customer.getId());
        assertEquals("Jane", transaction.getVaultCustomer(gateway).getFirstName());

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
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        CreditCard creditCard = transaction.getCreditCard();
        assertNotNull(creditCard.getToken());
        assertEquals("05/2009", transaction.getVaultCreditCard(gateway).getExpirationDate());

        Customer customer = transaction.getCustomer();
        assertNotNull(customer.getId());
        assertEquals("Jane", transaction.getVaultCustomer(gateway).getFirstName());
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
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        CreditCard creditCard = transaction.getCreditCard();
        assertNotNull(creditCard.getToken());
        assertEquals("05/2009", transaction.getVaultCreditCard(gateway).getExpirationDate());

        Customer customer = transaction.getCustomer();
        assertNotNull(customer.getId());
        assertEquals("Jane", transaction.getVaultCustomer(gateway).getFirstName());
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
        assertFalse(result.isSuccess());
        Transaction transaction = result.getTransaction();

        CreditCard creditCard = transaction.getCreditCard();
        assertNull(creditCard.getToken());
        assertNull(transaction.getVaultCreditCard(gateway));

        Customer customer = transaction.getCustomer();
        assertNull(customer.getId());
        assertNull(transaction.getVaultCustomer(gateway));
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
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        CreditCard creditCard = transaction.getVaultCreditCard(gateway);
        assertEquals("Carl", creditCard.getBillingAddress().getFirstName());
        assertEquals("Carl", transaction.getVaultBillingAddress(gateway).getFirstName());
        assertEquals("Andrew", transaction.getVaultShippingAddress(gateway).getFirstName());

        Customer customer = transaction.getVaultCustomer(gateway);
        assertEquals(2, customer.getAddresses().size());

        List<Address> addresses = new ArrayList<Address>(customer.getAddresses());
        Collections.sort(addresses, new Comparator<Address>() {
            public int compare(Address left, Address right) {
                return left.getFirstName().compareTo(right.getFirstName());
            }
        });
        assertEquals("Andrew", addresses.get(0).getFirstName());
        assertEquals("Carl", addresses.get(1).getFirstName());

        assertNotNull(transaction.getBillingAddress().getId());
        assertNotNull(transaction.getShippingAddress().getId());
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
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        assertEquals("Bob the Builder", transaction.getCreditCard().getCardholderName());
        assertNull(transaction.getVaultCreditCard(gateway));
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
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        assertEquals("Bob the Builder", transaction.getCreditCard().getCardholderName());
        assertEquals("Bob the Builder", transaction.getVaultCreditCard(gateway).getCardholderName());
    }

    @Test
    public void saleWithApplePayNonce() {
        String applePayNonce = Nonce.ApplePayAmex;

        TransactionRequest request = new TransactionRequest().
            amount(SandboxValues.TransactionAmount.AUTHORIZE.amount).
            paymentMethodNonce(applePayNonce);

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        assertEquals(PaymentInstrumentType.APPLE_PAY_CARD, transaction.getPaymentInstrumentType());
        assertNotNull(transaction.getApplePayDetails());
        assertNotNull(transaction.getApplePayDetails().getCardType());
        assertNotNull(transaction.getApplePayDetails().getPaymentInstrumentName());
        assertNotNull(transaction.getApplePayDetails().getSourceDescription());
        assertNotNull(transaction.getApplePayDetails().getExpirationMonth());
        assertNotNull(transaction.getApplePayDetails().getExpirationYear());
        assertNotNull(transaction.getApplePayDetails().getCardholderName());
        assertNotNull(transaction.getApplePayDetails().getLast4());
        assertNotNull(transaction.getApplePayDetails().getImageUrl());
        assertNotNull(transaction.getApplePayDetails().getBin());
        assertNotNull(transaction.getApplePayDetails().getPrepaid());
        assertNotNull(transaction.getApplePayDetails().getHealthcare());
        assertNotNull(transaction.getApplePayDetails().getDebit());
        assertNotNull(transaction.getApplePayDetails().getDurbinRegulated());
        assertNotNull(transaction.getApplePayDetails().getCommercial());
        assertNotNull(transaction.getApplePayDetails().getPayroll());
        assertNotNull(transaction.getApplePayDetails().getProductId());
    }

    @Test
    public void saleWithApplePayCardParams() {
        TransactionRequest request = new TransactionRequest()
                .amount(TransactionAmount.AUTHORIZE.amount)
                .applePayCardRequest()
                .number("370293001292109")
                .cardholderName("JANE SMITH")
                .cryptogram("AAAAAAAA/COBt84dnIEcwAA3gAAGhgEDoLABAAhAgAABAAAALnNCLw")
                .expirationMonth("10")
                .expirationYear("14")
                .eciIndicator("07")
                .done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        assertEquals(PaymentInstrumentType.APPLE_PAY_CARD, transaction.getPaymentInstrumentType());
        assertNotNull(transaction.getApplePayDetails());
        assertNotNull(transaction.getApplePayDetails().getCardType());
        assertNotNull(transaction.getApplePayDetails().getExpirationMonth());
        assertNotNull(transaction.getApplePayDetails().getExpirationYear());
        assertNotNull(transaction.getApplePayDetails().getCardholderName());
        assertNotNull(transaction.getApplePayDetails().getLast4());
        assertNotNull(transaction.getApplePayDetails().getImageUrl());
    }

    @Test
    public void saleWithAndroidPayProxyCardNonce() {
        String androidPayCardNonce = Nonce.AndroidPayDiscover;

        TransactionRequest request = new TransactionRequest().
            amount(SandboxValues.TransactionAmount.AUTHORIZE.amount).
            paymentMethodNonce(androidPayCardNonce);

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        assertEquals(PaymentInstrumentType.ANDROID_PAY_CARD, transaction.getPaymentInstrumentType());

        assertNotNull(transaction.getAndroidPayDetails());
        AndroidPayDetails androidPayDetails = transaction.getAndroidPayDetails();

        assertNull(androidPayDetails.getToken());
        assertNotNull(androidPayDetails.getBin());
        assertNotNull(androidPayDetails.getImageUrl());
        assertNotNull(androidPayDetails.getSourceCardType());
        assertNotNull(androidPayDetails.getSourceCardLast4());
        assertNotNull(androidPayDetails.getSourceDescription());
        assertNotNull(androidPayDetails.getVirtualCardType());
        assertNotNull(androidPayDetails.getVirtualCardLast4());
        assertNotNull(androidPayDetails.getGoogleTransactionId());
        assertNotNull(androidPayDetails.getCardType());
        assertNotNull(androidPayDetails.getLast4());
        assertNotNull(androidPayDetails.getExpirationMonth());
        assertNotNull(androidPayDetails.getExpirationYear());
        assertNotNull(androidPayDetails.getPrepaid());
        assertNotNull(androidPayDetails.getHealthcare());
        assertNotNull(androidPayDetails.getDebit());
        assertNotNull(androidPayDetails.getDurbinRegulated());
        assertNotNull(androidPayDetails.getCommercial());
        assertNotNull(androidPayDetails.getPayroll());
        assertNotNull(androidPayDetails.getProductId());
    }

    @Test
    public void saleWithAndroidPayNetworkTokenNonce() {
        String androidPayCardNonce = Nonce.AndroidPayMasterCard;

        TransactionRequest request = new TransactionRequest().
            amount(SandboxValues.TransactionAmount.AUTHORIZE.amount).
            paymentMethodNonce(androidPayCardNonce);

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        assertEquals(PaymentInstrumentType.ANDROID_PAY_CARD, transaction.getPaymentInstrumentType());

        assertNotNull(transaction.getAndroidPayDetails());
        AndroidPayDetails androidPayDetails = transaction.getAndroidPayDetails();

        assertNull(androidPayDetails.getToken());
        assertNotNull(androidPayDetails.getBin());
        assertNotNull(androidPayDetails.getImageUrl());
        assertNotNull(androidPayDetails.getSourceCardType());
        assertNotNull(androidPayDetails.getSourceCardLast4());
        assertNotNull(androidPayDetails.getSourceDescription());
        assertNotNull(androidPayDetails.getVirtualCardType());
        assertNotNull(androidPayDetails.getVirtualCardLast4());
        assertNotNull(androidPayDetails.getGoogleTransactionId());
        assertNotNull(androidPayDetails.getCardType());
        assertNotNull(androidPayDetails.getLast4());
        assertNotNull(androidPayDetails.getExpirationMonth());
        assertNotNull(androidPayDetails.getExpirationYear());
    }

    @Test
    public void saleWithAmexExpressCheckoutCardNonce() {
        String amexExpressCheckoutCardNonce = Nonce.AmexExpressCheckout;

        TransactionRequest request = new TransactionRequest().
            merchantAccountId(FAKE_AMEX_DIRECT_MERCHANT_ACCOUNT_ID).
            amount(SandboxValues.TransactionAmount.AUTHORIZE.amount).
            paymentMethodNonce(amexExpressCheckoutCardNonce);

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        assertEquals(PaymentInstrumentType.AMEX_EXPRESS_CHECKOUT_CARD, transaction.getPaymentInstrumentType());

        assertNotNull(transaction.getAmexExpressCheckoutDetails());
        AmexExpressCheckoutDetails amexExpressCheckoutDetails = transaction.getAmexExpressCheckoutDetails();

        assertNull(amexExpressCheckoutDetails.getToken());
        assertNotNull(amexExpressCheckoutDetails.getCardType());
        assertNotNull(amexExpressCheckoutDetails.getBin());
        assertNotNull(amexExpressCheckoutDetails.getExpirationMonth());
        assertNotNull(amexExpressCheckoutDetails.getExpirationYear());
        assertNotNull(amexExpressCheckoutDetails.getCardMemberNumber());
        assertNotNull(amexExpressCheckoutDetails.getCardMemberExpiryDate());
        assertNotNull(amexExpressCheckoutDetails.getImageUrl());
        assertNotNull(amexExpressCheckoutDetails.getSourceDescription());
    }

    @Test
    public void saleWithVenmoAccountNonce() {
        String venmoAccountNonce = Nonce.VenmoAccount;

        TransactionRequest request = new TransactionRequest()
            .merchantAccountId(FAKE_VENMO_ACCOUNT_MERCHANT_ACCOUNT_ID)
            .amount(SandboxValues.TransactionAmount.AUTHORIZE.amount)
            .paymentMethodNonce(venmoAccountNonce);

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        assertEquals(PaymentInstrumentType.VENMO_ACCOUNT, transaction.getPaymentInstrumentType());

        VenmoAccountDetails venmoAccountDetails = transaction.getVenmoAccountDetails();
        assertNotNull(venmoAccountDetails);

        assertNull(venmoAccountDetails.getToken());
        assertNotNull(venmoAccountDetails.getUsername());
        assertNotNull(venmoAccountDetails.getVenmoUserId());
        assertNotNull(venmoAccountDetails.getImageUrl());
        assertNotNull(venmoAccountDetails.getSourceDescription());
    }

    @Test
    public void saleWithVenmoAccountNonceAndProfileId() {
        String venmoAccountNonce = Nonce.VenmoAccount;

        TransactionRequest request = new TransactionRequest()
            .merchantAccountId(FAKE_VENMO_ACCOUNT_MERCHANT_ACCOUNT_ID)
            .amount(SandboxValues.TransactionAmount.AUTHORIZE.amount)
            .paymentMethodNonce(venmoAccountNonce)
            .options()
                .venmo()
                    .profileId("integration_venmo_merchant_public_id")
                    .done()
                .done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
    }

    @Test
    public void saleWithThreeDSecureOptionRequired() {
        TransactionRequest request = new TransactionRequest().
            merchantAccountId(THREE_D_SECURE_MERCHANT_ACCOUNT_ID).
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            options().
                threeDSecure().
                    required(true).
                    done().
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());

        Transaction transaction = result.getTransaction();
        assertEquals(Transaction.Status.GATEWAY_REJECTED, transaction.getStatus());
        assertEquals(Transaction.GatewayRejectionReason.THREE_D_SECURE, transaction.getGatewayRejectionReason());
    }

    @Test
    public void saleWithThreeDSecureToken() {
        String threeDSecureToken = TestHelper.createTest3DS(gateway, THREE_D_SECURE_MERCHANT_ACCOUNT_ID, new ThreeDSecureRequestForTests().
            number(CreditCardNumber.VISA.number).
            expirationMonth("05").
            expirationYear("2009")
        );

        TransactionRequest request = new TransactionRequest().
            merchantAccountId(THREE_D_SECURE_MERCHANT_ACCOUNT_ID).
            amount(TransactionAmount.AUTHORIZE.amount).
            threeDSecureToken(threeDSecureToken).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        assertEquals(Transaction.Status.AUTHORIZED, transaction.getStatus());
    }

    @Test
    public void saleErrorWithNullThreeDSecureToken() {
        String threeDSecureToken = null;

        TransactionRequest request = new TransactionRequest().
            merchantAccountId(THREE_D_SECURE_MERCHANT_ACCOUNT_ID).
            amount(TransactionAmount.AUTHORIZE.amount).
            threeDSecureToken(threeDSecureToken).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());
        assertEquals(ValidationErrorCode.TRANSACTION_THREE_D_SECURE_TOKEN_IS_INVALID,
                result.getErrors().forObject("transaction").onField("threeDSecureToken").get(0).getCode());
    }

    @Test
    public void saleErrorWithMismatchedThreeDSecureData() {
        String threeDSecureToken = TestHelper.createTest3DS(gateway, THREE_D_SECURE_MERCHANT_ACCOUNT_ID, new ThreeDSecureRequestForTests().
            number(CreditCardNumber.VISA.number).
            expirationMonth("05").
            expirationYear("2009")
        );

        TransactionRequest request = new TransactionRequest().
            merchantAccountId(THREE_D_SECURE_MERCHANT_ACCOUNT_ID).
            amount(TransactionAmount.AUTHORIZE.amount).
            threeDSecureToken(threeDSecureToken).
            creditCard().
                number(CreditCardNumber.MASTER_CARD.number).
                expirationDate("05/2009").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());
        assertEquals(ValidationErrorCode.TRANSACTION_THREE_D_SECURE_TRANSACTION_DATA_DOESNT_MATCH_VERIFY,
                result.getErrors().forObject("transaction").onField("threeDSecureToken").get(0).getCode());
    }

    @Test
    public void saleWithThreeDSecurePassThru() {
        TransactionRequest request = new TransactionRequest().
            merchantAccountId(THREE_D_SECURE_MERCHANT_ACCOUNT_ID).
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            threeDSecurePassThru().
                eciFlag("02").
                cavv("some_cavv").
                xid("some_xid").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());

        Transaction transaction = result.getTarget();
        assertEquals(Transaction.Status.AUTHORIZED, transaction.getStatus());
    }

    @Test
    public void saleWithThreeDSecureAdyenPassThru() {
        TransactionRequest request = new TransactionRequest().
            merchantAccountId(ADYEN_MERCHANT_ACCOUNT_ID).
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("10/2020").
                cvv("737").
                done().
            threeDSecurePassThru().
                eciFlag("02").
                cavv("some_cavv").
                xid("some_xid").
                authenticationResponse("Y").
                directoryResponse("Y").
                cavvAlgorithm("2").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());

        Transaction transaction = result.getTarget();
        assertEquals(Transaction.Status.AUTHORIZED, transaction.getStatus());
    }

    @Test
    public void saleWithThreeDSecureAdyenPassThruMissingAuthenticationResponse() {
        TransactionRequest request = new TransactionRequest().
            merchantAccountId(ADYEN_MERCHANT_ACCOUNT_ID).
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("10/2020").
                cvv("737").
                done().
            threeDSecurePassThru().
                eciFlag("02").
                cavv("some_cavv").
                xid("some_xid").
                authenticationResponse("").
                directoryResponse("Y").
                cavvAlgorithm("2").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());

        assertEquals(ValidationErrorCode.TRANSACTION_THREE_D_SECURE_PASS_THRU_AUTHENTICATION_RESPONSE_IS_INVALID,
                result.getErrors().forObject("transaction").forObject("threeDSecurePassThru").onField("authenticationResponse").get(0).getCode());
    }

    @Test
    public void saleWithThreeDSecureAdyenPassThruMissingDirectoryResponse() {
        TransactionRequest request = new TransactionRequest().
            merchantAccountId(ADYEN_MERCHANT_ACCOUNT_ID).
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("10/2020").
                cvv("737").
                done().
            threeDSecurePassThru().
                eciFlag("02").
                cavv("some_cavv").
                xid("some_xid").
                authenticationResponse("Y").
                directoryResponse("").
                cavvAlgorithm("2").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());

        assertEquals(ValidationErrorCode.TRANSACTION_THREE_D_SECURE_PASS_THRU_DIRECTORY_RESPONSE_IS_INVALID,
                result.getErrors().forObject("transaction").forObject("threeDSecurePassThru").onField("directoryResponse").get(0).getCode());
    }

    public void saleWithThreeDSecureAdyenPassThruMissingCavvAlgorithm() {
        TransactionRequest request = new TransactionRequest().
            merchantAccountId(ADYEN_MERCHANT_ACCOUNT_ID).
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("10/2020").
                cvv("737").
                done().
            threeDSecurePassThru().
                eciFlag("02").
                cavv("some_cavv").
                xid("some_xid").
                authenticationResponse("Y").
                directoryResponse("Y").
                cavvAlgorithm("").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());

        assertEquals(ValidationErrorCode.TRANSACTION_THREE_D_SECURE_PASS_THRU_CAVV_ALGORITHM_IS_INVALID,
                result.getErrors().forObject("transaction").forObject("threeDSecurePassThru").onField("cavvAlgorithm").get(0).getCode());
    }

    @Test
    public void saleErrorWithThreeDSecurePassThruWhenMerchantAccountDoesNotSupportCardType() {
        TransactionRequest request = new TransactionRequest().
            merchantAccountId("heartland_ma").
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            threeDSecurePassThru().
                eciFlag("02").
                cavv("some_cavv").
                xid("some_xid").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());

        assertEquals(ValidationErrorCode.TRANSACTION_THREE_D_SECURE_PASS_THRU_MERCHANT_ACCOUNT_DOES_NOT_SUPPORT_CARD_TYPE,
                result.getErrors().forObject("transaction").onField("merchantAccountId").get(0).getCode());
    }

    @Test
    public void saleErrorWithMissingThreeDSecurePassThruEciFlag() {
        TransactionRequest request = new TransactionRequest().
            merchantAccountId(THREE_D_SECURE_MERCHANT_ACCOUNT_ID).
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            threeDSecurePassThru().
                eciFlag("").
                cavv("some_cavv").
                xid("some_xid").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());

        assertEquals(ValidationErrorCode.TRANSACTION_THREE_D_SECURE_PASS_THRU_ECI_FLAG_IS_REQUIRED,
                result.getErrors().forObject("transaction").forObject("threeDSecurePassThru").onField("eciFlag").get(0).getCode());
    }

    @Test
    public void saleErrorWithMissingThreeDSecurePassThruCavvOrXid() {
        TransactionRequest request = new TransactionRequest().
            merchantAccountId(THREE_D_SECURE_MERCHANT_ACCOUNT_ID).
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            threeDSecurePassThru().
                eciFlag("05").
                cavv("").
                xid("").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());

        assertEquals(ValidationErrorCode.TRANSACTION_THREE_D_SECURE_PASS_THRU_CAVV_IS_REQUIRED,
                result.getErrors().forObject("transaction").forObject("threeDSecurePassThru").onField("cavv").get(0).getCode());
    }

    @Test
    public void saleErrorWithInvalidThreeDSecurePassThruEciFlag() {
        TransactionRequest request = new TransactionRequest().
            merchantAccountId(THREE_D_SECURE_MERCHANT_ACCOUNT_ID).
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            threeDSecurePassThru().
                eciFlag("bad_eci_flag").
                cavv("some_cavv").
                xid("some_xid").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());

        assertEquals(ValidationErrorCode.TRANSACTION_THREE_D_SECURE_PASS_THRU_ECI_FLAG_IS_INVALID,
                result.getErrors().forObject("transaction").forObject("threeDSecurePassThru").onField("eciFlag").get(0).getCode());
    }

    @Test
    public void saleWithIdealPaymentId() {
        TransactionRequest request = new TransactionRequest()
            .merchantAccountId("ideal_merchant_account")
            .amount(SandboxValues.TransactionAmount.AUTHORIZE.amount)
            .paymentMethodNonce(TestHelper.generateValidIdealPaymentId(gateway))
            .orderId("ABC123")
            .options()
                .submitForSettlement(true)
                .done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());

        Transaction transaction = result.getTarget();
        assertEquals(PaymentInstrumentType.IDEAL_PAYMENT, transaction.getPaymentInstrumentType());
        assertEquals(Transaction.Status.SETTLED, transaction.getStatus());

        IdealPaymentDetails idealPaymentDetails = transaction.getIdealPaymentDetails();
        assertTrue(Pattern.matches("^idealpayment_\\w{6,}$", idealPaymentDetails.getIdealPaymentId()));
        assertTrue(Pattern.matches("^\\d{16,}$", idealPaymentDetails.getIdealTransactionId()));
        assertTrue(idealPaymentDetails.getImageUrl().startsWith("https://"));
        assertNotNull(idealPaymentDetails.getMaskedIban());
        assertNotNull(idealPaymentDetails.getBic());
    }

    @Test
    public void saleWithInvalidIdealPaymentId() {
        BigDecimal amount = new BigDecimal("3.00");

        TransactionRequest request = new TransactionRequest()
            .merchantAccountId("ideal_merchant_account")
            .amount(amount)
            .paymentMethodNonce(TestHelper.generateValidIdealPaymentId(gateway, amount))
            .options()
                .submitForSettlement(true)
                .done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());
        assertEquals(ValidationErrorCode.TRANSACTION_IDEAL_PAYMENT_NOT_COMPLETE,
                result.getErrors().forObject("transaction").onField("paymentMethodNonce").get(0).getCode());
    }

    @Test
    public void saleWithAmexRewards() {
        TransactionRequest request = new TransactionRequest().
            merchantAccountId(FAKE_AMEX_DIRECT_MERCHANT_ACCOUNT_ID).
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.AmexPayWithPoints.SUCCESS.number).
                expirationDate("12/2020").
                done()
            .options().
                submitForSettlement(true).
                amexRewards().
                    requestId("ABC123").
                    points("1000").
                    currencyAmount("10.00").
                    currencyIsoCode("USD").
                    done().
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        assertEquals(Transaction.Status.SUBMITTED_FOR_SETTLEMENT, transaction.getStatus());
    }

    @Test
    public void saleWithAmexRewardsSucceedsEvenIfCardIneligible() {
        TransactionRequest request = new TransactionRequest().
            merchantAccountId(FAKE_AMEX_DIRECT_MERCHANT_ACCOUNT_ID).
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.AmexPayWithPoints.INELIGIBLE_CARD.number).
                expirationDate("12/2020").
                done()
            .options().
                submitForSettlement(true).
                amexRewards().
                    requestId("ABC123").
                    points("1000").
                    currencyAmount("10.00").
                    currencyIsoCode("USD").
                    done().
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        assertEquals(Transaction.Status.SUBMITTED_FOR_SETTLEMENT, transaction.getStatus());
    }

    @Test
    public void saleWithAmexRewardsSucceedsEvenIfCardBalanceIsInsufficient() {
        TransactionRequest request = new TransactionRequest().
            merchantAccountId(FAKE_AMEX_DIRECT_MERCHANT_ACCOUNT_ID).
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.AmexPayWithPoints.INSUFFICIENT_POINTS.number).
                expirationDate("12/2020").
                done()
            .options().
                submitForSettlement(true).
                amexRewards().
                    requestId("ABC123").
                    points("1000").
                    currencyAmount("10.00").
                    currencyIsoCode("USD").
                    done().
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        assertEquals(Transaction.Status.SUBMITTED_FOR_SETTLEMENT, transaction.getStatus());
    }

    @Test
    public void submitForSettlementWithAmexRewards() {
        String nonce = TestHelper.generateOneTimePayPalNonce(gateway);

        TransactionRequest request = new TransactionRequest().
            merchantAccountId(FAKE_AMEX_DIRECT_MERCHANT_ACCOUNT_ID).
            amount(new BigDecimal("100.00")).
            creditCard().
                number(CreditCardNumber.AmexPayWithPoints.SUCCESS.number).
                expirationDate("12/2020").
                done()
            .options().
                amexRewards().
                    requestId("ABC123").
                    points("1000").
                    currencyAmount("10.00").
                    currencyIsoCode("USD").
                    done().
                done();

        Result<Transaction> saleResult = gateway.transaction().sale(request);

        assertTrue(saleResult.isSuccess());
        assertEquals(Transaction.Status.AUTHORIZED, saleResult.getTarget().getStatus());

        Result<Transaction> submitForSettlementResult = gateway.transaction().submitForSettlement(saleResult.getTarget().getId());
        assertTrue(submitForSettlementResult.isSuccess());
        assertEquals(Transaction.Status.SUBMITTED_FOR_SETTLEMENT, submitForSettlementResult.getTarget().getStatus());
    }

    @Test
    public void submitForSettlementWithAmexRewardsSucceedsEvenIfCardIsIneligible() {
        String nonce = TestHelper.generateOneTimePayPalNonce(gateway);

        TransactionRequest request = new TransactionRequest().
            merchantAccountId(FAKE_AMEX_DIRECT_MERCHANT_ACCOUNT_ID).
            amount(new BigDecimal("100.00")).
            creditCard().
                number(CreditCardNumber.AmexPayWithPoints.INELIGIBLE_CARD.number).
                expirationDate("12/2020").
                done()
            .options().
                amexRewards().
                    requestId("ABC123").
                    points("1000").
                    currencyAmount("10.00").
                    currencyIsoCode("USD").
                    done().
                done();

        Result<Transaction> saleResult = gateway.transaction().sale(request);

        assertTrue(saleResult.isSuccess());
        assertEquals(Transaction.Status.AUTHORIZED, saleResult.getTarget().getStatus());

        Result<Transaction> submitForSettlementResult = gateway.transaction().submitForSettlement(saleResult.getTarget().getId());
        assertTrue(submitForSettlementResult.isSuccess());
        assertEquals(Transaction.Status.SUBMITTED_FOR_SETTLEMENT, submitForSettlementResult.getTarget().getStatus());
    }

    @Test
    public void submitForSettlementWithAmexRewardsSucceedsEvenIfCardBalanceIsInsufficient() {
        String nonce = TestHelper.generateOneTimePayPalNonce(gateway);

        TransactionRequest request = new TransactionRequest().
            merchantAccountId(FAKE_AMEX_DIRECT_MERCHANT_ACCOUNT_ID).
            amount(new BigDecimal("100.00")).
            creditCard().
                number(CreditCardNumber.AmexPayWithPoints.INSUFFICIENT_POINTS.number).
                expirationDate("12/2020").
                done()
            .options().
                amexRewards().
                    requestId("ABC123").
                    points("1000").
                    currencyAmount("10.00").
                    currencyIsoCode("USD").
                    done().
                done();

        Result<Transaction> saleResult = gateway.transaction().sale(request);

        assertTrue(saleResult.isSuccess());
        assertEquals(Transaction.Status.AUTHORIZED, saleResult.getTarget().getStatus());

        Result<Transaction> submitForSettlementResult = gateway.transaction().submitForSettlement(saleResult.getTarget().getId());
        assertTrue(submitForSettlementResult.isSuccess());
        assertEquals(Transaction.Status.SUBMITTED_FOR_SETTLEMENT, submitForSettlementResult.getTarget().getStatus());
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
        assertFalse(result.isSuccess());
        Transaction transaction = result.getTransaction();

        assertEquals(new BigDecimal("2000.00"), transaction.getAmount());
        assertEquals(Transaction.Status.PROCESSOR_DECLINED, transaction.getStatus());
        assertEquals("2000", transaction.getProcessorResponseCode());
        assertNotNull(transaction.getProcessorResponseText());
        assertEquals("Do Not Honor", transaction.getProcessorResponseText());
        assertEquals(ProcessorResponseType.SOFT_DECLINED, transaction.getProcessorResponseType());
        assertEquals("2000 : Do Not Honor", transaction.getAdditionalProcessorResponse());

        CreditCard creditCard = transaction.getCreditCard();
        assertEquals("411111", creditCard.getBin());
        assertEquals("1111", creditCard.getLast4());
        assertEquals("05", creditCard.getExpirationMonth());
        assertEquals("2009", creditCard.getExpirationYear());
        assertEquals("05/2009", creditCard.getExpirationDate());
    }

    @Test
    public void saleHardDeclined() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.HARD_DECLINE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());
        Transaction transaction = result.getTransaction();

        assertEquals(new BigDecimal("2015.00"), transaction.getAmount());
        assertEquals(Transaction.Status.PROCESSOR_DECLINED, transaction.getStatus());
        assertEquals("2015", transaction.getProcessorResponseCode());
        assertNotNull(transaction.getProcessorResponseText());
        assertEquals("Transaction Not Allowed", transaction.getProcessorResponseText());
        assertEquals(ProcessorResponseType.HARD_DECLINED, transaction.getProcessorResponseType());
        assertEquals("2015 : Transaction Not Allowed", transaction.getAdditionalProcessorResponse());

        CreditCard creditCard = transaction.getCreditCard();
        assertEquals("411111", creditCard.getBin());
        assertEquals("1111", creditCard.getLast4());
        assertEquals("05", creditCard.getExpirationMonth());
        assertEquals("2009", creditCard.getExpirationYear());
        assertEquals("05/2009", creditCard.getExpirationDate());
    }

    @Test
    public void saleWithFraudCardIsDeclined() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.FRAUD.number).
                expirationDate("05/2016").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());
        Transaction transaction = result.getTransaction();

        assertEquals(Transaction.Status.GATEWAY_REJECTED, transaction.getStatus());
        assertEquals(Transaction.GatewayRejectionReason.FRAUD, transaction.getGatewayRejectionReason());
    }

    @Test
    public void saleWithSecurityParams() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            deviceSessionId("abc123").
            fraudMerchantId("456").
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
    }

    @Test
    public void saleWithRiskDataParam() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            riskData().
                customerBrowser("IE6").
                customerIP("192.168.0.1").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
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
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        Map<String, String> expected = new HashMap<String, String>();
        expected.put("store_me", "custom value");
        expected.put("another_stored_field", "custom value2");

        assertEquals(expected, transaction.getCustomFields());
    }

    @Test
    public void saleReturnsCreditCardPaymentInstrumentType() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done();
        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        assertEquals(
            PaymentInstrumentType.CREDIT_CARD,
            transaction.getPaymentInstrumentType()
        );
    }

    @Test
    public void saleWithRecurringFlag() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            recurring(true).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done();
        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();
        assertTrue(transaction.getRecurring());
    }

    @Test
    public void saleWithTransactionSourceAsRecurringFirst() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            transactionSource("recurring_first").
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done();
        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();
        assertTrue(transaction.getRecurring());
    }

    @Test
    public void saleWithTransactionSourceAsRecurring() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            transactionSource("recurring").
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done();
        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();
        assertTrue(transaction.getRecurring());
    }

    @Test
    public void saleWithTransactionSourceAsMerchant() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            transactionSource("merchant").
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done();
        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();
        assertFalse(transaction.getRecurring());
    }

    @Test
    public void saleWithTransactionSourceAsMoto() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            transactionSource("moto").
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done();
        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();
        assertFalse(transaction.getRecurring());
    }

    @Test
    public void saleWithTransactionSourceInvalid() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            transactionSource("invalid_value").
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done();
        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());
        assertEquals(ValidationErrorCode.TRANSACTION_TRANSACTION_SOURCE_IS_INVALID,
            result.getErrors().forObject("transaction").onField("transactionSource").get(0).getCode());
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
        assertFalse(result.isSuccess());

        assertEquals(ValidationErrorCode.ADDRESS_COUNTRY_NAME_IS_NOT_ACCEPTED,
                result.getErrors().forObject("transaction").forObject("billing").onField("countryName").get(0).getCode());
        assertEquals(ValidationErrorCode.ADDRESS_COUNTRY_CODE_ALPHA2_IS_NOT_ACCEPTED,
                result.getErrors().forObject("transaction").forObject("billing").onField("countryCodeAlpha2").get(0).getCode());
        assertEquals(ValidationErrorCode.ADDRESS_COUNTRY_CODE_ALPHA3_IS_NOT_ACCEPTED,
                result.getErrors().forObject("transaction").forObject("billing").onField("countryCodeAlpha3").get(0).getCode());
        assertEquals(ValidationErrorCode.ADDRESS_COUNTRY_CODE_NUMERIC_IS_NOT_ACCEPTED,
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
        assertFalse(result.isSuccess());

        assertEquals(ValidationErrorCode.TRANSACTION_CUSTOM_FIELD_IS_INVALID,
                result.getErrors().forObject("transaction").onField("customFields").get(0).getCode());
    }

    @Test
    public void saleWithMultipleValidationErrorsOnSameField() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest creditCardRequest = new CreditCardRequest().
            customerId(customer.getId()).
            cvv("123").
            number("5105105105105100").
            expirationDate("05/12");
        CreditCard creditCard = gateway.creditCard().create(creditCardRequest).getTarget();
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            paymentMethodToken(creditCard.getToken()).
            shipsFromPostalCode("1234$$567890").
            creditCard().
                number(CreditCardNumber.VISA.number).
                cvv("321").
                expirationDate("04/2009").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());
        List<ValidationError> errors = result.getErrors().forObject("transaction").onField("shipsFromPostalCode");

        assertNull(result.getTransaction());
        assertNull(result.getCreditCardVerification());
        assertEquals(2, errors.size());

        List<ValidationErrorCode> validationErrorCodes = new ArrayList<ValidationErrorCode>();
        validationErrorCodes.add(errors.get(0).getCode());
        validationErrorCodes.add(errors.get(1).getCode());
        assertTrue(validationErrorCodes.contains(ValidationErrorCode.TRANSACTION_SHIPS_FROM_POSTAL_CODE_INVALID_CHARACTERS));
        assertTrue(validationErrorCodes.contains(ValidationErrorCode.TRANSACTION_SHIPS_FROM_POSTAL_CODE_IS_TOO_LONG));
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
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        assertEquals(creditCard.getToken(), transaction.getCreditCard().getToken());
        assertEquals("510510", transaction.getCreditCard().getBin());
        assertEquals("05/2012", transaction.getCreditCard().getExpirationDate());
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
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        assertEquals(creditCard.getToken(), transaction.getCreditCard().getToken());
        assertEquals("510510", transaction.getCreditCard().getBin());
        assertEquals("05/2012", transaction.getCreditCard().getExpirationDate());
        assertEquals("S", transaction.getCvvResponseCode());
    }

    @Test
    public void saleWithPaymentMethodTokenAndNonce() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest creditCardRequest = new CreditCardRequest().
            customerId(customer.getId()).
            number("5105105105105100").
            expirationDate("05/12");
        CreditCard creditCard = gateway.creditCard().create(creditCardRequest).getTarget();

        CreditCardRequest cvvRequest = new CreditCardRequest().cvv("123");
        String nonce = TestHelper.generateNonceForCreditCard(gateway, cvvRequest, customer.getId(), false);

        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            paymentMethodToken(creditCard.getToken()).
            paymentMethodNonce(nonce);

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        assertEquals("M", transaction.getCvvResponseCode());
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
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        assertEquals(shippingAddress.getId(), transaction.getShippingAddress().getId());
        assertEquals("Carl", transaction.getShippingAddress().getFirstName());
    }

    @Test
    public void saleUsesBillingAddressFromVault() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();

        gateway.creditCard().create(new CreditCardRequest().
            customerId(customer.getId()).
            cvv("123").
            number("5105105105105100").
            expirationDate("05/12")).getTarget();

        Address billingAddress = gateway.address().create(customer.getId(),
                new AddressRequest().firstName("Carl")).getTarget();

        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            customerId(customer.getId()).
            billingAddressId(billingAddress.getId());

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        assertEquals(billingAddress.getId(), transaction.getBillingAddress().getId());
        assertEquals("Carl", transaction.getBillingAddress().getFirstName());
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
        assertFalse(result.isSuccess());
        assertNull(result.getTarget());
        assertEquals(ValidationErrorCode.TRANSACTION_AMOUNT_IS_REQUIRED, result.getErrors().forObject("transaction").onField("amount").get(0).getCode());

        Map<String, String> parameters = result.getParameters();
        assertEquals(null, parameters.get("transaction[amount]"));
        assertEquals("05", parameters.get("transaction[credit_card][expiration_month]"));
        assertEquals("2010", parameters.get("transaction[credit_card][expiration_year]"));
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
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        assertEquals(Transaction.Status.SUBMITTED_FOR_SETTLEMENT, transaction.getStatus());
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
                url("ebay.com").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        assertEquals("123*123456789012345678", transaction.getDescriptor().getName());
        assertEquals("3334445555", transaction.getDescriptor().getPhone());
        assertEquals("ebay.com", transaction.getDescriptor().getUrl());
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
                url("12345678901234").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());

        assertEquals(ValidationErrorCode.DESCRIPTOR_NAME_FORMAT_IS_INVALID,
            result.getErrors().forObject("transaction").forObject("descriptor").onField("name").get(0).getCode());

        assertEquals(ValidationErrorCode.DESCRIPTOR_PHONE_FORMAT_IS_INVALID,
            result.getErrors().forObject("transaction").forObject("descriptor").onField("phone").get(0).getCode());

        assertEquals(ValidationErrorCode.DESCRIPTOR_URL_FORMAT_IS_INVALID,
                result.getErrors().forObject("transaction").forObject("descriptor").onField("url").get(0).getCode());
    }

    @Test
    public void saleWithLodgingIndustryData() {
        TransactionRequest request = new TransactionRequest().
                amount(TransactionAmount.AUTHORIZE.amount).
                creditCard().
                    number(CreditCardNumber.VISA.number).
                    expirationDate("05/2009").
                    done().
                industry().
                    industryType(Transaction.IndustryType.LODGING).
                    data().
                        folioNumber("aaa").
                        checkInDate("2014-07-07").
                        checkOutDate("2014-08-08").
                        roomRate("2.00").
                        done().
                    done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
    }

    @Test
    public void saleWithIndustryDataValidation() {
        TransactionRequest request = new TransactionRequest().
                amount(TransactionAmount.AUTHORIZE.amount).
                creditCard().
                    number(CreditCardNumber.VISA.number).
                    expirationDate("05/2009").
                    done().
                industry().
                    industryType(Transaction.IndustryType.LODGING).
                    data().
                        folioNumber("aaa").
                        checkInDate("2014-07-07").
                        checkOutDate("2014-06-06").
                        done().
                    done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());

        assertEquals(ValidationErrorCode.INDUSTRY_DATA_LODGING_CHECK_OUT_DATE_MUST_FOLLOW_CHECK_IN_DATE,
                result.getErrors().forObject("transaction").forObject("industry").onField("checkOutDate").get(0).getCode());
    }

    @Test
    public void saleWithTravelCruiseIndustryData() {
        TransactionRequest request = new TransactionRequest().
                amount(TransactionAmount.AUTHORIZE.amount).
                creditCard().
                    number(CreditCardNumber.VISA.number).
                    expirationDate("05/2009").
                    done().
                industry().
                    industryType(Transaction.IndustryType.TRAVEL_CRUISE).
                    data().
                        travelPackage("flight").
                        departureDate("2014-07-07").
                        lodgingCheckInDate("2014-07-07").
                        lodgingCheckOutDate("2014-08-08").
                        lodgingName("Disney").
                        done().
                    done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
    }

    @Test
    public void saleWithTravelCruiseIndustryDataValidation() {
        TransactionRequest request = new TransactionRequest().
                amount(TransactionAmount.AUTHORIZE.amount).
                creditCard().
                    number(CreditCardNumber.VISA.number).
                    expirationDate("05/2009").
                    done().
                industry().
                    industryType(Transaction.IndustryType.TRAVEL_CRUISE).
                    data().
                        travelPackage("plane").
                        departureDate("2014-07-07").
                        lodgingCheckInDate("2014-07-07").
                        lodgingCheckOutDate("2014-08-08").
                        lodgingName("Disney").
                        done().
                    done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());

        assertEquals(ValidationErrorCode.INDUSTRY_DATA_TRAVEL_CRUISE_TRAVEL_PACKAGE_IS_INVALID,
                result.getErrors().forObject("transaction").forObject("industry").onField("travelPackage").get(0).getCode());
    }

    @Test
    public void saleWithTravelFlightIndustryData() {
        Calendar issuedDate = Calendar.getInstance();
        issuedDate.setTimeZone(TimeZone.getTimeZone("US/Mountain"));
        issuedDate.add(Calendar.MONTH, 1);
        issuedDate.add(Calendar.DAY_OF_MONTH, 1);

        Calendar legDate1 = Calendar.getInstance();
        legDate1.setTimeZone(TimeZone.getTimeZone("US/Mountain"));
        legDate1.add(Calendar.MONTH, 1);
        legDate1.add(Calendar.DAY_OF_MONTH, 2);

        Calendar legDate2 = Calendar.getInstance();
        legDate2.setTimeZone(TimeZone.getTimeZone("US/Mountain"));
        legDate2.add(Calendar.MONTH, 1);
        legDate2.add(Calendar.DAY_OF_MONTH, 3);

        TransactionRequest request = new TransactionRequest().
                amount(TransactionAmount.AUTHORIZE.amount).
                paymentMethodNonce(Nonce.PayPalOneTimePayment).
                options().
                    submitForSettlement(true).
                    done().
                industry().
                    industryType(Transaction.IndustryType.TRAVEL_FLIGHT).
                    data().
                        passengerFirstName("John").
                        passengerLastName("Doe").
                        passengerMiddleInitial("M").
                        passengerTitle("Mr.").
                        issuedDate(issuedDate).
                        travelAgencyName("Expedia").
                        travelAgencyCode("12345678").
                        ticketNumber("ticket-number").
                        issuingCarrierCode("AA").
                        customerCode("customer-code").
                        fareAmount(new BigDecimal("70.00")).
                        feeAmount(new BigDecimal("10.00")).
                        taxAmount(new BigDecimal("20.00")).
                        restrictedTicket(false).
                        leg().
                            conjunctionTicket("CJ0001").
                            exchangeTicket("ET0001").
                            couponNumber("1").
                            serviceClass("Y").
                            carrierCode("AA").
                            fareBasisCode("W").
                            flightNumber("AA100").
                            departureDate(legDate1).
                            departureAirportCode("MDW").
                            departureTime("08:00").
                            arrivalAirportCode("ATX").
                            arrivalTime("10:00").
                            stopoverPermitted(false).
                            fareAmount(new BigDecimal("35.00")).
                            feeAmount(new BigDecimal("5.00")).
                            taxAmount(new BigDecimal("10.00")).
                            endorsementOrRestrictions("NOT REFUNDABLE").
                            done().
                        leg().
                            conjunctionTicket("CJ0002").
                            exchangeTicket("ET0002").
                            couponNumber("1").
                            serviceClass("Y").
                            carrierCode("AA").
                            fareBasisCode("W").
                            flightNumber("AA200").
                            departureDate(legDate2).
                            departureAirportCode("ATX").
                            departureTime("12:00").
                            arrivalAirportCode("MDW").
                            arrivalTime("14:00").
                            stopoverPermitted(false).
                            fareAmount(new BigDecimal("35.00")).
                            feeAmount(new BigDecimal("5.00")).
                            taxAmount(new BigDecimal("10.00")).
                            endorsementOrRestrictions("NOT REFUNDABLE").
                            done().
                        done().
                    done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
    }

    @Test
    public void saleWithTravelFlightIndustryDataValidation() {
        TransactionRequest request = new TransactionRequest().
                amount(TransactionAmount.AUTHORIZE.amount).
                paymentMethodNonce(Nonce.PayPalOneTimePayment).
                options().
                    submitForSettlement(true).
                    done().
                industry().
                    industryType(Transaction.IndustryType.TRAVEL_FLIGHT).
                    data().
                        fareAmount(new BigDecimal("-1.23")).
                        leg().
                            fareAmount(new BigDecimal("-1.23")).
                            done().
                        done().
                    done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());

        assertEquals(ValidationErrorCode.INDUSTRY_DATA_TRAVEL_FLIGHT_FARE_AMOUNT_CANNOT_BE_NEGATIVE,
                result.getErrors().forObject("transaction").forObject("industry").onField("fareAmount").get(0).getCode());
        assertEquals(ValidationErrorCode.INDUSTRY_DATA_LEG_TRAVEL_FLIGHT_FARE_AMOUNT_CANNOT_BE_NEGATIVE,
                result.getErrors().forObject("transaction").forObject("industry").forObject("legs").forObject("index_0").onField("fareAmount").get(0).getCode());
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
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        assertEquals(new BigDecimal("10.00"), transaction.getTaxAmount());
        assertTrue(transaction.isTaxExempt());
        assertEquals("12345", transaction.getPurchaseOrderNumber());
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
        assertFalse(result.isSuccess());

        assertEquals(ValidationErrorCode.TRANSACTION_PURCHASE_ORDER_NUMBER_IS_TOO_LONG,
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
        assertFalse(result.isSuccess());

        assertEquals(ValidationErrorCode.TRANSACTION_PURCHASE_ORDER_NUMBER_IS_INVALID,
            result.getErrors().forObject("transaction").onField("purchaseOrderNumber").get(0).getCode());
    }

    @Test
    public void saleWithLevel3SummaryData() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            shippingAmount(new BigDecimal("1.00")).
            discountAmount(new BigDecimal("2.00")).
            shipsFromPostalCode("12345");

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        assertEquals(new BigDecimal("1.00"), transaction.getShippingAmount());
        assertEquals(new BigDecimal("2.00"), transaction.getDiscountAmount());
        assertEquals("12345", transaction.getShipsFromPostalCode());
    }

    @Test
    public void saleWithLevel3SummaryDataValidationErrorDiscountAmountCannotBeNegative() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            discountAmount(new BigDecimal("-2.00"));

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());

        assertEquals(ValidationErrorCode.TRANSACTION_DISCOUNT_AMOUNT_CANNOT_BE_NEGATIVE,
            result.getErrors().forObject("transaction").onField("discountAmount").get(0).getCode());
    }

    @Test
    public void saleWithLevel3SummaryDataValidationErrorDiscountAmountIsTooLarge() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            discountAmount(new BigDecimal("2147483648"));

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());

        assertEquals(ValidationErrorCode.TRANSACTION_DISCOUNT_AMOUNT_IS_TOO_LARGE,
            result.getErrors().forObject("transaction").onField("discountAmount").get(0).getCode());
    }

    @Test
    public void saleWithLevel3SummaryDataValidationErrorShippingAmountCannotBeNegative() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            shippingAmount(new BigDecimal("-2.00"));

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());

        assertEquals(ValidationErrorCode.TRANSACTION_SHIPPING_AMOUNT_CANNOT_BE_NEGATIVE,
            result.getErrors().forObject("transaction").onField("shippingAmount").get(0).getCode());
    }

    @Test
    public void saleWithLevel3SummaryDataValidationErrorShippingAmountIsTooLarge() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            shippingAmount(new BigDecimal("2147483648"));

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());

        assertEquals(ValidationErrorCode.TRANSACTION_SHIPPING_AMOUNT_IS_TOO_LARGE,
            result.getErrors().forObject("transaction").onField("shippingAmount").get(0).getCode());
    }

    @Test
    public void saleWithLevel3SummaryDataValidationErrorShipsFromPostalCodeIsTooLong() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            shipsFromPostalCode("1234567890");

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());

        assertEquals(ValidationErrorCode.TRANSACTION_SHIPS_FROM_POSTAL_CODE_IS_TOO_LONG,
            result.getErrors().forObject("transaction").onField("shipsFromPostalCode").get(0).getCode());
    }

    @Test
    public void saleWithLevel3SummaryDataValidationErrorShipsFromPostalCodeInvalidCharacters() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            shipsFromPostalCode("1$345");

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());

        assertEquals(ValidationErrorCode.TRANSACTION_SHIPS_FROM_POSTAL_CODE_INVALID_CHARACTERS,
            result.getErrors().forObject("transaction").onField("shipsFromPostalCode").get(0).getCode());
    }

    @Test
    public void saleWithVenmoSdkPaymentMethodCode() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            venmoSdkPaymentMethodCode(VenmoSdk.PaymentMethodCode.Visa.code);

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
        assertEquals("411111", result.getTarget().getCreditCard().getBin());
    }

    @Test
    public void saleWithVenmoSdkSession() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            options().
              venmoSdkSession(VenmoSdk.Session.Valid.value).
              done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
        assertFalse(result.getTarget().getCreditCard().isVenmoSdk());
    }

    @Test
    public void saleWithAdvancedFraudCheckingSkipped() {
        createAdvancedFraudMerchantGateway();
        TransactionRequest request = new TransactionRequest().
                amount(TransactionAmount.AUTHORIZE.amount).
                creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2016").
                done().
                options().
                skipAdvancedFraudChecking(true).
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
        assertNull(result.getTarget().getRiskData());
    }

    @Test
    public void saleWithSkipAvsOptionSet() {
        TransactionRequest request = new TransactionRequest().
                amount(TransactionAmount.AUTHORIZE.amount).
                creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2019").
                done().
                options().
                skipAvs(true).
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
        assertNull(result.getTarget().getAvsErrorResponseCode());
        assertEquals("B", result.getTarget().getAvsStreetAddressResponseCode());
    }

    @Test
    public void saleWithSkipCvvOptionSet() {
        TransactionRequest request = new TransactionRequest().
                amount(TransactionAmount.AUTHORIZE.amount).
                creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2019").
                done().
                options().
                skipCvv(true).
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
        assertEquals("B", result.getTarget().getCvvResponseCode());
    }

    @Test
    public void saleWithLineItemsZero() {
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("45.15")).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());

        Transaction transaction = result.getTarget();

        List<TransactionLineItem> lineItems = transaction.getLineItems(gateway);
        assertEquals(0, lineItems.size());
    }

    @Test
    public void saleWithLineItemsSingleOnlyRequiredFields() {
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("35.05")).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            lineItem().
                quantity(new BigDecimal("1.0232")).
                name("Name #1").
                kind(TransactionLineItem.Kind.DEBIT).
                unitAmount(new BigDecimal("45.1232")).
                totalAmount(new BigDecimal("45.15")).
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());

        Transaction transaction = result.getTarget();

        List<TransactionLineItem> lineItems = transaction.getLineItems(gateway);
        assertEquals(1, lineItems.size());

        TransactionLineItem lineItem = lineItems.get(0);
        assertEquals(new BigDecimal("1.0232"), lineItem.getQuantity());
        assertEquals("Name #1", lineItem.getName());
        assertEquals(TransactionLineItem.Kind.DEBIT, lineItem.getKind());
        assertEquals(new BigDecimal("45.1232"), lineItem.getUnitAmount());
        assertEquals(new BigDecimal("45.15"), lineItem.getTotalAmount());
    }

    @Test
    public void saleWithLineItemsSingleZeroAmountFields() {
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("35.05")).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            lineItem().
                quantity(new BigDecimal("1.0232")).
                name("Name #1").
                kind(TransactionLineItem.Kind.DEBIT).
                unitAmount(new BigDecimal("45.1232")).
                totalAmount(new BigDecimal("45.15")).
                discountAmount(new BigDecimal("0")).
                taxAmount(new BigDecimal("0")).
                unitTaxAmount(new BigDecimal("0")).
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());

        Transaction transaction = result.getTarget();

        List<TransactionLineItem> lineItems = transaction.getLineItems(gateway);
        assertEquals(1, lineItems.size());

        TransactionLineItem lineItem = lineItems.get(0);
        assertEquals(new BigDecimal("1.0232"), lineItem.getQuantity());
        assertEquals("Name #1", lineItem.getName());
        assertEquals(TransactionLineItem.Kind.DEBIT, lineItem.getKind());
        assertEquals(new BigDecimal("45.1232"), lineItem.getUnitAmount());
        assertEquals(new BigDecimal("45.15"), lineItem.getTotalAmount());
        assertEquals(new BigDecimal("0.00"), lineItem.getDiscountAmount());
        assertEquals(new BigDecimal("0.00"), lineItem.getTaxAmount());
        assertEquals(new BigDecimal("0.00"), lineItem.getUnitTaxAmount());
    }

    @Test
    public void saleWithLineItemsSingle() {
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("45.15")).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            lineItem().
                quantity(new BigDecimal("1.0232")).
                name("Name #1").
                description("Description #1").
                kind(TransactionLineItem.Kind.DEBIT).
                unitAmount(new BigDecimal("45.1232")).
                unitTaxAmount(new BigDecimal("1.23")).
                unitOfMeasure("gallon").
                discountAmount(new BigDecimal("1.02")).
                taxAmount(new BigDecimal("4.55")).
                totalAmount(new BigDecimal("45.15")).
                productCode("23434").
                commodityCode("9SAASSD8724").
                url("https://example.com/products/23434").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());

        Transaction transaction = result.getTarget();

        List<TransactionLineItem> lineItems = transaction.getLineItems(gateway);
        assertEquals(1, lineItems.size());

        TransactionLineItem lineItem = lineItems.get(0);
        assertEquals(new BigDecimal("1.0232"), lineItem.getQuantity());
        assertEquals("Name #1", lineItem.getName());
        assertEquals("Description #1", lineItem.getDescription());
        assertEquals(TransactionLineItem.Kind.DEBIT, lineItem.getKind());
        assertEquals(new BigDecimal("45.1232"), lineItem.getUnitAmount());
        assertEquals(new BigDecimal("1.23"), lineItem.getUnitTaxAmount());
        assertEquals("gallon", lineItem.getUnitOfMeasure());
        assertEquals(new BigDecimal("1.02"), lineItem.getDiscountAmount());
        assertEquals(new BigDecimal("45.15"), lineItem.getTotalAmount());
        assertEquals("23434", lineItem.getProductCode());
        assertEquals("9SAASSD8724", lineItem.getCommodityCode());
        assertEquals("https://example.com/products/23434", lineItem.getUrl());
        assertEquals(new BigDecimal("4.55"), lineItem.getTaxAmount());
    }

    @Test
    public void saleWithLineItemsMultiple() {
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("35.05")).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            lineItem().
                quantity(new BigDecimal("1.0232")).
                name("Name #1").
                description("Description #1").
                kind(TransactionLineItem.Kind.DEBIT).
                unitAmount(new BigDecimal("45.1232")).
                unitOfMeasure("gallon").
                discountAmount(new BigDecimal("1.02")).
                taxAmount(new BigDecimal("4.55")).
                totalAmount(new BigDecimal("45.15")).
                productCode("23434").
                commodityCode("9SAASSD8724").
                done().
            lineItem().
                quantity(new BigDecimal("2.02")).
                name("Name #2").
                description("Description #2").
                kind(TransactionLineItem.Kind.CREDIT).
                unitAmount(new BigDecimal("5")).
                unitOfMeasure("gallon").
                taxAmount(new BigDecimal("4.55")).
                totalAmount(new BigDecimal("45.15")).
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());

        Transaction transaction = result.getTarget();

        List<TransactionLineItem> lineItems = transaction.getLineItems(gateway);
        assertEquals(2, lineItems.size());

        TransactionLineItem lineItem1 = null;
        for (TransactionLineItem lineItem : lineItems) {
            if (lineItem.getName().equals("Name #1")) {
                lineItem1 = lineItem;
                break;
            }
        }
        if (lineItem1 == null) {
            fail("TransactionLineItem with name \"Name #1\" not returned.");
        }
        assertEquals(new BigDecimal("1.0232"), lineItem1.getQuantity());
        assertEquals("Name #1", lineItem1.getName());
        assertEquals("Description #1", lineItem1.getDescription());
        assertEquals(TransactionLineItem.Kind.DEBIT, lineItem1.getKind());
        assertEquals(new BigDecimal("45.1232"), lineItem1.getUnitAmount());
        assertEquals("gallon", lineItem1.getUnitOfMeasure());
        assertEquals(new BigDecimal("1.02"), lineItem1.getDiscountAmount());
        assertEquals(new BigDecimal("45.15"), lineItem1.getTotalAmount());
        assertEquals("23434", lineItem1.getProductCode());
        assertEquals("9SAASSD8724", lineItem1.getCommodityCode());
        assertEquals(new BigDecimal("4.55"), lineItem1.getTaxAmount());

        TransactionLineItem lineItem2 = null;
        for (TransactionLineItem lineItem : lineItems) {
            if (lineItem.getName().equals("Name #2")) {
                lineItem2 = lineItem;
                break;
            }
        }
        if (lineItem2 == null) {
            fail("TransactionLineItem with name \"Name #2\" not returned.");
        }
        assertEquals(new BigDecimal("2.02"), lineItem2.getQuantity());
        assertEquals("Name #2", lineItem2.getName());
        assertEquals("Description #2", lineItem2.getDescription());
        assertEquals(TransactionLineItem.Kind.CREDIT, lineItem2.getKind());
        assertEquals(new BigDecimal("5"), lineItem2.getUnitAmount());
        assertEquals("gallon", lineItem2.getUnitOfMeasure());
        assertEquals(new BigDecimal("45.15"), lineItem2.getTotalAmount());
        assertEquals(null, lineItem2.getDiscountAmount());
        assertEquals(null, lineItem2.getProductCode());
        assertEquals(null, lineItem2.getCommodityCode());
        assertEquals(new BigDecimal("4.55"), lineItem2.getTaxAmount());
    }

    @Test
    public void saleWithLineItemsValidationErrorCommodityCodeIsTooLong() {
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("35.05")).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            lineItem().
                quantity(new BigDecimal("1.0232")).
                name("Name #1").
                kind(TransactionLineItem.Kind.DEBIT).
                unitAmount(new BigDecimal("45.1232")).
                unitOfMeasure("gallon").
                discountAmount(new BigDecimal("1.02")).
                taxAmount(new BigDecimal("4.55")).
                totalAmount(new BigDecimal("45.15")).
                productCode("23434").
                commodityCode("9SAASSD8724").
                done().
            lineItem().
                quantity(new BigDecimal("1.0232")).
                name("Name #2").
                kind(TransactionLineItem.Kind.DEBIT).
                unitAmount(new BigDecimal("45.1232")).
                unitOfMeasure("gallon").
                discountAmount(new BigDecimal("1.02")).
                taxAmount(new BigDecimal("4.55")).
                totalAmount(new BigDecimal("45.15")).
                productCode("23434").
                commodityCode("0123456789123").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());

        assertEquals(
            ValidationErrorCode.TRANSACTION_LINE_ITEM_COMMODITY_CODE_IS_TOO_LONG,
            result.getErrors().forObject("transaction").forObject("line_items").forObject("index_1").onField("commodity_code").get(0).getCode()
        );
    }

    @Test
    public void saleWithLineItemsValidationErrorDescriptionIsTooLong() {
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("35.05")).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            lineItem().
                quantity(new BigDecimal("1.0232")).
                name("Name #1").
                kind(TransactionLineItem.Kind.DEBIT).
                unitAmount(new BigDecimal("45.1232")).
                unitOfMeasure("gallon").
                discountAmount(new BigDecimal("1.02")).
                taxAmount(new BigDecimal("4.55")).
                totalAmount(new BigDecimal("45.15")).
                productCode("23434").
                commodityCode("9SAASSD8724").
                done().
            lineItem().
                quantity(new BigDecimal("1.0232")).
                name("Name #2").
                description("This is a line item description which is far too long. Like, way too long to be practical. We don't like how long this line item description is.").
                kind(TransactionLineItem.Kind.DEBIT).
                unitAmount(new BigDecimal("45.1232")).
                unitOfMeasure("gallon").
                discountAmount(new BigDecimal("1.02")).
                taxAmount(new BigDecimal("4.55")).
                totalAmount(new BigDecimal("45.15")).
                productCode("23434").
                commodityCode("9SAASSD8724").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());

        assertEquals(
            ValidationErrorCode.TRANSACTION_LINE_ITEM_DESCRIPTION_IS_TOO_LONG,
            result.getErrors().forObject("transaction").forObject("line_items").forObject("index_1").onField("description").get(0).getCode()
        );
    }

    @Test
    public void saleWithLineItemsValidationErrorDiscountAmountIsTooLarge() {
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("35.05")).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            lineItem().
                quantity(new BigDecimal("1.0232")).
                name("Name #1").
                kind(TransactionLineItem.Kind.DEBIT).
                unitAmount(new BigDecimal("45.1232")).
                unitOfMeasure("gallon").
                discountAmount(new BigDecimal("1.02")).
                taxAmount(new BigDecimal("4.55")).
                totalAmount(new BigDecimal("45.15")).
                productCode("23434").
                commodityCode("9SAASSD8724").
                done().
            lineItem().
                quantity(new BigDecimal("1.0232")).
                name("Name #2").
                kind(TransactionLineItem.Kind.DEBIT).
                unitAmount(new BigDecimal("45.1232")).
                unitOfMeasure("gallon").
                discountAmount(new BigDecimal("2147483648")).
                taxAmount(new BigDecimal("4.55")).
                totalAmount(new BigDecimal("45.15")).
                productCode("23434").
                commodityCode("9SAASSD8724").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());

        assertEquals(
            ValidationErrorCode.TRANSACTION_LINE_ITEM_DISCOUNT_AMOUNT_IS_TOO_LARGE,
            result.getErrors().forObject("transaction").forObject("line_items").forObject("index_1").onField("discountAmount").get(0).getCode()
        );
    }

    @Test
    public void saleWithLineItemsValidationErrorDiscountAmountCannotBeNegative() {
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("35.05")).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            lineItem().
                quantity(new BigDecimal("1.0232")).
                name("Name #1").
                kind(TransactionLineItem.Kind.DEBIT).
                unitAmount(new BigDecimal("45.1232")).
                unitOfMeasure("gallon").
                discountAmount(new BigDecimal("1.02")).
                taxAmount(new BigDecimal("4.55")).
                totalAmount(new BigDecimal("45.15")).
                productCode("23434").
                commodityCode("9SAASSD8724").
                done().
            lineItem().
                quantity(new BigDecimal("1.0232")).
                name("Name #2").
                kind(TransactionLineItem.Kind.DEBIT).
                unitAmount(new BigDecimal("45.1232")).
                unitOfMeasure("gallon").
                discountAmount(new BigDecimal("-2")).
                taxAmount(new BigDecimal("4.55")).
                totalAmount(new BigDecimal("45.15")).
                productCode("23434").
                commodityCode("9SAASSD8724").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());

        assertEquals(
            ValidationErrorCode.TRANSACTION_LINE_ITEM_DISCOUNT_AMOUNT_CANNOT_BE_NEGATIVE,
            result.getErrors().forObject("transaction").forObject("line_items").forObject("index_1").onField("discountAmount").get(0).getCode()
        );
    }

    @Test
    public void saleWithLineItemsValidationErrorTaxAmountIsTooLarge() {
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("35.05")).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            lineItem().
                quantity(new BigDecimal("1.0232")).
                name("Name #2").
                kind(TransactionLineItem.Kind.DEBIT).
                unitAmount(new BigDecimal("45.1232")).
                unitOfMeasure("gallon").
                discountAmount(new BigDecimal("1.02")).
                taxAmount(new BigDecimal("2147483648")).
                totalAmount(new BigDecimal("45.15")).
                productCode("23434").
                commodityCode("9SAASSD8724").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());

        assertEquals(
            ValidationErrorCode.TRANSACTION_LINE_ITEM_TAX_AMOUNT_IS_TOO_LARGE,
            result.getErrors().forObject("transaction").forObject("line_items").forObject("index_0").onField("taxAmount").get(0).getCode()
        );
    }

    @Test
    public void saleWithLineItemsValidationErrorTaxAmountCannotBeNegative() {
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("35.05")).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            lineItem().
                quantity(new BigDecimal("1.0232")).
                name("Name #1").
                kind(TransactionLineItem.Kind.DEBIT).
                unitAmount(new BigDecimal("45.1232")).
                unitOfMeasure("gallon").
                discountAmount(new BigDecimal("1.02")).
                taxAmount(new BigDecimal("-2")).
                totalAmount(new BigDecimal("45.15")).
                productCode("23434").
                commodityCode("9SAASSD8724").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());

        assertEquals(
            ValidationErrorCode.TRANSACTION_LINE_ITEM_TAX_AMOUNT_CANNOT_BE_NEGATIVE,
            result.getErrors().forObject("transaction").forObject("line_items").forObject("index_0").onField("taxAmount").get(0).getCode()
        );
    }

    @Test
    public void saleWithLineItemsValidationErrorKindIsRequired() {
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("35.05")).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            lineItem().
                quantity(new BigDecimal("1.0232")).
                name("Name #1").
                kind(TransactionLineItem.Kind.DEBIT).
                unitAmount(new BigDecimal("45.1232")).
                unitOfMeasure("gallon").
                discountAmount(new BigDecimal("1.02")).
                taxAmount(new BigDecimal("4.55")).
                totalAmount(new BigDecimal("45.15")).
                productCode("23434").
                commodityCode("9SAASSD8724").
                done().
            lineItem().
                quantity(new BigDecimal("1.0232")).
                name("Name #2").
                unitAmount(new BigDecimal("45.1232")).
                unitOfMeasure("gallon").
                discountAmount(new BigDecimal("1.02")).
                taxAmount(new BigDecimal("4.55")).
                totalAmount(new BigDecimal("45.15")).
                productCode("23434").
                commodityCode("9SAASSD8724").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());

        assertEquals(
            ValidationErrorCode.TRANSACTION_LINE_ITEM_KIND_IS_REQUIRED,
            result.getErrors().forObject("transaction").forObject("line_items").forObject("index_1").onField("kind").get(0).getCode()
        );
    }

    @Test
    public void saleWithLineItemsValidationErrorNameIsRequired() {
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("35.05")).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            lineItem().
                quantity(new BigDecimal("1.0232")).
                name("Name #1").
                kind(TransactionLineItem.Kind.DEBIT).
                unitAmount(new BigDecimal("45.1232")).
                unitOfMeasure("gallon").
                discountAmount(new BigDecimal("1.02")).
                taxAmount(new BigDecimal("4.55")).
                totalAmount(new BigDecimal("45.15")).
                productCode("23434").
                commodityCode("9SAASSD8724").
                done().
            lineItem().
                quantity(new BigDecimal("1.0232")).
                kind(TransactionLineItem.Kind.DEBIT).
                unitAmount(new BigDecimal("45.1232")).
                unitOfMeasure("gallon").
                discountAmount(new BigDecimal("1.02")).
                taxAmount(new BigDecimal("4.55")).
                totalAmount(new BigDecimal("45.15")).
                productCode("23434").
                commodityCode("9SAASSD8724").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());

        assertEquals(
            ValidationErrorCode.TRANSACTION_LINE_ITEM_NAME_IS_REQUIRED,
            result.getErrors().forObject("transaction").forObject("line_items").forObject("index_1").onField("name").get(0).getCode()
        );
    }

    @Test
    public void saleWithLineItemsValidationErrorNameIsTooLong() {
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("35.05")).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            lineItem().
                quantity(new BigDecimal("1.0232")).
                name("Name #1").
                kind(TransactionLineItem.Kind.DEBIT).
                unitAmount(new BigDecimal("45.1232")).
                unitOfMeasure("gallon").
                discountAmount(new BigDecimal("1.02")).
                taxAmount(new BigDecimal("4.55")).
                totalAmount(new BigDecimal("45.15")).
                productCode("23434").
                commodityCode("9SAASSD8724").
                done().
            lineItem().
                quantity(new BigDecimal("1.0232")).
                name("123456789012345678901234567890123456").
                kind(TransactionLineItem.Kind.DEBIT).
                unitAmount(new BigDecimal("45.1232")).
                unitOfMeasure("gallon").
                discountAmount(new BigDecimal("1.02")).
                taxAmount(new BigDecimal("4.55")).
                totalAmount(new BigDecimal("45.15")).
                productCode("23434").
                commodityCode("9SAASSD8724").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());

        assertEquals(
            ValidationErrorCode.TRANSACTION_LINE_ITEM_NAME_IS_TOO_LONG,
            result.getErrors().forObject("transaction").forObject("line_items").forObject("index_1").onField("name").get(0).getCode()
        );
    }

    @Test
    public void saleWithLineItemsValidationErrorProductCodeIsTooLong() {
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("35.05")).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            lineItem().
                quantity(new BigDecimal("1.0232")).
                name("Name #1").
                kind(TransactionLineItem.Kind.DEBIT).
                unitAmount(new BigDecimal("45.1232")).
                unitOfMeasure("gallon").
                discountAmount(new BigDecimal("1.02")).
                taxAmount(new BigDecimal("4.55")).
                totalAmount(new BigDecimal("45.15")).
                productCode("23434").
                commodityCode("9SAASSD8724").
                done().
            lineItem().
                quantity(new BigDecimal("1.0232")).
                name("Name #2").
                kind(TransactionLineItem.Kind.CREDIT).
                unitAmount(new BigDecimal("45.1232")).
                unitOfMeasure("gallon").
                discountAmount(new BigDecimal("1.02")).
                taxAmount(new BigDecimal("4.55")).
                totalAmount(new BigDecimal("45.15")).
                productCode("123456789012345678901234567890123456").
                commodityCode("9SAASSD8724").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());

        assertEquals(
            ValidationErrorCode.TRANSACTION_LINE_ITEM_PRODUCT_CODE_IS_TOO_LONG,
            result.getErrors().forObject("transaction").forObject("line_items").forObject("index_1").onField("productCode").get(0).getCode()
        );
    }

    @Test
    public void saleWithLineItemsValidationErrorQuantityIsRequired() {
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("35.05")).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            lineItem().
                quantity(new BigDecimal("1.0232")).
                name("Name #1").
                kind(TransactionLineItem.Kind.DEBIT).
                unitAmount(new BigDecimal("45.1232")).
                unitOfMeasure("gallon").
                discountAmount(new BigDecimal("1.02")).
                taxAmount(new BigDecimal("4.55")).
                totalAmount(new BigDecimal("45.15")).
                productCode("23434").
                commodityCode("9SAASSD8724").
                done().
            lineItem().
                name("Name #2").
                kind(TransactionLineItem.Kind.CREDIT).
                unitAmount(new BigDecimal("45.1232")).
                unitOfMeasure("gallon").
                discountAmount(new BigDecimal("1.02")).
                taxAmount(new BigDecimal("4.55")).
                totalAmount(new BigDecimal("45.15")).
                productCode("23434").
                commodityCode("9SAASSD8724").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());

        assertEquals(
            ValidationErrorCode.TRANSACTION_LINE_ITEM_QUANTITY_IS_REQUIRED,
            result.getErrors().forObject("transaction").forObject("line_items").forObject("index_1").onField("quantity").get(0).getCode()
        );
    }

    @Test
    public void saleWithLineItemsValidationErrorQuantityIsTooLarge() {
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("35.05")).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            lineItem().
                quantity(new BigDecimal("1.0232")).
                name("Name #1").
                kind(TransactionLineItem.Kind.DEBIT).
                unitAmount(new BigDecimal("45.1232")).
                unitOfMeasure("gallon").
                discountAmount(new BigDecimal("1.02")).
                taxAmount(new BigDecimal("4.55")).
                totalAmount(new BigDecimal("45.15")).
                productCode("23434").
                commodityCode("9SAASSD8724").
                done().
            lineItem().
                quantity(new BigDecimal("2147483648")).
                name("Name #2").
                kind(TransactionLineItem.Kind.CREDIT).
                unitAmount(new BigDecimal("45.1232")).
                unitOfMeasure("gallon").
                discountAmount(new BigDecimal("1.02")).
                taxAmount(new BigDecimal("4.55")).
                totalAmount(new BigDecimal("45.15")).
                productCode("23434").
                commodityCode("9SAASSD8724").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());

        assertEquals(
            ValidationErrorCode.TRANSACTION_LINE_ITEM_QUANTITY_IS_TOO_LARGE,
            result.getErrors().forObject("transaction").forObject("line_items").forObject("index_1").onField("quantity").get(0).getCode()
        );
    }

    @Test
    public void saleWithLineItemsValidationErrorTotalAmountIsRequired() {
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("35.05")).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            lineItem().
                quantity(new BigDecimal("1.0232")).
                name("Name #1").
                kind(TransactionLineItem.Kind.DEBIT).
                unitAmount(new BigDecimal("45.1232")).
                unitOfMeasure("gallon").
                discountAmount(new BigDecimal("1.02")).
                taxAmount(new BigDecimal("4.55")).
                totalAmount(new BigDecimal("45.15")).
                productCode("23434").
                commodityCode("9SAASSD8724").
                done().
            lineItem().
                quantity(new BigDecimal("1.0232")).
                name("Name #2").
                kind(TransactionLineItem.Kind.CREDIT).
                unitAmount(new BigDecimal("45.1232")).
                unitOfMeasure("gallon").
                discountAmount(new BigDecimal("1.02")).
                taxAmount(new BigDecimal("4.55")).
                productCode("23434").
                commodityCode("9SAASSD8724").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());

        assertEquals(
            ValidationErrorCode.TRANSACTION_LINE_ITEM_TOTAL_AMOUNT_IS_REQUIRED,
            result.getErrors().forObject("transaction").forObject("line_items").forObject("index_1").onField("totalAmount").get(0).getCode()
        );
    }

    @Test
    public void saleWithLineItemsValidationErrorTotalAmountIsTooLarge() {
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("35.05")).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            lineItem().
                quantity(new BigDecimal("1.0232")).
                name("Name #1").
                kind(TransactionLineItem.Kind.DEBIT).
                unitAmount(new BigDecimal("45.1232")).
                unitOfMeasure("gallon").
                discountAmount(new BigDecimal("1.02")).
                taxAmount(new BigDecimal("4.55")).
                totalAmount(new BigDecimal("45.15")).
                productCode("23434").
                commodityCode("9SAASSD8724").
                done().
            lineItem().
                quantity(new BigDecimal("1.0232")).
                name("Name #2").
                kind(TransactionLineItem.Kind.CREDIT).
                unitAmount(new BigDecimal("45.1232")).
                unitOfMeasure("gallon").
                discountAmount(new BigDecimal("1.02")).
                taxAmount(new BigDecimal("4.55")).
                totalAmount(new BigDecimal("2147483648")).
                productCode("23434").
                commodityCode("9SAASSD8724").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());

        assertEquals(
            ValidationErrorCode.TRANSACTION_LINE_ITEM_TOTAL_AMOUNT_IS_TOO_LARGE,
            result.getErrors().forObject("transaction").forObject("line_items").forObject("index_1").onField("totalAmount").get(0).getCode()
        );
    }

    @Test
    public void saleWithLineItemsValidationErrorTotalAmountMustBeGreaterThanZero() {
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("35.05")).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            lineItem().
                quantity(new BigDecimal("1.0232")).
                name("Name #1").
                kind(TransactionLineItem.Kind.DEBIT).
                unitAmount(new BigDecimal("45.1232")).
                unitOfMeasure("gallon").
                discountAmount(new BigDecimal("1.02")).
                taxAmount(new BigDecimal("4.55")).
                totalAmount(new BigDecimal("45.15")).
                productCode("23434").
                commodityCode("9SAASSD8724").
                done().
            lineItem().
                quantity(new BigDecimal("1.0232")).
                name("Name #2").
                kind(TransactionLineItem.Kind.CREDIT).
                unitAmount(new BigDecimal("45.1232")).
                unitOfMeasure("gallon").
                discountAmount(new BigDecimal("1.02")).
                taxAmount(new BigDecimal("4.55")).
                totalAmount(new BigDecimal("-2")).
                productCode("23434").
                commodityCode("9SAASSD8724").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());

        assertEquals(
            ValidationErrorCode.TRANSACTION_LINE_ITEM_TOTAL_AMOUNT_MUST_BE_GREATER_THAN_ZERO,
            result.getErrors().forObject("transaction").forObject("line_items").forObject("index_1").onField("totalAmount").get(0).getCode()
        );
    }

    @Test
    public void saleWithLineItemsValidationErrorUnitAmountIsRequired() {
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("35.05")).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            lineItem().
                quantity(new BigDecimal("1.0232")).
                name("Name #1").
                kind(TransactionLineItem.Kind.DEBIT).
                unitAmount(new BigDecimal("45.1232")).
                unitOfMeasure("gallon").
                discountAmount(new BigDecimal("1.02")).
                taxAmount(new BigDecimal("4.55")).
                totalAmount(new BigDecimal("45.15")).
                productCode("23434").
                commodityCode("9SAASSD8724").
                done().
            lineItem().
                quantity(new BigDecimal("1.0232")).
                name("Name #2").
                kind(TransactionLineItem.Kind.CREDIT).
                unitOfMeasure("gallon").
                discountAmount(new BigDecimal("1.02")).
                taxAmount(new BigDecimal("4.55")).
                totalAmount(new BigDecimal("45.15")).
                productCode("23434").
                commodityCode("9SAASSD8724").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());

        assertEquals(
            ValidationErrorCode.TRANSACTION_LINE_ITEM_UNIT_AMOUNT_IS_REQUIRED,
            result.getErrors().forObject("transaction").forObject("line_items").forObject("index_1").onField("unitAmount").get(0).getCode()
        );
    }

    @Test
    public void saleWithLineItemsValidationErrorUnitAmountIsTooLarge() {
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("35.05")).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            lineItem().
                quantity(new BigDecimal("1.0232")).
                name("Name #1").
                kind(TransactionLineItem.Kind.DEBIT).
                unitAmount(new BigDecimal("45.1232")).
                unitOfMeasure("gallon").
                discountAmount(new BigDecimal("1.02")).
                taxAmount(new BigDecimal("4.55")).
                totalAmount(new BigDecimal("45.15")).
                productCode("23434").
                commodityCode("9SAASSD8724").
                done().
            lineItem().
                quantity(new BigDecimal("1.0232")).
                name("Name #2").
                kind(TransactionLineItem.Kind.CREDIT).
                unitAmount(new BigDecimal("2147483648")).
                unitOfMeasure("gallon").
                discountAmount(new BigDecimal("1.02")).
                taxAmount(new BigDecimal("4.55")).
                totalAmount(new BigDecimal("45.15")).
                productCode("23434").
                commodityCode("9SAASSD8724").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());

        assertEquals(
            ValidationErrorCode.TRANSACTION_LINE_ITEM_UNIT_AMOUNT_IS_TOO_LARGE,
            result.getErrors().forObject("transaction").forObject("line_items").forObject("index_1").onField("unitAmount").get(0).getCode()
        );
    }

    @Test
    public void saleWithLineItemsValidationErrorUnitAmountMustBeGreaterThanZero() {
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("35.05")).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            lineItem().
                quantity(new BigDecimal("1.0232")).
                name("Name #1").
                kind(TransactionLineItem.Kind.DEBIT).
                unitAmount(new BigDecimal("45.1232")).
                unitOfMeasure("gallon").
                discountAmount(new BigDecimal("1.02")).
                taxAmount(new BigDecimal("4.55")).
                totalAmount(new BigDecimal("45.15")).
                productCode("23434").
                commodityCode("9SAASSD8724").
                done().
            lineItem().
                quantity(new BigDecimal("1.0232")).
                name("Name #2").
                kind(TransactionLineItem.Kind.CREDIT).
                unitAmount(new BigDecimal("-2")).
                unitOfMeasure("gallon").
                discountAmount(new BigDecimal("1.02")).
                taxAmount(new BigDecimal("4.55")).
                totalAmount(new BigDecimal("45.15")).
                productCode("23434").
                commodityCode("9SAASSD8724").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());

        assertEquals(
            ValidationErrorCode.TRANSACTION_LINE_ITEM_UNIT_AMOUNT_MUST_BE_GREATER_THAN_ZERO,
            result.getErrors().forObject("transaction").forObject("line_items").forObject("index_1").onField("unitAmount").get(0).getCode()
        );
    }

    @Test
    public void saleWithLineItemsValidationErrorUnitOfMeasureIsTooLong() {
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("35.05")).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            lineItem().
                quantity(new BigDecimal("1.0232")).
                name("Name #1").
                kind(TransactionLineItem.Kind.DEBIT).
                unitAmount(new BigDecimal("45.1232")).
                unitOfMeasure("gallon").
                discountAmount(new BigDecimal("1.02")).
                taxAmount(new BigDecimal("4.55")).
                totalAmount(new BigDecimal("45.15")).
                productCode("23434").
                commodityCode("9SAASSD8724").
                done().
            lineItem().
                quantity(new BigDecimal("1.0232")).
                name("Name #2").
                kind(TransactionLineItem.Kind.CREDIT).
                unitAmount(new BigDecimal("45.1232")).
                unitOfMeasure("1234567890123").
                discountAmount(new BigDecimal("1.02")).
                taxAmount(new BigDecimal("4.55")).
                totalAmount(new BigDecimal("45.15")).
                productCode("23434").
                commodityCode("9SAASSD8724").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());

        assertEquals(
            ValidationErrorCode.TRANSACTION_LINE_ITEM_UNIT_OF_MEASURE_IS_TOO_LONG,
            result.getErrors().forObject("transaction").forObject("line_items").forObject("index_1").onField("unitOfMeasure").get(0).getCode()
        );
    }

    @Test
    public void saleWithLineItemsValidationErrorUnitTaxAmountFormatIsInvalid() {
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("35.05")).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            lineItem().
                quantity(new BigDecimal("1.2322")).
                name("Name #1").
                kind(TransactionLineItem.Kind.DEBIT).
                unitAmount(new BigDecimal("45.1232")).
                unitOfMeasure("gallon").
                discountAmount(new BigDecimal("1.02")).
                taxAmount(new BigDecimal("4.55")).
                totalAmount(new BigDecimal("45.15")).
                productCode("23434").
                commodityCode("9SAASSD8724").
                done().
            lineItem().
                quantity(new BigDecimal("1.2322")).
                name("Name #2").
                kind(TransactionLineItem.Kind.CREDIT).
                unitAmount(new BigDecimal("45.0122")).
                unitTaxAmount(new BigDecimal("2.012")).
                unitOfMeasure("gallon").
                discountAmount(new BigDecimal("1.02")).
                taxAmount(new BigDecimal("4.55")).
                totalAmount(new BigDecimal("45.15")).
                productCode("23434").
                commodityCode("9SAASSD8724").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());

        assertEquals(
            ValidationErrorCode.TRANSACTION_LINE_ITEM_UNIT_TAX_AMOUNT_FORMAT_IS_INVALID,
            result.getErrors().forObject("transaction").forObject("line_items").forObject("index_1").onField("unitTaxAmount").get(0).getCode()
        );
    }

    @Test
    public void saleWithLineItemsValidationErrorUnitTaxAmountIsTooLarge() {
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("35.05")).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            lineItem().
                quantity(new BigDecimal("1.2322")).
                name("Name #1").
                kind(TransactionLineItem.Kind.DEBIT).
                unitAmount(new BigDecimal("45.1232")).
                unitTaxAmount(new BigDecimal("1.23")).
                unitOfMeasure("gallon").
                discountAmount(new BigDecimal("1.02")).
                taxAmount(new BigDecimal("4.55")).
                totalAmount(new BigDecimal("45.15")).
                productCode("23434").
                commodityCode("9SAASSD8724").
                done().
            lineItem().
                quantity(new BigDecimal("1.2322")).
                name("Name #2").
                kind(TransactionLineItem.Kind.CREDIT).
                unitAmount(new BigDecimal("45.0122")).
                unitTaxAmount(new BigDecimal("2147483648")).
                unitOfMeasure("gallon").
                discountAmount(new BigDecimal("1.02")).
                taxAmount(new BigDecimal("4.55")).
                totalAmount(new BigDecimal("45.15")).
                productCode("23434").
                commodityCode("9SAASSD8724").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());

        assertEquals(
            ValidationErrorCode.TRANSACTION_LINE_ITEM_UNIT_TAX_AMOUNT_IS_TOO_LARGE,
            result.getErrors().forObject("transaction").forObject("line_items").forObject("index_1").onField("unitTaxAmount").get(0).getCode()
        );
    }

    @Test
    public void saleWithLineItemsValidationErrorUnitTaxAmountCannotBeNegative() {
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("35.05")).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            lineItem().
                quantity(new BigDecimal("1.2322")).
                name("Name #1").
                kind(TransactionLineItem.Kind.DEBIT).
                unitAmount(new BigDecimal("45.1232")).
                unitOfMeasure("gallon").
                discountAmount(new BigDecimal("1.02")).
                taxAmount(new BigDecimal("4.55")).
                totalAmount(new BigDecimal("45.15")).
                productCode("23434").
                commodityCode("9SAASSD8724").
                done().
            lineItem().
                quantity(new BigDecimal("1.2322")).
                name("Name #2").
                kind(TransactionLineItem.Kind.CREDIT).
                unitAmount(new BigDecimal("45.0122")).
                unitTaxAmount(new BigDecimal("-1.23")).
                unitOfMeasure("gallon").
                discountAmount(new BigDecimal("1.02")).
                taxAmount(new BigDecimal("4.55")).
                totalAmount(new BigDecimal("45.15")).
                productCode("23434").
                commodityCode("9SAASSD8724").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());

        assertEquals(
            ValidationErrorCode.TRANSACTION_LINE_ITEM_UNIT_TAX_AMOUNT_CANNOT_BE_NEGATIVE,
            result.getErrors().forObject("transaction").forObject("line_items").forObject("index_1").onField("unitTaxAmount").get(0).getCode()
        );
    }

    @Test
    public void saleWithLineItemsValidationErrorTooManyLineItems() {
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("35.05")).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done();

        for (int i = 0; i < 250; i++) {
            request.
                lineItem().
                    quantity(new BigDecimal("2.02")).
                    name("Line item #" + i).
                    kind(TransactionLineItem.Kind.CREDIT).
                    unitAmount(new BigDecimal("5")).
                    unitOfMeasure("gallon").
                    totalAmount(new BigDecimal("10.1"));
        }

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());

        assertEquals(
            ValidationErrorCode.TRANSACTION_TOO_MANY_LINE_ITEMS,
            result.getErrors().forObject("transaction").onField("line_items").get(0).getCode()
        );
    }

    @Test
    public void saleVisaReceivesNetworkTransactionIdentifier() {
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("10.00")).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        Transaction transaction = result.getTarget();
        assertTrue(transaction.getNetworkTransactionId().length() > 0);
    }

    @Test
    public void saleNonVisaDoesNotReceiveNetworkTransactionIdentifier() {
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("10.00")).
            creditCard().
                number(CreditCardNumber.MASTER_CARD.number).
                expirationDate("05/2009").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        Transaction transaction = result.getTarget();
        assertNull(transaction.getNetworkTransactionId());
    }

    @Test
    public void saleWithExternalVaultStatusVisa() {
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("10.00")).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            externalVault().
                vaulted().
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();
        assertTrue(transaction.getNetworkTransactionId().length() > 0);
    }

    @Test
    public void saleWithExternalVaultStatusNonVisa() {
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("10.00")).
            creditCard().
                number(CreditCardNumber.MASTER_CARD.number).
                expirationDate("05/2009").
                done().
            externalVault().
                vaulted().
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();
        assertNull(transaction.getNetworkTransactionId());
    }

    @Test
    public void saleWithExternalVaultPreviousNetworkTransactionId() {
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("10.00")).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            externalVault().
                vaulted().
                previousNetworkTransactionId("123456789012345").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();
        assertTrue(transaction.getNetworkTransactionId().length() > 0);
    }

    @Test
    public void saleWithExternalVaultStatusVaultedWithoutPreviousNetworkTransactionId() {
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("10.00")).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            externalVault().
                vaulted().
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();
        assertTrue(transaction.getNetworkTransactionId().length() > 0);
    }

    @Test
    public void saleWithExternalVaultValidationErrorInvalidPaymentInstrumentWithExternalVault() {
        Customer customer = gateway.customer().create(new CustomerRequest()).getTarget();
        CreditCardRequest creditCardRequest = new CreditCardRequest().
            customerId(customer.getId()).
            cvv("123").
            number("5105105105105100").
            expirationDate("05/12");
        CreditCard creditCard = gateway.creditCard().create(creditCardRequest).getTarget();
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("10.00")).
            paymentMethodToken(creditCard.getToken()).
            externalVault().
                vaulted().
                previousNetworkTransactionId("123456789012345").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());
        assertEquals(
            ValidationErrorCode.TRANSACTION_PAYMENT_INSTRUMENT_WITH_EXTERNAL_VAULT_IS_INVALID,
            result.getErrors().forObject("transaction").onField("external_vault").get(0).getCode()
        );
    }

    @Test
    public void saleWithExternalVaultValidationErrorInvalidStatusWithPreviousNetworkTransactionId() {
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("10.00")).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            externalVault().
                willVault().
                previousNetworkTransactionId("123456789012345").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());
        assertEquals(
            ValidationErrorCode.TRANSACTION_EXTERNAL_VAULT_STATUS_WITH_PREVIOUS_NETWORK_TRANSACTION_ID_IS_INVALID,
            result.getErrors().forObject("transaction").forObject("external_vault").onField("status").get(0).getCode()
        );
    }

    @Test
    public void saleWithExternalVaultValidationErrorInvalidPreviousNetworkTransactionId() {
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("10.00")).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            externalVault().
                vaulted().
                previousNetworkTransactionId("not_valid").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());
        assertEquals(
            ValidationErrorCode.TRANSACTION_EXTERNAL_VAULT_PREVIOUS_NETWORK_TRANSACTION_ID_IS_INVALID,
            result.getErrors().forObject("transaction").forObject("external_vault").onField("previousNetworkTransactionId").get(0).getCode()
        );
    }

    @Test
    public void saleWithExternalVaultValidationErrorInvalidCardType() {
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("10.00")).
            creditCard().
                number(CreditCardNumber.MASTER_CARD.number).
                expirationDate("05/2009").
                done().
            externalVault().
                vaulted().
                previousNetworkTransactionId("123456789012345").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());
        assertEquals(
            ValidationErrorCode.TRANSACTION_EXTERNAL_VAULT_CARD_TYPE_IS_INVALID,
            result.getErrors().forObject("transaction").forObject("external_vault").onField("previousNetworkTransactionId").get(0).getCode()
        );
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

        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        assertEquals("United States of America", transaction.getBillingAddress().getCountryName());
        assertEquals("US", transaction.getBillingAddress().getCountryCodeAlpha2());
        assertEquals("USA", transaction.getBillingAddress().getCountryCodeAlpha3());
        assertEquals("840", transaction.getBillingAddress().getCountryCodeNumeric());
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

        assertFalse(result.isSuccess());

        assertEquals(ValidationErrorCode.ADDRESS_COUNTRY_NAME_IS_NOT_ACCEPTED,
                result.getErrors().forObject("transaction").forObject("billing").onField("countryName").get(0).getCode());
        assertEquals(ValidationErrorCode.ADDRESS_COUNTRY_CODE_ALPHA2_IS_NOT_ACCEPTED,
                result.getErrors().forObject("transaction").forObject("billing").onField("countryCodeAlpha2").get(0).getCode());
        assertEquals(ValidationErrorCode.ADDRESS_COUNTRY_CODE_ALPHA3_IS_NOT_ACCEPTED,
                result.getErrors().forObject("transaction").forObject("billing").onField("countryCodeAlpha3").get(0).getCode());
        assertEquals(ValidationErrorCode.ADDRESS_COUNTRY_CODE_NUMERIC_IS_NOT_ACCEPTED,
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
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        assertEquals(new BigDecimal("1000.00"), transaction.getAmount());
        assertEquals(Transaction.Type.CREDIT, transaction.getType());
        assertEquals(Transaction.Status.SUBMITTED_FOR_SETTLEMENT, transaction.getStatus());

        CreditCard creditCard = transaction.getCreditCard();
        assertEquals("411111", creditCard.getBin());
        assertEquals("1111", creditCard.getLast4());
        assertEquals("05", creditCard.getExpirationMonth());
        assertEquals("2009", creditCard.getExpirationYear());
        assertEquals("05/2009", creditCard.getExpirationDate());
    }

    @Test
    public void creditWithSpecifyingMerchantAccountId() {
        TransactionRequest request = new TransactionRequest().
            amount(SandboxValues.TransactionAmount.AUTHORIZE.amount).
            merchantAccountId(NON_DEFAULT_MERCHANT_ACCOUNT_ID).
            creditCard().
                number(SandboxValues.CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done();

        Result<Transaction> result = gateway.transaction().credit(request);
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        assertEquals(NON_DEFAULT_MERCHANT_ACCOUNT_ID, transaction.getMerchantAccountId());
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
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        assertEquals(DEFAULT_MERCHANT_ACCOUNT_ID, transaction.getMerchantAccountId());
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
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        Map<String, String> expected = new HashMap<String, String>();
        expected.put("store_me", "custom value");
        expected.put("another_stored_field", "custom value2");

        assertEquals(expected, transaction.getCustomFields());
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
        assertFalse(result.isSuccess());
        assertNull(result.getTarget());
        assertEquals(ValidationErrorCode.TRANSACTION_AMOUNT_IS_REQUIRED, result.getErrors().forObject("transaction").onField("amount").get(0).getCode());

        Map<String, String> parameters = result.getParameters();
        assertEquals(null, parameters.get("transaction[amount]"));
        assertEquals("05", parameters.get("transaction[credit_card][expiration_month]"));
        assertEquals("2010", parameters.get("transaction[credit_card][expiration_year]"));
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

        assertEquals(transaction.getId(), foundTransaction.getId());
        assertEquals(Transaction.Status.AUTHORIZED, foundTransaction.getStatus());
        assertEquals("05/2008", foundTransaction.getCreditCard().getExpirationDate());
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
    public void findWithDisbursementDetails() throws Exception {
        Calendar disbursementCalendar = CalendarTestUtils.date("2013-04-10");

        Transaction foundTransaction = gateway.transaction().find(DISBURSEMENT_TRANSACTION_ID);
        DisbursementDetails disbursementDetails = foundTransaction.getDisbursementDetails();

        assertEquals(true, foundTransaction.isDisbursed());
        assertEquals(disbursementCalendar, disbursementDetails.getDisbursementDate());
        assertEquals("USD", disbursementDetails.getSettlementCurrencyIsoCode());
        assertEquals(false, disbursementDetails.isFundsHeld());
        assertEquals(new BigDecimal("1"), disbursementDetails.getSettlementCurrencyExchangeRate());
        assertEquals(new BigDecimal("100.00"), disbursementDetails.getSettlementAmount());
    }

    @Test
    public void findWithDisputes() throws Exception {
        Calendar disputeCalendar = CalendarTestUtils.date("2014-03-01");
        Calendar replyCalendar = CalendarTestUtils.date("2014-03-21");
        Calendar openedCalendar = CalendarTestUtils.date("2014-03-01");
        Calendar wonCalendar = CalendarTestUtils.date("2014-03-07");

        Transaction foundTransaction = gateway.transaction().find(DISPUTED_TRANSACTION_ID);
        List<Dispute> disputes = foundTransaction.getDisputes();
        Dispute dispute = disputes.get(0);

        assertEquals(disputeCalendar, dispute.getReceivedDate());
        assertEquals(replyCalendar, dispute.getReplyByDate());
        assertEquals("USD", dispute.getCurrencyIsoCode());
        assertEquals(Dispute.Reason.FRAUD, dispute.getReason());
        assertEquals(Dispute.Status.WON, dispute.getStatus());
        assertEquals(new BigDecimal("250.00"), dispute.getAmount());
        assertEquals(new BigDecimal("1000.00"), dispute.getTransactionDetails().getAmount());
        assertEquals(DISPUTED_TRANSACTION_ID, dispute.getTransactionDetails().getId());
        assertEquals(Dispute.Kind.CHARGEBACK, dispute.getKind());
        assertEquals(openedCalendar, dispute.getOpenedDate());
        assertEquals(wonCalendar, dispute.getWonDate());
    }

    @Test
    public void findWithAuthAdjustmentApproved() throws Exception {
        Transaction foundTransaction = gateway.transaction().find(AUTH_ADJUSTMENT_TRANSACTION_ID);
        List<AuthorizationAdjustment> authorizationAdjustments = foundTransaction.getAuthorizationAdjustments();
        AuthorizationAdjustment authorizationAdjustment = authorizationAdjustments.get(0);

        assertEquals(new BigDecimal("-20.00"), authorizationAdjustment.getAmount());
        assertEquals(true, authorizationAdjustment.isSuccess());
        assertEquals(Calendar.getInstance().get(Calendar.YEAR), authorizationAdjustment.getTimestamp().get(Calendar.YEAR));
        assertEquals("1000", authorizationAdjustment.getProcessorResponseCode());
        assertEquals("Approved", authorizationAdjustment.getProcessorResponseText());
        assertEquals(ProcessorResponseType.APPROVED, authorizationAdjustment.getProcessorResponseType());
    }

    @Test
    public void findWithAuthAdjustmentSoftDeclined() throws Exception {
        Transaction foundTransaction = gateway.transaction().find(AUTH_ADJUSTMENT_SOFT_DECLINED_TRANSACTION_ID);
        List<AuthorizationAdjustment> authorizationAdjustments = foundTransaction.getAuthorizationAdjustments();
        AuthorizationAdjustment authorizationAdjustment = authorizationAdjustments.get(0);

        assertEquals(new BigDecimal("-20.00"), authorizationAdjustment.getAmount());
        assertEquals(false, authorizationAdjustment.isSuccess());
        assertEquals(Calendar.getInstance().get(Calendar.YEAR), authorizationAdjustment.getTimestamp().get(Calendar.YEAR));
        assertEquals("3000", authorizationAdjustment.getProcessorResponseCode());
        assertEquals("Processor Network Unavailable - Try Again", authorizationAdjustment.getProcessorResponseText());
        assertEquals(ProcessorResponseType.SOFT_DECLINED, authorizationAdjustment.getProcessorResponseType());
    }

    @Test
    public void findWithAuthAdjustmentHardDeclined() throws Exception {
        Transaction foundTransaction = gateway.transaction().find(AUTH_ADJUSTMENT_HARD_DECLINED_TRANSACTION_ID);
        List<AuthorizationAdjustment> authorizationAdjustments = foundTransaction.getAuthorizationAdjustments();
        AuthorizationAdjustment authorizationAdjustment = authorizationAdjustments.get(0);

        assertEquals(new BigDecimal("-20.00"), authorizationAdjustment.getAmount());
        assertEquals(false, authorizationAdjustment.isSuccess());
        assertEquals(Calendar.getInstance().get(Calendar.YEAR), authorizationAdjustment.getTimestamp().get(Calendar.YEAR));
        assertEquals("2015", authorizationAdjustment.getProcessorResponseCode());
        assertEquals("Transaction Not Allowed", authorizationAdjustment.getProcessorResponseText());
        assertEquals(ProcessorResponseType.HARD_DECLINED, authorizationAdjustment.getProcessorResponseType());
    }

    @Test
    public void findWithRetrievals() throws Exception {
        Calendar disputeCalendar = CalendarTestUtils.date("2014-03-01");
        Calendar replyCalendar = CalendarTestUtils.date("2014-03-21");

        Transaction foundTransaction = gateway.transaction().find("retrievaltransaction");
        List<Dispute> disputes = foundTransaction.getDisputes();
        Dispute dispute = disputes.get(0);

        assertEquals("USD", dispute.getCurrencyIsoCode());
        assertEquals(Dispute.Reason.RETRIEVAL, dispute.getReason());
        assertEquals(Dispute.Status.OPEN, dispute.getStatus());
        assertEquals(new BigDecimal("1000.00"), dispute.getAmount());
        assertEquals(new BigDecimal("1000.00"), dispute.getTransactionDetails().getAmount());
        assertEquals("retrievaltransaction", dispute.getTransactionDetails().getId());
    }

    @Test
    public void findWithThreeDSecureInfo() throws Exception {
        Transaction foundTransaction = gateway.transaction().find("threedsecuredtransaction");
        ThreeDSecureInfo info = foundTransaction.getThreeDSecureInfo();

        assertEquals("Y", info.getEnrolled());
        assertTrue(info.isLiabilityShifted());
        assertTrue(info.isLiabilityShiftPossible());
        assertEquals("authenticate_successful", info.getStatus());
    }

    @Test
    public void findWithoutThreeDSecureInfo() throws Exception {
        Transaction foundTransaction = gateway.transaction().find("settledtransaction");

        assertNull(foundTransaction.getThreeDSecureInfo());
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
        assertTrue(result.isSuccess());

        assertEquals(transaction.getId(), result.getTarget().getId());
        assertEquals(Transaction.Status.VOIDED, result.getTarget().getStatus());
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

        assertFalse(result.isSuccess());
        assertEquals(ValidationErrorCode.TRANSACTION_CANNOT_BE_VOIDED,
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

        assertEquals(2, settledTransaction.getStatusHistory().size());
        assertEquals(Transaction.Status.AUTHORIZED, settledTransaction.getStatusHistory().get(0).getStatus());
        assertEquals(Transaction.Status.SUBMITTED_FOR_SETTLEMENT, settledTransaction.getStatusHistory().get(1).getStatus());
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

        assertTrue(result.isSuccess());
        assertEquals(Transaction.Status.SUBMITTED_FOR_SETTLEMENT, result.getTarget().getStatus());
        assertEquals(TransactionAmount.AUTHORIZE.amount, result.getTarget().getAmount());
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

        assertTrue(result.isSuccess());
        assertEquals(Transaction.Status.SUBMITTED_FOR_SETTLEMENT, result.getTarget().getStatus());
        assertEquals(new BigDecimal("50.00"), result.getTarget().getAmount());
    }

    @Test
    public void submitForSettlementWithOrderId() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2008").
                done();
        Transaction transaction = gateway.transaction().sale(request).getTarget();

        TransactionRequest submitForSettlementRequest = new TransactionRequest().
            orderId("1234");

        Result<Transaction> result = gateway.transaction().submitForSettlement(transaction.getId(), submitForSettlementRequest);

        assertTrue(result.isSuccess());
        assertEquals(Transaction.Status.SUBMITTED_FOR_SETTLEMENT, result.getTarget().getStatus());
        assertEquals(new String("1234"), result.getTarget().getOrderId());
    }

    @Test
    public void submitForSettlementWithDescriptors() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2008").
                done();
        Transaction transaction = gateway.transaction().sale(request).getTarget();

        TransactionRequest submitForSettlementRequest = new TransactionRequest().
            descriptor().
                name("123*123456789012345678").
                phone("3334445555").
                url("ebay.com").
                done();

        Result<Transaction> result = gateway.transaction().submitForSettlement(transaction.getId(), submitForSettlementRequest);

        assertTrue(result.isSuccess());
        assertEquals(Transaction.Status.SUBMITTED_FOR_SETTLEMENT, result.getTarget().getStatus());
        assertEquals("123*123456789012345678", result.getTarget().getDescriptor().getName());
        assertEquals("3334445555", result.getTarget().getDescriptor().getPhone());
        assertEquals("ebay.com", result.getTarget().getDescriptor().getUrl());
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

        assertFalse(result.isSuccess());
        assertEquals(ValidationErrorCode.TRANSACTION_CANNOT_SUBMIT_FOR_SETTLEMENT,
                result.getErrors().forObject("transaction").onField("base").get(0).getCode());
    }

    @Test
    public void updateDetails() {
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

        TransactionRequest updateDetailsRequest = new TransactionRequest().
            amount(new BigDecimal("123.45")).
            descriptor().
                name("123*123456789012345678").
                phone("3334445555").
                url("ebay.com").
                done().
            orderId("1234");

        Result<Transaction> result = gateway.transaction().updateDetails(transaction.getId(), updateDetailsRequest);

        assertTrue(result.isSuccess());
        assertEquals(Transaction.Status.SUBMITTED_FOR_SETTLEMENT, result.getTarget().getStatus());
        assertEquals(new BigDecimal("123.45"), result.getTarget().getAmount());
        assertEquals(new String("1234"), result.getTarget().getOrderId());
        assertEquals("123*123456789012345678", result.getTarget().getDescriptor().getName());
        assertEquals("3334445555", result.getTarget().getDescriptor().getPhone());
        assertEquals("ebay.com", result.getTarget().getDescriptor().getUrl());
    }

    @Test
    public void updateDetailsWithInvalidAmount() {
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

        TransactionRequest updateDetailsRequest = new TransactionRequest().
            amount(new BigDecimal("9999")).
            descriptor().
                name("123*123456789012345678").
                phone("3334445555").
                url("ebay.com").
                done().
            orderId("1234");

        Result<Transaction> result = gateway.transaction().updateDetails(transaction.getId(), updateDetailsRequest);

        assertFalse(result.isSuccess());
        assertEquals(ValidationErrorCode.TRANSACTION_SETTLEMENT_AMOUNT_IS_TOO_LARGE,
                result.getErrors().forObject("transaction").onField("amount").get(0).getCode());
    }

    @Test
    public void updateDetailsWithInvalidDescriptor() {
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

        TransactionRequest updateDetailsRequest = new TransactionRequest().
            descriptor().
                name("invalid name").
                phone("invalid phone").
                url("invalid url is way too long to be valid").
                done().
            orderId("1234");

        Result<Transaction> result = gateway.transaction().updateDetails(transaction.getId(), updateDetailsRequest);

        assertFalse(result.isSuccess());
        assertEquals(ValidationErrorCode.DESCRIPTOR_NAME_FORMAT_IS_INVALID,
                result.getErrors().forObject("transaction").forObject("descriptor").onField("name").get(0).getCode());
        assertEquals(ValidationErrorCode.DESCRIPTOR_PHONE_FORMAT_IS_INVALID,
                result.getErrors().forObject("transaction").forObject("descriptor").onField("phone").get(0).getCode());
        assertEquals(ValidationErrorCode.DESCRIPTOR_URL_FORMAT_IS_INVALID,
                result.getErrors().forObject("transaction").forObject("descriptor").onField("url").get(0).getCode());
    }

    @Test
    public void updateDetailsWithBadStatus() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2008").
                done();

        Transaction transaction = gateway.transaction().sale(request).getTarget();

        TransactionRequest updateDetailsRequest = new TransactionRequest().
            amount(new BigDecimal("123.45")).
            descriptor().
                name("123*123456789012345678").
                phone("3334445555").
                url("ebay.com").
                done().
            orderId("1234");

        Result<Transaction> result = gateway.transaction().updateDetails(transaction.getId(), updateDetailsRequest);

        assertFalse(result.isSuccess());
        assertEquals(ValidationErrorCode.TRANSACTION_CANNOT_UPDATE_DETAILS_NOT_SUBMITTED_FOR_SETTLEMENT,
                result.getErrors().forObject("transaction").onField("base").get(0).getCode());
    }

    @Test
    public void updateDetailsWithInvalidProcessor() {
        TransactionRequest request = new TransactionRequest().
            merchantAccountId(FAKE_AMEX_DIRECT_MERCHANT_ACCOUNT_ID).
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.AmexPayWithPoints.SUCCESS.number).
                expirationDate("12/2020").
                done().
            options().
                submitForSettlement(true).
                done();

        Transaction transaction = gateway.transaction().sale(request).getTarget();

        TransactionRequest updateDetailsRequest = new TransactionRequest().
            amount(new BigDecimal("123.45")).
            descriptor().
                name("123*123456789012345678").
                phone("3334445555").
                url("ebay.com").
                done().
            orderId("1234");

        Result<Transaction> result = gateway.transaction().updateDetails(transaction.getId(), updateDetailsRequest);

        assertFalse(result.isSuccess());
        assertEquals(ValidationErrorCode.TRANSACTION_PROCESSOR_DOES_NOT_SUPPORT_UPDATING_DETAILS,
                result.getErrors().forObject("transaction").onField("base").get(0).getCode());
    }

    @Test(expected = NotFoundException.class)
    public void submitForSettlementWithBadId() {
        gateway.transaction().submitForSettlement("badId");
    }

    @Test
    public void searchOnAllTextFields() {
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

        assertEquals(1, collection.getMaximumSize());
        assertEquals(transaction.getId(), collection.getFirst().getId());
    }

    @Test
    public void searchWithCreditCardNumberStartsWithEndsWith() {
        String creditCardToken = String.valueOf(new Random().nextInt());

        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("1000")).
            creditCard().
                number("4111111111111111").
                expirationDate("05/2012").
                cardholderName("Tom Smith").
                token(creditCardToken).
                done();

        Transaction transaction = gateway.transaction().sale(request).getTarget();
        TestHelper.settle(gateway, transaction.getId());
        transaction = gateway.transaction().find(transaction.getId());

        TransactionSearchRequest searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            creditCardCardholderName().is("Tom Smith").
            creditCardExpirationDate().is("05/2012").
            creditCardNumber().startsWith("411111").
            creditCardNumber().endsWith("1111");

        ResourceCollection<Transaction> collection = gateway.transaction().search(searchRequest);

        assertEquals(1, collection.getMaximumSize());
        assertEquals(transaction.getId(), collection.getFirst().getId());
    }

    @Test
    public void searchWithCreditCardNumberStartsWithEndsWithReusingPartialNode() {
        String creditCardToken = String.valueOf(new Random().nextInt());

        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("1000")).
            creditCard().
                number("4111111111111111").
                expirationDate("05/2012").
                cardholderName("Tom Smith").
                token(creditCardToken).
                done();

        Transaction transaction = gateway.transaction().sale(request).getTarget();
        TestHelper.settle(gateway, transaction.getId());
        transaction = gateway.transaction().find(transaction.getId());

        TransactionSearchRequest searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            creditCardCardholderName().is("Tom Smith").
            creditCardExpirationDate().is("05/2012");

        PartialMatchNode<TransactionSearchRequest> creditCardPartialNode = searchRequest.creditCardNumber();
        creditCardPartialNode.startsWith("4111");
        creditCardPartialNode.endsWith("1111");

        ResourceCollection<Transaction> collection = gateway.transaction().search(searchRequest);

        assertEquals(1, collection.getMaximumSize());
        assertEquals(transaction.getId(), collection.getFirst().getId());
    }

    @Test
    public void searchWithCreditCardNumberStartsWithEndsWithPositiveAndNegativeCases() {
        String creditCardToken = String.valueOf(new Random().nextInt());

        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("1000")).
            creditCard().
                number("4111111111111111").
                expirationDate("05/2012").
                cardholderName("Tom Smith").
                token(creditCardToken).
                done();

        Transaction transaction = gateway.transaction().sale(request).getTarget();
        TestHelper.settle(gateway, transaction.getId());
        transaction = gateway.transaction().find(transaction.getId());

        TransactionSearchRequest searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            creditCardCardholderName().is("Tom Smith").
            creditCardExpirationDate().is("05/2012").
            creditCardNumber().startsWith("4112").
            creditCardNumber().endsWith("1111");

        ResourceCollection<Transaction> collection = gateway.transaction().search(searchRequest);

        assertEquals(0, collection.getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            creditCardCardholderName().is("Tom Smith").
            creditCardExpirationDate().is("05/2012").
            creditCardNumber().startsWith("4111").
            creditCardNumber().endsWith("1112");

        collection = gateway.transaction().search(searchRequest);

        assertEquals(0, collection.getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            creditCardCardholderName().is("Tom Smith").
            creditCardExpirationDate().is("05/2012").
            creditCardNumber().startsWith("4111").
            creditCardNumber().endsWith("1111");

        collection = gateway.transaction().search(searchRequest);

        assertEquals(1, collection.getMaximumSize());
        assertEquals(transaction.getId(), collection.getFirst().getId());
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
        assertEquals(1, collection.getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            creditCardCardholderName().endsWith("Smith");

        collection = gateway.transaction().search(searchRequest);
        assertEquals(1, collection.getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            creditCardCardholderName().contains("m Sm");

        collection = gateway.transaction().search(searchRequest);
        assertEquals(1, collection.getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            creditCardCardholderName().isNot("Tom Smith");

        collection = gateway.transaction().search(searchRequest);
        assertEquals(0, collection.getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            user().is("integration_user_public_id").
            id().is(transaction.getId());

        collection = gateway.transaction().search(searchRequest);
        assertEquals(1, collection.getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            creditCardUniqueIdentifier().is(transaction.getCreditCard().getUniqueNumberIdentifier()).
            id().is(transaction.getId());

        collection = gateway.transaction().search(searchRequest);
        assertEquals(1, collection.getMaximumSize());
    }

    @Test
    public void searchOnsPaymentInstrumentTypeIsCreditCard() {
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
            paymentInstrumentType().is("CreditCardDetail");

        ResourceCollection<Transaction> collection = gateway.transaction().search(searchRequest);
        assertEquals(collection.getFirst().getPaymentInstrumentType(), PaymentInstrumentType.CREDIT_CARD);
    }

    @Test
    public void searchOnsPaymentInstrumentTypeIsPayPal() {
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("1000")).
            paymentMethodNonce(Nonce.PayPalFuturePayment);

        Transaction transaction = gateway.transaction().sale(request).getTarget();

        TransactionSearchRequest searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            paymentInstrumentType().is("PayPalDetail");

        ResourceCollection<Transaction> collection = gateway.transaction().search(searchRequest);
        assertEquals(collection.getFirst().getPaymentInstrumentType(), PaymentInstrumentType.PAYPAL_ACCOUNT);
    }

    @Test
    public void searchOnsPaymentInstrumentTypeIsApplePay() {
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("1000")).
            paymentMethodNonce(Nonce.ApplePayVisa);

        Transaction transaction = gateway.transaction().sale(request).getTarget();

        TransactionSearchRequest searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            paymentInstrumentType().is("ApplePayDetail");

        ResourceCollection<Transaction> collection = gateway.transaction().search(searchRequest);
        assertEquals(collection.getFirst().getPaymentInstrumentType(), PaymentInstrumentType.APPLE_PAY_CARD);
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
        assertEquals(1, collection.getMaximumSize());
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

        assertEquals(1, collection.getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            createdUsing().in(Transaction.CreatedUsing.FULL_INFORMATION, Transaction.CreatedUsing.TOKEN);

        collection = gateway.transaction().search(searchRequest);

        assertEquals(1, collection.getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            createdUsing().is(Transaction.CreatedUsing.TOKEN);

        collection = gateway.transaction().search(searchRequest);

        assertEquals(0, collection.getMaximumSize());
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

        assertEquals(1, collection.getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            creditCardCustomerLocation().in(CreditCard.CustomerLocation.US, CreditCard.CustomerLocation.INTERNATIONAL);

        collection = gateway.transaction().search(searchRequest);

        assertEquals(1, collection.getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            creditCardCustomerLocation().is(CreditCard.CustomerLocation.INTERNATIONAL);

        collection = gateway.transaction().search(searchRequest);

        assertEquals(0, collection.getMaximumSize());
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

        assertEquals(1, collection.getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            merchantAccountId().in(transaction.getMerchantAccountId(), "badmerchantaccountid");

        collection = gateway.transaction().search(searchRequest);

        assertEquals(1, collection.getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            merchantAccountId().is("badmerchantaccountid");

        collection = gateway.transaction().search(searchRequest);

        assertEquals(0, collection.getMaximumSize());
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

        assertEquals(1, collection.getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            creditCardCardType().in(CreditCard.CardType.VISA, CreditCard.CardType.MASTER_CARD);

        collection = gateway.transaction().search(searchRequest);

        assertEquals(1, collection.getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            creditCardCardType().is(CreditCard.CardType.MASTER_CARD);

        collection = gateway.transaction().search(searchRequest);

        assertEquals(0, collection.getMaximumSize());
    }

    @Test
    public void searchOnCreditCardTypeElo() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            merchantAccountId(ADYEN_MERCHANT_ACCOUNT_ID).
            creditCard().
                number(CreditCardNumber.ELO.number).
                expirationDate("10/2020").
                cvv("737").
                done();

        Transaction transaction = gateway.transaction().sale(request).getTarget();

        TransactionSearchRequest searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            creditCardCardType().is(CreditCard.CardType.ELO);

        ResourceCollection<Transaction> collection = gateway.transaction().search(searchRequest);

        assertEquals(1, collection.getMaximumSize());
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

        assertEquals(1, collection.getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            status().in(Transaction.Status.AUTHORIZED, Transaction.Status.GATEWAY_REJECTED);

        collection = gateway.transaction().search(searchRequest);

        assertEquals(1, collection.getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            status().is(Transaction.Status.GATEWAY_REJECTED);

        collection = gateway.transaction().search(searchRequest);

        assertEquals(0, collection.getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            status().is(Transaction.Status.SETTLEMENT_CONFIRMED);

        collection = gateway.transaction().search(searchRequest);

        assertEquals(0, collection.getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            status().is(Transaction.Status.SETTLEMENT_DECLINED);

        collection = gateway.transaction().search(searchRequest);

        assertEquals(0, collection.getMaximumSize());
    }

    @Test
    public void searchOnAuthorizationExpiredStatus() {
        TransactionSearchRequest searchRequest = new TransactionSearchRequest().
            status().is(Transaction.Status.AUTHORIZATION_EXPIRED);
        ResourceCollection<Transaction> collection = gateway.transaction().search(searchRequest);

        assertTrue(collection.getMaximumSize() > 0);
        assertEquals(Transaction.Status.AUTHORIZATION_EXPIRED, collection.getFirst().getStatus());
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

        assertEquals(1, collection.getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            source().in(Transaction.Source.API, Transaction.Source.CONTROL_PANEL);

        collection = gateway.transaction().search(searchRequest);

        assertEquals(1, collection.getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            source().is(Transaction.Source.CONTROL_PANEL);

        collection = gateway.transaction().search(searchRequest);

        assertEquals(0, collection.getMaximumSize());

        searchRequest = new TransactionSearchRequest().
          id().is(transaction.getId()).
          source().in(Transaction.Source.API, Transaction.Source.RECURRING);

        collection = gateway.transaction().search(searchRequest);

        assertEquals(1, collection.getMaximumSize());

        searchRequest = new TransactionSearchRequest().
          id().is(transaction.getId()).
          source().in(Transaction.Source.RECURRING);

        collection = gateway.transaction().search(searchRequest);

        assertEquals(0, collection.getMaximumSize());
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

        assertEquals(2, collection.getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            creditCardCardholderName().is(name).
            type().is(Transaction.Type.SALE);

        collection = gateway.transaction().search(searchRequest);

        assertEquals(1, collection.getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            creditCardCardholderName().is(name).
            type().is(Transaction.Type.CREDIT).
            refund().is(true);

        collection = gateway.transaction().search(searchRequest);

        assertEquals(1, collection.getMaximumSize());
        assertEquals(refundTransaction.getId(), collection.getFirst().getId());

        searchRequest = new TransactionSearchRequest().
            creditCardCardholderName().is(name).
            type().is(Transaction.Type.CREDIT).
            refund().is(false);

        collection = gateway.transaction().search(searchRequest);

        assertEquals(1, collection.getMaximumSize());
        assertEquals(creditTransaction.getId(), collection.getFirst().getId());
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

        assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            amount().greaterThanOrEqualTo(new BigDecimal("500"));

        assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            amount().lessThanOrEqualTo(new BigDecimal("1500"));

        assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            amount().between(new BigDecimal("1300"), new BigDecimal("1500"));

        assertEquals(0, gateway.transaction().search(searchRequest).getMaximumSize());
    }

    @Test
    public void searchOnDisbursementDate() throws ParseException {
        Calendar disbursementTime = CalendarTestUtils.dateTime("2013-04-10T00:00:00Z");

        Calendar threeDaysEarlier = ((Calendar) disbursementTime.clone());
        threeDaysEarlier.add(Calendar.DAY_OF_MONTH, -3);

        Calendar oneDayEarlier = ((Calendar) disbursementTime.clone());
        oneDayEarlier.add(Calendar.DAY_OF_MONTH, -1);

        Calendar oneDayLater = ((Calendar) disbursementTime.clone());
        oneDayLater.add(Calendar.DAY_OF_MONTH, 1);

        TransactionSearchRequest searchRequest = new TransactionSearchRequest().
                id().is(DISBURSEMENT_TRANSACTION_ID).
                disbursementDate().between(oneDayEarlier, oneDayLater);

        assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
                id().is(DISBURSEMENT_TRANSACTION_ID).
                disbursementDate().greaterThanOrEqualTo(oneDayEarlier);

        assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
                id().is(DISBURSEMENT_TRANSACTION_ID).
                disbursementDate().lessThanOrEqualTo(oneDayLater);

        assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
                id().is(DISBURSEMENT_TRANSACTION_ID).
                disbursementDate().between(threeDaysEarlier, oneDayEarlier);

        assertEquals(0, gateway.transaction().search(searchRequest).getMaximumSize());
    }

    @Test
    public void searchOnDisbursementDateUsingLocalTime() throws ParseException {

        Calendar oneDayEarlier = CalendarTestUtils.dateTime("2013-04-09T00:00:00Z", "CST");
        Calendar oneDayLater = CalendarTestUtils.dateTime("2013-04-11T00:00:00Z", "CST");

        TransactionSearchRequest searchRequest = new TransactionSearchRequest().
                id().is(DISBURSEMENT_TRANSACTION_ID).
                disbursementDate().between(oneDayEarlier, oneDayLater);

        assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());
    }

    @Test
    public void searchOnDisputeDate() throws ParseException, InterruptedException {
        Calendar today = Calendar.getInstance();

        Calendar threeDaysEarlier = ((Calendar) today.clone());
        threeDaysEarlier.add(Calendar.DAY_OF_MONTH, -3);

        Calendar oneDayEarlier = ((Calendar) today.clone());
        oneDayEarlier.add(Calendar.DAY_OF_MONTH, -1);

        Calendar oneDayLater = ((Calendar) today.clone());
        oneDayLater.add(Calendar.DAY_OF_MONTH, 1);

        Transaction transaction = this.getDisputedTransaction();

        TransactionSearchRequest searchRequest = new TransactionSearchRequest()
            .id().is(transaction.getId())
            .disputeDate().between(oneDayEarlier, oneDayLater);

        ResourceCollection<Transaction> collection = gateway.transaction().search(searchRequest);

        assertEquals(1, collection.getMaximumSize());

        searchRequest = new TransactionSearchRequest().
                id().is(transaction.getId()).
                disputeDate().greaterThanOrEqualTo(oneDayEarlier);

        assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
                id().is(transaction.getId()).
                disputeDate().lessThanOrEqualTo(oneDayLater);

        assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
                id().is(transaction.getId()).
                disputeDate().between(threeDaysEarlier, oneDayEarlier);

        assertEquals(0, gateway.transaction().search(searchRequest).getMaximumSize());
    }

    @Test
    public void searchOnDisputeDateUsingLocalTime() throws ParseException, InterruptedException {
        Transaction transaction = this.getDisputedTransaction();

        Calendar oneDayEarlier = CalendarTestUtils.today("CST");
        oneDayEarlier.add(Calendar.DATE, -1);

        Calendar oneDayLater = CalendarTestUtils.today("CST");
        oneDayLater.add(Calendar.DATE, 1);

        TransactionSearchRequest searchRequest = new TransactionSearchRequest().
                id().is(transaction.getId()).
                disputeDate().between(oneDayEarlier, oneDayLater);

        ResourceCollection<Transaction> collection = null;

        for (int i=0; i<90; i++) {
            Thread.sleep(1000);

            collection = gateway.transaction().search(searchRequest);

            if (collection.getMaximumSize() > 0) {
                break;
            }
        }

        assertEquals(1, collection.getMaximumSize());
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

        Calendar threeDaysEarlier = ((Calendar) createdAt.clone());
        threeDaysEarlier.add(Calendar.DAY_OF_MONTH, -3);

        Calendar oneDayEarlier = ((Calendar) createdAt.clone());
        oneDayEarlier.add(Calendar.DAY_OF_MONTH, -1);

        Calendar oneDayLater = ((Calendar) createdAt.clone());
        oneDayLater.add(Calendar.DAY_OF_MONTH, 1);

        TransactionSearchRequest searchRequest = new TransactionSearchRequest().
                id().is(transaction.getId()).
                createdAt().between(oneDayEarlier, oneDayLater);

        assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
                id().is(transaction.getId()).
                createdAt().greaterThanOrEqualTo(oneDayEarlier);

        assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
                id().is(transaction.getId()).
                createdAt().lessThanOrEqualTo(oneDayLater);

        assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
                id().is(transaction.getId()).
                createdAt().between(threeDaysEarlier, oneDayEarlier);

        assertEquals(0, gateway.transaction().search(searchRequest).getMaximumSize());
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

        assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());
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

        assertTrue(results.getMaximumSize() > 0);
        assertEquals(Transaction.Status.AUTHORIZATION_EXPIRED, results.getFirst().getStatus());

        searchRequest = new TransactionSearchRequest().
            authorizationExpiredAt().between(threeDaysEarlier, oneDayEarlier);

        assertEquals(0, gateway.transaction().search(searchRequest).getMaximumSize());
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

        assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            authorizedAt().greaterThanOrEqualTo(oneDayEarlier);

        assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            authorizedAt().lessThanOrEqualTo(oneDayLater);

        assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            authorizedAt().between(threeDaysEarlier, oneDayEarlier);

        assertEquals(0, gateway.transaction().search(searchRequest).getMaximumSize());
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

        assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            failedAt().greaterThanOrEqualTo(oneDayEarlier);

        assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            failedAt().lessThanOrEqualTo(oneDayLater);

        assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            failedAt().between(threeDaysEarlier, oneDayEarlier);

        assertEquals(0, gateway.transaction().search(searchRequest).getMaximumSize());
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

        assertEquals(1, processingRulesGateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            gatewayRejectedAt().greaterThanOrEqualTo(oneDayEarlier);

        assertEquals(1, processingRulesGateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            gatewayRejectedAt().lessThanOrEqualTo(oneDayLater);

        assertEquals(1, processingRulesGateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            gatewayRejectedAt().between(threeDaysEarlier, oneDayEarlier);

        assertEquals(0, processingRulesGateway.transaction().search(searchRequest).getMaximumSize());
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

        assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            processorDeclinedAt().greaterThanOrEqualTo(oneDayEarlier);

        assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            processorDeclinedAt().lessThanOrEqualTo(oneDayLater);

        assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            processorDeclinedAt().between(threeDaysEarlier, oneDayEarlier);

        assertEquals(0, gateway.transaction().search(searchRequest).getMaximumSize());
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

        assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            settledAt().greaterThanOrEqualTo(oneDayEarlier);

        assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            settledAt().lessThanOrEqualTo(oneDayLater);

        assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            settledAt().between(threeDaysEarlier, oneDayEarlier);

        assertEquals(0, gateway.transaction().search(searchRequest).getMaximumSize());
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

        assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            submittedForSettlementAt().greaterThanOrEqualTo(oneDayEarlier);

        assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            submittedForSettlementAt().lessThanOrEqualTo(oneDayLater);

        assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            submittedForSettlementAt().between(threeDaysEarlier, oneDayEarlier);

        assertEquals(0, gateway.transaction().search(searchRequest).getMaximumSize());
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

        assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            voidedAt().greaterThanOrEqualTo(oneDayEarlier);

        assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            voidedAt().lessThanOrEqualTo(oneDayLater);

        assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            voidedAt().between(threeDaysEarlier, oneDayEarlier);

        assertEquals(0, gateway.transaction().search(searchRequest).getMaximumSize());
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

        assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());

        searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            authorizedAt().between(threeDaysEarlier, oneDayEarlier).
            submittedForSettlementAt().between(threeDaysEarlier, oneDayEarlier);

        assertEquals(0, gateway.transaction().search(searchRequest).getMaximumSize());
    }

    @Test
    public void searchOnPayPalFields() {
        String nonce = TestHelper.generateOneTimePayPalNonce(gateway);
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            paymentMethodNonce(nonce);

        Transaction transaction = gateway.transaction().sale(request).getTarget();

        TransactionSearchRequest searchRequest = new TransactionSearchRequest().
            id().is(transaction.getId()).
            paypalPaymentId().startsWith("PAY").
            paypalPayerEmail().is("payer@example.com").
            paypalAuthorizationId().startsWith("AUTH");

        assertEquals(1, gateway.transaction().search(searchRequest).getMaximumSize());
    }

    @Test(expected = DownForMaintenanceException.class)
    public void searchReturnsAndHandlesInvalidCriteria() {
        TransactionSearchRequest searchRequest = new TransactionSearchRequest().
            amount().is(new BigDecimal("-500"));

        gateway.transaction().search(searchRequest);
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
        assertTrue(result.isSuccess());

        Transaction refund = result.getTarget();
        Transaction originalTransaction = gateway.transaction().find(transactionId);

        assertEquals(Transaction.Type.CREDIT, refund.getType());
        assertEquals(originalTransaction.getAmount(), refund.getAmount());
        assertEquals(refund.getId(), originalTransaction.getRefundId());
        assertEquals(originalTransaction.getId(), refund.getRefundedTransactionId());
    }

    @Test
    public void refundTransactionWithOrderId() {
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

        TransactionRefundRequest refundRequest = new TransactionRefundRequest().orderId("12345678");

        Result<Transaction> result = gateway.transaction().refund(transaction.getId(), refundRequest);
        assertTrue(result.isSuccess());
        assertEquals("12345678", result.getTarget().getOrderId());
    }

    @Test
    public void refundTransactionWithAmountAndOrderId() {
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

        TransactionRefundRequest refundRequest = new TransactionRefundRequest().
            orderId("12345678").
            amount(TransactionAmount.AUTHORIZE.amount.divide(new BigDecimal("2")));

        Result<Transaction> result = gateway.transaction().refund(transaction.getId(), refundRequest);
        assertTrue(result.isSuccess());
        assertEquals("12345678", result.getTarget().getOrderId());
        assertEquals(TransactionAmount.AUTHORIZE.amount.divide(new BigDecimal("2")), result.getTarget().getAmount());
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
        assertTrue(result.isSuccess());
        assertEquals(Transaction.Type.CREDIT, result.getTarget().getType());
        assertEquals(TransactionAmount.AUTHORIZE.amount.divide(new BigDecimal(2)), result.getTarget().getAmount());
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
        assertEquals(Transaction.Type.CREDIT, refund1.getType());
        assertEquals(TransactionAmount.AUTHORIZE.amount.divide(new BigDecimal(2)), refund1.getAmount());

        Transaction refund2 = gateway.transaction().refund(transaction.getId(), TransactionAmount.AUTHORIZE.amount.divide(new BigDecimal(2))).getTarget();
        assertEquals(Transaction.Type.CREDIT, refund2.getType());
        assertEquals(TransactionAmount.AUTHORIZE.amount.divide(new BigDecimal(2)), refund2.getAmount());

        transaction = gateway.transaction().find(transaction.getId());
        assertTrue(TestHelper.listIncludes(transaction.getRefundIds(), refund1.getId()));
        assertTrue(TestHelper.listIncludes(transaction.getRefundIds(), refund1.getId()));
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
        assertEquals(Transaction.Status.AUTHORIZED, transaction.getStatus());

        Result<Transaction> result = gateway.transaction().refund(transaction.getId());
        assertFalse(result.isSuccess());

        assertEquals(ValidationErrorCode.TRANSACTION_CANNOT_REFUND_UNLESS_SETTLED,
                result.getErrors().forObject("transaction").onField("base").get(0).getCode());
    }

    @Test
    public void unrecognizedStatus() {
        String xml = "<transaction><status>foobar</status><billing/><credit-card/><customer/><descriptor/><shipping/><subscription/><service-fee></service-fee><disbursement-details/><type>sale</type></transaction>";
        Transaction transaction = new Transaction(NodeWrapperFactory.instance.create(xml));
        assertEquals(Transaction.Status.UNRECOGNIZED, transaction.getStatus());
    }

    @Test
    public void unrecognizedType() {
        String xml = "<transaction><type>foobar</type><billing/><credit-card/><customer/><descriptor/><shipping/><subscription/><service-fee></service-fee><disbursement-details/><type>sale</type></transaction>";
        Transaction transaction = new Transaction(NodeWrapperFactory.instance.create(xml));
        assertEquals(Transaction.Type.UNRECOGNIZED, transaction.getType());
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
        assertFalse(result.isSuccess());
        Transaction transaction = result.getTransaction();

        assertEquals(Transaction.GatewayRejectionReason.CVV, transaction.getGatewayRejectionReason());
    }

    @Test
    public void gatewayRejectedOnApplicationIncomplete() {
        gateway = new BraintreeGateway("client_id$development$integration_client_id", "client_secret$development$integration_client_secret");

        MerchantRequest request = new MerchantRequest().
            email("name@email.com").
            countryCodeAlpha3("USA").
            paymentMethods(Arrays.asList("credit_card", "paypal"));

        Result<Merchant> merchantResult = gateway.merchant().create(request);

        gateway = new BraintreeGateway(merchantResult.getTarget().getCredentials().getAccessToken());

        TransactionRequest transactionRequest = new TransactionRequest().
            amount(new BigDecimal(4000.00)).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2020").
                done();

        Result<Transaction> result = gateway.transaction().sale(transactionRequest);
        assertFalse(result.isSuccess());
        Transaction transaction = result.getTransaction();

        assertEquals(Transaction.GatewayRejectionReason.APPLICATION_INCOMPLETE, transaction.getGatewayRejectionReason());
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
        assertFalse(result.isSuccess());
        Transaction transaction = result.getTransaction();

        assertEquals(Transaction.GatewayRejectionReason.AVS, transaction.getGatewayRejectionReason());
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
        assertFalse(result.isSuccess());
        Transaction transaction = result.getTransaction();

        assertEquals(Transaction.GatewayRejectionReason.AVS_AND_CVV, transaction.getGatewayRejectionReason());
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

        assertEquals(PlanFixture.PLAN_WITHOUT_TRIAL.getId(), transaction.getPlanId());

        List<AddOn> addOns = transaction.getAddOns();
        Collections.sort(addOns, new TestHelper.CompareModificationsById());

        assertEquals(2, addOns.size());

        assertEquals("increase_10", addOns.get(0).getId());
        assertEquals(new BigDecimal("11.00"), addOns.get(0).getAmount());
        assertEquals(new Integer(5), addOns.get(0).getNumberOfBillingCycles());
        assertEquals(new Integer(2), addOns.get(0).getQuantity());
        assertFalse(addOns.get(0).neverExpires());

        assertEquals("increase_20", addOns.get(1).getId());
        assertEquals(new BigDecimal("21.00"), addOns.get(1).getAmount());
        assertEquals(new Integer(6), addOns.get(1).getNumberOfBillingCycles());
        assertEquals(new Integer(3), addOns.get(1).getQuantity());
        assertFalse(addOns.get(1).neverExpires());

        List<Discount> discounts = transaction.getDiscounts();
        assertEquals(1, discounts.size());

        assertEquals("discount_7", discounts.get(0).getId());
        assertEquals(new BigDecimal("7.50"), discounts.get(0).getAmount());
        assertNull(discounts.get(0).getNumberOfBillingCycles());
        assertEquals(new Integer(2), discounts.get(0).getQuantity());
        assertTrue(discounts.get(0).neverExpires());
    }

    @Test
    public void serviceFee() {
        TransactionRequest request = new TransactionRequest().
                merchantAccountId(NON_DEFAULT_SUB_MERCHANT_ACCOUNT_ID).
                amount(new BigDecimal("100.00")).
                creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
                serviceFeeAmount(new BigDecimal("1.00"));

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();

        assertEquals(new BigDecimal("1.00"), transaction.getServiceFeeAmount());
    }

    @Test
    public void serviceFeeNotAllowedForMasterMerchant() {
        TransactionRequest request = new TransactionRequest().
            merchantAccountId(NON_DEFAULT_MERCHANT_ACCOUNT_ID).
            amount(new BigDecimal("100.00")).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2017").
                done().
            serviceFeeAmount(new BigDecimal("1.00"));

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());
        assertEquals(ValidationErrorCode.TRANSACTION_SERVICE_FEE_AMOUNT_NOT_ALLOWED_ON_MASTER_MERCHANT_ACCOUNT,
            result.getErrors().forObject("transaction").onField("service_fee_amount").get(0).getCode());
    }

    @Test
    public void serviceFeeRequiredWhenUsingSubmerchant() {
        TransactionRequest request = new TransactionRequest().
            merchantAccountId(NON_DEFAULT_SUB_MERCHANT_ACCOUNT_ID).
            amount(new BigDecimal("100.00")).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());

        assertEquals(ValidationErrorCode.TRANSACTION_SUB_MERCHANT_ACCOUNT_REQUIRES_SERVICE_FEE_AMOUNT,
                result.getErrors().forObject("transaction").onField("merchant_account_id").get(0).getCode());
    }

    @Test
    public void negativeServiceFee() {
        TransactionRequest request = new TransactionRequest().
                merchantAccountId(NON_DEFAULT_SUB_MERCHANT_ACCOUNT_ID).
                amount(new BigDecimal("100.00")).
                creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
                serviceFeeAmount(new BigDecimal("-1.00"));

        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());
        assertEquals(ValidationErrorCode.TRANSACTION_SERVICE_FEE_AMOUNT_CANNOT_BE_NEGATIVE,
                result.getErrors().forObject("transaction").onField("service_fee_amount").get(0).getCode());
    }

    @Test
    public void holdInEscrowOnCreate() {
        TransactionRequest request = new TransactionRequest().
            merchantAccountId(NON_DEFAULT_SUB_MERCHANT_ACCOUNT_ID).
            amount(new BigDecimal("100.00")).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            serviceFeeAmount(new BigDecimal("1.00")).
            options().
                holdInEscrow(true).
                done();
        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
        assertEquals(
                Transaction.EscrowStatus.HOLD_PENDING,
                result.getTarget().getEscrowStatus()
                );
    }

    @Test
    public void holdInEscrowOnSaleForMasterMerchantAccount() {
        TransactionRequest request = new TransactionRequest().
            merchantAccountId(NON_DEFAULT_MERCHANT_ACCOUNT_ID).
            amount(new BigDecimal("100.00")).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2009").
                done().
            serviceFeeAmount(new BigDecimal("1.00")).
            options().
                holdInEscrow(true).
                done();
        Result<Transaction> result = gateway.transaction().sale(request);
        assertFalse(result.isSuccess());
        assertEquals(
                ValidationErrorCode.TRANSACTION_CANNOT_HOLD_IN_ESCROW,
                result.getErrors().forObject("transaction").onField("base").get(0).getCode()
                );
    }

    @Test
    public void holdInEscrowAfterSale() {
        TransactionRequest request = new TransactionRequest().
            merchantAccountId(NON_DEFAULT_SUB_MERCHANT_ACCOUNT_ID).
            amount(new BigDecimal("100.00")).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2012").
                done().
            serviceFeeAmount(new BigDecimal("1.00"));
        Result<Transaction> sale = gateway.transaction().sale(request);
        assertTrue(sale.isSuccess());
        String transactionID = sale.getTarget().getId();
        Result<Transaction> holdInEscrow = gateway.transaction().holdInEscrow(transactionID);
        assertTrue(holdInEscrow.isSuccess());
        assertEquals(
                Transaction.EscrowStatus.HOLD_PENDING,
                holdInEscrow.getTarget().getEscrowStatus()
                );
    }

    @Test
    public void holdInEscrowAfterSaleFailsForMasterMerchants() {
        TransactionRequest request = new TransactionRequest().
            merchantAccountId(NON_DEFAULT_MERCHANT_ACCOUNT_ID).
            amount(new BigDecimal("100.00")).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2012").
                done();
        Result<Transaction> sale = gateway.transaction().sale(request);
        assertTrue(sale.isSuccess());
        Result<Transaction> holdInEscrow = gateway.transaction().holdInEscrow(sale.getTarget().getId());
        assertFalse(holdInEscrow.isSuccess());
        assertEquals(
                ValidationErrorCode.TRANSACTION_CANNOT_HOLD_IN_ESCROW,
                holdInEscrow.getErrors().forObject("transaction").onField("base").get(0).getCode()
                );
    }

    @Test
    public void releaseFromEscrow() {
        TransactionRequest request = new TransactionRequest().
            merchantAccountId(NON_DEFAULT_SUB_MERCHANT_ACCOUNT_ID).
            amount(new BigDecimal("100.00")).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2012").
                done().
            serviceFeeAmount(new BigDecimal("1.00"));
        Result<Transaction> saleResult = gateway.transaction().sale(request);
        assertTrue(saleResult.isSuccess());
        TestHelper.escrow(gateway, saleResult.getTarget().getId());
        Result<Transaction> releaseResult = gateway.transaction().releaseFromEscrow(saleResult.getTarget().getId());
        assertTrue(releaseResult.isSuccess());
        assertEquals(
                Transaction.EscrowStatus.RELEASE_PENDING,
                releaseResult.getTarget().getEscrowStatus()
                );
    }

    @Test
    public void releaseFromEscrowFailsWhenTransactionIsNotEscrowed() {
        TransactionRequest request = new TransactionRequest().
            merchantAccountId(NON_DEFAULT_SUB_MERCHANT_ACCOUNT_ID).
            amount(new BigDecimal("100.00")).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2012").
                done().
            serviceFeeAmount(new BigDecimal("1.00"));
        Result<Transaction> saleResult = gateway.transaction().sale(request);
        assertTrue(saleResult.isSuccess());
        Result<Transaction> releaseResult = gateway.transaction().releaseFromEscrow(saleResult.getTarget().getId());
        assertFalse(releaseResult.isSuccess());
        assertEquals(
                ValidationErrorCode.TRANSACTION_CANNOT_RELEASE_FROM_ESCROW,
                releaseResult.getErrors().forObject("transaction").onField("base").get(0).getCode()
                );
    }

    @Test
    public void cancelReleaseSucceeds() {
        TransactionRequest request = new TransactionRequest().
            merchantAccountId(NON_DEFAULT_SUB_MERCHANT_ACCOUNT_ID).
            amount(new BigDecimal("100.00")).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2012").
                done().
            serviceFeeAmount(new BigDecimal("1.00"));
        Result<Transaction> saleResult = gateway.transaction().sale(request);
        assertTrue(saleResult.isSuccess());
        TestHelper.escrow(gateway, saleResult.getTarget().getId());
        Result<Transaction> releaseResult = gateway.transaction().releaseFromEscrow(saleResult.getTarget().getId());
        Result<Transaction> cancelResult = gateway.transaction().cancelRelease(saleResult.getTarget().getId());
        assertTrue(cancelResult.isSuccess());
        assertEquals(
                Transaction.EscrowStatus.HELD,
                cancelResult.getTarget().getEscrowStatus()
                );
    }

    @Test
    public void cancelReleaseFailsReleasingNonPendingTransactions() {
        TransactionRequest request = new TransactionRequest().
            merchantAccountId(NON_DEFAULT_SUB_MERCHANT_ACCOUNT_ID).
            amount(new BigDecimal("100.00")).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2012").
                done().
            serviceFeeAmount(new BigDecimal("1.00"));
        Result<Transaction> saleResult = gateway.transaction().sale(request);
        assertTrue(saleResult.isSuccess());
        TestHelper.escrow(gateway, saleResult.getTarget().getId());
        Result<Transaction> cancelResult = gateway.transaction().cancelRelease(saleResult.getTarget().getId());
        assertFalse(cancelResult.isSuccess());
        assertEquals(
                ValidationErrorCode.TRANSACTION_CANNOT_CANCEL_RELEASE,
                cancelResult.getErrors().forObject("transaction").onField("base").get(0).getCode()
                );
    }

    @Test
    public void createOneTimePayPalTransaction() {
        String nonce = TestHelper.generateOneTimePayPalNonce(gateway);
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("100.00")).
            paymentMethodNonce(nonce);

        Result<Transaction> saleResult = gateway.transaction().sale(request);

        assertTrue(saleResult.isSuccess());
        assertNotNull(saleResult.getTarget().getPayPalDetails());
        assertNotNull(saleResult.getTarget().getPayPalDetails().getPayerEmail());
        assertNotNull(saleResult.getTarget().getPayPalDetails().getPaymentId());
        assertNotNull(saleResult.getTarget().getPayPalDetails().getAuthorizationId());
        assertNotNull(saleResult.getTarget().getPayPalDetails().getImageUrl());
        assertNotNull(saleResult.getTarget().getPayPalDetails().getDebugId());
        assertNull(saleResult.getTarget().getPayPalDetails().getToken());

        assertEquals(
            PaymentInstrumentType.PAYPAL_ACCOUNT,
            saleResult.getTarget().getPaymentInstrumentType()
        );
    }

    @Test
    public void createPayPalTransactionWithLocalPaymentContent() {
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("100.00")).
            options().
                submitForSettlement(true).
                done().
            paypalAccount().
              payerId("fake-payer-id").
              paymentId("fake-payment-id").
              done();

        Result<Transaction> saleResult = gateway.transaction().sale(request);

        assertTrue(saleResult.isSuccess());
        assertNotNull(saleResult.getTarget().getPayPalDetails());
        assertEquals("fake-payer-id", saleResult.getTarget().getPayPalDetails().getPayerId());
        assertEquals("fake-payment-id", saleResult.getTarget().getPayPalDetails().getPaymentId());
        assertEquals(
            PaymentInstrumentType.PAYPAL_ACCOUNT,
            saleResult.getTarget().getPaymentInstrumentType()
        );
    }

    @Test
    public void createPayPalTransactionWithPayeeId() {
        String nonce = TestHelper.generateOneTimePayPalNonce(gateway);
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("100.00")).
            paymentMethodNonce(nonce).
            paypalAccount().
              payeeId("fake-payee-id").
              done();

        Result<Transaction> saleResult = gateway.transaction().sale(request);

        assertTrue(saleResult.isSuccess());
        assertNotNull(saleResult.getTarget().getPayPalDetails());
        assertNotNull(saleResult.getTarget().getPayPalDetails().getPayerId());
        assertNotNull(saleResult.getTarget().getPayPalDetails().getPaymentId());
        assertNotNull(saleResult.getTarget().getPayPalDetails().getAuthorizationId());
        assertNotNull(saleResult.getTarget().getPayPalDetails().getImageUrl());
        assertNotNull(saleResult.getTarget().getPayPalDetails().getDebugId());
        assertNull(saleResult.getTarget().getPayPalDetails().getToken());
        assertEquals("fake-payee-id", saleResult.getTarget().getPayPalDetails().getPayeeId());
        assertEquals(
            PaymentInstrumentType.PAYPAL_ACCOUNT,
            saleResult.getTarget().getPaymentInstrumentType()
        );
    }

    @Test
    public void createPayPalTransactionWithPayeeIdInOptionsParams() {
        String nonce = TestHelper.generateOneTimePayPalNonce(gateway);
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("100.00")).
            paymentMethodNonce(nonce).
            paypalAccount().
              done().
            options().
              payeeId("fake-payee-id").
              done();

        Result<Transaction> saleResult = gateway.transaction().sale(request);

        assertTrue(saleResult.isSuccess());
        assertNotNull(saleResult.getTarget().getPayPalDetails());
        assertNotNull(saleResult.getTarget().getPayPalDetails().getPayerId());
        assertNotNull(saleResult.getTarget().getPayPalDetails().getPaymentId());
        assertNotNull(saleResult.getTarget().getPayPalDetails().getAuthorizationId());
        assertNotNull(saleResult.getTarget().getPayPalDetails().getImageUrl());
        assertNotNull(saleResult.getTarget().getPayPalDetails().getDebugId());
        assertNull(saleResult.getTarget().getPayPalDetails().getToken());
        assertEquals("fake-payee-id", saleResult.getTarget().getPayPalDetails().getPayeeId());
        assertEquals(
            PaymentInstrumentType.PAYPAL_ACCOUNT,
            saleResult.getTarget().getPaymentInstrumentType()
        );
    }

    @Test
    public void createPayPalTransactionWithPayeeIdInOptionsPayPalParams() {
        String nonce = TestHelper.generateOneTimePayPalNonce(gateway);
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("100.00")).
            paymentMethodNonce(nonce).
            paypalAccount().
              done().
            options().
              paypal().
                payeeId("fake-payee-id").
                done().
              done();

        Result<Transaction> saleResult = gateway.transaction().sale(request);

        assertTrue(saleResult.isSuccess());
        assertNotNull(saleResult.getTarget().getPayPalDetails());
        assertNotNull(saleResult.getTarget().getPayPalDetails().getPayerId());
        assertNotNull(saleResult.getTarget().getPayPalDetails().getPaymentId());
        assertNotNull(saleResult.getTarget().getPayPalDetails().getAuthorizationId());
        assertNotNull(saleResult.getTarget().getPayPalDetails().getImageUrl());
        assertNotNull(saleResult.getTarget().getPayPalDetails().getDebugId());
        assertNull(saleResult.getTarget().getPayPalDetails().getToken());
        assertEquals("fake-payee-id", saleResult.getTarget().getPayPalDetails().getPayeeId());
        assertEquals(
            PaymentInstrumentType.PAYPAL_ACCOUNT,
            saleResult.getTarget().getPaymentInstrumentType()
        );
    }

    @Test
    public void createPayPalTransactionWithPayeeEmail() {
        String nonce = TestHelper.generateOneTimePayPalNonce(gateway);
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("100.00")).
            paymentMethodNonce(nonce).
            paypalAccount().
              payeeEmail("payee@example.com").
              done();

        Result<Transaction> saleResult = gateway.transaction().sale(request);

        assertTrue(saleResult.isSuccess());
        assertNotNull(saleResult.getTarget().getPayPalDetails());
        assertNotNull(saleResult.getTarget().getPayPalDetails().getPayerEmail());
        assertNotNull(saleResult.getTarget().getPayPalDetails().getPaymentId());
        assertNotNull(saleResult.getTarget().getPayPalDetails().getAuthorizationId());
        assertNotNull(saleResult.getTarget().getPayPalDetails().getImageUrl());
        assertNotNull(saleResult.getTarget().getPayPalDetails().getDebugId());
        assertNull(saleResult.getTarget().getPayPalDetails().getToken());
        assertEquals("payee@example.com", saleResult.getTarget().getPayPalDetails().getPayeeEmail());
        assertEquals(
            PaymentInstrumentType.PAYPAL_ACCOUNT,
            saleResult.getTarget().getPaymentInstrumentType()
        );
    }

    @Test
    public void createPayPalTransactionWithPayeeEmailInOptionsParams() {
        String nonce = TestHelper.generateOneTimePayPalNonce(gateway);
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("100.00")).
            paymentMethodNonce(nonce).
            paypalAccount().
              done().
            options().
              payeeEmail("payee@example.com").
              done();

        Result<Transaction> saleResult = gateway.transaction().sale(request);

        assertTrue(saleResult.isSuccess());
        assertNotNull(saleResult.getTarget().getPayPalDetails());
        assertNotNull(saleResult.getTarget().getPayPalDetails().getPayerEmail());
        assertNotNull(saleResult.getTarget().getPayPalDetails().getPaymentId());
        assertNotNull(saleResult.getTarget().getPayPalDetails().getAuthorizationId());
        assertNotNull(saleResult.getTarget().getPayPalDetails().getImageUrl());
        assertNotNull(saleResult.getTarget().getPayPalDetails().getDebugId());
        assertNull(saleResult.getTarget().getPayPalDetails().getToken());
        assertEquals("payee@example.com", saleResult.getTarget().getPayPalDetails().getPayeeEmail());
        assertEquals(
            PaymentInstrumentType.PAYPAL_ACCOUNT,
            saleResult.getTarget().getPaymentInstrumentType()
        );
    }

    @Test
    public void createPayPalTransactionWithPayeeEmailInOptionsPayPalParams() {
        String nonce = TestHelper.generateOneTimePayPalNonce(gateway);
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("100.00")).
            paymentMethodNonce(nonce).
            paypalAccount().
              done().
            options().
              paypal().
                payeeEmail("payee@example.com").
                done().
              done();

        Result<Transaction> saleResult = gateway.transaction().sale(request);

        assertTrue(saleResult.isSuccess());
        assertNotNull(saleResult.getTarget().getPayPalDetails());
        assertNotNull(saleResult.getTarget().getPayPalDetails().getPayerEmail());
        assertNotNull(saleResult.getTarget().getPayPalDetails().getPaymentId());
        assertNotNull(saleResult.getTarget().getPayPalDetails().getAuthorizationId());
        assertNotNull(saleResult.getTarget().getPayPalDetails().getImageUrl());
        assertNotNull(saleResult.getTarget().getPayPalDetails().getDebugId());
        assertNull(saleResult.getTarget().getPayPalDetails().getToken());
        assertEquals("payee@example.com", saleResult.getTarget().getPayPalDetails().getPayeeEmail());
        assertEquals(
            PaymentInstrumentType.PAYPAL_ACCOUNT,
            saleResult.getTarget().getPaymentInstrumentType()
        );
    }

    @Test
    public void createPayPalTransactionWithPayPalCustomField() {
        String nonce = TestHelper.generateOneTimePayPalNonce(gateway);
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("100.00")).
            paymentMethodNonce(nonce).
            paypalAccount().
              done().
            options().
              paypal().
                customField("custom field stuff").
                done().
              done();

        Result<Transaction> saleResult = gateway.transaction().sale(request);

        assertTrue(saleResult.isSuccess());
        assertNotNull(saleResult.getTarget().getPayPalDetails());
        assertNotNull(saleResult.getTarget().getPayPalDetails().getPayerEmail());
        assertNotNull(saleResult.getTarget().getPayPalDetails().getPaymentId());
        assertNotNull(saleResult.getTarget().getPayPalDetails().getAuthorizationId());
        assertNotNull(saleResult.getTarget().getPayPalDetails().getImageUrl());
        assertNotNull(saleResult.getTarget().getPayPalDetails().getDebugId());
        assertNull(saleResult.getTarget().getPayPalDetails().getToken());
        assertEquals("custom field stuff", saleResult.getTarget().getPayPalDetails().getCustomField());
        assertEquals(
            PaymentInstrumentType.PAYPAL_ACCOUNT,
            saleResult.getTarget().getPaymentInstrumentType()
        );
    }

    @Test
    public void createPayPalTransactionWithPayPalSupplementaryData() {
        String nonce = TestHelper.generateOneTimePayPalNonce(gateway);
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("100.00")).
            paymentMethodNonce(nonce).
            paypalAccount().
              done().
            options().
              paypal().
                supplementaryData("key1", "value1").
                supplementaryData("key2", "value2").
                done().
              done();

        Result<Transaction> saleResult = gateway.transaction().sale(request);

        // note - supplementary data is not returned in response
        assertTrue(saleResult.isSuccess());
    }

    @Test
    public void createPayPalTransactionWithPayPalDescription() {
        String nonce = TestHelper.generateOneTimePayPalNonce(gateway);
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("100.00")).
            paymentMethodNonce(nonce).
            paypalAccount().
              done().
            options().
              paypal().
                description("Product description").
                done().
              done();

        Result<Transaction> saleResult = gateway.transaction().sale(request);

        assertTrue(saleResult.isSuccess());
        assertNotNull(saleResult.getTarget().getPayPalDetails());
        assertEquals("Product description", saleResult.getTarget().getPayPalDetails().getDescription());
    }

    @Test
    public void createOneTimePayPalTransactionAndAttemptToVault() {
        String nonce = TestHelper.generateOneTimePayPalNonce(gateway);
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("100.00")).
            paymentMethodNonce(nonce).
            options().
                storeInVault(true).
                done();

        Result<Transaction> saleResult = gateway.transaction().sale(request);

        assertTrue(saleResult.isSuccess());
        assertNotNull(saleResult.getTarget().getPayPalDetails());
        assertNotNull(saleResult.getTarget().getPayPalDetails().getPayerEmail());
        assertNotNull(saleResult.getTarget().getPayPalDetails().getPaymentId());
        assertNotNull(saleResult.getTarget().getPayPalDetails().getAuthorizationId());
        assertNotNull(saleResult.getTarget().getPayPalDetails().getDebugId());
        assertNull(saleResult.getTarget().getPayPalDetails().getToken());
    }

    @Test
    public void createFuturePaymentPayPalTransactionAndAttemptToVault() {
        String nonce = TestHelper.generateFuturePaymentPayPalNonce(gateway);
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("100.00")).
            paymentMethodNonce(nonce).
            options().
                storeInVault(true).
                done();

        Result<Transaction> saleResult = gateway.transaction().sale(request);

        assertTrue(saleResult.isSuccess());
        assertNotNull(saleResult.getTarget().getPayPalDetails());
        assertNotNull(saleResult.getTarget().getPayPalDetails().getPayerEmail());
        assertNotNull(saleResult.getTarget().getPayPalDetails().getPaymentId());
        assertNotNull(saleResult.getTarget().getPayPalDetails().getAuthorizationId());
        assertNotNull(saleResult.getTarget().getPayPalDetails().getToken());
        assertNotNull(saleResult.getTarget().getPayPalDetails().getDebugId());
    }

    @Test
    public void createPayPalTransactionFromVaultRecord() {
        String nonce = TestHelper.generateFuturePaymentPayPalNonce(gateway);
        Result<Customer> customerResult = gateway.customer().create(new CustomerRequest());
        assertTrue(customerResult.isSuccess());
        Customer customer = customerResult.getTarget();

        PaymentMethodRequest vaultRequest = new PaymentMethodRequest().
            customerId(customer.getId()).
            paymentMethodNonce(nonce);

        Result<? extends PaymentMethod> vaultResult = gateway.paymentMethod().create(vaultRequest);
        assertTrue(vaultResult.isSuccess());

        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("100.00")).
            paymentMethodToken(vaultResult.getTarget().getToken());

        Result<Transaction> saleResult = gateway.transaction().sale(request);

        assertTrue(saleResult.isSuccess());
        assertNotNull(saleResult.getTarget().getPayPalDetails());
        assertNotNull(saleResult.getTarget().getPayPalDetails().getPayerEmail());
        assertNotNull(saleResult.getTarget().getPayPalDetails().getPaymentId());
        assertNotNull(saleResult.getTarget().getPayPalDetails().getAuthorizationId());
        assertNotNull(saleResult.getTarget().getPayPalDetails().getToken());
        assertNotNull(saleResult.getTarget().getPayPalDetails().getDebugId());
    }

    @Test
    public void submitPayPalTransactionForSettlement() {
        String nonce = TestHelper.generateOneTimePayPalNonce(gateway);
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("100.00")).
            paymentMethodNonce(nonce);

        Result<Transaction> saleResult = gateway.transaction().sale(request);

        assertTrue(saleResult.isSuccess());

        Result<Transaction> submitForSettlementResult = gateway.transaction().submitForSettlement(saleResult.getTarget().getId());
        assertTrue(submitForSettlementResult.isSuccess());
        assertEquals(Transaction.Status.SETTLING, submitForSettlementResult.getTarget().getStatus());
    }

    @Test
    public void voidPayPalTransaction() {
        String nonce = TestHelper.generateOneTimePayPalNonce(gateway);
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("100.00")).
            paymentMethodNonce(nonce);

        Result<Transaction> saleResult = gateway.transaction().sale(request);
        assertTrue(saleResult.isSuccess());

        Result<Transaction> submitForSettlementResult = gateway.transaction().voidTransaction(saleResult.getTarget().getId());
        assertTrue(submitForSettlementResult.isSuccess());
        assertEquals(Transaction.Status.VOIDED, submitForSettlementResult.getTarget().getStatus());
    }

    @Test
    public void refundPayPalTransaction() {
        String nonce = TestHelper.generateOneTimePayPalNonce(gateway);
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            paymentMethodNonce(nonce).
            options().
                submitForSettlement(true).
                done();
        Transaction transaction = gateway.transaction().sale(request).getTarget();
        TestHelper.settle(gateway, transaction.getId());

        Result<Transaction> result = gateway.transaction().refund(transaction.getId(), TransactionAmount.AUTHORIZE.amount.divide(new BigDecimal(2)));
        assertTrue(result.isSuccess());
        assertEquals(Transaction.Type.CREDIT, result.getTarget().getType());
        assertEquals(TransactionAmount.AUTHORIZE.amount.divide(new BigDecimal(2)), result.getTarget().getAmount());
    }

    @Test
    public void returnsAllRequiredPaypalFields() {
        Transaction transaction = gateway.transaction().find("settledtransaction");
        assertNotNull(transaction.getPayPalDetails().getDebugId());
        assertNotNull(transaction.getPayPalDetails().getPayerEmail());
        assertNotNull(transaction.getPayPalDetails().getAuthorizationId());
        assertNotNull(transaction.getPayPalDetails().getPayerId());
        assertNotNull(transaction.getPayPalDetails().getPayerFirstName());
        assertNotNull(transaction.getPayPalDetails().getPayerLastName());
        assertNotNull(transaction.getPayPalDetails().getPayerStatus());
        assertNotNull(transaction.getPayPalDetails().getSellerProtectionStatus());
        assertNotNull(transaction.getPayPalDetails().getCaptureId());
        assertNotNull(transaction.getPayPalDetails().getRefundId());
        assertNotNull(transaction.getPayPalDetails().getTransactionFeeAmount());
        assertNotNull(transaction.getPayPalDetails().getTransactionFeeCurrencyIsoCode());
        assertNotNull(transaction.getPayPalDetails().getRefundFromTransactionFeeAmount());
        assertNotNull(transaction.getPayPalDetails().getRefundFromTransactionFeeCurrencyIsoCode());
    }

    @Test
    public void successfulPartialSettlementSale()
    {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.MASTER_CARD.number).
                expirationDate("05/2009").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
        Transaction authorizedTransaction = result.getTarget();

        assertEquals(TransactionAmount.AUTHORIZE.amount, authorizedTransaction.getAmount());
        assertEquals(Transaction.Type.SALE, authorizedTransaction.getType());
        assertNotNull(authorizedTransaction.getProcessorAuthorizationCode());
        assertEquals(Transaction.Status.AUTHORIZED, authorizedTransaction.getStatus());

        BigDecimal amount1 = new BigDecimal("400.00");
        Result<Transaction> partialSettlementResult1 = gateway.transaction().submitForPartialSettlement(authorizedTransaction.getId(), amount1);
        Transaction partialSettlementTransaction1 = partialSettlementResult1.getTarget();
        assertEquals(amount1, partialSettlementTransaction1.getAmount());
        assertEquals(Transaction.Type.SALE, partialSettlementTransaction1.getType());
        assertEquals(Transaction.Status.SUBMITTED_FOR_SETTLEMENT, partialSettlementTransaction1.getStatus());
        assertEquals(authorizedTransaction.getId(), partialSettlementTransaction1.getAuthorizedTransactionId());

        BigDecimal amount2 = new BigDecimal("600.00");
        Result<Transaction> partialSettlementResult2 = gateway.transaction().submitForPartialSettlement(authorizedTransaction.getId(), amount2);
        Transaction partialSettlementTransaction2 = partialSettlementResult2.getTarget();
        assertEquals(amount2, partialSettlementTransaction2.getAmount());
        assertEquals(Transaction.Type.SALE, partialSettlementTransaction2.getType());
        assertEquals(Transaction.Status.SUBMITTED_FOR_SETTLEMENT, partialSettlementTransaction2.getStatus());

        Transaction refreshedAuthorizedTransaction = gateway.transaction().find(authorizedTransaction.getId());
        assertEquals(2, refreshedAuthorizedTransaction.getPartialSettlementTransactionIds().size());
    }

    @Test
    public void submitForPartialSettlementWithOrderId() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2008").
                done();
        Transaction transaction = gateway.transaction().sale(request).getTarget();

        BigDecimal amount = new BigDecimal("400.00");
        TransactionRequest submitForPartialSettlementRequest = new TransactionRequest().
            amount(amount).
            orderId("1234");

        Result<Transaction> result = gateway.transaction().submitForPartialSettlement(transaction.getId(), submitForPartialSettlementRequest);

        assertTrue(result.isSuccess());
        assertEquals(Transaction.Status.SUBMITTED_FOR_SETTLEMENT, result.getTarget().getStatus());
        assertEquals("1234", result.getTarget().getOrderId());
    }

    @Test
    public void submitForPartialSettlementWithDescriptors()
    {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.VISA.number).
                expirationDate("05/2008").
                done();
        Transaction authorizedTransaction = gateway.transaction().sale(request).getTarget();

        BigDecimal amount = new BigDecimal("400.00");
        TransactionRequest submitForPartialSettlementRequest = new TransactionRequest().
            amount(amount).
            descriptor().
                name("123*123456789012345678").
                phone("3334445555").
                url("ebay.com").
                done();

        Result<Transaction> result = gateway.transaction().submitForPartialSettlement(authorizedTransaction.getId(), submitForPartialSettlementRequest);

        assertTrue(result.isSuccess());
        assertEquals(Transaction.Status.SUBMITTED_FOR_SETTLEMENT, result.getTarget().getStatus());
        assertEquals("123*123456789012345678", result.getTarget().getDescriptor().getName());
        assertEquals("3334445555", result.getTarget().getDescriptor().getPhone());
        assertEquals("ebay.com", result.getTarget().getDescriptor().getUrl());
    }

    @Test
    public void cannotCreatePartialSettlementTransactionsOnPartialSettlementTransactions() {
        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            creditCard().
                number(CreditCardNumber.MASTER_CARD.number).
                expirationDate("05/2009").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
        Transaction authorizedTransaction = result.getTarget();

        BigDecimal amount1 = new BigDecimal("400.00");
        Result<Transaction> partialSettlementResult1 = gateway.transaction().submitForPartialSettlement(authorizedTransaction.getId(), amount1);
        Transaction partialSettlementTransaction = partialSettlementResult1.getTarget();

        BigDecimal amount2 = new BigDecimal("100.00");
        Result<Transaction> partialSettlementResult2 = gateway.transaction().submitForPartialSettlement(partialSettlementTransaction.getId(), amount2);
        assertFalse(partialSettlementResult2.isSuccess());

        assertEquals(ValidationErrorCode.TRANSACTION_CANNOT_SUBMIT_FOR_PARTIAL_SETTLEMENT,
                partialSettlementResult2.getErrors().forObject("transaction").onField("base").get(0).getCode());
    }

    @Test
    public void sharedPaymentMethodNonce() {
        BraintreeGateway sharerGateway = new BraintreeGateway(Environment.DEVELOPMENT, "integration_merchant_public_id", "oauth_app_partner_user_public_key", "oauth_app_partner_user_private_key");
        Customer customer = sharerGateway.customer().create(new CustomerRequest().
                creditCard().
                number("5105105105105100").
                expirationDate("05/19").
                billingAddress().
                    postalCode("94107").
                    done().
                done()
        ).getTarget();
        CreditCard card = customer.getCreditCards().get(0);
        Address billingAddress = card.getBillingAddress();
        Address shippingAddress = sharerGateway.address().create(customer.getId(),
                new AddressRequest().postalCode("94107")).getTarget();

        BraintreeGateway oauthGateway = new BraintreeGateway("client_id$development$integration_client_id", "client_secret$development$integration_client_secret");
        String code = TestHelper.createOAuthGrant(oauthGateway, "integration_merchant_id", "shared_vault_transactions");

        OAuthCredentialsRequest oauthRequest = new OAuthCredentialsRequest().
             code(code).
             scope("shared_vault_transactions");

        Result<OAuthCredentials> accessTokenResult = oauthGateway.oauth().createTokenFromCode(oauthRequest);

        BraintreeGateway gateway = new BraintreeGateway(accessTokenResult.getTarget().getAccessToken());

        String sharedNonce = sharerGateway.paymentMethodNonce().create(card.getToken()).getTarget().getNonce();

        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            sharedPaymentMethodNonce(sharedNonce).
            sharedCustomerId(customer.getId()).
            sharedShippingAddressId(shippingAddress.getId()).
            sharedBillingAddressId(billingAddress.getId());

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());

        Transaction transaction = result.getTarget();

        assertEquals(transaction.getFacilitatedDetails().getMerchantId(), "integration_merchant_id");
        assertEquals(transaction.getFacilitatedDetails().getMerchantName(), "14ladders");
        assertEquals(transaction.getFacilitatedDetails().getPaymentMethodNonce(), null);
        assertEquals(transaction.getFacilitatorDetails().getOauthApplicationClientId(), "client_id$development$integration_client_id");
        assertEquals(transaction.getFacilitatorDetails().getOauthApplicationName(), "PseudoShop");
    }


    @Test
    public void sharedPaymentMethodToken() {
        BraintreeGateway sharerGateway = new BraintreeGateway(Environment.DEVELOPMENT, "integration_merchant_public_id", "oauth_app_partner_user_public_key", "oauth_app_partner_user_private_key");
        Customer customer = sharerGateway.customer().create(new CustomerRequest().
                creditCard().
                number("5105105105105100").
                expirationDate("05/19").
                billingAddress().
                    postalCode("94107").
                    done().
                done()
        ).getTarget();
        CreditCard card = customer.getCreditCards().get(0);
        Address billingAddress = card.getBillingAddress();
        Address shippingAddress = sharerGateway.address().create(customer.getId(),
                new AddressRequest().postalCode("94107")).getTarget();

        BraintreeGateway oauthGateway = new BraintreeGateway("client_id$development$integration_client_id", "client_secret$development$integration_client_secret");
        String code = TestHelper.createOAuthGrant(oauthGateway, "integration_merchant_id", "shared_vault_transactions");

        OAuthCredentialsRequest oauthRequest = new OAuthCredentialsRequest().
             code(code).
             scope("shared_vault_transactions");

        Result<OAuthCredentials> accessTokenResult = oauthGateway.oauth().createTokenFromCode(oauthRequest);

        BraintreeGateway gateway = new BraintreeGateway(accessTokenResult.getTarget().getAccessToken());

        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            sharedPaymentMethodToken(card.getToken()).
            sharedCustomerId(customer.getId()).
            sharedShippingAddressId(shippingAddress.getId()).
            sharedBillingAddressId(billingAddress.getId());

        Result<Transaction> result = gateway.transaction().sale(request);
        assertTrue(result.isSuccess());
    }

    @Test
    public void paymentMethodGrantIncludeBillingPostalCode() {
        BraintreeGateway partnerGateway = new BraintreeGateway(
                Environment.DEVELOPMENT,
                "integration_merchant_public_id",
                "oauth_app_partner_user_public_key",
                "oauth_app_partner_user_private_key"
        );
        Customer customer = partnerGateway.customer().create(new CustomerRequest().
                creditCard().
                number("5105105105105100").
                expirationDate("05/19").
                billingAddress().
                    postalCode("94107").
                    done().
                done()
        ).getTarget();
        CreditCard creditCard = customer.getCreditCards().get(0);

        BraintreeGateway oauthGateway = new BraintreeGateway(
                "client_id$development$integration_client_id",
                "client_secret$development$integration_client_secret"
        );
        String code = TestHelper.createOAuthGrant(oauthGateway, "integration_merchant_id", "grant_payment_method");

        OAuthCredentialsRequest oauthRequest = new OAuthCredentialsRequest().
             code(code).
             scope("grant_payment_method");

        Result<OAuthCredentials> accessTokenResult = oauthGateway.oauth().createTokenFromCode(oauthRequest);
        BraintreeGateway grantGateway = new BraintreeGateway(accessTokenResult.getTarget().getAccessToken());
        PaymentMethodGrantRequest grantRequest = new PaymentMethodGrantRequest().allowVaulting(false).includeBillingPostalCode(true);
        Result<PaymentMethodNonce> grantResult = grantGateway.paymentMethod().grant(creditCard.getToken(), grantRequest);

        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            paymentMethodNonce(grantResult.getTarget().getNonce());

        Result<Transaction> transactionResult = gateway.transaction().sale(request);
        assertTrue(transactionResult.isSuccess());
        assertEquals(transactionResult.getTarget().getBillingAddress().getPostalCode(), "94107");
    }

    @Test
    public void paymentMethodGrantIncludesFacilitatedInformation() {
        BraintreeGateway partnerGateway = new BraintreeGateway(
                Environment.DEVELOPMENT,
                "integration_merchant_public_id",
                "oauth_app_partner_user_public_key",
                "oauth_app_partner_user_private_key"
        );
        Customer customer = partnerGateway.customer().create(new CustomerRequest().
                creditCard().
                number("5105105105105100").
                expirationDate("04/22").
                done()
        ).getTarget();
        CreditCard creditCard = customer.getCreditCards().get(0);

        BraintreeGateway oauthGateway = new BraintreeGateway(
                "client_id$development$integration_client_id",
                "client_secret$development$integration_client_secret"
        );
        String code = TestHelper.createOAuthGrant(oauthGateway, "integration_merchant_id", "grant_payment_method");

        OAuthCredentialsRequest oauthRequest = new OAuthCredentialsRequest().
             code(code).
             scope("grant_payment_method");

        Result<OAuthCredentials> accessTokenResult = oauthGateway.oauth().createTokenFromCode(oauthRequest);
        BraintreeGateway grantGateway = new BraintreeGateway(accessTokenResult.getTarget().getAccessToken());
        PaymentMethodGrantRequest grantRequest = new PaymentMethodGrantRequest().allowVaulting(false);
        Result<PaymentMethodNonce> grantResult = grantGateway.paymentMethod().grant(creditCard.getToken(), grantRequest);

        TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
            paymentMethodNonce(grantResult.getTarget().getNonce());

        Result<Transaction> transactionResult = gateway.transaction().sale(request);
        assertTrue(transactionResult.isSuccess());

        Transaction transaction = transactionResult.getTarget();

        assertEquals(transaction.getFacilitatedDetails().getMerchantId(), "integration_merchant_id");
        assertEquals(transaction.getFacilitatedDetails().getMerchantName(), "14ladders");
        assertEquals(transaction.getFacilitatedDetails().getPaymentMethodNonce(), grantResult.getTarget().getNonce());
        assertEquals(transaction.getFacilitatorDetails().getOauthApplicationClientId(), "client_id$development$integration_client_id");
        assertEquals(transaction.getFacilitatorDetails().getOauthApplicationName(), "PseudoShop");
        assertEquals(transaction.getFacilitatorDetails().getSourcePaymentMethodToken(), creditCard.getToken());
    }
}
