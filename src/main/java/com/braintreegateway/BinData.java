package com.braintreegateway;

import static com.braintreegateway.util.EnumUtils.findByToString;

import com.braintreegateway.CreditCard.*;
import com.braintreegateway.util.NodeWrapper;
import java.util.Map;
import com.braintreegateway.enums.PrepaidReloadable;

public class BinData {
    private String commercial;
    private String countryOfIssuance;
    private String debit;
    private String durbinRegulated;
    private String healthcare;
    private String issuingBank;
    private String payroll;
    private String prepaid;
    private String prepaidReloadable;
    private String productId;

    public BinData(NodeWrapper node) {
        commercial = node.findString("commercial");
        countryOfIssuance = node.findString("country-of-issuance");
        debit = node.findString("debit");
        durbinRegulated = node.findString("durbin-regulated");
        healthcare = node.findString("healthcare");
        issuingBank = node.findString("issuing-bank");
        payroll = node.findString("payroll");
        prepaid = node.findString("prepaid");
        prepaidReloadable = node.findString("prepaid-reloadable");
        productId = node.findString("product-id");
    }

    public BinData(Map<String, String> map) {
        commercial = map.get("commercial");
        countryOfIssuance = map.get("countryOfIssuance");
        debit = map.get("debit");
        durbinRegulated = map.get("durbinRegulated");
        healthcare = map.get("healthcare");
        issuingBank = map.get("issuingBank");
        payroll = map.get("payroll");
        prepaid = map.get("prepaid");
        prepaidReloadable = map.get("prepaid-reloadable");
        productId = map.get("productId");
    }

    public Commercial getCommercial() {
        return findByToString(Commercial.values(), commercial, Commercial.UNKNOWN);
    }

    public Debit getDebit() {
        return findByToString(Debit.values(), debit, Debit.UNKNOWN);
    }

    public DurbinRegulated getDurbinRegulated() {
        return findByToString(DurbinRegulated.values(), durbinRegulated, DurbinRegulated.UNKNOWN);
    }

    public Healthcare getHealthcare() {
        return findByToString(Healthcare.values(), healthcare, Healthcare.UNKNOWN);
    }

    public Payroll getPayroll() {
        return findByToString(Payroll.values(), payroll, Payroll.UNKNOWN);
    }

    public Prepaid getPrepaid() {
        return findByToString(Prepaid.values(), prepaid, Prepaid.UNKNOWN);
    }

    public PrepaidReloadable getPrepaidReloadable() {
        return findByToString(PrepaidReloadable.values(), prepaidReloadable, PrepaidReloadable.UNKNOWN);
    }

    public String getProductId() {
        if ("".equals(productId)) {
            return "Unknown";
        } else {
            return productId;
        }
    }

    public String getCountryOfIssuance() {
        if ("".equals(countryOfIssuance)) {
            return "Unknown";
        } else {
            return countryOfIssuance;
        }
    }

    public String getIssuingBank() {
        if ("".equals(issuingBank)) {
            return "Unknown";
        } else {
            return issuingBank;
        }
    }
}
