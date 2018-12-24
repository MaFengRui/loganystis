package com.qf.analytis.bean;

import lombok.Data;

/**
 * Created with IDEA
 * author 光明顶斗士
 * Date:18-12-18
 * Time:下午4:37
 * Vision:1.1
 * Description:用于封装浏览器信息
 */
@Data
public  class AgentInfo {
    private String browserName;
    private String browserVersion;
    private String osName;
    private String osVersion;
    public AgentInfo() {
    }

    public AgentInfo(String browserName, String browserVersion, String osName, String osVersion) {
        this.browserName = browserName;
        this.browserVersion = browserVersion;
        this.osName = osName;
        this.osVersion = osVersion;
    }

    @Override
    public String toString() {
        return "AgentInfo{" +
                "browserName='" + browserName + '\'' +
                ", browserVersion='" + browserVersion + '\'' +
                ", osName='" + osName + '\'' +
                ", osVersion='" + osVersion + '\'' +
                '}';
    }
}