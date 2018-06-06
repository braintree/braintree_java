package com.braintreegateway;

import com.braintreegateway.test.TestingGateway;
import com.braintreegateway.util.GraphQLClient;
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

    private Configuration configuration;
    private GraphQLClient graphQLClient;
    private Http http;

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
        this.configuration = new Configuration(environment, merchantId, publicKey, privateKey);
        this.http = new Http(configuration);
        this.graphQLClient = new GraphQLClient(configuration);
    }

    public BraintreeGateway(String environment, String merchantId, String publicKey, String privateKey) {
        this.configuration = new Configuration(environment, merchantId, publicKey, privateKey);
        this.http = new Http(configuration);
        this.graphQLClient = new GraphQLClient(configuration);
    }

    public BraintreeGateway(String clientId, String clientSecret) {
        this.configuration = new Configuration(clientId, clientSecret);
        this.http = new Http(configuration);
        this.graphQLClient = new GraphQLClient(configuration);
    }

    public BraintreeGateway(String accessToken) {
        this.configuration = new Configuration(accessToken);
        this.http = new Http(configuration);
        this.graphQLClient = new GraphQLClient(configuration);
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
     * @return a BraintreeGateway specifically for Partner usage
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
        return new AddOnGateway(http, configuration);
    }

    /**
     * Returns an {@link AddressGateway} for interacting with {@link Address}
     * objects.
     *
     * @return an {@link AddressGateway}.
     */
    public AddressGateway address() {
        return new AddressGateway(http, configuration);
    }

    public ClientTokenGateway clientToken() {
        return new ClientTokenGateway(http, configuration);
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

    public UsBankAccountVerificationGateway usBankAccountVerification() {
        return new UsBankAccountVerificationGateway(http, configuration);
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
        return new DiscountGateway(http, configuration);
    }

    /**
     * Returns a {@link DisputeGateway} for interacting with {@link Dispute}
     * objects.
     *
     * @return an {@link DisputeGateway}.
     */
    public DisputeGateway dispute() {
        return new DisputeGateway(http, configuration);
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setProxy(String url, Integer port) {
        configuration.setProxy(url, port);
    }

    public PaymentMethodGateway paymentMethod() {
        return new PaymentMethodGateway(http, configuration);
    }

    public PaymentMethodNonceGateway paymentMethodNonce() {
        return new PaymentMethodNonceGateway(http, configuration);
    }

    public PayPalAccountGateway paypalAccount() {
        return new PayPalAccountGateway(http, configuration);
    }

    public UsBankAccountGateway usBankAccount() {
        return new UsBankAccountGateway(this, http, configuration);
    }

    public IdealPaymentGateway idealPayment() {
        return new IdealPaymentGateway(this, http, configuration);
    }

    /**
     * Returns an {@link PlanGateway} for interacting with {@link Plan} objects.
     *
     * @return an {@link PlanGateway}.
     */
    public PlanGateway plan() {
        return new PlanGateway(http, configuration);
    }

    public SettlementBatchSummaryGateway settlementBatchSummary() {
        return new SettlementBatchSummaryGateway(http, configuration);
    }

    /**
     * Returns an {@link SubscriptionGateway} for interacting with
     * {@link Subscription} objects.
     *
     * @return an {@link SubscriptionGateway}.
     */
    public SubscriptionGateway subscription() {
        return new SubscriptionGateway(http, configuration);
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

    /**
     * Returns an {@link TransactionLineItemGateway} for interacting with
     * {@link TransactionLineItem} objects.
     *
     * @return an {@link TransactionLineItemGateway}.
     */
    public TransactionLineItemGateway transactionLineItem() {
        return new TransactionLineItemGateway(http, configuration);
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
        return new MerchantAccountGateway(http, configuration);
    }

    public MerchantGateway merchant() {
        return new MerchantGateway(http, configuration);
    }

    public OAuthGateway oauth() {
        return new OAuthGateway(http, configuration);
    }

    public TestingGateway testing() {
        return new TestingGateway(http, configuration);
    }

    public DocumentUploadGateway documentUpload() {
        return new DocumentUploadGateway(http, configuration);
    }
}
