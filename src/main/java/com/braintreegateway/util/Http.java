package com.braintreegateway.util;

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
import java.security.Principal;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
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

import com.braintreegateway.Configuration;
import com.braintreegateway.Request;
import com.braintreegateway.exceptions.AuthenticationException;
import com.braintreegateway.exceptions.AuthorizationException;
import com.braintreegateway.exceptions.DownForMaintenanceException;
import com.braintreegateway.exceptions.NotFoundException;
import com.braintreegateway.exceptions.ServerException;
import com.braintreegateway.exceptions.TimeoutException;
import com.braintreegateway.exceptions.TooManyRequestsException;
import com.braintreegateway.exceptions.UnexpectedException;
import com.braintreegateway.exceptions.UpgradeRequiredException;
import com.braintreegateway.org.apache.commons.codec.binary.Base64;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

public class Http {
    public static final String LINE_FEED = "\r\n";
    private volatile SSLSocketFactory sslSocketFactory;

    enum RequestMethod {
        DELETE, GET, POST, PUT;
    }

    private Configuration configuration;

    public Http(Configuration configuration) {
        this.configuration = configuration;
    }

    public NodeWrapper delete(String url) {
        return httpRequest(RequestMethod.DELETE, url);
    }

    public NodeWrapper get(String url) {
        return httpRequest(RequestMethod.GET, url);
    }

    public NodeWrapper post(String url) {
        return httpRequest(RequestMethod.POST, url, null, null);
    }

    public NodeWrapper post(String url, Request request) {
        return httpRequest(RequestMethod.POST, url, request.toXML(), null);
    }

    public NodeWrapper post(String url, String request) {
        return httpRequest(RequestMethod.POST, url, request, null);
    }

    public NodeWrapper postMultipart(String url, String request, File file) {
        return httpRequest(RequestMethod.POST, url, request, file);
    }

    public NodeWrapper put(String url) {
        return httpRequest(RequestMethod.PUT, url, null, null);
    }

    public NodeWrapper put(String url, Request request) {
        return httpRequest(RequestMethod.PUT, url, request.toXML(), null);
    }

    private NodeWrapper httpRequest(RequestMethod requestMethod, String url) {
        return httpRequest(requestMethod, url, null, null);
    }

    private NodeWrapper httpRequest(RequestMethod requestMethod, String url, String postBody, File file) {
        HttpURLConnection connection = null;
        NodeWrapper nodeWrapper = null;
        String boundary = "boundary" + System.currentTimeMillis();
        String contentType = file == null ? "application/xml" : "multipart/form-data; boundary=" + boundary;

        try {
            connection = buildConnection(requestMethod, url, contentType);

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
                        ObjectMapper mapper = new ObjectMapper();
                        JsonNode jsonNode = mapper.readTree(postBody);
                        Iterator<Map.Entry<String, JsonNode>> fields = jsonNode.fields();
                        while (fields.hasNext()) {
                            Map.Entry<String, JsonNode> entry = fields.next();
                            addFormField(entry.getKey(), entry.getValue().asText(), writer, boundary);
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

            throwExceptionIfErrorStatusCode(connection.getResponseCode(), null);

            InputStream responseStream = null;
            try {
                responseStream = connection.getResponseCode() == 422 ? connection.getErrorStream() : connection.getInputStream();

                if ("gzip".equalsIgnoreCase(connection.getContentEncoding())) {
                    responseStream = new GZIPInputStream(responseStream);
                }

                String xml = StringUtils.inputStreamToString(responseStream);

                logger.log(Level.INFO, "[Braintree] [{0}]] {1} {2}", new Object[] { getCurrentTime(), requestMethod.toString(), url });
                logger.log(Level.FINE, "[Braintree] [{0}] {1} {2} {3}", new Object[] { getCurrentTime(), requestMethod.toString(), url, connection.getResponseCode() });

                if (xml != null) {
                    logger.log(Level.FINE, formatSanitizeBodyForLog(xml));
                }

                if (xml == null || xml.trim().equals("")) {
                    return null;
                }

                nodeWrapper = NodeWrapperFactory.instance.create(xml);
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
            if (connection != null) {
                connection.disconnect();
            }
        }

        return nodeWrapper;
    }

    private void addFormField(String key, String value, PrintWriter writer, String boundary) {
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"" + key + "\"").append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.append(value).append(LINE_FEED);
        writer.flush();
    }

    private void addFilePart(String fieldName, File uploadFile, PrintWriter writer, OutputStream outputStream, String boundary)
      throws IOException {
        String filename = uploadFile.getName();

        writer.append("--" + boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"" + fieldName + "\"; filename=\"" + filename + "\"").append(LINE_FEED);
        writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(filename)).append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.flush();

        FileInputStream inputStream = new FileInputStream(uploadFile);
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.flush();
        inputStream.close();

        writer.append(LINE_FEED);
        writer.flush();
    }

