package com.qf.analytis.bean;

import com.qf.analytis.common.CommonConstants;
import com.qf.analytis.utils.PropertiesManagerUtil;
import lombok.Data;

/**
 * Created with IDEA
 * author 光明顶斗士
 * Date:18-12-18
 * Time:下午4:34
 * Vision:1.1
 * Description：用来封装ip信息
 */
@Data
public  class RegionInfo{
    private String DEFAULT_VALUE = PropertiesManagerUtil.getPropertyValue(CommonConstants.DEFAULT_IP_INFO);
    private String country=DEFAULT_VALUE;
    private String province=DEFAULT_VALUE;
    private String city=DEFAULT_VALUE;

    public RegionInfo() {
    }
    @Override
    public String toString() {
        return "RegionInfo{" +
                "country='" + country + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                '}';
    }



}
