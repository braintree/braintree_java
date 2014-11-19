package com.braintreegateway;

public class TransactionIndustryDataRequest extends Request {

    private final TransactionIndustryRequest parent;
    private String folioNumber;
    private String checkInDate;
    private String checkOutDate;
    private String travelPackage;
    private String departureDate;
    private String lodgingCheckInDate;
    private String lodgingCheckOutDate;
    private String lodgingName;
    private String roomRate;

    public TransactionIndustryDataRequest(IndustryRequest parent) {
        this.parent = (TransactionIndustryRequest) parent;
    }

    public TransactionIndustryDataRequest folioNumber(String folioNumber) {
        this.folioNumber = folioNumber;
        return this;
    }

    public TransactionIndustryDataRequest checkInDate(String checkInDate) {
        this.checkInDate = checkInDate;
        return this;
    }

    public TransactionIndustryDataRequest checkOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
        return this;
    }

    public TransactionIndustryDataRequest travelPackage(String travelPackage) {
        this.travelPackage = travelPackage;
        return this;
    }

    public TransactionIndustryDataRequest departureDate(String departureDate){
        this.departureDate = departureDate;
        return this;
    }

    public TransactionIndustryDataRequest lodgingCheckInDate(String lodgingCheckInDate){
        this.lodgingCheckInDate = lodgingCheckInDate;
        return this;
    }

    public TransactionIndustryDataRequest lodgingCheckOutDate(String lodgingCheckOutDate){
        this.lodgingCheckOutDate = lodgingCheckOutDate;
        return this;
    }

    public TransactionIndustryDataRequest lodgingName(String lodgingName){
        this.lodgingName = lodgingName;
        return this;
    }

    public TransactionIndustryDataRequest roomRate(String roomRate){
        this.roomRate = roomRate;
        return this;
    }

    @Override
    public String toXML() {
        return buildRequest("data").toXML();
    }

    @Override
    public String toQueryString(String root) {
        return buildRequest(root).toQueryString();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root).
                addElement("folioNumber", folioNumber).
                addElement("checkInDate", checkInDate).
                addElement("checkOutDate", checkOutDate).
                addElement("travelPackage", travelPackage).
                addElement("departureDate", departureDate).
                addElement("lodgingCheckInDate", lodgingCheckInDate).
                addElement("lodgingCheckOutDate", lodgingCheckOutDate).
                addElement("lodgingName", lodgingName).
                addElement("roomRate", roomRate);
    }

    public TransactionIndustryRequest done() {
        return parent;
    }

}
