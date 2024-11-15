package com.braintreegateway.util;

import java.io.File;
import java.util.Map;

public interface HttpClient {

    HttpResponse request(RequestMethod requestMethod, String url, Payload payload);

    enum RequestMethod {
        DELETE, GET, POST, PUT;
    }

    class Payload {

        public static final String APPLICATION_JSON = "application/json";
        public static final String APPLICATION_XML = "application/xml";
        public static final String MULTIPART_FORM_DATA = "multipart/form-data";

        private final Map<String, String> headers;
        private final String contentType;
        private final String body;
        private final File file;

        private Payload(Map<String, String> headers, String contentType, String body, File file) {
            this.headers = headers;
            this.contentType = contentType;
            this.body = body;
            this.file = file;
        }

        public static Payload json(Map<String, String> headers, String content) {
            return new Payload(headers, APPLICATION_JSON, content, null);
        }

        public static Payload xml(Map<String, String> headers, String content) {
            return new Payload(headers, APPLICATION_XML, content, null);
        }

        public static Payload multipart(Map<String, String> headers, String content, File file) {
            return new Payload(headers, MULTIPART_FORM_DATA, content, file);
        }

        public Map<String, String> getHeaders() {
            return headers;
        }

        public String getContentType() {
            return contentType;
        }

        public String getBody() {
            return body;
        }

        public File getFile() {
            return file;
        }
    }

    class HttpResponse {
        private final int responseCode;
        private final String responseMessage;
        private final String responseBody;

        public HttpResponse(int responseCode, String responseMessage, String responseBody) {
            this.responseCode = responseCode;
            this.responseMessage = responseMessage;
            this.responseBody = responseBody;
        }

        public int getResponseCode() {
            return responseCode;
        }

        public String getResponseMessage() {
            return responseMessage;
        }

        public String getResponseBody() {
            return responseBody;
        }
    }
}
