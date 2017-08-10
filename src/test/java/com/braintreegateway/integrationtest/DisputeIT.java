package com.braintreegateway.integrationtest;

import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.util.Http;
import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Dispute;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.TransactionRequest;
import com.braintreegateway.ValidationError;
import com.braintreegateway.ValidationErrorCode;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;

import static com.braintreegateway.SandboxValues.Dispute.CHARGEBACK;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class DisputeIT extends IntegrationTest {

    private Http http;

    @Before
    public void createHttp() {
        http = new Http(this.gateway.getConfiguration());
    }

    @Test
    public void acceptChangesDisputeStatusToAccepted() {
        Dispute dispute = createSampleDispute();

        Result result = gateway.dispute()
            .accept(dispute.getId());

        assertTrue(result.isSuccess());

        Dispute updatedDispute = gateway.dispute()
            .find(dispute.getId());

        assertEquals(Dispute.Status.ACCEPTED, updatedDispute.getStatus());
    }

    @Test
    public void acceptWhenDisputeNotOpenErrors() {
        Result<Dispute> result = gateway.dispute()
            .accept("wells_dispute");

        assertFalse(result.isSuccess());

        ValidationError error = result.getErrors()
            .forObject("dispute")
            .getAllValidationErrors()
            .get(0);

        assertEquals(ValidationErrorCode.DISPUTE_CAN_ONLY_ACCEPT_OPEN_DISPUTE, error.getCode());
        assertEquals("Disputes can only be accepted when they are in an Open state", error.getMessage());
    }

    @Test
    public void acceptThrowsNotFoundExceptionWhenDisputeIsNotFound() {
        try {
            gateway.dispute().accept("invalid-id");
            fail("DisputeGateway#accept allowed an invalid id");
        } catch (NotFoundException exception) {
            assertEquals("dispute with id \"invalid-id\" not found", exception.getMessage());
        }
    }

	@Test
    public void finalizeChangesDisputeStatusToDisputed() {
        Dispute dispute = createSampleDispute();

        Result result = gateway.dispute()
            .finalize(dispute.getId());

        assertTrue(result.isSuccess());

        Dispute updatedDispute = gateway.dispute()
            .find(dispute.getId());

        assertEquals(Dispute.Status.DISPUTED, updatedDispute.getStatus());
    }

    @Test
    public void finalizeWhenDisputeNotOpenErrors() {
        Result<Dispute> result = gateway.dispute()
            .finalize("wells_dispute");

        assertFalse(result.isSuccess());

        ValidationError error = result.getErrors()
            .forObject("dispute")
            .getAllValidationErrors()
            .get(0);

        assertEquals(ValidationErrorCode.DISPUTE_CAN_ONLY_FINALIZE_OPEN_DISPUTE, error.getCode());
        assertEquals("Disputes can only be finalized when they are in an Open state", error.getMessage());
    }

    @Test
    public void finalizeWhenDisputeNotFoundErrors() {
        try {
            gateway.dispute().finalize("invalid-id");
            fail("DisputeGateway#finalize allowed an invalid id");
        } catch (NotFoundException exception) {
            assertEquals("dispute with id \"invalid-id\" not found", exception.getMessage());
        }
    }

    @Test
    public void findReturnsDisputeWithGivenId() {
        Dispute dispute = gateway.dispute().find("open_dispute");

        assertEquals(new BigDecimal("31.00"), dispute.getDisputedAmount());
        assertEquals(new BigDecimal("0.00"), dispute.getWonAmount());
        assertEquals("open_dispute", dispute.getId());
        assertEquals(Dispute.Status.OPEN, dispute.getStatus());
        assertEquals("open_disputed_transaction", dispute.getTransaction().getId());
    }

    @Test
    public void findThrowsNotFoundExceptionWhenDisputeNotFound() {
        try {
            gateway.dispute().find("invalid-id");
            fail("DisputeGateway#find allowed an invalid id");
        } catch (NotFoundException exception) {
            assertEquals("dispute with id \"invalid-id\" not found", exception.getMessage());
        }
    }

    public Dispute createSampleDispute() {
        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("100.00")).
            creditCard().
                number(CHARGEBACK).
                expirationDate("12/2019").
                done();

        Result<Transaction> result = gateway.transaction()
            .sale(request);

        if (!result.isSuccess()) {
            String reason = result.getTransaction().getProcessorResponseText();
            throw new RuntimeException("Could not create a transaction: " + reason);
        }

        return result.getTarget()
            .getDisputes()
            .get(0);
    }
}
