package com.wpy.utils;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * 运算工具类
 *
 * @author 13940
 */
@Slf4j
public class MathUtils {

    /**
     * 除法
     * @param fenzi
     * @param fenmu
     * @param scale
     * @return
     */
    public static BigDecimal div(Integer fenzi,Integer fenmu,int scale){
        if(scale<0){
            scale=2;
        }
        BigDecimal b1 = new BigDecimal(fenzi);
        BigDecimal b2 = new BigDecimal(fenmu);
        return b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP);
    }
}
