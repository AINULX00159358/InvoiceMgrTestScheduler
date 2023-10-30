package com.tudublin.invoice.app.scheduler;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
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
  
     public static void post(String id){
        try {
                final HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                        .uri(new URI(ENDPOINT))
                        .POST(HttpRequest.BodyPublishers.ofString(Cloudevent.createData(), Charset.defaultCharset()));
                final HttpRequest request = decorateHeader(requestBuilder, CONTENT_TYPE);
                System.out.println("Posting data for id "+ id);
                final HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println(Thread.currentThread().getName() + " ID ="+id+"   RESPONSE "+ response.statusCode()  );
            }catch (Exception exception){
                exception.printStackTrace();
        }
    }

    public static HttpRequest decorateHeader(final HttpRequest.Builder requestBuilder, String contentType){
        requestBuilder.setHeader("Content-Type", contentType);
        requestBuilder.setHeader("Ce-Specversion", "1.0");
        requestBuilder.setHeader("aeg-sas-key", CID);
        return requestBuilder.build();
    }


    public static Runnable createRunnable(String id) {
        return ()-> PostRequest.post(id);
    }

}
