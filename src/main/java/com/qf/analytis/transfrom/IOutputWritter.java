package com.qf.analytis.transfrom;

import com.qf.analytis.model.dimension.StatsDimension;
import com.qf.analytis.model.value.StatsOutputValue;
import com.qf.analytis.transfrom.service.IDimension;
import org.apache.hadoop.conf.Configuration;

import java.sql.PreparedStatement;

/**
 * 操作结果表的接口
 */
public interface IOutputWritter {

    /**
     * 为每一个kpi的最终结果赋值的接口
     * @param conf
        * @param key
        * @param value
        * @param ps
        * @param iDimension
        */
        void output(Configuration conf,
        StatsDimension key,
        StatsOutputValue value,
        PreparedStatement ps,
        IDimension iDimension);
        }
