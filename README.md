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
                amount(new BigDecimal("100.00")).
                creditCard().
                    number("4111111111111111").
                    expirationDate("05/2012").
                    done();
        
            Transaction transaction = gateway.transaction().sale(request).getTarget();
            System.out.println("Transaction ID: " + transaction.getId());
            System.out.println("Status: " + transaction.getStatus());
        }
    }

## License

See the LICENSE file.



