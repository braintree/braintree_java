package com.braintreegateway.util;

import java.io.*;
import java.security.*;
import javax.net.ssl.*;

public class HttpsTest {
  public static void main(String[] args) {
    String ksName = "keystore.jks";
    char ksPass[] = "password".toCharArray();
    char ctPass[] = "password".toCharArray();

    try {
      KeyStore ks = KeyStore.getInstance("JKS");
      ks.load(new FileInputStream(ksName), ksPass);

      KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
      kmf.init(ks, ctPass);

      SSLContext sc = SSLContext.getInstance("TLS");
      sc.init(kmf.getKeyManagers(), null, null);

      SSLServerSocketFactory ssf = sc.getServerSocketFactory();
      SSLServerSocket s = (SSLServerSocket) ssf.createServerSocket(19443);
      System.out.println("Server started.");

      SSLSocket c = (SSLSocket) s.accept();
      BufferedWriter w = new BufferedWriter(new OutputStreamWriter(c.getOutputStream()));
      BufferedReader r = new BufferedReader(new InputStreamReader(c.getInputStream()));

      w.write("HTTP/1.0 200 OK\nContent-Type: text/html\n\n<html><body>Hello world!</body></html>\n");
      w.flush();
      w.close();

      r.close();
      c.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
