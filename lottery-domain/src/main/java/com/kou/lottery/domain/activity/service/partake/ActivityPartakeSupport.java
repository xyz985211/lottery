package com.kou.lottery.domain.activity.service.partake;

import com.kou.lottery.domain.activity.model.req.PartakeReq;
import com.kou.lottery.domain.activity.model.vo.ActivityBillVO;
import com.kou.lottery.domain.activity.repository.ActivityRepository;

import javax.annotation.Resource;

/**
 * @author MiManchi
 * Date: 2022/6/7 20:20
 * Package: com.kou.lottery.domain.activity.service.partake
 *
 * 活动领取模操作，一些通用的数据服务
 */
public class ActivityPartakeSupport {

    @Resource
    protected ActivityRepository activityRepository;

    protected ActivityBillVO queryActivityBill(PartakeReq req){
        return activityRepository.queryActivityBill(req);
    }
}
