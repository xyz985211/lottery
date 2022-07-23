package com.kou.lottery.rpc.res;

import com.kou.lottery.common.Result;
import com.kou.lottery.rpc.dto.AwardDTO;

import java.io.Serializable;

/**
 * @author MiManchi
 * Date: 2022/6/17 11:46
 * Package: com.kou.lottery.rpc.res
 *
 * 抽奖结果
 */
public class DrawRes extends Result implements Serializable {

    private AwardDTO awardDTO;

    public DrawRes(String code, String info) {
        super(code, info);
    }

    public AwardDTO getAwardDTO() {
        return awardDTO;
    }

    public void setAwardDTO(AwardDTO awardDTO) {
        this.awardDTO = awardDTO;
    }
}
