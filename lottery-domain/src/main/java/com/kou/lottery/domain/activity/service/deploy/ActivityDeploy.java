package com.kou.lottery.domain.activity.service.deploy;

import com.kou.lottery.domain.activity.model.req.ActivityConfigReq;
import com.kou.lottery.domain.activity.model.vo.ActivityVO;

import java.util.List;

/**
 * @author MiManchi
 * Date: 2022/6/5 14:58
 * Package: com.kou.lottery.domain.activity.service.deploy
 *
 * 部署活动配置接口
 */
public interface ActivityDeploy {

    /**
     * 创建活动信息
     *
     * @param req 活动配置信息
     */
    void createActivity(ActivityConfigReq req);

    /**
     * 修改活动信息
     *
     * @param req 活动配置信息
     */
    void updateActivity(ActivityConfigReq req);

    /**
     * 扫描待处理的活动列表，状态为：通过、活动中
     * 通过 -> 时间符合时 -> 活动中
     * 活动中 -> 时间到期时 -> 关闭
     *
     * @param id ID
     * @return 待处理的活动集合
     */
    List<ActivityVO> scanToDoActivityList(Long id);
}
