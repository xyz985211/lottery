package com.kou.lottery.rpc;

import com.kou.lottery.rpc.req.DrawReq;
import com.kou.lottery.rpc.req.QuantificationDrawReq;
import com.kou.lottery.rpc.res.DrawRes;

/**
 * @author MiManchi
 * Date: 2022/6/17 11:46
 * Package: com.kou.lottery.rpc
 *
 * 抽奖活动展台接口
 */
public interface LotteryActivityBooth {

    /**
     * 指定活动抽奖
     * @param drawReq 请求参数
     * @return        抽奖结果
     */
    DrawRes doDraw(DrawReq drawReq);

    /**
     * 量化人群抽奖
     * @param quantificationDrawReq 请求参数
     * @return                      抽奖结果
     */
    DrawRes doQuantificationDraw(QuantificationDrawReq quantificationDrawReq);
}
