package com.braintreegateway.unittest;

import com.braintreegateway.TransactionLineItem;
import com.braintreegateway.util.SimpleNodeWrapper;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TransactionLineItemTest {
    @Test
    public void testDeserializesTransactionLineItems() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<transaction_line_item>\n"
            + "  <commodity-code>9SAASSD8724</commodity-code>\n"
            + "  <discount-amount>1.02</discount-amount>\n"
            + "  <image-url>https://google.com/image.png</image-url>\n"
            + "  <kind>DEBIT</kind>\n"
            + "  <name>Name #1</name>\n"
            + "  <product-code>23434</product-code>\n"
            + "  <quantity>1.2322</quantity>\n"
            + "  <tax-amount>4.55</tax-amount>\n"
            + "  <total-amount>45.15</total-amount>\n"
            + "  <unit-amount>1.23</unit-amount>\n"
            + "  <unit-of-measure>gallon</unit-of-measure>\n"
            + "  <upc-code>3878935708DA</upc-code>\n"
            + "  <upc-type>UPC-A</upc-type>\n"
            + "  <url>https://example.com/products/23434</url>\n"
            + "</transaction_line_item>\n";

        SimpleNodeWrapper transactionLineItemNode = SimpleNodeWrapper.parse(xml);
        TransactionLineItem item = new TransactionLineItem(transactionLineItemNode);
        assertEquals("9SAASSD8724", item.getCommodityCode());
        assertEquals(new BigDecimal("1.02"), item.getDiscountAmount());
        assertEquals("https://google.com/image.png", item.getImageUrl());
        assertEquals(TransactionLineItem.Kind.DEBIT, item.getKind());
        assertEquals("Name #1", item.getName());
        assertEquals("23434", item.getProductCode());
        assertEquals(new BigDecimal("1.2322"), item.getQuantity());
        assertEquals(new BigDecimal("4.55"), item.getTaxAmount());
        assertEquals(new BigDecimal("45.15"), item.getTotalAmount());
        assertEquals(new BigDecimal("1.23"), item.getUnitAmount());
        assertEquals("gallon", item.getUnitOfMeasure());
        assertEquals("3878935708DA", item.getUpcCode());
        assertEquals("UPC-A", item.getUpcType());
        assertEquals("https://example.com/products/23434", item.getUrl());
    }
}
