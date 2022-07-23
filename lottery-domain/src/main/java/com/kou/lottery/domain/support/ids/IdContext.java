package com.kou.lottery.domain.support.ids;

import com.kou.lottery.common.Constants;
import com.kou.lottery.domain.support.ids.policy.RandomNumeric;
import com.kou.lottery.domain.support.ids.policy.ShortCode;
import com.kou.lottery.domain.support.ids.policy.SnowFlake;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author MiManchi
 * Date: 2022/6/5 20:29
 * Package: com.kou.lottery.domain.support.ids
 *
 * Id 策略模式上下文配置「在正式的完整的系统架构中，ID 的生成会有单独的服务来完成，其他服务来调用 ID 生成接口即可」
 */
@Configuration
public class IdContext {

    /**
     * 创建 ID 生成策略对象，属于策略设计模式的使用方式
     *
     * @param snowFlake 雪花算法，长码，大量
     * @param shortCode 日期算法，短码，少量，全局唯一需要自己保证
     * @param randomNumeric 随机算法，短码，大量，全局唯一需要自己保证
     * @return IIdGenerator 实现类
     */
    @Bean
    public Map<Constants.Ids, IdGenerator> idGenerator(SnowFlake snowFlake, ShortCode shortCode, RandomNumeric randomNumeric){
        Map<Constants.Ids, IdGenerator> idGeneratorMap = new HashMap<>(8);
        idGeneratorMap.put(Constants.Ids.SnowFlake, snowFlake);
        idGeneratorMap.put(Constants.Ids.ShortCode, shortCode);
        idGeneratorMap.put(Constants.Ids.RandomNumeric, randomNumeric);
        return idGeneratorMap;
    }
}
