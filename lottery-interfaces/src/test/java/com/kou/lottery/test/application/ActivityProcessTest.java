package com.kou.lottery.test.application;

import com.alibaba.fastjson.JSON;
import com.kou.lottery.application.process.ActivityProcess;
import com.kou.lottery.application.process.req.DrawProcessReq;
import com.kou.lottery.application.process.res.DrawProcessResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author MiManchi
 * Date: 2022/6/9 16:58
 * Package: com.kou.lottery.test.application
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class ActivityProcessTest {

    @Resource
    private ActivityProcess activityProcess;

    @Test
    public void test_doDrawProcess() {
        DrawProcessReq req = new DrawProcessReq();
        req.setuId("xiaofuge");
        req.setActivityId(100001L);
        DrawProcessResult drawProcessResult = activityProcess.doDrawProcess(req);

        log.info("请求入参：{}", JSON.toJSONString(req));
        log.info("测试结果：{}", JSON.toJSONString(drawProcessResult));
    }
}
