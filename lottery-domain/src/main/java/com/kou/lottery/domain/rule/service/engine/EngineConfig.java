package com.kou.lottery.domain.rule.service.engine;

import com.kou.lottery.domain.rule.service.logic.LogicFilter;
import com.kou.lottery.domain.rule.service.logic.impl.UserAgeFilter;
import com.kou.lottery.domain.rule.service.logic.impl.UserGenderFilter;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author MiManchi
 * Date: 2022/6/17 9:03
 * Package: com.kou.lottery.domain.rule.service.engine
 *
 * 规则配置
 */
public class EngineConfig {

    protected static Map<String, LogicFilter> logicFilterMap = new ConcurrentHashMap<>();

    @Resource
    private UserAgeFilter userAgeFilter;
    @Resource
    private UserGenderFilter userGenderFilter;

    @PostConstruct
    public void init(){
        logicFilterMap.put("userAge", userAgeFilter);
        logicFilterMap.put("userGender", userGenderFilter);
    }
}
