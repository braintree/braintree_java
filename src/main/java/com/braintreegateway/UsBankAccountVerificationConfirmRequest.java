package com.braintreegateway;
import java.util.List;

public class UsBankAccountVerificationConfirmRequest extends Request {
    private List<Integer> depositAmounts;

    public UsBankAccountVerificationConfirmRequest() {
    }

    public UsBankAccountVerificationConfirmRequest depositAmounts(List<Integer> depositAmounts) {
        this.depositAmounts = depositAmounts;
        return this;
    }

    @Override
    public String toXML() {
        RequestBuilder builder = new RequestBuilder("usBankAccountVerification");

        if (!depositAmounts.isEmpty()) {
            builder.addElement("depositAmounts", this.depositAmounts);
        }

        return builder.toXML();
    }
}
