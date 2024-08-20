package com.braintreegateway.unittest;

import com.braintreegateway.SandboxValues.TransactionAmount;
import com.braintreegateway.TransactionRequest;
import com.braintreegateway.testhelpers.TestHelper;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TransactionRequestTest {
    @Test
    public void toQueryStringWithNestedCustomer() {
        TransactionRequest request = new TransactionRequest().
            customer().
                firstName("Drew").
                done();

        assertEquals("transaction%5Bcustomer%5D%5Bfirst_name%5D=Drew", request.toQueryString());
    }

    @Test
    public void customFieldsEscapesKeysAndValues() {
        TransactionRequest request = new TransactionRequest().customField("ke&y", "va<lue");
        TestHelper.assertIncludes("<customFields><ke&amp;y>va&lt;lue</ke&amp;y></customFields>", request.toXML());
    }

    @Test
    public void toXmlIncludesSecurityParams() {
        TransactionRequest request = new TransactionRequest().
          deviceSessionId("device_session").
          fraudMerchantId("fraud_merchant");
        TestHelper.assertIncludes("device_session", request.toXML());
        TestHelper.assertIncludes("fraud_merchant", request.toXML());
    }

    @Test
    public void toXmlIncludesBundle() {
        TransactionRequest request = new TransactionRequest().deviceData("{\"device_session_id\": \"mydsid\", \"fraud_merchant_id\": \"myfmid\"}");
        TestHelper.assertIncludes("mydsid", request.toXML());
        TestHelper.assertIncludes("myfmid", request.toXML());
    }

    @Test
    public void toXmlIncludesAdvancedFraudCheckingFlag() {
        TransactionRequest request = new TransactionRequest().
                options().
                skipAdvancedFraudChecking(false).
                done();
        TestHelper.assertIncludes("<skipAdvancedFraudChecking>false</skipAdvancedFraudChecking>", request.toXML());
    }

    @Test
    public void toXmlIncludesInstallmentParams() {
        TransactionRequest request = new TransactionRequest().installments().count(10).done();
        TestHelper.assertIncludes("installments", request.toXML());
        TestHelper.assertIncludes("count", request.toXML());
    }

    @Test
    public void toXmlIncludesExchangeRateQuoteId() {
        TransactionRequest request = new TransactionRequest()
            .amount(new BigDecimal(10))
            .exchangeRateQuoteId("dummyExchangeRateQuoteId1234");
        TestHelper.assertIncludes("amount", request.toXML());
        TestHelper.assertIncludes("exchangeRateQuoteId", request.toXML());
    }

    @Test
    public void toXmlIncludesProcessingOverrides() {
        TransactionRequest request = new TransactionRequest().
            options().
                processingOverrides().
                    customerEmail("tom@gmail.com").
                    customerFirstName("tom").
                    customerLastName("smith").
                    customerTaxIdentifier("111111111111111").
                    done().
            done();
        TestHelper.assertIncludes("<processingOverrides><customerEmail>tom@gmail.com</customerEmail><customerFirstName>tom</customerFirstName><customerLastName>smith</customerLastName><customerTaxIdentifier>111111111111111</customerTaxIdentifier></processingOverrides>", request.toXML());
    }

    @Test
		public void toXmlIncludesProcessDebitAsCredit(){
			TransactionRequest request = new TransactionRequest().
            amount(TransactionAmount.AUTHORIZE.amount).
			merchantAccountId("processdebitascredit").
            options().
                creditCard().
                    processDebitAsCredit(true).
                    done().
                done();
		    TestHelper.assertIncludes("<processDebitAsCredit>true</processDebitAsCredit>",request.toXML());
		}

    @Test
    public void toXmlIncludesForeignRetailer() {
        TransactionRequest request = new TransactionRequest().foreignRetailer(true);
        TestHelper.assertIncludes("<foreignRetailer>true</foreignRetailer>", request.toXML());
    }

    @Test
    public void toXmlIncludesInternationalPhone() {
        TransactionRequest request = new TransactionRequest().billingAddress().internationalPhone().countryCode("1").nationalNumber("3121234567").done().done();
        TestHelper.assertIncludes("<billing><internationalPhone><countryCode>1</countryCode><nationalNumber>3121234567</nationalNumber></internationalPhone></billing>", request.toXML());
    }
  
    @Test
    public void toXmlIncludeFinalCapture() {
        TransactionRequest request = new TransactionRequest().finalCapture(true);
        TestHelper.assertIncludes("<finalCapture>true</finalCapture>", request.toXML());
    }
}
