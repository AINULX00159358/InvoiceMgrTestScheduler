package com.tudublin.invoice.app.scheduler;

import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.UUID;
import java.util.concurrent.*;


/**
 * Hello world!
 *
 */
public class App 
{
    private static final ScheduledExecutorService internalScheduler =
                                                    Executors.newScheduledThreadPool(1000);
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);
    private static final ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
    private static final int TPS = Optional.ofNullable(System.getenv("tps")).map(Integer::parseInt).orElse(30);

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            try {
                scheduler.shutdownNow();
            }catch (Exception e){
                e.printStackTrace();
            }
            try {
                internalScheduler.shutdownNow();
            }catch (Exception e){
                e.printStackTrace();
            }
        }));
    }
    public static void main( String[] args ) throws Exception {
        System.out.println( "executing App Scheduler with TPS "+ TPS );

        Runnable runnable = () -> {
            for (int y=0; y< TPS; y++){
                final UUID uuid = UUID.randomUUID();
                long delay = threadLocalRandom.nextLong(10, 800);
                internalScheduler.schedule(()->PostRequest.post(uuid.toString()), delay, TimeUnit.MILLISECONDS);
            }
            System.out.println("Added batch of 30");
        };
        ScheduledFuture<?> scheduledFuture = scheduler.scheduleAtFixedRate(runnable,1, 1, TimeUnit.SECONDS);
       // executorservice.shutdownNow();
    }
}
