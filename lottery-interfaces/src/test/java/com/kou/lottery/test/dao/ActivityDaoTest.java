package com.kou.lottery.test.dao;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kou.lottery.infrastructure.dao.ActivityDao;
import com.kou.lottery.infrastructure.po.Activity;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author MiManchi
 * Date: 2022/6/9 21:28
 * Package: com.kou.lottery.test.dao
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ActivityDaoTest {

    @Resource
    private ActivityDao activityDao;

    @Test
    public void test_queryActivityById() {
        LambdaQueryWrapper<Activity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Activity::getActivityId, 100001L);
        Activity activity = activityDao.selectOne(queryWrapper);
        log.info("测试结果：{}", JSON.toJSONString(activity));
    }
}
