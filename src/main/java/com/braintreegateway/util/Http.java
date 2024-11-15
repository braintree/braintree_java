package com.braintreegateway.util;

import com.braintreegateway.Configuration;
import com.braintreegateway.Request;
import com.braintreegateway.exceptions.AuthenticationException;
import com.braintreegateway.exceptions.AuthorizationException;
import com.braintreegateway.exceptions.GatewayTimeoutException;
import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.exceptions.RequestTimeoutException;
import com.braintreegateway.exceptions.ServerException;
import com.braintreegateway.exceptions.ServiceUnavailableException;
import com.braintreegateway.exceptions.TooManyRequestsException;
import com.braintreegateway.exceptions.UnexpectedException;
import com.braintreegateway.exceptions.UpgradeRequiredException;
import com.braintreegateway.org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.braintreegateway.util.HttpClient.HttpResponse;
import static com.braintreegateway.util.HttpClient.Payload;
import static com.braintreegateway.util.HttpClient.RequestMethod;

public class Http {
    private static final Pattern NUMBER_PATTERN = Pattern.compile("<number>(.{6}).+?(.{4})</number>");
    private static final Pattern START_GROUP_PATTERN = Pattern.compile("(^)", Pattern.MULTILINE);
    private static final Pattern CVV_PATTERN = Pattern.compile("<cvv>.+?</cvv>");
    private static final Pattern ENCRYPTED_CARD_DATA_PATTERN = Pattern.compile("<encryptedCardData>.+?</encryptedCardData>");
    private final Configuration configuration;

    public Http(Configuration configuration) {
        this.configuration = configuration;
    }

    public NodeWrapper delete(String url) {
        return xmlHttpRequest(RequestMethod.DELETE, url);
    }

    public NodeWrapper get(String url) {
        return xmlHttpRequest(RequestMethod.GET, url);
    }

    public NodeWrapper post(String url) {
        return xmlHttpRequest(RequestMethod.POST, url, null, null);
    }

    public NodeWrapper post(String url, Request request) {
        return xmlHttpRequest(RequestMethod.POST, url, request.toXML(), null);
    }

    public NodeWrapper post(String url, String request) {
        return xmlHttpRequest(RequestMethod.POST, url, request, null);
    }

    public NodeWrapper postMultipart(String url, String request, File file) {
        return xmlHttpRequest(RequestMethod.POST, url, request, file);
    }

    public NodeWrapper put(String url) {
        return xmlHttpRequest(RequestMethod.PUT, url, null, null);
    }

    public NodeWrapper put(String url, Request request) {
        return xmlHttpRequest(RequestMethod.PUT, url, request.toXML(), null);
    }

    private NodeWrapper xmlHttpRequest(RequestMethod requestMethod, String url) {
        return xmlHttpRequest(requestMethod, url, null, null);
    }

    protected String httpDo(RequestMethod requestMethod, String url, String logUrl, Payload payload) {
        Logger logger = configuration.getLogger();

        if (payload.getBody() != null && logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE, formatSanitizeBodyForLog(payload.getBody()));
        }

        HttpClient httpClient = configuration.getHttpClient();
        HttpResponse httpResponse = httpClient.request(requestMethod, url, payload);
        throwExceptionIfErrorStatusCode(httpResponse.getResponseCode(), httpResponse.getResponseMessage());

        if (logger.isLoggable(Level.INFO)) {
            logger.log(Level.INFO,
                "[Braintree] [{0}]] {1} {2}",
                new Object[] {getCurrentTime(), requestMethod.toString(), logUrl});
        }

        if (logger.isLoggable(Level.FINE)) {
            logger.log(Level.FINE,
                "[Braintree] [{0}] {1} {2} {3}",
                new Object[] {getCurrentTime(), requestMethod.toString(), logUrl, httpResponse.getResponseCode()});

            if (httpResponse.getResponseBody() != null) {
                logger.log(Level.FINE, formatSanitizeBodyForLog(httpResponse.getResponseBody()));
            }
        }

        return httpResponse.getResponseBody();
    }

    protected Map<String, String> constructHeaders(String acceptType) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Accept", acceptType);
        headers.put("User-Agent", "Braintree Java " + Configuration.VERSION);
        headers.put("X-ApiVersion", Configuration.apiVersion());
        headers.put("Authorization", authorizationHeader());
        headers.put("Accept-Encoding", "gzip");

        return headers;
    }

    private NodeWrapper xmlHttpRequest(RequestMethod requestMethod, String url, String postBody, File file) {
        Map<String, String> headers = constructHeaders("application/xml");

        Payload payload = file == null ? Payload.xml(headers, postBody) : Payload.multipart(headers, postBody, file);
        String response = httpDo(requestMethod, configuration.getBaseURL() + url, url, payload);

        if (response != null) {
            return NodeWrapperFactory.instance.create(response);
        }

        return null;
    }

    protected String formatSanitizeBodyForLog(String body) {
        if (body == null) {
            return body;
        }

        Matcher regexMatcher = START_GROUP_PATTERN.matcher(body);
        if (regexMatcher.find()) {
            body = regexMatcher.replaceAll("[Braintree] $1");
        }

        regexMatcher = NUMBER_PATTERN.matcher(body);
        if (regexMatcher.find()) {
            body = regexMatcher.replaceAll("<number>$1******$2</number>");
        }

        body = ENCRYPTED_CARD_DATA_PATTERN.matcher(body).replaceAll("<encryptedCardData>***</encryptedCardData>");
        body = CVV_PATTERN.matcher(body).replaceAll("<cvv>***</cvv>");

        return body;
    }

    protected String getCurrentTime() {
        return new SimpleDateFormat("d/MMM/yyyy HH:mm:ss Z").format(new Date());
    }

    public static void throwExceptionIfErrorStatusCode(int statusCode, String message) {
        if (isErrorCode(statusCode)) {
            String decodedMessage = null;
            if (message != null) {
                try {
                    decodedMessage = URLDecoder.decode(message, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    Logger logger = Logger.getLogger("Braintree");
                    logger.log(Level.FINEST, e.getMessage(), e.getStackTrace());
                }
            }

            switch (statusCode) {
                case 401:
                    throw new AuthenticationException();
                case 403:
                    throw new AuthorizationException(decodedMessage);
                case 404:
                    throw new NotFoundException();
                case 408:
                    throw new RequestTimeoutException();
                case 426:
                    throw new UpgradeRequiredException();
                case 429:
                    throw new TooManyRequestsException();
                case 500:
                    throw new ServerException();
                case 503:
                    throw new ServiceUnavailableException();
                case 504:
                    throw new GatewayTimeoutException();
                default:
                    throw new UnexpectedException("Unexpected HTTP_RESPONSE " + statusCode);

            }
        }
    }

    private static boolean isErrorCode(int responseCode) {
        return responseCode != 200 && responseCode != 201 && responseCode != 422;
    }

    public String authorizationHeader() {
        if (configuration.isAccessToken()) {
            return "Bearer " + configuration.getAccessToken();
        }
        String credentials;
        if (configuration.isClientCredentials()) {
            credentials = configuration.getClientId() + ":" + configuration.getClientSecret();
        } else {
            credentials = configuration.getPublicKey() + ":" + configuration.getPrivateKey();
        }
        return "Basic " + Base64.encodeBase64String(credentials.getBytes()).trim();
    }
}
