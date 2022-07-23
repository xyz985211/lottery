package com.kou.lottery.test.domain;

import com.kou.lottery.common.Constants;
import com.kou.lottery.domain.support.ids.IdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author MiManchi
 * Date: 2022/6/5 20:40
 * Package: com.kou.lottery.test.domain
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class SupportTest {

    @Resource
    private Map<Constants.Ids, IdGenerator> idGeneratorMap;

    @Test
    public void test_ids() {
        log.info("雪花算法策略，生成ID：{}", idGeneratorMap.get(Constants.Ids.SnowFlake).nextId());
        log.info("日期算法策略，生成ID：{}", idGeneratorMap.get(Constants.Ids.ShortCode).nextId());
        log.info("随机算法策略，生成ID：{}", idGeneratorMap.get(Constants.Ids.RandomNumeric).nextId());
    }
}
