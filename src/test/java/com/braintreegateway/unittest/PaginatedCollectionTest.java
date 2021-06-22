package com.braintreegateway.unittest;

import java.util.ArrayList;
import java.util.List;

import com.braintreegateway.PaginatedCollection;
import com.braintreegateway.PaginatedResult;
import com.braintreegateway.SimplePager;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

public class PaginatedCollectionTest {
    @Test
    public void testGetsSinglePageFullCollection() {
        SimplePager pager = mock(SimplePager.class);

        List<Integer> values = new ArrayList<Integer>();
        values.add(1);
        when(pager.getPage(1)).thenReturn(new PaginatedResult(1, 1, values));

        PaginatedCollection<Integer> collection = new PaginatedCollection<Integer>(pager);

        List<Integer> results = new ArrayList<Integer>();
        for (Integer i : collection) {
            results.add(i);
        }

        verify(pager, times(1)).getPage(1);
        verify(pager, times(1)).getPage(any(int.class));
    }
}
