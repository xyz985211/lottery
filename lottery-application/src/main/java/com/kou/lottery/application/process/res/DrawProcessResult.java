package com.kou.lottery.application.process.res;

import com.kou.lottery.common.Result;
import com.kou.lottery.domain.strategy.model.vo.DrawAwardVO;

/**
 * @author MiManchi
 * Date: 2022/6/8 22:37
 * Package: com.kou.lottery.application.process.res
 *
 * 活动抽奖结果
 */
public class DrawProcessResult extends Result {

    private DrawAwardVO drawAwardVO;

    public DrawProcessResult(String code, String info) {
        super(code, info);
    }

    public DrawProcessResult(String code, String info, DrawAwardVO drawAwardVO) {
        super(code, info);
        this.drawAwardVO = drawAwardVO;
    }

    public DrawAwardVO getDrawAwardVO() {
        return drawAwardVO;
    }

    public void setDrawAwardVO(DrawAwardVO drawAwardVO) {
        this.drawAwardVO = drawAwardVO;
    }
}
