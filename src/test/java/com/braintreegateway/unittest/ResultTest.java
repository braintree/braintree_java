package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.braintreegateway.Plan;
import com.braintreegateway.Result;
import com.braintreegateway.Subscription;
import com.braintreegateway.Transaction;
import com.braintreegateway.ValidationErrors;
import com.braintreegateway.util.SimpleNodeWrapper;

public class ResultTest {

    private static final String PARAMS_XML =
            "<params><controller>plans</controller></params>";
    private static final String ERROR_XML =
            "<errors><errors type=\"array\"/></errors>";

    @Test
    public void successResultHasTargetAndIsSuccess() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse("<plan><id>plan-id</id></plan>");
        Result<Plan> result = new Result<>(node, Plan.class);

        assertAll("success result",
                () -> assertTrue(result.isSuccess()),
                () -> assertEquals("plan-id", result.getTarget().getId()),
                () -> assertNull(result.getErrors()));
    }

    @Test
    public void errorResultWithPlanPopulatesPlanAndErrors() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<api-error-response>" +
                ERROR_XML +
                PARAMS_XML +
                "<message>Plan is invalid.</message>" +
                "<plan><id>a-plan-id</id></plan>" +
                "</api-error-response>");

        Result<Plan> result = new Result<>(node, Plan.class);

        assertAll("error result with plan",
                () -> assertFalse(result.isSuccess()),
                () -> assertNotNull(result.getPlan()),
                () -> assertEquals("a-plan-id", result.getPlan().getId()),
                () -> assertEquals("Plan is invalid.", result.getMessage()),
                () -> assertNotNull(result.getParameters()));
    }

    @Test
    public void errorResultWithSubscriptionPopulatesSubscription() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<api-error-response>" +
                ERROR_XML +
                PARAMS_XML +
                "<subscription><id>sub-id</id><status>Active</status>" +
                "<transactions type=\"array\"/><add-ons type=\"array\"/>" +
                "<discounts type=\"array\"/><status-history type=\"array\"/>" +
                "</subscription>" +
                "</api-error-response>");

        Result<Subscription> result = new Result<>(node, Subscription.class);

        assertAll("error result with subscription",
                () -> assertFalse(result.isSuccess()),
                () -> assertNotNull(result.getSubscription()),
                () -> assertEquals("sub-id", result.getSubscription().getId()));
    }

    @Test
    public void errorResultWithTransactionPopulatesTransaction() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<api-error-response>" +
                ERROR_XML +
                PARAMS_XML +
                "<transaction><id>txn-id</id><type>sale</type><amount>10.00</amount></transaction>" +
                "</api-error-response>");

        Result<Transaction> result = new Result<>(node, Transaction.class);

        assertAll("error result with transaction",
                () -> assertFalse(result.isSuccess()),
                () -> assertNotNull(result.getTransaction()),
                () -> assertEquals("txn-id", result.getTransaction().getId()));
    }

    @Test
    public void resultConstructorFromValidationErrors() {
        ValidationErrors errors = new ValidationErrors();
        Result<Plan> result = new Result<>(errors);

        assertAll("result from validation errors",
                () -> assertFalse(result.isSuccess()),
                () -> assertNotNull(result.getErrors()));
    }

    @Test
    public void noArgConstructorIsSuccess() {
        Result<Plan> result = new Result<>();

        assertTrue(result.isSuccess());
    }

    @Test
    public void targetConstructorSetsTarget() {
        Plan plan = new Plan(SimpleNodeWrapper.parse("<plan><id>plan-id</id></plan>"));
        Result<Plan> result = new Result<>(plan);

        assertAll("result from target",
                () -> assertTrue(result.isSuccess()),
                () -> assertEquals("plan-id", result.getTarget().getId()));
    }

    @Test
    public void errorResultWithUsBankAccountVerification() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<api-error-response>" +
                ERROR_XML +
                PARAMS_XML +
                "<us-bank-account-verification>" +
                "<id>ubav-id</id><status>verified</status>" +
                "</us-bank-account-verification>" +
                "</api-error-response>");

        Result<Plan> result = new Result<>(node, Plan.class);

        assertAll("error result with us bank account verification",
                () -> assertFalse(result.isSuccess()),
                () -> assertNotNull(result.getUsBankAccountVerification()),
                () -> assertEquals("ubav-id", result.getUsBankAccountVerification().getId()));
    }

    @Test
    public void errorResultWithCreditCardVerification() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<api-error-response>" +
                ERROR_XML +
                PARAMS_XML +
                "<verification><id>ccv-id</id></verification>" +
                "</api-error-response>");

        Result<Plan> result = new Result<>(node, Plan.class);

        assertAll("error result with credit card verification",
                () -> assertFalse(result.isSuccess()),
                () -> assertNotNull(result.getCreditCardVerification()));
    }
}
