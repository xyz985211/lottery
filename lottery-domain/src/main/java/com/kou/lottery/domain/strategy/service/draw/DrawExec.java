package com.kou.lottery.domain.strategy.service.draw;

import com.kou.lottery.domain.strategy.model.req.DrawReq;
import com.kou.lottery.domain.strategy.model.res.DrawResult;

/**
 * @author MiManchi
 * Date: 2022/5/27 21:13
 * Package: com.kou.lottery.domain.strategy.service.dram
 */
public interface DrawExec {

    /**
     * 抽奖方法
     * @param req 抽奖参数：用户id、策略id
     * @return 中奖结果
     */
    DrawResult doDrawExec(DrawReq req);
}
