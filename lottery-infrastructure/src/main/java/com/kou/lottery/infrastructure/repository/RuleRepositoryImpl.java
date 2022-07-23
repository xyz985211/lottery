package com.kou.lottery.infrastructure.repository;

import com.kou.lottery.common.Constants;
import com.kou.lottery.domain.rule.model.aggregates.TreeRuleRich;
import com.kou.lottery.domain.rule.model.vo.TreeNodeLineVO;
import com.kou.lottery.domain.rule.model.vo.TreeNodeVO;
import com.kou.lottery.domain.rule.model.vo.TreeRootVO;
import com.kou.lottery.domain.rule.repository.RuleRepository;
import com.kou.lottery.infrastructure.dao.RuleTreeDao;
import com.kou.lottery.infrastructure.dao.RuleTreeNodeDao;
import com.kou.lottery.infrastructure.dao.RuleTreeNodeLineDao;
import com.kou.lottery.infrastructure.po.RuleTree;
import com.kou.lottery.infrastructure.po.RuleTreeNode;
import com.kou.lottery.infrastructure.po.RuleTreeNodeLine;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author MiManchi
 * Date: 2022/6/11 17:20
 * Package: com.kou.lottery.infrastructure.repository
 */
@Repository
public class RuleRepositoryImpl implements RuleRepository {

    @Resource
    private RuleTreeDao ruleTreeDao;
    @Resource
    private RuleTreeNodeDao ruleTreeNodeDao;
    @Resource
    private RuleTreeNodeLineDao ruleTreeNodeLineDao;

    @Override
    public TreeRuleRich queryTreeRuleRich(Long treeId) {

        //  规则树
        RuleTree ruleTree = ruleTreeDao.queryRuleTreeByTreeId(treeId);
        TreeRootVO treeRootVO = new TreeRootVO();
        treeRootVO.setTreeId(ruleTree.getId());
        treeRootVO.setTreeName(ruleTree.getTreeName());
        treeRootVO.setTreeRootNodeId(ruleTree.getTreeRootNodeId());

        //  树节点->树连接线
        Map<Long, TreeNodeVO> treeNodeMap = new HashMap<>();
        List<RuleTreeNode> ruleTreeNodeList = ruleTreeNodeDao.queryRuleTreeNodeList(treeId);
        for (RuleTreeNode ruleTreeNode : ruleTreeNodeList) {
            List<TreeNodeLineVO> treeNodeLineInfoList = new ArrayList<>();
            if (Constants.NodeType.STEM.equals(ruleTreeNode.getNodeType())){

                RuleTreeNodeLine ruleTreeNodeLineReq = new RuleTreeNodeLine();
                ruleTreeNodeLineReq.setTreeId(treeId);
                ruleTreeNodeLineReq.setNodeIdFrom(ruleTreeNode.getId());
                List<RuleTreeNodeLine> ruleTreeNodeLineList = ruleTreeNodeLineDao.queryRuleTreeNodeLineList(ruleTreeNodeLineReq);

                for (RuleTreeNodeLine nodeLine : ruleTreeNodeLineList) {
                    TreeNodeLineVO treeNodeLineVO = new TreeNodeLineVO();
                    treeNodeLineVO.setNodeIdFrom(nodeLine.getNodeIdFrom());
                    treeNodeLineVO.setNodeIdTo(nodeLine.getNodeIdTo());
                    treeNodeLineVO.setRuleLimitType(nodeLine.getRuleLimitType());
                    treeNodeLineVO.setRuleLimitValue(nodeLine.getRuleLimitValue());
                    treeNodeLineInfoList.add(treeNodeLineVO);
                }
            }
            TreeNodeVO treeNodeInfo =new TreeNodeVO();
            treeNodeInfo.setTreeId(ruleTreeNode.getTreeId());
            treeNodeInfo.setTreeNodeId(ruleTreeNode.getId());
            treeNodeInfo.setNodeType(ruleTreeNode.getNodeType());
            treeNodeInfo.setNodeValue(ruleTreeNode.getNodeValue());
            treeNodeInfo.setRuleKey(ruleTreeNode.getRuleKey());
            treeNodeInfo.setRuleDesc(ruleTreeNode.getRuleDesc());
            treeNodeInfo.setTreeNodeLineInfoList(treeNodeLineInfoList);

            treeNodeMap.put(ruleTreeNode.getId(), treeNodeInfo);
        }

        TreeRuleRich treeRuleRich = new TreeRuleRich();
        treeRuleRich.setTreeRoot(treeRootVO);
        treeRuleRich.setTreeNodeMap(treeNodeMap);

        return treeRuleRich;
    }
}
