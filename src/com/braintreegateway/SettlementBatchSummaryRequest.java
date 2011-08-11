package com.braintreegateway;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class SettlementBatchSummaryRequest extends Request {
    
    private Calendar settlementDate;
    private String groupByCustomField;
    
    public SettlementBatchSummaryRequest() {
        super();
    }
    
    public String toXML() {
        return buildRequest("settlement-batch-summary").toXML();
    }
    
    public SettlementBatchSummaryRequest settlementDate(Calendar settlementDate) {
        this.settlementDate = settlementDate;
        return this;
    }
    

    protected RequestBuilder buildRequest(String root) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        RequestBuilder request = new RequestBuilder(root);
        request.addElement("settlement-date",  dateFormat.format(settlementDate.getTime()));
        if (groupByCustomField != null) {
            request.addElement("group-by-custom-field", groupByCustomField);            
        }
        return request;
    }

    public SettlementBatchSummaryRequest groupByCustomField(String groupByCustomField) {
        this.groupByCustomField = groupByCustomField;
        return this;
    }
}
