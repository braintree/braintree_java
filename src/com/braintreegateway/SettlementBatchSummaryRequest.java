package com.braintreegateway;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
        RequestBuilder request = new RequestBuilder(root);
        request.addElement("settlement-date", dateString(settlementDate));
        if (groupByCustomField != null) {
            request.addElement("group-by-custom-field", groupByCustomField);            
        }
        return request;
    }

    public static String dateString(Calendar settlementDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setCalendar(settlementDate);
        return dateFormat.format(settlementDate.getTime());
    }

    public SettlementBatchSummaryRequest groupByCustomField(String groupByCustomField) {
        this.groupByCustomField = groupByCustomField;
        return this;
    }
}
