package com.routecostloader.app;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * This class can implement a GoF like proxy to redirect to the real method that will process the diff info (not only suggested positions)
 */
public class RESTServicesConsumer {

    public RESTServicesConsumer(){}

    // Call the services in the provided URL
    // NB. CAn be improved as the GoF to divide the diff kind of data published by the API
    public String POST(String url, String json) throws IOException {
        StringBuilder resp = new StringBuilder();
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost postRequest = new HttpPost(url);

        StringEntity input = new StringEntity("json");
        input.setContentType("application/json");
        postRequest.setEntity(input);

        HttpResponse response = httpClient.execute(postRequest);

        if (response.getStatusLine().getStatusCode() != 201) {
            throw new IOException("Failed : HTTP error code : "
                    + response.getStatusLine().getStatusCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

        String output;
        System.out.println("Output from Server .... \n");
        while ((output = br.readLine()) != null) {
            System.out.println(output);
            resp.append(output);
        }

        return resp.toString();
    }


}