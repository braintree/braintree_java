package com.braintreegateway.util;

import com.braintreegateway.Configuration;
import com.braintreegateway.Request;
import com.braintreegateway.exceptions.*;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
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
import java.util.Iterator;
import java.util.zip.GZIPInputStream;

public class Http {

    enum RequestMethod {
        DELETE, GET, POST, PUT;
    }

    private String authorizationHeader;
    private String baseMerchantURL;
    private String[] certificateFilenames;
    private String version;

    public Http(String authorizationHeader, String baseMerchantURL, String[] certificateFilenames, String version) {
        this.authorizationHeader = authorizationHeader;
        this.baseMerchantURL = baseMerchantURL;
        this.certificateFilenames = Arrays.copyOf(certificateFilenames, certificateFilenames.length);
        this.version = version;
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
        try {
            HttpURLConnection connection = buildConnection(requestMethod, url);

            if (connection instanceof HttpsURLConnection) {
                ((HttpsURLConnection) connection).setSSLSocketFactory(getSSLSocketFactory());
            }

            if (postBody != null) {
                connection.getOutputStream().write(postBody.getBytes("UTF-8"));
                connection.getOutputStream().close();
            }
            throwExceptionIfErrorStatusCode(connection.getResponseCode(), null);
            if (requestMethod.equals(RequestMethod.DELETE)) {
                return null;
            }
            InputStream responseStream = connection.getResponseCode() == 422 ? connection.getErrorStream() : connection.getInputStream();

            if ("gzip".equalsIgnoreCase(connection.getContentEncoding())) {
                responseStream = new GZIPInputStream(responseStream);
            }
            String xml = StringUtils.inputStreamToString(responseStream);

            responseStream.close();
            return NodeWrapperFactory.instance.create(xml);
        } catch (IOException e) {
            throw new UnexpectedException(e.getMessage(), e);
        }
    }

    private SSLSocketFactory getSSLSocketFactory() {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);

            for (String certificateFilename : certificateFilenames) {
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                InputStream certStream = Http.class.getClassLoader().getResourceAsStream(certificateFilename);

                Collection c = cf.generateCertificates(certStream);
                Iterator i = c.iterator();
                while (i.hasNext()) {
                    Certificate cert = (Certificate)i.next();
                    if (cert instanceof X509Certificate) {
                      X509Certificate x509cert = (X509Certificate) cert;
                      Principal principal = x509cert.getSubjectDN();
                      String subject = principal.getName();
                      keyStore.setCertificateEntry(subject, cert);
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
        URL url = new URL(baseMerchantURL + urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(requestMethod.toString());
        connection.addRequestProperty("Accept", "application/xml");
        connection.addRequestProperty("User-Agent", "Braintree Java " + version);
        connection.addRequestProperty("X-ApiVersion", Configuration.apiVersion());
        connection.addRequestProperty("Authorization", authorizationHeader);
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
}
