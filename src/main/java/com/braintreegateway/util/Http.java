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
import com.braintreegateway.exceptions.TimeoutException;
import com.braintreegateway.exceptions.TooManyRequestsException;
import com.braintreegateway.exceptions.UnexpectedException;
import com.braintreegateway.exceptions.UpgradeRequiredException;
import com.braintreegateway.org.apache.commons.codec.binary.Base64;
import com.fasterxml.jackson.jr.ob.JSON;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

public class Http {
    public static final String LINE_FEED = "\r\n";
    private static final Pattern NUMBER_PATTERN = Pattern.compile("<number>(.{6}).+?(.{4})</number>");
    private static final Pattern START_GROUP_PATTERN = Pattern.compile("(^)", Pattern.MULTILINE);
    private static final Pattern CVV_PATTERN = Pattern.compile("<cvv>.+?</cvv>");
    
    
    private volatile SSLSocketFactory sslSocketFactory;

    public enum RequestMethod {
        DELETE, GET, POST, PUT;
    }

    private Configuration configuration;

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

    protected String httpDo(RequestMethod requestMethod,
                            String url,
                            String postBody,
                            File file,
                            HttpURLConnection connection,
                            Map<String, String> headers,
                            String boundary) {
        String response = null;

        try {
            Logger logger = configuration.getLogger();
            if (postBody != null) {
                logger.log(Level.FINE, formatSanitizeBodyForLog(postBody));
            }

            if (connection instanceof HttpsURLConnection) {
                ((HttpsURLConnection) connection).setSSLSocketFactory(getSSLSocketFactory());
            }

            if (postBody != null) {
                OutputStream outputStream = null;
                try {
                    outputStream = connection.getOutputStream();
                    PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"), true);

                    if (file == null) {
                        outputStream.write(postBody.getBytes("UTF-8"));
                    } else {
                        Map<String, Object> map = JSON.std.mapFrom(postBody);
                        Iterator<?> it = map.entrySet().iterator();
                        while (it.hasNext()) {
                            Map.Entry pair = (Map.Entry) it.next();
                            addFormField((String) pair.getKey(), (String) pair.getValue(), writer, boundary);
                        }
                        addFilePart("file", file, writer, outputStream, boundary);
                        finish(writer, boundary);
                    }
                } finally {
                    if (outputStream != null) {
                        outputStream.close();
                    }
                }
            }

            throwExceptionIfErrorStatusCode(connection.getResponseCode(), connection.getResponseMessage());

            InputStream responseStream = null;
            try {
                responseStream =
                    connection.getResponseCode() == 422 ? connection.getErrorStream() : connection.getInputStream();

                if ("gzip".equalsIgnoreCase(connection.getContentEncoding())) {
                    responseStream = new GZIPInputStream(responseStream);
                }

                response = StringUtils.inputStreamToString(responseStream);

                logger.log(Level.INFO,
                           "[Braintree] [{0}]] {1} {2}",
                           new Object[]{getCurrentTime(), requestMethod.toString(), url});
                logger.log(Level.FINE,
                           "[Braintree] [{0}] {1} {2} {3}",
                           new Object[]{getCurrentTime(), requestMethod.toString(), url, connection.getResponseCode()});

                if (response != null) {
                    logger.log(Level.FINE, formatSanitizeBodyForLog(response));
                }

                if (response == null || response.trim().equals("")) {
                    return null;
                }
            } finally {
                if (responseStream != null) {
                    responseStream.close();
                }
            }
        } catch (SocketTimeoutException e) {
            throw new TimeoutException(e.getMessage(), e);
        } catch (IOException e) {
            throw new UnexpectedException(e.getMessage(), e);
        } finally {
            connection.disconnect();
        }

        return response;
    }

    protected Map<String, String> constructHeaders(String acceptType, String contentType) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Accept", acceptType);
        headers.put("User-Agent", "Braintree Java " + Configuration.VERSION);
        headers.put("X-ApiVersion", Configuration.apiVersion());
        headers.put("Authorization", authorizationHeader());
        headers.put("Accept-Encoding", "gzip");
        headers.put("Content-Type", contentType);

