package com.braintreegateway;

import com.braintreegateway.org.apache.commons.codec.binary.Base64;
import com.braintreegateway.util.Http;
import com.braintreegateway.util.TrUtil;

/**
 * This is the primary interface to the Braintree Gateway.  
 * It is used to interact with:
 * <ul>
 * <li> {@link AddressGateway Addresses}
 * <li> {@link CreditCardGateway CreditCards}
 * <li> {@link CustomerGateway Customers}
 * <li> {@link SubscriptionGateway Subscriptions}
 * <li> {@link TransactionGateway Transactions} 
 * </ul>  
 * 
 * Quick Start Example:
 * <pre>
 * import java.math.BigDecimal;
 * import com.braintreegateway.*;
 * 
 * public class BraintreeExample {
 * 
 *    public static void main(String[] args) {
 *        BraintreeGateway gateway = new BraintreeGateway(
 *            Environment.SANDBOX, 
 *            "the_merchant_id", 
 *            "the_public_key", 
 *            "the_private_key"
 *        );
 *    
 *        TransactionRequest request = new TransactionRequest().
 *            amount(new BigDecimal("100.00")).
 *            creditCard().
 *                number("4111111111111111").
 *                expirationDate("05/2012").
 *                done();
 *    
 *        Transaction transaction = gateway.transaction().sale(request).getTarget();
 *        System.out.println("Transaction ID: " + transaction.getId());
 *        System.out.println("Status: " + transaction.getStatus());
 *    }
 * }
 * </pre>
 */
public class BraintreeGateway {

    public static final String VERSION = "2.6.0";

    private Configuration configuration;
    private Environment environment;
    private Http http;
    private String merchantId;
    private String privateKey;
    private String publicKey;

    /**
     * Instantiates a BraintreeGateway.  Use the values provided by Braintree.
     * @param environment Either {@link Environment#SANDBOX} or {@link Environment#PRODUCTION}.
     * @param merchantId the merchant id provided by Braintree.
     * @param publicKey the public key provided by Braintree.
     * @param privateKey the private key provided by Braintree.
     */
    public BraintreeGateway(Environment environment, String merchantId, String publicKey, String privateKey) {
        this.environment = environment;
        this.merchantId = merchantId;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.configuration = new Configuration(baseMerchantURL(), publicKey, privateKey);
        this.http = new Http(getAuthorizationHeader(), baseMerchantURL(), BraintreeGateway.VERSION);
    }
    
    /**
     * Returns an {@link AddressGateway} for interacting with {@link Address} objects.
     * @return an {@link AddressGateway}.
     */
    public AddressGateway address() {
        return new AddressGateway(http);
    }

    public String baseMerchantURL() {
        return environment.baseURL + "/merchants/" + merchantId;
    }
    
    public Configuration getConfiguration() {
        return configuration;
    }

    /**
     * Returns an {@link CreditCardGateway} for interacting with {@link CreditCard} objects.
     * @return an {@link CreditCardGateway}.
     */
    public CreditCardGateway creditCard() {
        return new CreditCardGateway(http, configuration);
    }

    /**
     * Returns an {@link CustomerGateway} for interacting with {@link Customer} objects.
     * @return an {@link CustomerGateway}.
     */
    public CustomerGateway customer() {
        return new CustomerGateway(http, configuration);
    }

    public String getAuthorizationHeader() {
        return "Basic " + Base64.encodeBase64String((publicKey + ":" + privateKey).getBytes()).trim();
    }

    /**
     * Returns an {@link SubscriptionGateway} for interacting with {@link Subscription} objects.
     * @return an {@link SubscriptionGateway}.
     */
    public SubscriptionGateway subscription() {
        return new SubscriptionGateway(http);
    }

    /**
     * Returns an {@link TransactionGateway} for interacting with {@link Transaction} objects.
     * @return an {@link TransactionGateway}.
     */
    public TransactionGateway transaction() {
        return new TransactionGateway(http, configuration);
    }

    public TransparentRedirectGateway transparentRedirect() {
        return new TransparentRedirectGateway(http, configuration);
    }
    
    /**
     * Returns encoded transparent redirect data for the given {@link Request} and redirect URL
     * @param trData the transparent redirect data as a {@link Request} object.
     * @param redirectURL the redirect URL for the user after the transparent redirect POST.
     * @return a String of encoded transparent redirect data.
     */
    public String trData(Request trData, String redirectURL) {
        return new TrUtil(configuration).buildTrData(trData, redirectURL);
    }
}
