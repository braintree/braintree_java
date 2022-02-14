# Braintree Java library

The Braintree Java library provides integration access to the Braintree Gateway.

## Please Note
> **The Payment Card Industry (PCI) Council has [mandated](https://blog.pcisecuritystandards.org/migrating-from-ssl-and-early-tls) that early versions of TLS be retired from service.  All organizations that handle credit card information are required to comply with this standard. As part of this obligation, Braintree is updating its services to require TLS 1.2 for all HTTPS connections. Braintree will also require HTTP/1.1 for all connections. Please see our [technical documentation](https://github.com/paypal/tls-update) for more information.**

## Dependencies

* none

Java version >= 8 is required. The Braintree Java SDK is tested against Java versions 8 and 11.

## Versions

Braintree employs a deprecation policy for our SDKs. For more information on the statuses of an SDK check our [developer docs](https://developer.paypal.com/braintree/docs/reference/general/server-sdk-deprecation-policy).

| Major version number | Status      | Released   | Deprecated   | Unsupported  |
| -------------------- | ----------- | ---------- | ------------ | ------------ |
| 3.x.x                | Active      | June 2020  | TBA          | TBA          |
| 2.x.x                | Inactive    | April 2010 | June 2022    | June 2023    |

## Documentation

 * [Official documentation](https://developer.paypal.com/braintree/docs/start/hello-server/java)

Updating from an Inactive, Deprecated, or Unsupported version of this SDK? Check our [Migration Guide](https://developer.paypal.com/braintree/docs/reference/general/server-sdk-migration-guide/java) for tips.

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

        TransactionRequest request = new TransactionRequest()
            .amount(new BigDecimal("1000.00"))
            .paymentMethodNonce(nonceFromTheClient)
            .options()
                .submitForSettlement(true)
                .done();

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

## Development

See our [development notes](DEVELOPMENT.md).

## Open Source Attribution

A list of open source projects that help power Braintree can be found [here](https://www.braintreepayments.com/developers/open-source).

## License

See the LICENSE file.
