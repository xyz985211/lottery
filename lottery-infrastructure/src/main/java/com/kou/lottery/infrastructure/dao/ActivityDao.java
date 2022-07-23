package com.kou.lottery.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kou.lottery.domain.activity.model.vo.AlterStateVO;
import com.kou.lottery.infrastructure.po.Activity;

import java.util.List;

/**
 * @author MiManchi
 * Date: 2022/5/25 16:04
 * Package: com.kou.lottery.infrastructrue.dao
 */
public interface ActivityDao extends BaseMapper<Activity> {

    /**
     * 变更活动状态
     *
     * @param alterStateVO  [activityId、beforeState、afterState]
     * @return 更新数量
     */
    int alterState(AlterStateVO alterStateVO);

    /**
     * 扣减活动库存
     * @param activityId 活动ID
     * @return 更新数量
     */
    int subtractionActivityStock(Long activityId);

    /**
     * 扫描待处理的活动列表，状态为：通过、活动中
     *
     * @param id ID
     * @return 待处理的活动集合
     */
    List<Activity> scanToDoActivityList(Long id);

    /**
     * 更新用户领取活动后，活动库存
     *
     * @param activity  入参
     */
    void updateActivityStock(Activity activity);

}
