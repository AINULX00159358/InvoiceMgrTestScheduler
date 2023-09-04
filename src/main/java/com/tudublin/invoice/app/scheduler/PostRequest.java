package com.tudublin.invoice.app.scheduler;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.*;

public class PostRequest {

    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    private static final String ENDPOINT = "https://invoicemgr.northeurope-1.eventgrid.azure.net/api/events?overload=cloudEvent&api-version=2018-01-01";
    private static final String CID = "uWKbOUKZ/qoFGnHFiEugJCAqzRcr8R+pWRo7szJibTE=";
    private static final String CONTENT_TYPE = "application/cloudevents-batch+json;charset=utf-8";

    public static void post(String id){
        try {
                final byte[] sampleData = Cloudevent.createJson().getBytes();
                final HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                        .uri(new URI(ENDPOINT))
                        .POST(HttpRequest.BodyPublishers.ofByteArray(sampleData));
                requestBuilder.setHeader("Content-Type", CONTENT_TYPE);
                requestBuilder.setHeader("aeg-sas-key", CID);
                final HttpRequest request = requestBuilder.build();
                System.out.println("Posting data for id "+ id);
                final HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println(Thread.currentThread().getName() + " ID ="+id+"   RESPONSE "+ response.statusCode()  );
            }catch (Exception exception){
                exception.printStackTrace();
        }
    }

    public static Runnable createRunnable(String id) {
        return ()-> PostRequest.post(id);
    }

}
