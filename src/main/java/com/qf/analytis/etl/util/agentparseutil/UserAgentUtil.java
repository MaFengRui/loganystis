package com.qf.analytis.etl.util.agentparseutil;
import com.qf.analytis.bean.AgentInfo;
import cz.mallat.uasparser.OnlineUpdater;
import cz.mallat.uasparser.UASparser;
import cz.mallat.uasparser.UserAgentInfo;
import org.apache.log4j.Logger;
import java.io.IOException;

/**
 * 分析浏览器信息
 */
public class UserAgentUtil  {
    private static Logger logger = Logger.getLogger(UserAgentUtil.class);
    private static UASparser uaSparser = null;
    //静态代码块
    static {
        try {
            uaSparser = new UASparser(OnlineUpdater.getVendoredInputStream());
        } catch (IOException e) {
            logger.error("获取uaParser对象异常");
        }
    }
    public static AgentInfo parserUserAgent(String userAgent){
        AgentInfo info = new AgentInfo();
        try {
            UserAgentInfo userAgentInfo = uaSparser.parse(userAgent);
            info.setBrowserName(userAgentInfo.getUaFamily());
            info.setBrowserVersion(userAgentInfo.getBrowserVersionInfo());
            info.setOsName(userAgentInfo.getOsFamily());
            info.setOsVersion(userAgentInfo.getOsName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return info;
    }


}