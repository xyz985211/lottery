package com.kou.lottery.infrastructure.dao;

import com.kou.lottery.infrastructure.po.UserStrategyExport;
import com.kou.middleware.db.router.annotation.DBRouter;
import com.kou.middleware.db.router.annotation.DBRouterStrategy;

import java.util.List;

/**
 * @author MiManchi
 * Date: 2022/6/6 20:34
 * Package: com.kou.lottery.infrastructure.dao
 *
 * 用户策略计算结果表DAO
 */
@DBRouterStrategy(splitTable = true)    // 当为true时表示 分库分表
public interface UserStrategyExportDao {

    /**
     * 新增数据
     * @param userStrategyExport 用户策略
     */
    @DBRouter(key = "uId")
    void insert(UserStrategyExport userStrategyExport);

    /**
     * 查询数据
     * @param uId 用户ID
     * @return 用户策略
     */
    @DBRouter
    UserStrategyExport queryUserStrategyExportByUId(String uId);

    /**
     * 更新发奖状态
     * @param userStrategyExport 发奖信息
     */
    @DBRouter
    void updateUserAwardState(UserStrategyExport userStrategyExport);

    /**
     * 更新发送MQ状态
     * @param userStrategyExport 发送消息
     */
    @DBRouter
    void updateInvoiceMqState(UserStrategyExport userStrategyExport);

    /**
     * 扫描发货单 MQ 状态，把未发送 MQ 的单子扫描出来，做补偿
     *
     * @return 发货单
     */
    List<UserStrategyExport> scanInvoiceMqState();

}
