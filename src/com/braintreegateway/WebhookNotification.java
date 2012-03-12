package com.braintreegateway;

import com.braintreegateway.util.EnumUtils;
import com.braintreegateway.util.NodeWrapper;

public class WebhookNotification {
    public enum Kind {
        SUBSCRIPTION_PAST_DUE("subscription_past_due"), 
        UNRECOGNIZED("unrecognized");

        private final String name;

        Kind(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
    
	private Subscription subscription;
	private Kind kind;
	
	public WebhookNotification(NodeWrapper node) {
		this.kind = EnumUtils.findByName(Kind.class, node.findString("kind"));
		
		if (node.findFirst("subject/subscription") != null) {
			this.subscription = new Subscription(node.findFirst("subject/subscription"));
		}
	}

	public Kind getKind() {
		return this.kind;
	}

	public Subscription getSubscription() {
		return this.subscription;
	}
}
