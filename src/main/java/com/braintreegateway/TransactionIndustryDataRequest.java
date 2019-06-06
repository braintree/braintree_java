package com.braintreegateway;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
    private BigDecimal roomTax;
    private String passengerFirstName;
    private String passengerLastName;
    private String passengerMiddleInitial;
    private String passengerTitle;
    private Calendar issuedDate;
    private String travelAgencyName;
    private String travelAgencyCode;
    private String ticketNumber;
    private String issuingCarrierCode;
    private String customerCode;
    private BigDecimal fareAmount;
    private BigDecimal feeAmount;
    private BigDecimal taxAmount;
    private Boolean restrictedTicket;
    private Boolean noShow;
    private Boolean advancedDeposit;
    private Boolean fireSafe;
    private String propertyPhone;
    private List<TransactionIndustryDataLegRequest> legRequests;
    private List<TransactionIndustryDataAdditionalChargeRequest> additionalChargeRequests;

    public TransactionIndustryDataRequest(IndustryRequest parent) {
        this.parent = (TransactionIndustryRequest) parent;
        this.legRequests = new ArrayList<TransactionIndustryDataLegRequest>();
        this.additionalChargeRequests = new ArrayList<TransactionIndustryDataAdditionalChargeRequest>();
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

    public TransactionIndustryDataRequest roomRate(BigDecimal roomRate){
        this.roomRate = roomRate.toString();
        return this;
    }

    public TransactionIndustryDataRequest roomTax(BigDecimal roomTax){
        this.roomTax = roomTax;
        return this;
    }

    public TransactionIndustryDataRequest passengerFirstName(String passengerFirstName) {
        this.passengerFirstName = passengerFirstName;
        return this;
    }

    public TransactionIndustryDataRequest passengerLastName(String passengerLastName) {
        this.passengerLastName = passengerLastName;
        return this;
    }

    public TransactionIndustryDataRequest passengerMiddleInitial(String passengerMiddleInitial) {
        this.passengerMiddleInitial = passengerMiddleInitial;
        return this;
    }

    public TransactionIndustryDataRequest passengerTitle(String passengerTitle) {
        this.passengerTitle = passengerTitle;
        return this;
    }

    public TransactionIndustryDataRequest issuedDate(Calendar issuedDate) {
        this.issuedDate = issuedDate;
        return this;
    }

    public TransactionIndustryDataRequest travelAgencyName(String travelAgencyName) {
        this.travelAgencyName = travelAgencyName;
        return this;
    }

    public TransactionIndustryDataRequest travelAgencyCode(String travelAgencyCode) {
        this.travelAgencyCode = travelAgencyCode;
        return this;
    }

    public TransactionIndustryDataRequest ticketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
        return this;
    }

    public TransactionIndustryDataRequest issuingCarrierCode(String issuingCarrierCode) {
        this.issuingCarrierCode = issuingCarrierCode;
        return this;
    }

    public TransactionIndustryDataRequest customerCode(String customerCode) {
        this.customerCode = customerCode;
        return this;
    }

    public TransactionIndustryDataRequest fareAmount(BigDecimal fareAmount) {
        this.fareAmount = fareAmount;
        return this;
    }

    public TransactionIndustryDataRequest feeAmount(BigDecimal feeAmount) {
        this.feeAmount = feeAmount;
        return this;
    }

    public TransactionIndustryDataRequest taxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
        return this;
    }

    public TransactionIndustryDataRequest restrictedTicket(Boolean restrictedTicket) {
        this.restrictedTicket = restrictedTicket;
        return this;
    }

    public TransactionIndustryDataRequest noShow(Boolean noShow) {
        this.noShow = noShow;
        return this;
    }

    public TransactionIndustryDataRequest advancedDeposit(Boolean advancedDeposit) {
        this.advancedDeposit = advancedDeposit;
        return this;
    }

    public TransactionIndustryDataRequest fireSafe(Boolean fireSafe) {
        this.fireSafe = fireSafe;
        return this;
    }

    public TransactionIndustryDataRequest propertyPhone(String propertyPhone) {
        this.propertyPhone = propertyPhone;
        return this;
    }

    public TransactionIndustryDataLegRequest leg() {
        TransactionIndustryDataLegRequest legRequest = new TransactionIndustryDataLegRequest(this);
        legRequests.add(legRequest);
        return legRequest;
    }

    public TransactionIndustryDataAdditionalChargeRequest additionalCharge() {
        TransactionIndustryDataAdditionalChargeRequest additionalChargeRequest = new TransactionIndustryDataAdditionalChargeRequest(this);
        additionalChargeRequests.add(additionalChargeRequest);
        return additionalChargeRequest;
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
        RequestBuilder builder = new RequestBuilder(root).
                addElement("folioNumber", folioNumber).
                addElement("checkInDate", checkInDate).
                addElement("checkOutDate", checkOutDate).
                addElement("travelPackage", travelPackage).
                addElement("departureDate", departureDate).
                addElement("lodgingCheckInDate", lodgingCheckInDate).
                addElement("lodgingCheckOutDate", lodgingCheckOutDate).
                addElement("lodgingName", lodgingName).
                addElement("roomRate", roomRate).
                addElement("roomTax", roomTax).
                addElement("passengerFirstName", passengerFirstName).
                addElement("passengerLastName", passengerLastName).
                addElement("passengerMiddleInitial", passengerMiddleInitial).
                addElement("passengerTitle", passengerTitle).
                addElement("issuedDate", issuedDate).
                addElement("travelAgencyName", travelAgencyName).
                addElement("travelAgencyCode", travelAgencyCode).
                addElement("ticketNumber", ticketNumber).
                addElement("issuingCarrierCode", issuingCarrierCode).
                addElement("customerCode", customerCode).
                addElement("fareAmount", fareAmount).
                addElement("feeAmount", feeAmount).
                addElement("taxAmount", taxAmount).
                addElement("restrictedTicket", restrictedTicket).
                addElement("noShow", noShow).
                addElement("advancedDeposit", advancedDeposit).
                addElement("fireSafe", fireSafe).
                addElement("propertyPhone", propertyPhone);

        if (!legRequests.isEmpty()) {
            builder.addElement("legs", legRequests);
        }

        if (!additionalChargeRequests.isEmpty()) {
            builder.addElement("additionalCharges", additionalChargeRequests);
        }

        return builder;
    }

    public TransactionIndustryRequest done() {
        return parent;
    }

}
