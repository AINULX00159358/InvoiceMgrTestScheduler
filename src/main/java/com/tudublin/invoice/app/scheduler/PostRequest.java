package com.tudublin.invoice.app.scheduler;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class PostRequest {

    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    private static final List<String> postTypesSupported = Arrays.asList("JSON", "CLOUDEVENT"); 
    private static final String ENDPOINT = Optional.ofNullable(System.getenv("ENDPOINT")).orElse("https://invoicemgr.northeurope-1.eventgrid.azure.net/api/events?overload=cloudEvent&api-version=2018-01-01");
    private static final String CID = Optional.ofNullable(System.getenv("CID")).orElse("uWKbOUKZ/qoFGnHFiEugJCAqzRcr8R+pWRo7szJibTE=");
    private static final String POST_TYPE = Optional.ofNullable(System.getenv("POST_TYPE")).filter(x-> postTypesSupported.contains(x)).orElse("JSON");

    public static void post(String id){
        if (POST_TYPE.equalsIgnoreCase("JSON")) {
            postAsJson(id);
        } else {
            postCloudEvent(id);
        }
    }
    
    
    public static void postCloudEvent(String id){
        try {
                final byte[] sampleData = Cloudevent.createCEAsJson().getBytes();
                final HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                        .uri(new URI(ENDPOINT))
                        .POST(HttpRequest.BodyPublishers.ofByteArray(sampleData));
                requestBuilder.setHeader("Content-Type", "application/cloudevents-batch+json;charset=utf-8");
                requestBuilder.setHeader("aeg-sas-key", CID);
                final HttpRequest request = requestBuilder.build();
                System.out.println("Posting data for id "+ id);
                final HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println(Thread.currentThread().getName() + " ID ="+id+"   RESPONSE "+ response.statusCode()  );
            }catch (Exception exception){
                exception.printStackTrace();
        }
    }

     public static void postAsJson(String id){
        try {
                final HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                        .uri(new URI(ENDPOINT))
                        .POST(HttpRequest.BodyPublishers.ofString(Cloudevent.createData(), Charset.defaultCharset()));
                final HttpRequest request = decorateHeader(requestBuilder, "application/json");
                System.out.println("Posting data for id "+ id);
                final HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println(Thread.currentThread().getName() + " ID ="+id+"   RESPONSE "+ response.statusCode()  );
            }catch (Exception exception){
                exception.printStackTrace();
        }
    }

    public static HttpRequest decorateHeader(final HttpRequest.Builder requestBuilder, String contentType){

        requestBuilder.setHeader("Ce-Id", "test");
        requestBuilder.setHeader("Ce-Type", "InvoiceGenRequest");
        requestBuilder.setHeader("Ce-Source", "Invoice.scheduler");
        requestBuilder.setHeader("Ce-Subject", "Invoice.Gen.Req");
        requestBuilder.setHeader("Content-Type", contentType);
        requestBuilder.setHeader("Ce-Specversion", "1.0");
        requestBuilder.setHeader("aeg-sas-key", CID);
        return requestBuilder.build();
    }


    public static Runnable createRunnable(String id) {
        return ()-> PostRequest.post(id);
    }

}
