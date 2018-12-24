package com.qf.analytis.transfrom.newMember;

import com.qf.analytis.common.CommonConstants;
import com.qf.analytis.etl.util.TimeUtil;
import com.qf.analytis.model.dimension.StatsUserDimension;
import com.qf.analytis.model.kpi.OutputWritable;
import com.qf.analytis.model.map.TimeOutputValue;
import com.qf.analytis.transfrom.OutputToMySqlFormat;
import com.qf.analytis.utils.PropertiesManagerUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Created with IDEA
 * author 光明顶斗士
 * Date:18-12-21
 * Time:上午8:05
 * Vision:1.1
 * Description:活跃用户新增
 */
public class newVipRunner implements Tool {
    private static final Logger logger = Logger.getLogger(newVipRunner.class);
    private Configuration conf = new Configuration();
    //主函数－－入口
    public static void main(String[] args) {
        try {
            ToolRunner.run(new Configuration(),new newVipRunner(),args);
        } catch (Exception e) {
            logger.error("Active_USER TO MYSQL IS FAILED!!",e);
        }
    }
    @Override
    public int run(String[] strings) throws Exception {

        Configuration conf = this.conf;
        conf.set("fs.defaultFS","file:///");
        conf.set("mapreduce.framework.name","local");
        //为结果表中的created赋值，设置到conf中，需要我们传递参数－－一定要在job获取前设置参数
        this.setArgs(strings,conf);
//        String date = TimeUtil.parseLongToString(GlobalConstants.DEFAULT_FORMAT);//这么做不符合实际生产，因为数据不可能是当天立刻产生的
//        conf.set(GlobalConstants.RUNNING_DATE,date);
        Job job = Job.getInstance(conf, "New_member_to_mysql");
        job.setJarByClass(newVipRunner.class);
        //设置map参数
        job.setMapperClass(NewMemberMapper.class);
        job.setMapOutputKeyClass(StatsUserDimension.class);
        job.setMapOutputValueClass(TimeOutputValue.class);
        //设置reduce相关参数
        //设置reduce端的输出格式化类
        job.setReducerClass(newVipReducer.class);
        job.setOutputKeyClass(StatsUserDimension.class);
        job.setOutputValueClass(OutputWritable.class);
        job.setOutputFormatClass(OutputToMySqlFormat.class);
        //设置 reduce Task的数量
        job.setNumReduceTasks(1);
        //设置输入参数
        this.handleInputOutput(job);
        return job.waitForCompletion(true)? 0:1;
        //        if(job.waitForCompletion(true)){
//            this.computeNewTotalUser(job);//修改1
//            return 0;
//        }else{
//            return 1;
//        }

    }

    /**
     * 设置输入输出,_SUCCESS文件里面是空的，所以可以直接读取清洗后的数据存储目录
     * @param job
     */
    private void handleInputOutput(Job job) {
//        String[] fields = job.getConfiguration().get(GlobalConstants.RUNNING_DATE).split("-");
//        String month = fields[1];
//        String day = fields[2];

        try {
            FileSystem fs = FileSystem.get(job.getConfiguration());
            Path inpath = new Path(PropertiesManagerUtil.getPropertyValue(CommonConstants.TEST_INPUT_PATH));

            if(fs.exists(inpath)){
                FileInputFormat.addInputPath(job,inpath);
//                FileOutputFormat.setOutputPath(job,outpath);
            }else{
                throw new RuntimeException("输入路径不存在inpath:" + inpath.toString());
            }
        } catch (IOException e) {
            logger.warn("设置输入输出路径异常！！！",e);
        }
    }



