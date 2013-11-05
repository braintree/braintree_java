package com.braintreegateway;

import com.braintreegateway.org.apache.commons.codec.binary.Base64;
import com.braintreegateway.util.ClientLibraryProperties;
import com.braintreegateway.util.Http;
import com.braintreegateway.util.TrUtil;

/**
 * This is the primary interface to the Braintree Gateway. It is used to
 * interact with:
 * <ul>
 * <li> {@link AddressGateway Addresses}
 * <li> {@link CreditCardGateway CreditCards}
 * <li> {@link CustomerGateway Customers}
 * <li> {@link SubscriptionGateway Subscriptions}
 * <li> {@link TransactionGateway Transactions}
 * </ul>
 *
 * Quick Start Example:
 *
 * <pre>
 * import java.math.BigDecimal;
 * import com.braintreegateway.*;
 *
 * public class BraintreeExample {
 *
 *     public static void main(String[] args) {
 *         BraintreeGateway gateway = new BraintreeGateway(Environment.SANDBOX, &quot;the_merchant_id&quot;, &quot;the_public_key&quot;, &quot;the_private_key&quot;);
 *
 *         TransactionRequest request = new TransactionRequest().amount(new BigDecimal(&quot;100.00&quot;)).creditCard().number(&quot;4111111111111111&quot;).expirationDate(&quot;05/2012&quot;).done();
 *
 *         Transaction transaction = gateway.transaction().sale(request).getTarget();
 *         System.out.println(&quot;Transaction ID: &quot; + transaction.getId());
 *         System.out.println(&quot;Status: &quot; + transaction.getStatus());
 *     }
 * }
 * </pre>
 */
public class BraintreeGateway {

    public static final String VERSION = new ClientLibraryProperties().version();

    private Configuration configuration;
    private Environment environment;
    private Http http;
    private String merchantId;
    private String privateKey;
    private String publicKey;

    /**
     * Instantiates a BraintreeGateway. Use the values provided by Braintree.
     *
     * @param environment
     *            Either {@link Environment#SANDBOX} or
     *            {@link Environment#PRODUCTION}.
     * @param merchantId
     *            the merchant id provided by Braintree.
     * @param publicKey
     *            the public key provided by Braintree.
     * @param privateKey
     *            the private key provided by Braintree.
     */
    public BraintreeGateway(Environment environment, String merchantId, String publicKey, String privateKey) {
        this.environment = environment;
        this.merchantId = merchantId;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.configuration = new Configuration(baseMerchantURL(), publicKey, privateKey);
        this.http = new Http(getAuthorizationHeader(), baseMerchantURL(), environment.certificateFilenames, BraintreeGateway.VERSION);
    }

    /**
     * Returns a BraintreeGateway specifically for Partner usage. Unless you are a partner, use the regular constructor instead.
     *
     * @param environment
     *            Either {@link Environment#SANDBOX} or
     *            {@link Environment#PRODUCTION}.
     * @param partnerId
     *            the partner id provided by Braintree.
     * @param publicKey
     *            the public key provided by Braintree.
     * @param privateKey
     *            the private key provided by Braintree.
     */
    public static BraintreeGateway forPartner(Environment environment, String partnerId, String publicKey, String privateKey) {
        return new BraintreeGateway(environment, partnerId, publicKey, privateKey);
    }

    /**
     * Returns an {@link AddOnGateway} for interacting with {@link AddOn}
     * objects.
     *
     * @return an {@link AddOnGateway}.
     */
    public AddOnGateway addOn() {
        return new AddOnGateway(http);
    }

    /**
     * Returns an {@link AddressGateway} for interacting with {@link Address}
     * objects.
     *
     * @return an {@link AddressGateway}.
     */
    public AddressGateway address() {
        return new AddressGateway(http);
    }

    public String baseMerchantURL() {
        return environment.baseURL + "/merchants/" + merchantId;
    }

    /**
     * Returns an {@link CreditCardGateway} for interacting with
     * {@link CreditCard} objects.
     *
     * @return an {@link CreditCardGateway}.
     */
    public CreditCardGateway creditCard() {
        return new CreditCardGateway(http, configuration);
    }

    public CreditCardVerificationGateway creditCardVerification() {
        return new CreditCardVerificationGateway(http, configuration);
    }

    /**
     * Returns an {@link CustomerGateway} for interacting with {@link Customer}
     * objects.
     *
     * @return an {@link CustomerGateway}.
     */
    public CustomerGateway customer() {
        return new CustomerGateway(http, configuration);
    }

    /**
     * Returns an {@link DiscountGateway} for interacting with {@link Discount}
     * objects.
     *
     * @return an {@link DiscountGateway}.
     */
    public DiscountGateway discount() {
        return new DiscountGateway(http);
    }

    public String getAuthorizationHeader() {
        return "Basic " + Base64.encodeBase64String((publicKey + ":" + privateKey).getBytes()).trim();
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    /**
     * Returns a String used for client side authentication.
     *
     * @param customerId
     *         the Id of the customer to authenticate an action for
     * @return a String.
     */
    public String generateAuthorizationFingerprint(String customerId) {
      return AuthorizationFingerprint.generate(this.merchantId, this.publicKey, this.privateKey, customerId);
    }

    /**
     * Returns a String used for client side authentication.
     *
     * @return a String.
     */
    public String generateAuthorizationFingerprint() {
      return generateAuthorizationFingerprint(null);
    }

    /**
     * Returns an {@link PlanGateway} for interacting with {@link Plan} objects.
     *
     * @return an {@link PlanGateway}.
     */
    public PlanGateway plan() {
        return new PlanGateway(http);
    }

    public SettlementBatchSummaryGateway settlementBatchSummary() {
        return new SettlementBatchSummaryGateway(http);
    }

    /**
     * Returns an {@link SubscriptionGateway} for interacting with
     * {@link Subscription} objects.
     *
     * @return an {@link SubscriptionGateway}.
     */
    public SubscriptionGateway subscription() {
        return new SubscriptionGateway(http);
    }

    /**
     * Returns an {@link TransactionGateway} for interacting with
     * {@link Transaction} objects.
     *
     * @return an {@link TransactionGateway}.
     */
    public TransactionGateway transaction() {
        return new TransactionGateway(http, configuration);
    }

    public TransparentRedirectGateway transparentRedirect() {
        return new TransparentRedirectGateway(http, configuration);
    }

    /**
     * Returns encoded transparent redirect data for the given {@link Request}
     * and redirect URL
     *
     * @param trData
     *            the transparent redirect data as a {@link Request} object.
     * @param redirectURL
     *            the redirect URL for the user after the transparent redirect
     *            POST.
     * @return a String of encoded transparent redirect data.
     */
    public String trData(Request trData, String redirectURL) {
        return new TrUtil(configuration).buildTrData(trData, redirectURL);
    }

    public WebhookNotificationGateway webhookNotification() {
        return new WebhookNotificationGateway(configuration);
    }

    public WebhookTestingGateway webhookTesting() {
        return new WebhookTestingGateway(configuration);
    }

    /**
     * Returns an {@link MerchantAccountGateway} for interacting with
     * {@link MerchantAccount} objects.
     *
     * @return an {@link MerchantAccountGateway}.
     */
    public MerchantAccountGateway merchantAccount() {
        return new MerchantAccountGateway(http);
    }

    public String getPrivateKey() {
      return privateKey;
    }

    public String getPublicKey() {
      return publicKey;
    }
}
