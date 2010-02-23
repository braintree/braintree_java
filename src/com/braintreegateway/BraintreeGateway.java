package com.braintreegateway;

import com.braintreegateway.exceptions.UnexpectedException;
import com.braintreegateway.org.apache.commons.codec.binary.Base64;
import com.braintreegateway.util.Http;
import com.braintreegateway.util.StringUtils;
import com.braintreegateway.util.TrUtil;

public class BraintreeGateway {

    private Configuration configuration;
    private Environment environment;
    private Http http;
    private String merchantId;
    private String privateKey;
    private String publicKey;

    public BraintreeGateway(Environment environment, String merchantId, String publicKey, String privateKey) {
        this.environment = environment;
        this.merchantId = merchantId;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.configuration = new Configuration(baseMerchantURL(), publicKey, privateKey);
        this.http = new Http(getAuthorizationHeader(), baseMerchantURL(), getVersion());
    }

    public AddressGateway address() {
        return new AddressGateway(http);
    }

    public String baseMerchantURL() {
        return environment.baseURL + "/merchants/" + merchantId;
    }

    public CreditCardGateway creditCard() {
        return new CreditCardGateway(http, configuration);
    }

    public CustomerGateway customer() {
        return new CustomerGateway(http, configuration);
    }

    public String getAuthorizationHeader() {
        return "Basic " + Base64.encodeBase64String((publicKey + ":" + privateKey).getBytes()).trim();
    }

    public String getVersion() {
        try {
            return StringUtils.getFileContents("VERSION").trim();
        } catch (Exception e) {
            throw new UnexpectedException("Cannot read VERSION file: " + e.getMessage());
        }
    }

    public boolean isTrDataValid(String trData) {
        return new TrUtil(configuration).validateTrData(trData);
    }

    public TransactionGateway transaction() {
        return new TransactionGateway(http, configuration);
    }

    public String trData(Request trData, String redirectURL) {
        return new TrUtil(configuration).buildTrData(trData, redirectURL);
    }
}
