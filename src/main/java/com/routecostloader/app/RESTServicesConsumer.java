package com.routecostloader.app;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * This class can implement a GoF like proxy to redirect to the real method that will process the diff info (not only suggested positions)
 */
public class RESTServicesConsumer {
    private int HTTP_TIME_OUT = 30000;

    public RESTServicesConsumer(){}

    // Call the services in the provided URL
    // NB. CAn be improved as the GoF to divide the diff kind of data published by the API
    public String POST(String url, String json) throws IOException {
        StringBuilder resp = new StringBuilder();

        HttpParams my_httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(my_httpParams, HTTP_TIME_OUT);
        HttpConnectionParams.setSoTimeout(my_httpParams, 0);
        DefaultHttpClient httpClient = new DefaultHttpClient(my_httpParams);


        HttpPost postRequest = new HttpPost(url);
        //postRequest.addHeader("content-type", "application/x-www-form-urlencoded"); // MUST NOT BE INCLUDED

        System.out.println("\nJSON TO SEND: " + json);
        StringEntity input = new StringEntity(json);
        input.setContentType("application/json");
        postRequest.setEntity(input);

        try {
            HttpResponse response = httpClient.execute(postRequest);
            if (response.getStatusLine().getStatusCode() > 201) {
                throw new IOException("Failed : HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

            String output;
            System.out.println("Output from Server:");
            while ((output = br.readLine()) != null) {
                System.out.println(output);
                resp.append(output);
            }
            System.out.print("\n");
            httpClient.getConnectionManager().shutdown();
            return resp.toString();

        } catch (IOException e) {
            httpClient.getConnectionManager().shutdown();
            throw e;
        }





    }


}