package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;

public final class IndividualDetails {
    private final String firstName;
    private final String lastName;
    private final String dateOfBirth;
    private final String email;
    private final String phone;
    private final Address address;
    private final String ssnLast4;

    public IndividualDetails(NodeWrapper node) {
        firstName = node.findString("first-name");
        lastName = node.findString("last-name");
        dateOfBirth = node.findString("date-of-birth");
        email = node.findString("email");
        phone = node.findString("phone");
        ssnLast4 = node.findString("ssn-last-4");
        NodeWrapper addressNode = node.findFirst("address");
        if (addressNode != null)
            this.address = new Address(addressNode);
        else
            this.address = null;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public Address getAddress() {
        return address;
    }

    public String getSsnLast4() {
        return ssnLast4;
    }
}
