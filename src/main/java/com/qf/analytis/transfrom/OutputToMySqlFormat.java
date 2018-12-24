package com.qf.analytis.transfrom;

import com.qf.analytis.common.KpiType;
import com.qf.analytis.model.dimension.StatsDimension;
import com.qf.analytis.model.kpi.OutputWritable;
import com.qf.analytis.transfrom.service.IDimension;
import com.qf.analytis.transfrom.service.impl.IDimenisonImpl;
import com.qf.analytis.utils.JdbcUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.db.DBOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputCommitter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName OutputToMySqlFormat
 * @Author lyd
 * @Date $ $
 * @Vesion 1.0
 * @Description 将结果输出到mysql的自定义类
 **/
public class OutputToMySqlFormat extends OutputFormat<StatsDimension, OutputWritable> {

    //DBOutputFormat

    /**
     *
     * 获取输出记录
     * @param taskAttemptContext
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    @Override
    public RecordWriter<StatsDimension, OutputWritable> getRecordWriter(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
       Connection conn = JdbcUtils.getConn();//获取数据库连接
       Configuration conf = taskAttemptContext.getConfiguration();//获取sql语句,获取处理不同sql语句的实现类
       IDimension iDimension = new IDimenisonImpl();//在数据库中获取ID的方法
        return new OutputToMysqlRecordWritter(conf,conn,iDimension);
    }

    @Override
    public void checkOutputSpecs(JobContext jobContext) throws IOException, InterruptedException {
        //检测输出空间
    }

    @Override
    public OutputCommitter getOutputCommitter(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        return new FileOutputCommitter(null,taskAttemptContext);
    }


    /**
     * 用于封装写出记录到mysql的信息
     */
    public static class OutputToMysqlRecordWritter extends RecordWriter<StatsDimension,OutputWritable>{
        Configuration conf = null; //用于获取sql语句
        Connection conn = null;//获取连接
        IDimension iDimension = null; //根据reduce端传过来key，获取维度对象，使用维度的属性，查询数据库，获得维度id
        //存储kpi与ps
        private Map<KpiType,PreparedStatement> map = new HashMap<KpiType,PreparedStatement>();
        //存储kpi与对应的输出sql进行批处理
        private Map<KpiType,Integer> batch = new HashMap<KpiType,Integer>();

        //给内部类对象赋值，调用write方法
        public OutputToMysqlRecordWritter(Configuration conf, Connection conn, IDimension iDimension) {
            this.conf = conf;
            this.conn = conn;
            this.iDimension = iDimension;
        }

        /**
         * 写
         * @param key
         * @param value
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        public void write(StatsDimension key, OutputWritable value) throws IOException, InterruptedException {
            //获取kpi
            KpiType kpi = value.getKpi();
            PreparedStatement ps = null;
            int count = 1;
            try {
                //获取ps，先检查缓存中是否存在，然后得到ps的对象后先给缓存
                if(map.containsKey(kpi)){
                    ps = map.get(kpi);
                } else {
                    //通过configuration获取ps语句
                    ps = conn.prepareStatement(conf.get(kpi.name));
                    String s = conf.get(kpi.name);
                    map.put(kpi,ps);  //将新增加的ps存储到map中
                }


                this.batch.put(kpi,count);
                count++;

                //为ps赋值准备
                String calssName = conf.get("writter_"+kpi.name);
                Class<?> classz = Class.forName(calssName); //将报名+类名转换成类
                //接口调用子类方法
                IOutputWritter writter = (IOutputWritter)classz.newInstance();
                //调用IOutputWritter中的output方法
                writter.output(conf,key,value,ps,iDimension);

                //对赋值好的ps进行执行t
                if(batch.size()%50 == 0 || batch.get(kpi)%50 == 0){  //有50个ps执行
                    ps.executeBatch();  //批量执行
                    this.conn.commit(); //提交批处理执行
                    batch.remove(kpi); //将执行完的ps移除掉
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        /**
         *
         * @param taskAttemptContext
         * @throws IOException
         * @throws InterruptedException
         */
        @Override
        public void close(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
            try {
                for (Map.Entry<KpiType,PreparedStatement> en:map.entrySet()){
                    en.getValue().executeBatch(); //将剩余的ps进行执行
//                    this.conn.commit();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                for (Map.Entry<KpiType,PreparedStatement> en:map.entrySet()){
                    JdbcUtils.close(conn,en.getValue(),null); //关闭所有能关闭的资源
                }
            }
        }
    }
}