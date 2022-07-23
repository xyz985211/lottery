package com.kou.lottery.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kou.lottery.infrastructure.po.Award;

/**
 * @author MiManchi
 * Date: 2022/5/27 17:02
 * Package: com.kou.lottery.infrastructure.dao
 */
public interface AwardDao extends BaseMapper<Award> {

    /**
     * 查询奖品信息
     *
     * @param awardId 奖品ID
     * @return        奖品信息
     */
    Award queryAwardInfo(String awardId);
}
