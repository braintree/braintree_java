package come.braintegreegateway.integrationtest;

import com.braintreegateway.*;
import com.braintreegateway.SandboxValues.CreditCardNumber;
import com.braintreegateway.SandboxValues.TransactionAmount;
import com.braintreegateway.exceptions.TestOperationPerformedInProductionException;
import com.braintreegateway.test.CreditCardNumbers;
import com.braintreegateway.test.VenmoSdk;
import com.braintreegateway.testhelpers.CalendarTestUtils;
import com.braintreegateway.testhelpers.MerchantAccountTestConstants;
import com.braintreegateway.testhelpers.TestHelper;
import com.braintreegateway.testhelpers.ThreeDSecureRequestForTests;
import com.braintreegateway.util.NodeWrapperFactory;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

import static org.junit.Assert.*;

public class TestingGatewayIT implements MerchantAccountTestConstants {

    @SuppressWarnings("deprecation")
    @Test(expected = TestOperationPerformedInProductionException.class)
    public void testSettleRaisesErrorInProduction() {
        BraintreeGateway gateway = new BraintreeGateway(Environment.PRODUCTION, "merchant_id", "public_key", "private_key");

        gateway.testing().settle("transaction_id");
    }

    @SuppressWarnings("deprecation")
    @Test(expected = TestOperationPerformedInProductionException.class)
    public void testSettlementConfirmRaisesErrorInProduction() {
        BraintreeGateway gateway = new BraintreeGateway(Environment.PRODUCTION, "merchant_id", "public_key", "private_key");

        gateway.testing().settlementConfirm("transaction_id");
    }

    @SuppressWarnings("deprecation")
    @Test(expected = TestOperationPerformedInProductionException.class)
    public void testSettlementDeclineRaisesErrorInProduction() {
        BraintreeGateway gateway = new BraintreeGateway(Environment.PRODUCTION, "merchant_id", "public_key", "private_key");

        gateway.testing().settlementDecline("transaction_id");
    }


}
