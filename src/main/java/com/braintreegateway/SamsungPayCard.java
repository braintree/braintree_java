package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

// NEXT_MAJOR_VERSION remove this class
// SamsungPay has been deprecated
@Deprecated
public class SamsungPayCard implements PaymentMethod {

  private Address billingAddress;
  private String bin;
  private String cardholderName;
  private String cardType;
  private Calendar createdAt;
  private String customerId;
  private String customerLocation;
  private String expirationMonth;
  private String expirationYear;
  private boolean isDefault;
  private boolean isVenmoSdk;
  private boolean isExpired;
  private String imageUrl;
  private String last4;
  private String commercial;
  private String debit;
  private String durbinRegulated;
  private String healthcare;
  private String payroll;
  private String prepaid;
  private String productId;
  private String countryOfIssuance;
  private String issuingBank;
  private String uniqueNumberIdentifier;
  private List<Subscription> subscriptions;
  private String token;
  private Calendar updatedAt;

  public SamsungPayCard(NodeWrapper node) {
    token = node.findString("token");
    createdAt = node.findDateTime("created-at");
    updatedAt = node.findDateTime("updated-at");
    bin = node.findString("bin");
    cardType = node.findString("card-type");
    cardholderName = node.findString("cardholder-name");
    customerId = node.findString("customer-id");
    customerLocation = node.findString("customer-location");
    expirationMonth = node.findString("expiration-month");
    expirationYear = node.findString("expiration-year");
    imageUrl = node.findString("image-url");
    isDefault = node.findBoolean("default");
    isExpired = node.findBoolean("expired");
    last4 = node.findString("last-4");
    commercial = node.findString("commercial");
    debit = node.findString("debit");
    durbinRegulated = node.findString("durbin-regulated");
    healthcare = node.findString("healthcare");
    payroll = node.findString("payroll");
    prepaid = node.findString("prepaid");
    productId = node.findString("product-id");
    countryOfIssuance = node.findString("country-of-issuance");
    issuingBank = node.findString("issuing-bank");
    uniqueNumberIdentifier = node.findString("unique-number-identifier");
    NodeWrapper billingAddressResponse = node.findFirst("billing-address");
    if (billingAddressResponse != null) {
      billingAddress = new Address(billingAddressResponse);
    }
    subscriptions = new ArrayList<Subscription>();
    for (NodeWrapper subscriptionResponse : node.findAll("subscriptions/subscription")) {
      subscriptions.add(new Subscription(subscriptionResponse));
    }
  }

  public Address getBillingAddress() {
    return billingAddress;
  }

  public String getBin() {
    return bin;
  }

  public String getCardholderName() {
    return cardholderName;
  }

  public String getCardType() {
    return cardType;
  }

  public Calendar getCreatedAt() {
    return createdAt;
  }

  public String getCustomerId() {
    return customerId;
  }

  public String getCustomerLocation() {
    return customerLocation;
  }

  public String getExpirationDate() {
    return expirationMonth + "/" + expirationYear;
  }

  public String getExpirationMonth() {
    return expirationMonth;
  }

  public String getExpirationYear() {
    return expirationYear;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public String getLast4() {
    return last4;
  }

  public String getMaskedNumber() {
    return getBin() + "******" + getLast4();
  }

  public CreditCard.Commercial getCommercial() {
    if (commercial.equals(CreditCard.Commercial.YES.toString())) {
      return CreditCard.Commercial.YES;
    } else if (commercial.equals(CreditCard.Commercial.NO.toString())) {
      return CreditCard.Commercial.NO;
    } else {
      return CreditCard.Commercial.UNKNOWN;
    }
  }

  public CreditCard.Debit getDebit() {
    if (debit.equals(CreditCard.Debit.YES.toString())) {
      return CreditCard.Debit.YES;
    } else if (debit.equals(CreditCard.Debit.NO.toString())) {
      return CreditCard.Debit.NO;
    } else {
      return CreditCard.Debit.UNKNOWN;
    }
  }

  public CreditCard.DurbinRegulated getDurbinRegulated() {
    if (durbinRegulated.equals(CreditCard.DurbinRegulated.YES.toString())) {
      return CreditCard.DurbinRegulated.YES;
    } else if (durbinRegulated.equals(CreditCard.DurbinRegulated.NO.toString())) {
      return CreditCard.DurbinRegulated.NO;
    } else {
      return CreditCard.DurbinRegulated.UNKNOWN;
    }
  }

  public CreditCard.Healthcare getHealthcare() {
    if (healthcare.equals(CreditCard.Healthcare.YES.toString())) {
      return CreditCard.Healthcare.YES;
    } else if (healthcare.equals(CreditCard.Healthcare.NO.toString())) {
      return CreditCard.Healthcare.NO;
    } else {
      return CreditCard.Healthcare.UNKNOWN;
    }
  }

  public CreditCard.Payroll getPayroll() {
    if (payroll.equals(CreditCard.Payroll.YES.toString())) {
      return CreditCard.Payroll.YES;
    } else if (payroll.equals(CreditCard.Payroll.NO.toString())) {
      return CreditCard.Payroll.NO;
    } else {
      return CreditCard.Payroll.UNKNOWN;
    }
  }

  public CreditCard.Prepaid getPrepaid() {
    if (prepaid.equals(CreditCard.Prepaid.YES.toString())) {
      return CreditCard.Prepaid.YES;
    } else if (prepaid.equals(CreditCard.Prepaid.NO.toString())) {
      return CreditCard.Prepaid.NO;
    } else {
      return CreditCard.Prepaid.UNKNOWN;
    }
  }

  public String getProductId() {
    if (productId.equals("")) {
      return "Unknown";
    } else {
      return productId;
    }
  }

  public String getCountryOfIssuance() {
    if (countryOfIssuance.equals("")) {
      return "Unknown";
    } else {
      return countryOfIssuance;
    }
  }

  public String getIssuingBank() {
    if (issuingBank.equals("")) {
      return "Unknown";
    } else {
      return issuingBank;
    }
  }

  public String getUniqueNumberIdentifier() {
    return uniqueNumberIdentifier;
  }

  public List<Subscription> getSubscriptions() {
    return subscriptions;
  }

  public String getToken() {
    return token;
  }

  public Calendar getUpdatedAt() {
    return updatedAt;
  }

  public boolean isDefault() {
    return isDefault;
  }

  //NEXT_MAJOR_VERSION remove this method
  /**
   * @deprecated - The Venmo SDK integration is Unsupported. Please update your integration to use Pay with Venmo instead
  */
  @Deprecated
  public boolean isVenmoSdk() {
    return isVenmoSdk;
  }

  public boolean isExpired() {
    return isExpired;
  }
}
