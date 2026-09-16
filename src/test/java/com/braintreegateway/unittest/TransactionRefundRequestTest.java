package com.braintreegateway.unittest;

import com.braintreegateway.TransactionRefundRequest;
import com.braintreegateway.testhelpers.TestHelper;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TransactionRefundRequestTest {
    @Test
    public void toXmlIncludesAmount() {
        TransactionRefundRequest request = new TransactionRefundRequest().amount(new BigDecimal("10.00"));
        TestHelper.assertIncludes("<amount>10.00</amount>", request.toXML());
    }

    @Test
    public void toXmlIncludesOrderId() {
        TransactionRefundRequest request = new TransactionRefundRequest().orderId("order-123");
        TestHelper.assertIncludes("<orderId>order-123</orderId>", request.toXML());
    }

    @Test
    public void toXmlIncludesMerchantAccountId() {
        TransactionRefundRequest request = new TransactionRefundRequest().merchantAccountId("merchant-456");
        TestHelper.assertIncludes("<merchantAccountId>merchant-456</merchantAccountId>", request.toXML());
    }

    @Test
    public void toXmlIncludesApiRequestKey() {
        TransactionRefundRequest request = new TransactionRefundRequest().apiRequestKey("test-api-key-123");
        TestHelper.assertIncludes("<api-request-key>test-api-key-123</api-request-key>", request.toXML());
    }

    @Test
    public void toXmlExcludesApiRequestKeyWhenNull() {
        TransactionRefundRequest request = new TransactionRefundRequest().apiRequestKey(null);
        assertFalse(request.toXML().contains("api-request-key"));
    }

    @Test
    public void toXmlIncludesSurchargeAmount() {
        TransactionRefundRequest request = new TransactionRefundRequest().surchargeAmount(new BigDecimal("1.00"));
        TestHelper.assertIncludes("<surchargeAmount>1.00</surchargeAmount>", request.toXML());
    }

    @Test
    public void toXmlIncludesAllFields() {
        TransactionRefundRequest request = new TransactionRefundRequest()
            .amount(new BigDecimal("25.50"))
            .apiRequestKey("refund-key-789")
            .orderId("order-abc")
            .merchantAccountId("merchant-xyz")
            .surchargeAmount(new BigDecimal("1.00"));

        String xml = request.toXML();
        TestHelper.assertIncludes("<amount>25.50</amount>", xml);
        TestHelper.assertIncludes("<api-request-key>refund-key-789</api-request-key>", xml);
        TestHelper.assertIncludes("<orderId>order-abc</orderId>", xml);
        TestHelper.assertIncludes("<merchantAccountId>merchant-xyz</merchantAccountId>", xml);
        TestHelper.assertIncludes("<surchargeAmount>1.00</surchargeAmount>", xml);
    }
}
