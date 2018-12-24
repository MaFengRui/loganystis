package com.qf.analytis.common;

import javax.xml.soap.SAAJResult;
import java.security.SecureRandom;

/**
 * Created with IDEA
 * author 光明顶斗士
 * Date:18-12-18
 * Time:下午3:56
 * Vision:1.1
 * Description:共同的常亮
 */
public interface CommonConstants {

    //运行模式
    String JOB_RUN_MODE = "job.run.mode";
    //配置文件路径
    String COMMON_CONFIG_FILE_NAME ="conf.properties";
    //淘宝ip获取
    String TOBAO_REQUEST_IPPREX="tobao.request.ipprex";
    //默认IP bean的信息
    String DEFAULT_IP_INFO="default.ip.info";
    //解析ip地址编码
    String ETL_UTIL_IPPARSEUTIL_IPPARSEUTIL_IPTAOBAOPARSER2_ENCODE = "etl.util.ipparseutil.IpParseUtil.ipTaoBaoParser2.encode";
    //存储分隔符
    String AGENTINFO_SPLIT_SYMBOL="agentinfo.split.symbol";
    /**
     * 运行时间
     */
    String RUNNING_DATE="running.date";
    //测试输入路径
    String TEST_INPUT_PATH="test.input.path";
    //测试输出路径
    String TEST_OUTPUT_PATH="test.output.path";
    /**
     * 业务上面的字段
     */
    String ALL_OF_VALUE = "all.of.value";
    /**
     *jDBC Driver
     */
    String JDBC_DRIVER= "jdbc.driver";
    /**
     * 连接mysql的url
     */
    String MYSQL_URL="mysql.url";
    /**
     * 连接mysql的密码
     */
    String MYSQL_PWD="mysql.pwd";
    /**
     * 下面是啥？
     */
    long DAY_OF_MILLSECOND=Long.valueOf("day.of.millsecond");
    /**
     * MYSQL连接用户
     */
    String MYSQL_USER="mysql.user";

}
