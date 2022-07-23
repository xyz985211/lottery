package com.kou.lottery.domain.strategy.model.res;

import com.kou.lottery.common.Constants;
import com.kou.lottery.domain.strategy.model.vo.DrawAwardVO;

/**
 * @author MiManchi
 * Date: 2022/5/27 17:14
 * Package: com.kou.lottery.domain.strategy.model.res
 *
 * 抽奖结果
 */
public class DrawResult {

    /**
     * 用户id
     */
    private String uId;

    /**
     * 策略id
     */
    private Long strategyId;

    /**
     * 中奖状态：0未中奖、1已中奖、2兜底奖 Constants.DrawState
     */
    private Integer drawState = Constants.DrawState.FAIL.getCode();

    /**
     * 中奖奖品信息
     */
    private DrawAwardVO drawAwardVo;

    public DrawResult() {
    }

    public DrawResult(String uId, Long strategyId, Integer drawState) {
        this.uId = uId;
        this.strategyId = strategyId;
        this.drawState = drawState;
    }

    public DrawResult(String uId, Long strategyId, Integer drawState, DrawAwardVO drawAwardVo) {
        this.uId = uId;
        this.strategyId = strategyId;
        this.drawState = drawState;
        this.drawAwardVo = drawAwardVo;
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

    public Integer getDrawState() {
        return drawState;
    }

    public void setDrawState(Integer drawState) {
        this.drawState = drawState;
    }

    public DrawAwardVO getDrawAwardInfo() {
        return drawAwardVo;
    }

    public void setDrawAwardInfo(DrawAwardVO drawAwardVo) {
        this.drawAwardVo = drawAwardVo;
    }
}
