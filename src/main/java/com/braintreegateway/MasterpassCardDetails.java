package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

@Deprecated
public class MasterpassCardDetails {
    private String bin;
    private String cardType;
    private String cardholderName;
    private String expirationMonth;
    private String expirationYear;
    private String imageUrl;
    private String issuerLocation;
    private String last4;
    private String paymentInstrumentName;
    private String token;
    private String commercial;
    private String debit;
    private String durbinRegulated;
    private String healthcare;
    private String payroll;
    private String prepaid;
    private String productId;
    private String countryOfIssuance;
    private String issuingBank;

    public MasterpassCardDetails(NodeWrapper node) {
        bin = node.findString("bin");
        cardType = node.findString("card-type");
        cardholderName = node.findString("cardholder-name");
        expirationMonth = node.findString("expiration-month");
        expirationYear = node.findString("expiration-year");
        imageUrl = node.findString("image-url");
        issuerLocation = node.findString("issuer-location");
        last4 = node.findString("last-4");
        token = node.findString("token");
        commercial = node.findString("commercial");
        debit = node.findString("debit");
        durbinRegulated = node.findString("durbin-regulated");
        healthcare = node.findString("healthcare");
        payroll = node.findString("payroll");
        prepaid = node.findString("prepaid");
        productId = node.findString("product-id");
        countryOfIssuance = node.findString("country-of-issuance");
        issuingBank = node.findString("issuing-bank");
    }

    public String getBin() {
        return bin;
    }

    public String getCardType() {
        return cardType;
    }

    public String getCardholderName() {
        return cardholderName;
    }

    public String getExpirationDate() {
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
      if(commercial.equals(CreditCard.Commercial.YES.toString())) {
        return CreditCard.Commercial.YES;
      } else if (commercial.equals(CreditCard.Commercial.NO.toString())) {
        return CreditCard.Commercial.NO;
      } else {
        return CreditCard.Commercial.UNKNOWN;
      }
    }

    public CreditCard.Debit getDebit() {
      if(debit.equals(CreditCard.Debit.YES.toString())) {
        return CreditCard.Debit.YES;
      } else if (debit.equals(CreditCard.Debit.NO.toString())) {
        return CreditCard.Debit.NO;
      } else {
        return CreditCard.Debit.UNKNOWN;
      }
    }

    public CreditCard.DurbinRegulated getDurbinRegulated() {
      if(durbinRegulated.equals(CreditCard.DurbinRegulated.YES.toString())) {
        return CreditCard.DurbinRegulated.YES;
      } else if (durbinRegulated.equals(CreditCard.DurbinRegulated.NO.toString())) {
        return CreditCard.DurbinRegulated.NO;
      } else {
        return CreditCard.DurbinRegulated.UNKNOWN;
      }
    }

    public CreditCard.Healthcare getHealthcare() {
      if(healthcare.equals(CreditCard.Healthcare.YES.toString())) {
        return CreditCard.Healthcare.YES;
      } else if (healthcare.equals(CreditCard.Healthcare.NO.toString())) {
        return CreditCard.Healthcare.NO;
      } else {
        return CreditCard.Healthcare.UNKNOWN;
      }
    }

    public CreditCard.Payroll getPayroll() {
      if(payroll.equals(CreditCard.Payroll.YES.toString())) {
        return CreditCard.Payroll.YES;
      } else if (payroll.equals(CreditCard.Payroll.NO.toString())) {
        return CreditCard.Payroll.NO;
      } else {
        return CreditCard.Payroll.UNKNOWN;
      }
    }

    public CreditCard.Prepaid getPrepaid() {
      if(prepaid.equals(CreditCard.Prepaid.YES.toString())) {
        return CreditCard.Prepaid.YES;
      } else if (prepaid.equals(CreditCard.Prepaid.NO.toString())) {
        return CreditCard.Prepaid.NO;
      } else {
        return CreditCard.Prepaid.UNKNOWN;
      }
    }

    public String getProductId() {
        if(productId.equals("")) {
            return "Unknown";
        } else {
            return productId;
        }
    }

    public String getCountryOfIssuance() {
        if(countryOfIssuance.equals("")) {
            return "Unknown";
        } else {
            return countryOfIssuance;
        }
    }

    public String getIssuingBank() {
        if(issuingBank.equals("")) {
            return "Unknown";
        } else {
            return issuingBank;
        }
    }
}
