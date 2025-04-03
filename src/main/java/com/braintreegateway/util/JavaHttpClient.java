package com.braintreegateway.util;

import com.braintreegateway.Configuration;
import com.braintreegateway.exceptions.TimeoutException;
import com.braintreegateway.exceptions.UnexpectedException;
import com.fasterxml.jackson.jr.ob.JSON;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import static com.braintreegateway.util.HttpClient.Payload.MULTIPART_FORM_DATA;

public class JavaHttpClient implements HttpClient {
    public static final String LINE_FEED = "\r\n";

    private final Configuration configuration;
    private volatile SSLSocketFactory sslSocketFactory;

    public JavaHttpClient(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public HttpResponse request(RequestMethod requestMethod, String url, Payload payload) {
        HttpURLConnection connection = null;
        String boundary = "boundary" + System.currentTimeMillis();

        try {
            connection = buildConnection(requestMethod, url, payload, boundary);
        } catch (IOException e) {
            throw new UnexpectedException(e.getMessage(), e);
        }

        try {
            if (connection instanceof HttpsURLConnection) {
                ((HttpsURLConnection) connection).setSSLSocketFactory(getSSLSocketFactory());
            }

            String body = payload.getBody();
            if (body != null) {
                OutputStream outputStream = null;
                try {
                    outputStream = connection.getOutputStream();
                    PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"), true);

                    File file = payload.getFile();
                    if (file == null) {
                        outputStream.write(body.getBytes("UTF-8"));
                    } else {
                        Map<String, Object> map = JSON.std.mapFrom(body);
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

            return new HttpResponse(connection.getResponseCode(), connection.getResponseMessage(), readResponseBody(connection));
        } catch (SocketTimeoutException e) {
            throw new TimeoutException(e.getMessage(), e);
        } catch (IOException e) {
            throw new UnexpectedException(e.getMessage(), e);
        } finally {
            connection.disconnect();
        }
    }

    private HttpURLConnection buildConnection(RequestMethod requestMethod,
                                              String urlString,
                                              Payload payload,
                                              String boundary) throws IOException {
        URL url = new URL(urlString);
        int connectTimeout = configuration.getConnectTimeout();
        HttpURLConnection connection;
        if (configuration.usesProxy()) {
            connection = (HttpURLConnection) url.openConnection(configuration.getProxy());
        } else {
            connection = (HttpURLConnection) url.openConnection();
        }
        connection.setRequestMethod(requestMethod.toString());
        for (Map.Entry<String, String> entry : payload.getHeaders().entrySet()) {
            connection.addRequestProperty(entry.getKey(), entry.getValue());
        }

        String contentType = payload.getContentType();
        if (contentType.equals(MULTIPART_FORM_DATA)) {
            contentType += "; boundary=" + boundary;
        }

        connection.addRequestProperty("Content-Type", contentType);

        connection.setDoOutput(true);
        connection.setReadTimeout(configuration.getTimeout());

        if (connectTimeout > 0) {
            connection.setConnectTimeout(connectTimeout);
        }

        return connection;
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
                            new Object[] {e.getMessage()});
                        throw new UnexpectedException(e.getMessage(), e);
                    }
                }
            }
        }
        return sslSocketFactory;
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

    private String readResponseBody(HttpURLConnection connection) throws IOException {
        if (connection.getResponseCode() >= 400 && connection.getResponseCode() != 422) {
            return null;
        }

        String response = null;
        InputStream responseStream = null;
        try {
            InputStream errorStream = connection.getErrorStream();
            responseStream = errorStream != null ? errorStream : connection.getInputStream();
            if ("gzip".equalsIgnoreCase(connection.getContentEncoding())) {
                responseStream = new GZIPInputStream(responseStream);
            }

            response = StringUtils.inputStreamToString(responseStream);
            if (response == null || response.trim().equals("")) {
                response = null;
            }
        } finally {
            if (responseStream != null) {
                responseStream.close();
            }
        }

        return response;
    }
}
