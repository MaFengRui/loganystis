package com.qf.analytis.etl.util.logparseutil;

import com.qf.analytis.bean.AgentInfo;
import com.qf.analytis.bean.RegionInfo;
import com.qf.analytis.common.CommonConstants;
import com.qf.analytis.common.FiledName;
import com.qf.analytis.etl.util.agentparseutil.UserAgentUtil;
import com.qf.analytis.etl.util.ipparseutil.IpParseUtil;
import com.qf.analytis.utils.PropertiesManagerUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created with IDEA
 * author 光明顶斗士
 * Date:18-12-18
 * Time:下午9:00
 * Vision:1.1
 * Description:日志解析工具类
 */
public class LogParserUtil {

    private static Logger logger = Logger.getLogger(LogParserUtil.class);

    public static Map<String,String> parseLog(String LogText){
        //map用来保存所有的字段信息
        Map<String,String>filedMap = new ConcurrentHashMap<>();
        if(!LogText.isEmpty()){
            String[] splits_log = LogText.split("\\^A");
            if (splits_log.length == 4){
                filedMap.put(FiledName.LOG_IP,splits_log[0]);
                filedMap.put(FiledName.LOG_SERVER_TIME,splits_log[1].replaceAll("\\.",""));
                //处理参数列表
                String args = splits_log[3];
                //将最后一个字段进行解析。里面包含多个信息
                handleParam(args,filedMap);
                //处理ip
                handleIp(filedMap);
                //处理浏览器信息
                handleAgent(filedMap);
            }
        }

        return filedMap;
    }

    /**
     * 处理浏览器信息
     * @param map
     */
    private static void handleAgent(Map<String, String> map) {
        if(map.containsKey(FiledName.LOG_USERAGENT)){
            AgentInfo info = UserAgentUtil.parserUserAgent(map.get(FiledName.LOG_USERAGENT));
            map.put(FiledName.LOG_BROWSER_NAME,info.getBrowserName());
            map.put(FiledName.LOG_BROWSER_VERSION,info.getBrowserVersion());
            map.put(FiledName.LOG_OS_NAME,info.getOsName());
            map.put(FiledName.LOG_OS_VERSION,info.getOsVersion());
        }
    }

    /**
     * 处理IP
     * @param map
     */
    private static void handleIp(Map<String, String> map) {

        if(map.containsKey(FiledName.LOG_IP)){
            RegionInfo info = IpParseUtil.ipParser(map.get(FiledName.LOG_IP));
            map.put(FiledName.LOG_COUNTRY,info.getCountry());
            map.put(FiledName.LOG_PROVINCE,info.getProvince());
            map.put(FiledName.LOG_CITY,info.getCity());
        }
    }

    /**
     * 处理参数列表。将参数列表放入map集合中
     * @param params
     * @param map
     */
    private static void handleParam(String params, Map<String, String> map) {
        if (StringUtils.isNotEmpty(params)) {
            int index = params.indexOf("?");
            if (index > 0) {
                //先截取再拆分，拆分的结果是键值对(字符串)的数组
                String[] fields = params.substring(index + 1).split("&");
                for (String field : fields) {
                    String[] kvs = field.split("=");
                    String k = kvs[0];
                    String v = null;
                    try {
                        v = URLDecoder.decode(kvs[1], PropertiesManagerUtil.getPropertyValue(CommonConstants.ETL_UTIL_IPPARSEUTIL_IPPARSEUTIL_IPTAOBAOPARSER2_ENCODE));
                    } catch (UnsupportedEncodingException e) {
                        logger.error("url解码异常", e);
                    }
                    if (StringUtils.isNotEmpty(k)) {
                        map.put(k, v);
                    }
                }
            }
        }
    }



}