    private void finish(PrintWriter writer, String boundary) {
        writer.append("--" + boundary + "--").append(LINE_FEED);
        writer.append(LINE_FEED).flush();
        writer.close();
    }

    private String formatSanitizeBodyForLog(String body) {
        if (body == null) {
            return body;
        }

        Pattern regex = Pattern.compile("(^)", Pattern.MULTILINE);
        Matcher regexMatcher = regex.matcher(body);
        if (regexMatcher.find()) {
            body = regexMatcher.replaceAll("[Braintree] $1");
        }

        regex = Pattern.compile("<number>(.{6}).+?(.{4})</number>");
        regexMatcher = regex.matcher(body);
        if (regexMatcher.find()) {
            body = regexMatcher.replaceAll("<number>$1******$2</number>");
        }

        body = body.replaceAll("<cvv>.+?</cvv>", "<cvv>***</cvv>");

        return body;
    }

    private String getCurrentTime() {
        return new SimpleDateFormat("d/MMM/yyyy HH:mm:ss Z").format(new Date());
    }

    private SSLSocketFactory getSSLSocketFactory() {
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
                        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                        tmf.init(keyStore);

                        SSLContext sslContext = null;
                        try {
                            // Use TLS v1.2 explicitly for Java 1.6 or Java 7 JVMs that support it but do not turn it on by
                            // default
                            sslContext = SSLContext.getInstance("TLSv1.2");
                        } catch (NoSuchAlgorithmException e) {
                            sslContext = SSLContext.getInstance("TLS");
                        }
                        sslContext.init((KeyManager[]) kmf.getKeyManagers(), tmf.getTrustManagers(), SecureRandom.getInstance("SHA1PRNG"));

                        sslSocketFactory = sslContext.getSocketFactory();
                    } catch (Exception e) {
                        Logger logger = configuration.getLogger();
                        logger.log(Level.SEVERE, "SSL Verification failed. Error message: {0}", new Object[] { e.getMessage() });
                        throw new UnexpectedException(e.getMessage(), e);
                    }
                }
            }
        }
        return sslSocketFactory;
    }

    private HttpURLConnection buildConnection(RequestMethod requestMethod, String urlString, String contentType) throws java.io.IOException {
        URL url = new URL(configuration.getBaseURL() + urlString);
        int connectTimeout = configuration.getConnectTimeout();
        HttpURLConnection connection;
        if (configuration.usesProxy()) {
            connection = (HttpURLConnection) url.openConnection(configuration.getProxy());
        } else {
            connection = (HttpURLConnection) url.openConnection();
        }
        connection.setRequestMethod(requestMethod.toString());
        connection.addRequestProperty("Accept", "application/xml");
        connection.addRequestProperty("User-Agent", "Braintree Java " + Configuration.VERSION);
        connection.addRequestProperty("X-ApiVersion", Configuration.apiVersion());
        connection.addRequestProperty("Authorization", authorizationHeader());
        connection.addRequestProperty("Accept-Encoding", "gzip");
        connection.setRequestProperty("Content-Type", contentType);

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
            case 426:
                throw new UpgradeRequiredException();
            case 429:
                throw new TooManyRequestsException();
            case 500:
                throw new ServerException();
            case 503:
                throw new DownForMaintenanceException();
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
