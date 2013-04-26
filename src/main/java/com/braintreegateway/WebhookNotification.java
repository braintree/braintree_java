package com.braintreegateway;

import com.braintreegateway.util.EnumUtils;
import com.braintreegateway.util.NodeWrapper;

import java.util.Calendar;

public class WebhookNotification {
    public enum Kind {
        SUBSCRIPTION_CANCELED("subscription_canceled"),
        SUBSCRIPTION_CHARGED_SUCCESSFULLY("subscription_charged_successfully"),
        SUBSCRIPTION_CHARGED_UNSUCCESSFULLY("subscription_charged_unsuccessfully"),
        SUBSCRIPTION_EXPIRED("subscription_expired"),
        SUBSCRIPTION_TRIAL_ENDED("subscription_trial_ended"),
        SUBSCRIPTION_WENT_ACTIVE("subscription_went_active"),
        SUBSCRIPTION_WENT_PAST_DUE("subscription_went_past_due"),
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
    private Calendar timestamp;
	
	public WebhookNotification(NodeWrapper node) {
		this.kind = EnumUtils.findByName(Kind.class, node.findString("kind"));
		this.timestamp = node.findDateTime("timestamp");
		
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

    public Calendar getTimestamp() {
        return this.timestamp;
    }
}
