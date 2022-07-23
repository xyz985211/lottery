package com.kou.lottery.infrastructure.dao;

import com.kou.lottery.infrastructure.po.RuleTreeNodeLine;

import java.util.List;

/**
 * @author MiManchi
 * Date: 2022/6/11 17:48
 * Package: com.kou.lottery.infrastructure.dao
 */
public interface RuleTreeNodeLineDao {

    /**
     * 查询规则树节点连线集合
     * @param req   入参
     * @return      规则树节点连线集合
     */
    List<RuleTreeNodeLine> queryRuleTreeNodeLineList(RuleTreeNodeLine req);

    /**
     * 查询规则树连线数量
     *
     * @param treeId    规则树ID
     * @return          规则树连线数量
     */
    int queryTreeNodeLineCount(Long treeId);
}
