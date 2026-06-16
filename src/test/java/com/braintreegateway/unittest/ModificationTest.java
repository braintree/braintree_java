package com.braintreegateway.unittest;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.braintreegateway.Modification;
import com.braintreegateway.util.SimpleNodeWrapper;

public class ModificationTest {

    @Test
    public void parsesAllFields() {
        SimpleNodeWrapper node = SimpleNodeWrapper.parse(
                "<modification>" +
                "<amount>10.00</amount>" +
                "<current-billing-cycle>2</current-billing-cycle>" +
                "<description>a description</description>" +
                "<id>a-modification-id</id>" +
                "<kind>discount</kind>" +
                "<quantity>3</quantity>" +
                "<name>a name</name>" +
                "<never-expires type=\"boolean\">true</never-expires>" +
                "<number-of-billing-cycles>5</number-of-billing-cycles>" +
                "<plan-id>a-plan-id</plan-id>" +
                "</modification>");

        Modification modification = new Modification(node);

        assertAll("modification fields",
                () -> assertEquals(new BigDecimal("10.00"), modification.getAmount()),
                () -> assertEquals(Integer.valueOf(2), modification.getCurrentBillingCycle()),
                () -> assertEquals("a description", modification.getDescription()),
                () -> assertEquals("a-modification-id", modification.getId()),
                () -> assertEquals("discount", modification.getKind()),
                () -> assertEquals(Integer.valueOf(3), modification.getQuantity()),
                () -> assertEquals("a name", modification.getName()),
                () -> assertTrue(modification.neverExpires()),
                () -> assertEquals(Integer.valueOf(5), modification.getNumberOfBillingCycles()),
                () -> assertEquals("a-plan-id", modification.getPlanId()));
    }
}
