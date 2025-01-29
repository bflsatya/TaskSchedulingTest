package com.appviewx.controllers;

import com.appviewx.config.RedisConnectionConfig;
import com.appviewx.config.SingletonBeanProvider;
import com.appviewx.config.TestSingletonWithConstructorParam;
import org.redisson.api.RScript;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisException;
import org.redisson.codec.JsonJacksonCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@RestController
public class AddTenantController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddTenantController.class);

    RedissonClient redissonClient;

    @Value("${queueScriptFileName}")
    private String queueScriptFileName;

    @Value("${deQueueScriptFileName}")
    private String deQueueScriptFileName;

    private String shaDigestQueue;

    private String shaDigestDeQueue;

    @Autowired
    private SingletonBeanProvider singletonBeanProvider;

    Executor executor = Executors.newFixedThreadPool(2);

    @Autowired
    public AddTenantController(RedisConnectionConfig redisConnectionConfig) {
        this.redissonClient = redisConnectionConfig.getRedissonClient();
    }

    @PostConstruct
    public void initializeShaDigest() {
        /*RScript script = redissonClient.getScript(JsonJacksonCodec.INSTANCE);
        this.shaDigestQueue = getSHADigestFromLuaScript(queueScriptFileName, script);
        this.shaDigestDeQueue = getSHADigestFromLuaScript(deQueueScriptFileName, script);*/
    }

    @GetMapping("/queue-async")
    public void generateAsyncRequestAsync(@RequestParam(value = "tenantId") String tenantId, @RequestParam(value = "serviceName") String serviceName, @RequestParam(value = "sequenceId") int sequenceId) throws ExecutionException, InterruptedException {
        TestSingletonWithConstructorParam testSingletonWithConstructorParam = singletonBeanProvider.getTestSingletonWithConstructorParam(sequenceId);
        testSingletonWithConstructorParam.printSequenceId();
        LOGGER.info("Thread name when request received is : {}", Thread.currentThread().getName());
        //processQueueAsyncRequest(tenantId, serviceName, 1);
    }

    private void processQueueAsyncRequest(String tenantId, String serviceName, int retryAttempt) {
        if(retryAttempt <= 3) {
            CompletableFuture.supplyAsync(() -> {
                LOGGER.info("Thread name inside supplyAsync is : {}", Thread.currentThread().getName());
                return executeForQueue(tenantId, serviceName);
            }).whenComplete((resultFromPreviousStep, exception) -> {
                LOGGER.info("Thread name inside whenComplete is : {}", Thread.currentThread().getName());
                if(exception == null) {
                    LOGGER.info("Successfully Processed the Async Request");
                    LOGGER.info("Result is : {}", resultFromPreviousStep);
                } else {
                    Boolean retryWorthyException = isRetryWorthyException(exception.getCause());
                    if(resultFromPreviousStep == null && retryWorthyException) {
                        // Handle exception
                        if(checkIfExceptionDueToNoScript(exception.getMessage())) {
                            RScript script = redissonClient.getScript(JsonJacksonCodec.INSTANCE);
                            this.shaDigestQueue = getSHADigestFromLuaScript(this.queueScriptFileName, script);
                        }
                        processQueueAsyncRequest(tenantId, serviceName, retryAttempt + 1);
                    } else if(!retryWorthyException) {
                        LOGGER.error("Unhandled Exception thrown with message : {}", exception.getMessage());
                    }
                }
            });

        } else {
            LOGGER.error("No more retries available. Failure");
        }
    }

    private Boolean isRetryWorthyException(Throwable exception) {
        if(exception instanceof RedisException) {
            return true;
        }
        return false;
    }

    private LuaResponse executeForQueue(String tenantId, String serviceName) {
        Object[] keysArray = new Object[]{serviceName, "testDeque"};
        LuaResponse result = executeLuaScriptForQueue(this.shaDigestQueue, Arrays.asList(keysArray), tenantId);
        return result;
    }


    @GetMapping("/dequeue")
    public Object pollForAsyncRequest(@RequestHeader Map<String,String> headers, HttpServletRequest request, HttpServletResponse response, @RequestParam(value = "serviceName") String serviceName, @RequestParam(value = "circularListName") String circularListName ) {
        LOGGER.info("in pollForAsyncRequest() method.");
        Object[] keysArray = new Object[]{serviceName, circularListName};

        long startTime = System.currentTimeMillis();
        LuaResponse result = executeLuaScript(this.shaDigestDeQueue, Arrays.asList(keysArray), -1);
        LOGGER.info("DeQueue Script Execution time : {}", (System.currentTimeMillis() - startTime));

        LOGGER.info("result: {}", result);

        return result;
    }

    private Boolean checkIfExceptionDueToNoScript(String exceptionMessage) {
        if(exceptionMessage.contains("NOSCRIPT No matching script.")) {
            return true;
        }
        return false;
    }

    private LuaResponse executeLuaScriptForQueue(String shaDigest, List<Object> keysList, Object... scriptArguments ) {
        RScript script = redissonClient.getScript(JsonJacksonCodec.INSTANCE);
        LuaResponse result = null;
        result = this.<LuaResponse>execute(script, shaDigest, keysList, scriptArguments);
        return result;
    }

    private LuaResponse executeLuaScript(String shaDigest, List<Object> keysList, Object... scriptArguments ) {
        RScript script = redissonClient.getScript(JsonJacksonCodec.INSTANCE);
        LuaResponse resultAsObject = null;
        try {
            resultAsObject = this.<LuaResponse>execute(script, shaDigest, keysList, scriptArguments);
            LOGGER.info("{}",resultAsObject);
        } catch (RedisException redisException) {
            LOGGER.error("Exception Caught ");
            if(checkIfExceptionDueToNoScript(redisException.getMessage())) {
                LOGGER.error("NOSCRIPT Exception Caught and the Dequeue script is getting Loaded again. ");
                this.shaDigestDeQueue = getSHADigestFromLuaScript(this.deQueueScriptFileName, script);
                shaDigest = this.shaDigestDeQueue;
                resultAsObject = this.<LuaResponse>execute(script, shaDigest, keysList, scriptArguments);
            } else {
                throw redisException;
            }
        }
        return resultAsObject;
    }

    private <T> T execute(RScript script, String shaDigest, List<Object> keysList, Object[] scriptArguments) {
        T result = script.evalSha(RScript.Mode.READ_WRITE, shaDigest, RScript.ReturnType.VALUE, keysList, scriptArguments);
        return result;
    }

    private String getSHADigestFromLuaScript(String fileName, RScript script) {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(fileName/*"dequeue-script.lua"*/);
        String luaScriptAsString = null;
        try (InputStreamReader isr = new InputStreamReader(resourceAsStream);
             BufferedReader reader = new BufferedReader(isr)) {
            luaScriptAsString = reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String shaDigest = generateSHAForLuaScript(script, luaScriptAsString);
        return shaDigest;
    }

    private String generateSHAForLuaScript(RScript script, String luaScriptAsString) {
        return script.scriptLoad(luaScriptAsString);
    }
}
