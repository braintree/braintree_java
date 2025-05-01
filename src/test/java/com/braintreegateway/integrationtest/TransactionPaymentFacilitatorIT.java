package com.braintreegateway.integrationtest;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

import com.braintreegateway.BraintreeGateway;

import com.braintreegateway.PaymentFacilitatorRequest;
import com.braintreegateway.Environment;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.TransactionRequest;
import com.braintreegateway.Transaction.ScaExemption;
import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.exceptions.UnexpectedException;
import com.braintreegateway.test.CreditCardNumbers;
import com.braintreegateway.test.Nonce;
import com.braintreegateway.testhelpers.CalendarTestUtils;
import com.braintreegateway.testhelpers.MerchantAccountTestConstants;
import com.braintreegateway.testhelpers.TestHelper;
import com.braintreegateway.testhelpers.ThreeDSecureRequestForTests;
import com.braintreegateway.util.NodeWrapperFactory;
import com.braintreegateway.ValidationError;
import com.braintreegateway.ValidationErrorCode;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
import static org.junit.jupiter.api.Assertions.*;

public class TransactionPaymentFacilitatorIT extends IntegrationTest implements MerchantAccountTestConstants {

    public static final String DISBURSEMENT_TRANSACTION_ID = "deposittransaction";
    public static final String DISPUTED_TRANSACTION_ID = "disputedtransaction";
    public static final String TWO_DISPUTE_TRANSACTION_ID = "2disputetransaction";
    public static final String AUTH_ADJUSTMENT_TRANSACTION_ID = "authadjustmenttransaction";
    public static final String AUTH_ADJUSTMENT_SOFT_DECLINED_TRANSACTION_ID = "authadjustmenttransactionsoftdeclined";
    public static final String AUTH_ADJUSTMENT_HARD_DECLINED_TRANSACTION_ID = "authadjustmenttransactionharddeclined";


    @Test
    public void testTransactionWithSubMrechantAndPymentFacilitatatorPayfacMerchant() { 
        TransactionRequest request = new TransactionRequest().
                amount(new BigDecimal("100.00")).
                merchantAccountId("card_processor_brl_payfac").
                creditCard().
                    number("4111111111111111").
                    expirationDate("06/2026").
                    cvv("123").
                    done().
                descriptor().
                    name("companynme12*product1").
                    phone("1232344444").
                    url("example.com").
                    done().
                billingAddress(). 
                    firstName("Bob James").
                    countryCodeAlpha2("CA").
                    extendedAddress("").
                    locality("Trois-Rivires").
                    region("QC").
                    postalCode("G8Y 156").
                    streetAddress("2346 Boul Lane").
                    done().
                paymentFacilitator().
                         paymentFacilitatorId("98765432109").
                         subMerchant().
                                referenceNumber("123456789012345").
                                taxId("99112233445577").
                                legalName("Fooda").
                                address().
                                        streetAddress("10880 Ibitinga").
                                        locality("Araraquara").
                                        region("SP").
                                        countryCodeAlpha2("BR").
                                        postalCode("13525000").
                                        internationalPhone().
                                                countryCode("55").
                                                nationalNumber("9876543210").
                                                done().
                                        done().
                            done().
                        done().
                options().
                    storeInVaultOnSuccess(true).
                    done();
   
        Result<Transaction> result = gateway.transaction().sale(request);

        assertTrue(result.isSuccess());
        Transaction transaction = result.getTarget();
        assertEquals(Transaction.Status.AUTHORIZED, transaction.getStatus());
    }
    
    @Test
    public void testTransactionWithSubMerchantAndPaymentFacilitatatorNonBrazilMerchant() {
        TransactionRequest request = new TransactionRequest().
                amount(new BigDecimal("100.00")).
                creditCard().
                    number("4111111111111111").
                    expirationDate("06/2026"). 
                    cvv("123").
                    done().
                descriptor().
                    name("companynme12*product1").
                    phone("1232344444").
                    url("example.com").
                    done().
                billingAddress(). 
                    firstName("Bob James").
                    countryCodeAlpha2("CA").
                    extendedAddress("").
                    locality("Trois-Rivires").
                    region("QC").
                    postalCode("G8Y 156").
                    streetAddress("2346 Boul Lane").
                    done().
                paymentFacilitator().
                         paymentFacilitatorId("98765432109").
                         subMerchant().
                                referenceNumber("123456789012345").
                                taxId("99112233445577").
                                legalName("Fooda").
                                address().
                                        streetAddress("10880 Ibitinga").
                                        locality("Araraquara").
                                        region("SP").
                                        countryCodeAlpha2("BR").
                                        postalCode("13525000").
                                        internationalPhone().
                                                countryCode("55").
                                                nationalNumber("9876543210").
                                                done().
                                       done().
                             done().
                        done().
                options().
                    storeInVaultOnSuccess(true). 
                    done();

        BraintreeGateway ezpGateway = new BraintreeGateway(
            Environment.DEVELOPMENT,
            "pp_credit_ezp_merchant",
            "pp_credit_ezp_merchant_public_key",
            "pp_credit_ezp_merchant_private_key"
        );

        Result<Transaction> result = ezpGateway.transaction().sale(request);

        assertFalse(result.isSuccess());
        assertEquals(ValidationErrorCode.TRANSACTION_PAYMENT_FACILITATOR_NOT_APPLICABLE, result.getErrors().getAllDeepValidationErrors().get(0).getCode());;

    }
}