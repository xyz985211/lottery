package com.kou.lottery.test.interfaces;

import com.alibaba.fastjson.JSON;
import com.kou.lottery.rpc.LotteryActivityBooth;
import com.kou.lottery.rpc.req.DrawReq;
import com.kou.lottery.rpc.req.QuantificationDrawReq;
import com.kou.lottery.rpc.res.DrawRes;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * @author MiManchi
 * Date: 2022/6/18 9:16
 * Package: com.kou.lottery.test.interfaces
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class LotteryActivityBoothTest {

    @Resource
    private LotteryActivityBooth lotteryActivityBooth;

    @Test
    public void test_doDraw() {
        DrawReq drawReq = new DrawReq();
        drawReq.setuId("xiaokou3");
        drawReq.setActivityId(100001L);
        DrawRes drawRes = lotteryActivityBooth.doDraw(drawReq);
        log.info("请求参数：{}", JSON.toJSONString(drawReq));
        log.info("测试结果：{}", JSON.toJSONString(drawRes));

    }

    @Test
    public void test_doQuantificationDraw() {
        QuantificationDrawReq req = new QuantificationDrawReq();
        req.setuId("xiaokou");
        req.setTreeId(2110081902L);
        req.setValMap(new HashMap<String, Object>() {{
            put("gender", "man");
            put("age", "18");
        }});

        DrawRes drawRes = lotteryActivityBooth.doQuantificationDraw(req);
        log.info("请求参数：{}", JSON.toJSONString(req));
        log.info("测试结果：{}", JSON.toJSONString(drawRes));

    }
}