        return headers;
    }

    private NodeWrapper xmlHttpRequest(RequestMethod requestMethod, String url, String postBody, File file) {
        HttpURLConnection connection = null;
        String boundary = "boundary" + System.currentTimeMillis();
        String contentType = file == null ? "application/xml" : "multipart/form-data; boundary=" + boundary;

        Map<String, String> headers = constructHeaders("application/xml", contentType);

        try {
            connection = buildConnection(requestMethod, configuration.getBaseURL() + url, headers);
        } catch (IOException e) {
            throw new UnexpectedException(e.getMessage(), e);
        }

        String response = httpDo(requestMethod, url, postBody, file, connection, headers, boundary);

        if (response != null) {
            return NodeWrapperFactory.instance.create(response);
        }

        return null;
    }

    private void addFormField(String key, String value, PrintWriter writer, String boundary) {
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"" + key + "\"").append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.append(value).append(LINE_FEED);
        writer.flush();
    }

    private void addFilePart(String fieldName,
                             File uploadFile,
                             PrintWriter writer,
                             OutputStream outputStream,
                             String boundary)
        throws IOException {
        String filename = uploadFile.getName();

        writer.append("--" + boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"" + fieldName + "\"; filename=\"" + filename + "\"")
            .append(LINE_FEED);
        writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(filename)).append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.flush();

        FileInputStream inputStream = new FileInputStream(uploadFile);
        try {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
        } finally {
            inputStream.close();
        }

        writer.append(LINE_FEED);
        writer.flush();
    }

    private void finish(PrintWriter writer, String boundary) {
        writer.append("--" + boundary + "--").append(LINE_FEED);
        writer.append(LINE_FEED).flush();
        writer.close();
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

        body = CVV_PATTERN.matcher(body).replaceAll("<cvv>***</cvv>");

        return body;
    }

    protected String getCurrentTime() {
        return new SimpleDateFormat("d/MMM/yyyy HH:mm:ss Z").format(new Date());
    }

    protected SSLSocketFactory getSSLSocketFactory() {
        if (sslSocketFactory == null) {
            synchronized (this) {
                if (sslSocketFactory == null) {
                    try {
                        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                        keyStore.load(null);

                        for (String certificateFilename : configuration.getEnvironment().certificateFilenames) {
                            CertificateFactory cf = CertificateFactory.getInstance("X.509");
                            InputStream certStream = null;
                            try {
                                certStream = Http.class.getClassLoader().getResourceAsStream(certificateFilename);

                                Collection<? extends Certificate> coll = cf.generateCertificates(certStream);
                                for (Certificate cert : coll) {
                                    if (cert instanceof X509Certificate) {
                                        X509Certificate x509cert = (X509Certificate) cert;
                                        Principal principal = x509cert.getSubjectDN();
                                        String subject = principal.getName();
                                        keyStore.setCertificateEntry(subject, cert);
                                    }
                                }
                            } finally {
                                if (certStream != null) {
                                    certStream.close();
                                }
                            }
                        }

                        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                        kmf.init(keyStore, null);
                        TrustManagerFactory tmf =
                            TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                        tmf.init(keyStore);

                        SSLContext sslContext = null;
                        try {
                            // Use TLS v1.2 explicitly for Java 1.6 or Java 7 JVMs that support it but do not turn it on by
                            // default
                            sslContext = SSLContext.getInstance("TLSv1.2");
                        } catch (NoSuchAlgorithmException e) {
                            sslContext = SSLContext.getInstance("TLS");
                        }
                        sslContext.init((KeyManager[]) kmf.getKeyManagers(),
                                        tmf.getTrustManagers(),
                                        SecureRandom.getInstance("SHA1PRNG"));

                        sslSocketFactory = sslContext.getSocketFactory();
                    } catch (Exception e) {
                        Logger logger = configuration.getLogger();
                        logger.log(Level.SEVERE,
                                   "SSL Verification failed. Error message: {0}",
                                   new Object[]{e.getMessage()});
                        throw new UnexpectedException(e.getMessage(), e);
                    }
                }
            }
        }
        return sslSocketFactory;
    }

    protected HttpURLConnection buildConnection(RequestMethod requestMethod,
                                                String urlString,
                                                Map<String, String> headers) throws java.io.IOException {
        URL url = new URL(urlString);
        int connectTimeout = configuration.getConnectTimeout();
        HttpURLConnection connection;
        if (configuration.usesProxy()) {
            connection = (HttpURLConnection) url.openConnection(configuration.getProxy());
        } else {
            connection = (HttpURLConnection) url.openConnection();
        }
        connection.setRequestMethod(requestMethod.toString());
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            connection.addRequestProperty(entry.getKey(), entry.getValue());
        }

        connection.setDoOutput(true);
        connection.setReadTimeout(configuration.getTimeout());

        if (connectTimeout > 0) {
            connection.setConnectTimeout(connectTimeout);
        }

        return connection;
    }

    public static void throwExceptionIfErrorStatusCode(int statusCode, String message) {
        String decodedMessage = null;
        if (message != null) {
            try {
                decodedMessage = URLDecoder.decode(message, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                Logger logger = Logger.getLogger("Braintree");
                logger.log(Level.FINEST, e.getMessage(), e.getStackTrace());
            }
        }

        if (isErrorCode(statusCode)) {
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
