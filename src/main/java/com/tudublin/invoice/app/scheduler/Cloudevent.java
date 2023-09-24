package com.tudublin.invoice.app.scheduler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class Cloudevent {

    private static final UUID uuid = UUID.randomUUID();
    private static final TimeZone tz = TimeZone.getTimeZone("UTC");
    private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
    static
    {
        df.setTimeZone(tz);
    }

    public static String createCEAsJson() {
        String nowAsISO = df.format(new Date());
        String json = "[\n" +
                "  {\n" +
                "    \"id\": \"EVENT-ID\",\n" +
                "    \"source\": \"/CLIENT-ID/Invoice/Business/Gen\",\n" +
                "    \"specversion\": \"1.0\",\n" +
                "    \"data\": {\n" +
                "      \"custID\": \"CUST-ID\",\n" +
                "      \"amount\": AMOUNT\n" +
                "    },\n" +
                "    \"type\": \"InvoiceGenRequest\",\n" +
                "    \"time\": \"TIME\"\n" +
                "  }\n" +
                "]";
        return json.replace("EVENT-ID", uuid.toString())
                .replace("CLIENT-ID", "client-"+ System.currentTimeMillis())
                .replace("CUST-ID", uuid.toString())
                .replace("AMOUNT", ""+ThreadLocalRandom.current().nextInt(10000, 200000))
                .replace("TIME", nowAsISO);
    }

    public static String createData() {
        String data =  "{\"custID\":\"CUST-ID\", \"amount\": AMOUNT, \"date\": \"21/09/2323\"}";
        return data.replace("EVENT-ID", uuid.toString())
        .replace("CLIENT-ID", "client-"+ System.currentTimeMillis())
        .replace("CUST-ID", uuid.toString())
        .replace("AMOUNT", ""+ThreadLocalRandom.current().nextInt(10000, 200000))
        .replace("TIME", nowAsISO);
    }
}
