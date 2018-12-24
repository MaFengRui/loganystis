package com.qf.analytis.common;

/**
 * Created with IDEA
 * author 光明顶斗士
 * Date:18-12-19
 * Time:下午10:25
 * Vision:1.1
 * Description:动态获取kpi类型，就是统计指标，每一个类型我们都封装快好了
 */
public enum KpiType {
    NEW_INSTALL_USER("new_install_user"),
    BROWSER_NEW_INSTALL_USER("browser_new_install_user"),
    ACTIVE_USER("active_user"),
    BROWSER_ACTIVE_USER("browser_active_user"),
    ACTIVE_MEMBER("active_member"),
    BROWSER_ACTIVE_MEMBER("browser_active_member"),
    NEW_MEMBER("new_member"),
    BROWSER_NEW_MEMBER("browser_new_member"),
    MEMBER_INFO("member_info"),
    SESSION("session"),
    BROWSER_SESSION("browser_session"),
    HOURLY_ACTIVE_USER("hourly_active_user"),
    LOCAL("local");

    public final String name; //注意枚举不能使用静态
    private KpiType(String name){
        this.name = name;
    }
    public static KpiType ValueOfKpiName(String name){
        for (KpiType kpiType: values()){

            if (kpiType.name.equals(name)){
                return kpiType;
            }
        }
        return null;
    }


}
