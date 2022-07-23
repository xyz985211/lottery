package com.kou.lottery.domain.rule.service.logic;

import com.kou.lottery.common.Constants;
import com.kou.lottery.domain.rule.model.req.DecisionMatterReq;
import com.kou.lottery.domain.rule.model.vo.TreeNodeLineVO;

import java.util.List;

/**
 * @author MiManchi
 * Date: 2022/6/17 8:38
 * Package: com.kou.lottery.domain.rule.service.logic
 *
 * 规则基础抽象类
 */
public abstract class BaseLogic implements LogicFilter{

    /**
     * 逻辑决策器
     * @param matterValue          决策值
     * @param treeNodeLineInfoList 决策节点
     * @return                     下一个节点Id
     */
    @Override
    public Long filter(String matterValue, List<TreeNodeLineVO> treeNodeLineInfoList) {
        for (TreeNodeLineVO nodeLine : treeNodeLineInfoList) {
            if (decisionLogic(matterValue, nodeLine)){
                return nodeLine.getNodeIdTo();
            }
        }
        return Constants.Global.TREE_NULL_NODE;
    }

    @Override
    public abstract String matterValue(DecisionMatterReq decisionMatter);

    private boolean decisionLogic(String matterValue, TreeNodeLineVO nodeLine) {
        switch (nodeLine.getRuleLimitType()) {
            case Constants.RuleLimitType.EQUAL:
                return matterValue.equals(nodeLine.getRuleLimitValue());
            case Constants.RuleLimitType.GT:
                return Double.parseDouble(matterValue) > Double.parseDouble(nodeLine.getRuleLimitValue());
            case Constants.RuleLimitType.LT:
                return Double.parseDouble(matterValue) < Double.parseDouble(nodeLine.getRuleLimitValue());
            case Constants.RuleLimitType.GE:
                return Double.parseDouble(matterValue) >= Double.parseDouble(nodeLine.getRuleLimitValue());
            case Constants.RuleLimitType.LE:
                return Double.parseDouble(matterValue) <= Double.parseDouble(nodeLine.getRuleLimitValue());
            default:
                return false;
        }
    }
}
