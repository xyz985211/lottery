package com.kou.lottery.test.dao;

import com.alibaba.fastjson.JSON;
import com.kou.lottery.common.Constants;
import com.kou.lottery.domain.support.ids.IdGenerator;
import com.kou.lottery.infrastructure.dao.UserStrategyExportDao;
import com.kou.lottery.infrastructure.po.UserStrategyExport;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Map;

/**
 * @author MiManchi
 * Date: 2022/6/9 21:29
 * Package: com.kou.lottery.test.dao
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class UserStrategyExportDaoTest {

    @Resource
    private UserStrategyExportDao userStrategyExportDao;

    @Resource
    private Map<Constants.Ids, IdGenerator> idGeneratorMap;

    @Test
    public void test_insert() {
        UserStrategyExport userStrategyExport = new UserStrategyExport();
        userStrategyExport.setuId("Uhdgkw766120d");
        userStrategyExport.setActivityId(idGeneratorMap.get(Constants.Ids.ShortCode).nextId());
        userStrategyExport.setOrderId(idGeneratorMap.get(Constants.Ids.SnowFlake).nextId());
        userStrategyExport.setStrategyId(idGeneratorMap.get(Constants.Ids.RandomNumeric).nextId());
        userStrategyExport.setStrategyMode(Constants.StrategyMode.SINGLE.getCode());
        userStrategyExport.setGrantType(1);
        userStrategyExport.setGrantDate(new Date());
        userStrategyExport.setGrantState(1);
        userStrategyExport.setAwardId("1");
        userStrategyExport.setAwardType(Constants.AwardType.DESC.getCode());
        userStrategyExport.setAwardName("IMac");
        userStrategyExport.setAwardContent("奖品描述");
        userStrategyExport.setUuid(String.valueOf(userStrategyExport.getOrderId()));

        userStrategyExportDao.insert(userStrategyExport);
    }

    @Test
    public void test_select() {
        UserStrategyExport userStrategyExport = userStrategyExportDao.queryUserStrategyExportByUId("Uhdgkw766120d");
        log.info("测试结果：{}", JSON.toJSONString(userStrategyExport));
    }
}
