package com.braintreegateway;

import java.util.HashMap;
import java.util.Map;

import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.exceptions.ServerException;
import com.braintreegateway.graphql.inputs.CreateLocalPaymentContextInput;
import com.braintreegateway.util.GraphQLClient;

/**
 * Creates and manages local payment contexts.
 */
public class LocalPaymentContextGateway {

    private static final String CREATE_LOCAL_PAYMENT_CONTEXT =
        "mutation CreateLocalPaymentContext($input: CreateLocalPaymentContextInput!) { " +
        "  createLocalPaymentContext(input: $input) { " +
        "    paymentContext { " +
        "      id " +
        "      legacyId " +
        "      type " +
        "      paymentId " +
        "      approvalUrl " +
        "      merchantAccountId " +
        "      orderId " +
        "      createdAt " +
        "      transactedAt " +
        "      approvedAt " +
        "      amount { " +
        "        value " +
        "        currencyCode " +
        "      } " +
        "    } " +
        "  } " +
        "}";

    private static final String FIND_LOCAL_PAYMENT_CONTEXT =
        "query Node($id: ID!) { " +
        "  node(id: $id) { " +
        "    ... on LocalPaymentContext { " +
        "      id " +
        "      legacyId " +
        "      type " +
        "      amount { " +
        "        value " +
        "        currencyIsoCode " +
        "      } " +
        "      approvalUrl " +
        "      merchantAccountId " +
        "      transactedAt " +
        "      approvedAt " +
        "      createdAt " +
        "      updatedAt " +
        "      expiredAt " +
        "      paymentId " +
        "      orderId " +
        "    } " +
        "  } " +
        "}";

    private final GraphQLClient graphQLClient;

    public LocalPaymentContextGateway(GraphQLClient graphQLClient) {
        this.graphQLClient = graphQLClient;
    }

    /**
     * Creates a new local payment context.
     *
     * Example:
     *
     * <pre>
     * MonetaryAmountInput amount = new MonetaryAmountInput();
     * amount.setValue(new BigDecimal("10.00"));
     * amount.setCurrencyCode("EUR");
     *
     * CreateLocalPaymentContextInput input = CreateLocalPaymentContextInput
     *   .builder()
     *   .amount(amount)
     *   .type(LocalPaymentType.MBWAY)
     *   .merchantAccountId("eur_pwpp_multi_account_merchant_account")
     *   .build();
     *
     * Result&lt;LocalPaymentContext&gt; result = gateway.localPayment().create(input);
     *
     * if (result.isSuccess()) {
     *   LocalPaymentContext context = result.getTarget();
     *   String approvalUrl = context.getApprovalUrl();
     * }
     * </pre>
     *
     * @param input The input parameters for creating a local payment context.
     *
     * @return a {@link Result} object with LocalPaymentContext if successful, or errors otherwise.
     *
     * @throws ServerException If there is an unexpected error during the process.
     */
    public Result<LocalPaymentContext> create(CreateLocalPaymentContextInput input) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("input", input.toGraphQLVariables());

        try {
            Map<String, Object> response = graphQLClient.query(CREATE_LOCAL_PAYMENT_CONTEXT, variables);
            ValidationErrors errors = GraphQLClient.getErrors(response);
            if (errors != null) {
                return new Result<>(errors);
            }
            Map<String, Object> paymentContextData = getValue(response, "data.createLocalPaymentContext.paymentContext");
            LocalPaymentContext paymentContext = new LocalPaymentContext(paymentContextData);
            return new Result<>(paymentContext);
        } catch (Throwable e) {
            throw new ServerException(e.getMessage());
        }
    }

    /**
     * Finds a local payment context by ID.
     *
     * Example:
     *
     * <pre>
     * Result&lt;LocalPaymentContext&gt; result = gateway.localPayment().find("payment_context_id");
     *
     * if (result.isSuccess()) {
     *   LocalPaymentContext context = result.getTarget();
     * }
     * </pre>
     *
     * @param id The global GraphQL ID of the local payment context.
     *
     * @return a {@link Result} object with LocalPaymentContext if successful, or errors otherwise.
     *
     * @throws NotFoundException If the local payment context does not exist.
     * @throws ServerException If there is an unexpected error during the process.
     */
    public Result<LocalPaymentContext> find(String id) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("id", id);

        try {
            Map<String, Object> response = graphQLClient.query(FIND_LOCAL_PAYMENT_CONTEXT, variables);
            ValidationErrors errors = GraphQLClient.getErrors(response);
            if (errors != null) {
                return new Result<>(errors);
            }
            Map<String, Object> nodeData = getValue(response, "data.node");
            if (nodeData == null) {
                throw new NotFoundException("Local payment context not found");
            }
            LocalPaymentContext paymentContext = new LocalPaymentContext(nodeData);
            return new Result<>(paymentContext);
        } catch (NotFoundException e) {
            throw e;
        } catch (Throwable e) {
            throw new ServerException(e.getMessage());
        }
    }

    private static <T> T getValue(Map<String, Object> response, String key) {
        Map<String, Object> map = response;
        String[] keyParts = key.split("\\.");
        for (int k = 0; k < keyParts.length - 1; k++) {
            String subKey = keyParts[k];
            map = popValue(map, subKey);
        }
        String lastKey = keyParts[keyParts.length - 1];
        return popValue(map, lastKey);
    }

    private static <T> T popValue(Map<String, Object> response, String key) {
        if (!response.containsKey(key)) {
            throw new ServerException("Couldn't parse response");
        }
        return (T) response.get(key);
    }
}
