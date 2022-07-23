package com.kou.lottery.domain.rule.service.engine.impl;

import com.kou.lottery.domain.rule.model.aggregates.TreeRuleRich;
import com.kou.lottery.domain.rule.model.req.DecisionMatterReq;
import com.kou.lottery.domain.rule.model.res.EngineResult;
import com.kou.lottery.domain.rule.model.vo.TreeNodeVO;
import com.kou.lottery.domain.rule.repository.RuleRepository;
import com.kou.lottery.domain.rule.service.engine.EngineBase;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author MiManchi
 * Date: 2022/6/17 9:31
 * Package: com.kou.lottery.domain.rule.service.engine.impl
 */
@Service
public class RuleEngineHandle extends EngineBase {

    @Resource
    private RuleRepository ruleRepository;

    @Override
    public EngineResult process(DecisionMatterReq matter){
        //  决策树规则
        TreeRuleRich treeRuleRich = ruleRepository.queryTreeRuleRich(matter.getTreeId());
        if (null == treeRuleRich){
            throw new RuntimeException("Tree Rule is null!");
        }

        //  决策节点
        TreeNodeVO treeNodeInfo = super.engineDecisionMaker(treeRuleRich, matter);

        //  决策结果
        return new EngineResult(matter.getUserId(), treeNodeInfo.getTreeId(), treeNodeInfo.getTreeNodeId(), treeNodeInfo.getNodeValue());
    }
}
