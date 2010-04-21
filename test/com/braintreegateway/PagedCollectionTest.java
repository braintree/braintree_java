package com.braintreegateway;

import junit.framework.Assert;

import org.junit.Test;

import com.braintreegateway.util.NodeWrapper;

public class PagedCollectionTest {

    @Test
    public void getFirst() {
        NodeWrapper xml = new NodeWrapper("<credit-card-transactions type=\"collection\">" +
                "<current-page-number type=\"integer\">1</current-page-number>" +
                "<page-size type=\"integer\">50</page-size>" +
                "<total-items type=\"integer\">1</total-items>" +
                "<transaction><id>abc</id><billing /><credit-card /><customer /><shipping /></transaction>" +
                "<transaction><id>def</id><billing /><credit-card /><customer /><shipping /></transaction>" +
                "<transaction><id>ghi</id><billing /><credit-card /><customer /><shipping /></transaction>" +
                "</credit-card-transactions>"
        );
       
        PagedCollection<Transaction> pagedCollection = new PagedCollection<Transaction>(null, xml, Transaction.class);
        Assert.assertEquals("abc", pagedCollection.getFirst().getId());
    }
}
