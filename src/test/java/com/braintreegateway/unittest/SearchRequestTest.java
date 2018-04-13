package com.braintreegateway.unittest;

import com.braintreegateway.SearchRequest;
import com.braintreegateway.SearchCriteria;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class SearchRequestTest {

    @Test
    public void generatesXMLForSingleSearchCriteria() {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.addCriteria("credit_card_number", new SearchCriteria("starts_with", "4111"));

        String expectedXml = "<search><credit_card_number><starts_with>4111</starts_with></credit_card_number></search>";
        assertEquals(expectedXml, searchRequest.toXML());
    }

    @Test
    public void generatesXMLForMultipleSearchCriteriaInNode() {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.addCriteria("credit_card_number", new SearchCriteria("starts_with", "4111"));
        searchRequest.addCriteria("credit_card_number", new SearchCriteria("ends_with", "1111"));

        String expectedXml = "<search><credit_card_number><starts_with>4111</starts_with><ends_with>1111</ends_with></credit_card_number></search>";
        assertEquals(expectedXml, searchRequest.toXML());
    }
}
