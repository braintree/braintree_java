package com.braintreegateway.util;

public abstract class Logger {
	private boolean enabled = false;	
	final static Logger DUMMY_LOGGER = new Logger() {
		@Override
		public void doLogging(String httpHeaders, String requestMethod, String url, String requestBody, String response) {}
	};
	/**
	 * 
	 * @param requestMethod - PUT, GET, POST, DELETE
	 * @param url - requested URL
	 * @param requestBody - post body or null, if no data
	 * @param response - response data from gateway or null, if no data
	 */
	public abstract void doLogging(String httpHeaders, String requestMethod, String url, String requestBody, String response);
	
	final void httpLogging(String httpHeaders, String requestMethod, String url, String requestBody, String response) {
		if (isEnabled()) {
			doLogging(nullToString(httpHeaders), requestMethod, url,nullToString(requestBody), nullToString(response));
		}
	}		
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean isEnabled) {
		enabled = isEnabled;
	}

	private String nullToString(String nullString) {
		return nullString==null?"no data":nullString;
	}
	
}
