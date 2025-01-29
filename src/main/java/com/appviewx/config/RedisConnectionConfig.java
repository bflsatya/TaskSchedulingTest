package com.appviewx.config;

import com.appviewx.controllers.AddTenantController;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RedisConnectionConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisConnectionConfig.class);
    RedissonClient redissonClient;

    private Config getSentinelsConfig() {
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

    public RedissonClient getRedissonClient() {
        /*try {

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
        }*/
        return redissonClient;
    }
}
