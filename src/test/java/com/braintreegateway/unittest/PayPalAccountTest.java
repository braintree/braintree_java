package com.braintreegateway.unittest;

import java.util.Calendar;

import com.braintreegateway.PayPalAccount;
import com.braintreegateway.util.SimpleNodeWrapper;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PayPalAccountTest {
  @Test
  public void includesFields() {
    String xml = "<paypal-account>" +
                 "<billing-agreement-id>billingagreementid</billing-agreement-id>" +
                 "<created-at type=\"datetime\">2018-04-12T19:54:16Z</created-at>" +
                 "<customer-id>1396526238</customer-id>" +
                 "<edit-paypal-vault-id>ENCR-BA-ID1</edit-paypal-vault-id>" +
                 "<email>some-email</email>" +
                 "<funding-source-description>VISA 1234</funding-source-description>" +
                 "<image-url>https://google.com/image.png</image-url>" +
                 "<default>true</default>" +
                 "<payer-id>1357</payer-id>" +
                 "<revoked-at type=\"datetime\">2019-05-13T20:55:17Z</revoked-at>" +
                 "<subscriptions type=\"array\">" +
                 "</subscriptions>" +
                 "<token>token</token>" +
                 "<updated-at type=\"datetime\">2018-04-12T19:54:16Z</updated-at>" +
                 "</paypal-account>";
    SimpleNodeWrapper node = SimpleNodeWrapper.parse(xml);

    PayPalAccount account = new PayPalAccount(node);

    assertEquals("billingagreementid", account.getBillingAgreementId());

    assertEquals(2018, account.getCreatedAt().get(Calendar.YEAR));
    assertEquals(4, account.getCreatedAt().get(Calendar.MONTH)+1);
    assertEquals(12, account.getCreatedAt().get(Calendar.DAY_OF_MONTH));
    assertEquals(7, account.getCreatedAt().get(Calendar.HOUR));
    assertEquals(54, account.getCreatedAt().get(Calendar.MINUTE));
    assertEquals(16, account.getCreatedAt().get(Calendar.SECOND));

    assertEquals("1396526238", account.getCustomerId());
    assertEquals("ENCR-BA-ID1", account.getEditPaypalVaultId());
    assertEquals("some-email", account.getEmail());
    assertEquals("VISA 1234", account.getFundingSourceDescription());
    assertEquals("https://google.com/image.png", account.getImageUrl());
    assertEquals("1357", account.getPayerId());

    assertEquals(2019, account.getRevokedAt().get(Calendar.YEAR));
    assertEquals(5, account.getRevokedAt().get(Calendar.MONTH)+1);
    assertEquals(13, account.getRevokedAt().get(Calendar.DAY_OF_MONTH));
    assertEquals(8, account.getRevokedAt().get(Calendar.HOUR));
    assertEquals(55, account.getRevokedAt().get(Calendar.MINUTE));
    assertEquals(17, account.getRevokedAt().get(Calendar.SECOND));

    assertEquals(0, account.getSubscriptions().size());
    assertEquals("token", account.getToken());

    assertEquals(2018, account.getUpdatedAt().get(Calendar.YEAR));
    assertEquals(4, account.getUpdatedAt().get(Calendar.MONTH)+1);
    assertEquals(12, account.getUpdatedAt().get(Calendar.DAY_OF_MONTH));
    assertEquals(7, account.getUpdatedAt().get(Calendar.HOUR));
    assertEquals(54, account.getUpdatedAt().get(Calendar.MINUTE));
    assertEquals(16, account.getUpdatedAt().get(Calendar.SECOND));

    assertTrue(account.isDefault());
  }
}
