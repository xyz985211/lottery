package com.kou.lottery.application.process.res;

import com.kou.lottery.common.Result;

/**
 * @author MiManchi
 * Date: 2022/6/17 11:14
 * Package: com.kou.lottery.application.process.res
 */
public class RuleQuantificationCrowdResult extends Result {

    /** 活动ID */
    private Long activityId;

    public RuleQuantificationCrowdResult(String code, String info) {
        super(code, info);
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }
}
