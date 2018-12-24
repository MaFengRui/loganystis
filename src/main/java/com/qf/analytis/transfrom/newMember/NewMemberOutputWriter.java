package com.qf.analytis.transfrom.newMember;
import com.qf.analytis.common.CommonConstants;
import com.qf.analytis.common.KpiType;
import com.qf.analytis.model.dimension.StatsCommonDimension;
import com.qf.analytis.model.dimension.StatsDimension;
import com.qf.analytis.model.dimension.StatsUserDimension;
import com.qf.analytis.model.kpi.OutputWritable;
import com.qf.analytis.model.value.StatsOutputValue;
import com.qf.analytis.transfrom.IOutputWritter;
import com.qf.analytis.transfrom.service.IDimension;
import com.qf.analytis.utils.PropertiesManagerUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @description 用户模块和浏览器模块下的新增用户的输出到表中的实现类
 * @author:
 * @create: 2018-12-02 23:49:45
 **/
public class NewMemberOutputWriter implements IOutputWritter {

    @Override
    public void output(Configuration conf, StatsDimension key, StatsOutputValue statsOutputValue, PreparedStatement ps, IDimension iDimension) {
        try {
            //创建用于用户模块和浏览器模块map端和reduce端输出的key的对象
            StatsUserDimension k= (StatsUserDimension) key;
            OutputWritable value = (OutputWritable)statsOutputValue;
            //获取新增会员的值
            int newMember = ((IntWritable)(value.getValue().get(new IntWritable(-1)))).get();
            int i = 0 ;
            //获取到时间维度和平台维度的id
            ps.setInt(++i,iDimension.getDimensionByObject(k.getStatsCommon().getDate()));
            ps.setInt(++i,iDimension.getDimensionByObject(k.getStatsCommon().getPlatform()));
            if(value.getKpi().equals(KpiType.BROWSER_NEW_MEMBER)){
                ps.setInt(++i,iDimension.getDimensionByObject(k.getBrowser()));
            }
            //需要在runner类，在运行赋值的时候设置
            ps.setInt(++i,newMember);
            ps.setString(++i,conf.get(PropertiesManagerUtil.getPropertyValue(CommonConstants.RUNNING_DATE)));
            ps.setInt(++i,newMember);
            ps.addBatch();//添加到批处理中。
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
