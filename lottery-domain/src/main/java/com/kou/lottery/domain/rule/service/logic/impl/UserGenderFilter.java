package com.kou.lottery.domain.rule.service.logic.impl;

import com.kou.lottery.domain.rule.model.req.DecisionMatterReq;
import com.kou.lottery.domain.rule.service.logic.BaseLogic;
import org.springframework.stereotype.Component;

/**
 * @author MiManchi
 * Date: 2022/6/17 8:45
 * Package: com.kou.lottery.domain.rule.service.logic.impl
 *
 * 性别规则
 */
@Component
public class UserGenderFilter extends BaseLogic {

    @Override
    public String matterValue(DecisionMatterReq decisionMatter) {
        return decisionMatter.getValMap().get("gender").toString();
    }
}