    private void setArgs(String[] strings, Configuration conf) {
        String date = null;
        for (int i = 0; i < strings.length; i++) {
            if (strings[i].equals("-d")){

                if (i+1 < strings.length){
                    date = strings[i+1];
                    break;
                }
            }

        }
        System.out.println(date);
        if (date == null){
            date = TimeUtil.getYesterday();
        }
        conf.set(PropertiesManagerUtil.getPropertyValue(CommonConstants.RUNNING_DATE),date);
    }
    //修改1
    /**
     * 计算新增的总用户（重点）
     *
     * 1、获取运行当天的日期，然后再获取到运行当天前一天的日期，然后获取对应时间维度Id
     * 2、当对应时间维度Id都大于0，则正常计算：查询前一天的新增总用户，获取当天的新增用户
     * @param
     */
//    private void computeNewTotalUser(Job job) {
//        /**
//         *  1、根据运行当天获取日期
//         *  2、获取日期和前一天的对应的时间维度
//         *  3、根据时间维度获取对应的时间维度ID
//         *  4、根据前天的时间维度Id获取前天的新增总用户，根据当天的时间维度Id获取当天的新增用户
//         *  5、更新当天的新增总用户
//         *  6、同一维度前一天？？
//         */
//
//
//        String date = job.getConfiguration().get(GlobalConstants.RUNNING_DATE);
//        long nowday = TimeUtil.parseString2Long(date);
//        long yesterday = nowday - GlobalConstants.DAY_OF_MILLSECOND;
//
//        //获取时间维度
//        DateDimension nowDateDiemnsion = DateDimension.buildDate(nowday, DateEnum.DAY);
//        DateDimension yesterdayDateDiemnsion = DateDimension.buildDate(yesterday,DateEnum.DAY);
//
//        IDimension iDimension = new IDimensionImpl();
//        //获取时间维度Id
//        int nowDateDimensionId = -1;
//        int yesterdayDateDimensionId = -1;
//
//
//        Connection conn = null;
//        PreparedStatement ps = null;
//        ResultSet rs = null;
//        try {
//            nowDateDimensionId = iDimension.getDimensionIdByObject(nowDateDiemnsion);
//            yesterdayDateDimensionId = iDimension.getDimensionIdByObject(yesterdayDateDiemnsion);
//
//
//            conn = JdbcUtil.getConn();
//            Map<String,Integer> map = new HashMap<String,Integer>();
//            //开始判断维度Id是否正确
//            if(nowDateDimensionId > 0){
//                ps = conn.prepareStatement(conf.get("other_new_total_browser_user_now_sql"));
//                ps.setInt(1,nowDateDimensionId);
//                rs = ps.executeQuery();
//                while (rs.next()) {
//                    int platformId = rs.getInt("platform_dimension_id");
//                    int browserId = rs.getInt("browser_dimension_id");
//                    int newUsers = rs.getInt("new_install_users");
//                    map.put(platformId+"_"+browserId,newUsers);
//                }
//            }
//
//            //查询前一天的新增总用户
//            if(yesterdayDateDimensionId > 0){
//                ps = conn.prepareStatement(conf.get("other_new_total_browser_user_yesterday_sql"));
//                ps.setInt(1,nowDateDimensionId);
//                rs = ps.executeQuery();
//                while (rs.next()) {
//                    int platformId = rs.getInt("platform_dimension_id");
//                    int browserId = rs.getInt("browser_dimension_id");
//                    int newTotalUsers = rs.getInt("total_install_users");
//                    String key = platformId +"_"+browserId;
//                    if(map.containsKey(key)){
//                        newTotalUsers += map.get(key);
//                    }
//                    //存储
//                    map.put(key,newTotalUsers);
//                }
//            }
//
//            //更新
//            if(map.size() > 0){
//                for (Map.Entry<String,Integer> en:map.entrySet()){
//                    ps = conn.prepareStatement(conf.get("other_new_total_browser_user_update_sql"));
//
//                    //赋值
//                    String[] fields = en.getKey().split("_");
//                    ps.setInt(1,nowDateDimensionId);
//                    ps.setInt(2,Integer.parseInt(fields[0]));
//                    ps.setInt(3,Integer.parseInt(fields[1]));
//                    ps.setInt(4,en.getValue());
//                    ps.setString(5,conf.get(GlobalConstants.RUNNING_DATE));
//                    ps.setInt(6,en.getValue());
//                    //执行更新
//                    ps.execute();
//                }
//            }
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }finally {
//            JdbcUtil.close(conn,ps,rs);
//        }
//
//    }
    @Override
    public void setConf(Configuration conf) {
        conf.addResource("output_mapping.xml");
        conf.addResource("output_writter.xml");
        conf.addResource("other_mapping.xml");
//        conf.addResource("total_mapping.xml");//修改1
        this.conf = conf;
    }

    @Override
    public Configuration getConf() {
        return this.conf;
    }
}
