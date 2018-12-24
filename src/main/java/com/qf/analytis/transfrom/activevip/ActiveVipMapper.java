package com.qf.analytis.transfrom.activevip;
import com.qf.analytis.common.DateEnum;
import com.qf.analytis.common.KpiType;
import com.qf.analytis.model.basedimension.BrowserDimension;
import com.qf.analytis.model.basedimension.DateDimension;
import com.qf.analytis.model.basedimension.KpiDimension;
import com.qf.analytis.model.basedimension.PlatformDimension;
import com.qf.analytis.model.dimension.StatsCommonDimension;
import com.qf.analytis.model.dimension.StatsUserDimension;
import com.qf.analytis.model.map.TimeOutputValue;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.log4j.Logger;

import java.io.IOException;


/**
 * Created with IDEA
 * author 光明顶斗士
 * Date:18-12-19
 * Time:下午10:34
 * Vision:1.1
 * Description:活跃会员的mapper
 */
public class ActiveVipMapper extends Mapper<LongWritable, Text, StatsUserDimension, TimeOutputValue>{
   private static  final Logger logger= Logger.getLogger(ActiveVipMapper.class);
   private  StatsUserDimension k = new StatsUserDimension();
   private  TimeOutputValue v = new TimeOutputValue();
   //定义你的kpi类型
   private KpiDimension active_member = new KpiDimension(KpiType.ACTIVE_MEMBER.name); //按照活跃会员的kpi去分析
   private KpiDimension newBrowserUserKpi = new KpiDimension(KpiType.BROWSER_ACTIVE_MEMBER.name); //按照浏览器活跃会员去分析

    public ActiveVipMapper() {
        super();
    }



    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        if (StringUtils.isEmpty(line)){
            return;
        }
        String[] fileds = line.split("\\u0001");
        String e_pv = fileds[2];
        String u_mid = fileds[4];
        if (StringUtils.isNotEmpty(u_mid) && StringUtils.isNotEmpty(e_pv)){
            //获取想要的字段
            String serverTime = fileds[1];
            String platform = fileds[13];
            String browserName = fileds[24];
            String browserVersion = fileds[25];
            if (StringUtils.isEmpty(serverTime)){
                logger.info("serverTime  is null"+serverTime);
                return;
            }
            Long time = Long.valueOf(serverTime);
            //构造输出的key
            PlatformDimension platformDimension = PlatformDimension.getInstance(platform);
            DateDimension dateDimension = DateDimension.buildDate(time, DateEnum.DAY);
            StatsCommonDimension statsCommon = this.k.getStatsCommon();
            statsCommon.setKpi(active_member);
            statsCommon.setDate(dateDimension); //时间维度
            statsCommon.setPlatform(platformDimension);//平台维度
            BrowserDimension browserDimension = new BrowserDimension("", "");//浏览器维度
            this.k.setStatsCommon(statsCommon);
            this.k.setBrowser(browserDimension);
            this.v.setId(u_mid); //value设置成会员id
            this.v.setTime(time);
            context.write(this.k,this.v);
            //浏览器新增下的值
            browserDimension = new BrowserDimension(browserName,browserVersion);
            statsCommon.setKpi(newBrowserUserKpi);
            this.k.setBrowser(browserDimension);
            this.k.setStatsCommon(statsCommon);
            context.write(this.k,this.v);
        }else {

            return;

        }


    }

}
