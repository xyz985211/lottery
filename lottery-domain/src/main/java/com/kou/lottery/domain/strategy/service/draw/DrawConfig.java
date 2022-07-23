package com.kou.lottery.domain.strategy.service.draw;

import com.kou.lottery.common.Constants;
import com.kou.lottery.domain.strategy.service.algorithm.DrawAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author MiManchi
 * Date: 2022/5/27 21:14
 * Package: com.kou.lottery.domain.strategy.service.dram
 */
public class DrawConfig {

    @Resource
    private DrawAlgorithm entiretyRateRandomDrawAlgorithmImpl;
    @Resource
    private DrawAlgorithm singleRateRandomDrawAlgorithmImpl;

    /**
     * 抽奖策略组
     */
    protected static Map<Integer, DrawAlgorithm> drawAlgorithmGroup = new ConcurrentHashMap<>();

    /**
     * @PostConstruct 该注解被用来修饰一个非静态的void（）方法。
     * 被 @PostConstruct 修饰的方法会在服务器加载Servlet的时候运行，并且只会被服务器执行一次。
     * @PostConstruct 在构造函数之后执行，init（）方法之前执行。
     */
    @PostConstruct
    public void init(){
        drawAlgorithmGroup.put(Constants.StrategyMode.ENTIRETY.getCode(), entiretyRateRandomDrawAlgorithmImpl);
        drawAlgorithmGroup.put(Constants.StrategyMode.SINGLE.getCode(), singleRateRandomDrawAlgorithmImpl);
    }
}
