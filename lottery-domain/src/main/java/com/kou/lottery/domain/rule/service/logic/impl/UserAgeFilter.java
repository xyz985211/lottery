package com.kou.lottery.domain.rule.service.logic.impl;

import com.kou.lottery.domain.rule.model.req.DecisionMatterReq;
import com.kou.lottery.domain.rule.service.logic.BaseLogic;
import org.springframework.stereotype.Component;

/**
 * @author MiManchi
 * Date: 2022/6/17 8:42
 * Package: com.kou.lottery.domain.rule.service.logic.impl
 *
 * 年龄规则
 */
@Component
public class UserAgeFilter extends BaseLogic {

    /**
     * 获取决策值
     *
     * @param decisionMatter 决策物料
     * @return               决策值
     */
    @Override
    public String matterValue(DecisionMatterReq decisionMatter) {
        return decisionMatter.getValMap().get("age").toString();
    }
}
