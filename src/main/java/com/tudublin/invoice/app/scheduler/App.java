package com.tudublin.invoice.app.scheduler;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.*;


/**
 *
 */
public class App 
{
    private static final ScheduledExecutorService internalScheduler = Executors.newScheduledThreadPool(1000);
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);
    private static final ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
    private static final int TPS = Optional.ofNullable(System.getenv("TPS")).map(Integer::parseInt).orElse(30);
    private static final int PERIOD = Optional.ofNullable(System.getenv("PERIOD")).map(Integer::parseInt).orElse(1);
    
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
        ScheduledFuture<?> scheduledFuture = scheduler.scheduleAtFixedRate(runnable,1, PERIOD, TimeUnit.SECONDS);
       // executorservice.shutdownNow();
    }
}
