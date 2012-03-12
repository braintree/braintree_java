package com.braintreegateway;

import org.junit.Assert;
import org.junit.Test;

import com.braintreegateway.util.NodeWrapper;
import com.braintreegateway.util.NodeWrapperFactory;

public class WebhookNotificationTest {

	@Test
	public void createNotificationWithUnrecognizedKind() {
		String xml = "<notification><kind>" + "bad_kind" + "</kind></notification>";
		NodeWrapper node = NodeWrapperFactory.instance.create(xml);
		
		WebhookNotification notification = new WebhookNotification(node);
		Assert.assertEquals(WebhookNotification.Kind.UNRECOGNIZED, notification.getKind());
	}
}
