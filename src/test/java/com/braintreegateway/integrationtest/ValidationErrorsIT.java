package com.braintreegateway.integrationtest;

import com.braintreegateway.ValidationError;
import com.braintreegateway.ValidationErrorCode;
import com.braintreegateway.ValidationErrors;
import com.braintreegateway.util.NodeWrapperFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ValidationErrorsIT {

    @Test
    public void onField() {
        ValidationErrors errors = new ValidationErrors();
        errors.addError(new ValidationError("country_name", ValidationErrorCode.ADDRESS_COUNTRY_NAME_IS_NOT_ACCEPTED, "invalid country"));
        assertEquals(ValidationErrorCode.ADDRESS_COUNTRY_NAME_IS_NOT_ACCEPTED, errors.onField("countryName").get(0).getCode());
        assertEquals("invalid country", errors.onField("countryName").get(0).getMessage());
    }

    @Test
    public void onFieldAlsoWorksWithUnderscores() {
        ValidationErrors errors = new ValidationErrors();
        errors.addError(new ValidationError("country_name", ValidationErrorCode.ADDRESS_COUNTRY_NAME_IS_NOT_ACCEPTED, "invalid country"));
        assertEquals(ValidationErrorCode.ADDRESS_COUNTRY_NAME_IS_NOT_ACCEPTED, errors.onField("country_name").get(0).getCode());
        assertEquals("invalid country", errors.onField("country_name").get(0).getMessage());
    }

    @Test
    public void nonExistingField() {
        ValidationErrors errors = new ValidationErrors();
        assertTrue(errors.onField("foo").isEmpty());
    }

    @Test
    public void forObject() {
        ValidationErrors addressErrors = new ValidationErrors();
        addressErrors.addError(new ValidationError("country_name", ValidationErrorCode.ADDRESS_COUNTRY_NAME_IS_NOT_ACCEPTED, "invalid country"));

        ValidationErrors errors = new ValidationErrors();
        errors.addErrors("address", addressErrors);
        assertEquals(ValidationErrorCode.ADDRESS_COUNTRY_NAME_IS_NOT_ACCEPTED, errors.forObject("address").onField("countryName").get(0).getCode());
        assertEquals("invalid country", errors.forObject("address").onField("country_name").get(0).getMessage());
    }

    @Test
    public void forObjectOnNonExistingObject() {
        ValidationErrors errors = new ValidationErrors();
        assertEquals(0, errors.forObject("invalid").size());
    }

    @Test
    public void forObjectAlsoWorksWithUnderscores() {
        ValidationErrors addressErrors = new ValidationErrors();
        addressErrors.addError(new ValidationError("name", ValidationErrorCode.ADDRESS_FIRST_NAME_IS_TOO_LONG, "invalid name"));

        ValidationErrors errors = new ValidationErrors();
        errors.addErrors("billing-address", addressErrors);
        assertEquals(ValidationErrorCode.ADDRESS_FIRST_NAME_IS_TOO_LONG, errors.forObject("billing_address").onField("name").get(0).getCode());
    }

    @Test
    public void size() {
        ValidationErrors errors = new ValidationErrors();
        errors.addError(new ValidationError("countryName", ValidationErrorCode.ADDRESS_COUNTRY_NAME_IS_NOT_ACCEPTED, "invalid country"));
        errors.addError(new ValidationError("anotherField", ValidationErrorCode.ADDRESS_COMPANY_IS_TOO_LONG, "another message"));
        assertEquals(2, errors.size());
    }

    @Test
    public void deepSize() {
        ValidationErrors addressErrors = new ValidationErrors();
        addressErrors.addError(new ValidationError("countryName", ValidationErrorCode.ADDRESS_COUNTRY_NAME_IS_NOT_ACCEPTED, "invalid country"));
        addressErrors.addError(new ValidationError("anotherField", ValidationErrorCode.ADDRESS_COMPANY_IS_TOO_LONG, "another message"));

        ValidationErrors errors = new ValidationErrors();
        errors.addError(new ValidationError("someField", ValidationErrorCode.ADDRESS_EXTENDED_ADDRESS_IS_TOO_LONG, "some message"));
        errors.addErrors("address", addressErrors);

        assertEquals(3, errors.deepSize());
        assertEquals(1, errors.size());

        assertEquals(2, errors.forObject("address").deepSize());
        assertEquals(2, errors.forObject("address").size());
    }

    @Test
    public void getAllValidationErrors() {
        ValidationErrors addressErrors = new ValidationErrors();
        addressErrors.addError(new ValidationError("countryName", ValidationErrorCode.ADDRESS_COUNTRY_NAME_IS_NOT_ACCEPTED, "invalid country"));
        addressErrors.addError(new ValidationError("anotherField", ValidationErrorCode.ADDRESS_COMPANY_IS_TOO_LONG, "another message"));

        ValidationErrors errors = new ValidationErrors();
        errors.addError(new ValidationError("someField", ValidationErrorCode.ADDRESS_FIRST_NAME_IS_TOO_LONG, "some message"));
        errors.addErrors("address", addressErrors);

        assertEquals(1, errors.getAllValidationErrors().size());
        assertEquals(ValidationErrorCode.ADDRESS_FIRST_NAME_IS_TOO_LONG, errors.getAllValidationErrors().get(0).getCode());
    }

    @Test
    public void getAllDeepValidationErrors() {
        ValidationErrors addressErrors = new ValidationErrors();
        addressErrors.addError(new ValidationError("countryName", ValidationErrorCode.ADDRESS_COUNTRY_NAME_IS_NOT_ACCEPTED, "1"));
        addressErrors.addError(new ValidationError("anotherField", ValidationErrorCode.ADDRESS_COMPANY_IS_TOO_LONG, "2"));

        ValidationErrors errors = new ValidationErrors();
        errors.addError(new ValidationError("someField", ValidationErrorCode.ADDRESS_FIRST_NAME_IS_TOO_LONG, "3"));
        errors.addErrors("address", addressErrors);

        assertEquals(3, errors.getAllDeepValidationErrors().size());

        List<ValidationError> validationErrors = new ArrayList<ValidationError>(errors.getAllDeepValidationErrors());
        Collections.sort(validationErrors, new Comparator<ValidationError>() {
            public int compare(ValidationError left, ValidationError right) {
                return left.getCode().compareTo(right.getCode());
            }
        });

        assertEquals(ValidationErrorCode.ADDRESS_COMPANY_IS_TOO_LONG, validationErrors.get(0).getCode());
        assertEquals(ValidationErrorCode.ADDRESS_COUNTRY_NAME_IS_NOT_ACCEPTED, validationErrors.get(1).getCode());
        assertEquals(ValidationErrorCode.ADDRESS_FIRST_NAME_IS_TOO_LONG, validationErrors.get(2).getCode());
    }


    @Test
    public void parseSimpleValidationErrors() {
        StringBuilder builder = new StringBuilder();
        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        builder.append("<api-error-response>");
        builder.append("  <errors>");
        builder.append("    <address>");
        builder.append("      <errors type=\"array\">");
        builder.append("        <error>");
        builder.append("          <code>91803</code>");
        builder.append("          <message>Country name is not an accepted country.</message>");
        builder.append("          <attribute type=\"symbol\">country_name</attribute>");
        builder.append("        </error>");
        builder.append("      </errors>");
        builder.append("    </address>");
        builder.append("    <errors type=\"array\"/>");
        builder.append("  </errors>");
        builder.append("</api-error-response>");

        ValidationErrors errors = new ValidationErrors(NodeWrapperFactory.instance.create(builder.toString()));
        assertEquals(1, errors.deepSize());
        assertEquals(ValidationErrorCode.ADDRESS_COUNTRY_NAME_IS_NOT_ACCEPTED, errors.forObject("address").onField("country_name").get(0).getCode());
        assertEquals(ValidationErrorCode.ADDRESS_COUNTRY_NAME_IS_NOT_ACCEPTED, errors.forObject("address").onField("countryName").get(0).getCode());
    }

    @Test
    public void parseMulitpleValidationErrorsOnOneObject() {
        StringBuilder builder = new StringBuilder();
        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        builder.append("<api-error-response>");
        builder.append("  <errors>");
        builder.append("    <address>");
        builder.append("      <errors type=\"array\">");
        builder.append("        <error>");
        builder.append("          <code>91803</code>");
        builder.append("          <message>Country name is not an accepted country.</message>");
        builder.append("          <attribute type=\"symbol\">country_name</attribute>");
        builder.append("        </error>");
        builder.append("        <error>");
        builder.append("          <code>81812</code>");
        builder.append("          <message>Street address is too long.</message>");
        builder.append("          <attribute type=\"symbol\">street_address</attribute>");
        builder.append("        </error>");
        builder.append("      </errors>");
        builder.append("    </address>");
        builder.append("    <errors type=\"array\"/>");
        builder.append("  </errors>");
        builder.append("</api-error-response>");

        ValidationErrors errors = new ValidationErrors(NodeWrapperFactory.instance.create(builder.toString()));
        assertEquals(2, errors.deepSize());
        assertEquals(ValidationErrorCode.ADDRESS_COUNTRY_NAME_IS_NOT_ACCEPTED, errors.forObject("address").onField("countryName").get(0).getCode());
        assertEquals(ValidationErrorCode.ADDRESS_STREET_ADDRESS_IS_TOO_LONG, errors.forObject("address").onField("streetAddress").get(0).getCode());
    }

    @Test
    public void parseMulitpleValidationErrorsOnOneField() {
        StringBuilder builder = new StringBuilder();
        builder.append("<api-error-response>");
        builder.append("  <errors>");
        builder.append("    <transaction>");
        builder.append("      <errors type=\"array\">");
        builder.append("        <error>");
        builder.append("          <code>91516</code>");
        builder.append("          <message>Cannot provide both payment_method_token and customer_id unless the payment_method belongs to the customer.</message>");
        builder.append("          <attribute type=\"symbol\">base</attribute>");
        builder.append("        </error>");
        builder.append("        <error>");
        builder.append("          <code>91515</code>");
        builder.append("          <message>Cannot provide both payment_method_token and credit_card attributes.</message>");
        builder.append("          <attribute type=\"symbol\">base</attribute>");
        builder.append("        </error>");
        builder.append("      </errors>");
        builder.append("    </transaction>");
        builder.append("    <errors type=\"array\"/>");
        builder.append("  </errors>");
        builder.append("</api-error-response>");

        ValidationErrors errors = new ValidationErrors(NodeWrapperFactory.instance.create(builder.toString()));
        assertEquals(2, errors.deepSize());
        assertEquals(2, errors.forObject("transaction").onField("base").size());
    }

    @Test
    public void parseValidationErrorOnNestedObject() {
        StringBuilder builder = new StringBuilder();
        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        builder.append("<api-error-response>");
        builder.append("  <errors>");
        builder.append("    <errors type=\"array\"/>");
        builder.append("    <credit-card>");
        builder.append("      <billing-address>");
        builder.append("        <errors type=\"array\">");
        builder.append("          <error>");
        builder.append("            <code>91803</code>");
        builder.append("            <message>Country name is not an accepted country.</message>");
        builder.append("            <attribute type=\"symbol\">country_name</attribute>");
        builder.append("          </error>");
        builder.append("        </errors>");
        builder.append("      </billing-address>");
        builder.append("      <errors type=\"array\"/>");
        builder.append("    </credit-card>");
        builder.append("  </errors>");
        builder.append("</api-error-response>");

        ValidationErrors errors = new ValidationErrors(NodeWrapperFactory.instance.create(builder.toString()));
        assertEquals(1, errors.deepSize());
        assertEquals(ValidationErrorCode.ADDRESS_COUNTRY_NAME_IS_NOT_ACCEPTED, errors.forObject("creditCard").forObject("billingAddress").onField("countryName").get(0).getCode());
    }

    @Test
    public void parseValidationErrorsAtMultipleLevels() {
        StringBuilder builder = new StringBuilder();
        builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        builder.append("<api-error-response>");
        builder.append("  <errors>");
        builder.append("    <customer>");
        builder.append("      <errors type=\"array\">");
        builder.append("        <error>");
        builder.append("          <code>81608</code>");
        builder.append("          <message>First name is too long.</message>");
        builder.append("          <attribute type=\"symbol\">first_name</attribute>");
        builder.append("        </error>");
        builder.append("      </errors>");
        builder.append("      <credit-card>");
        builder.append("        <billing-address>");
        builder.append("          <errors type=\"array\">");
        builder.append("            <error>");
        builder.append("              <code>91803</code>");
        builder.append("              <message>Country name is not an accepted country.</message>");
        builder.append("              <attribute type=\"symbol\">country_name</attribute>");
        builder.append("            </error>");
        builder.append("          </errors>");
        builder.append("        </billing-address>");
        builder.append("        <errors type=\"array\">");
        builder.append("          <error>");
        builder.append("            <code>81715</code>");
        builder.append("            <message>Credit card number is invalid.</message>");
        builder.append("            <attribute type=\"symbol\">number</attribute>");
        builder.append("          </error>");
        builder.append("        </errors>");
        builder.append("      </credit-card>");
        builder.append("    </customer>");
        builder.append("    <errors type=\"array\"/>");
        builder.append("  </errors>");
        builder.append("</api-error-response>");


        ValidationErrors errors = new ValidationErrors(NodeWrapperFactory.instance.create(builder.toString()));

        assertEquals(3, errors.deepSize());
        assertEquals(0, errors.size());

        assertEquals(3, errors.forObject("customer").deepSize());
        assertEquals(1, errors.forObject("customer").size());
        assertEquals(ValidationErrorCode.CUSTOMER_FIRST_NAME_IS_TOO_LONG, errors.forObject("customer").onField("firstName").get(0).getCode());

        assertEquals(2, errors.forObject("customer").forObject("creditCard").deepSize());
        assertEquals(1, errors.forObject("customer").forObject("creditCard").size());
        assertEquals(ValidationErrorCode.CREDIT_CARD_NUMBER_IS_INVALID, errors.forObject("customer").forObject("creditCard").onField("number").get(0).getCode());

        assertEquals(1, errors.forObject("customer").forObject("creditCard").forObject("billingAddress").deepSize());
        assertEquals(1, errors.forObject("customer").forObject("creditCard").forObject("billingAddress").size());
        assertEquals(ValidationErrorCode.ADDRESS_COUNTRY_NAME_IS_NOT_ACCEPTED, errors.forObject("customer").forObject("creditCard").forObject("billingAddress").onField("countryName").get(0).getCode());
    }
}
