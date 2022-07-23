package com.kou.lottery.domain.support.ids.policy;

import com.kou.lottery.domain.support.ids.IdGenerator;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

/**
 * @author MiManchi
 * Date: 2022/6/5 20:18
 * Package: com.kou.lottery.domain.support.ids.policy
 *
 * 工具类生成 org.apache.commons.lang3.RandomStringUtils
 */
@Component
public class RandomNumeric implements IdGenerator {

    @Override
    public long nextId() {
        return Long.parseLong(RandomStringUtils.randomNumeric(11));
    }
}
