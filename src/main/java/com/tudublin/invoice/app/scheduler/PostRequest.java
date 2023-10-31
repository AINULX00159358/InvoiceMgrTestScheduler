package com.tudublin.invoice.app.scheduler;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Optional;

public class PostRequest {

    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    
    private static final String CONTENT_TYPE = Optional.ofNullable(System.getenv("CONTENT_TYPE")).orElse(Defaults.CONTENT_TYPE);
    private static final String ENDPOINT = Optional.ofNullable(System.getenv("ENDPOINT")).orElse(Defaults.ENDPOINT);
    private static final String CID = Optional.ofNullable(System.getenv("CID")).orElse(Defaults.CID);
     
    private static volatile boolean firstTime = true;

     public static boolean post(String id) {
        String data = Cloudevent.createCEAsJson();
       if ( firstTime ){
            firstTime = false;
            System.out.println("ENDPOINT "+ ENDPOINT);
            System.out.println("CONTENT_TYPE "+ CONTENT_TYPE);
            System.out.println("CID "+ CID);
             System.out.println("DATA "+ data);
             System.out.println("Posting data for id "+ id);
       }

        try {
                final HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                        .uri(new URI(ENDPOINT))
                        .setHeader("Content-Type", CONTENT_TYPE)
                        .setHeader("aeg-sas-key", CID)
                        .POST(HttpRequest.BodyPublishers.ofString(data,StandardCharsets.UTF_8));
                final HttpRequest request = requestBuilder.build();
                
                final HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println(Thread.currentThread().getName() + " ID ="+id+"   RESPONSE "+ response.statusCode()  );
                return response.statusCode() /100 == 2;
            }catch (Exception exception){
                exception.printStackTrace();
                return false;
        }
    }

    public static Runnable createRunnable(String id) {
        return ()-> PostRequest.post(id);
    }
}