package com.braintreegateway;

import com.braintreegateway.util.NodeWrapper;
import java.util.ArrayList;
import java.util.List;

public class GrantedPaymentInstrumentUpdate {
    private String grantOwnerMerchantId;
    private String grantRecipientMerchantId;
    private String paymentMethodNonce;
    private String token;
    private List<String> updatedFields;

    public GrantedPaymentInstrumentUpdate(NodeWrapper node) {

        this.grantOwnerMerchantId = node.findString("grant-owner-merchant-id");
        this.grantRecipientMerchantId = node.findString("grant-recipient-merchant-id");
        this.paymentMethodNonce = node.findString("payment-method-nonce/nonce");
        this.token = node.findString("token");

        this.updatedFields = new ArrayList<String>();
        for (NodeWrapper field : node.findAll("updated-fields/item")) {
            updatedFields.add(field.findString("."));
        }
    }

    public String getGrantOwnerMerchantId() {
        return grantOwnerMerchantId;
    }

    public String getGrantRecipientMerchantId() {
        return grantRecipientMerchantId;
    }

    public String getPaymentMethodNonce(){
        return paymentMethodNonce;
    }

    public String getToken(){
        return token;
    }

    public List<String> getUpdatedFields(){
        return updatedFields;
    }
 }
