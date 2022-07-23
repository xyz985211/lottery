package com.kou.lottery.domain.strategy.model.req;


/**
 * @author MiManchi
 * Date: 2022/5/27 17:08
 * Package: com.kou.lottery.domain.strategy.model.req
 *
 * 抽奖请求对象
 */
public class DrawReq {
    /**
     * 用户id
     */
    private String uId;

    /**
     * 策略id
     */
    private Long strategyId;

    /**
     * 防重ID
     */
    private String uuid;

    public DrawReq() {
    }

    public DrawReq(String uId, Long strategyId) {
        this.uId = uId;
        this.strategyId = strategyId;
    }

    public DrawReq(String uId, Long strategyId, String uuid) {
        this.uId = uId;
        this.strategyId = strategyId;
        this.uuid = uuid;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public Long getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(Long strategyId) {
        this.strategyId = strategyId;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
