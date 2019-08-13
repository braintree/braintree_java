package com.braintreegateway.integrationtest;
import com.braintreegateway.Customer;
import com.braintreegateway.Transaction;
import com.braintreegateway.TransactionSearchRequest;
import com.braintreegateway.ResourceCollection;
import com.braintreegateway.CustomActionsPaymentMethod;
import com.braintreegateway.CustomActionsPaymentMethodDetails;

import java.util.List;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CustomActionsPaymentMethodIT extends IntegrationTest {

  @Test
  public void findWithId() {
    Customer customer = gateway.customer().find("CUSTOM_ACTION_CUSTOMER");

    List<CustomActionsPaymentMethod> customActionsPaymentMethods = customer.getCustomActionsPaymentMethods();
    assertTrue(customActionsPaymentMethods.size() > 0);

    CustomActionsPaymentMethod customActionsPaymentMethod = customActionsPaymentMethods.get(0);

    assertEquals(customActionsPaymentMethod.getActionName(), "bank_account");
    assertTrue(customActionsPaymentMethod.getFields().size() > 0);
    assertNotNull(customActionsPaymentMethod.getFields().get(0).getName());
    assertNotNull(customActionsPaymentMethod.getFields().get(0).getDisplayValue());
    assertNotNull(customActionsPaymentMethod.getCreatedAt());
    assertNotNull(customActionsPaymentMethod.getCustomerId());
    assertNotNull(customActionsPaymentMethod.isDefault());
    assertNotNull(customActionsPaymentMethod.getImageUrl());
    assertNotNull(customActionsPaymentMethod.getSubscriptions());
    assertNotNull(customActionsPaymentMethod.getToken());
    assertNotNull(customActionsPaymentMethod.getGlobalId());
    assertNotNull(customActionsPaymentMethod.getUniqueNumberIdentifier());
    assertNotNull(customActionsPaymentMethod.getUpdatedAt());
  }

  @Test
  public void paymentMethodFindWithId() {
    Customer customer = gateway.customer().find("CUSTOM_ACTION_CUSTOMER");

    List<CustomActionsPaymentMethod> customActionsPaymentMethods = customer.getCustomActionsPaymentMethods();
    assertTrue(customActionsPaymentMethods.size() > 0);

    CustomActionsPaymentMethod customActionsPaymentMethod = (CustomActionsPaymentMethod) gateway.paymentMethod().find(customActionsPaymentMethods.get(0).getToken());
    assertEquals(customActionsPaymentMethod.getActionName(), "bank_account");
    assertTrue(customActionsPaymentMethod.getFields().size() > 0);
    assertNotNull(customActionsPaymentMethod.getFields().get(0).getName());
    assertNotNull(customActionsPaymentMethod.getFields().get(0).getDisplayValue());
    assertNotNull(customActionsPaymentMethod.getCreatedAt());
    assertNotNull(customActionsPaymentMethod.getCustomerId());
    assertNotNull(customActionsPaymentMethod.isDefault());
    assertNotNull(customActionsPaymentMethod.getImageUrl());
    assertNotNull(customActionsPaymentMethod.getSubscriptions());
    assertNotNull(customActionsPaymentMethod.getToken());
    assertNotNull(customActionsPaymentMethod.getGlobalId());
    assertNotNull(customActionsPaymentMethod.getUniqueNumberIdentifier());
    assertNotNull(customActionsPaymentMethod.getUpdatedAt());
  }

  @Test
  public void transactionFindWithId() {
    TransactionSearchRequest searchRequest = new TransactionSearchRequest().
        orderId().is("CUSTOM_ACTION_TRANSACTION");

    ResourceCollection<Transaction> collection = gateway.transaction().search(searchRequest);

    assertEquals(1, collection.getMaximumSize());

    CustomActionsPaymentMethodDetails customActionsPaymentMethodDetails = collection.
        getFirst().
        getCustomActionsPaymentMethodDetails();

    assertNotNull(customActionsPaymentMethodDetails.getActionName());
    assertEquals(customActionsPaymentMethodDetails.getFields().size(), 1);
    assertNotNull(customActionsPaymentMethodDetails.getFields().get(0).getName());
    assertNotNull(customActionsPaymentMethodDetails.getFields().get(0).getDisplayValue());
    assertNull(customActionsPaymentMethodDetails.getUniqueNumberIdentifier());
    assertNull(customActionsPaymentMethodDetails.getToken());
    assertNull(customActionsPaymentMethodDetails.getGlobalId());
  }

  @Test
  public void vaultedTransactionFindWithId_hasNonNullUniqueNumberIdentifier() {
    TransactionSearchRequest searchRequest = new TransactionSearchRequest().
        orderId().is("VAULTED_CUSTOM_ACTION_TRANSACTION");

    ResourceCollection<Transaction> collection = gateway.transaction().search(searchRequest);

    assertEquals(1, collection.getMaximumSize());

    CustomActionsPaymentMethodDetails customActionsPaymentMethodDetails = collection.
        getFirst().
        getCustomActionsPaymentMethodDetails();

    assertNotNull(customActionsPaymentMethodDetails.getUniqueNumberIdentifier());
  }
}
