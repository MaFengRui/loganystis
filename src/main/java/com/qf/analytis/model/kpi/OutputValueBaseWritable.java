package com.qf.analytis.model.kpi;

import com.qf.analytis.common.KpiType;
import org.apache.hadoop.io.Writable;

/**
 * Created with IDEA
 * author 光明顶斗士
 * Date:18-12-20
 * Time:上午8:22
 * Vision:1.1
 * Description:map或者是reduce阶段输出value的类型的顶级父类
 */
public abstract class OutputValueBaseWritable implements Writable {
    /**
     * 获取kpi
     * @return
     */
    public abstract KpiType getKpi(); //获取一个kpi的抽象方法
}
