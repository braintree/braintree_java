package com.braintreegateway.graphql.inputs;

import java.util.HashMap;
import java.util.Map;
import com.braintreegateway.util.Experimental;

@Experimental("This class is experimental and may change in future releases.")
public class PayerInfoInput {

    private String givenName;
    private String surname;
    private String email;
    private String phoneCountryCode;
    private String phoneNumber;
    private ShippingAddressInput shippingAddress;
    private BillingAddressInput billingAddress;

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneCountryCode() {
        return phoneCountryCode;
    }

    public void setPhoneCountryCode(String phoneCountryCode) {
        this.phoneCountryCode = phoneCountryCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public ShippingAddressInput getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(ShippingAddressInput shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public BillingAddressInput getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(BillingAddressInput billingAddress) {
        this.billingAddress = billingAddress;
    }

    public Map<String, Object> toGraphQLVariables() {
        Map<String, Object> variables = new HashMap<>();
        if (givenName != null) {
            variables.put("givenName", givenName);
        }
        if (surname != null) {
            variables.put("surname", surname);
        }
        if (email != null) {
            variables.put("email", email);
        }
        if (phoneCountryCode != null) {
            variables.put("phoneCountryCode", phoneCountryCode);
        }
        if (phoneNumber != null) {
            variables.put("phoneNumber", phoneNumber);
        }
        if (shippingAddress != null) {
            variables.put("shippingAddress", shippingAddress.toGraphQLVariables());
        }
        if (billingAddress != null) {
            variables.put("billingAddress", billingAddress.toGraphQLVariables());
        }
        return variables;
    }

    private PayerInfoInput(Builder builder) {
        this.givenName = builder.givenName;
        this.surname = builder.surname;
        this.email = builder.email;
        this.phoneCountryCode = builder.phoneCountryCode;
        this.phoneNumber = builder.phoneNumber;
        this.shippingAddress = builder.shippingAddress;
        this.billingAddress = builder.billingAddress;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String givenName;
        private String surname;
        private String email;
        private String phoneCountryCode;
        private String phoneNumber;
        private ShippingAddressInput shippingAddress;
        private BillingAddressInput billingAddress;

        public Builder givenName(String givenName) {
            this.givenName = givenName;
            return this;
        }

        public Builder surname(String surname) {
            this.surname = surname;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder phoneCountryCode(String phoneCountryCode) {
            this.phoneCountryCode = phoneCountryCode;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder shippingAddress(ShippingAddressInput shippingAddress) {
            this.shippingAddress = shippingAddress;
            return this;
        }

        public Builder billingAddress(BillingAddressInput billingAddress) {
            this.billingAddress = billingAddress;
            return this;
        }

        public PayerInfoInput build() {
            return new PayerInfoInput(this);
        }
    }
}
