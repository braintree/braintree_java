package com.braintreegateway.graphql.inputs;

import java.util.HashMap;
import java.util.Map;
import com.braintreegateway.util.Experimental;

@Experimental("This class is experimental and may change in future releases.")
public class ShippingAddressInput {

    private String countryCodeAlpha2;
    private String streetAddress;
    private String extendedAddress;
    private String locality;
    private String region;
    private String postalCode;

    public String getCountryCodeAlpha2() {
        return countryCodeAlpha2;
    }

    public void setCountryCodeAlpha2(String countryCodeAlpha2) {
        this.countryCodeAlpha2 = countryCodeAlpha2;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getExtendedAddress() {
        return extendedAddress;
    }

    public void setExtendedAddress(String extendedAddress) {
        this.extendedAddress = extendedAddress;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Map<String, Object> toGraphQLVariables() {
        Map<String, Object> variables = new HashMap<>();
        if (countryCodeAlpha2 != null) {
            variables.put("countryCode", countryCodeAlpha2);
        }
        if (streetAddress != null) {
            variables.put("streetAddress", streetAddress);
        }
        if (extendedAddress != null) {
            variables.put("extendedAddress", extendedAddress);
        }
        if (locality != null) {
            variables.put("locality", locality);
        }
        if (region != null) {
            variables.put("region", region);
        }
        if (postalCode != null) {
            variables.put("postalCode", postalCode);
        }
        return variables;
    }

    private ShippingAddressInput(Builder builder) {
        this.countryCodeAlpha2 = builder.countryCodeAlpha2;
        this.streetAddress = builder.streetAddress;
        this.extendedAddress = builder.extendedAddress;
        this.locality = builder.locality;
        this.region = builder.region;
        this.postalCode = builder.postalCode;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String countryCodeAlpha2;
        private String streetAddress;
        private String extendedAddress;
        private String locality;
        private String region;
        private String postalCode;

        public Builder countryCodeAlpha2(String countryCodeAlpha2) {
            this.countryCodeAlpha2 = countryCodeAlpha2;
            return this;
        }

        public Builder streetAddress(String streetAddress) {
            this.streetAddress = streetAddress;
            return this;
        }

        public Builder extendedAddress(String extendedAddress) {
            this.extendedAddress = extendedAddress;
            return this;
        }

        public Builder locality(String locality) {
            this.locality = locality;
            return this;
        }

        public Builder region(String region) {
            this.region = region;
            return this;
        }

        public Builder postalCode(String postalCode) {
            this.postalCode = postalCode;
            return this;
        }

        public ShippingAddressInput build() {
            return new ShippingAddressInput(this);
        }
    }
}
