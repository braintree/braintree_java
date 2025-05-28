package com.braintreegateway;

import com.braintreegateway.enums.Business;
import com.braintreegateway.enums.Consumer;
import com.braintreegateway.enums.Corporate;
import com.braintreegateway.enums.PrepaidReloadable;
import com.braintreegateway.enums.Purchase;
import com.braintreegateway.util.NodeWrapper;
import static com.braintreegateway.util.EnumUtils.findByToString;

public class MetaCheckoutCardDetails {
  private String bin;
  private String business;
  private String cardType;
  private String cardholderName;
  private String commercial;
  private String consumer;
  private String containerId;
  private String corporate;
  private String countryOfIssuance;
  private String debit;
  private String durbinRegulated;
  private String expirationMonth;
  private String expirationYear;
  private String healthcare;
  private String imageUrl;
  private String issuingBank;
  private String last4;
  private String payroll;
  private String prepaid;
  private String prepaidReloadable;
  private String productId;
  private String purchase;
  private String token;

  public MetaCheckoutCardDetails(NodeWrapper node) {
    bin = node.findString("bin");
    business = node.findString("business");
    cardType = node.findString("card-type");
    cardholderName = node.findString("cardholder-name");
    commercial = node.findString("commercial");
    consumer = node.findString("consumer");
    containerId = node.findString("container-id");
    corporate = node.findString("corporate");
    countryOfIssuance = node.findString("country-of-issuance");
    debit = node.findString("debit");
    durbinRegulated = node.findString("durbin-regulated");
    expirationMonth = node.findString("expiration-month");
    expirationYear = node.findString("expiration-year");
    healthcare = node.findString("healthcare");
    imageUrl = node.findString("image-url");
    issuingBank = node.findString("issuing-bank");
    last4 = node.findString("last-4");
    payroll = node.findString("payroll");
    prepaid = node.findString("prepaid");
    prepaidReloadable = node.findString("prepaid-reloadable");
    productId = node.findString("product-id");
    purchase = node.findString("purchase");
    token = node.findString("token");
  }

  public String getBin() {
    return bin;
  }

  public String getContainerId() {
    return containerId;
  }

  public String getCardType() {
    return cardType;
  }

  public String getCardholderName() {
    return cardholderName;
  }

  public String getExpirationDate() {
    if (expirationMonth == null || expirationYear == null) {
      return "0/0";
    }
    return getExpirationMonth() + "/" + getExpirationYear();
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

  public String getToken() {
    return token;
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

  public Business getBusiness() {
    return findByToString(Business.values(), business, Business.UNKNOWN);
  }

  public Consumer getConsumer() {
    return findByToString(Consumer.values(), consumer, Consumer.UNKNOWN);
  }

  public Corporate getCorporate() {
    return findByToString(Corporate.values(), corporate, Corporate.UNKNOWN);
  }

  public Purchase getPurchase() {
    return findByToString(Purchase.values(), purchase, Purchase.UNKNOWN);
  }
}
