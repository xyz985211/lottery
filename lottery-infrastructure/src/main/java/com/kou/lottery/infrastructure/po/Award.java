package com.kou.lottery.infrastructure.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author MiManchi
 * Date: 2022/5/27 16:51
 * Package: com.kou.lottery.infrastructure.po
 *
 * 奖品信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Award {

    /** 自增ID */
    private Long id;

    /** 奖品ID */
    private String awardId;

    /** 奖品类型（1:文字描述、2:兑换码、3:优惠券、4:实物奖品） */
    private Integer awardType;

    /** 奖品名称 */
    private String awardName;

    /** 奖品内容「描述、奖品码、sku」 */
    private String awardContent;

    /** 创建时间 */
    private Date createTime;

    /** 修改时间 */
    private Date updateTime;

}
