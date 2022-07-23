package com.kou.lottery.domain.rule.repository;

import com.kou.lottery.domain.rule.model.aggregates.TreeRuleRich;

/**
 * @author MiManchi
 * Date: 2022/6/11 17:14
 * Package: com.kou.lottery.domain.rule.repository
 *
 * 规则信息仓储服务接口
 */
public interface RuleRepository {

    /**
     * 查询规则决策树配置
     *
     * @param treeId    决策树ID
     * @return          决策树配置
     */
    TreeRuleRich queryTreeRuleRich(Long treeId);
}
