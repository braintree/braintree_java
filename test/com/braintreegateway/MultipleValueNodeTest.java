package com.braintreegateway;

import junit.framework.Assert;

import org.junit.Test;

public class MultipleValueNodeTest {
    @Test
    public void raisesErrorIfInvalidValueIsGiven() {
        MultipleValueNode<TransactionSearchRequest> node = new MultipleValueNode<TransactionSearchRequest>("test", new TransactionSearchRequest(), CreditCard.CustomerLocation.values());
        try {
            node.is("badvalue");
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected to raise
        }
    }
    
    @Test
    public void doesNotRaiseErrorIfValidValueIsGiven() {
        MultipleValueNode<TransactionSearchRequest> node = new MultipleValueNode<TransactionSearchRequest>("test", new TransactionSearchRequest(), CreditCard.CustomerLocation.values());
        node.is(CreditCard.CustomerLocation.values()[0]);
    }
}
