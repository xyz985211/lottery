package com.kou.lottery.test;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kou.lottery.common.Constants;
import com.kou.lottery.domain.award.model.req.GoodsReq;
import com.kou.lottery.domain.award.model.res.DistributionRes;
import com.kou.lottery.domain.award.service.factory.DistributionGoodsFactory;
import com.kou.lottery.domain.award.service.goods.DistributionGoods;
import com.kou.lottery.domain.strategy.model.req.DrawReq;
import com.kou.lottery.domain.strategy.model.res.DrawResult;
import com.kou.lottery.domain.strategy.model.vo.DrawAwardVO;
import com.kou.lottery.domain.strategy.service.draw.DrawExec;
import com.kou.lottery.infrastructure.dao.ActivityDao;
import com.kou.lottery.infrastructure.po.Activity;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author MiManchi
 * Date: 2022/5/25 20:01
 * Package: com.kou.lottery.test
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SpringRunnerTest {

    @Resource
    private ActivityDao activityDao;

    @Resource
    private DrawExec drawExec;

    @Resource
    private DistributionGoodsFactory distributionGoodsFactory;

    @Test
    public void test_drawExec() {
        drawExec.doDrawExec(new DrawReq("小寇", 10001L));
        drawExec.doDrawExec(new DrawReq("小佳佳", 10001L));
        drawExec.doDrawExec(new DrawReq("小蜗牛", 10001L));
        drawExec.doDrawExec(new DrawReq("八杯水", 10001L));
    }

    @Test
    public void test_award() {
        // 执行抽奖
        DrawResult drawResult = drawExec.doDrawExec(new DrawReq("小寇", 10001L));

        // 判断抽奖结果
        Integer drawState = drawResult.getDrawState();
        if (Constants.DrawState.FAIL.getCode().equals(drawState)) {
            log.info("未中奖 DrawAwardInfo is null");
            return;
        }

        // 封装发奖参数，orderId：2109313442431 为模拟ID，需要在用户参与领奖活动时生成
        DrawAwardVO drawAwardVo = drawResult.getDrawAwardInfo();
        GoodsReq goodsReq = new GoodsReq(drawResult.getuId(), 2109313442431L, drawAwardVo.getAwardId(), drawAwardVo.getAwardName(), drawAwardVo.getAwardContent());

        // 根据 awardType 从抽奖工厂中获取对应的发奖服务
        DistributionGoods distributionGoodsService = distributionGoodsFactory.getDistributionGoodsService(drawAwardVo.getAwardType());
        DistributionRes distributionRes = distributionGoodsService.doDistribution(goodsReq);

        log.info("测试结果：{}", JSON.toJSONString(distributionRes));
    }


    @Test
    public void test_insert() {
        Activity activity = new Activity();
        activity.setActivityId(100001L);
        activity.setActivityName("测试活动");
        activity.setActivityDesc("仅用于插入数据测试");
        activity.setBeginDateTime(new Date());
        activity.setEndDateTime(new Date());
        activity.setStockCount(100);
        activity.setTakeCount(10);
        activity.setState(0);
        activity.setCreator("xiaokou");
        activityDao.insert(activity);
    }

    @Test
    public void test_select() {
        LambdaQueryWrapper<Activity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Activity::getActivityId, 100001L);
        Activity activity = activityDao.selectOne(queryWrapper);
        log.info("测试结果：{}", JSON.toJSONString(activity));
    }

}
