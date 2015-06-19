package com.braintreegateway.util;

import com.braintreegateway.Configuration;
import com.braintreegateway.Request;
import com.braintreegateway.exceptions.*;
import com.braintreegateway.org.apache.commons.codec.binary.Base64;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.security.KeyStore;
import java.security.Principal;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collection;
import java.util.zip.GZIPInputStream;

public class Http {

    enum RequestMethod {
        DELETE, GET, POST, PUT;
    }

    private Configuration configuration;

    public Http(Configuration configuration) {
        this.configuration = configuration;
    }

    public void delete(String url) {
        httpRequest(RequestMethod.DELETE, url);
    }

    public NodeWrapper get(String url) {
        return httpRequest(RequestMethod.GET, url);
    }

    public NodeWrapper post(String url) {
        return httpRequest(RequestMethod.POST, url, null);
    }

    public NodeWrapper post(String url, Request request) {
        return httpRequest(RequestMethod.POST, url, request.toXML());
    }

    public NodeWrapper put(String url) {
        return httpRequest(RequestMethod.PUT, url, null);
    }

    public NodeWrapper put(String url, Request request) {
        return httpRequest(RequestMethod.PUT, url, request.toXML());
    }

    private NodeWrapper httpRequest(RequestMethod requestMethod, String url) {
        return httpRequest(requestMethod, url, null);
    }

    private NodeWrapper httpRequest(RequestMethod requestMethod, String url, String postBody) {
        HttpURLConnection connection = null;
        NodeWrapper nodeWrapper = null;

        try {
            connection = buildConnection(requestMethod, url);

            if (connection instanceof HttpsURLConnection) {
                ((HttpsURLConnection) connection).setSSLSocketFactory(getSSLSocketFactory());
            }

            if (postBody != null) {
                OutputStream outputStream = null;
                try {
                    outputStream = connection.getOutputStream();
                    outputStream.write(postBody.getBytes("UTF-8"));
                } finally {
                    if (outputStream != null) {
                        outputStream.close();
                    }
                }
            }

            throwExceptionIfErrorStatusCode(connection.getResponseCode(), null);
            if (requestMethod.equals(RequestMethod.DELETE)) {
                return null;
            }

            InputStream responseStream = null;
            try {
                responseStream = connection.getResponseCode() == 422 ? connection.getErrorStream() : connection.getInputStream();

                if ("gzip".equalsIgnoreCase(connection.getContentEncoding())) {
                    responseStream = new GZIPInputStream(responseStream);
                }

                String xml = StringUtils.inputStreamToString(responseStream);
                nodeWrapper = NodeWrapperFactory.instance.create(xml);
            } finally {
                if (responseStream != null) {
                    responseStream.close();
                }
            }
        } catch (IOException e) {
            throw new UnexpectedException(e.getMessage(), e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return nodeWrapper;
    }

    private SSLSocketFactory getSSLSocketFactory() {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);

            for (String certificateFilename : configuration.environment.certificateFilenames) {
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

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init((KeyManager[]) kmf.getKeyManagers(), tmf.getTrustManagers(), SecureRandom.getInstance("SHA1PRNG"));

            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new UnexpectedException(e.getMessage(), e);
        }
    }

    private HttpURLConnection buildConnection(RequestMethod requestMethod, String urlString) throws java.io.IOException {
        URL url = new URL(configuration.getBaseURL() + urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(requestMethod.toString());
        connection.addRequestProperty("Accept", "application/xml");
        connection.addRequestProperty("User-Agent", "Braintree Java " + Configuration.VERSION);
        connection.addRequestProperty("X-ApiVersion", Configuration.apiVersion());
        connection.addRequestProperty("Authorization", authorizationHeader());
        connection.addRequestProperty("Accept-Encoding", "gzip");
        connection.addRequestProperty("Content-Type", "application/xml");
        connection.setDoOutput(true);
        connection.setReadTimeout(60000);
        return connection;
    }

    public static void throwExceptionIfErrorStatusCode(int statusCode, String message) {
        String decodedMessage = null;
        if (message != null) {
            try {
                decodedMessage = URLDecoder.decode(message, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
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
        return "Basic " + Base64.encodeBase64String((configuration.publicKey + ":" + configuration.privateKey).getBytes()).trim();
    }
}
