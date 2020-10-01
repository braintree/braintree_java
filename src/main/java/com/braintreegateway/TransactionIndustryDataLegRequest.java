package com.braintreegateway;

import java.math.BigDecimal;
import java.util.Calendar;

public class TransactionIndustryDataLegRequest extends Request {

    private TransactionIndustryDataRequest parent;
    private String conjunctionTicket;
    private String exchangeTicket;
    private String couponNumber;
    private String serviceClass;
    private String carrierCode;
    private String fareBasisCode;
    private String flightNumber;
    private Calendar departureDate;
    private String departureAirportCode;
    private String departureTime;
    private String arrivalAirportCode;
    private String arrivalTime;
    private Boolean stopoverPermitted;
    private BigDecimal fareAmount;
    private BigDecimal feeAmount;
    private BigDecimal taxAmount;
    private String endorsementOrRestrictions;

    public TransactionIndustryDataLegRequest(TransactionIndustryDataRequest parent) {
        this.parent = parent;
    }

    public TransactionIndustryDataLegRequest conjunctionTicket(String conjunctionTicket) {
        this.conjunctionTicket = conjunctionTicket;
        return this;
    }

    public TransactionIndustryDataLegRequest exchangeTicket(String exchangeTicket) {
        this.exchangeTicket = exchangeTicket;
        return this;
    }

    public TransactionIndustryDataLegRequest couponNumber(String couponNumber) {
        this.couponNumber = couponNumber;
        return this;
    }

    public TransactionIndustryDataLegRequest serviceClass(String serviceClass) {
        this.serviceClass = serviceClass;
        return this;
    }

    public TransactionIndustryDataLegRequest carrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
        return this;
    }

    public TransactionIndustryDataLegRequest fareBasisCode(String fareBasisCode) {
        this.fareBasisCode = fareBasisCode;
        return this;
    }

    public TransactionIndustryDataLegRequest flightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
        return this;
    }

    public TransactionIndustryDataLegRequest departureDate(Calendar departureDate) {
        this.departureDate = departureDate;
        return this;
    }

    public TransactionIndustryDataLegRequest departureAirportCode(String departureAirportCode) {
        this.departureAirportCode = departureAirportCode;
        return this;
    }

    public TransactionIndustryDataLegRequest departureTime(String departureTime) {
        this.departureTime = departureTime;
        return this;
    }

    public TransactionIndustryDataLegRequest arrivalAirportCode(String arrivalAirportCode) {
        this.arrivalAirportCode = arrivalAirportCode;
        return this;
    }

    public TransactionIndustryDataLegRequest arrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
        return this;
    }

    public TransactionIndustryDataLegRequest stopoverPermitted(Boolean stopoverPermitted) {
        this.stopoverPermitted = stopoverPermitted;
        return this;
    }

    public TransactionIndustryDataLegRequest fareAmount(BigDecimal fareAmount) {
        this.fareAmount = fareAmount;
        return this;
    }

    public TransactionIndustryDataLegRequest feeAmount(BigDecimal feeAmount) {
        this.feeAmount = feeAmount;
        return this;
    }

    public TransactionIndustryDataLegRequest taxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
        return this;
    }

    public TransactionIndustryDataLegRequest endorsementOrRestrictions(String endorsementOrRestrictions) {
        this.endorsementOrRestrictions = endorsementOrRestrictions;
        return this;
    }

    public TransactionIndustryDataRequest done() {
        return parent;
    }

    @Override
    public String toXML() {
        return buildRequest("item").toXML();
    }

    @Override
    public String toQueryString() {
        return toQueryString("item");
    }

    @Override
    public String toQueryString(String root) {
        return buildRequest(root).toQueryString();
    }

    protected RequestBuilder buildRequest(String root) {
        return new RequestBuilder(root)
            .addElement("conjunctionTicket", conjunctionTicket)
            .addElement("exchangeTicket", exchangeTicket)
            .addElement("couponNumber", couponNumber)
            .addElement("serviceClass", serviceClass)
            .addElement("carrierCode", carrierCode)
            .addElement("fareBasisCode", fareBasisCode)
            .addElement("flightNumber", flightNumber)
            .addElement("departureDate", departureDate)
            .addElement("departureAirportCode", departureAirportCode)
            .addElement("departureTime", departureTime)
            .addElement("arrivalAirportCode", arrivalAirportCode)
            .addElement("arrivalTime", arrivalTime)
            .addElement("stopoverPermitted", stopoverPermitted)
            .addElement("fareAmount", fareAmount)
            .addElement("feeAmount", feeAmount)
            .addElement("taxAmount", taxAmount)
            .addElement("endorsementOrRestrictions", endorsementOrRestrictions);
    }
}
