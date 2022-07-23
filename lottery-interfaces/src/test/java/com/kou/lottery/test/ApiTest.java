package com.kou.lottery.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.SecureRandom;
import java.util.*;

/**
 * @author MiManchi
 * Date: 2022/5/25 20:01
 * Package: com.kou.lottery.test
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiTest {

    /**
     * 抽奖策略测试
     * <p>
     * https://www.jugong.wang/random-portal/my/qa
     * https://csrc.nist.gov/Projects/Random-Bit-Generation/Documentation-and-Software
     * Java 随机数生成器 Random & SecureRandom 原理分析 https://blog.csdn.net/hustspy1990/article/details/93364805
     * 使用 SecureRandom 产生随机数采坑记录 https://blog.csdn.net/weixin_41385912/article/details/103267277
     */
    @Test
    public void test_strategy() {
        SecureRandom random = new SecureRandom();
        int rate = random.nextInt(100);

        System.out.println("概率：" + rate);

        List<Map<String, String>> strategyList = new ArrayList<>();

        strategyList.add(new HashMap<String, String>() {{
            put("awardDesc", "一等奖：彩电");
            put("awardId", "10001");
            put("awardCount", "3");
            put("awardRate", "0.2");
        }});

        strategyList.add(new HashMap<String, String>() {{
            put("awardDesc", "二等奖：冰箱");
            put("awardId", "10002");
            put("awardCount", "5");
            put("awardRate", "0.3");
        }});

        strategyList.add(new HashMap<String, String>() {{
            put("awardDesc", "三等奖：洗衣机");
            put("awardId", "10003");
            put("awardCount", "10");
            put("awardRate", "0.5");
        }});


    }


    @Test
    public void test_idx() {

        Map<Integer, Integer> map = new HashMap<>();

        int HASH_INCREMENT = 0x61c88647;
        int hashCode = 0;
        for (int i = 1; i <= 100; i++) {
            hashCode = i * HASH_INCREMENT + HASH_INCREMENT;
            int idx = hashCode & (128 - 1);

            map.merge(idx, 1, Integer::sum);

            System.out.println("斐波那契散列：" + idx + " 普通散列：" + (String.valueOf(i).hashCode() & (128 - 1)));
        }

        System.out.println(map);
    }

    @Test
    public void test_id() {
        String random = RandomStringUtils.randomNumeric(9);
        System.out.println(random);
    }
}

