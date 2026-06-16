package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import com.braintreegateway.SearchCriteria;

public class SearchCriteriaTest {

    @Test
    public void typeValueConstructorBuildsXmlElement() {
        SearchCriteria criteria = new SearchCriteria("is", "the-value");

        assertTrue(criteria.toXML().contains("<is>the-value</is>"), criteria.toXML());
    }

    @Test
    public void listConstructorBuildsItemElements() {
        SearchCriteria criteria = new SearchCriteria(Arrays.asList("one", "two"));

        String xml = criteria.toXML();
        assertTrue(xml.contains("<item>one</item>"), xml);
        assertTrue(xml.contains("<item>two</item>"), xml);
    }

    @Test
    public void toQueryStringThrowsUnsupportedOperationException() {
        SearchCriteria criteria = new SearchCriteria("is", "value");

        assertThrows(UnsupportedOperationException.class, () -> criteria.toQueryString());
        assertThrows(UnsupportedOperationException.class, () -> criteria.toQueryString("root"));
    }
}
