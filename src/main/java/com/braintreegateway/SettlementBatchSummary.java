package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SettlementBatchSummary {

    private List<Map<String,String>> records;

    public SettlementBatchSummary(NodeWrapper node) {
        records = new ArrayList<Map<String,String>>();
        
        for (NodeWrapper record : node.findAll("records/record")) {
            records.add(record.findMap("*"));
        }
    }

    public List<Map<String,String>> getRecords() {
        return records;
    }

}
