package com.neobank.apigateway.config;

import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.redis.redisson.cas.RedissonBasedProxyManager;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.command.CommandAsyncExecutor;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {
    @org.springframework.beans.factory.annotation.Value("${spring.data.redis.host:localhost}")
    private String redisHost;

    @org.springframework.beans.factory.annotation.Value("${spring.data.redis.port:6379}")
    private String redisPort;

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redissonClient() {
        org.redisson.config.Config config = new Config();
        config.useSingleServer().setAddress("redis://" + redisHost + ":" + redisPort);
        return Redisson.create(config);
    }

    @Bean
    public ProxyManager<String> proxyManager(RedissonClient redissonClient) {
        CommandAsyncExecutor commandExecutor = ((Redisson) redissonClient).getCommandExecutor();

        return RedissonBasedProxyManager.builderFor(commandExecutor).build();
    }
}
