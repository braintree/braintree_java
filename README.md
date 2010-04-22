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

## License

See the LICENSE file.
