package com.csu.backend.utils;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

// 限流工具类，用于限制一段时间内邮件的发送
@Component
public class FlowUtils {
    @Resource
    StringRedisTemplate template;

    public boolean limitOnceCheck(String key, int blockTime) {
        if (Boolean.TRUE.equals(template.hasKey(key))) {
            // 如果redis队列中有这个key，则说明还在冷却中
            return false;
        } else {
            // 若没有这个key，则添加到redis当中，进入冷却时间
            template.opsForValue().set(key, "", blockTime, TimeUnit.SECONDS);
        }
        return true;
    }
}
