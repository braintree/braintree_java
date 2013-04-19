package com.braintreegateway;

import java.util.ArrayList;
import java.util.List;

import com.braintreegateway.util.NodeWrapperFactory;
import org.junit.Assert;

import org.junit.Test;

import com.braintreegateway.util.NodeWrapper;

public class ResourceCollectionTest {
    private String[] values = new String[] { "a", "b", "c", "d", "e" };
    
    class TestPager implements Pager<String> {
        public List<String> getPage(List<String> ids) {
            List<String> results = new ArrayList<String>();
            
            for (String id : ids) {
                results.add(values[Integer.parseInt(id)]);
            }
            return results;
        }
    }

    @Test
    public void getFirst() {
        NodeWrapper xml = NodeWrapperFactory.instance.create("<search-results>" +
                "<page-size>2</page-size>" +
                "<ids type=\"array\">" +
                    "<items>0</items>" +
                    "<items>1</items>" +
                    "<items>2</items>" +
                    "<items>3</items>" +
                    "<items>4</items>" +                    
                "</ids>" +
                "</search-results>"
        );
       
        ResourceCollection<String> resourceCollection = new ResourceCollection<String>(new TestPager(), xml);

        int index = 0;
        int count = 0;
        for (String string : resourceCollection) {
            Assert.assertEquals(values[index], string);
            index++;
            count++;
        }
        Assert.assertEquals(values.length, count);
    }
}
