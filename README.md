# Braintree Java Client Library

The Braintree library provides integration access to the Braintree Gateway.

## Dependencies

* none

## Quick Start Example

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

            Result<Transaction> result = gateway.transaction().sale(request);

            if (result.isSuccess()) {
                Transaction transaction = result.getTarget();
                if (Transaction.Status.AUTHORIZED.equals(transaction.getStatus())) {
                    System.out.println("Success!: " + transaction.getId());
                } else {
                    System.out.println("Error processing transaction:");
                    System.out.println("  Status: " + transaction.getStatus());
                    System.out.println("  Code: " + transaction.getProcessorResponseCode());
                    System.out.println("  Code: " + transaction.getProcessorResponseText());
                }
            } else {
                for (ValidationError error : result.getErrors().getAllDeepValidationErrors()) {
                   System.out.println("Attribute: " + error.getAttribute());
                   System.out.println("  Code: " + error.getCode());
                   System.out.println("  Message: " + error.getMessage());
                }
            }
        }
    }

## License

See the LICENSE file.
