package com.braintreegateway;

import java.math.BigDecimal;

public class TransactionServiceFeeRequest extends Request {
	private TransactionRequest parent;
	private BigDecimal amount;
	private String merchantAccountId;

	public TransactionServiceFeeRequest(TransactionRequest parent) {
		this.parent = parent;
	}

	public TransactionServiceFeeRequest amount(BigDecimal amount) {
		this.amount = amount;
		return this;
	}

	public TransactionServiceFeeRequest merchantAccountId(String merchantAccountId) {
		this.merchantAccountId = merchantAccountId;
		return this;
	}

	public TransactionRequest done() {
		return parent;
	}

	public String toXML() {
		return new RequestBuilder("serviceFee").
	            addElement("amount", amount).
	            addElement("merchantAccountId", merchantAccountId).toXML();
	}
}
