package com.kou.lottery.infrastructure.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author MiManchi
 * Date: 2022/5/24 15:16
 * Package: com.kou.lottery.infrastructrue.po
 *
 * 活动信息基础表
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Activity {

    /**
     * 自增ID
     */
    private Long id;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 活动描述
     */
    private String activityDesc;

    /**
     * 开始时间
     */
    private Date beginDateTime;

    /**
     * 结束时间
     */
    private Date endDateTime;

    /**
     * 库存
     */
    private Integer stockCount;

    /**
     * 库存剩余
     */
    private Integer stockSurplusCount;

    /**
     * 每人可参与次数
     */
    private Integer takeCount;

    /**
     * 策略ID
     */
    private Long strategyId;

    /**
     * 活动状态：1编辑、2提审、3撤审、4通过、5运行(审核通过后worker扫描状态)、6拒绝、7关闭、8开启
     */
    private Integer state;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;
}
