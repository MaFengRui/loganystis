/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: NewUserOutputWritter
 * Author:   14751
 * Date:     2018/9/21 23:43
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间         版本号            描述
 */
package com.qf.analytis.transfrom.activevip;

import com.qf.analytis.common.CommonConstants;
import com.qf.analytis.common.KpiType;
import com.qf.analytis.model.dimension.StatsDimension;
import com.qf.analytis.model.dimension.StatsUserDimension;
import com.qf.analytis.model.kpi.OutputWritable;
import com.qf.analytis.model.value.StatsOutputValue;
import com.qf.analytis.transfrom.IOutputWritter;
import com.qf.analytis.transfrom.service.IDimension;
import com.qf.analytis.utils.PropertiesManagerUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.log4j.Logger;

import java.sql.PreparedStatement;

/**
 * 〈一句话功能简述〉<br> 
 * 活跃会员指标分析
 *
 * @author 14751
 * @create 2018/9/21 
 * @since 1.0.0
 */
public class BrowserActiveVipOutputWritter implements IOutputWritter {
    private static final Logger logger = Logger.getLogger(BrowserActiveVipOutputWritter.class);
    @Override
    //这里通过key和value给ps语句赋值
    public void output(Configuration conf, StatsDimension key, StatsOutputValue value, PreparedStatement ps, IDimension iDimension) {

        try {
            StatsUserDimension k = (StatsUserDimension) key;
            OutputWritable v = (OutputWritable) value;

            //获取活跃用户的值
            int ActiveUser = ((IntWritable)(v.getValue().get(new IntWritable(-1)))).get();

            int i = 0;
            ps.setInt(++i,iDimension.getDimensionByObject(k.getStatsCommon().getDate()));
            ps.setInt(++i,iDimension.getDimensionByObject(k.getStatsCommon().getPlatform()));
            //修改1
            if(v.getKpi().equals(KpiType.BROWSER_ACTIVE_USER)){
                ps.setInt(++i,iDimension.getDimensionByObject(k.getBrowser()));
            }
            ps.setInt(++i,ActiveUser);
            ps.setString(++i,conf.get(PropertiesManagerUtil.getPropertyValue(CommonConstants.RUNNING_DATE)));//注意这里需要在runner类里面进行赋值
            ps.setInt(++i,ActiveUser);

            ps.addBatch();//添加到批处理中，批量执行SQL语句
        } catch (Exception e) {
            logger.warn("给ps赋值失败！！！");
    }
    }
}