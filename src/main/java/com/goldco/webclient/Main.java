/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.goldco.webclient;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.util.ssl.SslContextFactory;

/**
 *
 * @author gerry
 */
public class Main {

    private static final int HTTPS_PORT = 8443;
    private static final int HTTP_PORT = 8080;
    // private static final String KEYSTORE_PATH = EmbeddedJettyMain.class.getResource("keystore").toExternalForm();
    private static final String KEYSTORE_PATH = Main.class.getResource("keystore256").toExternalForm();

    private static final String KEYSTORE_MANAGER_PASSWORD = "secret";
    private static final String KEYSTORE_PASSWORD = "secret";
    private static final String KEYSTORE_TYPE = "jks";
    private static final String TRUSTSTORE_PATH = Main.class.getResource("truststore").toExternalForm();
    private static final String TRUSTSTORE_PASSWORD = "secret";

    public enum ServerMode {

        http, https
    };

    public static void main(String[] args) throws Exception {

        ServerMode mode = (args.length == 0 ? ServerMode.http : ServerMode.https);

        client(mode);
    }

    public static void client(ServerMode mode) {
        if (mode == ServerMode.http) {
            System.out.println("HTTP mode");

            try {

                HttpClient httpClient = new HttpClient();
                httpClient.setFollowRedirects(false);
                URI uri = new URI("http://localhost:8080/example");
                httpClient.start();
                ContentResponse response = httpClient.GET(uri);
                System.out.println(response.getContentAsString());
                // httpClient.start();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            System.out.println("HTTS mode");
            try {

                SslContextFactory sslContextFactory = new SslContextFactory();
                // Defining keystore path and passwords
                sslContextFactory.setKeyStorePath(KEYSTORE_PATH);
                sslContextFactory.setKeyStorePassword(KEYSTORE_PASSWORD);
                sslContextFactory.setKeyManagerPassword(KEYSTORE_MANAGER_PASSWORD);
                sslContextFactory.setKeyStoreType("jks");
                sslContextFactory.setTrustStorePath(TRUSTSTORE_PATH);
                sslContextFactory.setTrustStorePassword(TRUSTSTORE_PASSWORD);
                sslContextFactory.setTrustStoreType("jks");// Instantiate HttpClient with the SslContextFactory
                HttpClient httpClient = new HttpClient(sslContextFactory);

                httpClient.setFollowRedirects(true);
                httpClient.start();
                URI uri = new URI("https://localhost:8443/example");
                ContentResponse response = httpClient.GET(uri);
                System.out.println("Status=" + response.getStatus());
                System.out.println(response.getContentAsString());

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
