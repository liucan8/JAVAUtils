package com.lc.utils;


import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.net.ConnectException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class HttpRequestUtil {


    private static Logger logger = LoggerFactory.getLogger(HttpRequestUtil.class);

    /**
     * 创建httpClient
     *
     * @return
     */
    private static CloseableHttpClient createSSLInsecureClient() {
        SSLContext sslcontext = createSSLContext();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new HostnameVerifier() {

            @Override
            public boolean verify(String paramString, SSLSession paramSSLSession) {
                return true;
            }
        });

        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        return httpclient;
    }

    /**
     * 获取初始化sslContext
     *
     * @return
     */
    private static SSLContext createSSLContext() {
        SSLContext sslcontext = null;
        try {
            sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new SecureRandom());
        } catch (NoSuchAlgorithmException e) {
            logger.error("HttpRequestUtil.createSSLContext: " + e.getMessage());
        } catch (KeyManagementException e) {
            logger.error("HttpRequestUtil.createSSLContext: " + e.getMessage());
        }
        return sslcontext;
    }


    public static String doPost(String url, String json) throws Exception {
        CloseableHttpClient client = null;
        try {
            if (StringUtils.startsWith(url, "https")) {
                client = createSSLInsecureClient();
            } else {
                client = HttpClients.createDefault();
            }

            HttpPost post = new HttpPost(url);

            StringEntity s = new StringEntity(json, "UTF-8");
            s.setContentEncoding("UTF-8");
            s.setContentType("application/json");
            post.setEntity(s);
            CloseableHttpResponse response = client.execute(post);
            try {
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    return EntityUtils.toString(response.getEntity(), "UTF-8");
                }
            } finally {
                response.close();
            }
        } catch (Exception e) {
            if (e instanceof HttpHostConnectException || e.getCause() instanceof ConnectException) {
                throw new ConnectException();
            }
            logger.error("HttpRequestUtil.doPost: " + e.getMessage());
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (Exception e) {
                }
            }
        }
        return "";
    }


    public static String doGet(String url) throws Exception {
        CloseableHttpClient client = null;
        try {
            if (StringUtils.startsWith(url, "https")) {
                client = createSSLInsecureClient();
            } else {
                client = HttpClients.createDefault();
            }
            HttpGet httpget = new HttpGet(url);
            CloseableHttpResponse response = client.execute(httpget);
            try {
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    return EntityUtils.toString(response.getEntity(), "UTF-8");
                }
            } finally {
                response.close();
            }
        } catch (Exception e) {
            if (e instanceof HttpHostConnectException || e.getCause() instanceof ConnectException) {
                throw e;
            }
            logger.error("HttpRequestUtil.doGet: " + e.getMessage());
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (Exception e) {
                    // this exception can be ignored
                }
            }
        }
        return "";
    }


    /**
     * 自定义静态私有类
     */
    private static class TrustAnyTrustManager implements X509TrustManager {

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }

}
