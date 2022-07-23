package com.kou.lottery.infrastructure.dao;

import com.kou.lottery.infrastructure.po.RuleTree;

/**
 * @author MiManchi
 * Date: 2022/6/11 17:24
 * Package: com.kou.lottery.infrastructure.dao
 */
public interface RuleTreeDao {

    /**
     * 规则树查询
     * @param id ID
     * @return   规则树
     */
    RuleTree queryRuleTreeByTreeId(Long id);

    /**
     * 规则树简要信息查询
     * @param treeId 规则树ID
     * @return       规则树
     */
    RuleTree queryTreeSummaryInfo(Long treeId);
}
