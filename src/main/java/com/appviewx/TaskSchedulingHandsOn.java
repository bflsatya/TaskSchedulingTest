package com.appviewx;


import com.appviewx.controllers.LuaResponse;
import org.redisson.Redisson;
import org.redisson.api.*;
import org.redisson.api.stream.StreamReadGroupArgs;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.DriverManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Supplier;


public class TaskSchedulingHandsOn {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskSchedulingHandsOn.class);
    private static RedissonClient redissonClient;

    private ExecutorService executor
            = Executors.newSingleThreadExecutor();

    public Future<Integer> calculate(Integer input) {
        executor.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return input * input;
            }
        });
        return executor.submit(() -> {
            Thread.sleep(1000);
            return input * input;
        });
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        RList<LuaResponse> list = getRedissonClient().getList("test-json", JsonJacksonCodec.INSTANCE);
        list.add(new LuaResponse("tenant8",1));
        LuaResponse luaResponse = list.get(15);
        Integer age = 1;
        LOGGER.info("Main thread name is : {} ", Thread.currentThread().getName());
        CompletableFuture<String> maturityFuture = CompletableFuture.supplyAsync(new Supplier<Integer>() {
            @Override
            public Integer get() {
                LOGGER.info("Thread name inside supplyAsync is : {}", Thread.currentThread().getName());
                if (age < 0) {
                    return age;
                    //throw new IllegalArgumentException("Age can not be negative");
                }
                if (age > 18) {
                    return /*"Adult"*/ age;
                } else {
                    return /*"Child"*/ age
                            ;
                }
            }
        }).thenApply(age1 -> {
            if(age1 < 0) {
                throw new IllegalArgumentException("Age can not be negative");
            }
            return "Age >= 0";
        }).exceptionally(ex -> {
            System.out.println("Oops! We have an exception - " + ex.getMessage());
            return "Unknown!";
        });
        //maturityFuture.get();
        System.out.println("Maturity : " + maturityFuture.get());
        ThreadPoolTaskScheduler threadPoolTaskScheduler = createTaskSchedulerForPolling();
        LOGGER.info("Scheduling async request poller with fixed delay : {} ", 1000);
        Date startTime = new Date(Instant.now().plus(5, ChronoUnit.SECONDS).toEpochMilli());
        /*threadPoolTaskScheduler.scheduleWithFixedDelay(new Runnable() {
                                                           @Override
                                                           public void run() {
                                                               pollQueueReaderForJobs();
                                                           }
                                                       }, startTime,
                Long.parseLong("1000"));*/
        /*RRingBuffer<String> newRingBuffer = getRedissonClient().getRingBuffer("newRingBuffer");
        newRingBuffer.trySetCapacity(2);
        newRingBuffer.add("Tenant1");
        newRingBuffer.add("Tenant2");
        String peek = newRingBuffer.element();
        LOGGER.info("peek : {}", peek);
        String peek2 = newRingBuffer.element();
        LOGGER.info("peek2 : {}", peek2);*/
        try {
            String s = readFile("/home/satya/FragmaDocuments/appviewx/queue-script.lua", Charset.defaultCharset());
            LOGGER.info("File contents : {}", s);
        } catch (IOException e) {
            e.printStackTrace();
        }
        RDeque<String> rDeque = getRedissonClient().getDeque("testDeque");
        rDeque.add("samy");
        RTransaction transaction = getRedissonClient().createTransaction(TransactionOptions.defaults());
        RStream<String, String> subSystemsStream = getRedissonClient().getStream("avx_subsystems_stream");

        //subSystemsStream.createGroup("testGroup", StreamMessageId.NEWEST);
        /*List<StreamGroup> streamGroups = subSystemsStream.listGroups();
        for(StreamGroup streamGroup : streamGroups) {
            streamGroup.getName();
        }*/

        //while(true) {
            Map<StreamMessageId, Map<String, String>> streamMessageIdMapMap = subSystemsStream.readGroup("testGroup", "Consumer1", StreamReadGroupArgs.neverDelivered());
            for(Map.Entry<StreamMessageId, Map<String, String>> streamMessageIdMapEntry : streamMessageIdMapMap.entrySet()) {
                LOGGER.info("StreamMessageId is : {}", streamMessageIdMapEntry.getKey());
                LOGGER.info("Message value is : {}", streamMessageIdMapEntry.getValue());
            }
        //}
    }
    private static ThreadPoolTaskScheduler createTaskSchedulerForPolling() {
        int corePoolSize = 1;
        LOGGER.info("Initializing ThreadPoolTaskScheduler for Async request polling with Core Pool Size :  {} ",
                corePoolSize);
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(corePoolSize);
        threadPoolTaskScheduler.initialize();
        return threadPoolTaskScheduler;
    }
    private static void pollQueueReaderForJobs() {
        LOGGER.info("in pollQueueReaderForJobs()");
        while(true) {

        }
    }

    private static Config getSentinelsConfig() {
        LOGGER.info("Redis connecting in sentinel mode ");
        Config config = new Config();
        //config.setCodec(StringCodec.INSTANCE);

        List<String> sentinalUrls = new ArrayList<>();
        sentinalUrls.add("redis://localhost:26379");
        config.useSentinelServers().setPassword("appviewx@123")
                .setMasterName("mymaster").setDnsMonitoringInterval(-1).setCheckSentinelsList(false)
                .setSentinelAddresses(sentinalUrls);
        return config;
    }

    public static RedissonClient getRedissonClient() {
        try {

            if (redissonClient == null) {
                LOGGER.info(" Creating redisson client with url {} ", "redis://localhost:26379");
                Config config;
                config = getSentinelsConfig();
                String yaml = config.toYAML();
                LOGGER.info(" config redisson server with yaml {} ", yaml);
                redissonClient = Redisson.create(config);
            }
        } catch (Exception e) {
            LOGGER.error("Error while creating redis config", e);
            throw new RuntimeException("REDIS CONNECTION IS FAILED");
        }
        return redissonClient;
    }

    static String readFile(String path, Charset encoding)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}
