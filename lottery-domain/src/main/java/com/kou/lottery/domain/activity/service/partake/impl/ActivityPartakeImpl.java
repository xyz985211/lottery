package com.kou.lottery.domain.activity.service.partake.impl;

import com.kou.lottery.common.Constants;
import com.kou.lottery.common.Result;
import com.kou.lottery.domain.activity.model.req.PartakeReq;
import com.kou.lottery.domain.activity.model.res.StockResult;
import com.kou.lottery.domain.activity.model.vo.*;
import com.kou.lottery.domain.activity.repository.UserTakeActivityRepository;
import com.kou.lottery.domain.activity.service.partake.BaseActivityPartake;
import com.kou.middleware.db.router.strategy.IDBRouterStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author MiManchi
 * Date: 2022/6/7 20:39
 * Package: com.kou.lottery.domain.activity.service.partake.impl
 */
@Service
@Slf4j
public class ActivityPartakeImpl extends BaseActivityPartake {

    @Resource
    private UserTakeActivityRepository userTakeActivityRepository;
    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    private IDBRouterStrategy dbRouterStrategy;

    @Override
    protected UserTakeActivityVO queryNoConsumedTakeActivityOrder(Long activityId, String uId) {
        return userTakeActivityRepository.queryNoConsumedTakeActivityOrder(activityId, uId);
    }

    /**
     * 活动信息校验处理，把活动库存、状态、日期、个人参与次数
     *
     * @param partake 参与活动请求
     * @param bill    活动账单
     * @return 校验结果
     */
    @Override
    protected Result checkActivityBill(PartakeReq partake, ActivityBillVO bill) {
        // 校验：活动状态
        if (!Constants.ActivityState.DOING.getCode().equals(bill.getState())) {
            log.warn("活动当前状态非可用 state：{}", bill.getState());
            return Result.buildResult(Constants.ResponseCode.UN_ERROR, "活动当前状态非可用");
        }

        // 校验：活动日期
        if (bill.getBeginDateTime().after(partake.getPartakeDate()) || bill.getEndDateTime().before(partake.getPartakeDate())) {
            log.warn("活动时间范围非可用 beginDateTime：{} endDateTime：{}", bill.getBeginDateTime(), bill.getEndDateTime());
            return Result.buildResult(Constants.ResponseCode.UN_ERROR, "活动时间范围非可用");
        }

        // 校验：活动库存
        if (bill.getStockSurplusCount() <= 0) {
            log.warn("活动剩余库存非可用 stockSurplusCount：{}", bill.getStockSurplusCount());
            return Result.buildResult(Constants.ResponseCode.UN_ERROR, "活动剩余库存非可用");
        }

        // 校验：个人库存 - 个人活动剩余可领取次数
        if (null != bill.getUserTakeLeftCount() && bill.getUserTakeLeftCount() <= 0) {
            log.warn("个人领取次数非可用 userTakeLeftCount：{}", bill.getUserTakeLeftCount());
            return Result.buildResult(Constants.ResponseCode.UN_ERROR, "个人领取次数非可用");
        }

        return Result.buildSuccessResult();
    }

    /**
     * 扣减活动库存
     *
     * @param req 参与活动请求
     * @return 扣减结果
     */
    @Override
    protected Result subtractionActivityStock(PartakeReq req) {
        int count = super.activityRepository.subtractionActivityStock(req.getActivityId());
        if (0 == count) {
            log.error("扣减活动库存失败 activityId：{}", req.getActivityId());
            return Result.buildResult(Constants.ResponseCode.NO_UPDATE);
        }
        return Result.buildSuccessResult();
    }

    /**
     * 扣减活动库存，通过Redis
     *
     * @param uId        用户ID
     * @param activityId 活动号
     * @param stockCount 总库存
     * @return 扣减结果
     */
    @Override
    protected StockResult subtractionActivityStockByRedis(String uId, Long activityId, Integer stockCount) {
        return super.activityRepository.subtractionActivityStockByRedis(uId, activityId, stockCount);
    }

    /**
     * 恢复活动库存，通过Redis 【如果发现异常，则需要进行缓存库存恢复，只保证不超卖的特性，所以不保证一定能恢复占用库存，另外最终可以由任务进行补偿库存】
     *
     * @param activityId 活动ID
     * @param tokenKey   分布式 KEY 用于清理
     * @param code       状态
     */
    @Override
    protected void recoverActivityCacheStockByRedis(Long activityId, String tokenKey, String code) {
        super.activityRepository.recoverActivityCacheStockByRedis(activityId, tokenKey, code);
    }

