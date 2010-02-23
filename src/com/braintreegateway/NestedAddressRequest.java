package com.braintreegateway;

import com.braintreegateway.util.QueryString;

public class NestedAddressRequest<T> extends Request {

    private String countryName;
    private String extendedAddress;
    private String firstName;
    private String lastName;
    private String locality;
    private T parent;
    private String postalCode;
    private String region;
    private String streetAddress;
    private String company;
    private String tagName;

    public NestedAddressRequest(T parent, String tagName) {
        this.parent = parent;
        this.tagName = tagName;
    }

    public NestedAddressRequest<T> company(String company) {
        this.company = company;
        return this;
    }

    public NestedAddressRequest<T> countryName(String countryName) {
        this.countryName = countryName;
        return this;
    }

    public NestedAddressRequest<T> extendedAddress(String extendedAddress) {
        this.extendedAddress = extendedAddress;
        return this;
    }

    public NestedAddressRequest<T> firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public NestedAddressRequest<T> lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public NestedAddressRequest<T> locality(String locality) {
        this.locality = locality;
        return this;
    }

    public NestedAddressRequest<T> postalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public NestedAddressRequest<T> region(String region) {
        this.region = region;
        return this;
    }

    public NestedAddressRequest<T> streetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
        return this;
    }

    @Override
    public String toXML() {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("<%s>", tagName));
        builder.append(buildXMLElement("firstName", firstName));
        builder.append(buildXMLElement("lastName", lastName));
        builder.append(buildXMLElement("company", company));
        builder.append(buildXMLElement("countryName", countryName));
        builder.append(buildXMLElement("extendedAddress", extendedAddress));
        builder.append(buildXMLElement("locality", locality));
        builder.append(buildXMLElement("postalCode", postalCode));
        builder.append(buildXMLElement("region", region));
        builder.append(buildXMLElement("streetAddress", streetAddress));
        builder.append(String.format("</%s>", tagName));
        return builder.toString();
    }

    public T done() {
        return parent;
    }

    public String toQueryString() {
        return toQueryString(tagName);
    }

    public String toQueryString(String root) {
        return new QueryString().
            append(parentBracketChildString(root, "first_name"), firstName).
            append(parentBracketChildString(root, "last_name"), lastName).
            append(parentBracketChildString(root, "company"), company).
            append(parentBracketChildString(root, "country_name"), countryName).
            append(parentBracketChildString(root, "extended_address"), extendedAddress).
            append(parentBracketChildString(root, "locality"), locality).
            append(parentBracketChildString(root, "postal_code"), postalCode).
            append(parentBracketChildString(root, "region"), region).
            append(parentBracketChildString(root, "street_address"), streetAddress).
            toString();
    }
}
