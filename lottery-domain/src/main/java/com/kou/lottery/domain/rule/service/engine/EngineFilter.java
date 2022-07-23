package com.kou.lottery.domain.rule.service.engine;

import com.kou.lottery.domain.rule.model.req.DecisionMatterReq;
import com.kou.lottery.domain.rule.model.res.EngineResult;

/**
 * @author MiManchi
 * Date: 2022/6/17 8:46
 * Package: com.kou.lottery.domain.rule.service.engine
 *
 * 规则过滤器引擎
 */
public interface EngineFilter {

    /**
     * 规则过滤器接口
     *
     * @param matter      规则决策物料
     * @return            规则决策结果
     */
    EngineResult process(final DecisionMatterReq matter);
}
