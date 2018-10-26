package com.braintreegateway;

import com.braintreegateway.util.EnumUtils;
import com.braintreegateway.util.NodeWrapper;

import java.math.BigDecimal;
import java.util.Calendar;

public class AuthorizationAdjustment {
    private BigDecimal amount;
    private Boolean success;
    private Calendar timestamp;
    private String processorResponseCode;
    private String processorResponseText;
    private ProcessorResponseType processorResponseType;

    public AuthorizationAdjustment(NodeWrapper node) {
        amount = node.findBigDecimal("amount");
        success = node.findBoolean("success");
        timestamp = node.findDateTime("timestamp");
        processorResponseCode = node.findString("processor-response-code");
        processorResponseText = node.findString("processor-response-text");
        processorResponseType = EnumUtils.findByName(ProcessorResponseType.class, node.findString("processor-response-type"), ProcessorResponseType.UNRECOGNIZED);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Boolean isSuccess() {
        return success;
    }

    public Calendar getTimestamp() {
        return timestamp;
    }

    public String getProcessorResponseCode() {
        return processorResponseCode;
    }

    public String getProcessorResponseText() {
        return processorResponseText;
    }

    public ProcessorResponseType getProcessorResponseType() {
        return processorResponseType;
    }
}
