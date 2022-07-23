package com.kou.lottery.infrastructure.dao;

import com.kou.lottery.infrastructure.po.UserTakeActivityCount;
import com.kou.middleware.db.router.annotation.DBRouter;

/**
 * @author MiManchi
 * Date: 2022/6/7 16:59
 * Package: com.kou.lottery.infrastructure.dao
 *
 * 用户活动参与次数表Dao
 */
public interface UserTakeActivityCountDao {

    /**
     * 查询用户领取次数信息
     * @param userTakeActivityCountReq 请求入参【活动号、用户ID】
     * @return 领取结果
     */
    @DBRouter
    UserTakeActivityCount queryUserTakeActivityCount(UserTakeActivityCount userTakeActivityCountReq);

    /**
     * 插入领取次数信息
     * @param userTakeActivityCount 请求入参
     */
    void insert(UserTakeActivityCount userTakeActivityCount);

    /**
     * 更新领取次数信息
     * @param userTakeActivityCount 请求入参
     * @return 更新数量
     */
    int updateLeftCount(UserTakeActivityCount userTakeActivityCount);
}
