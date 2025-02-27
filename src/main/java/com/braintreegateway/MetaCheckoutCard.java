package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;
import com.braintreegateway.util.StringUtils;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.braintreegateway.enums.PrepaidReloadable;
import static com.braintreegateway.util.EnumUtils.findByToString;

public class MetaCheckoutCard implements PaymentMethod {

  private String bin;
  private String cardholderName;
  private String cardType;
  private String commercial;
  private String containerId;
  private String countryOfIssuance;
  private Calendar createdAt;
  private String customerLocation;
  private String debit;
  private String durbinRegulated;
  private String expirationMonth;
  private String expirationYear;
  private String healthcare;
  private String imageUrl;
  private boolean isExpired;
  private String issuingBank;
  private String last4;
  private String payroll;
  private String prepaid;
  private String prepaidReloadable;
  private String productId;
  private String token;
  private String uniqueNumberIdentifier;
  private Calendar updatedAt;
  private CreditCardVerification verification;

  public MetaCheckoutCard(NodeWrapper node) {

    bin = node.findString("bin");
    cardType = node.findString("card-type");
    cardholderName = node.findString("cardholder-name");
    commercial = node.findString("commercial");
    containerId = node.findString("container-id");
    countryOfIssuance = node.findString("country-of-issuance");
    createdAt = node.findDateTime("created-at");
    customerLocation = node.findString("customer-location");
    debit = node.findString("debit");
    durbinRegulated = node.findString("durbin-regulated");
    expirationMonth = node.findString("expiration-month");
    expirationYear = node.findString("expiration-year");
    healthcare = node.findString("healthcare");
    imageUrl = node.findString("image-url");
    isExpired = node.findBoolean("expired");
    issuingBank = node.findString("issuing-bank");
    last4 = node.findString("last-4");
    payroll = node.findString("payroll");
    prepaid = node.findString("prepaid");
    prepaidReloadable = node.findString("prepaid-reloadable");
    productId = node.findString("product-id");
    token = node.findString("token");
    updatedAt = node.findDateTime("updated-at");
    uniqueNumberIdentifier = node.findString("unique-number-identifier");

    final List<NodeWrapper> verificationNodes = node.findAll("verifications/verification");
    verification = findNewestVerification(verificationNodes);
  }

  private CreditCardVerification findNewestVerification(List<NodeWrapper> verificationNodes) {
    if (verificationNodes.size() > 0) {
      Collections.sort(verificationNodes, new Comparator<NodeWrapper>() {
        public int compare(NodeWrapper node1, NodeWrapper node2) {
          Calendar createdAt1 = node1.findDateTime("created-at");
          Calendar createdAt2 = node2.findDateTime("created-at");

          return createdAt2.compareTo(createdAt1);
        }
      });

      return new CreditCardVerification(verificationNodes.get(0));
    }

    return null;
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

  public String getContainerId() {
    return containerId;
  }

  public String getCustomerLocation() {
    return customerLocation;
  }

  public String getExpirationDate() {
    if (expirationMonth == null || expirationYear == null) {
      return "0/0";
    }
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

  public PrepaidReloadable getPrepaidReloadable() {
    return findByToString(PrepaidReloadable.values(), prepaidReloadable, PrepaidReloadable.UNKNOWN);
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

  public String getToken() {
    return token;
  }

  public Calendar getUpdatedAt() {
    return updatedAt;
  }

  public boolean isExpired() {
    return isExpired;
  }

  public CreditCardVerification getVerification() {
    return verification;
  }

  @Override
  public String getCustomerId() {
    throw new UnsupportedOperationException("Unimplemented method 'getCustomerId'");
  }

  @Override
  public List<Subscription> getSubscriptions() {
    throw new UnsupportedOperationException("Unimplemented method 'getSubscriptions'");
  }

  @Override
  public boolean isDefault() {
    throw new UnsupportedOperationException("Unimplemented method 'isDefault'");
  }
}
