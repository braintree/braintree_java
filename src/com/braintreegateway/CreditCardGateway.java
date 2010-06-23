package com.braintreegateway;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import com.braintreegateway.util.Http;
import com.braintreegateway.util.NodeWrapper;

/**
 * Provides methods to create, delete, find, and update {@link CreditCard}
 * objects. This class does not need to be instantiated directly. Instead, use
 * {@link BraintreeGateway#creditCard()} to get an instance of this class:
 * 
 * <pre>
 * BraintreeGateway gateway = new BraintreeGateway(...);
 * gateway.creditCard().create(...)
 * </pre>
 * 
 * For more detailed information on {@link CreditCard CreditCards}, see <a
 * href="http://www.braintreepaymentsolutions.com/gateway/credit-card-api"
 * target
 * ="_blank">http://www.braintreepaymentsolutions.com/gateway/credit-card-api
 * </a><br />
 * For more detailed information on credit card verifications, see <a href=
 * "http://www.braintreepaymentsolutions.com/gateway/credit-card-verification-api"
 * target="_blank">http://www.braintreepaymentsolutions.com/gateway/credit-card-
 * verification-api</a>
 */
public class CreditCardGateway {
    private Configuration configuration;
    private Http http;

    public CreditCardGateway(Http http, Configuration configuration) {
        this.configuration = configuration;
        this.http = http;
    }

    /**
     * Confirms the transparent redirect request and creates a
     * {@link CreditCard} based on the parameters submitted with the transparent
     * redirect.
     * 
     * @param queryString
     *            the queryString of the transparent redirect.
     * @return a {@link Result}.
     */
    public Result<CreditCard> confirmTransparentRedirect(String queryString) {
        TransparentRedirectRequest trRequest = new TransparentRedirectRequest(configuration, queryString);
        NodeWrapper node = http.post("/payment_methods/all/confirm_transparent_redirect_request", trRequest);
        return new Result<CreditCard>(node, CreditCard.class);
    }

    /**
     * Creates an {@link CreditCard}.
     * 
     * @param request
     *            the request.
     * @return a {@link Result}.
     */
    public Result<CreditCard> create(CreditCardRequest request) {
        NodeWrapper node = http.post("/payment_methods", request);
        return new Result<CreditCard>(node, CreditCard.class);
    }

    /**
     * Deletes a {@link CreditCard}.
     * 
     * @param token
     *            the CreditCard's token.
     * @return a {@link Result}.
     */
    public Result<CreditCard> delete(String token) {
        http.delete("/payment_methods/" + token);
        return new Result<CreditCard>();
    }

    /**
     * Finds a {@link CreditCard}.
     * 
     * @param token
     *            the CreditCard's token.
     * @return the {@link CreditCard} or raises a
     *         {@link com.braintreegateway.exceptions.NotFoundException}.
     */
    public CreditCard find(String token) {
        return new CreditCard(http.get("/payment_methods/" + token));
    }

    /**
     * Returns the transparent redirect URL for creating a {@link CreditCard}.
     * 
     * @return a URL as a String.
     */
    public String transparentRedirectURLForCreate() {
        return configuration.baseMerchantURL + "/payment_methods/all/create_via_transparent_redirect_request";
    }

    /**
     * Returns the transparent redirect URL for updating a {@link CreditCard}.
     * 
     * @return a URL as a String.
     */
    public String transparentRedirectURLForUpdate() {
        return configuration.baseMerchantURL + "/payment_methods/all/update_via_transparent_redirect_request";
    }

    /**
     * Updates a {@link CreditCard}.
     * 
     * @param token
     *            the CreditCard's token.
     * @param request
     *            the request.
     * @return a {@link Result}.
     */
    public Result<CreditCard> update(String token, CreditCardRequest request) {
        NodeWrapper node = http.put("/payment_methods/" + token, request);
        return new Result<CreditCard>(node, CreditCard.class);
    }

    /**
     * Returns a {@link ResourceCollection} of all expired credit cards.
     * 
     * @return a {@link ResourceCollection}.
     */
    public ResourceCollection<CreditCard> expired() {
        NodeWrapper response = http.post("/payment_methods/all/expired_ids");
        return new ResourceCollection<CreditCard>(new ExpiredCreditCardPager(this), response);
    }

    List<CreditCard> fetchExpiredCreditCards(List<String> ids) {
        CustomerSearchRequest query = new CustomerSearchRequest().ids().in(ids);

        NodeWrapper response = http.post("/payment_methods/all/expired", query);

        List<CreditCard> items = new ArrayList<CreditCard>();
        for (NodeWrapper node : response.findAll("credit-card")) {
            items.add(new CreditCard(node));
        }

        return items;
    }

    /**
     * Returns a {@link ResourceCollection} of all credit cards expiring between
     * the given calendars.
     *
     * @return a {@link ResourceCollection}.
     */
    public ResourceCollection<CreditCard> expiringBetween(Calendar start, Calendar end) {
        String queryString = dateQueryString(start, end);
        NodeWrapper response = http.post("/payment_methods/all/expiring_ids?" + queryString);
        return new ResourceCollection<CreditCard>(new ExpiringCreditCardPager(this, queryString), response);
    }

    List<CreditCard> fetchExpiringCreditCards(List<String> ids, String queryString) {
        CustomerSearchRequest query = new CustomerSearchRequest().ids().in(ids);

        NodeWrapper response = http.post("/payment_methods/all/expiring?" + queryString, query);

        List<CreditCard> items = new ArrayList<CreditCard>();
        for (NodeWrapper node : response.findAll("credit-card")) {
            items.add(new CreditCard(node));
        }

        return items;
    }

    private String dateQueryString(Calendar start, Calendar end) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMyyyy");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String formattedStart = dateFormat.format(start.getTime());
        String formattedEnd = dateFormat.format(end.getTime());

        return String.format("start=%s&end=%s", formattedStart, formattedEnd);
    }
}
