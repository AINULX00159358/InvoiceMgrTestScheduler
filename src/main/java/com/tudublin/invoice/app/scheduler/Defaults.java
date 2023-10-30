package com.tudublin.invoice.app.scheduler;

public final class Defaults {
    private Defaults(){}

    public static final Integer TPS = 30;
    public static final Integer PERIOD = 1;
    public static final String CONTENT_TYPE = "application/cloudevents-batch+json;charset=utf-8";
    public static final String ENDPOINT = "https://invicemgrtopic.northeurope-1.eventgrid.azure.net/api/events";
    public static final String CID = "gwT5MMbuZwmHv4JIefrCg/tc8Hh8OMkxA9rY0pNMr90=";

}
