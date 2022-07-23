package com.kou.lottery.domain.rule.service.logic;

import com.kou.lottery.domain.rule.model.req.DecisionMatterReq;
import com.kou.lottery.domain.rule.model.vo.TreeNodeLineVO;

import java.util.List;

/**
 * @author MiManchi
 * Date: 2022/6/17 8:36
 * Package: com.kou.lottery.domain.rule.service.logic
 *
 * 规则过滤器接口
 */
public interface LogicFilter {

    /**
     * 逻辑决策器
     * @param matterValue          决策值
     * @param treeNodeLineInfoList 决策节点
     * @return                     下一个节点Id
     */
    Long filter(String matterValue, List<TreeNodeLineVO> treeNodeLineInfoList);

    /**
     * 获取决策值
     *
     * @param decisionMatter 决策物料
     * @return               决策值
     */
    String matterValue(DecisionMatterReq decisionMatter);
}
