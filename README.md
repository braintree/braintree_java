# Braintree Java Client Library

The Braintree library provides integration access to the Braintree Gateway.

## Dependencies

* none

## Quick Start Example

````java
import java.math.BigDecimal;
import com.braintreegateway.*;

public class BraintreeExample {
    public static void main(String[] args) {
        BraintreeGateway gateway = new BraintreeGateway(
            Environment.SANDBOX,
            "the_merchant_id",
            "the_public_key",
            "the_private_key"
        );

        TransactionRequest request = new TransactionRequest().
            amount(new BigDecimal("1000.00")).
            creditCard().
                number("4111111111111111").
                expirationDate("05/2009").
                done();

        Result<Transaction> result = gateway.transaction().sale(request);

        if (result.isSuccess()) {
            Transaction transaction = result.getTarget();
            System.out.println("Success!: " + transaction.getId());
        } else if (result.getTransaction() != null) {
            Transaction transaction = result.getTransaction();
            System.out.println("Error processing transaction:");
            System.out.println("  Status: " + transaction.getStatus());
            System.out.println("  Code: " + transaction.getProcessorResponseCode());
            System.out.println("  Text: " + transaction.getProcessorResponseText());
        } else {
            for (ValidationError error : result.getErrors().getAllDeepValidationErrors()) {
               System.out.println("Attribute: " + error.getAttribute());
               System.out.println("  Code: " + error.getCode());
               System.out.println("  Message: " + error.getMessage());
            }
        }
    }
}
````

## Documentation

 * [Official documentation](https://developers.braintreepayments.com/java/sdk/server/overview)

## Maven

  With Maven installed, this package can be built simply by running this command:

     mvn package

  The resulting jar file will be produced in the directory named "target".

### In repositories:

     Maven Central, which should be enabled by default. No additional repositories are required.

### In dependencies

    <dependency>
      <groupId>com.braintreepayments.gateway</groupId>
      <artifactId>braintree-java</artifactId>
      <version>PUT VERSION NUMBER HERE</version>
    </dependency>

## Open Source Attribution

A list of open source projects that help power Braintree can be found [here](https://www.braintreepayments.com/developers/open-source).

## License

See the LICENSE file.
