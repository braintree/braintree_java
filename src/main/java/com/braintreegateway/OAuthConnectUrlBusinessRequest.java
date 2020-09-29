package com.braintreegateway;

public class OAuthConnectUrlBusinessRequest extends Request {

    private OAuthConnectUrlRequest parentRequest;
    private String name;
    private String registeredAs;
    private String industry;
    private String description;
    private String streetAddress;
    private String locality;
    private String region;
    private String postalCode;
    private String country;
    private String annualVolumeAmount;
    private String averageTransactionAmount;
    private String maximumTransactionAmount;
    private Boolean shipPhysicalGoods;
    private Integer fulfillmentCompletedIn;
    private String currency;
    private String website;
    private String establishedOn;

    public OAuthConnectUrlBusinessRequest(OAuthConnectUrlRequest parent) {
        this.parentRequest = parent;
    }

    public OAuthConnectUrlBusinessRequest name(String name) {
        this.name = name;
        return this;
    }

    public OAuthConnectUrlBusinessRequest registeredAs(String registeredAs) {
        this.registeredAs = registeredAs;
        return this;
    }

    public OAuthConnectUrlBusinessRequest industry(String industry) {
        this.industry = industry;
        return this;
    }

    public OAuthConnectUrlBusinessRequest description(String description) {
        this.description = description;
        return this;
    }

    public OAuthConnectUrlBusinessRequest streetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
        return this;
    }

    public OAuthConnectUrlBusinessRequest locality(String locality) {
        this.locality = locality;
        return this;
    }

    public OAuthConnectUrlBusinessRequest region(String region) {
        this.region = region;
        return this;
    }

    public OAuthConnectUrlBusinessRequest postalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public OAuthConnectUrlBusinessRequest country(String country) {
        this.country = country;
        return this;
    }

    public OAuthConnectUrlBusinessRequest annualVolumeAmount(String annualVolumeAmount) {
        this.annualVolumeAmount = annualVolumeAmount;
        return this;
    }

    public OAuthConnectUrlBusinessRequest averageTransactionAmount(String averageTransactionAmount) {
        this.averageTransactionAmount = averageTransactionAmount;
        return this;
    }

    public OAuthConnectUrlBusinessRequest maximumTransactionAmount(String maximumTransactionAmount) {
        this.maximumTransactionAmount = maximumTransactionAmount;
        return this;
    }

    public OAuthConnectUrlBusinessRequest shipPhysicalGoods(Boolean shipPhysicalGoods) {
        this.shipPhysicalGoods = shipPhysicalGoods;
        return this;
    }

    public OAuthConnectUrlBusinessRequest fulfillmentCompletedIn(Integer fulfillmentCompletedIn) {
        this.fulfillmentCompletedIn = fulfillmentCompletedIn;
        return this;
    }

    public OAuthConnectUrlBusinessRequest currency(String currency) {
        this.currency = currency;
        return this;
    }

    public OAuthConnectUrlBusinessRequest website(String website) {
        this.website = website;
        return this;
    }

    public OAuthConnectUrlBusinessRequest establishedOn(String establishedOn) {
        this.establishedOn = establishedOn;
        return this;
    }

    @Override
    public String toQueryString(String root) {
        RequestBuilder builder = new RequestBuilder("business")
            .addElement("name", name)
            .addElement("registeredAs", registeredAs)
            .addElement("industry", industry)
            .addElement("description", description)
            .addElement("streetAddress", streetAddress)
            .addElement("country", country)
            .addElement("locality", locality)
            .addElement("region", region)
            .addElement("postalCode", postalCode)
            .addElement("annualVolumeAmount", annualVolumeAmount)
            .addElement("averageTransactionAmount", averageTransactionAmount)
            .addElement("maximumTransactionAmount", maximumTransactionAmount)
            .addElement("shipPhysicalGoods", shipPhysicalGoods)
            .addElement("fulfillmentCompletedIn", fulfillmentCompletedIn)
            .addElement("currency", currency)
            .addElement("website", website)
            .addElement("establishedOn", establishedOn);

        return builder.toQueryString();
    }

    public OAuthConnectUrlRequest done() {
        return parentRequest;
    }
}
