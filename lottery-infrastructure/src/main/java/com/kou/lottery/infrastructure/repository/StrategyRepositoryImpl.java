package com.kou.lottery.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kou.lottery.domain.strategy.model.aggregates.StrategyRich;
import com.kou.lottery.domain.strategy.model.vo.AwardBriefVO;
import com.kou.lottery.domain.strategy.model.vo.StrategyBriefVO;
import com.kou.lottery.domain.strategy.model.vo.StrategyDetailBriefVO;
import com.kou.lottery.infrastructure.dao.AwardDao;
import com.kou.lottery.infrastructure.dao.StrategyDao;
import com.kou.lottery.infrastructure.dao.StrategyDetailDao;
import com.kou.lottery.infrastructure.po.Award;
import com.kou.lottery.infrastructure.po.Strategy;
import com.kou.lottery.infrastructure.po.StrategyDetail;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author MiManchi
 * Date: 2022/5/27 17:25
 * Package: com.kou.lottery.domain.strategy.repository.impl
 *
 * 策略表仓储服务
 */
@Repository
public class StrategyRepositoryImpl implements com.kou.lottery.domain.strategy.repository.StrategyRepository {

    @Resource
    private StrategyDao strategyDao;
    @Resource
    private StrategyDetailDao strategyDetailDao;
    @Resource
    private AwardDao awardDao;

    /**
     * 通过策略id查找策略配置，策略明细
     *
     * @param strategyId 策略id
     * @return 策略的聚合对象
     */
    @Override
    public StrategyRich queryStrategyRich(Long strategyId) {
        LambdaQueryWrapper<Strategy> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(Strategy::getStrategyId, strategyId);
        Strategy strategy = strategyDao.selectOne(queryWrapper1);

        LambdaQueryWrapper<StrategyDetail> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(StrategyDetail::getStrategyId, strategyId);
        List<StrategyDetail> strategyDetailList = strategyDetailDao.selectList(queryWrapper2);

        StrategyBriefVO strategyBriefVO = new StrategyBriefVO();
        BeanUtils.copyProperties(strategy, strategyBriefVO);

        List<StrategyDetailBriefVO> strategyDetailBriefVOList = new ArrayList<>();
        for (StrategyDetail strategyDetail : strategyDetailList) {
            StrategyDetailBriefVO strategyDetailBriefVO = new StrategyDetailBriefVO();
            BeanUtils.copyProperties(strategyDetail, strategyDetailBriefVO);
            strategyDetailBriefVOList.add(strategyDetailBriefVO);
        }

        return new StrategyRich(strategyId, strategyBriefVO, strategyDetailBriefVOList);
    }

    /**
     * 根据奖品id查询奖品信息
     *
     * @param awardId 奖品id
     * @return 奖品信息
     */
    @Override
    public AwardBriefVO queryAwardInfo(String awardId) {
        LambdaQueryWrapper<Award> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Award::getAwardId, awardId);
        Award award = awardDao.selectOne(queryWrapper);

        // 可以使用 BeanUtils.copyProperties(award, awardBriefVO)、或者基于ASM实现的Bean-Mapping，但在效率上最好的依旧是硬编码
        AwardBriefVO awardBriefVO = new AwardBriefVO();
        awardBriefVO.setAwardId(award.getAwardId());
        awardBriefVO.setAwardType(award.getAwardType());
        awardBriefVO.setAwardName(award.getAwardName());
        awardBriefVO.setAwardContent(award.getAwardContent());

        return awardBriefVO;
    }

    /**
     * 根据策略id查找奖品剩余库存为0的奖品id
     *
     * @param strategyId 策略id
     * @return 奖品id集合
     */
    @Override
    public List<String> queryNoStockStrategyAwardList(Long strategyId) {
        return strategyDetailDao.queryNoStockStrategyAwardList(strategyId);
    }

    /**
     * 扣减库存
     *
     * @param strategyId 策略ID
     * @param awardId    奖品ID
     * @return 扣减结果
     */
    @Override
    public boolean deductStock(Long strategyId, String awardId) {
        StrategyDetail req = new StrategyDetail();
        req.setStrategyId(strategyId);
        req.setAwardId(awardId);
        int count = strategyDetailDao.deductStock(req);
        return count == 1;
    }
}