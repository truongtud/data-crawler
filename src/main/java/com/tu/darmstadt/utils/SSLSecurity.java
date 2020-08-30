package com.tu.darmstadt.utils;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

/** @see https://nanashi07.blogspot.de/2014/06/enable-ssl-connection-for-jsoup.html */
public class SSLSecurity {

  public static void enableSSLSocket() throws KeyManagementException, NoSuchAlgorithmException {
    HttpsURLConnection.setDefaultHostnameVerifier(
        new HostnameVerifier() {

          public boolean verify(String hostname, SSLSession session) {
            return true;
          }
        });

    SSLContext context = SSLContext.getInstance("SSL");
    context.init(
        null,
        new X509TrustManager[] {
          new X509TrustManager() {

            public void checkClientTrusted(X509Certificate[] chain, String authType) {}

            public void checkServerTrusted(X509Certificate[] chain, String authType) {}

            public X509Certificate[] getAcceptedIssuers() {
              return new X509Certificate[0];
            }
          }
        },
        new SecureRandom());
    HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
  }
}
