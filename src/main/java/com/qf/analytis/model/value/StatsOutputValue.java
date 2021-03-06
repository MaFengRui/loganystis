package com.qf.analytis.model.value;

import com.qf.analytis.common.KpiType;
import org.apache.hadoop.io.Writable;

/**
 * 封装map或者reduce阶段输出的value类型的顶级父类
 */
public abstract class StatsOutputValue implements Writable {
    //获取kpi的抽象方法
    public abstract KpiType getKpi();
}