    /**
     * 领取活动
     *
     * @param partake 参与活动请求
     * @param bill    活动账单
     * @return 领取结果
     */
    @Override
    protected Result grabActivity(PartakeReq partake, ActivityBillVO bill, Long takeId) {
        try {
            dbRouterStrategy.doRouter(partake.getuId());
            return transactionTemplate.execute(status -> {
                try {
                    // 扣减个人已参与次数
                    int updateCount = userTakeActivityRepository.subtractionLeftCount(bill.getActivityId(), bill.getActivityName(), bill.getTakeCount(), bill.getUserTakeLeftCount(), partake.getuId(), partake.getPartakeDate());
                    if (0 == updateCount) {
                        status.setRollbackOnly();
                        log.error("领取活动，扣减个人已参与次数失败 activityId：{} uId：{}", partake.getActivityId(), partake.getuId());
                        return Result.buildResult(Constants.ResponseCode.NO_UPDATE);
                    }

                    // 写入领取活动记录
                    userTakeActivityRepository.takeActivity(bill.getActivityId(), bill.getActivityName(), bill.getStrategyId(), bill.getTakeCount(), bill.getUserTakeLeftCount(), partake.getuId(), partake.getPartakeDate(), takeId);
                } catch (DuplicateKeyException e) {
                    status.setRollbackOnly();
                    log.error("领取活动，唯一索引冲突 activityId：{} uId：{}", partake.getActivityId(), partake.getuId(), e);
                    return Result.buildResult(Constants.ResponseCode.INDEX_DUP);
                }
                return Result.buildSuccessResult();
            });
        } finally {
            dbRouterStrategy.clear();
        }
    }

    /**
     * 保存奖品单
     * @param drawOrder 奖品单
     * @return          保存结果
     */
    @Override
    public Result recordDrawOrder(DrawOrderVO drawOrder) {
        try {
            dbRouterStrategy.doRouter(drawOrder.getuId());
            return transactionTemplate.execute(status -> {
                try {
                    int count = userTakeActivityRepository.lockTackActivity(drawOrder.getuId(), drawOrder.getActivityId(), drawOrder.getTakeId());
                    if (0 == count){
                        status.setRollbackOnly();
                        log.error("记录中奖单，个人参与活动抽奖已消耗完 activityId：{} uId：{}", drawOrder.getActivityId(), drawOrder.getuId());
                        return Result.buildResult(Constants.ResponseCode.NO_UPDATE);
                    }

                    // 保存抽奖信息
                    userTakeActivityRepository.saveUserStrategyExport(drawOrder);
                }catch (DuplicateKeyException e){
                    status.setRollbackOnly();
                    log.error("记录中奖单，唯一索引冲突 activityId：{} uId：{}", drawOrder.getActivityId(), drawOrder.getuId(), e);
                    return Result.buildResult(Constants.ResponseCode.INDEX_DUP);
                }
                return Result.buildSuccessResult();
            });
        } finally {
            dbRouterStrategy.clear();
        }
    }

    /**
     * 更新发货单MQ状态
     *  @param uId      用户ID
     * @param orderId   订单ID
     * @param mqState   MQ 发送状态
     */
    @Override
    public void updateInvoiceMqState(String uId, Long orderId, Integer mqState) {
        userTakeActivityRepository.updateInvoiceMqState(uId, orderId, mqState);
    }

    /**
     * 扫描发货单 MQ 状态，把未发送 MQ 的单子扫描出来，做补偿
     *
     * @param dbCount 指定分库
     * @param tbCount 指定分表
     * @return 发货单
     */
    @Override
    public List<InvoiceVO> scanInvoiceMqState(int dbCount, int tbCount) {
        try {
            // 设置路由
            dbRouterStrategy.setDBKey(dbCount);
            dbRouterStrategy.setTBKey(tbCount);

            // 查询数据
            return userTakeActivityRepository.scanInvoiceMqState();
        } finally {
            dbRouterStrategy.clear();
        }
    }

    /**
     * 更新活动库存
     *
     * @param activityPartakeRecordVO   活动领取记录
     */
    @Override
    public void updateActivityStock(ActivityPartakeRecordVO activityPartakeRecordVO) {
        userTakeActivityRepository.updateActivityStock(activityPartakeRecordVO);
    }
}
