package com.kou.lottery.domain.strategy.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author MiManchi
 * Date: 2022/6/4 15:13
 * Package: com.kou.lottery.domain.strategy.model.vo
 *
 * 奖品简要信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AwardBriefVO {

    /** 奖品ID */
    private String awardId;

    /** 奖品类型（1:文字描述、2:兑换码、3:优惠券、4:实物奖品） */
    private Integer awardType;

    /** 奖品名称 */
    private String awardName;

    /** 奖品内容「描述、奖品码、sku」 */
    private String awardContent;
}
