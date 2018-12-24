package com.qf.analytis.transfrom.activeuser;

import com.qf.analytis.common.KpiType;
import com.qf.analytis.model.dimension.StatsUserDimension;
import com.qf.analytis.model.kpi.OutputWritable;
import com.qf.analytis.model.map.TimeOutputValue;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IDEA
 * author 光明顶斗士
 * Date:18-12-20
 * Time:上午8:46
 * Vision:1.1
 * Description:新增用户的reucer，将,map的value进行去重
 */
public class ActiveUserReducer extends Reducer<StatsUserDimension, TimeOutputValue,StatsUserDimension, OutputWritable> {
    private static final Logger logger = Logger.getLogger(ActiveUserReducer.class);
    private OutputWritable v = new OutputWritable();
    //利用hashSet进行去重
    private Set unique = new HashSet();
    private MapWritable map = new MapWritable();
    public ActiveUserReducer() {

    }
    @Override
    protected void reduce(StatsUserDimension key, Iterable<TimeOutputValue> values, Context context) throws IOException, InterruptedException {
    map.clear();//清除map,因为map在外面定义，每一个key都需要调用一次reduce方法，也就是说上次的操作会ｂａｏｌｖ

        for (TimeOutputValue tv: values){
            this.unique.add(tv.getId()); //取出uuid进行赋值
        }
        //构造输出的value

        //根据kpi别名获取kpi类型（比较灵活）

        this.v.setKpi(KpiType.ValueOfKpiName(key.getStatsCommon().getKpi().getKpiName()));
        this.map.put(new IntWritable(-1),new IntWritable(this.unique.size()));
        System.out.println(unique.size());
        this.v.setValue(this.map);
        context.write(key,v);
        logger.info("当前kpi:"+key.getStatsCommon().getKpi().getKpiName()+"结果"+unique.size());
        /**
         * 注意点：
         * 如果只是输出到文件系统中，则不需要kpi，不需要声明集合map
         * value只需要uuid的个数，这就不要封装对象了
         */
        this.unique.clear();
    }

}
